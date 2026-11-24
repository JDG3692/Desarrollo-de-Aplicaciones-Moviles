package com.example.agendapersonal.model

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ActividadDaoTest {

    private lateinit var db: AgendaDatabase
    private lateinit var dao: ActividadDao

    @Before
    fun crear() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            AgendaDatabase::class.java
        ).build()
        dao = db.actividadDao()
    }

    @After
    fun cerrar() = db.close()

    private suspend fun insertar(
        titulo: String,
        fecha: String,
        hora: String = "10:00",
        completada: Boolean = false
    ): Actividad {
        val actividad = Actividad(
            titulo = titulo, fecha = fecha, hora = hora, completada = completada
        )
        return actividad.copy(id = dao.insertarActividad(actividad).toInt())
    }

    private suspend fun titulos() =
        dao.obtenerTodasLasActividades().map { it.titulo }.sorted()

    @Test
    fun insertaYConsultaPorId() = runBlocking {
        val actividad = insertar("A", "2026-11-12")

        assertEquals(actividad, dao.obtenerActividadPorId(actividad.id))
        assertEquals(null, dao.obtenerActividadPorId(actividad.id + 1))
    }

    @Test
    fun actualizaSoloLaActividadIndicada() = runBlocking {
        val a = insertar("A", "2026-11-12")
        val b = insertar("B", "2026-11-12")
        val cambiada = a.copy(titulo = "A2", hora = "11:30", completada = true)

        dao.actualizarActividad(cambiada)

        assertEquals(cambiada, dao.obtenerActividadPorId(a.id))
        assertEquals(b, dao.obtenerActividadPorId(b.id))
    }

    @Test
    fun eliminaSoloLaActividadIndicada() = runBlocking {
        val a = insertar("A", "2026-11-12")
        insertar("B", "2026-11-12")

        dao.eliminarActividad(a)

        assertEquals(listOf("B"), titulos())
    }

    @Test
    fun ordenaPorFechaYDespuesPorHora() = runBlocking {
        insertar("3", "2026-11-13", "08:00")
        insertar("2", "2026-11-12", "20:00")
        insertar("1", "2026-11-12", "09:00")

        assertEquals(
            listOf("1", "2", "3"),
            dao.obtenerActividades().first().map { it.titulo }
        )
    }

    @Test
    fun eliminaPorDia() = runBlocking {
        insertar("antes", "2026-11-11")
        insertar("dia", "2026-11-12")
        insertar("despues", "2026-11-13")

        assertEquals(
            listOf("dia"),
            dao.obtenerActividadesDelDia("2026-11-12").map { it.titulo }
        )
        assertEquals(1, dao.eliminarActividadesDelDia("2026-11-12"))
        assertEquals(listOf("antes", "despues"), titulos())
    }

    @Test
    fun elRangoIncluyeElInicioYExcluyeElFinal() = runBlocking {
        insertar("antes", "2026-10-31")
        insertar("inicio", "2026-11-01")
        insertar("ultimo", "2026-11-30")
        insertar("final", "2026-12-01")

        assertEquals(
            listOf("inicio", "ultimo"),
            dao.obtenerActividadesPorRango("2026-11-01", "2026-12-01")
                .map { it.titulo }
        )
        assertEquals(
            2, dao.eliminarActividadesPorRango("2026-11-01", "2026-12-01")
        )
        assertEquals(listOf("antes", "final"), titulos())
    }

    @Test
    fun eliminaSoloLasCompletadas() = runBlocking {
        insertar("pendiente", "2026-11-12")
        insertar("hecha", "2026-11-12", completada = true)

        assertEquals(
            listOf("hecha"),
            dao.obtenerActividadesCompletadas().map { it.titulo }
        )
        assertEquals(1, dao.eliminarActividadesCompletadas())
        assertEquals(listOf("pendiente"), titulos())
    }
}
