# Bóveda Local

Gestor de contraseñas para Android. Sin cuentas, sin nube, sin internet.

**Bóveda Local** (com.jlnavas3.bovedalocal) es una aplicación de gestión de contraseñas 100% offline diseñada para Android. Ofrece cifrado de alta seguridad, soporte para 2FA y Passkeys, autocompletado en el sistema, y características de seguridad avanzadas en una interfaz desarrollada en Kotlin y Jetpack Compose.

---

## Permisos

Bóveda Local está diseñada desde la base para no poder comunicarse con la red. **NO existe el permiso de INTERNET en la aplicación**, por lo que es imposible que tus datos salgan de tu dispositivo sin tu intervención directa.

| Permiso | Uso |
| :--- | :--- |
| `USE_BIOMETRIC` | Permite desbloquear la bóveda utilizando tu huella digital o biometría del dispositivo. |
| `VIBRATE` | Proporciona microinteracciones hápticas para una mejor experiencia de usuario. |
| `CAMERA` | Solo se utiliza para leer códigos QR al añadir tokens de autenticación de dos factores (2FA). Solo se activa si el usuario pulsa explícitamente "Escanear". |

---

## Cómo guarda las contraseñas

La bóveda utiliza estándares criptográficos modernos para asegurar que solo tú puedas acceder a los datos. La contraseña maestra **nunca se almacena** en ningún formato.

*   **Derivación de clave (KDF)**: Utiliza **Argon2id** para resistir ataques de fuerza bruta, con tres perfiles configurables según el dispositivo:
    *   *Estándar*: 64 MiB de memoria, 3 iteraciones, paralelismo 4.
    *   *Reforzado*: 128 MiB de memoria, 4 iteraciones, paralelismo 4.
    *   *Ultraseguro*: 256 MiB de memoria, 5 iteraciones, paralelismo 8.
*   **Cifrado**: Los datos se cifran utilizando **AES-256-GCM**.
*   **Unicidad**: Cada vez que se guarda la base de datos, se genera una nueva Sal (16 bytes) y un nuevo Nonce (12 bytes).
*   **Autenticidad**: La cabecera del archivo de la bóveda está autenticada como Datos Asociados (AAD) para evitar manipulaciones.
*   **Biometría segura**: Al usar la huella, la clave maestra se envuelve mediante el *Android Keystore* utilizando hardware respaldado (modo Fuerte Clase 3 o Compatible).
*   **Gestión de memoria**: Los secretos y contraseñas se borran de la memoria RAM inmediatamente después de su uso utilizando técnicas de zeroización (`Zeroizar.borrar()`).

---

## Qué hay dentro

Bóveda Local cuenta con un amplio abanico de funcionalidades, superando a muchos gestores tradicionales:

### Gestión y Productividad
*   **Servicio Autofill de Android**: Autocompleta credenciales directamente en otras apps y navegadores.
*   **Proveedor de Credenciales (Android 14+)**: Soporte moderno para iniciar sesión con **Passkeys** y contraseñas estándar.
*   **Campos personalizados**: Añade campos dinámicos a tus entradas (texto, texto oculto, PIN).
*   **Organización**: Etiquetas y filtros por categoría, selección múltiple para borrar varias entradas a la vez.
*   **Navegación fluida**: Índice alfabético lateral interactivo con efecto ola (estilo Niagara Launcher).
*   **Historial**: Mantén un registro de las contraseñas anteriores de cada cuenta.
*   **Papelera de reciclaje**: Posibilidad de restaurar elementos eliminados hasta 30 días después.

### Autenticador 2FA integrado
*   Autenticador TOTP (RFC 6238) integrado con lector QR basado en ZXing.
*   **Widget de escritorio** para acceder rápidamente a los códigos 2FA favoritos.

### Generador de Contraseñas
*   Generador criptográficamente seguro (usando `SecureRandom`).
*   Soporte para contraseñas aleatorias clásicas, frases tipo Diceware (de 3 a 12 palabras) y patrones personalizados.
*   **Quick Settings Tile**: Generador de contraseñas rápido directamente desde los ajustes rápidos de Android.
*   Medidor de fuerza de contraseñas integrado (zxcvbn).

### Copias de Seguridad y Migración
*   Exportación e importación de la bóveda cifrada en formato `.bvda`.
*   Importación desde archivos CSV provenientes de Google, Chrome, Bitwarden, LastPass, etc.
*   Recordatorios automáticos para realizar exportaciones de seguridad offline.

