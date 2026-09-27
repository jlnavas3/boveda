@file:Suppress("DEPRECATION")

package com.jlnavas3.bovedalocal.camara

import android.content.Context
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.hardware.Camera
import android.os.Build
import android.view.Surface
import android.view.TextureView
import android.view.WindowManager
import com.jlnavas3.bovedalocal.util.Diagnostico

/**
 * Lógica matemática de selección de cámara, parámetros de resolución,
 * cálculo de rotación de pantalla y matriz de transformación de TextureView.
 */
object ConfiguradorCamaraLegada {

    const val AREA_MAXIMA = 1280 * 720

    fun elegirCamara(): Int {
        val total = try {
            Camera.getNumberOfCameras()
        } catch (e: Exception) {
            Diagnostico.apuntar("camara", "Legado: getNumberOfCameras falló", e)
            0
        }
        if (total <= 0) return -1
        val info = Camera.CameraInfo()
        for (i in 0 until total) {
            try {
                Camera.getCameraInfo(i, info)
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) return i
            } catch (e: Exception) {
                // seguimos con la siguiente
            }
        }
        return 0
    }

    fun configurar(cam: Camera, id: Int, contexto: Context): ConfiguracionCamaraLegada {
        val params = cam.parameters
        val tam = elegirTamano(params.supportedPreviewSizes)
        params.setPreviewSize(tam.width, tam.height)
        params.previewFormat = ImageFormat.NV21
        val modos = params.supportedFocusModes ?: emptyList()
        var enfoqueManual = false
        when {
            modos.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) ->
                params.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
            modos.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO) ->
                params.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
            modos.contains(Camera.Parameters.FOCUS_MODE_AUTO) -> {
                params.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
                enfoqueManual = true
            }
        }
        try {
            cam.parameters = params
        } catch (e: Exception) {
            // Hay HAL que rechazan el modo de enfoque: lo dejamos como venga y probamos solo el tamaño.
            Diagnostico.apuntar("camara", "Legado: el HAL rechazó los parámetros; reintento solo con tamaño", e)
            val basicos = cam.parameters
            basicos.setPreviewSize(tam.width, tam.height)
            basicos.previewFormat = ImageFormat.NV21
            cam.parameters = basicos
            enfoqueManual = false
        }
        val reales = cam.parameters.previewSize
        val rotacion = orientacionPantalla(id, contexto)
        try {
            cam.setDisplayOrientation(rotacion)
        } catch (e: Exception) {
            Diagnostico.apuntar("camara", "Legado: setDisplayOrientation falló", e)
        }
        return ConfiguracionCamaraLegada(
            anchoPrevia = reales.width,
            altoPrevia = reales.height,
            rotacionPrevia = rotacion,
            enfoqueManual = enfoqueManual
        )
    }

    fun elegirTamano(tamanos: List<Camera.Size>?): Camera.Size {
        val lista = tamanos.orEmpty()
        require(lista.isNotEmpty()) { "La cámara no anuncia tamaños de previsualización" }
        return lista.filter { it.width * it.height <= AREA_MAXIMA }.maxByOrNull { it.width * it.height }
            ?: lista.minByOrNull { it.width * it.height }!!
    }

    fun orientacionPantalla(id: Int, contexto: Context): Int {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(id, info)
        val rotacion = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                contexto.display?.rotation ?: Surface.ROTATION_0
            } else {
                (contexto.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
            }
        } catch (e: Exception) {
            Surface.ROTATION_0
        }
        val grados = when (rotacion) {
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }
        return if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            (360 - (info.orientation + grados) % 360) % 360
        } else {
            (info.orientation - grados + 360) % 360
        }
    }

    fun ajustarTransformacion(vista: TextureView?, config: ConfiguracionCamaraLegada) {
        val textura = vista ?: return
        val anchoPrevia = config.anchoPrevia
        val altoPrevia = config.altoPrevia
        val rotacionPrevia = config.rotacionPrevia
        if (anchoPrevia == 0 || altoPrevia == 0 || textura.width == 0 || textura.height == 0) return
        val girada = rotacionPrevia == 90 || rotacionPrevia == 270
        val anchoImagen = if (girada) altoPrevia else anchoPrevia
        val altoImagen = if (girada) anchoPrevia else altoPrevia
        val escala = maxOf(textura.width.toFloat() / anchoImagen, textura.height.toFloat() / altoImagen)
        val matriz = Matrix()
        matriz.setScale(
            anchoImagen * escala / textura.width,
            altoImagen * escala / textura.height,
            textura.width / 2f,
            textura.height / 2f
        )
        textura.setTransform(matriz)
    }
}
