
package com.example.agendapersonal.alarm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.agendapersonal.MainActivity

// Recibe la señal de Android cuando llega la hora de una actividad.
class ActividadAlarmaReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val actividadId = intent.getIntExtra(EXTRA_ACTIVIDAD_ID, 0)
        val titulo = intent.getStringExtra(EXTRA_TITULO)
            ?: "Tienes una actividad pendiente"

        if (actividadId <= 0) return

        val alarmaActiva = intent.getBooleanExtra(
            EXTRA_ALARMA_ACTIVA,
            false
        )

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarmas de actividades",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Recordatorios de Agenda Personal"
                setSound(Settings.System.DEFAULT_NOTIFICATION_URI, null)
            }

            notificationManager.createNotificationChannel(channel)
        }

        val intentCompletar = Intent(
            context,
            ActividadAlarmaAccionReceiver::class.java
        ).apply {
            action = ActividadAlarmaAccionReceiver.ACCION_COMPLETAR
            putExtra(EXTRA_ACTIVIDAD_ID, actividadId)
        }

        val pendingIntentCompletar = PendingIntent.getBroadcast(
            context,
            actividadId,
            intentCompletar,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val accionCompletar = NotificationCompat.Action.Builder(
            android.R.drawable.checkbox_on_background,
            "Completar",
            pendingIntentCompletar
        ).build()

        val intentReprogramar = Intent(
            context,
            MainActivity::class.java
        ).apply {
            putExtra("abrir_actividad_id", actividadId)
            putExtra("detener_alarma_personalizada", true)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntentReprogramar = PendingIntent.getActivity(
            context,
            actividadId + 100000,
            intentReprogramar,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val accionReprogramar = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_edit,
            "Reprogramar",
            pendingIntentReprogramar
        ).build()

        val intentNoCompletar = Intent(
            context,
            ActividadAlarmaAccionReceiver::class.java
        ).apply {
            action = ActividadAlarmaAccionReceiver.ACCION_NO_COMPLETAR
            putExtra(EXTRA_ACTIVIDAD_ID, actividadId)
        }

        val pendingIntentNoCompletar = PendingIntent.getBroadcast(
            context,
            actividadId + 200000,
            intentNoCompletar,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val accionNoCompletar = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_close_clear_cancel,
            "No completar",
            pendingIntentNoCompletar
        ).build()

        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Es hora de tu actividad")
            .setContentText(titulo)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setOngoing(true)
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
            .addAction(accionCompletar)
            .addAction(accionReprogramar)
            .addAction(accionNoCompletar)
            .build()

        notificationManager.notify(actividadId, notification)


        if (alarmaActiva) {
            val intentSonido = Intent(
                context,
                ActividadAlarmaSonidoService::class.java
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(
                    context,
                    intentSonido
                )
            } else {
                context.startService(intentSonido)
            }
        }
    }


    companion object {
        const val EXTRA_ACTIVIDAD_ID = "actividad_id"
        const val EXTRA_TITULO = "actividad_titulo"
        const val CHANNEL_ID = "alarmas_actividades"
        const val EXTRA_ALARMA_ACTIVA = "alarma_activa"
    }
}
