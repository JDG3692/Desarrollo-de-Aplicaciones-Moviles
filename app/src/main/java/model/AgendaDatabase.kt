package com.example.agendapersonal.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Actividad::class],
    version = 1,
    exportSchema = false
)
abstract class AgendaDatabase : RoomDatabase() {

    abstract fun actividadDao(): ActividadDao

    companion object {
        @Volatile
        private var INSTANCE: AgendaDatabase? = null

        fun obtenerBaseDeDatos(context: Context): AgendaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AgendaDatabase::class.java,
                    "agenda_database"
                ).build()

                INSTANCE = instancia
                instancia
            }
        }
    }
}