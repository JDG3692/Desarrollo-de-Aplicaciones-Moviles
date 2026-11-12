package com.example.agendapersonal.util

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.Locale

class FormatoAlmacenamientoTest {

    private lateinit var localeOriginal: Locale

    // Idioma con dígitos no latinos: con Locale.getDefault() la fecha
    // se escribiría como "٢٠٢٦-١١-١٢" en lugar de "2026-11-12".
    private val localeDigitosArabes =
        Locale.forLanguageTag("ar-EG-u-nu-arab")

    @Before
    fun guardarLocale() {
        localeOriginal = Locale.getDefault()
        Locale.setDefault(localeDigitosArabes)
    }

    @After
    fun restaurarLocale() {
        Locale.setDefault(localeOriginal)
    }

    private fun fecha(anio: Int, mes: Int, dia: Int, hora: Int = 0, minuto: Int = 0) =
        Calendar.getInstance().apply {
            clear()
            set(anio, mes - 1, dia, hora, minuto)
        }.time

    @Test
    fun fechaSeGuardaConDigitosLatinosAunqueElIdiomaSeaOtro() {
        val texto = FormatoAlmacenamiento.fecha().format(fecha(2026, 11, 12))

        assertEquals("2026-11-12", texto)
    }

    @Test
    fun horaSeGuardaConDigitosLatinosAunqueElIdiomaSeaOtro() {
        val texto = FormatoAlmacenamiento.hora().format(fecha(2026, 11, 12, 7, 5))

        assertEquals("07:05", texto)
    }

    @Test
    fun formatearHoraRellenaConCerosYUsaDigitosLatinos() {
        assertEquals("07:05", FormatoAlmacenamiento.formatearHora(7, 5))
        assertEquals("23:59", FormatoAlmacenamiento.formatearHora(23, 59))
    }

    @Test
    fun fechaGuardadaSePuedeVolverALeer() {
        val leida = FormatoAlmacenamiento.fecha().parse("2026-11-12")!!

        assertEquals("2026-11-12", FormatoAlmacenamiento.fecha().format(leida))
    }
}
