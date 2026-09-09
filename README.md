# Pepo Bóveda

Gestor de contraseñas para Android. Sin cuentas, sin nube, sin permiso de internet.

No te pido que confíes en mí: te pido que lo compruebes. Abre
[`app/src/main/AndroidManifest.xml`](app/src/main/AndroidManifest.xml) y mira los permisos.
Son tres, y ninguno es `INTERNET`:

| Permiso | Para qué |
|---|---|
| `USE_BIOMETRIC` | abrir con la huella |
| `VIBRATE` | las microinteracciones |
| `CAMERA` | leer el QR de un 2FA, y solo si pulsas "escanear" |

Si una app no puede hablar con la red, no puede mandar tus contraseñas a ningún sitio.
Esta lista está verificada contra el APK compilado, no solo contra el manifiesto fuente
(`aapt dump permissions`): las librerías de androidx cuelan de fábrica `USE_FINGERPRINT`
y un permiso interno de broadcast dinámico, y ambos se retiran explícitamente con
`tools:node="remove"` para que la lista real no tenga ni uno de más.

## Cómo guarda las contraseñas

La contraseña maestra **no se guarda en ninguna parte**. Se pasa por **Argon2id**
(64 MiB, 3 iteraciones, paralelismo 4) para derivar una clave, y con ella se cifra la
bóveda entera con **AES-256-GCM**.

- La cabecera del archivo (sal y parámetros de Argon2) va como AAD, así que está
  autenticada: no se puede editar para rebajar el coste del KDF sin que falle el tag.
- Sal de 16 bytes y nonce de 12 bytes aleatorios, nuevos en cada escritura.
- La bóveda es un único archivo en el almacenamiento privado de la app.
- La huella no sustituye a la contraseña: envuelve la clave maestra con una clave AES del
  Android Keystore. Hay dos modos, y la app te dice cuál tienes:
  - **Fuerte** (el que se intenta siempre primero): la clave del Keystore exige huella de
    Clase 3 en cada uso y se invalida si cambia la biometría del dispositivo.
  - **Compatible** (solo si lo activas tú, con aviso): para móviles y ROMs donde la huella es
    de Clase 2 o donde el Keystore rechaza la operación aunque Android dé la huella por
    buena (error -26, típico de ROMs personalizadas). La clave sigue en el Keystore y no
    sale del móvil, pero no exige autenticación del chip: la huella o el PIN los comprueba
    Android y la app decide abrir. Es más débil, y por eso se explica antes de activarlo.

## Qué hay dentro

Bóveda cifrada, generador con `SecureRandom` y medidor de entropía, autenticador TOTP
(RFC 6238) con lector de QR mediante ZXing, servicio de autofill, proveedor de
credenciales de Android 14+ para passkeys y contraseñas, exportar/importar cifrado,
importar CSV desde otros gestores (Google, Chrome, Bitwarden, LastPass...), lista local
de contraseñas muy comunes filtradas en la edición y en el diagnóstico de salud,
recordatorio de exportación offline en la pantalla principal, menú lateral con selección
múltiple para borrar varias entradas de golpe, personalización de apariencia y bloqueo
automático por inactividad.

### Autofill y Credential Manager

Para usuario y contraseña hay dos caminos, porque Android también los tiene:

- **Autofill clásico**: detecta campos de usuario/contraseña en webs y apps, ofrece las
  entradas que coinciden con el dominio o paquete guardado y, si la bóveda está cerrada,
  abre una hoja de desbloqueo antes de rellenar. También puede guardar formularios cuando
  Android dispara el flujo de guardado.
- **Credential Manager en Android 14+**: la app se declara como proveedor de passkeys y
  también de contraseñas (`TYPE_PUBLIC_KEY_CREDENTIAL` y `TYPE_PASSWORD_CREDENTIAL`). Las
  apps modernas que pidan credenciales por esta API pueden recibir una contraseña guardada
  o pedir guardar una nueva, siempre con confirmación y bóveda desbloqueada.

La coincidencia es conservadora: solo se ofrecen credenciales si el dominio web o el
paquete de aplicación coincide con lo que está guardado en la entrada. Si una app dibuja
sus propios campos o no usa Autofill/Credential Manager, Android no llama a la bóveda y
ningún gestor puede forzar su aparición.

### Menú lateral y selección múltiple

Deslizando desde el borde izquierdo (o tocando el icono de menú) se abre un panel con
Generador, Passkeys, Autenticador 2FA, Ajustes y Bloquear, además del nombre de la app y
la versión compilada. En la lista, mantener pulsada una entrada entra en modo selección:
se pueden marcar varias (o todas de golpe, con el botón de seleccionar todo) y borrarlas
juntas en vez de una por una. La cabecera de la lista usa un buscador compacto y un
selector de filtros en la misma fila: mitad búsqueda, mitad filtro, con iconos y opciones
desplegables para Todo, Claves, Passkeys, Notas y Favoritos.

### Personalización

En Ajustes > Apariencia:

- **Tema**: sistema, claro u oscuro. El texto ya no es blanco puro en modo oscuro (para
  no deslumbrar de noche) y las etiquetas de sección usan el color elegido.
- **Nombre dentro de la app**: solo se ve en la cabecera del menú lateral. Android no deja
  poner un rótulo de icono libre en tiempo de ejecución, así que el nombre del icono del
  launcher queda fijo como "Bóveda local".
- **Color de la app y del icono**: 20 colores (la paleta clásica de Material Design, la
  misma que usa Fossify) que tiñen a la vez el acento de la app y el icono del launcher,
  con el mismo dibujo y solo el fondo cambiando de color. Cambiar de color cierra la app
  un instante (lo hace Android al cambiar su propio icono, no es un fallo) y avisa antes
  de hacerlo.
