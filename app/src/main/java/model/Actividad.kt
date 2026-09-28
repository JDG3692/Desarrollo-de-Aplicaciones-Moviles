package com.example.agendapersonal.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actividades")
data class Actividad(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val fecha: String,
    val hora: String,
    val completada: Boolean = false,
    // Indica si la alarma de esta actividad está activa.
    val alarmaActiva: Boolean = true
)