# 📅 Agenda Personal

## 1. Descripción del proyecto

**Agenda Personal** será una aplicación móvil para Android orientada principalmente a la **organización de actividades y compromisos laborales**, aunque también podrá utilizarse para actividades personales y académicas. La **pantalla principal** estará centrada en el día actual y mostrará las actividades pendientes correspondientes a esa fecha, permitiendo consultar rápidamente lo que está pendiente y agregar nuevas actividades.

La pantalla principal incluirá un encabezado de bienvenida, la fecha actual, un espacio para registrar rápidamente una actividad y su hora, y una sección de actividades para hoy organizada cronológicamente. Las actividades completadas se ocultarán de esta vista para mantenerla limpia y enfocada en los pendientes.

Desde la pantalla principal habrá opciones para acceder a una **vista de calendario mensual** y a una sección para **programar actividades para fechas futuras**. El calendario permitirá reconocer mediante una marca los días que tienen actividades y seleccionar una fecha para consultar sus actividades.

La aplicación generará un recordatorio visual y sonoro cuando llegue la hora programada. El usuario podrá aceptar la actividad o posponer el recordatorio. Al aceptarla, la actividad se marcará como completada y dejará de aparecer entre los pendientes del día, pero permanecerá almacenada para permitir implementar posteriormente un historial de actividades completadas.

La propuesta inicial busca mantener una aplicación sencilla, práctica y fácil de utilizar, pero con una estructura que permita ampliar sus funciones después de finalizar el proyecto académico.

## 2. Exposición del problema

En mi experiencia laboral, tengo diariamente diferentes **tareas, actividades y compromisos** que debo recordar y atender durante la jornada, la semana y el mes. Cuando se acumulan varios pendientes, puede resultar difícil mantenerlos todos presentes, especialmente cuando se utiliza la memoria o diferentes medios para registrar lo que se necesita realizar.

Aunque existen herramientas como Google Calendar y otras aplicaciones de organización, en algunas ocasiones considero que registrar una actividad puede resultar más elaborado de lo necesario cuando solamente se desea anotar rápidamente algo que se debe hacer. Esto puede llevar a que algunos pendientes terminen registrados en diferentes lugares o a que se dependa de la memoria para recordarlos.

A partir de esta necesidad surge la idea de desarrollar una agenda móvil sencilla, con un enfoque inicial en la **organización de actividades laborales**, que muestre en primer lugar los pendientes del día actual, facilite el registro rápido de nuevas actividades, permita programar compromisos futuros y ofrezca una visión mensual de las actividades. También se busca evitar que las actividades ya realizadas permanezcan acumuladas en la pantalla principal.

## 3. Objetivo del proyecto

### Objetivo general

Desarrollar una aplicación móvil para Android que facilite la **organización y seguimiento de actividades laborales**, mediante una pantalla principal centrada en el día actual, una vista de calendario mensual y un sistema de recordatorios.

### Objetivos específicos

- Mostrar inicialmente las actividades pendientes correspondientes al día actual.
- Permitir registrar rápidamente una actividad para el día actual indicando su descripción y hora.
- Permitir consultar las actividades de diferentes fechas mediante un calendario mensual.
- Permitir programar actividades para fechas diferentes a la fecha actual.
- Permitir editar una actividad existente, incluyendo su descripción, fecha y hora.
- Permitir eliminar actividades.
- Permitir marcar una actividad como completada y ocultarla de las actividades pendientes.
- Mantener almacenadas las actividades completadas para posibilitar un historial futuro.
- Organizar automáticamente las actividades pendientes por hora.
- Incorporar recordatorios visuales y sonoros asociados a las actividades.
- Permitir aceptar o posponer un recordatorio cuando llegue la hora programada.
- Mantener una primera versión sencilla y funcional que pueda ampliarse posteriormente.

## 4. Plataforma

El proyecto será desarrollado inicialmente para dispositivos **Android**, utilizando **Android Studio** como entorno de desarrollo.

Durante el desarrollo se estudiarán las herramientas y componentes necesarios para implementar la interfaz, el almacenamiento de la información, la gestión de fechas y horas y el sistema de notificaciones. La primera versión estará orientada principalmente al funcionamiento local en el dispositivo, dejando abierta la posibilidad de incorporar servicios adicionales en futuras versiones.

## 5. Interfaz de usuario e interfaz de administración

### Interfaz de usuario

La aplicación estará diseñada principalmente para un usuario que necesita organizar sus actividades laborales. Las principales vistas y funciones consideradas inicialmente son:

- **Pantalla principal / vista diaria:** mostrará un mensaje de bienvenida, la fecha actual, un formulario de registro rápido de actividades y las actividades pendientes del día ordenadas por hora.
- **Nueva actividad rápida:** permitirá escribir qué se necesita realizar y seleccionar una hora. Al registrarla desde la pantalla principal, quedará asociada automáticamente al día actual.
- **Programar actividad:** permitirá crear una actividad para una fecha diferente, indicando fecha, descripción y hora.
- **Vista mensual:** mostrará un calendario del mes con una marca en los días que tengan actividades y permitirá seleccionar una fecha para consultar sus actividades.
- **Edición de actividad:** al seleccionar una actividad se podrá modificar su descripción, fecha u hora, o eliminarla.
- **Recordatorio:** cuando llegue la fecha y hora programadas se mostrará una alerta visual y sonora con opciones para aceptar o posponer.

