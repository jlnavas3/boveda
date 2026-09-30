# Bóveda Local

[![Kotlin](https://img.shields.io/badge/Kotlin-2.x-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Platform](https://img.shields.io/badge/Android-Min%2029%20%7C%20Target%2035%20%7C%20Compile%2036-3DDC84?logo=android&logoColor=white)](https://developer.android.com/)
[![Tests](https://img.shields.io/badge/Tests-252%2F252%20Passing-brightgreen?logo=gradle)](https://gradle.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**Bóveda Local** (`com.jlnavas3.bovedalocal`) es una plataforma de alta seguridad criptográfica para la custodia local de credenciales, generación de tokens de autenticación de dos factores (**TOTP RFC 6238**) y proveedor oficial de llaves de acceso (**Passkeys / WebAuthn FIDO2**) en el sistema operativo Android.

La aplicación opera bajo una estricta política de **Confianza Cero en Red (Zero-Network Architecture)**: carece por diseño de cualquier interfaz telemática, garantizando que el material sensible jamás abandone los confines de la memoria volátil del dispositivo.

---

## 🛡️ Modelo de Aislamiento y Permisos

Bóveda Local no solicita ni declara permisos de conectividad a redes en su manifiesto (`AndroidManifest.xml`). Es telemáticamente hermética.

```xml
<!-- Manifiesto de Seguridad de Bóveda Local: CERO permisos de red -->
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- NO EXISTE: <uses-permission android:name="android.permission.INTERNET" /> -->
    <!-- NO EXISTE: <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> -->

    <uses-permission android:name="android.permission.USE_BIOMETRIC" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.CAMERA" /> <!-- Solo escáner QR local -->
</manifest>
```

| Permiso | Tipo | Justificación Técnica de Seguridad |
| :--- | :--- | :--- |
| `android.permission.USE_BIOMETRIC` | Normal | Autenticación biométrica de hardware de Clase 3 (Fuerte) o Clase 2 (Compatible) para liberar la clave maestra envuelta en el Android Keystore (`BiometricPrompt.CryptoObject`). |
| `android.permission.VIBRATE` | Normal | Respuesta háptica sub-milisegúndica en eventos de confirmación, validación de clave y deslizamiento en el índice alfabético Niagara. |
| `android.permission.CAMERA` | Peligroso (Runtime) | Lectura óptica en memoria de códigos QR estándar para tokens 2FA (`otpauth://totp/...`). El flujo óptico CameraX se decodifica en un hilo secundario y jamás persiste imágenes en disco. |

---

## 🔐 Especificación Criptográfica y Ciclo de Vida

El motor criptográfico de Bóveda Local prescinde de librerías propietarias y se adhiere a primitivas avaladas por el NIST y la IETF:

```mermaid
flowchart LR
    MasterKey["Clave Maestra (CharArray)"] --> KDF["Argon2id (RFC 9106)<br/>64–256 MiB RAM | 3–6 Iter"]
    KDF --> Key256["Clave Simétrica 256 bits"]
    
    Header49["Cabecera BVDA (49 Bytes)"] --> AAD["Additional Authenticated Data (AAD)"]
    Key256 & AAD --> AES["AES-256-GCM (NIST SP 800-38D)<br/>Nonce 96 bits | Tag 128 bits"]
    
    AES --> EncryptedFile["boveda.bvda (Cifrado Atómico fsync)"]
    Key256 --> Zeroize["Zeroizar.kt (Sobrescritura RAM 0x00)"]
```

### 1. Derivación de Clave: Argon2id (RFC 9106)
Resistente a ataques de compensación tiempo-memoria (TMTO) y computación masiva en clústeres GPU/ASIC mediante la variante híbrida `Argon2id` (v0x13):
* **Perfil Estándar:** 64 MiB RAM ($65,536\text{ KiB}$), 3 iteraciones, paralelismo = 4 lanes ($\approx 150\text{ ms}$).
* **Perfil Reforzado:** 128 MiB RAM ($131,072\text{ KiB}$), 4 iteraciones, paralelismo = 4 lanes ($\approx 300\text{ ms}$).
* **Perfil Ultra-Seguro:** 256 MiB RAM ($262,144\text{ KiB}$), 6 iteraciones, paralelismo = 4 lanes ($\approx 600\text{ ms}$).

### 2. Cifrado Autenticado: AES-256-GCM con Cabecera AAD
* **Cifrador:** `AES/GCM/NoPadding` con clave simétrica de 256 bits derivada por KDF.
* **Vector de Inicialización (Nonce):** 96 bits (12 bytes) CSPRNG generados individualmente en cada guardado.
* **Cabecera Inmutable Autenticada (49 Bytes):**
  `[MAGIC: 4B ("BVDA")][VERSION: 1B (0x01)][SALT: 16B][MEM: 4B][ITER: 4B][PAR: 4B][LEN: 4B][NONCE: 12B]`.
  La cabecera completa se vincula como **AAD** mediante `cipher.updateAAD(cabecera)`. Cualquier intento de alterar los parámetros de la cabecera (como reducir los requisitos de memoria de Argon2id) produce de inmediato una falla de integridad (`AEADBadTagException`).

### 3. Gestión y Purga de Memoria Volátil (Zeroización)
Se prohíbe el uso de cadenas inmutables (`java.lang.String`) para secretos. Toda clave o contraseña temporal reside en buffers primitivos (`CharArray`, `ByteArray`) y es inmediatamente sobrescrita con bytes nulos (`0x00`) tras su uso mediante [`Zeroizar.kt`](file:///home/jln/BovedaLocal/app/src/main/java/com/jlnavas3/bovedalocal/crypto/Zeroizar.kt).

### 4. Enlace Biométrico con Android Keystore
Al activar el desbloqueo por huella, la clave maestra se cifra y almacena en el Keystore respaldado por hardware seguro (**TEE / StrongBox Keymaster**). La clave solo se libera tras una autenticación biométrica exitosa validada por el framework del sistema mediante `BiometricPrompt.CryptoObject`.

---

## 🏛️ Arquitectura de Software y Modularización

El proyecto adopta **Clean Architecture** estructurada en MVI/MVVM con Jetpack Compose. El controlador central [`VaultViewModel.kt`](file:///home/jln/BovedaLocal/app/src/main/java/com/jlnavas3/bovedalocal/ui/VaultViewModel.kt) opera como una **Fachada Reactiva** desacoplada en 5 delegados especializados:

```mermaid
flowchart TD
    VM["VaultViewModel (Facade Central)"]
    
    VM --> D_Nav["VaultNavegacionDelegate<br/>Pila LIFO ArrayDeque + padreDe() + irPorId()"]
    VM --> D_Ent["VaultEntradasDelegate<br/>CRUD, Búsqueda Fonética, Filtros, Papelera"]
    VM --> D_Cic["VaultCicloBovedaDelegate<br/>Máquina de Estados, Backoff Anti-Fuerza Bruta"]
    VM --> D_Aju["VaultAjustesDelegate<br/>Tokens de Diseño, AjustesApp, DataStore"]
    VM --> D_Bkp["VaultBackupDelegate<br/>SAF Storage Access Framework, .bvda, CSV"]
```

---

## 🏷️ Matriz Funcional y Taxonomía Tri-Grama

Todos los módulos, submódulos y grupos de ajustes responden al estándar semántico de **Tri-Gramas con separación por guiones** (`XX-YYY[-ZZZ]`):

| Bloque Canónico | Mnemónico | Identificadores Destacados | Capacidades Principales |
| :--- | :--- | :--- | :--- |
| **00** | `AJU` | `00-AJU` | **Hub Central de Ajustes:** Vista unificada de las 18 secciones agrupadas con tarjetas redondeadas One UI 6, buscador en vivo y deep-linking con destello visual puro (sin mutaciones no deseadas). |
| **01** | `SEG` | `01-SEG-BIO`, `01-SEG-PAS`, `01-SEG-SNU`, `01-SEG-DST` | **Seguridad y Criptografía:** Biometría StrongBox, cambio de contraseña maestra, bóveda señuelo contra coacción y PIN de autodestrucción irreversible. |
| **02** | `APA` | `02-APA-THM`, `02-APA-GEO`, `02-APA-TYP`, `02-APA-ANI` | **Personalización Visual:** 20 paletas de acento, curvatura de esquinas (0–32 dp), grosor de borde (0–4 dp), espaciado dinámico (6–24 dp), tipografías del sistema con interlineado proporcional dinámico (`02-APA-TYP`) y animación estática de ahorro de energía (`02-APA-THM-NON`). |
| **03** | `LST` | `03-LST-AZX`, `03-LST-SLD`, `03-LST-DUP`, `03-LST-PAP` | **Gestión de Bóveda:** Doble FAB vertical inferior (Bloquear / Añadir), índice alfabético lateral estilo Niagara con física de ola interactiva, menú multinivel "Importar/exportar", swipe horizontal y modo comparación entre cuentas en Detalle, auditoría de salud de contraseñas, detección de duplicadas con selección múltiple y papelera. |
| **04** | `HER` | `04-HER-GEN`, `04-HER-2FA`, `04-HER-HST`, `04-HER-WGT` | **Herramientas de Productividad:** Generador CSPRNG con doble FAB vertical (Generar principal / Copiar secundario) y cabecera minimalista; Autenticador TOTP RFC 6238 con doble FAB (Escanear QR / Ingreso manual) y copia táctil de código; historial de contraseñas y calibrador de widgets. |
| **05** | `COP` | `05-COP-EXP`, `05-COP-ATM`, `05-COP-MAN` | **Copias de Seguridad:** Exportación selectiva y manual (.bvda), asociación de archivos `.bvda` en el sistema para importación y apertura directa protegida por autenticación, copias automáticas SAF e importador CSV universal (Bitwarden, Google, LastPass). |
| **06** | `SIS` | `06-SIS-DGN`, `06-SIS-LOG`, `06-SIS-ACR` | **Sistema y Auditoría:** Diagnóstico de hardware, SoC, RAM y TEE, registro de eventos exclusivamente local y kit de emergencia imprimible. |

---

## ⚙️ Integración con Servicios del Sistema Android

1. **Android Autofill Framework (`AutofillService`):**
   - Intercepta solicitudes del sistema operativo en formularios de inicio de sesión de navegadores y apps de terceros.
   - Analiza la estructura de accesibilidad (`AssistStructure`), detecta el nombre amigable de las aplicaciones instaladas (sustituyendo identificadores técnicos) y presenta datasets en `RemoteViews` con el ícono circular real de la app asociada.
2. **Credential Provider Platform (Android 14+ / API 34+):**
   - Actúa como proveedor nativo de **Passkeys** y credenciales FIDO2/WebAuthn. Procesa peticiones codificadas en CBOR, genera aserciones firmadas con curvas elípticas NIST P-256 (ES256) y despliega el nombre e ícono real de la app en la hoja de selección del sistema.
3. **Asociación de Archivos de Copia Cifrada (`.bvda`):**
   - Integra un `intent-filter` en el manifiesto para asociar y abrir directamente archivos `.bvda` desde el gestor de archivos de Android, requiriendo el desbloqueo previo de la bóveda y la contraseña de descifrado correspondiente.
4. **Panel de Ajustes Rápidos (`TileService`):**
   - Incluye un Quick Settings Tile en la cortina de notificaciones de Android para generar contraseñas criptográficas instantáneas sin necesidad de desbloquear la aplicación.
5. **Widgets de Escritorio:**
   - Widget interactivo para visualización de códigos TOTP 2FA con cuenta atrás reactiva.
   - Widget 1x1 para generación rápida de credenciales al toque.
6. **Mitigación Visual de Capturas (`FLAG_SECURE`):**
   - Activo de forma predeterminada para impedir capturas de pantalla, grabaciones de video y ocultar la previsualización de la bóveda en la ventana de apps recientes.

---

## 📂 Estructura Exhaustiva del Código Fuente

```text
com.jlnavas3.bovedalocal/
├── BovedaApp.kt                          # Application class: inyección de dependencias y ciclo de vida
├── autofill/                             # Subsistema Autofill Framework (W3C heuristics, RemoteViews)
├── camara/                               # Motor de escaneo óptico CameraX y decodificación QR ZXing
├── crypto/                               # Primitivas criptográficas (Argon2id, AES-GCM, TOTP, Zeroizar)
├── data/                                 # Capa de datos (Modelos inmutables, Repositorio, Parsers CSV)
├── passkey/                              # Credential Provider Framework (Android 14+ Passkeys / CBOR)
├── quicksettings/                        # Quick Settings Tile Service (Generador rápido)
├── ui/                                   # Capa de interfaz de usuario Jetpack Compose
│   ├── MainActivity.kt                   # Single-Activity, navegación basada en AnimatedContent y FLAG_SECURE
│   ├── Pantalla.kt                       # Sealed Class jerárquica con el catálogo de rutas
│   ├── VaultViewModel.kt                 # ViewModel Facade central
│   ├── VaultViewModelAjustes.kt          # Delegado de mutación de ajustes y tokens dinámicos
│   ├── VaultViewModelBackup.kt           # Delegado de SAF, importación y exportación de copias
│   ├── VaultViewModelCicloBoveda.kt      # Delegado de máquina de estados y temporizadores de sesión
│   ├── VaultViewModelEntradas.kt         # Delegado CRUD de credenciales, búsqueda y filtros
│   ├── VaultViewModelNavegacion.kt       # Delegado de pila LIFO y resolución canónica padreDe()
│   ├── componentes/                      # Catálogo de micro-componentes atómicos de Compose
│   │   ├── InsigniaValorBoveda.kt        # Badges cromáticos semánticos
│   │   ├── BotonBoveda.kt                # Botones primarios, secundarios y de advertencia
│   │   ├── CampoBoveda.kt                # Campos de entrada de texto formateados
│   │   ├── SwitchBoveda.kt               # Conmutadores temáticos
│   │   └── ajustes/                      # InsigniaIdAjuste, ComponenteGrupo, FilasAjustes
│   ├── pantallas/                        # Vistas organizadas por subpaquetes de dominio
│   │   ├── PantallaLista.kt              # Bóveda principal con cajón de navegación cuadrado
│   │   ├── lista/                        # BarraSuperior, Gestos de deslizamiento, Selección múltiple
│   │   ├── ajustes/                      # Hub central de ajustes y catálogo modular
│   │   ├── formas/                       # Subpáginas 02-APA-GEO (Curvatura, Grosor, Espaciado)
│   │   ├── tipografia/                   # Subpáginas 02-APA-TYP (Escala, Fuentes, Espaciado)
│   │   ├── calibracion/                  # Simuladores de engranajes y widgets
│   │   ├── autenticador/                 # Gestor 2FA con temporizadores de tarta
│   │   ├── generador/                    # Generador con medidor zxcvbn
│   │   ├── detalle/                      # Vista detallada de credenciales
│   │   └── edicion/                      # Edición reactiva con detección DAL
│   └── theme/                            # Sistema de tokens dinámicos (ColoresTema, FormasTema, Tema)
├── util/                                 # Utilidades de hardware, háptica y portapapeles
└── widget/                               # Implementación de AppWidgets de escritorio
```

---

## 🛠️ Ingeniería de Compilación y Calidad

### Requisitos del Entorno
* **JDK:** Java 17 (OpenJDK 17 o superior).
* **Android SDK:** `compileSdk = 36`, `minSdk = 29` (Android 10+), `targetSdk = 35` (Android 15).
* **Compilador:** Kotlin 2.x con optimizaciones Compose Compiler integradas.

### Comandos de Gradle

```bash
# Ejecutar la batería de pruebas unitarias automatizadas (252 tests)
./gradlew testDebugUnitTest

# Construir binario de depuración (Debug APK)
./gradlew assembleDebug

# Construir binarios optimizados de producción (Release APK y Android App Bundle)
./gradlew assembleRelease bundleRelease

# Inspección de versión del proyecto
./gradlew mostrarVersion

# Actualización semántica de versión (SemVer)
./gradlew bumpPatch   # 1.2.0 -> 1.2.1
./gradlew bumpMinor   # 1.2.0 -> 1.3.0
./gradlew bumpMajor   # 1.2.0 -> 2.0.0
```

### Configuración del Keystore de Release
Para compilar la versión de distribución firmada:
1. Copia `keystore.properties.ejemplo` a `keystore.properties`.
2. Especifica la ruta a tu almacén de claves criptográficas (`.jks`), alias y contraseñas.
3. Ejecuta `./gradlew assembleRelease`.

### Despliegue Dual en Dispositivos Físicos mediante ADB
```bash
# Instalación en dispositivo primario (ej. Google Pixel)
adb -s <SERIAL_PIXEL> install -r app/build/outputs/apk/release/app-arm64-v8a-release.apk

# Instalación en dispositivo secundario (ej. Xiaomi)
adb -s <SERIAL_XIAOMI> install -r app/build/outputs/apk/release/app-arm64-v8a-release.apk
```

---

## 📄 Licencia

Este proyecto se distribuye bajo la licencia de código abierto **MIT**. Consulta el archivo `LICENSE` para más detalles.
