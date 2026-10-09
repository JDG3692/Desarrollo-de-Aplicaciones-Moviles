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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AlarmOff
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.ui.res.painterResource
import com.example.agendapersonal.R
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.filled.Delete
import android.app.DatePickerDialog




// Pantalla principal de la agenda diaria.
// Recibe el ViewModel para acceder a las actividades guardadas.
// onProgramarActividad permite navegar a la pantalla para programar una actividad.
// onAbrirCalendario permite navegar a la pantalla del calendario.
@Composable
fun DiaScreen(
    agendaViewModel: AgendaViewModel = viewModel(),
    onProgramarActividad: () -> Unit = {},
    onAbrirCalendario: () -> Unit = {},
    onEditarActividad: (Int) -> Unit = {}
) {

    // Guarda el texto que el usuario escribe al crear una actividad.
    var textoTarea by remember { mutableStateOf("") }

    // Guarda la hora seleccionada para la nueva actividad.
    var horaTarea by remember { mutableStateOf("") }

    // Controla si se debe mostrar el selector de hora.
    var mostrarSelectorHora by remember { mutableStateOf(false) }

    // Controla la visibilidad del menú principal de la papelera.
    var mostrarMenuPapelera by remember { mutableStateOf(false) }

    // Controla la visibilidad del diálogo para eliminar actividades completadas.
    var mostrarConfirmacionCompletadas by remember { mutableStateOf(false) }

    // Controla la visibilidad del diálogo para elegir el tipo de eliminación por periodo.
    var mostrarMenuPeriodo by remember { mutableStateOf(false) }

    // Guarda el tipo de eliminación seleccionado por el usuario.
    var tipoEliminacion by remember { mutableStateOf("") }

    // Controla la visibilidad del diálogo de selección de actividad.
    var mostrarSeleccionActividad by remember { mutableStateOf(false) }

    // Controla la visibilidad del selector de fecha para eliminar por día.
    var mostrarSelectorFechaEliminacion by remember { mutableStateOf(false) }

    // Guarda la fecha seleccionada para la eliminación.
    var fechaEliminacion by remember { mutableStateOf("") }

    // Controla la visibilidad del diálogo de confirmación por periodo.
    var mostrarConfirmacionPeriodo by remember { mutableStateOf(false) }

    // Guarda las fechas inicial y final del rango que se eliminará.
    var fechaInicioEliminacion by remember { mutableStateOf("") }
    var fechaFinEliminacion by remember { mutableStateOf("") }

    // Guarda el identificador de la actividad elegida para eliminar.
    var actividadSeleccionadaId by remember { mutableStateOf<Int?>(null) }

    // Obtiene la fecha actual del dispositivo.
    val calendarioActual = Calendar.getInstance()

    // Guarda la fecha que el usuario está consultando en la pantalla.
    var fechaConsultada by remember {
        mutableStateOf(
            SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault()
            ).format(calendarioActual.time)
        )
    }
    // Controla si se muestra el aviso cuando se intenta editar una actividad completada.
    var mostrarAvisoActividadCompletada by remember {
        mutableStateOf(false)
    }

    // Obtiene la fecha de hoy para determinar cuándo mostrar las opciones de creación.
    val fechaHoy = SimpleDateFormat(
        "yyyy-MM-dd", Locale.getDefault()
    ).format(Date())

    // Observa las actividades almacenadas en Room.
    val actividades by agendaViewModel.actividades.collectAsState()

    // Color principal unificado de Agenda Personal.
    val azulPrincipal = Color(0xFF4B5F91)

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
            text = "Bienvenido a tu", fontSize = 28.sp, fontWeight = FontWeight.Bold
        )

        // Encabezado de la aplicación con acceso a la papelera.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nombre principal de la aplicación.
            Text(
                text = "Agenda Personal",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            // Acceso visual a las opciones de eliminación.
            IconButton(
                onClick = {
                    mostrarMenuPapelera = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Papelera",
                    tint = azulPrincipal
                )
            }
        }
        // Espacio entre el encabezado y la información de la fecha.
        Spacer(modifier = Modifier.height(20.dp))

        // Muestra "Hoy es" únicamente cuando se está consultando la fecha actual.
        if (fechaConsultada == fechaHoy) {
            Text(
                text = "Hoy es", fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        // Selector de fecha que permite consultar el día anterior, el día actual o días posteriores.
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para consultar el día anterior.
            IconButton(
                onClick = {
                    val calendario = Calendar.getInstance()

                    // Convierte la fecha consultada de texto a un objeto Calendar.
                    calendario.time = SimpleDateFormat(
                        "yyyy-MM-dd", Locale.getDefault()
                    ).parse(fechaConsultada)!!

                    // Retrocede un día en el calendario.
                    calendario.add(Calendar.DAY_OF_MONTH, -1)

                    // Guarda nuevamente la fecha consultada en formato yyyy-MM-dd.
                    fechaConsultada = SimpleDateFormat(
                        "yyyy-MM-dd", Locale.getDefault()
                    ).format(calendario.time)
                }, modifier = Modifier.width(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Día anterior",
                    tint = azulPrincipal
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
                        "EEEE", Locale("es", "CO")
                    ).format(
                        SimpleDateFormat(
                            "yyyy-MM-dd", Locale.getDefault()
                        ).parse(fechaConsultada)!!
                    ).uppercase(),
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = azulPrincipal,
                    textAlign = TextAlign.Center
                )
                // Muestra la fecha completa que está siendo consultada.
                Text(
                    text = SimpleDateFormat(
                        "d 'de' MMMM 'de' yyyy", Locale("es", "CO")
                    ).format(
                        SimpleDateFormat(
                            "yyyy-MM-dd", Locale.getDefault()
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
                        "yyyy-MM-dd", Locale.getDefault()
                    ).parse(fechaConsultada)!!

                    // Avanza un día en el calendario.
                    calendario.add(Calendar.DAY_OF_MONTH, 1)

                    // Guarda nuevamente la fecha consultada en formato yyyy-MM-dd.
                    fechaConsultada = SimpleDateFormat(
                        "yyyy-MM-dd", Locale.getDefault()
                    ).format(calendario.time)
                },
                // Define el ancho del botón de navegación.
                modifier = Modifier.width(48.dp)
            ) {
                // Icono de flecha que permite avanzar al día siguiente.
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Día siguiente",
                    tint = azulPrincipal
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
                text = "Nueva actividad", fontSize = 22.sp, fontWeight = FontWeight.Bold
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
                        tint = azulPrincipal
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
                        }, fontSize = 14.sp
                    )
                }
            }

            // Muestra el selector de hora cuando el usuario pulsa el botón anterior.
            if (mostrarSelectorHora) {
                val calendario = Calendar.getInstance()

                TimePickerDialog(
                    LocalContext.current,
                    { _, hora, minuto ->

                        // Obtiene la hora y minuto actuales del dispositivo.
                        val ahora = Calendar.getInstance()

                        // Convierte la hora seleccionada a minutos.
                        val minutosSeleccionados =
                            hora * 60 + minuto

                        // Convierte la hora actual a minutos.
                        val minutosActuales =
                            ahora.get(Calendar.HOUR_OF_DAY) * 60 +
                                    ahora.get(Calendar.MINUTE)

                        // Solo permite seleccionar la hora actual o una hora futura.
                        if (minutosSeleccionados >= minutosActuales) {
                            horaTarea = String.format(
                                "%02d:%02d",
                                hora,
                                minuto
                            )
                        }

                        // Cierra el selector de hora.
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
                                    "yyyy-MM-dd", Locale.getDefault()
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
                    text = "Agregar Actividad", fontSize = 14.sp
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
            }, fontSize = 22.sp, fontWeight = FontWeight.Bold
        )
        // Espacio entre el título y la lista de actividades.
        Spacer(modifier = Modifier.height(12.dp))

        // Obtiene las actividades correspondientes a la fecha consultada.
        val actividadesDelDia = actividades
            .filter { actividad ->
                actividad.fecha == fechaConsultada
            }
            .sortedBy { it.hora }

        // Muestra un mensaje cuando no existen actividades para el día consultado.
        if (actividadesDelDia.isEmpty()) {
            Text(
                text = "No hay actividades programadas para este día",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                color = Color.Gray
            )
        }

        // Recorre las actividades del día para construir sus tarjetas.
        actividadesDelDia.forEach { actividad ->
                // Comprueba si la actividad ya fue completada.
                val actividadCompletada = actividad.completada

                // Convierte la fecha de la actividad para comprobar si ya pasó.
                val fechaActividad = try {
                    SimpleDateFormat(
                        "yyyy-MM-dd", Locale.getDefault()
                    ).parse(actividad.fecha)
                } catch (e: Exception) {
                    null
                }

                // Obtiene la fecha actual sin considerar la hora.
                val fechaHoyCalendario = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time

                // Una actividad no completada queda vencida cuando su fecha ya pasó.
                val actividadNoCompletada =
                    !actividadCompletada && fechaActividad != null && fechaActividad.before(
                        fechaHoyCalendario
                    )
                // Tarjeta visual que contiene la información de una actividad.
                Card(
                    onClick = {
                        // Las actividades completadas no se pueden editar.
                        if (actividad.completada) {
                            mostrarAvisoActividadCompletada = true
                        } else {
                            // Las actividades pendientes sí pueden abrirse para editar.
                            onEditarActividad(actividad.id)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    // Borde utilizado para separar visualmente la tarjeta del fondo.
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, Color(0xFFD9E2F2)
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
                            checked = actividad.completada, onCheckedChange = {
                                // Marca la actividad como completada mediante el ViewModel
                                agendaViewModel.completarActividad(actividad.id)
                            })

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
                                        "hh:mm a", Locale.getDefault()
                                    ).format(
                                        SimpleDateFormat(
                                            "HH:mm", Locale.getDefault()
                                        ).parse(actividad.hora)!!
                                    )
                                } catch (e: Exception) {
                                    actividad.hora
                                },
                                fontSize = 12.sp,
                                color = azulPrincipal,
                                fontWeight = FontWeight.SemiBold
                            )
                            // Muestra el título o descripción como elemento principal.
                            Text(
                                text = actividad.titulo,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                        // Muestra un estado diferente según la situación de la actividad.
                        if (actividadCompletada) {

                            // Actividad que ya fue completada.
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Completada",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = azulPrincipal
                                )

                                // Icono que identifica una actividad completada.
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Actividad completada",
                                    modifier = Modifier.padding(start = 4.dp),
                                    tint = Color.Green
                                )
                            }

                        } else if (actividadNoCompletada) {

                            // Actividad cuya fecha ya pasó y no fue completada.
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "No completada",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = azulPrincipal
                                )

                                // Icono que identifica una actividad no completada.
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Actividad no completada",
                                    modifier = Modifier.padding(start = 4.dp),
                                    tint = Color.Red
                                )
                            }

                        } else {

                            // Actividad pendiente cuya fecha todavía no ha pasado.
                            IconButton(
                                onClick = {
                                    agendaViewModel.cambiarEstadoAlarma(actividad.id)
                                }) {
                                Icon(
                                    imageVector = if (actividad.alarmaActiva) Icons.Default.Alarm
                                    else Icons.Default.AlarmOff,
                                    contentDescription = if (actividad.alarmaActiva) "Alarma activada"
                                    else "Alarma silenciada",
                                    tint = azulPrincipal
                                )
                            }
                        }
                    }
                }
            }
        // Muestra un aviso cuando se intenta editar una actividad completada.
        if (mostrarAvisoActividadCompletada) {
            AlertDialog(
                onDismissRequest = {
                    mostrarAvisoActividadCompletada = false
                },
                title = {
                    Text("Actividad completada")
                },
                text = {
                    Text("Las actividades completadas no se pueden editar.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            mostrarAvisoActividadCompletada = false
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            )
        }

        // Muestra el menú principal de la papelera.
        if (mostrarMenuPapelera) {
            AlertDialog(
                onDismissRequest = {

                    mostrarMenuPapelera = false
                },
                containerColor = Color.White,
                titleContentColor = Color(0xFF303030),

                title = {
                    Text("Papelera")
                },
                text = {
                    Column {
                        Text("¿Qué deseas eliminar?")
                        Spacer(modifier = Modifier.height(12.dp))

                        // Opción para eliminar las actividades completadas.
                        OutlinedButton(
                            onClick = {
                                mostrarMenuPapelera = false
                                mostrarConfirmacionCompletadas = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Eliminar actividades completadas")
                        }

                        // Opción para seleccionar una actividad o un periodo.
                        OutlinedButton(
                            onClick = {
                                mostrarMenuPapelera = false
                                mostrarMenuPeriodo = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Eliminar actividades por periodo")
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            mostrarMenuPapelera = false
                        }
                    ) {
                        Text("Cerrar")
                    }
                }
            )
        }

        // Solicita confirmación antes de eliminar actividades completadas.
        if (mostrarConfirmacionCompletadas) {
            val cantidadCompletadas = actividades.count { it.completada }

            AlertDialog(
                onDismissRequest = {
                    mostrarConfirmacionCompletadas = false
                },
                containerColor = Color.White,
                titleContentColor = Color(0xFF303030),

                title = {
                    Text("Eliminar actividades completadas")
                },
                text = {
                    Text(
                        if (cantidadCompletadas == 0) {
                            "No hay actividades completadas para eliminar."
                        } else {
                            "Se eliminarán $cantidadCompletadas actividades completadas " +
                                    "de toda la agenda. Esta acción no se puede deshacer."
                        }
                    )
                },

                confirmButton = {
                    Button(
                        enabled = cantidadCompletadas > 0,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4B5F91),
                            contentColor = Color.White
                        ),
                        onClick = {
                            agendaViewModel.eliminarActividadesCompletadas()
                            mostrarConfirmacionCompletadas = false
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            mostrarConfirmacionCompletadas = false
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Permite seleccionar el alcance de la eliminación.
        if (mostrarMenuPeriodo) {
            AlertDialog(
                onDismissRequest = {
                    mostrarMenuPeriodo = false
                },
                containerColor = Color.White,
                titleContentColor = Color(0xFF303030),

                title = {
                    Text("Eliminar actividades por periodo")
                },
                text = {
                    Column {
                        Text("Selecciona qué deseas eliminar:")
                        Spacer(modifier = Modifier.height(12.dp))

                        // Permite seleccionar una actividad individual.
                        OutlinedButton(
                            onClick = {
                                tipoEliminacion = "actividad"
                                mostrarMenuPeriodo = false
                                mostrarSeleccionActividad = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Una actividad")
                        }

                        // Permite elegir un día específico.
                        OutlinedButton(
                            onClick = {
                                tipoEliminacion = "dia"
                                mostrarMenuPeriodo = false
                                mostrarSelectorFechaEliminacion = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Un día")
                        }

                        // Permite elegir un mes.
                        OutlinedButton(
                            onClick = {
                                tipoEliminacion = "mes"
                                mostrarMenuPeriodo = false
                                mostrarSelectorFechaEliminacion = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Un mes")
                        }

                        // Permite elegir un año.
                        OutlinedButton(
                            onClick = {
                                tipoEliminacion = "anio"
                                mostrarMenuPeriodo = false
                                mostrarSelectorFechaEliminacion = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Un año")
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            mostrarMenuPeriodo = false
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Muestra todas las actividades para seleccionar una que se desea eliminar.
        if (mostrarSeleccionActividad) {
            AlertDialog(
                onDismissRequest = {
                    mostrarSeleccionActividad = false
                    actividadSeleccionadaId = null
                },
                containerColor = Color.White,
                titleContentColor = Color(0xFF303030),

                title = {
                    Text("Seleccionar actividad")
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Informa si la agenda no contiene actividades.
                        if (actividades.isEmpty()) {
                            Text("No hay actividades en la agenda.")
                        }

                        // Muestra todas las actividades, sin limitarse al día consultado.
                        actividades.sortedWith(
                            compareBy<Actividad> { it.fecha }.thenBy { it.hora }
                        ).forEach { actividad ->
                            OutlinedButton(
                                onClick = {
                                    actividadSeleccionadaId = actividad.id
                                    mostrarSeleccionActividad = false
                                    mostrarConfirmacionPeriodo = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(actividad.titulo)
                                    Text(
                                        "${actividad.fecha} · ${actividad.hora}",
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            mostrarSeleccionActividad = false
                            actividadSeleccionadaId = null
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Solicita confirmación antes de eliminar una actividad individual.
        if (mostrarConfirmacionPeriodo && tipoEliminacion == "actividad") {

            // Busca la actividad seleccionada en la agenda.
            val actividadSeleccionada = actividades.find {
                it.id == actividadSeleccionadaId
            }

            AlertDialog(
                onDismissRequest = {
                    mostrarConfirmacionPeriodo = false
                    actividadSeleccionadaId = null
                },
                containerColor = Color.White,
                titleContentColor = Color(0xFF303030),
                title = {
                    Text("Eliminar actividad")
                },
                text = {
                    if (actividadSeleccionada != null) {
                        Text(
                            "¿Deseas eliminar esta actividad?\n\n" +
                                    "${actividadSeleccionada.titulo}\n" +
                                    "Fecha: ${actividadSeleccionada.fecha}\n" +
                                    "Hora: ${actividadSeleccionada.hora}\n\n" +
                                    "Esta acción no se puede deshacer."
                        )
                    } else {
                        Text("La actividad seleccionada ya no está disponible.")
                    }
                },
                confirmButton = {
                    Button(
                        enabled = actividadSeleccionada != null,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = azulPrincipal,
                            contentColor = Color.White
                        ),
                        onClick = {
                            actividadSeleccionada?.let {
                                agendaViewModel.eliminarActividad(it)
                            }

                            mostrarConfirmacionPeriodo = false
                            actividadSeleccionadaId = null
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            mostrarConfirmacionPeriodo = false
                            actividadSeleccionadaId = null
                        },
                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                            contentColor = azulPrincipal
                        )
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
        // Abre el selector nativo para elegir el día, mes o año.
        if (
            mostrarSelectorFechaEliminacion &&
            tipoEliminacion in listOf("dia", "mes", "anio")
        ) {
            val calendarioSelector = Calendar.getInstance()

            DatePickerDialog(
                LocalContext.current,
                { _, anio, mes, dia ->
                    val calendarioSeleccionado = Calendar.getInstance().apply {
                        set(Calendar.YEAR, anio)
                        set(Calendar.MONTH, mes)
                        set(Calendar.DAY_OF_MONTH, dia)
                    }

                    // Define el inicio y el final exclusivo del periodo elegido.
                    when (tipoEliminacion) {
                        "dia" -> {
                            fechaEliminacion = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(calendarioSeleccionado.time)

                            fechaInicioEliminacion = fechaEliminacion

                            calendarioSeleccionado.add(
                                Calendar.DAY_OF_MONTH,
                                1
                            )

                            fechaFinEliminacion = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(calendarioSeleccionado.time)
                        }

                        "mes" -> {
                            calendarioSeleccionado.set(
                                Calendar.DAY_OF_MONTH,
                                1
                            )

                            fechaInicioEliminacion = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(calendarioSeleccionado.time)

                            calendarioSeleccionado.add(
                                Calendar.MONTH,
                                1
                            )

                            fechaFinEliminacion = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(calendarioSeleccionado.time)
                        }

                        "anio" -> {
                            calendarioSeleccionado.set(
                                Calendar.MONTH,
                                Calendar.JANUARY
                            )
                            calendarioSeleccionado.set(
                                Calendar.DAY_OF_MONTH,
                                1
                            )

                            fechaInicioEliminacion = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(calendarioSeleccionado.time)

                            calendarioSeleccionado.add(
                                Calendar.YEAR,
                                1
                            )

                            fechaFinEliminacion = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(calendarioSeleccionado.time)
                        }
                    }

                    mostrarSelectorFechaEliminacion = false
                    mostrarConfirmacionPeriodo = true
                },
                calendarioSelector.get(Calendar.YEAR),
                calendarioSelector.get(Calendar.MONTH),
                calendarioSelector.get(Calendar.DAY_OF_MONTH)
            ).apply {
                setTitle(
                    when (tipoEliminacion) {
                        "dia" -> "Selecciona el día"
                        "mes" -> "Selecciona una fecha del mes"
                        "anio" -> "Selecciona una fecha del año"
                        else -> "Selecciona una fecha"
                    }
                )
            }.show()
        }

        // Solicita confirmación antes de eliminar las actividades de un día.
        if (mostrarConfirmacionPeriodo && tipoEliminacion == "dia") {
            val actividadesDelPeriodo = actividades.filter {
                it.fecha == fechaEliminacion
            }
            val cantidadActividades = actividadesDelPeriodo.size

            AlertDialog(
                onDismissRequest = {
                    mostrarConfirmacionPeriodo = false
                },
                containerColor = Color.White,
                titleContentColor = Color(0xFF303030),
                title = {
                    Text("Eliminar actividades del día")
                },
                text = {
                    Text(
                        if (cantidadActividades == 0) {
                            "No hay actividades para el día $fechaEliminacion."
                        } else {
                            "Se eliminarán $cantidadActividades actividades del día " +
                                    "$fechaEliminacion. Se incluyen actividades pendientes, " +
                                    "completadas y vencidas. Esta acción no se puede deshacer."
                        }
                    )
                },
                confirmButton = {
                    Button(
                        enabled = cantidadActividades > 0,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = azulPrincipal,
                            contentColor = Color.White
                        ),
                        onClick = {
                            agendaViewModel.eliminarActividadesDelDia(fechaEliminacion)
                            mostrarConfirmacionPeriodo = false
                            fechaEliminacion = ""
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            mostrarConfirmacionPeriodo = false
                            fechaEliminacion = ""
                        },
                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                            contentColor = azulPrincipal
                        )
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
        // Solicita confirmación para eliminar todas las actividades de un mes o año.
        if (
            mostrarConfirmacionPeriodo &&
            tipoEliminacion in listOf("mes", "anio")
        ) {
            val actividadesDelPeriodo = actividades.filter {
                it.fecha >= fechaInicioEliminacion &&
                        it.fecha < fechaFinEliminacion
            }

            val cantidadActividades = actividadesDelPeriodo.size

            val inicioPeriodo = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).parse(fechaInicioEliminacion)!!

            val descripcionPeriodo = when (tipoEliminacion) {
                "mes" -> SimpleDateFormat(
                    "MMMM 'de' yyyy",
                    Locale("es", "CO")
                ).format(inicioPeriodo)

                else -> fechaInicioEliminacion.substring(0, 4)
            }

            AlertDialog(
                onDismissRequest = {
                    mostrarConfirmacionPeriodo = false
                },
                containerColor = Color.White,
                titleContentColor = Color(0xFF303030),
                title = {
                    Text(
                        if (tipoEliminacion == "mes") {
                            "Eliminar actividades del mes"
                        } else {
                            "Eliminar actividades del año"
                        }
                    )
                },
                text = {
                    Text(
                        if (cantidadActividades == 0) {
                            "No hay actividades para $descripcionPeriodo."
                        } else {
                            "Se eliminarán $cantidadActividades actividades de " +
                                    "$descripcionPeriodo. Se incluyen actividades " +
                                    "pendientes, completadas y vencidas. " +
                                    "Esta acción no se puede deshacer."
                        }
                    )
                },
                confirmButton = {
                    Button(
                        enabled = cantidadActividades > 0,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = azulPrincipal,
                            contentColor = Color.White
                        ),
                        onClick = {
                            agendaViewModel.eliminarActividadesPorRango(
                                fechaInicioEliminacion,
                                fechaFinEliminacion
                            )

                            mostrarConfirmacionPeriodo = false
                            fechaInicioEliminacion = ""
                            fechaFinEliminacion = ""
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            mostrarConfirmacionPeriodo = false
                            fechaInicioEliminacion = ""
                            fechaFinEliminacion = ""
                        },
                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                            contentColor = azulPrincipal
                        )
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Espacio entre la lista de actividades y los botones de navegación.
        Spacer(modifier = Modifier.height(16.dp))

        // Fila que contiene los botones para acceder a otras funciones de la aplicación
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                            tint = azulPrincipal
                        )
                        // Espacio entre el icono y el texto.
                        Spacer(modifier = Modifier.width(6.dp))

                        // Texto del botón de programación.
                        Text(
                            text = "Programar\nactividad",
                            fontSize = 12.sp,
                            color = azulPrincipal
                        )
                    }
                }
            }
            // Botón para acceder al calendario mensual.
            Button(
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = azulPrincipal,
                    contentColor = Color.White
                ),
                onClick = onAbrirCalendario,

                modifier = Modifier
                    .weight(1f)
                    .height(52.dp), shape = RoundedCornerShape(12.dp)
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
                        text = "Calendario", fontSize = 12.sp, color = Color.White
                    )
                }
            }
        }
    }
}




