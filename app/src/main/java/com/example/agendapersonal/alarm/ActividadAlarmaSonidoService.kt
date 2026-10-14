
package com.example.agendapersonal.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.agendapersonal.R

class ActividadAlarmaSonidoService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        crearCanalServicio()

        val notificacion = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Agenda Personal")
            .setContentText("Reproduciendo el sonido de tu actividad")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setSilent(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notificacion,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            startForeground(NOTIFICATION_ID, notificacion)
        }

        // Evita iniciar el mismo sonido dos veces dentro del servicio.
        if (mediaPlayer != null) {
            return START_NOT_STICKY
        }

        mediaPlayer = MediaPlayer.create(
            this,
            R.raw.agenda_alarma_elegante
        )

        if (mediaPlayer == null) {
            detenerServicio()
            return START_NOT_STICKY
        }

        mediaPlayer?.setOnCompletionListener {
            detenerServicio()
        }

        mediaPlayer?.setOnErrorListener { _, _, _ ->
            detenerServicio()
            true
        }

        mediaPlayer?.start()

        return START_NOT_STICKY
    }

    private fun crearCanalServicio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CHANNEL_ID,
                "Reproducción de alarma",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Indica que se reproduce el sonido personalizado"
                setSound(null, null)
                enableVibration(false)
            }

            val manager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            manager.createNotificationChannel(canal)
        }
    }

    private fun detenerServicio() {
        mediaPlayer?.release()
        mediaPlayer = null

        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val CHANNEL_ID = "reproduccion_alarma_personalizada"
        const val NOTIFICATION_ID = 7301
    }
}
