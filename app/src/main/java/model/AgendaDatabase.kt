package com.example.agendapersonal.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Define la base de datos local de la aplicación utilizando Room.
// Contiene la entidad Actividad y proporciona acceso a su DAO.
@Database(
    entities = [Actividad::class],
    version = 3,
    exportSchema = false
)
abstract class AgendaDatabase : RoomDatabase() {

    // Proporciona el DAO utilizado para consultar y modificar actividades.
    abstract fun actividadDao(): ActividadDao

    companion object {
        // Mantiene una única instancia de la base de datos durante
        // la ejecución de la aplicación.
        @Volatile
        private var INSTANCE: AgendaDatabase? = null

        // Agrega la columna alarmaActiva a las actividades existentes
        // cuando la base de datos pasa de la versión 1 a la versión 2.
        private val MIGRATION_1_2 =
            object : androidx.room.migration.Migration(1, 2) {

                override fun migrate(
                    database: androidx.sqlite.db.SupportSQLiteDatabase
                ) {
                    // Agrega la nueva columna con valor predeterminado activo.
                    database.execSQL(
                        "ALTER TABLE actividades ADD COLUMN alarmaActiva INTEGER NOT NULL DEFAULT 1"
                    )
                }
            }
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE actividades ADD COLUMN noCompletada INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        // Obtiene la instancia de la base de datos y la crea
        // únicamente cuando todavía no existe.
        fun obtenerBaseDeDatos(context: Context): AgendaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AgendaDatabase::class.java,
                    "agenda_database"
                )
                    // Conserva los datos existentes al actualizar
                    // la estructura de la base de datos.
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()

                INSTANCE = instancia
                instancia
            }
        }
    }
}