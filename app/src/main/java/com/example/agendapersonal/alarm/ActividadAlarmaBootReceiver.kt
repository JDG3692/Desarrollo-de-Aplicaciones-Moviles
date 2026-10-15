
package com.example.agendapersonal.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.agendapersonal.model.AgendaDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

// Recibe los eventos de inicio de Android y los cambios de hora
// para restaurar las alarmas de las actividades pendientes.
class ActividadAlarmaBootReceiver : BroadcastReceiver() {

    // Se ejecuta cuando Android envía uno de los eventos registrados.
    override fun onReceive(context: Context, intent: Intent) {

        // Atiende únicamente los eventos que requieren restaurar alarmas.
        when (intent.action) {

            // Android terminó de iniciar el dispositivo.
            Intent.ACTION_BOOT_COMPLETED,

                // El usuario modificó la fecha o la hora del sistema.
            Intent.ACTION_TIME_CHANGED,

                // Cambió la zona horaria del dispositivo.
            Intent.ACTION_TIMEZONE_CHANGED -> {

                // Permite continuar el trabajo asíncrono después de onReceive().
                val pendingResult = goAsync()

                // Utiliza el contexto de la aplicación, que no depende de una pantalla.
                val appContext = context.applicationContext

                // Crea un ámbito de corrutinas para consultar Room sin bloquear
                // el hilo principal de Android.
                val scope = CoroutineScope(
                    SupervisorJob() + Dispatchers.IO
                )

                // Ejecuta la recuperación de alarmas en segundo plano.
                scope.launch {
                    try {
                        // Obtiene el DAO que permite consultar las actividades guardadas.
                        val dao = AgendaDatabase
                            .obtenerBaseDeDatos(appContext)
                            .actividadDao()

                        // Reutiliza el programador de alarmas existente.
                        val scheduler =
                            ActividadAlarmaScheduler(appContext)

                        // Recupera todas las actividades almacenadas en Room.
                        val actividades =
                            dao.obtenerTodasLasActividades()

                        // Omite las actividades que ya fueron completadas.
                        actividades
                            .filterNot { it.completada }
                            .forEach { actividad ->

                                // Vuelve a programar la alarma de cada actividad pendiente.
                                // El scheduler valida la fecha y los permisos necesarios.
                                scheduler.programar(actividad)
                            }
                    } finally {
                        // Indica a Android que el trabajo del receptor ha terminado.
                        pendingResult.finish()

                        // Libera los recursos del ámbito de corrutinas.
                        scope.cancel()
                    }
                }
            }
        }
    }
}
