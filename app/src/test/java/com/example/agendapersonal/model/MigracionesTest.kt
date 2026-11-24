package com.example.agendapersonal.model

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.agendapersonal.reiniciarBaseDeDatos
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

// Las versiones 1 y 2 no exportaron su esquema, así que se recrean a mano.
// Al abrir la base con la configuración real de la app, Room migra y además
// comprueba que las columnas coincidan con la entidad.
@RunWith(RobolectricTestRunner::class)
class MigracionesTest {

    private val contexto = ApplicationProvider.getApplicationContext<Context>()

    @After
    fun limpiar() = reiniciarBaseDeDatos()

    private fun crearBaseAntigua(version: Int, columnasExtra: String, valoresExtra: String) {
        contexto.openOrCreateDatabase("agenda_database", Context.MODE_PRIVATE, null).use {
            it.execSQL(
                "CREATE TABLE actividades (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "titulo TEXT NOT NULL, fecha TEXT NOT NULL, " +
                        "hora TEXT NOT NULL, completada INTEGER NOT NULL" +
                        "$columnasExtra)"
            )
            it.execSQL(
                "INSERT INTO actividades VALUES " +
                        "(7, 'Cita', '2026-11-12', '07:05', 1$valoresExtra)"
            )
            it.version = version
        }
    }

    private fun leer() = runBlocking {
        AgendaDatabase.obtenerBaseDeDatos(contexto)
            .actividadDao()
            .obtenerTodasLasActividades()
    }

    private val esperada = Actividad(
        id = 7, titulo = "Cita", fecha = "2026-11-12", hora = "07:05",
        completada = true, alarmaActiva = true, noCompletada = false
    )

    @Test
    fun deVersion1ConservaLosDatosYUsaLosValoresPredeterminados() {
        crearBaseAntigua(1, "", "")

        assertEquals(listOf(esperada), leer())
    }

    @Test
    fun deVersion2ConservaLaAlarmaDesactivada() {
        crearBaseAntigua(2, ", alarmaActiva INTEGER NOT NULL DEFAULT 1", ", 0")

        assertEquals(listOf(esperada.copy(alarmaActiva = false)), leer())
    }
}
