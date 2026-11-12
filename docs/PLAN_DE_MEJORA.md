# Plan de mejora — Agenda Personal

Este documento propone los cambios más importantes para la app, ordenados por prioridad. Salen de una revisión del código en `main` (commit `c8f73a8`). Es solo un plan: no modifica código. La idea es revisarlo, ajustarlo y después implementarlo en PRs pequeños, uno por fase.

> Las referencias de archivo son relativas a `app/src/main/java/`.

## Resumen

| Prioridad | Fase | Qué resuelve | Esfuerzo estimado |
|---|---|---|---|
| 🔴 Alta | 1. Bugs de notificaciones y navegación | Notificación que no se puede quitar y pantalla de edición que se reabre sola | Bajo |
| 🔴 Alta | 2. Fiabilidad de datos y alarmas | Actividades que desaparecen al cambiar de idioma, estado desactualizado | Bajo–medio |
| 🟠 Media | 3. Permisos y experiencia de usuario | Pantalla de ajustes que se abre en cada inicio, alarmas sin permiso | Medio |
| 🟡 Media | 4. Pruebas automatizadas | Proteger la base de datos, las migraciones y la lógica de fechas | Medio |
| 🟢 Baja | 5. Estructura y mantenimiento | `DiaScreen` de 1.300 líneas, código de fechas repetido, dependencias | Medio–alto |

---

## Fase 1 — Bugs de notificaciones y navegación 🔴

### 1.1 La notificación queda fija después de "Reprogramar"

- **Dónde:** `com/example/agendapersonal/alarm/ActividadAlarmaReceiver.kt:138` (`setOngoing(true)`).
- **Problema:** la notificación solo se elimina con "Completar" o "No completar" (`ActividadAlarmaAccionReceiver`). "Reprogramar" abre la edición y detiene el sonido, pero nadie llama a `NotificationManager.cancel(actividadId)`, ni siquiera al guardar la nueva hora. Además, la notificación no tiene `setContentIntent`, así que tocarla no hace nada.
- **Propuesta:**
  - Cancelar la notificación de la actividad al abrir la edición desde "Reprogramar", o al guardar los cambios en `AgendaViewModel.actualizarActividad`.
  - Agregar `setContentIntent` para que tocar la notificación abra la actividad.
  - Eliminar `ACCION_REPROGRAMAR` de `ActividadAlarmaAccionReceiver` (hoy está vacía: "Lo conectaremos después"), o implementarla.
- **Cómo comprobarlo:** programar una actividad, esperar la notificación, pulsar "Reprogramar", guardar con otra hora → la notificación desaparece y la alarma nueva suena a la hora nueva.

### 1.2 La pantalla de edición se reabre al girar el teléfono

- **Dónde:** `com/example/agendapersonal/MainActivity.kt:41` y `ui/navigation/AppNavigation.kt` (`LaunchedEffect`).
- **Problema:** cuando la Activity se recrea (rotación, cambio de tema, etc.), `onCreate` vuelve a leer `abrir_actividad_id` del intent y `AppNavigation` navega otra vez a `editar/{id}`. Además, si llega una segunda notificación de la misma actividad, el `LaunchedEffect` no se dispara porque la clave no cambió.
- **Propuesta:**
  - Leer el extra solo cuando `savedInstanceState == null` y quitarlo después de usarlo (`intent.removeExtra(...)`), igual que ya se hace con `detener_alarma_personalizada`.
  - Al terminar la navegación, poner `actividadIdPendiente` en `null` mediante un callback `onNavegacionConsumida`.
- **Cómo comprobarlo:** entrar desde "Reprogramar", volver atrás, girar el teléfono → se queda en la pantalla Día.

---

## Fase 2 — Fiabilidad de datos y alarmas 🔴

### 2.1 Formato de fechas dependiente del idioma del teléfono

