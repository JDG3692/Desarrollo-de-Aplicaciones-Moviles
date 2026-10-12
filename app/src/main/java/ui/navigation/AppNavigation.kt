
package com.example.agendapersonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agendapersonal.ui.screens.DiaScreen
import com.example.agendapersonal.ui.screens.ProgramarActividadScreen
import com.example.agendapersonal.ui.screens.CalendarioScreen
import com.example.agendapersonal.ui.screens.EditarActividadScreen

@Composable
fun AppNavigation(
    actividadIdParaEditar: Int? = null
) {
    val navController = rememberNavController()

    // Abre la edición cuando se recibe un ID desde una notificación.
    LaunchedEffect(actividadIdParaEditar) {
        if (actividadIdParaEditar != null && actividadIdParaEditar > 0) {
            navController.navigate("editar/$actividadIdParaEditar") {
                launchSingleTop = true
            }
        }
    }


    NavHost(
        navController = navController,
        startDestination = "dia"
    ) {
        composable("dia") {
            DiaScreen(
                onProgramarActividad = {
                    navController.navigate("programar")
                },
                onAbrirCalendario = {
                    navController.navigate("calendario")
                },
                onEditarActividad = { actividadId ->
                    navController.navigate("editar/$actividadId")
                }
            )
        }

        composable("programar") {
            ProgramarActividadScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable("calendario") {
            CalendarioScreen(
                onVolver = {
                    navController.popBackStack()
                },
                onEditarActividad = { actividadId ->
                    navController.navigate("editar/$actividadId")
                }
            )
        }

        composable("editar/{actividadId}") { backStackEntry ->
            val actividadId = backStackEntry
                .arguments
                ?.getString("actividadId")
                ?.toIntOrNull()

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
