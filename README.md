# 📅 Agenda Personal

## 1. Descripción del proyecto

**Agenda Personal** será una aplicación móvil para Android orientada a la organización de actividades y compromisos personales y académicos. La aplicación tendrá como elemento principal una vista de calendario mensual desde la cual el usuario podrá consultar las actividades programadas y registrar nuevos recordatorios.

La idea principal es que registrar una actividad sea rápido y sencillo. Cuando el usuario seleccione un día del calendario, podrá crear una actividad para ese día indicando solamente qué necesita recordar y a qué hora. También existirá una opción para programar una actividad para una fecha diferente sin tener que desplazarse hasta ese día en el calendario.

La aplicación generará un recordatorio visual y sonoro cuando llegue la hora programada. La propuesta inicial busca mantener una aplicación sencilla y práctica, pero con posibilidades de ampliación después de finalizar el proyecto académico.

## 2. Exposición del problema

En mi experiencia personal, tengo diferentes actividades y compromisos que debo recordar durante el día, la semana y el mes. Aunque existen herramientas como Google Calendar, en algunas ocasiones considero que registrar una actividad puede resultar más elaborado de lo necesario cuando solamente se desea anotar rápidamente algo que se debe realizar. Esto puede hacer que algunos pendientes terminen registrados en diferentes lugares o que se dependa de la memoria para recordarlos.

A partir de esta necesidad surge la idea de desarrollar una agenda móvil sencilla que permita visualizar las actividades de un mes y registrar rápidamente nuevos compromisos, incorporando recordatorios que ayuden a disminuir la posibilidad de olvidar una actividad.

## 3. Objetivo del proyecto

### Objetivo general

Desarrollar una aplicación móvil para Android que facilite la organización de actividades personales y académicas mediante un calendario mensual y un sistema de recordatorios.

### Objetivos específicos

- Permitir consultar las actividades mediante una vista de calendario mensual.
- Permitir crear rápidamente una actividad indicando qué se necesita recordar y la hora correspondiente.
- Permitir programar actividades para fechas diferentes a la que se está consultando.
- Permitir editar, eliminar y marcar como completadas las actividades.
- Incorporar recordatorios visuales y sonoros asociados a las actividades.
- Mantener una primera versión sencilla y funcional que pueda ampliarse posteriormente.

## 4. Plataforma

El proyecto será desarrollado inicialmente para dispositivos **Android**, utilizando **Android Studio** como entorno de desarrollo.

Durante el desarrollo se estudiarán las herramientas y componentes necesarios para implementar la interfaz, el almacenamiento de la información y el sistema de notificaciones. La primera versión estará orientada principalmente al funcionamiento local en el dispositivo, dejando abierta la posibilidad de incorporar servicios adicionales en futuras versiones.

## 5. Interfaz de usuario e interfaz de administración

### Interfaz de usuario

La aplicación estará diseñada principalmente para un usuario que necesita organizar sus actividades. Las principales pantallas consideradas inicialmente son:

- **Calendario mensual:** permitirá visualizar los días del mes y las actividades programadas.
- **Nueva actividad:** se abrirá al seleccionar un día y permitirá indicar qué se necesita recordar y a qué hora.
- **Programar actividad:** permitirá crear una actividad para una fecha diferente a la que se está consultando, indicando la fecha, qué se necesita recordar y la hora.
- **Detalle de actividad:** permitirá consultar una actividad, editarla, eliminarla o marcarla como completada.

### Interfaz de administración

La primera versión del proyecto no contempla diferentes tipos de usuarios ni un perfil administrativo independiente, debido a que la aplicación está planteada como una herramienta de uso personal. Las opciones de gestión necesarias estarán disponibles directamente para el usuario.

## 6. Funcionalidad

| Funcionalidad | Descripción |
|---|---|
| Calendario mensual | Permitir visualizar las actividades organizadas por fecha. |
| Crear actividad | Registrar rápidamente un recordatorio para el día seleccionado. |
| Programar actividad | Registrar un recordatorio para una fecha diferente a la que se está consultando. |
| Editar actividad | Modificar la información de una actividad existente. |
| Eliminar actividad | Eliminar actividades que ya no sean necesarias. |
| Completar actividad | Marcar una actividad como realizada. |
| Fecha y hora | Establecer cuándo debe realizarse una actividad. |
| Recordatorio | Programar una alerta asociada a una actividad. |
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

### Nueva actividad desde un día

```text
┌─────────────────────────┐
│    Nueva actividad      │
├─────────────────────────┤
│ ¿Qué necesitas recordar?│
│ [____________________]  │
│                         │
│ Hora                    │
│ [      07:00 PM      ]  │
│                         │
│ 🔔 Recordatorio: ON     │
│                         │
│       [ GUARDAR ]       │
└─────────────────────────┘
```

En este caso, la fecha queda determinada automáticamente por el día seleccionado en el calendario.

### Programar actividad para otra fecha

```text
┌─────────────────────────┐
│   Programar actividad   │
├─────────────────────────┤
│ ¿Qué necesitas recordar?│
│ [____________________]  │
│                         │
│ Fecha                   │
│ [    15/09/2026      ]  │
│                         │
│ Hora                    │
│ [      03:00 PM      ]  │
│                         │
│ 🔔 Recordatorio: ON     │
│                         │
│      [ PROGRAMAR ]      │
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
│ 🔔 Recordatorio         │
│                         │
│ [ EDITAR ] [ ELIMINAR ] │
│                         │
│ [ ✓ MARCAR COMPLETADA ] │
└─────────────────────────┘
```

## 8. Alcance inicial

La primera versión del proyecto se enfocará en las funciones básicas de una agenda personal y en el funcionamiento de los recordatorios. Se priorizará que registrar una actividad sea rápido y sencillo y que la aplicación pueda funcionar correctamente en un dispositivo Android antes de incorporar características adicionales.

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
