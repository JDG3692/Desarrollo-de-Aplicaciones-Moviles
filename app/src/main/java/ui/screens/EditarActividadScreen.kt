package com.example.agendapersonal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agendapersonal.viewmodel.AgendaViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


// Pantalla encargada de editar una actividad existente.
// Mantiene el mismo estilo visual general utilizado en las demás pantallas.
@Composable
fun EditarActividadScreen(
    actividadId: Int,
    onVolver: () -> Unit,
    agendaViewModel: AgendaViewModel = viewModel()
) {
    // Obtiene la lista actual de actividades desde Room.
    val actividades by agendaViewModel.actividades.collectAsState()

    // Busca la actividad que corresponde al identificador recibido.
    val actividad = actividades.find { it.id == actividadId }

    // Campos que contienen los datos editables de la actividad.
    var titulo by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    // Controla si se muestra el diálogo de confirmación de eliminación.
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    // Color principal utilizado en la aplicación.
    val azulPrincipal = Color(0xFF4B5F91)

    // Obtiene el contexto necesario para mostrar los selectores de Android.
    val contexto = LocalContext.current

    // Carga los datos de la actividad cuando esta está disponible.
    LaunchedEffect(actividad?.id) {
        if (actividad != null) {
            titulo = actividad.titulo
            fecha = actividad.fecha
            hora = actividad.hora
        }
    }

    // Espera a que las actividades estén disponibles antes de mostrar la edición.
    if (actividad == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Cargando actividad...",
                fontSize = 16.sp
            )
        }
        return
    }

    // Contenedor principal de la pantalla de edición.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        // Espacio superior para separar el contenido del borde de la pantalla.
        Spacer(modifier = Modifier.height(40.dp))

        // Encabezado que permite volver sin guardar los cambios.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para volver a la pantalla anterior.
            IconButton(
                onClick = onVolver,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                )
            }

            // Título principal de la pantalla.
            Text(
                text = "Editar actividad",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Campo utilizado para modificar el título de la actividad.
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = {
                Text("Título")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón que permite seleccionar una nueva fecha.
        OutlinedButton(
            onClick = {
                // Obtiene hoy como fecha mínima permitida para editar.
                val fechaMinima = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                // Convierte la fecha actual de la actividad para usarla inicialmente
                // en el selector.
                val fechaInicial = try {
                    SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).parse(fecha)
                } catch (e: Exception) {
                    null
                }

                // Si la fecha guardada no se puede convertir, utiliza hoy.
                val calendarioInicial = Calendar.getInstance().apply {
                    if (fechaInicial != null) {
                        time = fechaInicial
                    }
                }

                // Crea el selector de fecha.
                DatePickerDialog(
                    contexto,
                    { _, anio, mes, dia ->

                        // Guarda la fecha seleccionada en el formato utilizado por Room.
                        val fechaSeleccionada = Calendar.getInstance().apply {
                            set(
                                anio,
                                mes,
                                dia,
                                0,
                                0,
                                0
                            )
                            set(Calendar.MILLISECOND, 0)
                        }

                        fecha = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).format(fechaSeleccionada.time)
                    },
                    calendarioInicial.get(Calendar.YEAR),
                    calendarioInicial.get(Calendar.MONTH),
                    calendarioInicial.get(Calendar.DAY_OF_MONTH)
                ).apply {

                    // Impide seleccionar fechas anteriores a hoy.
                    datePicker.minDate = fechaMinima.timeInMillis

                }.show()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            // Organiza el icono y la fecha seleccionada.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Seleccionar fecha",
                    tint = azulPrincipal
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = try {
                        SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                        ).format(
                            SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).parse(fecha)!!
                        )
                    } catch (e: Exception) {
                        fecha
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón que permite seleccionar una nueva hora.
        OutlinedButton(
            onClick = {
                // Obtiene la fecha actual seleccionada en la actividad.
                val fechaActividad = try {
                    SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).parse(fecha)
                } catch (e: Exception) {
                    null
                }

                // Obtiene la fecha actual del dispositivo sin considerar la hora.
                val hoy = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                // Comprueba si la actividad está programada para hoy.
                val actividadEsHoy =
                    fechaActividad != null &&
                            fechaActividad.time == hoy.time.time

                // Obtiene la hora actual para usarla como referencia.
                val horaActual = Calendar.getInstance()

                // Convierte la hora guardada de la actividad para mostrarla inicialmente.
                val horaInicial = try {
                    SimpleDateFormat(
                        "HH:mm",
                        Locale.getDefault()
                    ).parse(hora)
                } catch (e: Exception) {
                    null
                }

                // Crea el calendario inicial del selector de hora.
                val calendarioInicial = Calendar.getInstance().apply {
                    if (horaInicial != null) {
                        time = horaInicial
                    }
                }

                // Crea el selector de hora de Android.
                TimePickerDialog(
                    contexto,
                    { _, horaSeleccionada, minutoSeleccionado ->

                        // Comprueba si la nueva hora está siendo seleccionada para hoy.
                        if (actividadEsHoy) {

                            // Convierte la hora elegida a minutos para compararla
                            // con la hora actual.
                            val minutosSeleccionados =
                                horaSeleccionada * 60 + minutoSeleccionado

                            val minutosActuales =
                                horaActual.get(Calendar.HOUR_OF_DAY) * 60 +
                                        horaActual.get(Calendar.MINUTE)

                            // Solo acepta horas iguales o posteriores a la actual.
                            if (minutosSeleccionados >= minutosActuales) {
                                hora = String.format(
                                    "%02d:%02d",
                                    horaSeleccionada,
                                    minutoSeleccionado
                                )
                            }
                        } else {

                            // Para fechas futuras se permite cualquier hora.
                            hora = String.format(
                                "%02d:%02d",
                                horaSeleccionada,
                                minutoSeleccionado
                            )
                        }
                    },
                    calendarioInicial.get(Calendar.HOUR_OF_DAY),
                    calendarioInicial.get(Calendar.MINUTE),
                    true
                ).show()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            // Organiza el icono y la hora seleccionada.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Seleccionar hora",
                    tint = azulPrincipal
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = try {
                        SimpleDateFormat(
                            "hh:mm a",
                            Locale.getDefault()
                        ).format(
                            SimpleDateFormat(
                                "HH:mm",
                                Locale.getDefault()
                            ).parse(hora)!!
                        )
                    } catch (e: Exception) {
                        hora
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Botón principal para guardar los cambios.
        Button(
            onClick = {
                // Convierte la fecha seleccionada a un objeto Calendar.
                val fechaSeleccionada = try {
                    SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).parse(fecha)
                } catch (e: Exception) {
                    null
                }

                // Obtiene hoy sin considerar la hora.
                val hoy = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                // Comprueba si la actividad está programada para hoy.
                val fechaEsHoy =
                    fechaSeleccionada != null &&
                            fechaSeleccionada.time == hoy.time.time

                // Comprueba si la hora seleccionada ya pasó.
                val horaEsInvalida = if (fechaEsHoy) {
                    try {
                        val horaPartes = hora.split(":")
                        val horaSeleccionada = horaPartes[0].toInt()
                        val minutoSeleccionado = horaPartes[1].toInt()

                        val ahora = Calendar.getInstance()

                        val minutosSeleccionados =
                            horaSeleccionada * 60 + minutoSeleccionado

                        val minutosActuales =
                            ahora.get(Calendar.HOUR_OF_DAY) * 60 +
                                    ahora.get(Calendar.MINUTE)

                        minutosSeleccionados < minutosActuales
                    } catch (e: Exception) {
                        false
                    }
                } else {
                    false
                }

                // Si la hora ya pasó, no permite guardar la actividad.
                if (horaEsInvalida) {
                    // No permite guardar una actividad de hoy con una hora que ya pasó.
                } else {
                    // Conserva el estado de completada y de alarma de la actividad.
                    val actividadActualizada = actividad.copy(
                        titulo = titulo,
                        fecha = fecha,
                        hora = hora
                    )

                    // Guarda los cambios en Room.
                    agendaViewModel.actualizarActividad(actividadActualizada)

                    // Regresa a la pantalla anterior.
                    onVolver()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = azulPrincipal,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Guardar cambios",
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón secundario para eliminar la actividad.
        OutlinedButton(
            onClick = {
                // Muestra el diálogo antes de eliminar.
                mostrarDialogoEliminar = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                contentColor = azulPrincipal
            )
        ) {
            Text(
                text = "Eliminar actividad",
                fontSize = 14.sp,
                color = azulPrincipal
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

    }

    // Muestra una confirmación antes de eliminar definitivamente la actividad.
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = {

                mostrarDialogoEliminar = false
            },
            containerColor = Color.White,
            titleContentColor = Color(0xFF303030),
            title = {
                Text("Eliminar actividad")
            },
            text = {
                Text("¿Estás seguro de que deseas eliminar esta actividad?")
            },
            confirmButton = {
                Button(
                    onClick = {

                        // Elimina la actividad de Room.
                        agendaViewModel.eliminarActividad(actividad)

                        // Cierra el diálogo.
                        mostrarDialogoEliminar = false

                        // Regresa a la pantalla anterior.
                        onVolver()
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = azulPrincipal,
                        contentColor = Color.White
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        // Cierra el diálogo sin eliminar.
                        mostrarDialogoEliminar = false
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = azulPrincipal
                    )
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

}