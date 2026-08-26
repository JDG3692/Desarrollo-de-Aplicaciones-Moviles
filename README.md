# 📅 Agenda Personal

## 1. Descripción del proyecto

**Agenda Personal** será una aplicación móvil para Android orientada a la organización de actividades y compromisos personales y académicos. La aplicación tendrá como elemento principal una vista de calendario mensual desde la cual el usuario podrá consultar las actividades programadas y registrar nuevas tareas, reuniones, eventos u otros compromisos.

Uno de los objetivos principales será facilitar el registro de actividades y permitir configurar recordatorios visuales y sonoros para ayudar al usuario a recordar sus compromisos en la fecha y hora establecidas.

La propuesta inicial busca mantener una aplicación sencilla y práctica, pero con posibilidades de ampliación después de finalizar el proyecto académico.

## 2. Exposición del problema

En mi experiencia personal, tengo diferentes actividades y compromisos que debo recordar durante el día, la semana y el mes. Aunque existen herramientas como Google Calendar, en algunas ocasiones considero que registrar una actividad puede resultar más elaborado de lo necesario cuando solamente se desea anotar rápidamente algo que se debe realizar. Esto puede hacer que algunos pendientes terminen registrados en diferentes lugares o que se dependa de la memoria para recordarlos.

A partir de esta necesidad surge la idea de desarrollar una agenda móvil sencilla que permita visualizar las actividades de un mes y registrar rápidamente nuevos compromisos, incorporando recordatorios que ayuden a disminuir la posibilidad de olvidar una actividad.

## 3. Objetivo del proyecto

### Objetivo general

Desarrollar una aplicación móvil para Android que facilite la organización de actividades personales y académicas mediante un calendario mensual y un sistema de recordatorios.

### Objetivos específicos

- Permitir registrar, editar y eliminar actividades.
- Facilitar la consulta de las actividades mediante una vista de calendario mensual.
- Permitir establecer una fecha y hora para cada actividad.
- Incorporar recordatorios visuales y sonoros asociados a las actividades.
- Mantener una primera versión sencilla y funcional que pueda ampliarse posteriormente.

## 4. Plataforma

El proyecto será desarrollado inicialmente para dispositivos **Android**, utilizando **Android Studio** como entorno de desarrollo.

Durante el desarrollo se estudiarán las herramientas y componentes necesarios para implementar la interfaz, el almacenamiento de la información y el sistema de notificaciones. La primera versión estará orientada principalmente al funcionamiento local en el dispositivo, dejando abierta la posibilidad de incorporar servicios adicionales en futuras versiones.

## 5. Interfaz de usuario e interfaz de administración

### Interfaz de usuario

La aplicación estará diseñada principalmente para un usuario que necesita organizar sus actividades. Las principales pantallas consideradas inicialmente son:

- **Calendario mensual:** permitirá visualizar los días del mes y las actividades programadas.
- **Nueva actividad:** permitirá registrar el título, descripción, fecha, hora y opciones de recordatorio.
- **Detalle de actividad:** permitirá consultar la información de una actividad, editarla, eliminarla o marcarla como completada.
- **Configuración:** permitirá administrar algunas preferencias relacionadas con los recordatorios y el funcionamiento de la aplicación.

### Interfaz de administración

La primera versión del proyecto no contempla diferentes tipos de usuarios ni un perfil administrativo independiente, debido a que la aplicación está planteada como una herramienta de uso personal. Las opciones de configuración y gestión necesarias estarán disponibles directamente para el usuario.

## 6. Funcionalidad

| Funcionalidad | Descripción |
|---|---|
| Calendario mensual | Permitir visualizar las actividades organizadas por fecha. |
| Crear actividad | Registrar un nuevo compromiso o pendiente. |
| Editar actividad | Modificar la información de una actividad existente. |
| Eliminar actividad | Eliminar actividades que ya no sean necesarias. |
| Completar actividad | Marcar una actividad como realizada. |
| Categorías | Permitir clasificar las actividades según su tipo. |
| Fecha y hora | Establecer cuándo debe realizarse una actividad. |
| Recordatorios | Programar una alerta asociada a una actividad. |
| Notificación | Mostrar una alerta visual y reproducir un sonido cuando corresponda. |

## 7. Diseño y wireframes

Los siguientes esquemas representan una propuesta inicial de las principales pantallas de la aplicación. Estos diseños podrán modificarse durante el desarrollo de acuerdo con las necesidades identificadas.

### Calendario mensual

```text
┌─────────────────────────┐
│       < Agosto >        │
├─────────────────────────┤
│ L  M  M  J  V  S  D     │
│                 1  2    │
│  3  4  5  6  7  8  9    │
│ 10 11 12 13 14 15 16    │
│ 17 18 19 20 21 22 23    │
│ 24 25 26 27 28 29 30    │
│ 31                      │
│                         │
│              ＋         │
└─────────────────────────┘
```

### Nueva actividad

```text
┌─────────────────────────┐
│    Nueva actividad      │
├─────────────────────────┤
│ Título                  │
│ [____________________]  │
│                         │
│ Descripción             │
│ [____________________]  │
│ [____________________]  │
│                         │
│ Fecha   [__/__/____]    │
│ Hora    [__:__]         │
│                         │
│ 🔔 Recordatorio   [ON]  │
│                         │
│       [ GUARDAR ]       │
└─────────────────────────┘
```

### Detalle de actividad

```text
┌─────────────────────────┐
│    Detalle actividad    │
├─────────────────────────┤
│ 📚 Entregar trabajo     │
│                         │
│ 28 de agosto            │
│ 7:00 PM                 │
│                         │
│ Recordatorio: Activado  │
│                         │
│ [ EDITAR ] [ ELIMINAR ] │
│                         │
│ [ ✓ MARCAR COMPLETADA ] │
└─────────────────────────┘
```

## 8. Alcance inicial

La primera versión del proyecto se enfocará en las funciones básicas de una agenda personal y en el funcionamiento de los recordatorios. Se priorizará que las funciones principales sean sencillas de utilizar y que la aplicación pueda funcionar correctamente en un dispositivo Android antes de incorporar características adicionales.

## 9. Posibles mejoras futuras

Una vez finalizado el proyecto académico, se contempla la posibilidad de continuar su desarrollo incorporando nuevas funciones según las necesidades identificadas durante su utilización. Algunas posibilidades podrían ser:

- Diferentes vistas del calendario, como semanal o diaria.
- Actividades recurrentes.
- Mayor personalización de los recordatorios.
- Copias de seguridad de la información.
- Sincronización entre dispositivos.
- Integración con otros servicios de calendario.
- Mejoras de diseño y accesibilidad.

Estas funciones no forman parte del alcance inicial y se considerarían posteriormente de acuerdo con la evolución del proyecto.
