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

@Composable
fun DiaScreen(
    agendaViewModel: AgendaViewModel = viewModel()
) {

    var textoTarea by remember { mutableStateOf("") }

    val actividades by agendaViewModel.actividades.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text("Agenda Personal")

        Text("Organiza tu día")

        TextField(
            value = textoTarea,
            onValueChange = { nuevoTexto ->
                textoTarea = nuevoTexto
            }
        )

        Button(
            onClick = {
                if (textoTarea.isNotBlank()) {
                    agendaViewModel.agregarActividad(
                        Actividad(
                            id = actividades.size + 1,
                            titulo = textoTarea,
                            fecha = "Hoy",
                            hora = "00:00"
                        )
                    )

                    textoTarea = ""
                }
            }
        ) {
            Text("Agregar tarea")
        }
        actividades.forEach { actividad ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = actividad.completada,
                    onCheckedChange = {
                        agendaViewModel.completarActividad(actividad.id)
                    }
                )

                Text(
                    text = "${actividad.hora} - ${actividad.titulo}",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}