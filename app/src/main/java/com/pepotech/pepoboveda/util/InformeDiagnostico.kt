package com.pepotech.pepoboveda.util

import android.content.Context
import androidx.biometric.BiometricManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import com.pepotech.pepoboveda.camara.MotorCamara
import com.pepotech.pepoboveda.camara.PermisoCamara
import com.pepotech.pepoboveda.data.VaultRepository
import com.pepotech.pepoboveda.data.modoBiometriaActivo

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
}
