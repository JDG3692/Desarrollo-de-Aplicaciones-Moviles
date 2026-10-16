# 📅 Agenda Personal

## 1. Descripción del proyecto

Desarrollé **Agenda Personal** como una aplicación móvil para Android que me permite organizar actividades personales, académicas y laborales desde un solo lugar. La aplicación integra una vista diaria, programación de actividades futuras, calendario mensual, edición y eliminación de registros, estados de cumplimiento y recordatorios por medio de alarmas y notificaciones.

Las actividades se guardan localmente con **Room**, por lo que permanecen almacenadas en la base de datos del dispositivo. La aplicación no requiere una cuenta ni un servicio en la nube para administrar los registros.

## 2. Exposición del problema y propósito

Organizar compromisos personales, académicos y laborales puede resultar difícil cuando las actividades se anotan en distintos lugares o no se revisan oportunamente. Esto puede dificultar la consulta de las tareas de un día, el seguimiento de las que todavía están pendientes y el recuerdo de aquellas que deben realizarse a una hora específica.

A partir de esa necesidad, desarrollé Agenda Personal para centralizar el registro y la consulta de actividades en una aplicación móvil. La solución permite revisar actividades por día o por calendario mensual, programar compromisos futuros, identificar si se completaron y recibir notificaciones a la hora establecida. El almacenamiento local permite conservar los registros en el dispositivo sin depender de una conexión a internet o de una cuenta externa.

El alcance actual está orientado a una persona que administra su propia agenda. No incluye cuentas compartidas, administración de múltiples usuarios ni sincronización en la nube.

## 3. Objetivos del proyecto

Mi objetivo general es desarrollar una aplicación Android sencilla que permita registrar, consultar y dar seguimiento a actividades mediante almacenamiento local y recordatorios.

Para cumplir ese objetivo, implementé:

- La consulta diaria y la navegación entre fechas.
- El registro rápido de actividades para el día consultado.
- La programación de actividades en fechas futuras.
- La consulta de actividades desde un calendario mensual.
- La edición de actividades que aún no están completadas.
- Los estados completada y no completada.
- La eliminación individual y por diferentes conjuntos de actividades o periodos.
- La persistencia local y los recordatorios mediante alarmas y notificaciones.

## 4. Plataforma y tecnologías utilizadas

- **Plataforma:** Android.
- **Lenguaje:** Kotlin.
- **Entorno de desarrollo:** Android Studio.
- **Interfaz:** Jetpack Compose y Material 3.
- **Persistencia local:** Room.
- **Estado y operaciones de datos:** ViewModel, StateFlow y corrutinas.
- **Navegación entre pantallas:** Navigation Compose.
- **Recordatorios:** AlarmManager, BroadcastReceiver, notificaciones de Android y un servicio para el sonido personalizado.

Mantengo el alcance del proyecto en el dispositivo. No he implementado cuentas de usuario, sincronización entre dispositivos ni almacenamiento en la nube.

## 5. Interfaz de usuario e interfaz de administrador

### 5.1 Interfaz de usuario

La interfaz principal está destinada a la persona que utiliza la agenda. Desde las pantallas disponibles puedo consultar, crear, editar, completar o eliminar actividades, revisar el calendario mensual y administrar el recordatorio asociado a cada registro.

La interfaz se organiza en cuatro pantallas principales:

#### Día / Inicio

En esta pantalla consulto las actividades de la fecha seleccionada y puedo avanzar o retroceder entre días. También puedo crear una actividad rápida para el día que estoy consultando, seleccionar su hora y abrir las pantallas de programación y calendario.

Cada actividad muestra su hora, título y estado. Las actividades completadas se identifican con el texto correspondiente y un check verde; las marcadas como no completadas muestran ese estado y una X roja. Las actividades pendientes muestran un icono de alarma que permite activar o desactivar el sonido personalizado del recordatorio.

La papelera abre opciones para eliminar actividades completadas o seleccionar actividades y periodos para eliminar. Las operaciones de eliminación que requieren confirmación muestran un diálogo antes de ejecutarse.

#### Programar actividad

En esta pantalla puedo escribir el título de una actividad, escoger una fecha futura y elegir la hora. No permito seleccionar el día actual ni fechas anteriores desde este formulario. Al guardar, la actividad se almacena en Room y la aplicación regresa a la pantalla anterior.

#### Calendario

Muestro el calendario mensual con un punto debajo de los días que tienen actividades. Al seleccionar un día, consulto la lista de actividades de esa fecha ordenada por hora. Las tarjetas reflejan si una actividad está pendiente, completada o marcada como no completada; cuando corresponde, también muestran el icono para activar o desactivar la alarma.