- **Dónde:** 38 usos de `SimpleDateFormat(..., Locale.getDefault())` en las pantallas; el scheduler lee con `Locale.ROOT` (`com/example/agendapersonal/alarm/ActividadAlarmaScheduler.kt:31`).
- **Problema:** las fechas y horas se **guardan** en Room con el idioma del dispositivo. En idiomas con dígitos no latinos (árabe, persa, hindi…) se guardan cadenas como `٢٠٢٦-١١-١٢`. Si después el usuario cambia de idioma, las cadenas nuevas (`2026-11-12`) ya no coinciden con las guardadas: la actividad deja de aparecer en Día y Calendario, las eliminaciones por día o por periodo no la encuentran y `ORDER BY fecha, hora` la ordena mal. (La alarma sí suena: el parser del scheduler acepta esos dígitos).
- **Propuesta:**
  - Crear un archivo `util/Fechas.kt` con funciones únicas para formatear y leer el formato de almacenamiento (`yyyy-MM-dd` y `HH:mm`) usando siempre `Locale.ROOT`, o mejor `java.time` (`LocalDate`/`LocalTime`, disponible con *desugaring* en `minSdk 24`).
  - Usar el idioma del usuario solo para **mostrar** fechas (por ejemplo "12 de noviembre"), nunca para guardarlas.
- **Cómo comprobarlo:** prueba unitaria que formatea con `Locale("ar")` como idioma por defecto y verifica que la cadena guardada sigue siendo ASCII.

### 2.2 Operaciones que leen un estado posiblemente desactualizado

- **Dónde:** `viewmodel/AgendaViewModel.kt:60` (`completarActividad`) y `:78` (`cambiarEstadoAlarma`).
- **Problema:** buscan la actividad en `actividades.value`. Como ese `StateFlow` usa `SharingStarted.WhileSubscribed(5000)`, puede estar vacío o no reflejar el último cambio, sobre todo si la acción se hizo desde la notificación. En ese caso la operación no hace nada o pisa datos con una copia vieja.
- **Propuesta:** leer la actividad de la base con `actividadDao.obtenerActividadPorId(id)`, como ya hace `ActividadAlarmaAccionReceiver`.

### 2.3 Detener el sonido con `stopService`

- **Dónde:** `com/example/agendapersonal/alarm/ActividadAlarmaAccionReceiver.kt:45` y `MainActivity.detenerAlarmaPersonalizada`.
- **Problema:** para detener el sonido se usa `startService(ACCION_DETENER)`. Si el servicio no estaba corriendo, eso lo **arranca** solo para detenerlo, y desde segundo plano puede lanzar `IllegalStateException` según la versión de Android.
- **Propuesta:** usar `context.stopService(Intent(context, ActividadAlarmaSonidoService::class.java))` y liberar los recursos en `onDestroy` (detener el `Handler` y abandonar el foco de audio allí también).

---

## Fase 3 — Permisos y experiencia de usuario 🟠

### 3.1 Los ajustes de alarmas exactas se abren en cada inicio

- **Dónde:** `com/example/agendapersonal/MainActivity.kt:106-112`.
- **Problema:** mientras el permiso no esté concedido, cada vez que se abre la app se lanza la pantalla de ajustes del sistema sin ninguna explicación. En Android 14+ este permiso viene **denegado por defecto**, así que le pasa a casi todos los usuarios nuevos.
- **Propuesta:** mostrar primero un diálogo que explique para qué se necesita el permiso, con botones "Ir a ajustes" y "Ahora no". Recordar la decisión y mostrar un aviso discreto en la pantalla Día si los recordatorios están desactivados.

### 3.2 Las alarmas fallan sin avisar si no hay permiso

- **Dónde:** `com/example/agendapersonal/alarm/ActividadAlarmaScheduler.kt:53`.
- **Problema:** si falta el permiso, `programar()` devuelve `false` y nadie revisa ese resultado. El usuario cree que tendrá recordatorio y no lo tendrá. Si concede el permiso más tarde, las actividades ya creadas siguen sin alarma.
- **Propuesta:**
  - Registrar un receiver para `AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED` que vuelva a programar todo, reutilizando la lógica de `ActividadAlarmaBootReceiver`.
  - Mostrar un mensaje en la UI cuando `programar()` devuelve `false`.

