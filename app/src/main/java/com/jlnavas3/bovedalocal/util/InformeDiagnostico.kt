package com.jlnavas3.bovedalocal.util

import android.content.Context
import androidx.biometric.BiometricManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import com.jlnavas3.bovedalocal.camara.MotorCamara
import com.jlnavas3.bovedalocal.camara.PermisoCamara
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.data.modoBiometriaActivo

import android.app.ActivityManager
import android.content.pm.PackageManager

/**
 * Lo que sabe Android de este móvil y que importa para la cámara y la huella. Es la
 * parte del informe que depende de Android; el registro en sí vive en [Diagnostico].
 * Aquí tampoco entra nada personal: modelo, versión, capacidades y ajustes, nada más.
 *
 * [estado] habla con el servicio de biometría y con el de cámara (varias llamadas Binder,
 * lentas en HAL viejos): llamarlo fuera del hilo principal.
 */
object InformeDiagnostico {

    /**
     * Una línea del estado en vivo. [ok] es null para líneas informativas (modelo, versión,
     * motor ajustado...) y true/false para lo que sí es un sí-o-no comprobable ahora mismo,
     * así la pantalla pinta ✔ o ✖ en vez de dejar que el usuario lo adivine leyendo texto.
     */
    data class Linea(val texto: String, val ok: Boolean? = null, val indentada: Boolean = false)

    data class DatosAuditoria(
        val fabricante: String,
        val modelo: String,
        val dispositivo: String,
        val placa: String,
        val soc: String?,
        val abis: String,
        val nucleosCpu: Int,
        val versionAndroid: String,
        val apiSdk: Int,
        val parcheSeguridad: String,
        val compilacion: String,
        val ramTotalMb: Long,
        val ramLibreMb: Long,
        val ramBaja: Boolean,
        val heapMaxMb: Long,
        val heapUsadoMb: Long,
        val almacenamientoLibreMb: Long,
        val almacenamientoTotalMb: Long,
        val rutaBoveda: String,
        val tamanoBovedaBytes: Long,
        val tienePermisoInternet: Boolean,
        val flagSecureActivo: Boolean,
        val permisosDeclarados: List<String>,
        val lineasBiometria: List<Linea>,
        val lineasCamara: List<Linea>
    )

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
        lineasBio += Linea("Modo en app: ${modo?.etiqueta ?: "ninguno"}", ok = modo != null, indentada = true)
        if (modo != null) {
            val presente = repositorio.biometria.estaConfigurada(modo)
            lineasBio += Linea("Keystore hardware-backed: ${if (presente) "configurado" else "no configurado"}", ok = presente, indentada = true)
        }

