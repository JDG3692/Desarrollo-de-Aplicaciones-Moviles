package com.example.agendapersonal

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.agendapersonal.ui.navigation.AppNavigation
import com.example.agendapersonal.ui.theme.AgendaPersonalTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.agendapersonal.alarm.ActividadAlarmaReceiver
import com.example.agendapersonal.alarm.ActividadAlarmaSonidoService

class MainActivity : ComponentActivity() {

    private var actividadIdPendiente by mutableStateOf<Int?>(null)

    private val solicitudNotificaciones =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            solicitarPermisoAlarmasExactas()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Al recrearse la Activity (rotación, cambio de tema, etc.) Android
        // entrega de nuevo el intent original con sus extras. Solo se procesa
        // la primera vez para no reabrir la edición ni detener otra alarma.
        // También se ignora si la app se abrió desde la lista de recientes.
        val esIntentNuevo = savedInstanceState == null &&
                (intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0

        if (esIntentNuevo) {
            procesarIntentDeNotificacion(intent)
        }

        solicitarPermisos()

        setContent {
            AgendaPersonalTheme {
                AppNavigation(
                    actividadIdParaEditar = actividadIdPendiente,
                    onActividadAbierta = {
                        // Permite que una nueva notificación de la misma
                        // actividad vuelva a abrir la edición.
                        actividadIdPendiente = null
                    }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        procesarIntentDeNotificacion(intent)
    }

    // Atiende los extras que envía la notificación de una actividad:
    // detiene el sonido personalizado y abre la edición. Los extras se
    // eliminan después de usarlos para no procesarlos dos veces.
    private fun procesarIntentDeNotificacion(intent: Intent) {
        detenerAlarmaPersonalizada(intent)

        val actividadId = intent
            .getIntExtra(ActividadAlarmaReceiver.EXTRA_ABRIR_ACTIVIDAD_ID, 0)
            .takeIf { it > 0 }

        intent.removeExtra(ActividadAlarmaReceiver.EXTRA_ABRIR_ACTIVIDAD_ID)

        if (actividadId != null) {
            actividadIdPendiente = actividadId
        }
    }

    private fun solicitarPermisos() {
        // Solicita permiso para mostrar notificaciones en Android 13 o superior.
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            solicitudNotificaciones.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            solicitarPermisoAlarmasExactas()
        }
    }

    private fun detenerAlarmaPersonalizada(intent: Intent?) {
        if (intent?.getBooleanExtra(ActividadAlarmaReceiver.EXTRA_DETENER_ALARMA, false) == true) {
            val intentServicio = Intent(
                this,
                ActividadAlarmaSonidoService::class.java
            ).apply {
                action = ActividadAlarmaSonidoService.ACCION_DETENER
            }

            startService(intentServicio)
            intent.removeExtra(ActividadAlarmaReceiver.EXTRA_DETENER_ALARMA)
        }
    }

    private fun solicitarPermisoAlarmasExactas() {
        // El acceso a alarmas exactas se concede desde los ajustes del sistema.
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        ) {
            val alarmManager =
                getSystemService(ALARM_SERVICE) as AlarmManager

            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(
                    Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                    Uri.parse("package:$packageName")
                )

                startActivity(intent)
            }
        }
    }
}