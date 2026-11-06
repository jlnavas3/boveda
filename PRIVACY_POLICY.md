# Política de Privacidad / Privacy Policy — Bóveda Local

*Última actualización / Last updated: 6 de noviembre de 2026*

---

## Español

### 1. Compromiso de Privacidad y Arquitectura de Confianza Cero
**Bóveda Local** es una aplicación de código abierto diseñada bajo el principio estricto de **Confianza Cero en Red (Zero-Network Architecture)**. La privacidad y la soberanía del usuario son los pilares fundamentales del proyecto.

### 2. Recopilación y Transmisión de Datos
* **Cero telemetría:** Bóveda Local **NO** recopila, almacena, rastrea ni transmite ningún tipo de dato personal, identificador de dispositivo, estadística de uso ni informe de fallos a servidores externos.
* **Sin servidores ni nube:** No existen servidores remotos, bases de datos en la nube ni servicios de terceros asociados a la aplicación.
* **Almacenamiento 100% local:** Toda la información custodiada (contraseñas, notas, llaves de acceso Passkeys, tarjetas, documentos, contactos) reside exclusivamente en el almacenamiento interno cifrado de tu dispositivo mediante cifrado autenticado AES-256-GCM derivado con Argon2id (RFC 9106).

### 3. Permisos del Dispositivo y Uso
Bóveda Local únicamente solicita los permisos estrictamente necesarios para su funcionamiento local:
* **Cámara (`android.permission.CAMERA`):** Se utiliza exclusivamente de forma local y en tiempo real para escanear códigos QR de autenticación de dos factores (TOTP RFC 6238) y para la sincronización óptica física mediante ráfagas QR (`bvda-opt://`). Ninguna imagen, fotografía o flujo de video se guarda en disco ni se transmite fuera de la memoria volátil.
* **Biometría (`android.permission.USE_BIOMETRIC`):** Se utiliza para la autenticación local respaldada por hardware seguro (Android Keystore StrongBox / TEE). Los datos biométricos (huella, rostro) son procesados exclusivamente por el sistema operativo Android y nunca son accesibles para la aplicación.
* **Vibración (`android.permission.VIBRATE`):** Proporciona respuesta háptica táctil al desbloquear la bóveda o copiar credenciales.
* **Cero permisos de red:** En su variante hermética (*airgapped*), la aplicación carece por completo del permiso `android.permission.INTERNET`.

### 4. Proveedores y Servicios de Terceros
Bóveda Local **NO** incluye kits de desarrollo de software (SDKs) de publicidad, analíticas, rastreo o redes sociales (sin Google Analytics, sin Firebase, sin Facebook SDK, sin librerías publicitarias).

### 5. Código Abierto y Auditoría
El código fuente completo es público, auditable y está licenciado bajo la Licencia Pública General GNU v3.0 (GPL-3.0-or-later):
[https://github.com/jlnavas3/boveda](https://github.com/jlnavas3/boveda)

### 6. Contacto
Si tienes preguntas sobre esta política de privacidad o sobre las garantías criptográficas de la aplicación, puedes abrir un issue o comunicarte a través del repositorio oficial de GitHub.

---

## English

### 1. Privacy Commitment & Zero-Network Architecture
**Bóveda Local** is an open-source application engineered under a strict **Zero-Network Architecture**. User privacy, local data sovereignty, and end-to-end user custody are the founding principles of this software.

### 2. Data Collection and Transmission
* **Zero Telemetry:** Bóveda Local **DOES NOT** collect, store, track, monitor, or transmit any personal data, device identifiers, usage statistics, or crash diagnostics to any external server.
* **No Remote Servers or Cloud:** There are no remote backends, cloud databases, or third-party services linked to this application.
* **Strictly Local Storage:** All vaulted data (passwords, secure notes, Passkeys, payment cards, identity records, private contacts) is stored solely in the encrypted internal storage of your device using authenticated AES-256-GCM encryption with Argon2id key derivation (RFC 9106).

### 3. Device Permissions & Usage
Bóveda Local requests only the minimum hardware permissions necessary for local operation:
* **Camera (`android.permission.CAMERA`):** Used exclusively in real-time to scan 2FA two-factor authentication QR codes (TOTP RFC 6238) and for physical optical airgap sync via animated QR bursts (`bvda-opt://`). No images, photos, or video frames are saved to persistent storage or transmitted.
* **Biometrics (`android.permission.USE_BIOMETRIC`):** Used for hardware-backed local authentication (Android Keystore StrongBox / TEE). Biometric data is handled entirely by the Android OS secure enclave; the app never accesses raw biometric data.
* **Haptics (`android.permission.VIBRATE`):** Provides physical haptic feedback upon unlocking or copying credentials.
* **Zero Network Access:** In its hermetic (*airgapped*) flavor, the application completely purges the `android.permission.INTERNET` permission from its manifest.

### 4. Third-Party Services and Trackers
Bóveda Local contains **ZERO** third-party trackers, advertising libraries, or analytical SDKs (no Google Analytics, no Firebase Analytics, no ad networks).

### 5. Open Source & Auditability
The complete source code is public, transparent, and licensed under the GNU General Public License v3.0 (GPL-3.0-or-later):
[https://github.com/jlnavas3/boveda](https://github.com/jlnavas3/boveda)

### 6. Contact
For any questions regarding this Privacy Policy, please open an issue on the official GitHub repository.
