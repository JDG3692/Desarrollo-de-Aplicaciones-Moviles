package com.example.agendapersonal.viewmodel

import androidx.lifecycle.ViewModel
import com.example.agendapersonal.model.Actividad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AgendaViewModel : ViewModel() {

    private val _actividades = MutableStateFlow<List<Actividad>>(emptyList())

    val actividades: StateFlow<List<Actividad>> = _actividades.asStateFlow()

    fun agregarActividad(actividad: Actividad) {
        _actividades.value = _actividades.value + actividad
    }

    fun completarActividad(id: Int) {
        _actividades.value = _actividades.value.map { actividad ->
            if (actividad.id == id) {
                actividad.copy(completada = true)
            } else {
                actividad
            }
        }
    }
}