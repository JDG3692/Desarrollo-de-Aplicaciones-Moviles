# 📅 Agenda Personal

## 1. Descripción del proyecto

Desarrollé **Agenda Personal** como una aplicación móvil para Android que me permite organizar actividades personales, académicas y laborales. Implementé una pantalla diaria, programación de actividades futuras, un calendario mensual, edición y eliminación de actividades, estados de cumplimiento y recordatorios mediante alarmas y notificaciones.

Guardo las actividades localmente con **Room**, de modo que permanecen en la base de datos del dispositivo. La aplicación no necesita una cuenta ni un servicio en la nube para administrar las actividades.

## 2. Problema y propósito

Busqué reunir en una sola aplicación el registro y seguimiento de actividades que se distribuyen durante el día y en fechas futuras. La agenda permite consultar qué tengo programado, reconocer el estado de cada actividad y recibir un recordatorio a la hora indicada.

## 3. Objetivos del proyecto

Mi objetivo general es desarrollar una aplicación Android sencilla para registrar, consultar y dar seguimiento a actividades con almacenamiento local y recordatorios.

Para cumplir ese objetivo, implementé:

- La consulta diaria y la navegación entre fechas.
- El registro rápido de actividades para el día consultado.
- La programación de actividades en fechas futuras.
- La consulta de actividades desde un calendario mensual.
- La edición de actividades que aún no están completadas.
- Los estados completada y no completada.
- La eliminación individual y por diferentes conjuntos de actividades o periodos.
- La persistencia local y los recordatorios mediante alarmas y notificaciones.

## 4. Tecnologías utilizadas

- **Plataforma:** Android.
- **Lenguaje:** Kotlin.
- **Entorno de desarrollo:** Android Studio.
- **Interfaz:** Jetpack Compose y Material 3.
- **Persistencia local:** Room.
- **Estado y operaciones de datos:** ViewModel, StateFlow y corrutinas.
- **Navegación entre pantallas:** Navigation Compose.
- **Recordatorios:** AlarmManager, BroadcastReceiver, notificaciones de Android y un servicio para el sonido personalizado.

Mantengo el alcance del proyecto en el dispositivo. No he implementado cuentas de usuario, sincronización entre dispositivos ni almacenamiento en la nube.

## 5. Pantallas principales

### Día / Inicio

En esta pantalla consulto las actividades de la fecha seleccionada y puedo avanzar o retroceder entre días. También puedo crear una actividad rápida para el día que estoy consultando, seleccionar su hora y abrir las pantallas de programación y calendario.

Cada actividad muestra su hora, título y estado. Las actividades completadas se identifican con el texto correspondiente y un check verde; las marcadas como no completadas muestran ese estado y una X roja. Las actividades pendientes muestran un icono de alarma que permite activar o desactivar el sonido personalizado del recordatorio.

La papelera abre opciones para eliminar actividades completadas o seleccionar actividades y periodos para eliminar. Las operaciones de eliminación que requieren confirmación muestran un diálogo antes de ejecutarse.

### Programar actividad

En esta pantalla puedo escribir el título de una actividad, escoger una fecha futura y elegir la hora. No permito seleccionar el día actual ni fechas anteriores desde este formulario. Al guardar, la actividad se almacena en Room y la aplicación regresa a la pantalla anterior.

### Calendario

Muestro el calendario mensual con un punto debajo de los días que tienen actividades. Al seleccionar un día, consulto la lista de actividades de esa fecha ordenada por hora. Las tarjetas reflejan si una actividad está pendiente, completada o marcada como no completada; cuando corresponde, también muestran el icono para activar o desactivar la alarma.

### Editar actividad

Desde una actividad no completada puedo abrir esta pantalla para modificar el título, la fecha y la hora, guardar los cambios o eliminarla. Valido las fechas para impedir fechas pasadas y, cuando la actividad queda para hoy, impido guardar una hora que ya pasó. Antes de eliminar una actividad individual, muestro una confirmación.

Las actividades completadas están protegidas contra edición desde las pantallas de consulta.

## 6. Alarmas, sonido y notificaciones

Implementé recordatorios asociados a la fecha y hora de cada actividad. Cuando llega la hora programada, Android muestra una notificación con el título de la actividad y estas acciones:

- **Completar:** marca la actividad como completada, cancela su alarma y descarta la notificación.
- **Reprogramar:** abre Agenda Personal directamente en la pantalla de edición de esa actividad. Desde allí puedo cambiar la fecha o la hora y guardar los cambios; la acción de la notificación no cambia la fecha por sí sola.
- **No completar:** guarda el estado de no completada, desactiva el sonido personalizado para esa actividad, cancela su alarma pendiente y descarta la notificación.

