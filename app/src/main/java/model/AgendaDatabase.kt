package com.example.agendapersonal.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Actividad::class],
    version = 2,
    exportSchema = false
)
abstract class AgendaDatabase : RoomDatabase() {

    abstract fun actividadDao(): ActividadDao

    companion object {
        @Volatile
        private var INSTANCE: AgendaDatabase? = null

        // Agrega la columna alarmaActiva a las actividades existentes.
        private val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(
                database: androidx.sqlite.db.SupportSQLiteDatabase
            ) {
                database.execSQL(
                    "ALTER TABLE actividades ADD COLUMN alarmaActiva INTEGER NOT NULL DEFAULT 1"
                )
            }
        }

        fun obtenerBaseDeDatos(context: Context): AgendaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AgendaDatabase::class.java,
                    "agenda_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instancia
                instancia
            }
        }
    }
}