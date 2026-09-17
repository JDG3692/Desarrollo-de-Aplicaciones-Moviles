package com.example.agendapersonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agendapersonal.ui.screens.DiaScreen
import com.example.agendapersonal.ui.screens.ProgramarActividadScreen
import com.example.agendapersonal.ui.screens.CalendarioScreen

// Define la navegación principal de la aplicación.
// Se encarga de controlar las diferentes pantallas y el cambio entre ellas.
@Composable
fun AppNavigation() {
    // Crea el controlador encargado de gestionar la navegación.
    val navController = rememberNavController()

    // Define las rutas disponibles y la pantalla inicial de la aplicación.
    NavHost(
        navController = navController,
        startDestination = "dia"
    ) {
        // Ruta correspondiente a la pantalla principal de la agenda diaria.
        composable("dia") {
            DiaScreen(
                // Navega desde la pantalla diaria hacia Programar actividad.
                onProgramarActividad = {
                    navController.navigate("programar")
                },
                // Navega desde la pantalla diaria hacia el Calendario.
                onAbrirCalendario = {
                    navController.navigate("calendario")
                }
            )
        }
        // Ruta correspondiente a la pantalla para programar actividades.
        composable("programar") {
            ProgramarActividadScreen(
                // Regresa a la pantalla anterior de la navegación.
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
        // Ruta correspondiente a la pantalla del calendario mensual.
        composable("calendario") {
            CalendarioScreen(
                // Regresa a la pantalla anterior de la navegación.
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}