### Personalización Visual
*   Interfaz construida con Jetpack Compose y Material 3.
*   Tema Claro, Oscuro y soporte para Material You (Monet) adaptable a los colores del sistema.
*   Suite completa de personalización visual: ajusta colores, bordes, tipografías y formas.
*   Posibilidad de elegir entre 20 variantes del icono de la aplicación.

---

## Seguridad Avanzada

Además de su robusta criptografía, la aplicación implementa mecanismos de seguridad física y contra espionaje:

*   **FLAG_SECURE**: Configurable por el usuario (activado por defecto) para bloquear capturas de pantalla y grabación de pantalla.
*   **Bóveda Señuelo**: Permite configurar un PIN falso que, al introducirse, abre una bóveda vacía o con datos falsos (para situaciones de coacción).
*   **PIN de Autodestrucción**: Si se introduce este PIN específico, la bóveda y todas sus copias locales se borran irreversiblemente.
*   **Bloqueo automático**: Por inactividad, configurable desde 5 segundos hasta 5 minutos.
*   **Limpieza de portapapeles**: Borrado automático de las contraseñas copiadas tras un tiempo determinado.
*   **Kit de emergencia**: Opción para imprimir o guardar de forma segura una hoja de recuperación.
*   **Diagnóstico y auditoría**: Herramienta de salud de la bóveda y registro (log) de eventos estrictamente local.

---

## Estructura del proyecto

El proyecto sigue una arquitectura moderna, modular y reactiva:

```text
com.jlnavas3.bovedalocal/
├── BovedaApp.kt              (Clase Application, inicialización global)
├── autofill/                 (Implementación del AutofillService y utilidades)
├── camara/                   (Motor de escaneo QR: CameraX, compatibilidad legacy y lector de imágenes)
├── crypto/                   (Implementaciones de Argon2, AES-256-GCM, TOTP, Keystore y generador seguro)
├── data/                     (Modelos de datos, Preferencias/Ajustes, Repositorio e Importador CSV)
├── passkey/                  (CredentialProvider para Android 14+, WebAuthn y procesamiento CBOR)
├── quicksettings/            (Servicio para el Quick Settings Tile)
├── ui/
│   ├── MainActivity.kt       (Punto de entrada de la app + navegación basada en AnimatedContent)
│   ├── Pantalla.kt           (Definición de las 22 pantallas mediante sealed classes)
│   ├── VaultViewModel.kt     (ViewModel principal para la gestión de estados)
│   ├── VaultViewModelAjustes.kt  (Delegado del ViewModel para los ajustes)
│   ├── VaultViewModelBackup.kt   (Delegado del ViewModel para las copias de seguridad)
│   ├── FlujoBiometria.kt     (Lógica del flujo de autenticación biométrica)
│   ├── componentes/          (Elementos UI reutilizables construidos en Compose)
│   ├── pantallas/            (Implementación Composable de las 22 pantallas)
│   └── theme/Tema.kt         (Sistema de tematización dinámico)
├── util/                     (Utilidades transversales: Háptica, Portapapeles, Diagnóstico, etc.)
└── widget/                   (Implementación del Widget de 2FA para el escritorio)
```

---

## Compilar y firmar

Este proyecto requiere Java 17 y está configurado con **compileSdk: 36**, **minSdk: 29**, y **targetSdk: 35**.

### Comandos de Gradle

```bash
# Construir versión de desarrollo (Debug)
./gradlew assembleDebug

# Ejecutar pruebas unitarias
./gradlew testDebugUnitTest

# Ver versión actual del proyecto
./gradlew mostrarVersion

# Herramientas para incrementar la versión
./gradlew bumpPatch   # Sube el tercer número (ej: 1.2.0 -> 1.2.1)
./gradlew bumpMinor   # Sube el segundo número (ej: 1.2.0 -> 1.3.0)
./gradlew bumpMajor   # Sube el primer número (ej: 1.2.0 -> 2.0.0)
```

### Firmar versión de Release

Para compilar la versión de distribución (Release), se requiere la configuración del archivo `keystore`.

1. Crea o localiza un archivo keystore válido (ej. `boveda-local.jks`).
2. Copia el archivo `keystore.properties.ejemplo` a `keystore.properties`.
3. Rellena los datos en `keystore.properties` apuntando a tu `.jks`.

```bash
# Construir versión de distribución (Release)
./gradlew assembleRelease
```

---

## Licencia

Este proyecto está bajo la licencia **MIT**. Eres libre de usar, modificar y distribuir el código.
