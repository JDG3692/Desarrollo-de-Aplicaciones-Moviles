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

class MainActivity : ComponentActivity() {

    private val solicitudNotificaciones =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            solicitarPermisoAlarmasExactas()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        solicitarPermisos()

        setContent {
            AgendaPersonalTheme {
                AppNavigation()
            }
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