# 📅 Agenda Personal

## 1. Descripción del proyecto

**Agenda Personal** es una aplicación móvil para Android que permite organizar actividades y compromisos laborales, personales y académicos. Incluye consulta diaria, programación de actividades futuras, calendario mensual, edición y eliminación de actividades, estados de cumplimiento y recordatorios mediante alarmas y notificaciones.

Las actividades se guardan localmente con **Room**, por lo que permanecen almacenadas en la base de datos del dispositivo. El proyecto está diseñado para funcionar sin depender de una cuenta ni de un servicio externo.

## 2. Problema que aborda

Las tareas y compromisos se distribuyen durante el día, la semana y el mes. Depender únicamente de la memoria o de distintos medios de registro dificulta mantenerlos organizados y recordar cuándo deben realizarse. Agenda Personal busca ofrecer una forma directa de registrar, consultar y seguir actividades desde una aplicación móvil sencilla.

## 3. Objetivos

### Objetivo general

Desarrollar una aplicación Android que facilite la organización y el seguimiento de actividades mediante una vista diaria, un calendario mensual, almacenamiento local y recordatorios.

### Objetivos específicos

- Consultar actividades correspondientes a una fecha y navegar entre días.
- Registrar actividades para el día consultado y programarlas para fechas futuras.
- Consultar actividades mediante un calendario mensual.
- Editar actividades que no estén completadas.
- Completar, marcar como no completadas y eliminar actividades.
- Mantener las actividades ordenadas por fecha y hora.
- Conservar los datos localmente.
- Avisar al usuario mediante alarmas y notificaciones cuando llega la hora programada.

## 4. Tecnologías

- **Plataforma:** Android.
- **Lenguaje:** Kotlin.
- **Entorno:** Android Studio.
- **Interfaz:** Jetpack Compose y Material 3.
- **Persistencia:** Room.
- **Arquitectura de estado:** ViewModel, StateFlow y corrutinas.
- **Navegación:** Navigation Compose.
- **Recordatorios:** AlarmManager, BroadcastReceiver, notificaciones de Android y un servicio para el sonido personalizado.

La aplicación funciona de forma local. Sincronización entre dispositivos, cuentas de usuario y servicios en la nube quedan fuera del alcance actual.

## 5. Pantallas y comportamiento

### Día

Permite consultar las actividades de la fecha seleccionada, navegar al día anterior o siguiente y registrar rápidamente una actividad para el día consultado. Las actividades se muestran en orden cronológico y reflejan su estado. Las actividades completadas no se pueden editar ni reprogramar.

### Programar actividad

Permite registrar una actividad mediante título, fecha y hora. La programación de actividades nuevas desde esta pantalla comienza mañana; no se admiten fechas pasadas ni el día actual.

### Calendario

Presenta una vista mensual con indicadores en los días que contienen actividades. Al seleccionar una fecha, se consultan las actividades correspondientes ordenadas por hora.

### Editar actividad

Permite modificar el título, la fecha y la hora de una actividad no completada. No admite fechas pasadas y, para el día actual, no permite una hora pasada. Las actividades completadas están protegidas contra edición y reprogramación.

### Alarmas y notificaciones

La aplicación programa alarmas para las actividades y muestra una notificación cuando llega la hora. La notificación incluye las acciones **Completar**, **Reprogramar** y **No completar**. El sonido predeterminado de la notificación es independiente del sonido personalizado opcional.

Las alarmas se cancelan cuando corresponde, por ejemplo al completar o eliminar una actividad. Un receptor de arranque recupera de Room las actividades pendientes y vuelve a programar sus alarmas después del reinicio del dispositivo y ante cambios de hora o zona horaria. La restauración después del reinicio se comprobó en el emulador con dos alarmas pendientes.

> **Permisos:** Android puede solicitar permiso para mostrar notificaciones y acceso para programar alarmas exactas. El funcionamiento de los recordatorios depende de que los permisos requeridos estén concedidos y de las restricciones del sistema operativo y del fabricante.

