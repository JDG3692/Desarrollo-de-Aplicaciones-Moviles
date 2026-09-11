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

@Composable
fun ProgramarActividadScreen(
    agendaViewModel: AgendaViewModel = viewModel(),
    onVolver: () -> Unit = {}
) {

    var textoTarea by remember {
        mutableStateOf("")
    }

    var fechaTarea by remember {
        mutableStateOf("")
    }

    var horaTarea by remember {
        mutableStateOf("")
    }

    val contexto = LocalContext.current
    val calendario = Calendar.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {


        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    onVolver()
                }
            ) {
                Text(
                    text = "←",
                    fontSize = 28.sp
                )
            }

            Text(
                text = "Programar actividad",
                fontSize = 28.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¿Qué tienes que hacer?",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = textoTarea,
            onValueChange = {
                textoTarea = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Escribe la actividad")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Fecha",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {

                DatePickerDialog(
                    contexto,
                    { _, anio, mes, dia ->

                        val fechaSeleccionada = Calendar.getInstance()

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
                    calendario.get(Calendar.YEAR),
                    calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {

                Text(
                    text = "📅",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (fechaTarea.isEmpty()) {
                        "Seleccionar fecha"
                    } else {
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

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = ">",
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Hora",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {

                TimePickerDialog(
                    contexto,
                    { _, hora, minuto ->

                        horaTarea = String.format(
                            "%02d:%02d",
                            hora,
                            minuto
                        )
                    },
                    calendario.get(Calendar.HOUR_OF_DAY),
                    calendario.get(Calendar.MINUTE),
                    true
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {

                Text(
                    text = "🕐",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (horaTarea.isEmpty()) {
                        "Seleccionar hora"
                    } else {
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

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = ">",
                    fontSize = 20.sp
                )
            }
        }


    Spacer(modifier = Modifier.height(50.dp))

    Button(
        onClick = {

            if (
                textoTarea.isNotBlank() &&
                fechaTarea.isNotBlank() &&
                horaTarea.isNotBlank()
            ) {

                agendaViewModel.agregarActividad(
                    Actividad(
                        titulo = textoTarea,
                        fecha = fechaTarea,
                        hora = horaTarea
                    )
                )

                textoTarea = ""
                fechaTarea = ""
                horaTarea = ""

                onVolver()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {

        Text(
            text = "Programar actividad"
        )
    }
}
}