La pantalla diaria se mantendrá enfocada en las actividades pendientes. Las actividades completadas se ocultarán de esta vista, mientras que sus datos se conservarán para una posible función de historial posterior.

### Interfaz de administración

La primera versión del proyecto no contempla diferentes tipos de usuarios ni un perfil administrativo independiente, debido a que la aplicación está planteada como una herramienta de uso personal para la **organización de actividades, principalmente laborales**. Las opciones de gestión necesarias estarán disponibles directamente para el usuario.

## 6. Funcionalidad

| Funcionalidad | Descripción |
|---|---|
| Pantalla diaria | Mostrar las actividades pendientes del día actual. |
| Bienvenida y fecha | Mostrar un mensaje de bienvenida y la fecha actual del dispositivo. |
| Agregar actividad | Registrar rápidamente una actividad para el día actual. |
| Selector de hora | Seleccionar hora mediante una interfaz sencilla con horas, minutos y AM/PM. |
| Orden cronológico | Mostrar automáticamente las actividades pendientes ordenadas por hora. |
| Programar actividad | Registrar una actividad para una fecha diferente a la actual. |
| Vista mensual | Mostrar un calendario con marcas en los días que tengan actividades. |
| Consulta por fecha | Mostrar las actividades correspondientes a una fecha seleccionada. |
| Editar actividad | Modificar la descripción, fecha u hora de una actividad existente. |
| Eliminar actividad | Eliminar una actividad que ya no sea necesaria. |
| Completar actividad | Marcar una actividad como realizada y ocultarla de las actividades pendientes. |
| Conservación de completadas | Mantener almacenadas las actividades completadas para un historial futuro. |
| Recordatorio | Programar una alerta asociada a la fecha y hora de una actividad. |
| Notificación | Mostrar una alerta visual y reproducir un sonido cuando corresponda. |
| Posponer | Permitir posponer un recordatorio para volver a notificar posteriormente. |

## 7. Diseño y wireframes

Esta sección se desarrollará mediante wireframes de las principales pantallas de la aplicación. Los diseños se utilizarán para definir con mayor claridad la distribución de los elementos y el flujo de navegación antes de completar la implementación.

La **pantalla principal / vista diaria** se plantea inicialmente con la siguiente estructura funcional:

1. Mensaje de bienvenida.
2. Fecha actual.
3. Campo para escribir una nueva actividad.
4. Selector amigable de hora.
5. Botón para agregar la actividad.
6. Sección de actividades para hoy, ordenadas por hora.
7. Destacado de la próxima actividad, cuando corresponda.
8. Estado informativo cuando no existan actividades pendientes.
9. Botón para programar una actividad para otra fecha.
10. Botón para acceder al calendario mensual.

Las actividades podrán seleccionarse para abrir una interfaz sencilla de edición. No se busca sobrecargar cada actividad con múltiples botones visibles.

Las pantallas consideradas inicialmente son:

- Pantalla principal / vista diaria.
- Programar actividad para otra fecha.
- Vista mensual.
- Consulta de actividades de una fecha.
- Edición de actividad mediante una interfaz sencilla.

## 8. Alcance inicial

La primera versión del proyecto se enfocará en las funciones básicas de una agenda personal, con **prioridad en la organización de actividades laborales**, dando especial importancia a la pantalla del día actual, la consulta mediante calendario mensual, la programación de actividades futuras y el funcionamiento de los recordatorios.

La pantalla principal deberá permitir registrar rápidamente actividades del día, seleccionar su hora, visualizarlas ordenadas cronológicamente, completarlas y editarlas. Las actividades completadas se ocultarán de la vista diaria, pero permanecerán almacenadas.

Las actividades programadas para fechas futuras se conservarán hasta llegar a su fecha correspondiente. Al cambiar el día, la vista diaria mostrará únicamente las actividades de la nueva fecha; no se propone borrar físicamente las actividades anteriores.

Se priorizará que registrar, consultar, editar y completar una actividad sea rápido y sencillo.

## 9. Posibles mejoras futuras

Una vez finalizado el proyecto académico, se contempla la posibilidad de continuar su desarrollo incorporando nuevas funciones según las necesidades identificadas durante su utilización. Algunas posibilidades podrían ser:

- Historial de actividades completadas.
- Actividades recurrentes.
- Estadísticas de actividades completadas.
- Mayor personalización de los recordatorios.
- Copias de seguridad de la información.
- Sincronización entre dispositivos.
- Integración con otros servicios de calendario.
- Mejoras de diseño y accesibilidad.

Estas funciones no forman parte del alcance inicial y se considerarían posteriormente de acuerdo con la evolución del proyecto.