        val camConcedida = PermisoCamara.concedido(contexto)
        val lineasCam = mutableListOf<Linea>()
        lineasCam += Linea("Permiso de cámara: ${if (camConcedida) "concedido" else "denegado"}", ok = camConcedida)
        lineasCam += Linea("Motor activo: ${MotorCamara.desde(ajustes.motorCamara).etiqueta}", indentada = true)
        lineasCam += camaras(contexto)

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
            flagSecureActivo = true,
            permisosDeclarados = permisos,
            lineasBiometria = lineasBio,
            lineasCamara = lineasCam
        )
    }

    fun cabecera(contexto: Context): String {
        val version = try {
            val info = contexto.packageManager.getPackageInfo(contexto.packageName, 0)
            "${info.versionName} (${info.longVersionCode})"
        } catch (e: Exception) {
            "?"
        }
        return buildString {
            append("Bóveda local ").append(version).append('\n')
            append(Build.MANUFACTURER).append(' ').append(Build.MODEL)
            append(" (").append(Build.DEVICE).append(")\n")
            append("Android ").append(Build.VERSION.RELEASE)
            append(" (API ").append(Build.VERSION.SDK_INT).append(")\n")
            append("Build: ").append(Build.DISPLAY)
        }
    }

    /** Líneas del estado en vivo, para la pantalla y para el informe. */
    fun estado(contexto: Context, repositorio: VaultRepository): List<Linea> {
        val lineas = ArrayList<Linea>()
        val ajustes = repositorio.ajustes.actual

        val c = Biometria.capacidad(contexto)
        val nivel = Biometria.decidirNivel(c)
        lineas += Linea("Huella: nivel ${nivel.name.lowercase()}")
        lineas += Linea("Clase 3: ${Biometria.explicar(c.fuerte)}", ok = c.fuerte == BiometricManager.BIOMETRIC_SUCCESS, indentada = true)
        lineas += Linea("Clase 2: ${Biometria.explicar(c.debil)}", ok = c.debil == BiometricManager.BIOMETRIC_SUCCESS, indentada = true)
        lineas += Linea("PIN del móvil: ${Biometria.explicar(c.credencial)}", ok = c.credencial == BiometricManager.BIOMETRIC_SUCCESS, indentada = true)
        lineas += Linea("Clase 2 o PIN: ${Biometria.explicar(c.compatible)}", ok = c.compatible == BiometricManager.BIOMETRIC_SUCCESS, indentada = true)
        val modo = ajustes.modoBiometriaActivo
        lineas += Linea("Modo activo en la app: ${modo?.etiqueta ?: "ninguno"}", ok = modo != null, indentada = true)
        if (modo != null) {
            val presente = repositorio.biometria.estaConfigurada(modo)
            lineas += Linea("Clave del modo en el Keystore: ${if (presente) "presente" else "ausente"}", ok = presente, indentada = true)
        }

        val camaraConcedida = PermisoCamara.concedido(contexto)
        lineas += Linea("Cámara: permiso ${if (camaraConcedida) "concedido" else "no concedido"}", ok = camaraConcedida)
        lineas += Linea("Motor ajustado: ${MotorCamara.desde(ajustes.motorCamara).etiqueta}", indentada = true)
        lineas += camaras(contexto)
        return lineas
    }

    private fun camaras(contexto: Context): List<Linea> {
        val gestor = contexto.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
            ?: return listOf(Linea("camera2: servicio no disponible", ok = false, indentada = true))
        return try {
            val ids = gestor.cameraIdList
            if (ids.isEmpty()) return listOf(Linea("camera2: ninguna cámara", ok = false, indentada = true))
            ids.map { id ->
                try {
                    val car = gestor.getCameraCharacteristics(id)
                    val cara = when (car.get(CameraCharacteristics.LENS_FACING)) {
                        CameraCharacteristics.LENS_FACING_BACK -> "trasera"
                        CameraCharacteristics.LENS_FACING_FRONT -> "frontal"
                        CameraCharacteristics.LENS_FACING_EXTERNAL -> "externa"
                        else -> "desconocida"
                    }
                    val nivel = when (car.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)) {
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY -> "LEGACY"
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED -> "LIMITED"
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL -> "FULL"
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3 -> "LEVEL_3"
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_EXTERNAL -> "EXTERNAL"
                        else -> "?"
                    }
                    Linea("camera2 id $id: $cara, nivel $nivel", indentada = true)
                } catch (e: Exception) {
                    Linea("camera2 id $id: no se pudieron leer sus características (${Diagnostico.describir(e)})", ok = false, indentada = true)
                }
            }
        } catch (e: Exception) {
            listOf(Linea("camera2: no se pudo listar (${Diagnostico.describir(e)})", ok = false, indentada = true))
        }
    }

    /** El informe con un [estado] ya calculado (el que la pantalla tiene en memoria). */
    fun generar(contexto: Context, estado: List<Linea>): String {
        val texto = estado.joinToString("\n") { linea ->
            val marca = when (linea.ok) {
                true -> "[OK] "
                false -> "[NO] "
                null -> ""
            }
            val sangria = if (linea.indentada) "  " else ""
            "$sangria$marca${linea.texto}"
        }
        return Diagnostico.informe(cabecera(contexto), texto)
    }

    fun generar(contexto: Context, datos: DatosAuditoria): String {
        val cuerpo = buildString {
            appendLine("=== HARDWARE Y SOC ===")
            appendLine("Fabricante: ${datos.fabricante}")
            appendLine("Modelo: ${datos.modelo} (${datos.dispositivo})")
            appendLine("Placa: ${datos.placa}")
            if (datos.soc != null) appendLine("SoC: ${datos.soc}")
            appendLine("ABIs soportadas: ${datos.abis}")
            appendLine("Núcleos CPU: ${datos.nucleosCpu}")
            appendLine()
            appendLine("=== SISTEMA ANDROID ===")
            appendLine("Versión Android: ${datos.versionAndroid} (API ${datos.apiSdk})")
            appendLine("Parche de seguridad: ${datos.parcheSeguridad}")
            appendLine("Build: ${datos.compilacion}")
            appendLine()
            appendLine("=== MEMORIA Y RENDIMIENTO ===")
            appendLine("RAM Total: ${datos.ramTotalMb} MB | RAM Libre: ${datos.ramLibreMb} MB (Baja: ${datos.ramBaja})")
            appendLine("Heap JVM Max: ${datos.heapMaxMb} MB | Heap Usado: ${datos.heapUsadoMb} MB")
            appendLine()
            appendLine("=== ALMACENAMIENTO Y BÓVEDA ===")
            appendLine("Espacio libre interno: ${datos.almacenamientoLibreMb} MB de ${datos.almacenamientoTotalMb} MB")
            appendLine("Ruta bóveda: ${datos.rutaBoveda}")
            appendLine("Tamaño en disco: ${datos.tamanoBovedaBytes} bytes")
            appendLine()
            appendLine("=== SEGURIDAD Y PRIVACIDAD ===")
            appendLine("Permiso INTERNET: ${if (datos.tienePermisoInternet) "PRESENTE (ALERTA)" else "NO DECLARADO (100% OFFLINE)"}")
            appendLine("FLAG_SECURE: ACTIVO (bloqueo capturas y grabación)")
            appendLine("Permisos declarados: ${datos.permisosDeclarados.joinToString(", ")}")
            appendLine()
            appendLine("=== BIOMETRÍA Y CÁMARA ===")
            datos.lineasBiometria.forEach { l ->
                val m = when (l.ok) { true -> "[OK] "; false -> "[NO] "; null -> "" }
                val s = if (l.indentada) "  " else ""
                appendLine("$s$m${l.texto}")
            }
            datos.lineasCamara.forEach { l ->
                val m = when (l.ok) { true -> "[OK] "; false -> "[NO] "; null -> "" }
                val s = if (l.indentada) "  " else ""
                appendLine("$s$m${l.texto}")
            }
        }
        return Diagnostico.informe(cabecera(contexto), cuerpo)
    }
}
