package com.example.agendapersonal.model

data class Actividad(
    val id: Int,
    val titulo: String,
    val fecha: String,
    val hora: String,
    var completada: Boolean = false
)