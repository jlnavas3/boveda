# Bóveda Local - Reglas del Proyecto

## Compilación y Despliegue en Dispositivos
Siempre que se compile una versión Release (`./gradlew assembleRelease`):
1. **Instalación en dispositivos**: Instalar el APK release para arquitectura ARM (`app-arm64-v8a-release.apk`) en todos los teléfonos conectados mediante `adb`:
   ```bash
   adb -s <device_id> install -r app/build/outputs/apk/release/app-arm64-v8a-release.apk
   ```
2. **Copia versionada en Descargas**: Copiar el APK versionado en la carpeta de descargas (`/sdcard/Download/BovedaLocal-v<version>.apk`) de todos los teléfonos conectados:
   ```bash
   adb -s <device_id> push app/build/outputs/apk/release/app-arm64-v8a-release.apk /sdcard/Download/BovedaLocal-v<version>.apk
   ```
