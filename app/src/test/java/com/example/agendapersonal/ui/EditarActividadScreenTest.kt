package com.example.agendapersonal.ui

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextReplacement
import androidx.test.core.app.ApplicationProvider
import com.example.agendapersonal.model.Actividad
import com.example.agendapersonal.model.AgendaDatabase
import com.example.agendapersonal.reiniciarBaseDeDatos
import com.example.agendapersonal.ui.screens.EditarActividadScreen
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
class EditarActividadScreenTest {

    @get:Rule
    val regla = createAndroidComposeRule<ComponentActivity>()

    private var actividadId by mutableStateOf(1)
    private val campo = hasSetTextAction()

    @Before
    fun guardarActividades() = runBlocking {
        reiniciarBaseDeDatos()
        val dao = AgendaDatabase
            .obtenerBaseDeDatos(ApplicationProvider.getApplicationContext<Context>())
            .actividadDao()
        dao.insertarActividad(Actividad(1, "Primera", "2099-01-01", "08:00"))
        dao.insertarActividad(Actividad(2, "Segunda", "2099-01-02", "09:00"))
        Unit
    }

    @After
    fun limpiar() = reiniciarBaseDeDatos()

    private fun mostrar(): StateRestorationTester {
        val tester = StateRestorationTester(regla)
        tester.setContent {
            EditarActividadScreen(actividadId = actividadId, onVolver = {})
        }
        regla.waitUntilAtLeastOneExists(campo and hasText("Primera"), 5_000)
        return tester
    }

    @Test
    fun conservaLoEscritoAlRecrearLaPantalla() {
        val tester = mostrar()
        regla.onNode(campo).performTextReplacement("Cambio sin guardar")

        tester.emulateSavedInstanceStateRestore()

        regla.onNodeWithText("Cambio sin guardar").assertExists()
    }

    // Una notificación reutiliza la pantalla con otra actividad. Si no cargara
    // sus datos, Guardar los reemplazaría por los de la anterior.
    @Test
    fun alCambiarDeActividadCargaLosDatosDeLaNueva() {
        mostrar()
        regla.onNode(campo).performTextReplacement("Cambio sin guardar")

        regla.runOnIdle { actividadId = 2 }

        regla.waitUntilAtLeastOneExists(campo and hasText("Segunda"), 5_000)
        regla.onNodeWithText("Cambio sin guardar").assertDoesNotExist()
    }
}
