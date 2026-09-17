package com.example.agendapersonal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agendapersonal.viewmodel.AgendaViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarioScreen(
    agendaViewModel: AgendaViewModel = viewModel(),
    onVolver: () -> Unit = {}
) {

    // Guarda el mes que actualmente está mostrando el calendario.
    var mesConsultado by remember {
        mutableStateOf(
            Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
            }
        )
    }

    // Guarda la fecha que el usuario tiene seleccionada.
    var fechaSeleccionada by remember {
        mutableStateOf(Calendar.getInstance())
    }

    // Obtiene las actividades almacenadas en Room.
    val actividades by agendaViewModel.actividades.collectAsState()

    // Obtiene las fechas que tienen al menos una actividad.
    val fechasConActividades = actividades
        .map { it.fecha }
        .toSet()

    // Color principal utilizado para los elementos destacados de la aplicación.
    val azulPrincipal = Color(0xFF3F6FC4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Espacio superior que separa el contenido de la parte superior de la pantalla.
        Spacer(
            modifier = Modifier.height(40.dp)
        )

        // Encabezado con el botón para regresar y el título de la pantalla.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Botón que regresa a la pantalla principal.
            IconButton(
                onClick = {
                    onVolver()
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar"
                )
            }

            // Título de la pantalla.
            Text(
                text = "Calendario",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // Selector de mes con los botones para navegar entre meses.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFFD9E2F0),
                    shape = RoundedCornerShape(10.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Botón para mostrar el mes anterior.
            IconButton(
                onClick = {
                    mesConsultado =
                        (mesConsultado.clone() as Calendar).apply {
                            add(Calendar.MONTH, -1)
                        }
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Mes anterior",
                    modifier = Modifier.size(18.dp)
                )
            }

            // Muestra el mes y año actualmente seleccionados.
            Text(
                text = SimpleDateFormat(
                    "MMMM yyyy",
                    Locale("es", "ES")
                ).format(mesConsultado.time)
                    .replaceFirstChar { it.uppercase() },
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            // Botón para mostrar el mes siguiente.
            IconButton(
                onClick = {
                    mesConsultado =
                        (mesConsultado.clone() as Calendar).apply {
                            add(Calendar.MONTH, 1)
                        }
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Mes siguiente",
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // Muestra los nombres de los días de la semana.
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            listOf(
                "Lun",
                "Mar",
                "Mié",
                "Jue",
                "Vie",
                "Sáb",
                "Dom"
            ).forEach { dia ->

                // Nombre de cada día de la semana.
                Text(
                    text = dia,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        // Calcula la cantidad de días y la posición inicial del mes.
        val calendario = mesConsultado.clone() as Calendar

        val cantidadDias =
            calendario.getActualMaximum(Calendar.DAY_OF_MONTH)

        val primerDiaSemana =
            calendario.get(Calendar.DAY_OF_WEEK)

        // Convierte el primer día para que lunes sea la primera columna.
        val desplazamiento = when (primerDiaSemana) {
            Calendar.MONDAY -> 0
            Calendar.TUESDAY -> 1
            Calendar.WEDNESDAY -> 2
            Calendar.THURSDAY -> 3
            Calendar.FRIDAY -> 4
            Calendar.SATURDAY -> 5
            Calendar.SUNDAY -> 6
            else -> 0
        }

        // Crea la lista de días incluyendo los espacios iniciales.
        val dias = List(desplazamiento) { 0 } +
                (1..cantidadDias).toList()

        // Completa la última semana para mantener siete columnas.
        val diasCompletos =
            dias + List((7 - dias.size % 7) % 7) { 0 }

        // Organiza las semanas del calendario.
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            // Divide los días en grupos de siete para formar cada semana.
            diasCompletos.chunked(7).forEach { semana ->

                // Representa una fila del calendario.
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    semana.forEach { dia ->

                        // Contenedor individual de cada día.
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clickable(enabled = dia != 0) {

                                    // Guarda la fecha seleccionada por el usuario.
                                    fechaSeleccionada =
                                        (mesConsultado.clone() as Calendar).apply {
                                            set(Calendar.DAY_OF_MONTH, dia)
                                        }
                                },
                            contentAlignment = Alignment.Center
                        ) {

                            // Los ceros representan espacios vacíos.
                            if (dia != 0) {

                                // Construye la fecha completa del día mostrado.
                                val fechaDia =
                                    (mesConsultado.clone() as Calendar).apply {
                                        set(Calendar.DAY_OF_MONTH, dia)
                                    }

                                // Convierte la fecha al formato utilizado por Room.
                                val fechaFormateada = SimpleDateFormat(
                                    "yyyy-MM-dd",
                                    Locale.getDefault()
                                ).format(fechaDia.time)

                                // Comprueba si el día tiene actividades.
                                val tieneActividades =
                                    fechaFormateada in fechasConActividades

                                // Comprueba si el día está seleccionado.
                                val esSeleccionado =
                                    fechaSeleccionada.get(Calendar.YEAR) ==
                                            mesConsultado.get(Calendar.YEAR) &&
                                            fechaSeleccionada.get(Calendar.MONTH) ==
                                            mesConsultado.get(Calendar.MONTH) &&
                                            fechaSeleccionada.get(Calendar.DAY_OF_MONTH) == dia

                                // Organiza el número y el punto de actividad.
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {

                                    // Círculo que identifica visualmente el día seleccionado.
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(
                                                color = if (esSeleccionado) {
                                                    azulPrincipal
                                                } else {
                                                    Color.Transparent
                                                },
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        // Número correspondiente al día.
                                        Text(
                                            text = dia.toString(),
                                            fontSize = 11.sp,
                                            fontWeight = if (esSeleccionado) {
                                                FontWeight.Bold
                                            } else {
                                                FontWeight.Normal
                                            },
                                            color = if (esSeleccionado) {
                                                Color.White
                                            } else {
                                                Color.Unspecified
                                            },
                                            textAlign = TextAlign.Center
                                        )
                                    }

                                    // Punto que indica que el día tiene actividades.
                                    if (tieneActividades) {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = 1.dp)
                                                .size(4.dp)
                                                .background(
                                                    color = azulPrincipal,
                                                    shape = CircleShape
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // Título de la sección de actividades.
        Text(
            text = "Actividades del día",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        // Formatea la fecha seleccionada para mostrarla al usuario.
        val fechaSeleccionadaTexto = SimpleDateFormat(
            "EEEE d 'de' MMMM 'de' yyyy",
            Locale("es", "ES")
        ).format(fechaSeleccionada.time)
            .replaceFirstChar { it.uppercase() }

        // Muestra la fecha seleccionada debajo del título.
        Text(
            text = fechaSeleccionadaTexto,
            fontSize = 11.sp,
            color = Color(0xFF777777)
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // Convierte la fecha seleccionada al formato utilizado por Room.
        val fechaSeleccionadaFormateada = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(fechaSeleccionada.time)

        // Obtiene solamente las actividades del día seleccionado.
        val actividadesDelDia = actividades
            .filter { actividad ->
                actividad.fecha == fechaSeleccionadaFormateada
            }
            .sortedBy { it.hora }

        // Lista vertical de actividades del día.
        // Lista de actividades correspondientes al día seleccionado.
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            // Comprueba si el día seleccionado no tiene actividades.
            if (actividadesDelDia.isEmpty()) {

                // Mensaje mostrado cuando no existen actividades para ese día.
                Text(
                    text = "No hay actividades para este día",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color(0xFF777777)
                )

            } else {

                // Recorre y muestra las actividades del día seleccionado.
                actividadesDelDia.forEach { actividad ->

                    // Tarjeta visual de una actividad.
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = Color(0xFFD9E2F0)
                        )
                    ) {

                        // Contenedor horizontal de la información de la actividad.
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // Línea azul lateral que identifica la actividad.
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .height(52.dp)
                                    .background(
                                        color = azulPrincipal,
                                        shape = RoundedCornerShape(
                                            topStart = 10.dp,
                                            bottomStart = 10.dp
                                        )
                                    )
                            )

                            // Muestra la hora de la actividad.
                            Text(
                                text = actividad.hora,
                                modifier = Modifier
                                    .width(58.dp)
                                    .padding(start = 10.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )

                            // Muestra el título de la actividad.
                            Text(
                                text = actividad.titulo,
                                modifier = Modifier.weight(1f),
                                fontSize = 12.sp,
                                color = Color(0xFF555555)
                            )

                            // Icono que representa el sonido de la actividad.
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Sonido",
                                tint = azulPrincipal,
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(18.dp)
                            )

                            // Icono que representa la notificación de la actividad.
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificación",
                                tint = azulPrincipal,
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}