
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

// ViewModel principal de la agenda.
// Conecta la interfaz con la base de datos y administra
// las operaciones que se realizan sobre las actividades.
class AgendaViewModel(application: Application) : AndroidViewModel(application) {

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
        // Ejecuta la operación de forma asíncrona.
        viewModelScope.launch {
            actividadDao.insertarActividad(actividad)
        }
    }

    // Marca como completada una actividad identificada por su ID.
    fun completarActividad(id: Int) {
        // Ejecuta la búsqueda y actualización en una corrutina.
        viewModelScope.launch {
            // Busca la actividad dentro de la lista disponible actualmente.
            val actividad = actividades.value.find { it.id == id }

            // Actualiza la actividad únicamente si fue encontrada.
            if (actividad != null) {
                // Conserva los demás datos y cambia el estado de completada.
                actividadDao.actualizarActividad(
                    actividad.copy(completada = true)
                )
            }
        }
    }

    // Activa o silencia la alarma de una actividad.
    fun cambiarEstadoAlarma(id: Int) {
        // Ejecuta la operación sin bloquear la interfaz.
        viewModelScope.launch {
            // Busca la actividad por su identificador.
            val actividad = actividades.value.find { it.id == id }

            // Continúa únicamente si la actividad existe en la lista actual.
            if (actividad != null) {
                // Invierte el estado de la alarma y guarda el cambio.
                actividadDao.actualizarActividad(
                    actividad.copy(
                        alarmaActiva = !actividad.alarmaActiva
                    )
                )
            }
        }
    }

    // Guarda los cambios realizados en una actividad existente.
    fun actualizarActividad(actividad: Actividad) {
        // Ejecuta la actualización en una corrutina.
        viewModelScope.launch {
            actividadDao.actualizarActividad(actividad)
        }
    }

    // Elimina una actividad de la base de datos.
    fun eliminarActividad(actividad: Actividad) {
        // Ejecuta la eliminación en una corrutina.
        viewModelScope.launch {
            actividadDao.eliminarActividad(actividad)
        }
    }

    // Elimina todas las actividades que ya están completadas.
    fun eliminarActividadesCompletadas() {
        viewModelScope.launch {
            actividadDao.eliminarActividadesCompletadas()
        }
    }

    // Elimina todas las actividades de un día específico.
    fun eliminarActividadesDelDia(fecha: String) {
        viewModelScope.launch {
            actividadDao.eliminarActividadesDelDia(fecha)
        }
    }

    // Elimina actividades dentro de un rango de fechas.
    // La fecha inicial se incluye y la fecha final se excluye.
    fun eliminarActividadesPorRango(
        fechaInicio: String,
        fechaFin: String
    ) {
        viewModelScope.launch {
            actividadDao.eliminarActividadesPorRango(fechaInicio, fechaFin)
        }
    }

}
