package com.example.agendapersonal.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Define las operaciones que permiten consultar y modificar
// las actividades almacenadas en la base de datos Room.
@Dao
interface ActividadDao {

    // Obtiene todas las actividades ordenadas primero por fecha
    // y después por hora. El Flow permite observar cambios automáticamente.
    @Query("SELECT * FROM actividades ORDER BY fecha, hora")
    fun obtenerActividades(): Flow<List<Actividad>>

    // Inserta una nueva actividad en la base de datos.
    @Insert
    suspend fun insertarActividad(actividad: Actividad)

    // Actualiza los datos de una actividad existente.
    @Update
    suspend fun actualizarActividad(actividad: Actividad)

    // Elimina una actividad de la base de datos.
    @Delete
    suspend fun eliminarActividad(actividad: Actividad)
}