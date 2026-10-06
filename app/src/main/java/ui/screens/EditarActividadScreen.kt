package com.example.agendapersonal.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agendapersonal.model.Actividad
import com.example.agendapersonal.viewmodel.AgendaViewModel

// Pantalla encargada de editar una actividad existente.
// Permite modificar título, fecha y hora, o eliminar la actividad.
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
            Text("Cargando actividad...")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Editar actividad"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo para modificar el título de la actividad.
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para modificar la fecha de la actividad.
        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para modificar la hora de la actividad.
        OutlinedTextField(
            value = hora,
            onValueChange = { hora = it },
            label = { Text("Hora") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón para guardar los cambios realizados.
        Button(
            onClick = {
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
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar cambios")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón para iniciar el proceso de eliminación.
        OutlinedButton(
            onClick = {
                mostrarDialogoEliminar = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar actividad")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón para cancelar la edición y volver sin guardar.
        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }

    // Muestra una confirmación antes de eliminar definitivamente la actividad.
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = {
                mostrarDialogoEliminar = false
            },
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
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}