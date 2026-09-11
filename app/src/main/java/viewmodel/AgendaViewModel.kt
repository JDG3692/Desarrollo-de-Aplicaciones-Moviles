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

class AgendaViewModel(application: Application) : AndroidViewModel(application) {

    private val actividadDao = AgendaDatabase
        .obtenerBaseDeDatos(application)
        .actividadDao()

    val actividades: StateFlow<List<Actividad>> =
        actividadDao.obtenerActividades()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun agregarActividad(actividad: Actividad) {
        viewModelScope.launch {
            actividadDao.insertarActividad(actividad)
        }
    }

    fun completarActividad(id: Int) {
        viewModelScope.launch {
            val actividad = actividades.value.find { it.id == id }

            if (actividad != null) {
                actividadDao.actualizarActividad(
                    actividad.copy(completada = true)
                )
            }
        }
    }
}