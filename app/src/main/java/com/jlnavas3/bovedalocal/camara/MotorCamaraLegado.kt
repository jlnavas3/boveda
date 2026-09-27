@file:Suppress("DEPRECATION")

package com.jlnavas3.bovedalocal.camara

import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Handler
import android.os.HandlerThread
import android.view.TextureView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.jlnavas3.bovedalocal.util.Diagnostico
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Motor de reserva con la API antigua de cámara (android.hardware.Camera). Está obsoleta
 * desde Android 5, pero sigue en todas las versiones y es la que funciona hasta en los
 * HAL más viejos o más rotos, que es justo cuando CameraX no puede.
 *
 * Los frames llegan en NV21: los primeros ancho*alto bytes son el plano Y compacto, que
 * es lo único que necesita ZXing; se decodifica sobre el propio buffer, sin copiar.
 *
 * Sigue el ciclo de vida de [dueno]: al irse la app al fondo suelta la cámara (si no,
 * Android nos la quita y no nos enteramos) y al volver la reabre sobre la misma vista.
 */
class MotorCamaraLegado(
    private val contexto: Context,
    private val dueno: LifecycleOwner,
    private val alFrame: (ByteArray, Int, Int) -> Unit,
    alEstado: (EstadoCamara) -> Unit
) : MotorBase("Legado", alEstado) {

    companion object {
        private const val INTERVALO_ENFOQUE_MS = 1_500L

        /** Aunque CameraX confirme el cierre, algún HAL tarda un poco más en soltarla: se insiste. */
        private const val INTENTOS_APERTURA = 6
        private const val ESPERA_ENTRE_INTENTOS_MS = 400L
        private const val ESPERA_TRAS_ERROR_MS = 600L
    }

    private val hiloPerezoso = lazy { HandlerThread("camara-legado").apply { start() } }
    private val hilo: HandlerThread by hiloPerezoso
    private val fondo: Handler by lazy { Handler(hilo.looper) }
    private val decodificador: ExecutorService = Executors.newSingleThreadExecutor()
    private val decodificando = AtomicBoolean(false)
    private val abriendo = AtomicBoolean(false)

    @Volatile
    private var camara: Camera? = null

    @Volatile
    private var superficie: SurfaceTexture? = null
    private var vista: TextureView? = null
    private var config = ConfiguracionCamaraLegada()

    private val observador = LifecycleEventObserver { _, evento ->
        when (evento) {
            Lifecycle.Event.ON_STOP -> pausar("la app se va al fondo")
            Lifecycle.Event.ON_START -> reanudar()
            else -> Unit
        }
    }

    private val enfocar = object : Runnable {
        override fun run() {
            if (terminado.get() || camara == null) return
            try {
                camara?.autoFocus { _, _ -> }
            } catch (e: Exception) {
                // Enfoque no disponible en este instante; se reintenta.
            }
            fondo.postDelayed(this, INTERVALO_ENFOQUE_MS)
        }
    }

    init {
        dueno.lifecycle.addObserver(observador)
    }

    /** La vista que hay que poner en pantalla. La cámara arranca cuando la superficie existe. */
    fun crearVista(): TextureView {
        empezarACronometrar()
        val textura = TextureView(contexto)
        textura.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(nueva: SurfaceTexture, ancho: Int, alto: Int) {
                superficie = nueva
                fondo.post { abrir(nueva) }
            }

            override fun onSurfaceTextureSizeChanged(nueva: SurfaceTexture, ancho: Int, alto: Int) {
                ajustarTransformacion()
            }

            override fun onSurfaceTextureDestroyed(vieja: SurfaceTexture): Boolean {
                superficie = null
                pausar("la superficie se destruyó")
                return true
            }

            override fun onSurfaceTextureUpdated(nueva: SurfaceTexture) = Unit
        }
        vista = textura
        return textura
    }

    private fun abrir(superficieDestino: SurfaceTexture) {
        if (terminado.get()) return
        // La superficie puede volver a estar disponible (se tapó y se destapó): con una
        // cámara ya abierta o abriéndose no se abre otra.
        if (camara != null || !abriendo.compareAndSet(false, true)) return
        try {
            val id = ConfiguradorCamaraLegada.elegirCamara()
            if (id < 0) {
                fallar("Este móvil no expone ninguna cámara", null)
                return
            }
            val cam = abrirConReintentos(id) ?: return
            try {
                config = ConfiguradorCamaraLegada.configurar(cam, id, contexto)
                val tamBuffer = config.anchoPrevia * config.altoPrevia * ImageFormat.getBitsPerPixel(ImageFormat.NV21) / 8
                repeat(2) { cam.addCallbackBuffer(ByteArray(tamBuffer)) }
                cam.setErrorCallback { error, _ -> alErrorDeCamara(error) }
                cam.setPreviewCallbackWithBuffer { datos, c -> procesar(datos, c) }
                cam.setPreviewTexture(superficieDestino)
                cam.startPreview()
                camara = cam
                Diagnostico.apuntar(
                    "camara",
                    "Legado: cámara $id abierta a ${config.anchoPrevia}x${config.altoPrevia}, rotación ${config.rotacionPrevia}, enfoque ${if (config.enfoqueManual) "por pulsos" else "continuo"}"
                )
                principal.post { ajustarTransformacion() }
                if (!yaHayFrames) armarVigilante()
                if (config.enfoqueManual) fondo.postDelayed(enfocar, INTERVALO_ENFOQUE_MS)
            } catch (e: Exception) {
                try {
                    cam.release()
                } catch (e2: Exception) {
                    // ya liberada
                }
                fallar("La cámara antigua no pudo arrancar", e)
            }
        } finally {
            abriendo.set(false)
        }
    }

    /** Devuelve null (y ya ha llamado a [fallar]) si tras varios intentos sigue sin abrirse. */
    private fun abrirConReintentos(id: Int): Camera? {
        var ultimo: Exception? = null
        for (intento in 1..INTENTOS_APERTURA) {
            if (terminado.get()) return null
            try {
                return Camera.open(id)
            } catch (e: Exception) {
                ultimo = e
                if (intento < INTENTOS_APERTURA) {
                    try {
                        Thread.sleep(ESPERA_ENTRE_INTENTOS_MS)
                    } catch (ie: InterruptedException) {
                        Thread.currentThread().interrupt()
                        break
                    }
                }
            }
        }
        fallar("La cámara está ocupada o no responde", ultimo)
        return null
    }

    private fun ajustarTransformacion() {
        ConfiguradorCamaraLegada.ajustarTransformacion(vista, config)
    }

    private fun procesar(datos: ByteArray, cam: Camera) {
        if (terminado.get()) return
        val ancho = config.anchoPrevia
        val alto = config.altoPrevia
        anotarFrame({ "${ancho}x${alto} NV21" }, conImagen = true)
        if (datos.size >= ancho * alto && decodificando.compareAndSet(false, true)) {
            // Se decodifica sobre el propio buffer y se devuelve a la cámara al terminar:
            // mientras tanto la cámara usa el otro, y si tarda, descarta frames. Es lo que queremos.
            try {
                decodificador.execute {
                    try {
                        alFrame(datos, ancho, alto)
                    } finally {
                        decodificando.set(false)
                        devolverBuffer(cam, datos)
                    }
                }
            } catch (e: Exception) {
                decodificando.set(false)
                devolverBuffer(cam, datos)
            }
        } else {
            devolverBuffer(cam, datos)
        }
    }

    private fun devolverBuffer(cam: Camera, datos: ByteArray) {
        try {
            cam.addCallbackBuffer(datos)
        } catch (e: Exception) {
            // cámara ya liberada
        }
    }

    /** Android nos ha quitado la cámara (otra app, el sistema): la soltamos y probamos a reabrir. */
    private fun alErrorDeCamara(error: Int) {
        Diagnostico.apuntar("camara", "Legado: error $error de la cámara; la suelto y reintento")
        fondo.post {
            cerrarCamara()
            fondo.postDelayed({ reanudar() }, ESPERA_TRAS_ERROR_MS)
        }
    }

    private fun pausar(motivo: String) {
        if (terminado.get() || camara == null) return
        Diagnostico.apuntar("camara", "Legado: suelto la cámara porque $motivo")
        fondo.post { cerrarCamara() }
    }

    private fun reanudar() {
        if (terminado.get()) return
        val destino = superficie ?: return
        if (!dueno.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) return
        fondo.post { abrir(destino) }
    }

    /** Solo en el hilo de fondo. */
    private fun cerrarCamara() {
        fondo.removeCallbacks(enfocar)
        val cam = camara ?: return
        camara = null
        try {
            cam.setErrorCallback(null)
            cam.setPreviewCallbackWithBuffer(null)
            cam.stopPreview()
        } catch (e: Exception) {
            // ya parada
        }
        try {
            cam.release()
        } catch (e: Exception) {
            // ya liberada
        }
    }

    override fun liberar() {
        try {
            dueno.lifecycle.removeObserver(observador)
        } catch (e: Exception) {
            // ya quitado
        }
        if (hiloPerezoso.isInitialized()) {
            fondo.post {
                cerrarCamara()
                hilo.quitSafely()
            }
        }
        decodificador.shutdown()
    }
}
