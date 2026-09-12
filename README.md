# Bóveda local

Gestor de contraseñas para Android. Sin cuentas, sin nube, sin permiso de internet.

## Origen

Este proyecto es un **fork** de [pepo-boveda](https://github.com/pepitolas13/pepo-boveda),
que he editado y personalizado para adaptarlo a mis necesidades. Se mantiene el crédito
al proyecto original y se conserva su licencia MIT.

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

Bóveda cifrada, generador criptográfico con `SecureRandom` y medidor de entropía (contraseñas aleatorias, frases Diceware de 3 a 12 palabras y motor de patrones personalizados), campos personalizados dinámicos (texto, oculto, PIN), autenticador TOTP (RFC 6238) con lector de QR mediante ZXing, widget de escritorio para códigos 2FA en vivo, Quick Settings Tile ("Generador Rápido") en la cortina de notificaciones, servicio de autofill, proveedor de credenciales de Android 14+ para passkeys y contraseñas, exportar/importar cifrado, importar CSV desde otros gestores (Google, Chrome, Bitwarden, LastPass...), lista local de contraseñas muy comunes filtradas en la edición y en el diagnóstico de salud, recordatorio de exportación offline en la pantalla principal, menú lateral con selección múltiple para borrar varias entradas de golpe, filtros por categoría y ordenación avanzada, abecedario lateral táctil con efecto de ola curvada estilo Niagara Launcher y escala progresiva de letras en GPU (120 FPS), suite de personalización visual en tiempo real (colores, bordes, tipografía), animación de bóveda continua gobernada por reloj VSync, protección incondicional con `FLAG_SECURE` permanente y bloqueo automático granular por inactividad.

### Autofill y Credential Manager

Para usuario y contraseña hay dos caminos, porque Android también los tiene:

- **Autofill clásico**: detecta campos de usuario/contraseña en webs y apps, ofrece las
  entradas que coinciden con el dominio o paquete guardado y, si la bóveda está cerrada,
  abre una hoja de desbloqueo antes de rellenar. También puede guardar formularios cuando
  Android dispara el flujo de guardado. Los tipos de datos declarados para guardar se
  ajustan dinámicamente a los campos detectados, por lo que también admite formularios
  parciales, inicios de sesión divididos y PINes numéricos. Para guardar, la bóveda debe
  seguir desbloqueada hasta que Android entregue el formulario: el bloqueo automático por
  inactividad dispone de intervalos granulares configurables (desde 5 segundos hasta 5 minutos)
  para que el traspaso de credenciales sea siempre seguro y fluido sin cierres prematuros.
- **Credential Manager en Android 14+**: la app se declara como proveedor de passkeys y
  también de contraseñas (`TYPE_PUBLIC_KEY_CREDENTIAL` y `TYPE_PASSWORD_CREDENTIAL`). Las
  apps modernas que pidan credenciales por esta API pueden recibir una contraseña guardada
  o pedir guardar una nueva, siempre con confirmación y bóveda desbloqueada.

La coincidencia es conservadora: solo se ofrecen credenciales si el dominio web o el
paquete de aplicación coincide con lo que está guardado en la entrada. Si una app dibuja
sus propios campos o no usa Autofill/Credential Manager, Android no llama a la bóveda y
ningún gestor puede forzar su aparición.

### Menú lateral, selección múltiple y ordenación

Deslizando desde el borde izquierdo (o tocando el icono de menú) se abre un panel con
Generador, Passkeys, Autenticador 2FA, Ajustes y Bloquear, además del nombre de la app y
la versión compilada. En la lista, mantener pulsada una entrada entra en modo selección:
se pueden marcar varias (o todas de golpe, con el botón de seleccionar todo) y borrarlas
juntas en vez de una por una.

La cabecera de la lista integra en una misma barra compacta el buscador, selector de filtros
(Todo, Claves, Passkeys, Notas, Favoritos) y un menú desplegable de **ordenación avanzada**:
- **Nombre (A - Z)**
- **Nombre (Z - A)**
- **Modificación reciente** (las editadas más recientemente primero)
- **Creación reciente** (las más nuevas primero)
- **Antigüedad** (las más antiguas primero)

La lista incorpora además un motor de **agrupación inteligente por servicio/subdominio** (`AgrupadorSitios`):
- Discrimina entre servicios con subdominios específicos (por ejemplo, cuentas de `account.xiaomi.com` se agrupan bajo `account.xiaomi` en la letra A, mientras que `xiaomi.com` se agrupa bajo `xiaomi` en la letra X).
- Ordena los grupos por su nombre visible real para garantizar perfecta correlación alfabética y cronológica sin saltos extraños.
- **Conmutable en Ajustes > Apariencia**: Se puede activar o desactivar en cualquier momento la opción *"Agrupar cuentas por sitio"* para alternar entre la vista agrupada con tarjetas plegables y una vista plana 100% individual.

Las etiquetas no contienen espacios: al guardar, `# trabajo` se normaliza como `#trabajo`.
La normalización también se aplica a etiquetas antiguas al mostrarlas, tanto en la lista
principal como en la edición y el detalle de cada entrada.

### Abecedario Lateral y Desplazamiento Rápido con Ola Estilo Niagara Launcher

Para listas con 5 o más entradas, el extremo derecho de la pantalla incorpora un índice alfabético táctil con localización completa en español (`#`, `A` a `Z` y `Ñ` en su posición alfabética correcta):

- **Efecto de Ola Fluida Continua (Niagara Launcher Wave)**:
  Al tocar o deslizar el dedo por el lateral, las letras forman un arco dinámico que sigue de forma tridimensional y orgánica el movimiento del pulgar, mediante una ventana de curvatura de medio coseno de orden $1.15$ que garantiza una transición suave y continua ($C^1$) con la columna vertical.
- **Escala Progresiva Acelerada por GPU (120 FPS)**:
  Las letras que entran en la ola aumentan progresivamente de tamaño a medida que ascienden por la curva hasta alcanzar su tamaño máximo en la cresta (configurable hasta 3.5x), encogiéndose armónicamente al descender hasta restablecer su escala base. La traslación y el escalado se procesan a nivel de RenderNode con `Modifier.graphicsLayer`, garantizando 120 FPS constantes sin recomposiciones de UI.
- **Círculo Flotante de Gran Formato en la Cresta**:
  Un globo indicador de 78 dp de diámetro, sin bordes, con fondo en degradado ámbar, sombra suave (`elevation = 14.dp`) y letra tipográfica en 38 sp proyectada hacia el centro de la pantalla.
- **Zona de Arrastre Táctil Cómoda**:
  El área de captura táctil se extiende 45 dp de forma predeterminada (configurable de 26 a 90 dp) hacia el interior desde el píxel 0 del borde físico, permitiendo iniciar el desplazamiento con total naturalidad sin tener que apuntar con precisión milimétrica sobre las letras. Las tarjetas de la lista disponen de un margen adaptativo de 36 dp para que el abecedario nunca tape los controles.
- **Control de Color y Luminosidad del Abecedario**:
  Control deslizante dedicado para regular la tonalidad y brillo de las letras de 10% a 100%, interpolando suavemente desde un tono oscuro y discreto (`#38404E`) hasta un blanco puro de alto contraste (`#FFFFFF`), adaptándose al gusto visual del usuario sin saturaciones estridentes.
- **Valores Predeterminados Optimizados y Persistencia Automática**:
  Amplitud de curvatura en 110 dp, alcance vertical en 250 dp, aumento de letras en cresta en 1.6x, zona táctil de arrastre en 45 dp y tono al 55%. Migración transparente de preferencias previas y botón de un solo toque para restablecer estos valores en cualquier momento.
- **Sección en Ajustes > "Abecedario lateral" con Vista Previa en Vivo**:
  Tarjeta de configuración completa con controles deslizantes (amplitud de ola, alcance vertical, escala de letras, proyección de burbuja, zona táctil y luminosidad de las letras), textos de ayuda multilínea, interruptores para activar/desactivar la ola o la vibración háptica, una **vista previa interactiva en vivo** donde probar los cambios en tiempo real, y un botón para **restablecer los valores por defecto**.

### Arquitectura Visual, Botones Normalizados y Navegación Universal

La aplicación cuenta con un sistema de diseño modular basado en contenedores semánticos (`ContenedorPrincipal`, `ContenedorSeccion`, `ContenedorTarjeta`, `ContenedorFila`, `ContenedorDestacado`, `CabeceraPantalla`, `BotonColorido`, `TarjetaAccion`), garantizando consistencia geométrica, márgenes uniformes y accesibilidad con cálculo dinámico de contraste WCAG (`ColorSobreAcento`, `ColorSobreTarjetas`, luminancia automática).

- **Navegación Universal con Flecha Atrás (`volverAtras`)**:
  Todas las pantallas secundarias y modales de la aplicación cuentan con una cabecera normalizada (`CabeceraPantalla`) que incluye botón de retroceso (`ArrowBack`) conectado a la pila real de navegación del `VaultViewModel`. Se elimina el comportamiento anterior de botones "Volver" rígidos que saltaban a la pantalla de inicio, respetando ahora el flujo natural entre menús y sincronizado con el botón/gesto de retroceso de Android (`BackHandler`).
- **Normalización de Botones Interactivos**:
  Toda la app (Ajustes, Papelera, Salud de la bóveda, Autenticador 2FA, Passkeys, Generador, Detalle y Edición) utiliza tarjetas y botones con altura normalizada a 52 dp y microinteracciones hápticas reactivas (`BotonColorido`, `BotonAmbar`, `BotonBorde`), vinculados a colores semánticos con significado funcional.
- **Pantalla de Desbloqueo y Animación Continua de Bóveda**:
  Mecanismo visual de 4 anillos concéntricos animados por un reloj de fotogramas por hardware (`withFrameMillis`), garantizando rotación angular fluida e ininterrumpida incluso si el sistema tiene desactivada la escala de duración de animaciones (`animator_duration_scale = 0`). Campo de contraseña con botón de visibilidad (`Icons.Filled.Visibility`) embebido y botones de acción normalizados a 52 dp.
- **Pantalla de Detalle de Entrada**:
  Presentación a ancho completo para evitar saltos de línea en correos o URLs largas. Reemplazo del desenfoque translúcido de contraseña por una máscara opaca y segura de puntos grandes (`•`), con fila de acciones inferior dedicada (contador de caracteres, revelado y copiado rápido) y botón de favoritos en la cabecera con actualización reactiva instantánea.
- **Pantalla de Edición**:
  Botón de generación de contraseña normalizado a 52 dp en división 50/50 junto a un menú desplegable para alternar directamente entre modos (Aleatoria, Diceware, Patrón) sin abandonar la pantalla ni perder los datos escritos. Selector de tipo de entrada en menú desplegable protegido (bloqueado en edición de cuentas existentes para prevenir corrupción de tipo) y unificación del cálculo de entropía y tiempo de fuerza bruta idéntico al generador dedicado.

En **Ajustes > Apariencia** se dispone de una suite completa de personalización en tiempo real, donde cualquier cambio se refleja inmediatamente en toda la app sin parpadeos ni reinicios:

1. **Personalización de colores y tema (`PantallaTema`)**:
   * **Color dinámico del sistema (Material You / Monet en Android 12+)**: Sincronización automática con la paleta y fondo de pantalla del dispositivo (`dynamicLightColorScheme` y `dynamicDarkColorScheme`).
   * **Desacoplamiento total**: El color del icono del launcher (las 20 variantes de `activity-alias` de Material Design / Fossify) es independiente de los colores internos.
   * **Colores Semánticos por Sección Funcional**: Asigna y personaliza colores independientes para cada módulo funcional de la aplicación:
     * *Seguridad y Acceso*
     * *Autenticador 2FA*
     * *Passkeys*
     * *Generador de Claves*
     * *Salud de la Bóveda*
     * *Papelera de Reciclaje*
     * *Copia de Seguridad y Exportación*
   * **Selectores cromáticos individuales**: Paletas rápidas y diálogo con selector manual HSV de color en tiempo real para:
     * Color de acento principal (botones, estados activos, selectores).
     * Color de íconos internos (independiente del acento).
     * Color de títulos y cabeceras de sección.
     * Color de tarjetas y superficies.
   * **Contraste dinámico garantizado**: El texto sobre botones y tarjetas calcula automáticamente su luminancia para asegurar perfecta legibilidad tanto en temas claros como oscuros.
   * Botón para restaurar la paleta predeterminada.

2. **Personalización de bordes y formas (`PantallaFormas`)**:
   * **Curvatura de esquinas**: Control deslizante continuo de 0 dp a 32 dp (desde esquinas vivas/rectangulares hasta bordes en píldora suave estilo Material 3).
   * **Grosor del borde**: Slider de 0 dp a 4 dp (desde diseño plano minimalista sin contornos hasta trazo grueso de alto impacto).
   * **Estilo y matiz perimetral**: Selector de trazo entre *Sutil*, *Acento*, *Marcado* o *Sin borde*.
   * **Espaciado de componentes**: Control deslizante de 6 dp a 24 dp para regular la separación y aire entre tarjetas, secciones y elementos interactivos.
   * **Presets rápidos de diseño**: Atajos de un toque para *Redondeado Moderno*, *Neobrutalista / Recto*, *Píldora M3*, *Compacto Técnico* y *Sin bordes*.
   * **Vista previa interactiva en vivo**: Tarjeta superior con campo de texto, botón principal y secundario que responden instantáneamente.
   * Botón para restablecer la geometría original.

3. **Personalización de tipografía y textos (`PantallaTipografia`)**:
   * **Escalado global de fuente**: Control deslizante de 0.80x a 1.35x con lectura porcentual (-20% a +35%) que escala armoniosamente todos los textos de la interfaz.
   * **Familias tipográficas**: Soporte nativo para *Sans-Serif*, *Monospace (Terminal / Hacker)*, *Serif (Editorial)* y *Cursiva*.
   * **Grosor y peso de texto**: Selector de densidad (*Fino*, *Normal*, *Medio*, *Seminegrita*, *Negrita*).
   * **Estilo cursiva / itálico**: Conmutador para activar o desactivar inclinación de fuente en toda la interfaz.
   * **Espaciado entre letras (*kerning*)**: Slider de -0.5 sp a +2.0 sp para condensar o expandir las palabras.
   * **Interlineado (*line height*)**: Slider de 0.85x a 1.40x para ajustar la altura de línea de los párrafos.
   * **Presets rápidos**: *Equilibrado*, *Terminal Mono*, *Editorial*, *Accesibilidad* y *Compacto*.
   * **Vista previa interactiva**: Muestra en tiempo real contraseñas monoespaciadas, títulos, subtítulos, botones y chips de estado.
   * Botón para restablecer la tipografía original.

4. **Avatares generativos y catálogo de marcas offline**:
   * **Degradados generativos únicos**: Cada servicio o entrada tiene un avatar con degradado HSV único y determinista basado en el hash del dominio o título.
   * **Catálogo vectorial de marcas (~40 servicios populares)**: Vectores locales integrados (Google, Apple, GitHub, Microsoft, Amazon, Spotify, Netflix, Steam, PayPal, Discord, Reddit, Telegram, WhatsApp, Instagram, Facebook, X/Twitter, Dropbox, LinkedIn, MercadoLibre, Uber, Airbnb, GitLab, Bitwarden, Proton, Twitch, Epic Games, YouTube, TikTok, Pinterest, eBay, Adobe, Cloudflare, OpenAI/ChatGPT, Wikipedia, Yahoo, Outlook, Notion, Slack, Zoom, Mastodon) que se muestran nítidamente sobre el avatar generativo con color de contraste automático.

5. **Microinteracciones y animaciones**:
   * **Temporizador TOTP circular animado (`AnilloTotp`)**: Cuenta atrás continua con anillo vectorial que muta suavemente de ámbar a rojo en los últimos segundos críticos.
   * **Deslizamiento para copiar (`Swipe-to-Action`)**: En la lista principal, deslizar hacia la derecha copia el usuario y hacia la izquierda copia la contraseña, con confirmación háptica inmediata.
   * **Transiciones de navegación**: Animaciones suaves de entrada y salida con muelles físicos (`AnimatedContent` con `spring`).

6. **Ajustes visuales adicionales**:
   * **Tema**: Sistema (sigue al teléfono), claro u oscuro (con tonos de descanso visual nocturno).
   * **Nombre dentro de la app**: Personaliza el rótulo mostrado en la cabecera del menú lateral.
   * **Densidad de lista**: Opciones *Predeterminada*, *Cómoda* y *Compacta* para maximizar la cantidad de entradas visibles por pantalla.
   * **Tarjetas plegables**: Acordeón interactivo que mantiene organizados los ajustes avanzados.

### Campos Personalizados (Custom Fields)

Muchas cuentas y servicios requieren información adicional más allá de un usuario y contraseña estándar:
preguntas de seguridad, PIN secundario de cajero o tarjeta, frases mnemónicas de recuperación (12 o 24 palabras),
números de cliente, IBAN, identificadores fiscales o tokens de soporte.

- **Tres tipos de datos especializados**:
  - `TEXTO`: Datos visibles directamente (identificadores, respuestas públicas, notas cortas).
  - `OCULTO`: Datos confidenciales protegidos con enmascaramiento con puntos (`••••••`), botón de revelar/ocultar y botón de copiado protegido al portapapeles.
  - `PIN`: Códigos numéricos protegidos y teclados adaptados.
- **Edición dinámica**: Posibilidad de añadir campos ilimitados, asignar etiquetas personalizadas, cambiar el tipo en cualquier momento o eliminarlos con un toque.
- **Compatibilidad total**: Almacenados de forma nativa en la estructura de datos cifrada de cada entrada, manteniendo total retrocompatibilidad con bóvedas previas.

### Generador Criptográfico: Aleatorio, Diceware (3 a 12 palabras) y Patrones

El motor criptográfico (`PasswordGenerator`) ofrece tres modalidades integradas tanto en la pantalla del
generador dedicada como directamente dentro del formulario de edición y creación de credenciales:

1. **Contraseñas Aleatorias Fuertes**:
   - Generación mediante `SecureRandom` del sistema.
   - Longitud ajustable (4 a 64 caracteres).
   - Conmutadores independientes para mayúsculas, minúsculas, dígitos y símbolos.
   - Medidor continuo de entropía en bits y clasificación de robustez en tiempo real.

2. **Frases de Paso Diceware (Passphrases)**:
   - Pensadas para contraseñas maestras, credenciales de WiFi o dispositivos donde es tedioso escribir símbolos (Smart TVs, consolas de videojuegos).
   - **Selector de 3 a 12 palabras**: Permite escoger con precisión el número exacto de palabras de la frase (3, 4, 5, 6, 7, 8, 9, 10, 11 o 12 palabras).
   - Catálogo local integrado en español de palabras legibles sin ambigüedad.
   - **Separador personalizable**: Guion (`-`), espacio (` `), barra baja (`_`), punto (`.`) o sin separación.

3. **Motor de Generación por Patrones Personalizados**:
   - Permite definir máscaras exactas para formatos específicos (claves de producto, activaciones, PINes estructurados):
     - `X`: Carácter alfanumérico en mayúscula (`A-Z`, `0-9`).
     - `A`: Letra mayúscula (`A-Z`).
     - `a`: Letra minúscula (`a-z`).
     - `9` o `d`: Dígito numérico (`0-9`).
     - `w`: Palabra aleatoria de frase de paso (Diceware).
     - Cualquier otro carácter se mantiene como **literal** (guiones, dos puntos, barras, espacios).
   - **Presets instantáneos**: *Licencia Windows* (`XXXXX-XXXXX-XXXXX-XXXXX-XXXXX`), *PIN 4 dígitos* (`9999`), *PIN 6 dígitos* (`999999`), *Token Hex/Alfanumérico* (`XXXX-XXXX-XXXX-XXXX`), *Frase con dígito* (`w-w-w-99`).
   - Cálculo automático de entropía en bits según la combinación de comodines utilizada.

### Mosaico de Ajustes Rápidos ("Quick Settings Tile - Generador Rápido")

Para situaciones donde necesitas generar una contraseña o código sobre la marcha sin abrir la app ni desbloquear
la bóveda (por ejemplo, al registrarte rápidamente en una app o sitio web):

- **Acceso desde la cortina de Android**: Mosaico "Generador Rápido" en los Quick Settings de Android (`TileService`).
- **Totalmente personalizable en Ajustes > Generador Rápido**:
  - **Modo de generación**: Longitud fija (10, 15, 20 o 30 caracteres) o por patrón personalizado.
  - **Patrón a medida**: Define la máscara exacta que usará el mosaico.
  - **Copiar al portapapeles**: Activa o desactiva la copia automática del valor generado al pulsar el tile.
  - **Aviso flotante (Toast)**: Muestra una notificación emergente con los primeros caracteres generados.
  - **Microvibración háptica**: Confirmación táctil instantánea al pulsar el botón del mosaico.

### Widget de Escritorio para Códigos 2FA

AppWidget nativo para la pantalla de inicio de Android (`WidgetTotpFavoritos`) que permite consultar rápidamente
los códigos de verificación en dos pasos sin necesidad de navegar por los menús:

- **Códigos en vivo**: Muestra las cuentas 2FA marcadas como favoritas en la bóveda, con sus códigos TOTP activos y los segundos restantes antes de su expiración.
- **Seguridad contextual**: Si la bóveda está bloqueada, el widget oculta los códigos y muestra un aviso de seguridad con un botón de un toque para abrir y desbloquear la app.
- **Sincronización reactiva**: El widget se actualiza automáticamente cada vez que se desbloquea la bóveda, se añade una cuenta o se cierra la sesión.

### Seguridad Incondicional: FLAG_SECURE Permanente y Bloqueo Granular

La seguridad no se negocia ni se relaja:

- **`FLAG_SECURE` Activo y Permanente**:
  - Aplicado incondicionalmente a nivel de ventana en todas las pantallas de la aplicación (`MainActivity`, autenticación de Autofill y las cuatro actividades del Credential Manager).
  - **Sin interruptores**: No existe ninguna opción en ajustes que permita desactivarlo. Esto garantiza protección ineludible contra grabaciones de pantalla encubiertas, spyware con permisos de captura, troyanos bancarios y filtraciones accidentales en la vista de aplicaciones recientes de Android.
  - Información transparente para el usuario en *Ajustes > Seguridad y acceso*.
- **Intervalos Granulares de Auto-bloqueo**:
  - Para evitar cierres prematuros durante el uso de Autofill o traspaso de datos entre aplicaciones, el bloqueo automático por inactividad ofrece una escala granular y precisa:
    `5 s`, `10 s`, `15 s`, `20 s`, `25 s`, `30 s`, `40 s`, `50 s`, `60 s (1 min)`, `120 s (2 min)` y `300 s (5 min)`.
  - El temporizador se reinicia de manera inteligente cada vez que la app pasa a primer plano.

### Autenticador 2FA

En Ajustes > Autenticador 2FA se configuran valores predeterminados para claves Base32
manuales: SHA1/SHA256/SHA512, 6/7/8 dígitos y períodos de 30/60/90 segundos. Los QR
`otpauth://` conservan sus propios parámetros. También se puede elegir mostrar `123 456`
o `123456`.

La versión actual es `1.0.1` (con versionado semántico automático en Gradle).

### Passkeys

Las passkeys se guardan como entradas propias: la clave privada queda cifrada dentro de
la bóveda y la pantalla muestra solo el servicio, dominio, cuenta y algoritmo. El
identificador interno de la credencial no se muestra para evitar confundirlo con los
identificadores que presentan los administradores de cada servicio.

El autenticador local usa un AAGUID estable para nuevas passkeys, mientras que cada
credencial conserva su propio identificador aleatorio. Las passkeys ya creadas no cambian
retroactivamente.

La agrupación de sitios usa la Public Suffix List mediante Guava para calcular el dominio
registrable en cualquier país (`.co.za`, `.com.au`, `.edu.ec`, etc.), sin depender de una
lista manual cerrada. Las actualizaciones futuras de Guava incorporan nuevos sufijos cuando
la Public Suffix List los reconoce.

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

### Diagnóstico y Auditoría Local: Diagnóstico de Seguridad

Accesible directamente desde el **Menú lateral > Diagnóstico de seguridad**, presenta un panel completo de telemetría de hardware y sistema en vivo, leyendo directamente las capacidades reales del dispositivo sin permisos de red ni recolección de datos privados:

- **Dispositivo y Procesador**: Modelo comercial, fabricante de la placa base, SoC del sistema (`ro.soc.model` / `ro.board.platform`), arquitecturas ABI compatibles y número total de núcleos de CPU detectados.
- **Memoria y Almacenamiento**: Estado en tiempo real de la memoria RAM del sistema (RAM disponible, RAM total y porcentaje en uso), asignación máxima de memoria Heap JVM para la app y espacio libre disponible en el almacenamiento interno privado.
- **Sistema Operativo y Parche de Seguridad**: Versión exacta de Android, nivel de API del SDK, fecha publicada del parche de seguridad del fabricante y estado activo incondicional de la bandera de ventana `FLAG_SECURE`.
- **Biometría y Hardware**: Clase de autenticación biométrica reconocida por Android (Biometría Fuerte Clase 3 o Modo Compatible Clase 2), presencia y soporte del sensor de huellas dactilares.
- **Sensores de Cámara**: Cantidad de lentes físicas detectadas (traseras y frontales) y nivel de compatibilidad de hardware del subsistema Camera2 (`LIMITED`, `FULL`, `LEVEL_3`).
- **Aislamiento de Red Offline**: Confirmación en tiempo real del Manifiesto de la app, certificando la ausencia absoluta del permiso `android.permission.INTERNET` e incapacidad física para transmitir datos.
- **Exportación Técnica**: Botones dedicados para copiar al portapapeles o compartir mediante el selector del sistema el informe técnico de diagnóstico completo y estructurado.

### Registro de Eventos en Vivo

Ubicado en el **Menú lateral > Registro de eventos**, ofrece una consola técnica diseñada para auditar el funcionamiento interno de la app o diagnosticar problemas en dispositivos específicos:

- **Buscador en Tiempo Real**: Filtrado dinámico e instantáneo de eventos por palabras clave, códigos o mensajes de estado.
- **Filtro por Categorías**: Chips de acceso rápido para aislar eventos de:
  - *Todos*: Traza completa de la sesión.
  - *Bóveda*: Eventos de ciclo de vida (apertura, bloqueo, guardado, reintentos).
  - *Huella*: Operaciones del subsistema biométrico y Keystore.
  - *Cámara*: Inicialización de motores CameraX o cámara de respaldo.
  - *Autofill*: Peticiones del framework de autorrelleno y Credential Manager.
  - *Errores*: Eventos críticos y excepciones resaltados visualmente en color de alerta.
- **Tarjetas de Evento con Metadatos**: Cada evento se visualiza en una tarjeta con su correspondiente distintivo de color, marca de tiempo y mensaje técnico.
- **Acciones Rápidas**: Opciones de un solo toque para copiar los eventos filtrados al portapapeles o compartirlos.
- **Privacidad Inquebrantable**: No registra ni procesa información confidencial del usuario (nunca guarda contraseñas, PINes, títulos, URLs, usuarios ni contenido de códigos QR).

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
keytool -genkeypair -v -keystore boveda-local.jks -alias bovedalocal \
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
  necesita más pruebas reales con apps que usen esa API.
- **Batería completa de pruebas automatizadas (140 tests unitarios):** Toda la lógica de negocio,
  seguridad y utilidades cuenta con cobertura exhaustiva en JVM (`./gradlew testDebugUnitTest`):
  criptografía AES-256-GCM, pares EC P-256 y firmas WebAuthn, zeroización de memoria,
  importador CSV (Google, Bitwarden, LastPass, Chrome), medidor de fuerza y entropía (zxcvbn),
  generador Diceware (3 a 12 palabras) y motor de patrones, OtpAuth y TOTP, modelos y campos
  personalizados, filtros de búsqueda, 5 criterios de ordenación, métricas de salud de la bóveda,
  agrupación avanzada de sitios y resolución de dominios/paquetes en Autofill y Credential Provider.
  Los tests instrumentados de Keystore y Argon2 también están verificados.
  Aun así, dos móviles no son un banco de pruebas exhaustivo de todos los fabricantes ni versiones de Android.
- **Aquí es donde me vienes bien tú.** Si la pruebas y algo se rompe, me vendrá
  fenomenal que me lo digas: qué móvil, qué versión de Android, qué hiciste y qué pasó.
  Lo más útil es el Registro en Ajustes > Transparencia > Registro, que lleva los eventos
  técnicos y ningún dato tuyo. Un fallo que encuentres es un fallo que dejo de tener. Abre un
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

## Arquitectura y Guía para Desarrolladores / Agentes IA

Para entender en profundidad cómo están estructuradas las conexiones internas de toda la aplicación, el flujo unidireccional de datos con Jetpack Compose y StateFlow, la pila de navegación, la concurrencia segura en `VaultRepository`, los servicios de sistema (Autofill, Passkeys, Credential Manager, Quick Settings Tile, App Widget) y los invariantes de seguridad inquebrantables, consulta la guía completa:

👉 **[ARQUITECTURA.md](ARQUITECTURA.md)**

El código lo ha revisado quien lo ha escrito, que es el peor revisor posible. Por eso
está aquí entero y por eso te pido que lo mires: no te pido confianza, te pido que lo
compruebes.

