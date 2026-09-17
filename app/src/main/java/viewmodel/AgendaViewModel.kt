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
// Se encarga de proporcionar las actividades a la interfaz
// y ejecutar las operaciones sobre la base de datos.
class AgendaViewModel(application: Application) : AndroidViewModel(application) {

    // Obtiene el DAO de actividades a través de la base de datos.
    // El DAO permite consultar, insertar y actualizar actividades.
    private val actividadDao = AgendaDatabase
        .obtenerBaseDeDatos(application)
        .actividadDao()

    // Expone la lista de actividades como un StateFlow.
    // La interfaz puede observar este estado y actualizarse automáticamente
    // cuando cambian los datos almacenados en Room.
    val actividades: StateFlow<List<Actividad>> =
        actividadDao.obtenerActividades()
            .stateIn(
                // Utiliza el ciclo de vida del ViewModel para las corrutinas.
                scope = viewModelScope,
                // Mantiene la observación mientras existan suscriptores.
                // Espera 5 segundos antes de detenerla cuando ya no hay suscriptores.
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    // Agrega una nueva actividad a la base de datos.
    fun agregarActividad(actividad: Actividad) {
        // Ejecuta la operación de base de datos en una corrutina.
        viewModelScope.launch {
            actividadDao.insertarActividad(actividad)
        }
    }
    // Marca una actividad como completada utilizando su identificador.
    fun completarActividad(id: Int) {
        // Ejecuta la actualización de la actividad en una corrutina.
        viewModelScope.launch {
            // Busca la actividad correspondiente dentro de la lista actual.
            val actividad = actividades.value.find { it.id == id }

            // Solo actualiza la actividad si fue encontrada.
            if (actividad != null) {
                // Crea una copia de la actividad cambiando su estado a completada
                // y la actualiza en la base de datos.
                actividadDao.actualizarActividad(
                    actividad.copy(completada = true)
                )
            }
        }
    }
}