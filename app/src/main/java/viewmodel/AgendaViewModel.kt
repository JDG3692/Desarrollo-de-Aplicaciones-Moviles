
package com.example.agendapersonal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.agendapersonal.model.Actividad
import com.example.agendapersonal.model.AgendaDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.agendapersonal.alarm.ActividadAlarmaScheduler

// ViewModel principal de la agenda.
// Conecta la interfaz con la base de datos y administra
// las operaciones que se realizan sobre las actividades.
class AgendaViewModel(application: Application) : AndroidViewModel(application) {

    private val alarmaScheduler =
        ActividadAlarmaScheduler(application.applicationContext)

    // Obtiene el DAO desde la instancia de la base de datos.
    // El DAO contiene las operaciones para consultar y modificar actividades.
    private val actividadDao = AgendaDatabase
        .obtenerBaseDeDatos(application)
        .actividadDao()

    // Expone las actividades almacenadas como un StateFlow.
    // La interfaz observa este estado y recibe las actualizaciones de Room.
    val actividades: StateFlow<List<Actividad>> =
        actividadDao.obtenerActividades()
            .stateIn(
                // Utiliza el alcance de corrutinas del ciclo de vida del ViewModel.
                scope = viewModelScope,

                // Mantiene la observación activa mientras existan suscriptores.
                // Espera cinco segundos antes de detenerla si dejan de observar.
                started = SharingStarted.WhileSubscribed(5000),

                // Valor inicial mientras se reciben los datos de la base de datos.
                initialValue = emptyList()
            )

    // Registra una actividad nueva en la base de datos.
    fun agregarActividad(actividad: Actividad) {
        viewModelScope.launch {
            val nuevoId = actividadDao.insertarActividad(actividad).toInt()

            alarmaScheduler.programar(
                actividad.copy(id = nuevoId)
            )
        }
    }

    // Marca como completada una actividad identificada por su ID.
    fun completarActividad(id: Int) {
        viewModelScope.launch {
            // Busca la actividad dentro de la lista disponible.
            val actividad = actividades.value.find { it.id == id }

            // Actualiza la actividad únicamente si fue encontrada.
            if (actividad != null) {
                actividadDao.actualizarActividad(
                    actividad.copy(completada = true)
                )

                // Cancela la alarma de la actividad completada.
                alarmaScheduler.cancelar(id)
            }
        }
    }

    // Activa o silencia la alarma de una actividad.
    fun cambiarEstadoAlarma(id: Int) {
        viewModelScope.launch {
            // Busca la actividad por su identificador.
            val actividad = actividades.value.find { it.id == id }

            // Continúa únicamente si la actividad existe.
            if (actividad != null) {
                // Invierte el estado de la alarma.
                val actividadActualizada = actividad.copy(
                    alarmaActiva = !actividad.alarmaActiva
                )

                // Guarda el cambio en la base de datos.
                actividadDao.actualizarActividad(actividadActualizada)

                // Mantiene programado el recordatorio aunque la alarma
                // adicional esté desactivada.
                alarmaScheduler.programar(actividadActualizada)
            }
        }
    }

    // Guarda los cambios realizados en una actividad existente.
    // Guarda los cambios y conserva el recordatorio
    // aunque el sonido personalizado esté desactivado.
    fun actualizarActividad(actividad: Actividad) {
        viewModelScope.launch {
            actividadDao.actualizarActividad(actividad)

            if (!actividad.completada) {
                alarmaScheduler.programar(actividad)
            } else {
                alarmaScheduler.cancelar(actividad.id)
            }
        }
    }

    // Elimina una actividad de la base de datos.
    fun eliminarActividad(actividad: Actividad) {
        // Ejecuta la eliminación en una corrutina.
        viewModelScope.launch {
            // Cancela la alarma asociada a la actividad.
            alarmaScheduler.cancelar(actividad.id)

            // Elimina la actividad de la base de datos.
            actividadDao.eliminarActividad(actividad)
        }
    }

    // Elimina todas las actividades que ya están completadas.

    // Elimina todas las actividades completadas.
    fun eliminarActividadesCompletadas() {
        viewModelScope.launch {
            // Consulta directamente Room para obtener todos los registros.
            val actividadesCompletadas =
                actividadDao.obtenerActividadesCompletadas()

            // Cancela cada alarma antes de eliminar los registros.
            actividadesCompletadas.forEach { actividad ->
                alarmaScheduler.cancelar(actividad.id)
            }

            // Elimina las actividades completadas de Room.
            actividadDao.eliminarActividadesCompletadas()
        }
    }

    // Elimina todas las actividades de un día específico.
    fun eliminarActividadesDelDia(fecha: String) {
        viewModelScope.launch {
            // Consulta directamente Room para obtener los registros del día.
            val actividadesDelDia =
                actividadDao.obtenerActividadesDelDia(fecha)

            // Cancela todas las alarmas asociadas.
            actividadesDelDia.forEach { actividad ->
                alarmaScheduler.cancelar(actividad.id)
            }

            // Elimina las actividades del día de Room.
            actividadDao.eliminarActividadesDelDia(fecha)
        }
    }

    // Elimina actividades dentro de un rango de fechas.
    // La fecha inicial se incluye y la fecha final se excluye.

    // Elimina las actividades incluidas en un rango de fechas.
    fun eliminarActividadesPorRango(
        fechaInicio: String,
        fechaFin: String
    ) {
        viewModelScope.launch {
            // Consulta directamente Room para obtener los registros del rango.
            val actividadesDelRango =
                actividadDao.obtenerActividadesPorRango(
                    fechaInicio,
                    fechaFin
                )

            // Cancela todas las alarmas de los registros afectados.
            actividadesDelRango.forEach { actividad ->
                alarmaScheduler.cancelar(actividad.id)
            }

            // Elimina los registros del rango de Room.
            actividadDao.eliminarActividadesPorRango(
                fechaInicio,
                fechaFin
            )
        }
    }


}
