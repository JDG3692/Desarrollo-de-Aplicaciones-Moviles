package com.example.agendapersonal.alarm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

// Recibe la señal de Android cuando llega la hora de una actividad.
class ActividadAlarmaReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Obtiene los datos enviados al programar la alarma.
        val actividadId = intent.getIntExtra(EXTRA_ACTIVIDAD_ID, 0)
        val titulo = intent.getStringExtra(EXTRA_TITULO)
            ?: "Tienes una actividad pendiente"

        // Comprueba que Android permita mostrar notificaciones.
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // Crea el canal de notificaciones si todavía no existe.
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
                setSound(
                    android.provider.Settings.System.DEFAULT_NOTIFICATION_URI,
                    null
                )
            }

            notificationManager.createNotificationChannel(channel)
        }

        // Construye la notificación con el título de la actividad.
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
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
            .build()

        // Utiliza el ID de la actividad para identificar su notificación.
        notificationManager.notify(actividadId, notification)
    }

    companion object {
        const val EXTRA_ACTIVIDAD_ID = "actividad_id"
        const val EXTRA_TITULO = "actividad_titulo"
        const val CHANNEL_ID = "alarmas_actividades"
    }
}