### 3.3 Formularios que se borran al girar la pantalla

- **Problema:** el estado de los formularios (`textoTarea`, `fechaTarea`, `horaTarea`, diálogos abiertos…) usa `remember`, así que se pierde al rotar el teléfono.
- **Propuesta:** usar `rememberSaveable` para lo que el usuario escribe, o pasar ese estado al ViewModel (ver 5.1).

---

## Fase 4 — Pruebas automatizadas 🟡

Hoy solo existen `ExampleUnitTest` y `ExampleInstrumentedTest`. El README describe muchas pruebas manuales; la propuesta es automatizar las que más protegen la app:

1. **DAO con Room en memoria** (`androidTest`): insertar, actualizar, eliminar por día, por rango (verificando que la fecha final no se incluye) y eliminar las completadas.
2. **Migraciones** (`androidTest` con `MigrationTestHelper`): cambiar a `exportSchema = true` (`model/AgendaDatabase.kt:15`), guardar los esquemas en `app/schemas/` y probar que los pasos 1→2 y 2→3 conservan los datos.
3. **Fechas** (`test`, JVM): las funciones de `util/Fechas.kt` de la fase 2.1, incluida la prueba con otro idioma por defecto.
4. **Scheduler** (`test` con Robolectric, opcional): no programa alarmas en el pasado, cancela las completadas y devuelve `false` sin permiso.

---

## Fase 5 — Estructura y mantenimiento 🟢

### 5.1 Dividir `DiaScreen.kt` (1.315 líneas)

- Extraer componentes: `TarjetaActividad`, `MenuPapelera`, los diálogos de eliminación y el selector de fecha y hora. Muchos se repiten casi iguales en `CalendarioScreen`.
- Agrupar los aproximadamente 15 `remember` sueltos en un `DiaUiState` dentro del ViewModel.

### 5.2 Paquetes y carpetas

- Mover `model/`, `viewmodel/` y `ui/` a `java/com/example/agendapersonal/`, para que la carpeta coincida con el `package` declarado (hoy Android Studio muestra advertencias).
- Si algún día se publica, cambiar `applicationId` (`com.example.*` no se acepta en Google Play).

### 5.3 Textos en `strings.xml`

- Hoy `strings.xml` solo tiene `app_name`. Pasar los textos de la UI y de las notificaciones a recursos facilita traducir la app y revisar la ortografía.

### 5.4 Dependencias y build

- Alinear versiones en `gradle/libs.versions.toml`: `lifecycle-runtime-ktx` 2.6.1 frente a `lifecycle-viewmodel-compose` 2.10.0; actualizar `core-ktx` 1.10.1 y `activity-compose` 1.8.0.
- Agregar `material-icons-extended` al catálogo de versiones.
- Activar la optimización (R8) en `release` (`app/build.gradle.kts:26`): `material-icons-extended` aumenta mucho el tamaño del APK sin ella.

### 5.5 Detalles menores

- `ActividadAlarmaSonidoService.kt:132`: el log dice "3 segundos", pero el retraso real es de 5000 ms (línea 140).
- Comentarios duplicados en `AgendaViewModel` (por ejemplo, antes de `eliminarActividadesCompletadas` y `eliminarActividadesPorRango`).
- Los `requestCode` de las acciones (`id + 100000`, `id + 200000`) chocan cuando hay más de 100.000 actividades. Es poco probable, pero se puede evitar usando acciones distintas en el `Intent`.

---

## Cómo proponemos implementarlo

- **Un PR por fase**, empezando por la 1 y la 2, que son pequeñas y corrigen bugs visibles.
- Cada PR incluye sus pruebas (fase 4) cuando sea posible, para que el arreglo quede protegido.
- Las fases 3 y 5 se pueden reordenar según lo que el curso valore más (experiencia de usuario o calidad de código).

¿Qué te parece? Puedes comentar directamente en las líneas de este archivo si quieres cambiar prioridades o descartar algún punto.