En las tarjetas de las pantallas Día y Calendario, el botón con el icono de alarma activa o desactiva el **sonido personalizado** que se reproduce cuando vence la actividad. No equivale a cancelar el recordatorio: mientras la actividad siga pendiente y tenga una hora futura válida, la notificación puede aparecer aunque el sonido personalizado esté desactivado. La notificación usa el sonido predeterminado del sistema.

También incorporé un servicio que reproduce y detiene el sonido personalizado y un receptor que vuelve a programar las alarmas pendientes después del reinicio del dispositivo y cuando cambia la fecha/hora del sistema o la zona horaria, según los eventos registrados por Android. Comprobé en el emulador la restauración después de reiniciar con dos alarmas pendientes; ambas volvieron a generar sus recordatorios sin tener que abrir manualmente la aplicación.

El funcionamiento de alarmas y notificaciones depende de los permisos de Android y de las restricciones de la versión del sistema y del fabricante. La aplicación solicita permiso para notificaciones en Android 13 o superior y dirige a los ajustes del sistema para conceder el acceso a alarmas exactas cuando hace falta.

## 7. Persistencia y componentes principales

Uso Room para guardar las actividades localmente. Los principales componentes que desarrollé son:

- **Actividad.kt:** representa una actividad, sus datos y sus estados.
- **ActividadDao.kt:** contiene las consultas y operaciones sobre las actividades.
- **AgendaDatabase.kt:** configura Room y la base de datos.
- **AgendaViewModel.kt:** conecta los datos almacenados con la interfaz y coordina las operaciones.
- **ActividadAlarmaScheduler.kt:** programa y cancela las alarmas.
- **ActividadAlarmaReceiver.kt:** presenta la notificación cuando llega la hora de una actividad.
- **ActividadAlarmaAccionReceiver.kt:** procesa las acciones Completar y No completar de la notificación.
- **ActividadAlarmaSonidoService.kt:** administra la reproducción del sonido personalizado.
- **ActividadAlarmaBootReceiver.kt:** vuelve a programar alarmas pendientes en respuesta a los eventos de sistema registrados.

## 8. Wireframes anotados

Preparé cuatro láminas anotadas para documentar las pantallas principales. Usé una paleta azul grisácea coherente con la interfaz y agregué indicadores que explican la función de los controles.

### Wireframe 01 — Día / Inicio

![Wireframe 01 — Día / Inicio](docs/wireframes/Wireframe%20de%20Agenda%20Personal.png)

### Wireframe 02 — Programar actividad

![Wireframe 02 — Programar actividad](docs/wireframes/Wireframe%2002_%20Programar%20actividad.png)

### Wireframe 03 — Calendario

![Wireframe 03 — Calendario](docs/wireframes/Wireframe%20del%20calendario%20mensual.png)

### Wireframe 04 — Editar actividad

![Wireframe 04 — Editar actividad](docs/wireframes/Gu%C3%ADa%20wireframe_%20Editar%20actividad.png)

## 9. Validaciones y límites actuales

- Las actividades se guardan en la base de datos local del dispositivo.
- No permito editar una actividad que ya esté completada.
- Las actividades vencidas sin completar se identifican como no completadas.
- El registro rápido desde Día usa la fecha que tengo seleccionada.
- El formulario Programar actividad está destinado a fechas futuras.
- Al editar una actividad para hoy, no permito guardar una hora pasada.
- La eliminación individual incluye una confirmación y las opciones de eliminación por periodo muestran pasos de confirmación cuando corresponde.
- No he implementado sincronización, inicio de sesión, actividades recurrentes, estadísticas ni copias de seguridad en la nube.

## 10. Pruebas realizadas

Durante el desarrollo comprobé los flujos de creación, persistencia, edición, completado y eliminación de actividades; el funcionamiento de alarmas y notificaciones con el sonido personalizado activado y desactivado; las acciones disponibles en las notificaciones; la cancelación de alarmas; y la restauración después de reiniciar el emulador. En una prueba de reinicio con dos alarmas pendientes, comprobé que ambas generaron nuevamente sus recordatorios sin abrir la aplicación manualmente.

Estas pruebas corresponden a las comprobaciones realizadas durante el desarrollo; no representan una certificación en todos los modelos de dispositivo ni en todas las versiones de Android.

## 11. Historial general del desarrollo

- Implementé las pantallas Día / Inicio, Programar actividad, Calendario y Editar actividad.
- Incorporé la persistencia local con Room.
- Añadí la validación de fechas y horas, estados de actividad y opciones de eliminación.
- Integré las alarmas, las notificaciones con acciones y el sonido personalizado opcional.
- Añadí la restauración de alarmas pendientes ante los eventos de sistema registrados.
- Preparé wireframes anotados para documentar las cuatro pantallas principales.
