package com.jlnavas3.bovedalocal.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.biometric.BiometricManager
import com.jlnavas3.bovedalocal.camara.MotorCamara
import com.jlnavas3.bovedalocal.camara.PermisoCamara
import com.jlnavas3.bovedalocal.data.BovedaSenuelo
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.data.modoBiometriaActivo

object RecolectorAuditoriaHardware {

    fun recopilarAuditoria(contexto: Context, repositorio: VaultRepository): DatosAuditoria {
        val pm = contexto.packageManager
        val permisos = try {
            val pkg = pm.getPackageInfo(contexto.packageName, PackageManager.GET_PERMISSIONS)
            pkg.requestedPermissions?.toList() ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
        val tieneInternet = permisos.any { it.contains("INTERNET", ignoreCase = true) }

        val actManager = contexto.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager?.getMemoryInfo(memInfo)
        val ramTotal = memInfo.totalMem / (1024 * 1024)
        val ramLibre = memInfo.availMem / (1024 * 1024)

        val rt = Runtime.getRuntime()
        val heapMax = rt.maxMemory() / (1024 * 1024)
        val heapTotal = rt.totalMemory() / (1024 * 1024)
        val heapFree = rt.freeMemory() / (1024 * 1024)
        val heapUsado = heapTotal - heapFree

        val espacioLibre = try { contexto.filesDir.usableSpace / (1024 * 1024) } catch (e: Exception) { 0L }
        val espacioTotal = try { contexto.filesDir.totalSpace / (1024 * 1024) } catch (e: Exception) { 0L }

        val soc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try { Build.SOC_MODEL } catch (e: Exception) { null }
        } else null

        val ajustes = repositorio.ajustes.actual
        val c = Biometria.capacidad(contexto)
        val nivel = Biometria.decidirNivel(c)
        val lineasBio = mutableListOf<Linea>()
        lineasBio += Linea("Nivel biométrico: ${nivel.name.lowercase()}")
        lineasBio += Linea("Clase 3 (fuerte): ${Biometria.explicar(c.fuerte)}", ok = c.fuerte == BiometricManager.BIOMETRIC_SUCCESS, indentada = true)
        lineasBio += Linea("Clase 2 (débil): ${Biometria.explicar(c.debil)}", ok = c.debil == BiometricManager.BIOMETRIC_SUCCESS, indentada = true)
        lineasBio += Linea("PIN/Credencial: ${Biometria.explicar(c.credencial)}", ok = c.credencial == BiometricManager.BIOMETRIC_SUCCESS, indentada = true)
        val modo = ajustes.modoBiometriaActivo
        val esSenuelo = repositorio.esModoSenuelo
        val senueloConfig = if (!esSenuelo) BovedaSenuelo.cargar(contexto) else null
        val senueloActivo = senueloConfig?.activo == true

        if (esSenuelo) {
            lineasBio += Linea(
                texto = "Modo en app: desactivado",
                ok = false,
                indentada = true,
                detalle = "No configurado en los ajustes de la bóveda"
            )
        } else {
            if (senueloActivo) {
                if (modo == null) {
                    lineasBio += Linea(
                        texto = "Modo en app: desactivado (Protección Bóveda Señuelo)",
                        ok = true,
                        indentada = true,
                        detalle = "Desactivada para impedir bypass por coacción física"
                    )
                } else {
                    lineasBio += Linea(
                        texto = "Modo en app: ${modo.etiqueta}",
                        ok = false,
                        indentada = true,
                        detalle = "Advertencia: abrirá siempre la bóveda real, debilitando la señuelo"
                    )
                    val presente = repositorio.biometria.estaConfigurada(modo)
                    lineasBio += Linea(
                        texto = "Keystore hardware-backed: ${if (presente) "configurado" else "no configurado"}",
                        ok = presente,
                        indentada = true
                    )
                }
                lineasBio += Linea(
                    texto = "Bóveda señuelo (PIN coacción): activa",
                    ok = true,
                    indentada = true,
                    detalle = "PIN configurado con ${senueloConfig?.entradas?.size ?: 0} cuentas simuladas"
                )
            } else {
                if (modo != null) {
                    lineasBio += Linea(
                        texto = "Modo en app: ${modo.etiqueta}",
                        ok = true,
                        indentada = true,
                        detalle = "Activo y vinculado a clave de bóveda"
                    )
                    val presente = repositorio.biometria.estaConfigurada(modo)
                    lineasBio += Linea(
                        texto = "Keystore hardware-backed: ${if (presente) "configurado" else "no configurado"}",
                        ok = presente,
                        indentada = true
                    )
                } else {
                    lineasBio += Linea(
                        texto = "Modo en app: desactivado",
                        ok = false,
                        indentada = true,
                        detalle = "Desactivado voluntariamente en Ajustes > Seguridad"
                    )
                }
                lineasBio += Linea(
                    texto = "Bóveda señuelo (PIN coacción): desactivada",
                    ok = null,
                    indentada = true,
                    detalle = "Protección opcional contra extorsión (Ajustes > Seguridad)"
                )
            }
        }

        val camConcedida = PermisoCamara.concedido(contexto)
        val lineasCam = mutableListOf<Linea>()
        lineasCam += Linea("Permiso de cámara: ${if (camConcedida) "concedido" else "denegado"}", ok = camConcedida)
        lineasCam += Linea("Motor activo: ${MotorCamara.desde(ajustes.motorCamara).etiqueta}", indentada = true)
        lineasCam += InformeDiagnostico.camaras(contexto)

        return DatosAuditoria(
            fabricante = Build.MANUFACTURER,
            modelo = Build.MODEL,
            dispositivo = Build.DEVICE,
            placa = Build.BOARD,
            soc = soc,
            abis = Build.SUPPORTED_ABIS.joinToString(", "),
            nucleosCpu = rt.availableProcessors(),
            versionAndroid = Build.VERSION.RELEASE,
            apiSdk = Build.VERSION.SDK_INT,
            parcheSeguridad = Build.VERSION.SECURITY_PATCH,
            compilacion = Build.DISPLAY,
            ramTotalMb = ramTotal,
            ramLibreMb = ramLibre,
            ramBaja = memInfo.lowMemory,
            heapMaxMb = heapMax,
            heapUsadoMb = heapUsado,
            almacenamientoLibreMb = espacioLibre,
            almacenamientoTotalMb = espacioTotal,
            rutaBoveda = repositorio.archivoBoveda.absolutePath,
            tamanoBovedaBytes = repositorio.archivoBoveda.length(),
            tienePermisoInternet = tieneInternet,
            flagSecureActivo = ajustes.proteccionPantalla,
            permisosDeclarados = permisos,
            lineasBiometria = lineasBio,
            lineasCamara = lineasCam,
            perfilArgon2 = repositorio.perfilArgon2Actual(),
            kdfParams = repositorio.paramsActuales()
        )
    }
}