## 6. Funcionalidades implementadas

| Funcionalidad | Estado |
|---|---|
| Consulta diaria y navegación entre fechas | Implementada |
| Registro rápido para el día consultado | Implementada |
| Programación de actividades futuras | Implementada |
| Calendario mensual con indicadores | Implementada |
| Consulta de actividades por fecha | Implementada |
| Edición de actividades no completadas | Implementada |
| Validación de fechas y horas | Implementada |
| Completado y estado «No completada» | Implementada |
| Eliminación individual y masiva por alcance/periodo | Implementada |
| Protección de actividades completadas | Implementada |
| Persistencia local con Room | Implementada |
| Programación y cancelación de alarmas | Implementada |
| Notificaciones del sistema con acciones | Implementada |
| Sonido personalizado opcional | Implementado |
| Restauración de alarmas después del reinicio | Implementada y comprobada en emulador |

## 7. Persistencia y estructura principal

Room administra las actividades guardadas localmente. Los componentes principales son:

- **Actividad.kt:** entidad que representa una actividad e incluye sus datos y estados.
- **ActividadDao.kt:** consultas y operaciones de inserción, actualización y eliminación.
- **AgendaDatabase.kt:** configuración de Room y migraciones de la base de datos.
- **AgendaViewModel.kt:** conecta los datos con la interfaz y coordina las operaciones.
- **ActividadAlarmaScheduler.kt:** programa y cancela alarmas.
- **ActividadAlarmaReceiver.kt:** muestra la notificación cuando vence una actividad.
- **ActividadAlarmaAccionReceiver.kt:** procesa las acciones de la notificación.
- **ActividadAlarmaSonidoService.kt:** reproduce el sonido personalizado cuando está habilitado.
- **ActividadAlarmaBootReceiver.kt:** restaura las alarmas de actividades pendientes al reiniciar y cuando cambia la hora o zona horaria.

## 8. Diseño y wireframes anotados

Se están incorporando las cuatro láminas anotadas de las pantallas principales de Agenda Personal. Los archivos se añadirán a esta sección a medida que se complete su carga al repositorio.

## 9. Validaciones y alcance

- Las actividades se almacenan localmente en el dispositivo.
- Las actividades completadas no se pueden editar ni reprogramar.
- Las actividades vencidas que siguen sin completar se identifican como «No completada».
- La creación rápida desde Día corresponde al día consultado.
- La programación desde la pantalla correspondiente es para fechas futuras.
- Las operaciones de eliminación masiva requieren confirmación.
- Las funciones de sincronización, cuentas, recurrencia, estadísticas y copias de seguridad en la nube no forman parte del alcance actual.

## 10. Pruebas realizadas

Durante el desarrollo se realizaron pruebas funcionales de persistencia, creación, edición, completado y eliminación de actividades; notificaciones con el sonido personalizado activado y desactivado; acciones desde la notificación; cancelación de alarmas; cambios de hora y zona horaria; y restauración después de reiniciar el emulador. El usuario confirmó que dos alarmas pendientes volvieron a sonar y mostrar notificaciones después del reinicio sin abrir manualmente la aplicación.

La versión debe compilarse y comprobarse nuevamente después de cualquier cambio final antes de generar el entregable.

## 11. Registro de cambios

- Se implementaron las pantallas de Día, Programar actividad, Calendario y Editar actividad.
- Se incorporó persistencia local con Room y migraciones de la base de datos.
- Se añadieron validaciones de fecha y hora, estados de actividad y eliminación por diferentes alcances.
- Se integraron alarmas exactas, notificaciones con acciones y sonido personalizado opcional.
- Se agregó la restauración de alarmas pendientes después del reinicio y ante cambios de hora o zona horaria.
- Se actualizaron las validaciones y los flujos de reprogramación de actividades no completadas.
