# 📅 Agenda Personal

## 1. Descripción del proyecto

**Agenda Personal** será una aplicación móvil para Android orientada principalmente a la **organización de actividades y compromisos laborales**, aunque también podrá utilizarse para actividades personales y académicas. La **pantalla principal** mostrará las actividades pendientes correspondientes al **día actual**, permitiendo consultar rápidamente lo que está pendiente y agregar nuevas actividades.

Desde la pantalla principal habrá una opción para acceder a una **vista de calendario mensual**, donde el usuario podrá visualizar las actividades programadas durante el mes y seleccionar una fecha específica para consultarlas.

La idea principal es que registrar una actividad sea rápido y sencillo. Cuando el usuario se encuentre en la pantalla del día actual, podrá agregar una actividad indicando solamente qué necesita recordar y a qué hora. También existirá una opción para programar una actividad para una fecha diferente sin tener que desplazarse hasta ese día en el calendario.

La aplicación generará un recordatorio visual y sonoro cuando llegue la hora programada. Una vez que una actividad sea completada, dejará de aparecer entre las actividades pendientes de la pantalla principal para mantener la vista limpia y enfocada en lo que todavía está por realizarse.

La propuesta inicial busca mantener una aplicación sencilla y práctica, pero con posibilidades de ampliación después de finalizar el proyecto académico.

## 2. Exposición del problema

En mi experiencia laboral, tengo diariamente diferentes **tareas, actividades y compromisos** que debo recordar y atender durante la jornada, la semana y el mes. Cuando se acumulan varios pendientes, puede resultar difícil mantenerlos todos presentes, especialmente cuando se utiliza la memoria o diferentes medios para registrar lo que se necesita realizar.

Aunque existen herramientas como Google Calendar y otras aplicaciones de organización, en algunas ocasiones considero que registrar una actividad puede resultar más elaborado de lo necesario cuando solamente se desea anotar rápidamente algo que se debe hacer. Esto puede llevar a que algunos pendientes terminen registrados en diferentes lugares o a que se dependa de la memoria para recordarlos.

A partir de esta necesidad surge la idea de desarrollar una agenda móvil sencilla, con un enfoque inicial en la **organización de actividades laborales**, que muestre en primer lugar los pendientes del día actual, permita consultar una visión mensual de los compromisos y facilite el registro rápido de nuevos recordatorios. También se busca evitar que las actividades ya realizadas permanezcan acumuladas en la pantalla principal.

## 3. Objetivo del proyecto

### Objetivo general

Desarrollar una aplicación móvil para Android que facilite la **organización y seguimiento de actividades laborales**, mediante una pantalla principal centrada en el día actual, una vista de calendario mensual y un sistema de recordatorios.

### Objetivos específicos

- Mostrar inicialmente las actividades correspondientes al día actual.
- Permitir consultar las actividades de diferentes fechas mediante un calendario mensual.
- Permitir crear rápidamente una actividad indicando qué se necesita recordar y la hora correspondiente.
- Permitir programar actividades para fechas diferentes a la fecha actual.
- Permitir editar y eliminar actividades.
- Permitir marcar una actividad como completada y retirarla de las actividades pendientes.
- Incorporar recordatorios visuales y sonoros asociados a las actividades.
- Mantener una primera versión sencilla y funcional que pueda ampliarse posteriormente.

## 4. Plataforma

El proyecto será desarrollado inicialmente para dispositivos **Android**, utilizando **Android Studio** como entorno de desarrollo.

Durante el desarrollo se estudiarán las herramientas y componentes necesarios para implementar la interfaz, el almacenamiento de la información y el sistema de notificaciones. La primera versión estará orientada principalmente al funcionamiento local en el dispositivo, dejando abierta la posibilidad de incorporar servicios adicionales en futuras versiones.

## 5. Interfaz de usuario e interfaz de administración

### Interfaz de usuario

La aplicación estará diseñada principalmente para un usuario que necesita organizar sus actividades laborales. Las principales vistas y funciones consideradas inicialmente son:

- **Pantalla principal / vista diaria:** mostrará las actividades pendientes del día actual y contará con un botón para agregar una nueva actividad.
- **Vista mensual:** permitirá visualizar el calendario completo y reconocer los días que tienen actividades programadas. Desde esta vista se podrá seleccionar una fecha para consultar sus actividades.
- **Nueva actividad:** permitirá indicar qué se necesita recordar y a qué hora. Cuando se cree desde la pantalla principal, la actividad quedará asociada al día actual.
- **Programar actividad:** permitirá crear una actividad para una fecha diferente, indicando la fecha, qué se necesita recordar y la hora.
- **Detalle de actividad:** permitirá consultar una actividad, editarla, eliminarla o marcarla como completada.

### Interfaz de administración

La primera versión del proyecto no contempla diferentes tipos de usuarios ni un perfil administrativo independiente, debido a que la aplicación está planteada como una herramienta de uso personal para la **organización de actividades, principalmente laborales**. Las opciones de gestión necesarias estarán disponibles directamente para el usuario.

## 6. Funcionalidad

| Funcionalidad | Descripción |
|---|---|
| Pantalla diaria | Mostrar las actividades pendientes del día actual. |
| Vista mensual | Visualizar las actividades organizadas dentro de un mes. |
| Agregar actividad | Registrar rápidamente un recordatorio para el día actual. |
| Programar actividad | Registrar un recordatorio para una fecha diferente a la actual. |
| Editar actividad | Modificar la información de una actividad existente. |
| Eliminar actividad | Eliminar una actividad que ya no sea necesaria. |
| Completar actividad | Marcar una actividad como realizada y retirarla de las actividades pendientes. |
| Fecha y hora | Establecer cuándo debe realizarse una actividad. |
| Recordatorio | Programar una alerta asociada a una actividad. |
| Notificación | Mostrar una alerta visual y reproducir un sonido cuando corresponda. |

## 7. Diseño y wireframes

Esta sección se desarrollará posteriormente mediante wireframes de las principales pantallas de la aplicación. Los diseños se utilizarán para definir con mayor claridad la distribución de los elementos y el flujo de navegación antes de comenzar la implementación.

Las pantallas que se consideran inicialmente son:

- Pantalla principal / vista diaria.
- Vista mensual.
- Nueva actividad.
- Programar actividad para otra fecha.
- Detalle de actividad.

## 8. Alcance inicial

La primera versión del proyecto se enfocará en las funciones básicas de una agenda personal, con **prioridad en la organización de actividades laborales**, dando especial importancia a la pantalla del día actual, la consulta mediante calendario mensual y el funcionamiento de los recordatorios. Se priorizará que registrar una actividad sea rápido y sencillo y que, una vez completada, deje de aparecer entre las actividades pendientes.

## 9. Posibles mejoras futuras

Una vez finalizado el proyecto académico, se contempla la posibilidad de continuar su desarrollo incorporando nuevas funciones según las necesidades identificadas durante su utilización. Algunas posibilidades podrían ser:

- Actividades recurrentes.
- Mayor personalización de los recordatorios.
- Historial de actividades completadas.
- Copias de seguridad de la información.
- Sincronización entre dispositivos.
- Integración con otros servicios de calendario.
- Mejoras de diseño y accesibilidad.

Estas funciones no forman parte del alcance inicial y se considerarían posteriormente de acuerdo con la evolución del proyecto.
