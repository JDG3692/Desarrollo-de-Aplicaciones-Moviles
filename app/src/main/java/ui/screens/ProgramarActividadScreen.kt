package com.example.agendapersonal.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agendapersonal.model.Actividad
import com.example.agendapersonal.viewmodel.AgendaViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.ArrowBack

// Pantalla utilizada para programar una actividad en una fecha y hora específicas.
// Recibe el ViewModel para guardar la actividad y una función para regresar
// a la pantalla anterior.
@Composable
fun ProgramarActividadScreen(
    agendaViewModel: AgendaViewModel = viewModel(),
    onVolver: () -> Unit = {}
) {
    // Guarda temporalmente el título o descripción de la actividad.
    var textoTarea by remember {
        mutableStateOf("")
    }
    // Guarda temporalmente la fecha seleccionada para la actividad.
    var fechaTarea by remember {
        mutableStateOf("")
    }
    // Guarda temporalmente la hora seleccionada para la actividad.
    var horaTarea by remember {
        mutableStateOf("")
    }
    // Obtiene el contexto actual necesario para mostrar los diálogos de Android.
    val contexto = LocalContext.current

    // Obtiene la fecha y hora actuales como valores iniciales para los selectores.
    val calendario = Calendar.getInstance()


    // Contenedor principal de la pantalla.
    // Ocupa el espacio disponible y aplica un margen interno de 24 dp.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        // Espacio superior para separar el contenido del borde de la pantalla.
        Spacer(modifier = Modifier.height(40.dp))
        // Encabezado de la pantalla con el botón para regresar y el título.
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            // Botón para regresar a la pantalla anterior.
            IconButton(
                onClick = {
                    onVolver()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar"
                )
            }
            // Título de la pantalla.
            Text(
                text = "Programar actividad",
                fontSize = 28.sp
            )
        }
        // Espacio entre el encabezado y el formulario.
        Spacer(modifier = Modifier.height(24.dp))

        // Indica al usuario qué información debe ingresar.
        Text(
            text = "¿Qué actividad quieres programar?",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        // Espacio entre la indicación y el campo de texto.
        Spacer(modifier = Modifier.height(8.dp))

        // Campo donde el usuario escribe el nombre o descripción de la actividad.
        TextField(
            value = textoTarea,
            onValueChange = { nuevoTexto ->
                // Actualiza el texto conforme el usuario escribe.
                textoTarea = nuevoTexto
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Escribe la actividad")
            },
            shape = RoundedCornerShape(8.dp),
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF0F1F8),
                focusedContainerColor = Color(0xFFF0F1F8),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )
        // Espacio entre el campo de actividad y la selección de fecha.
        Spacer(modifier = Modifier.height(16.dp))

        // Título de la sección para seleccionar la fecha.
        Text(
            text = "Fecha",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        // Espacio entre el título y el botón de selección.
        Spacer(modifier = Modifier.height(8.dp))

        // Botón que permite abrir el selector de fecha.
        OutlinedButton(
            onClick = {
                // Muestra el selector de fecha de Android.
                DatePickerDialog(
                    contexto,
                    { _, anio, mes, dia ->

                        // Crea un calendario con la fecha seleccionada.
                        val fechaSeleccionada = Calendar.getInstance()

                        // Guarda la fecha seleccionada en formato yyyy-MM-dd.
                        fechaSeleccionada.set(
                            anio,
                            mes,
                            dia
                        )

                        fechaTarea = SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).format(
                            fechaSeleccionada.time
                        )
                    },
                    // Utiliza la fecha actual como referencia inicial del selector.
                    calendario.get(Calendar.YEAR),
                    calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            // Organiza horizontalmente el icono, la fecha y la flecha.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                // Icono que representa la selección de fecha.
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Seleccionar fecha",
                    tint = androidx.compose.ui.graphics.Color(0xFF3F6FC4)
                )
                // Espacio entre el icono y el texto.
                Spacer(modifier = Modifier.width(8.dp))

                // Muestra un texto diferente según si ya se seleccionó una fecha.
                Text(
                    text = if (fechaTarea.isEmpty()) {
                        "Seleccionar fecha"

                    } else {
                        // Convierte la fecha almacenada a un formato más legible.
                        SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                        ).format(
                            SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).parse(fechaTarea)!!
                        )
                    }
                )
                // Empuja la flecha hacia el extremo derecho del botón.
                Spacer(modifier = Modifier.weight(1f))

                // Indica visualmente que el botón permite seleccionar una fecha.
                Text(
                    text = ">",
                    fontSize = 20.sp
                )
            }
        }
        // Espacio entre la selección de fecha y la selección de hora.
        Spacer(modifier = Modifier.height(12.dp))

        // Título de la sección para seleccionar la hora.
        Text(
            text = "Hora",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        // Espacio entre el título y el botón de selección.
        Spacer(modifier = Modifier.height(8.dp))

        // Botón que permite abrir el selector de hora.
        OutlinedButton(
            onClick = {
                // Muestra el selector de hora de Android.
                TimePickerDialog(
                    contexto,
                    { _, hora, minuto ->
                        // Guarda la hora seleccionada en formato HH:mm.
                        horaTarea = String.format(
                            "%02d:%02d",
                            hora,
                            minuto
                        )
                    },
                    // Utiliza la hora actual como referencia inicial.
                    calendario.get(Calendar.HOUR_OF_DAY),
                    calendario.get(Calendar.MINUTE),
                    true
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            // Organiza horizontalmente el icono, la hora y la flecha.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                // Icono que representa la selección de hora.
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Seleccionar hora",
                    tint = androidx.compose.ui.graphics.Color(0xFF3F6FC4)
                )
                // Espacio entre el icono y el texto.
                Spacer(modifier = Modifier.width(8.dp))

                // Muestra la hora seleccionada o el texto inicial.
                Text(
                    text = if (horaTarea.isEmpty()) {
                        "Seleccionar hora"
                    } else {
                        // Convierte la hora almacenada a formato de 12 horas.
                        SimpleDateFormat(
                            "hh:mm a",
                            Locale.getDefault()
                        ).format(
                            SimpleDateFormat(
                                "HH:mm",
                                Locale.getDefault()
                            ).parse(horaTarea)!!
                        )
                    }
                )
                // Empuja la flecha hacia el extremo derecho del botón.
                Spacer(modifier = Modifier.weight(1f))

                // Indica visualmente que el botón permite seleccionar una hora.
                Text(
                    text = ">",
                    fontSize = 20.sp
                )
            }
        }

        // Espacio entre la selección de hora y el botón para guardar.
        Spacer(modifier = Modifier.height(30.dp))

        // Botón que guarda la actividad programada.
        Button(
            onClick = {
                // Verifica que el título, la fecha y la hora hayan sido ingresados.
                if (
                    textoTarea.isNotBlank() &&
                    fechaTarea.isNotBlank() &&
                    horaTarea.isNotBlank()
                ) {
                    // Envía la actividad al ViewModel para guardarla en Room.
                    agendaViewModel.agregarActividad(
                        Actividad(
                            titulo = textoTarea,
                            fecha = fechaTarea,
                            hora = horaTarea
                        )
                    )
                    // Limpia los campos después de guardar la actividad.
                    textoTarea = ""
                    fechaTarea = ""
                    horaTarea = ""

                    // Regresa a la pantalla anterior después de guardar.
                    onVolver()
                }
            },
            // Define el ancho y la altura del botón.
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            // Texto mostrado dentro del botón.
            Text(
                text = "Programar actividad"
            )
        }
    }
}

