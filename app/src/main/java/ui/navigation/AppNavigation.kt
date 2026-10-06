package com.example.agendapersonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agendapersonal.ui.screens.DiaScreen
import com.example.agendapersonal.ui.screens.ProgramarActividadScreen
import com.example.agendapersonal.ui.screens.CalendarioScreen
import com.example.agendapersonal.ui.screens.EditarActividadScreen

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
                },
                // Abre la edición de la actividad seleccionada.
                onEditarActividad = { actividadId ->
                    navController.navigate("editar/$actividadId")
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
                },
                // Abre la edición de la actividad seleccionada.
                onEditarActividad = { actividadId ->
                    navController.navigate("editar/$actividadId")
                }
            )
        }

        // Ruta para editar una actividad existente.
        // Recibe el identificador de la actividad desde la pantalla anterior.
        composable("editar/{actividadId}") { backStackEntry ->

            // Obtiene el ID de la actividad enviado mediante la navegación.
            val actividadId = backStackEntry
                .arguments
                ?.getString("actividadId")
                ?.toIntOrNull()

            // Solo abre la pantalla si el ID recibido es válido.
            if (actividadId != null) {
                EditarActividadScreen(
                    actividadId = actividadId,
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}