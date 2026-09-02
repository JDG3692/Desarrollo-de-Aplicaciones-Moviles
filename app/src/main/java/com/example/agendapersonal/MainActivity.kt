package com.example.agendapersonal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.agendapersonal.ui.navigation.AppNavigation
import com.example.agendapersonal.ui.theme.AgendaPersonalTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AgendaPersonalTheme {
                AppNavigation()
            }
        }
    }
}