#### Editar actividad

Desde una actividad no completada puedo abrir esta pantalla para modificar el título, la fecha y la hora, guardar los cambios o eliminarla. Valido las fechas para impedir fechas pasadas y, cuando la actividad queda para hoy, impido guardar una hora que ya pasó. Antes de eliminar una actividad individual, muestro una confirmación.

Las actividades completadas están protegidas contra edición desde las pantallas de consulta.

### 5.2 Interfaz de administrador

El alcance actual de Agenda Personal **no contempla una interfaz de administrador independiente**. La aplicación es de uso individual y las operaciones de administración de la agenda —como modificar estados, editar y eliminar registros— se realizan desde la propia interfaz de usuario. No he implementado un panel administrativo, gestión de roles, administración de cuentas ni herramientas para supervisar a otros usuarios.

## 6. Funcionalidad implementada

### Gestión de actividades

Puedo registrar actividades, consultar las correspondientes a una fecha, programar actividades futuras, editar registros no completados y marcar una actividad como completada o no completada. También incorporé opciones para eliminar actividades individuales y grupos de actividades según las opciones disponibles en la aplicación.

### Alarmas, sonido y notificaciones

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

## 8. Diseño de la interfaz y wireframes anotados

Preparé cuatro láminas anotadas para documentar las pantallas principales. Usé una paleta azul grisácea coherente con la interfaz y agregué indicadores que explican la función de los controles. Los wireframes sirven como referencia visual del flujo y de la distribución de los elementos de cada pantalla.

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
- No he implementado sincronización, inicio de sesión, actividades recurrentes, estadísticas, copias de seguridad en la nube ni un panel administrativo independiente.

## 10. Pruebas realizadas

Durante el desarrollo comprobé los flujos de creación, persistencia, edición, completado y eliminación de actividades; el funcionamiento de alarmas y notificaciones con el sonido personalizado activado y desactivado; las acciones disponibles en las notificaciones; la cancelación de alarmas; y la restauración después de reiniciar el emulador. En una prueba de reinicio con dos alarmas pendientes, comprobé que ambas generaron nuevamente sus recordatorios sin abrir la aplicación manualmente.

Estas pruebas corresponden a las comprobaciones realizadas durante el desarrollo; no representan una certificación en todos los modelos de dispositivo ni en todas las versiones de Android.

## 11. Registro de cambios (Changelog)

Registro resumido de hitos documentados en el historial de commits del repositorio. Las fechas corresponden a las fechas de los commits de GitHub; cada enlace permite consultar el detalle del cambio.

| Fecha | Cambio |
|---|---|
| 2026-08-26 | Incorporé el borrador inicial del proyecto y documenté las vistas diaria y mensual. |
| 2026-08-26 | Simplifiqué el flujo de registro y programación de actividades y actualicé la documentación del flujo principal. |
| 2026-09-01 | Actualicé el enfoque y la descripción del proyecto en el README. |
| 2026-10-12 | Implementé alarmas y notificaciones para las actividades. |
| 2026-10-12 | Añadí acciones a las notificaciones de alarmas. |
| 2026-10-13 | Completé la implementación de los botones de acción de las notificaciones. |
| 2026-10-14 | Trabajé en el sonido personalizado, el comportamiento de alarmas en segundo plano, la cancelación de alarmas y las acciones de completar, no completar y reprogramar. |
| 2026-10-14 | Probé los flujos de eliminación de registros y corregí el comportamiento de la reprogramación de actividades no completadas. |
| 2026-10-15 | Probé la restauración de alarmas después del reinicio y actualicé el README con las funcionalidades y pruebas realizadas. |
| 2026-10-15 | Corregí un detalle visual en el calendario relacionado con actividades no completadas y actualicé los wireframes documentados. |
| 2026-10-16 | Incorporé las cuatro láminas de wireframes anotados al README y revisé la documentación en primera persona, incluyendo los detalles de alarmas y notificaciones. |

## 12. Historial general del desarrollo

- Implementé las pantallas Día / Inicio, Programar actividad, Calendario y Editar actividad.
- Incorporé la persistencia local con Room.
- Añadí la validación de fechas y horas, estados de actividad y opciones de eliminación.
- Integré las alarmas, las notificaciones con acciones y el sonido personalizado opcional.
- Añadí la restauración de alarmas pendientes ante los eventos de sistema registrados.
- Preparé wireframes anotados para documentar las cuatro pantallas principales.
