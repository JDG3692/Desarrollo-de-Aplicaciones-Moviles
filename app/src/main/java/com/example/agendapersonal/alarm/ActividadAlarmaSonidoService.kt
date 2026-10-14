
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
import android.util.Log
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Handler
import android.os.Looper


class ActividadAlarmaSonidoService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var audioManager: AudioManager
    private var solicitudFocoAudio: AudioFocusRequest? = null

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


        mediaPlayer = MediaPlayer()

        try {
            val atributosAudio = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            mediaPlayer?.setAudioAttributes(atributosAudio)

            resources.openRawResourceFd(
                R.raw.agenda_alarma_elegante
            ).use { archivo ->
                mediaPlayer?.setDataSource(
                    archivo.fileDescriptor,
                    archivo.startOffset,
                    archivo.length
                )
            }

            mediaPlayer?.prepare()
        } catch (e: Exception) {
            Log.e(TAG, "No se pudo preparar el MP3", e)
            detenerServicio()
            return START_NOT_STICKY
        }


        if (mediaPlayer == null) {
            Log.e(TAG, "No se pudo cargar el archivo MP3")
            detenerServicio()
            return START_NOT_STICKY
        }

        Log.i(
            TAG,
            "MP3 cargado correctamente. Duración: ${mediaPlayer?.duration} ms"
        )

        mediaPlayer?.setOnCompletionListener {
            detenerServicio()
        }

        mediaPlayer?.setOnErrorListener { _, _, _ ->
            detenerServicio()
            true
        }

        try {
            if (!solicitarFocoAudio()) {
                Log.e(TAG, "No se pudo obtener el foco de audio")
                detenerServicio()
                return START_NOT_STICKY
            }

            mediaPlayer?.start()
            Log.i(TAG, "Reproducción del MP3 iniciada con foco de audio")

        } catch (e: Exception) {
            Log.e(TAG, "Error al iniciar la reproducción del MP3", e)
            detenerServicio()
            return START_NOT_STICKY
        }

        return START_NOT_STICKY
    }

    private fun solicitarFocoAudio(): Boolean {
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val atributosAudio = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            solicitudFocoAudio = AudioFocusRequest.Builder(
                AudioManager.AUDIOFOCUS_GAIN
            )
                .setAudioAttributes(atributosAudio)
                .setAcceptsDelayedFocusGain(false)
                .setOnAudioFocusChangeListener { cambio ->
                    Log.i(TAG, "Cambio de foco de audio: $cambio")
                }
                .build()

            val resultado = audioManager.requestAudioFocus(
                solicitudFocoAudio!!
            )

            Log.i(TAG, "Resultado de solicitud de foco: $resultado")

            return resultado == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }

        @Suppress("DEPRECATION")
        val resultado = audioManager.requestAudioFocus(
            null,
            AudioManager.STREAM_ALARM,
            AudioManager.AUDIOFOCUS_GAIN
        )

        return resultado == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
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
        const val TAG = "AgendaAlarmaSonido"
    }
}
