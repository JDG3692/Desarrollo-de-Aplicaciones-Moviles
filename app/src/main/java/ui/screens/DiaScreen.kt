package com.example.agendapersonal.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agendapersonal.model.Actividad
import com.example.agendapersonal.viewmodel.AgendaViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.material3.Checkbox
import android.app.TimePickerDialog
import androidx.compose.material3.OutlinedButton
import java.util.Calendar
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.IconButton
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.ui.res.painterResource
import com.example.agendapersonal.R
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.ui.text.style.TextAlign


// Pantalla principal de la agenda diaria.
// Recibe el ViewModel para acceder a las actividades guardadas.
// onProgramarActividad permite navegar a la pantalla para programar una actividad.
// onAbrirCalendario permite navegar a la pantalla del calendario.
@Composable
fun DiaScreen(
    agendaViewModel: AgendaViewModel = viewModel(),
    onProgramarActividad: () -> Unit = {},
    onAbrirCalendario: () -> Unit = {}
) {

    // Guarda el texto que el usuario escribe al crear una actividad.
    var textoTarea by remember { mutableStateOf("") }

// Guarda la hora seleccionada para la nueva actividad.
    var horaTarea by remember { mutableStateOf("") }

// Controla si se debe mostrar el selector de hora.
    var mostrarSelectorHora by remember { mutableStateOf(false) }

// Guarda temporalmente las actividades que tienen el sonido activado.
    var actividadesConSonido by remember { mutableStateOf(setOf<Int>()) }

// Guarda temporalmente las actividades que tienen las notificaciones activadas.
    var actividadesConNotificacion by remember { mutableStateOf(setOf<Int>()) }

// Obtiene la fecha actual del dispositivo.
    val calendarioActual = Calendar.getInstance()

// Guarda la fecha que el usuario está consultando en la pantalla.
    var fechaConsultada by remember {
        mutableStateOf(
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(calendarioActual.time)
        )
    }

// Obtiene la fecha de hoy para determinar cuándo mostrar las opciones de creación.
    val fechaHoy = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault()
    ).format(Date())

// Observa las actividades almacenadas en Room.
    val actividades by agendaViewModel.actividades.collectAsState()