- **Densidad de lista**: predeterminada, cómoda o compacta. La compacta reduce altura,
  monogramas y separación entre filas para ver más entradas de la bóveda a la vez.

### Importar desde CSV

En Ajustes > Copia de seguridad > "Importar desde CSV" puedes traer las contraseñas
que exportan Google Password Manager, Chrome, Bitwarden o LastPass. La app reconoce
las columnas habituales de cada uno (`name`/`title`, `url`/`login_uri`,
`username`/`login_username`, `password`/`login_password`, `notes`/`extra`) y no
depende de ninguna librería externa ni de permisos nuevos: el archivo lo elige el
propio selector de documentos del sistema, se lee una vez y se cifra al entrar en la
bóveda. Ese CSV va sin cifrar mientras existe en el teléfono, así que la app avisa
antes de importarlo y conviene borrarlo después de usarlo.

### Contraseñas muy comunes y exportación

La app incluye una lista local de contraseñas muy utilizadas y frecuentes en filtraciones
conocidas. Si en la edición detecta que la contraseña está en esa lista, muestra un
aviso en rojo junto al medidor de fuerza. También se añade una métrica nueva en la
pantalla "Salud de la bóveda" para ver las entradas con contraseñas muy comunes y
agrupadas por sitio.

En Ajustes > Copia de seguridad hay además un recordatorio offline: si la última
exportación de la bóveda hace más días que el intervalo elegido, la app lo muestra como
un banner en la pantalla principal de la lista con un acceso directo a ese ajuste. No usa
WorkManager ni notificaciones del sistema: solo compara fechas en el móvil cuando se abre
la lista.

El lector de QR tiene tres caminos para que no se quede nadie fuera: CameraX; si falla,
un motor compatible con la API antigua de cámara (la que funciona hasta en los HAL más
viejos); y siempre, leer el QR desde una imagen o captura, o escribir la clave a mano.
El motor se puede forzar desde Ajustes.

En Ajustes > Audítame hay un **diagnóstico local**: lo que Android dice de la huella y de
la cámara de ese móvil y los últimos pasos que dio la app con ellas, con botón de copiar
y compartir. No registra nada tuyo (ni claves, ni contraseñas, ni el contenido de ningún
QR): solo qué paso se dio y qué excepción saltó. Es lo que necesito para arreglar un
fallo en un móvil que no tengo.

## Compilar y validar

Necesitas JDK 17 y el SDK de Android (compileSdk 36, minSdk 29).

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:connectedDebugAndroidTest
```

La última tarea ejecuta pruebas instrumentadas reales en un dispositivo o emulador con
Android. Se usan para verificar la criptografía nativa del Keystore, la lista local de
contraseñas comunes y el recordatorio de exportación guardado en ajustes.

Para una build de release firmada, copia `keystore.properties.ejemplo` a
`keystore.properties`, crea tu propio keystore y rellena tus valores:

```bash
keytool -genkeypair -v -keystore pepo-boveda.jks -alias pepoboveda \
        -keyalg RSA -keysize 4096 -validity 10000
./gradlew :app:assembleRelease
```

`keystore.properties` y los `.jks` están en `.gitignore` y no deben subirse nunca.

## Estado honesto

Esto es importante y no lo voy a esconder:

- **Probada a mano en dos dispositivos físicos.** Ahí se ha visto funcionar la bóveda,
  la biometría, el autofill, el registro de passkeys en el navegador, la cámara del
  escáner y el autenticador TOTP. El soporte de contraseñas por Credential Manager está
  implementado y compila contra `androidx.credentials:credentials:1.5.0`, pero todavía
  necesita más pruebas reales con apps que usen esa API. Lo que no está es una batería
  de pruebas automatizadas más amplia: los tests unitarios pasan (criptografía, Base32, TOTP,
  generador, dominios, CBOR) y los instrumentados de Argon2 también se han corrido y
  pasan. Pero dos móviles no son un banco de pruebas: no esperes que esté probada en
  todos los fabricantes ni en todas las versiones de Android.
- **Aquí es donde me vienes bien tú.** Si la pruebas y algo se rompe, me vendrá
  fenomenal que me lo digas: qué móvil, qué versión de Android, qué hiciste y qué pasó.
  Lo más útil es el informe de Ajustes > Audítame > "Copiar informe", que lleva justo eso
  y ningún dato tuyo. Un fallo que encuentres es un fallo que dejo de tener. Abre un
  issue sin miedo, y si te sabes buscar la vida, mira el código y dime qué está mal.
- **El primer aviso de la gente ya está atendido.** En un Redmi 6 con DotOS (ROM
  personalizada) no funcionaban ni la cámara del escáner ni la huella. No tengo ese
  móvil, así que no lo he podido reproducir; lo que he hecho es que ninguno de los dos
  pueda fallar en silencio: la cámara tiene motor de reserva y lectura desde imagen, la
  huella tiene el modo compatible y explica exactamente por qué no puede usar el fuerte,
  y todo queda apuntado en el diagnóstico para que el siguiente aviso venga con datos.
- Sigue abierto: si Android no devuelve la firma de la app que pide una passkey, se
  firma igual en vez de abortar. Y el APK pesa ~44 MB porque incluye Argon2 para las
  cuatro ABI; con splits bajaría a unos 15 MB.

El código lo ha revisado quien lo ha escrito, que es el peor revisor posible. Por eso
está aquí entero y por eso te pido que lo mires: no te pido confianza, te pido que lo
compruebes.
