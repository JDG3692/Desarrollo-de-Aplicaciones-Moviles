package com.example.agendapersonal.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.agendapersonal.model.Actividad
import java.text.SimpleDateFormat
import java.util.Locale

// Programa y cancela las alarmas del sistema para cada actividad.
class ActividadAlarmaScheduler(
    private val context: Context
) {

    // Obtiene el servicio de alarmas de Android.
    private val alarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Programa una alarma usando la fecha y hora de la actividad.
    fun programar(actividad: Actividad): Boolean {
        if (!actividad.alarmaActiva || actividad.completada) {
            cancelar(actividad.id)
            return true
        }

        // Convierte fecha y hora al instante correspondiente del dispositivo.
        val formato = SimpleDateFormat(
            "yyyy-MM-dd HH:mm",
            Locale.ROOT
        ).apply {
            isLenient = false
        }

        val fechaHora = try {
            formato.parse("${actividad.fecha} ${actividad.hora}")
        } catch (e: Exception) {
            null
        } ?: return false

        val instante = fechaHora.time

        // No programa alarmas para fechas y horas que ya pasaron.
        if (instante <= System.currentTimeMillis()) {
            cancelar(actividad.id)
            return false
        }

        // Comprueba el permiso especial para alarmas exactas.
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) {
            return false
        }

        val pendingIntent = crearPendingIntent(
            actividad.id,
            actividad.titulo
        )

        return try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                instante,
                pendingIntent
            )
            true
        } catch (e: SecurityException) {
            false
        }
    }

    // Cancela la alarma asociada a una actividad.
    fun cancelar(actividadId: Int) {
        val pendingIntent = crearPendingIntent(
            actividadId,
            null
        )

        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    // Genera un PendingIntent identificable por el ID de la actividad.
    private fun crearPendingIntent(
        actividadId: Int,
        titulo: String?
    ): PendingIntent {
        val intent = Intent(
            context,
            ActividadAlarmaReceiver::class.java
        ).apply {
            putExtra(
                ActividadAlarmaReceiver.EXTRA_ACTIVIDAD_ID,
                actividadId
            )

            if (titulo != null) {
                putExtra(
                    ActividadAlarmaReceiver.EXTRA_TITULO,
                    titulo
                )
            }
        }

        return PendingIntent.getBroadcast(
            context,
            actividadId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
    }
}