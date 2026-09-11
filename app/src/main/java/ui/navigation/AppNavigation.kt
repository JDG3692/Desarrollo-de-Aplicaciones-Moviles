package com.example.agendapersonal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agendapersonal.ui.screens.DiaScreen
import com.example.agendapersonal.ui.screens.ProgramarActividadScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "dia"
    ) {
        composable("dia") {
            DiaScreen(
                onProgramarActividad = {
                    navController.navigate("programar")
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
    }
}