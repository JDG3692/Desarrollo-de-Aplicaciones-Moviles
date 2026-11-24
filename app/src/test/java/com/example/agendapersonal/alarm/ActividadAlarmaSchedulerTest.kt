package com.example.agendapersonal.alarm

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.example.agendapersonal.model.Actividad
import com.example.agendapersonal.util.FormatoAlmacenamiento
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowAlarmManager

@RunWith(RobolectricTestRunner::class)
class ActividadAlarmaSchedulerTest {

    private val contexto = ApplicationProvider.getApplicationContext<Context>()
    private val scheduler = ActividadAlarmaScheduler(contexto)
    private val alarmas
        get() = shadowOf(contexto.getSystemService(AlarmManager::class.java))
            .scheduledAlarms

    private val futura = Actividad(
        id = 5, titulo = "Cita", fecha = "2099-01-02", hora = "07:05"
    )

    @Before
    fun concederPermiso() = ShadowAlarmManager.setCanScheduleExactAlarms(true)

    @After
    fun restaurarPermiso() = ShadowAlarmManager.setCanScheduleExactAlarms(false)

    @Test
    fun programaLaAlarmaALaHoraDeLaActividad() {
        assertTrue(scheduler.programar(futura))

        val esperado = FormatoAlmacenamiento.fecha().parse("2099-01-02")!!.time +
                (7 * 60 + 5) * 60_000L
        assertEquals(listOf(esperado), alarmas.map { it.triggerAtMs })

        val intent = shadowOf(alarmas.single().operation).savedIntent
        assertEquals(5, intent.getIntExtra(ActividadAlarmaReceiver.EXTRA_ACTIVIDAD_ID, 0))
        assertEquals("Cita", intent.getStringExtra(ActividadAlarmaReceiver.EXTRA_TITULO))
    }

    @Test
    fun reprogramarReemplazaLaAlarmaAnterior() {
        scheduler.programar(futura)
        scheduler.programar(futura.copy(hora = "09:00"))

        assertEquals(1, alarmas.size)
    }

    @Test
    fun noProgramaEnElPasadoYCancelaLaAnterior() {
        scheduler.programar(futura)

        assertFalse(scheduler.programar(futura.copy(fecha = "1969-01-01")))
        assertTrue(alarmas.isEmpty())
    }

    @Test
    fun cancelaLaAlarmaDeUnaActividadCompletada() {
        scheduler.programar(futura)

        assertTrue(scheduler.programar(futura.copy(completada = true)))
        assertTrue(alarmas.isEmpty())
    }

    @Test
    fun sinPermisoDevuelveFalseYNoPrograma() {
        ShadowAlarmManager.setCanScheduleExactAlarms(false)

        assertFalse(scheduler.programar(futura))
        assertTrue(alarmas.isEmpty())
    }

    @Test
    fun conFechaInvalidaDevuelveFalse() {
        assertFalse(scheduler.programar(futura.copy(fecha = "2099-13-40")))
        assertTrue(alarmas.isEmpty())
    }

    @Test
    fun elManifiestoRegistraElCambioDePermiso() {
        val receptores = contexto.packageManager.queryBroadcastReceivers(
            Intent(AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED)
                .setPackage(contexto.packageName),
            0
        )

        assertEquals(
            listOf(ActividadAlarmaBootReceiver::class.java.name),
            receptores.map { it.activityInfo.name }
        )
    }
}
