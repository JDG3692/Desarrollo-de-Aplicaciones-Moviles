package com.example.agendapersonal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendapersonal.ui.theme.AgendaPersonalTheme
import androidx.compose.runtime.mutableStateListOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgendaPersonalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(

                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting( modifier: Modifier = Modifier) {

    var tareaAgregada by remember { mutableStateOf(false) }
    var textoTarea by remember { mutableStateOf("") }
    val tareas = remember { mutableStateListOf<String>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Agenda Personal",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Text("Organiza tu día")

        TextField(
            value = textoTarea,
            onValueChange = { nuevoTexto ->
                textoTarea = nuevoTexto }
        )

        Button(
            onClick = {
               if((textoTarea.isNotBlank()))
                   tareas.add(textoTarea)
                    textoTarea = ""
                    tareaAgregada = true
            }

        ) {
            Text("Agregar tarea")
        }

        tareas.forEach { tarea ->
            Text(
                text = "• $tarea",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AgendaPersonalTheme {
        Greeting()
    }
}