// Inicializa los estados temporales de sonido y notificación para las actividades existentes.
    LaunchedEffect(actividades) {
        val ids = actividades.map { it.id }.toSet()

        actividadesConNotificacion = ids
        actividadesConSonido = ids
    }
    // Contenedor principal de la pantalla.
    // Ocupa el espacio disponible, permite desplazamiento vertical
    // y aplica un margen interno de 24 dp.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Espacio superior para separar el contenido del borde de la pantalla.
        Spacer(modifier = Modifier.height(30.dp))

        // Texto de bienvenida mostrado en la parte superior.
        Text(
            text = "Bienvenido a tu",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        // Nombre principal de la aplicación.
        Text(
            text = "Agenda Personal",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        // Espacio entre el encabezado y la información de la fecha.
        Spacer(modifier = Modifier.height(20.dp))

        // Muestra "Hoy es" únicamente cuando se está consultando la fecha actual.
        if (fechaConsultada == fechaHoy) {
            Text(
                text = "Hoy es",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        // Selector de fecha que permite consultar el día anterior, el día actual o días posteriores.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para consultar el día anterior.
            IconButton(
                onClick = {
                    val calendario = Calendar.getInstance()

                    // Convierte la fecha consultada de texto a un objeto Calendar.
                    calendario.time = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).parse(fechaConsultada)!!

                    // Retrocede un día en el calendario.
                    calendario.add(Calendar.DAY_OF_MONTH, -1)

                    // Guarda nuevamente la fecha consultada en formato yyyy-MM-dd.
                    fechaConsultada = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(calendario.time)
                },
                modifier = Modifier.width(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Día anterior",
                    tint = Color(0xFF3F6FC4)
                )
            }

            // Muestra el día de la semana y la fecha que actualmente se están consultando.
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Muestra el nombre del día de la semana.
                Text(
                    text = SimpleDateFormat(
                        "EEEE",
                        Locale("es", "CO")
                    ).format(
                        SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).parse(fechaConsultada)!!
                    ).uppercase(),
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3F6FC4),
                    textAlign = TextAlign.Center
                )
                // Muestra la fecha completa que está siendo consultada.
                Text(
                    text = SimpleDateFormat(
                        "d 'de' MMMM 'de' yyyy",
                        Locale("es", "CO")
                    ).format(
                        SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).parse(fechaConsultada)!!
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF303030),
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
            // Botón para consultar el día siguiente.
            IconButton(
                onClick = {
                    val calendario = Calendar.getInstance()

                    // Convierte la fecha consultada de texto a un objeto Calendar.
                    calendario.time = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).parse(fechaConsultada)!!

                    // Avanza un día en el calendario.
                    calendario.add(Calendar.DAY_OF_MONTH, 1)

                    // Guarda nuevamente la fecha consultada en formato yyyy-MM-dd.
                    fechaConsultada = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(calendario.time)
                },
                // Define el ancho del botón de navegación.
                modifier = Modifier.width(48.dp)
            ) {
                // Icono de flecha que permite avanzar al día siguiente.
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Día siguiente",
                    tint = Color(0xFF3F6FC4)
                )
            }
        }
        // Espacio entre el navegador de fechas y la sección de creación.
        Spacer(modifier = Modifier.height(20.dp))

        // Sección para crear rápidamente una nueva actividad.
        // Solo está disponible cuando se está consultando el día actual.
        if (fechaConsultada == fechaHoy) {

            // Título de la sección de creación de actividades.
            Text(
                text = "Nueva actividad",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            // Espacio entre el título y el campo de texto.
            Spacer(modifier = Modifier.height(12.dp))

            // Campo donde el usuario escribe el nombre o descripción de la actividad.
            TextField(
                value = textoTarea,
                onValueChange = { nuevoTexto ->
                    textoTarea = nuevoTexto
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("¿Qué tienes que hacer hoy?")
                },
                shape = RoundedCornerShape(8.dp),
                colors = androidx.compose.material3.TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF0F1F8),
                    focusedContainerColor = Color(0xFFF0F1F8),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )
            // Espacio entre el campo de texto y el selector de hora.
            Spacer(modifier = Modifier.height(10.dp))

            // Botón que permite seleccionar la hora de la actividad.
            OutlinedButton(
                onClick = {
                    mostrarSelectorHora = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icono que representa la selección de hora.
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Seleccionar hora",
                        tint = Color(0xFF3F6FC4)
                    )

                    // Espacio entre el icono y el texto.
                    Spacer(modifier = Modifier.width(8.dp))

                    // Muestra el texto correspondiente al estado de la hora.
                    // Si todavía no se ha seleccionado, muestra "Seleccionar hora".
                    Text(
                        text = if (horaTarea.isEmpty()) {
                            "Seleccionar hora"
                        } else {
                            "Hora: $horaTarea"
                        },
                        fontSize = 14.sp
                    )
                }
            }

            // Muestra el selector de hora cuando el usuario pulsa el botón anterior.
            if (mostrarSelectorHora) {
                val calendario = Calendar.getInstance()

                TimePickerDialog(
                    LocalContext.current,
                    { _, hora, minuto ->
                        horaTarea = String.format("%02d:%02d", hora, minuto)
                        mostrarSelectorHora = false
                    },
                    calendario.get(Calendar.HOUR_OF_DAY),
                    calendario.get(Calendar.MINUTE),
                    true
                ).show()
            }

            // Espacio entre el selector de hora y el botón para guardar la actividad.
            Spacer(modifier = Modifier.height(10.dp))

            // Botón que guarda la nueva actividad mediante el ViewModel.
            Button(
                onClick = {
                    // Solo permite guardar si se ha escrito un título y seleccionado una hora.
                    if (textoTarea.isNotBlank() && horaTarea.isNotBlank()) {
                        agendaViewModel.agregarActividad(
                            Actividad(
                                // Room genera automáticamente el identificador.
                                id = 0,
                                // Guarda el título escrito por el usuario.
                                titulo = textoTarea,
                                // Guarda la fecha actual de la actividad.
                                fecha = SimpleDateFormat(
                                    "yyyy-MM-dd",
                                    Locale.getDefault()
                                ).format(Date()),
                                // Guarda la hora seleccionada.
                                hora = horaTarea
                            )
                        )
                        // Limpia los campos después de guardar la actividad.
                        textoTarea = ""
                        horaTarea = ""
                    }
                },
                // Define el tamaño y el ancho del botón.
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                // Aplica bordes redondeados al botón.
                shape = RoundedCornerShape(24.dp)
            ) {
                // Texto mostrado dentro del botón.
                Text(
                    text = "Agregar Actividad",
                    fontSize = 14.sp
                )
            }

        }
        // Espacio entre la sección de creación y la lista de actividades.
        Spacer(modifier = Modifier.height(30.dp))

        // Título de la sección de actividades.
        // El texto cambia dependiendo de si se está consultando hoy u otro día.
        Text(
            text = if (fechaConsultada == fechaHoy) {
                "Actividades para hoy"
            } else {
                "Actividades para este día"
            },
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        // Espacio entre el título y la lista de actividades.
        Spacer(modifier = Modifier.height(12.dp))

        // Obtiene las actividades correspondientes a la fecha consultada.
        actividades
            .filter { actividad ->
                // Para días anteriores se muestran las actividades completadas.
                // Para hoy y días posteriores se muestran las actividades pendientes.
                if (fechaConsultada < fechaHoy) {
                    actividad.fecha == fechaConsultada && actividad.completada
                } else {
                    actividad.fecha == fechaConsultada && !actividad.completada
                }
            }
            // Ordena las actividades de acuerdo con su hora.
            .sortedBy { it.hora }
            // Recorre cada actividad para construir su tarjeta.
            .forEach { actividad ->
                // Tarjeta visual que contiene la información de una actividad.
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    // Borde utilizado para separar visualmente la tarjeta del fondo.
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0xFFD9E2F2)
                    )
                ) {
                    // Organiza horizontalmente el contenido de la actividad.
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Casilla que indica si la actividad está completada.
                        Checkbox(
                            checked = actividad.completada,
                            onCheckedChange = {
                                // Marca la actividad como completada mediante el ViewModel
                                agendaViewModel.completarActividad(actividad.id)
                            }
                        )

                        // Columna que contiene la hora y el título de la actividad.
                        // weight(1f) permite que ocupe el espacio disponible entre los iconos.
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            // Muestra la hora de la actividad en formato de 12 horas.
                            // Si ocurre algún problema al convertirla, muestra la hora original.
                            Text(
                                text = try {
                                    SimpleDateFormat(
                                        "hh:mm a",
                                        Locale.getDefault()
                                    ).format(
                                        SimpleDateFormat(
                                            "HH:mm",
                                            Locale.getDefault()
                                        ).parse(actividad.hora)!!
                                    )
                                } catch (e: Exception) {
                                    actividad.hora
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            // Muestra el título o descripción de la actividad.
                            Text(
                                text = actividad.titulo,
                                fontSize = 14.sp
                            )
                        }
                        // Botón para activar o desactivar el sonido de la actividad.
                        IconButton(
                            onClick = {
                                actividadesConSonido =
                                    if (actividad.id in actividadesConSonido) {
                                        // Si estaba activado, elimina el ID del conjunto.
                                        actividadesConSonido - actividad.id
                                    } else {
                                        // Si estaba desactivado, agrega el ID al conjunto.
                                        actividadesConSonido + actividad.id
                                    }
                            }
                        ) {
                            // Cambia el icono dependiendo de si el sonido está activo.
                            Icon(
                                imageVector = if (actividad.id in actividadesConSonido) {
                                    Icons.Default.VolumeUp
                                } else {
                                    Icons.Default.VolumeOff
                                },
                                // Descripción accesible del estado del sonido.
                                contentDescription = if (actividad.id in actividadesConSonido) {
                                    "Sonido activado"
                                } else {
                                    "Sonido desactivado"
                                },
                                // Cambia el color según el estado del sonido.
                                tint = if (actividad.id in actividadesConSonido) {
                                    Color(0xFF3F6FC4)
                                } else {
                                    Color.Gray
                                }
                            )
                        }
                        // Botón para activar o desactivar las notificaciones de la actividad.
                        IconButton(
                            onClick = {
                                actividadesConNotificacion =
                                    if (actividad.id in actividadesConNotificacion) {
                                        // Si estaba activada, elimina el ID del conjunto.
                                        actividadesConNotificacion - actividad.id
                                    } else {
                                        // Si estaba desactivada, agrega el ID al conjunto.
                                        actividadesConNotificacion + actividad.id
                                    }
                            }
                        ) {
                            // Cambia el icono dependiendo del estado de la notificación.
                            Icon(
                                imageVector = if (actividad.id in actividadesConNotificacion) {
                                    Icons.Default.Notifications
                                } else {
                                    Icons.Default.NotificationsOff
                                },
                                // Descripción accesible del estado de la notificación.
                                contentDescription = if (actividad.id in actividadesConNotificacion) {
                                    "Notificación activada"
                                } else {
                                    "Notificación desactivada"
                                },
                                // Cambia el color según el estado de la notificación.
                                tint = if (actividad.id in actividadesConNotificacion) {
                                    Color(0xFF3F6FC4)
                                } else {
                                    Color.Gray
                                }
                            )
                        }
                    }
                }
            }
        // Espacio entre la lista de actividades y los botones de navegación.
        Spacer(modifier = Modifier.height(16.dp))

        // Fila que contiene los botones para acceder a otras funciones de la aplicación
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // El botón "Programar actividad" solo se muestra cuando se consulta el día actual.
            if (fechaConsultada == fechaHoy) {

                // Botón para acceder a la pantalla de programación de actividades.
                OutlinedButton(
                    onClick = {
                        // Ejecuta la función de navegación recibida por DiaScreen.
                        onProgramarActividad()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icono utilizado para representar la programación de una actividad.
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar_add_on),
                            contentDescription = "Programar actividad",
                            tint = Color(0xFF3F6FC4)
                        )
                        // Espacio entre el icono y el texto.
                        Spacer(modifier = Modifier.width(6.dp))

                        // Texto del botón de programación.
                        Text(
                            text = "Programar\nactividad",
                            fontSize = 12.sp,
                            color = Color(0xFF3F6FC4)
                        )
                    }
                }
            }
            // Botón para acceder al calendario mensual.
            Button(
                onClick = onAbrirCalendario,

                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icono que representa el calendario.
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Calendario",
                        tint = Color.White
                    )
                    // Espacio entre el icono y el texto.
                    Spacer(modifier = Modifier.width(6.dp))

                    // Texto del botón para abrir el calendario.
                    Text(
                        text = "Calendario",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}




