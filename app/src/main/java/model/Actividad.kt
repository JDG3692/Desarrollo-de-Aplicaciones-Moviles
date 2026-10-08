package com.example.agendapersonal.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Representa una actividad de la agenda y define la información
// que se almacena de cada actividad en la base de datos Room.
@Entity(tableName = "actividades")
data class Actividad(
    // Identificador único generado automáticamente por Room.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Título o descripción de la actividad.
    val titulo: String,

    // Fecha de la actividad en formato yyyy-MM-dd.
    val fecha: String,

    // Hora de la actividad en formato HH:mm.
    val hora: String,

    // Indica si la actividad ya fue completada.
    val completada: Boolean = false,

    // Indica si la alarma de esta actividad está activa.
    val alarmaActiva: Boolean = true
)