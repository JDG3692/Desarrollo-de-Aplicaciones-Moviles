package com.example.agendapersonal.util

import java.text.SimpleDateFormat
import java.util.Locale

// Formatos con los que se guardan la fecha y la hora de una actividad en Room.
//
// Siempre usan Locale.ROOT: con el idioma del dispositivo, algunos idiomas
// (árabe, persa, hindi…) escriben dígitos no latinos, por ejemplo "٢٠٢٦-١١-١٢".
// Si el usuario cambia de idioma, esas cadenas ya no coinciden con las nuevas:
// la actividad deja de aparecer en Día y Calendario, las consultas
// "WHERE fecha = ..." y por rango no la encuentran, y "ORDER BY fecha, hora"
// la ordena mal.
//
// Para mostrar fechas al usuario se deben seguir usando formatos con su idioma.
object FormatoAlmacenamiento {

    const val PATRON_FECHA = "yyyy-MM-dd"
    const val PATRON_HORA = "HH:mm"

    // SimpleDateFormat no es seguro entre hilos: se crea uno nuevo en cada uso.
    fun fecha(): SimpleDateFormat = SimpleDateFormat(PATRON_FECHA, Locale.ROOT)

    fun hora(): SimpleDateFormat = SimpleDateFormat(PATRON_HORA, Locale.ROOT)

    // Convierte la hora y los minutos elegidos en el selector al formato HH:mm.
    fun formatearHora(hora: Int, minuto: Int): String =
        String.format(Locale.ROOT, "%02d:%02d", hora, minuto)
}
