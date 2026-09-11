package com.example.agendapersonal.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {

    @Query("SELECT * FROM actividades ORDER BY fecha, hora")
    fun obtenerActividades(): Flow<List<Actividad>>

    @Insert
    suspend fun insertarActividad(actividad: Actividad)

    @Update
    suspend fun actualizarActividad(actividad: Actividad)

    @Delete
    suspend fun eliminarActividad(actividad: Actividad)
}