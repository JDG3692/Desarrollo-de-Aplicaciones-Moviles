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


@Composable
fun DiaScreen(
    agendaViewModel: AgendaViewModel = viewModel()
) {

    var textoTarea by remember { mutableStateOf("") }
    var horaTarea by remember { mutableStateOf("") }
    var mostrarSelectorHora by remember { mutableStateOf(false) }
    var actividadesConSonido by remember { mutableStateOf(setOf<Int>()) }
    var actividadesConNotificacion by remember { mutableStateOf(setOf<Int>()) }

    val calendarioActual = Calendar.getInstance()

    var fechaConsultada by remember {
        mutableStateOf(
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(calendarioActual.time)
        )
    }
    val fechaHoy = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault()
    ).format(Date())

    val actividades by agendaViewModel.actividades.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Text(
            text = "Bienvenido a tu",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Agenda Personal",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (fechaConsultada == fechaHoy) {
            Text(
                text = "Hoy es",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    val calendario = Calendar.getInstance()
                    calendario.time = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).parse(fechaConsultada)!!

                    calendario.add(Calendar.DAY_OF_MONTH, -1)

                    fechaConsultada = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(calendario.time)
                }
            ) {
                Text(
                    text = "‹",
                    fontSize = 24.sp
                )
            }

            Card(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = SimpleDateFormat(
                        "EEEE, d 'de' MMMM 'de' yyyy",
                        Locale("es", "CO")
                    ).format(
                        SimpleDateFormat(
                            "yyyy-MM-dd",
                            Locale.getDefault()
                        ).parse(fechaConsultada)!!
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

            IconButton(
                onClick = {
                    val calendario = Calendar.getInstance()
                    calendario.time = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).parse(fechaConsultada)!!

                    calendario.add(Calendar.DAY_OF_MONTH, 1)

                    fechaConsultada = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(calendario.time)
                }
            ) {
                Text(
                    text = "›",
                    fontSize = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (fechaConsultada == fechaHoy) {

        Text(
            text = "Nueva actividad",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = textoTarea,
            onValueChange = { nuevoTexto ->
                textoTarea = nuevoTexto
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("¿Qué tienes que hacer?")
            },
            shape = RoundedCornerShape(8.dp),
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF0F1F8),
                focusedContainerColor = Color(0xFFF0F1F8),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

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

                Text(
                    text = "◷",
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

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

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (textoTarea.isNotBlank() && horaTarea.isNotBlank()) {
                    agendaViewModel.agregarActividad(
                        Actividad(
                            id = actividades.size + 1,
                            titulo = textoTarea,
                            fecha = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(Date()),
                            hora = horaTarea
                        )
                    )

                    textoTarea = ""
                    horaTarea = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Agregar Actividad",
                fontSize = 14.sp
            )
        }

        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (fechaConsultada == fechaHoy) {
                "Actividades para hoy"
            } else {
                "Actividades para este día"
            },
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        actividades
            .filter { actividad ->
                if (fechaConsultada < fechaHoy) {
                    actividad.fecha == fechaConsultada && actividad.completada
                } else {
                    actividad.fecha == fechaConsultada && !actividad.completada
                }
            }
            .sortedBy { it.hora }
            .forEach { actividad ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Checkbox(
                            checked = actividad.completada,
                            onCheckedChange = {
                                agendaViewModel.completarActividad(actividad.id)
                            }
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

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
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = actividad.titulo,
                                fontSize = 14.sp
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                actividadesConSonido =
                                    if (actividad.id in actividadesConSonido) {
                                        actividadesConSonido - actividad.id
                                    } else {
                                        actividadesConSonido + actividad.id
                                    }
                            },
                            modifier = Modifier
                                .height(40.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = if (actividad.id in actividadesConSonido) {
                                    "🔊"
                                } else {
                                    "🔇"
                                },
                                color = if (actividad.id in actividadesConSonido) {
                                    Color(0xFF3F6FC4)
                                } else {
                                    Color.Gray
                                }
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        OutlinedButton(
                            onClick = {
                                actividadesConNotificacion =
                                    if (actividad.id in actividadesConNotificacion) {
                                        actividadesConNotificacion - actividad.id
                                    } else {
                                        actividadesConNotificacion + actividad.id
                                    }
                            },
                            modifier = Modifier
                                .height(40.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = if (actividad.id in actividadesConNotificacion) {
                                    "🔔"
                                } else {
                                    "🔕"
                                },
                                color = if (actividad.id in actividadesConNotificacion) {
                                    Color(0xFF3F6FC4)
                                } else {
                                    Color.Gray
                                }
                            )
                        }
                    }
                }
            }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = {
                    // Más adelante abrirá Programar actividad
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Programar\nactividad",
                    fontSize = 12.sp
                )
            }

            Button(
                onClick = {
                    // Más adelante abrirá Calendario
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Calendario",
                    fontSize = 12.sp
                )
            }
        }
    }
}