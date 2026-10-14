
package com.example.agendapersonal.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.agendapersonal.model.AgendaDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActividadAlarmaAccionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val actividadId = intent.getIntExtra(
            ActividadAlarmaReceiver.EXTRA_ACTIVIDAD_ID,
            0
        )

        if (actividadId <= 0) return

        when (intent.action) {
            ACCION_COMPLETAR -> {
                completarActividad(context, actividadId)
            }

            ACCION_REPROGRAMAR -> {
                // Lo conectaremos después.
            }

            ACCION_NO_COMPLETAR -> {
                noCompletarActividad(context, actividadId)
            }
        }
    }
    private fun detenerSonido(context: Context) {
        val intent = Intent(
            context,
            ActividadAlarmaSonidoService::class.java
        ).apply {
            action = ActividadAlarmaSonidoService.ACCION_DETENER
        }

        context.startService(intent)
    }

    private fun completarActividad(
        context: Context,
        actividadId: Int
    ) {
        detenerSonido(context)
        val resultado = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dao = AgendaDatabase
                    .obtenerBaseDeDatos(context)
                    .actividadDao()

                val actividad = dao.obtenerActividadPorId(actividadId)

                if (actividad != null && !actividad.completada) {
                    dao.actualizarActividad(
                        actividad.copy(completada = true)
                    )

                    ActividadAlarmaScheduler(context)
                        .cancelar(actividadId)
                }

                val notificationManager =
                    context.getSystemService(
                        Context.NOTIFICATION_SERVICE
                    ) as NotificationManager

                notificationManager.cancel(actividadId)
            } finally {
                resultado.finish()
            }
        }
    }

    private fun noCompletarActividad(
        context: Context,
        actividadId: Int
    ) {
        detenerSonido(context)
        val resultado = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dao = AgendaDatabase
                    .obtenerBaseDeDatos(context)
                    .actividadDao()

                val actividad = dao.obtenerActividadPorId(actividadId)

                if (actividad != null && !actividad.completada) {
                    dao.actualizarActividad(
                        actividad.copy(
                            noCompletada = true,
                            alarmaActiva = false
                        )
                    )

                    ActividadAlarmaScheduler(context)
                        .cancelar(actividadId)
                }

                val notificationManager =
                    context.getSystemService(
                        Context.NOTIFICATION_SERVICE
                    ) as NotificationManager

                notificationManager.cancel(actividadId)
            } finally {
                resultado.finish()
            }
        }
    }

    companion object {
        const val ACCION_COMPLETAR =
            "com.example.agendapersonal.ACCION_COMPLETAR"

        const val ACCION_REPROGRAMAR =
            "com.example.agendapersonal.ACCION_REPROGRAMAR"

        const val ACCION_NO_COMPLETAR =
            "com.example.agendapersonal.ACCION_NO_COMPLETAR"
    }
}
