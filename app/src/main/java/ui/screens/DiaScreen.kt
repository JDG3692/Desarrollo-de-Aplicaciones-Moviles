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


@Composable
fun DiaScreen(
    agendaViewModel: AgendaViewModel = viewModel()
) {

    var textoTarea by remember { mutableStateOf("") }
    var horaTarea by remember { mutableStateOf("") }
    var mostrarSelectorHora by remember { mutableStateOf(false) }

    val actividades by agendaViewModel.actividades.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
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

        Text(
            text = "Hoy es",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = SimpleDateFormat(
                "EEEE, d 'de' MMMM 'de' yyyy",
                Locale("es", "CO")
            ).format(Date()),
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

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
            label = {
                Text("¿Qué tienes que hacer?")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Hora",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                mostrarSelectorHora = true
            }
        ) {
            Text(
                text = if (horaTarea.isEmpty()) {
                    "Seleccionar hora"
                } else {
                    "Hora: $horaTarea"
                }
            )
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
        Button(
            onClick = {
                if (textoTarea.isNotBlank() && horaTarea.isNotBlank()) {
                    agendaViewModel.agregarActividad(
                        Actividad(
                            id = actividades.size + 1,
                            titulo = textoTarea,
                            fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                            hora = horaTarea
                        )
                    )

                    textoTarea = ""
                    horaTarea = ""
                }
            }
        ) {
            Text("Agregar Actividad")
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Actividades para hoy",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        actividades
            .filter { !it.completada }
            .sortedBy { it.hora }
            .forEach { actividad ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {

                    Checkbox(
                        checked = actividad.completada,
                        onCheckedChange = {
                            agendaViewModel.completarActividad(actividad.id)
                        }
                    )

                    Column(
                        modifier = Modifier.padding(start = 8.dp)
                    ) {

                        Text(
                            text = try {
                                SimpleDateFormat("hh:mm a", Locale.getDefault()).format(
                                    SimpleDateFormat("HH:mm", Locale.getDefault()).parse(actividad.hora)!!
                                )
                            } catch (e: Exception) {
                                actividad.hora
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = actividad.titulo,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}