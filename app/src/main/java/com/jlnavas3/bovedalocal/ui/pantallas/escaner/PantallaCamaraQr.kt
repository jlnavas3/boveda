package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import android.Manifest
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.camara.EstadoCamara
import com.jlnavas3.bovedalocal.camara.LectorImagenes
import com.jlnavas3.bovedalocal.camara.LectorQr
import com.jlnavas3.bovedalocal.camara.MotorCamara
import com.jlnavas3.bovedalocal.camara.MotorCameraX
import com.jlnavas3.bovedalocal.camara.PermisoCamara
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.GoogleAuthMigration
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Pantalla completa e inmersiva de escaneo QR.
 *
 * Incluye:
 * - Visor a pantalla completa con máscara oscura y 4 esquinas angulares de enfoque.
 * - Captura en todo el frame de la cámara (identifica el QR aunque esté bajo la máscara).
 * - Respeta la configuración háptica de la app.
 * - Linterna (flash) integrada con botón de encendido/apagado.
 * - Selector de imágenes de la galería para leer QR desde capturas.
 * - Slider continuo de zoom con botones de aumento y disminución.
 * - Acceso directo a entrada manual.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCamaraQr(
    vm: VaultViewModel,
    actividad: FragmentActivity,
    entradaDestino: String?
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ambito = rememberCoroutineScope()
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val motorPreferido = MotorCamara.desde(ajustes.motorCamara)
    var motorActual by remember(motorPreferido) {
        mutableStateOf(if (motorPreferido == MotorCamara.COMPATIBLE) MotorCamara.COMPATIBLE else MotorCamara.CAMERAX)
    }

    var permiso by remember { mutableStateOf(PermisoCamara.concedido(contexto)) }
    var estadoPermiso by remember { mutableStateOf(EstadoPermiso.NO_PEDIDO) }

    var motorCameraX by remember { mutableStateOf<MotorCameraX?>(null) }
    var flashEncendido by remember { mutableStateOf(false) }
    var zoomRatio by remember { mutableFloatStateOf(0f) }

    var codigoDetectado by remember { mutableStateOf(false) }
    var mensajeEstado by remember { mutableStateOf<String?>(null) }
    var esErrorMensaje by remember { mutableStateOf(false) }
    var procesandoLectura by remember { mutableStateOf(false) }

    val yaLeido = remember { AtomicBoolean(false) }
    val principal = remember { Handler(Looper.getMainLooper()) }

    // Sincronizar estado háptico con la configuración de la app
    LaunchedEffect(ajustes) {
        Haptica.sincronizar(ajustes)
    }

    val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        permiso = concedido
        if (concedido) {
            estadoPermiso = EstadoPermiso.NO_PEDIDO
            Diagnostico.apuntar("camara", "Permiso de cámara concedido en escáner inmersivo")
        } else {
            val paraSiempre = !ActivityCompat.shouldShowRequestPermissionRationale(actividad, Manifest.permission.CAMERA)
            estadoPermiso = if (paraSiempre) EstadoPermiso.DENEGADO_PARA_SIEMPRE else EstadoPermiso.DENEGADO
        }
    }

    var permisoPedido by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!permiso && !permisoPedido) {
            permisoPedido = true
            pedirPermiso.launch(Manifest.permission.CAMERA)
        }
    }

    LifecycleResumeEffect(Unit) {
        val ahora = PermisoCamara.concedido(contexto)
        if (ahora != permiso) {
            permiso = ahora
            if (ahora) estadoPermiso = EstadoPermiso.NO_PEDIDO
        }
        onPauseOrDispose { }
    }

    fun procesarTexto(texto: String, origen: String): Boolean {
        if (GoogleAuthMigration.esEnlaceMigracion(texto)) {
            Diagnostico.apuntar("2fa", "Escaneo inmersivo detectó migración de Google Authenticator")
            vm.ir(Pantalla.ConfirmarMigracion(texto))
            return true
        }
        if (LectorQr.esQrDePasskey(texto)) {
            Diagnostico.apuntar("2fa", "Escaneo detectó un QR de Passkey en lugar de TOTP")
            mensajeEstado = "Este QR es una Passkey, no un código 2FA."
            esErrorMensaje = true
            return false
        }
        if (!vm.altaTotp(texto, entradaDestino)) {
            Diagnostico.apuntar("2fa", "Formato de clave 2FA no reconocido ($origen)")
            mensajeEstado = "El código QR no contiene un doble factor válido."
            esErrorMensaje = true
            return false
        }
        val detalle = if (origen == "la cámara") "cámara inmersiva" else origen
        Diagnostico.apuntar("2fa", "Doble factor añadido exitosamente ($detalle)")
        vm.avisar("Doble factor añadido")
        vm.volverAtras()
        return true
    }

    val alFrame: (ByteArray, Int, Int) -> Unit = { datos, ancho, alto ->
        if (!yaLeido.get() && !procesandoLectura) {
            // Decodifica sobre el frame completo: detecta el QR sin importar si está bajo la máscara
            val texto = LectorQr.decodificarLuminancia(datos, ancho, alto, probarInvertido = true)
            if (texto != null && yaLeido.compareAndSet(false, true)) {
                principal.post {
                    procesandoLectura = true
                    codigoDetectado = true

                    // Vibración háptica respetando ajustes de la app
                    if (ajustes.hapticaApp) {
                        haptica.exito()
                    }

                    // Pequeña pausa de 250ms para apreciar el destello verde de las esquinas
                    principal.postDelayed({
                        val valido = procesarTexto(texto, "la cámara")
                        if (!valido) {
                            if (ajustes.hapticaApp) haptica.error()
                            principal.postDelayed({
                                codigoDetectado = false
                                procesandoLectura = false
                                yaLeido.set(false)
                                mensajeEstado = null
                            }, 2_500)
                        }
                    }, 250)
                }
            }
        }
    }

    val elegirImagen = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        BovedaApp.salidaTerminada(contexto)
        if (uri == null) return@rememberLauncherForActivityResult
        mensajeEstado = "Buscando código QR en la imagen…"
        esErrorMensaje = false
        ambito.launch {
            val texto = withContext(Dispatchers.Default) { LectorImagenes.leerQr(contexto, uri) }
            when {
                texto == null -> {
                    if (ajustes.hapticaApp) haptica.error()
                    mensajeEstado = "No se encontró ningún QR en esa imagen."
                    esErrorMensaje = true
                }
                procesarTexto(texto, "una imagen") -> {
                    if (ajustes.hapticaApp) haptica.exito()
                }
                else -> {
                    if (ajustes.hapticaApp) haptica.error()
                    mensajeEstado = "La imagen tiene un QR, pero no es de doble factor."
                    esErrorMensaje = true
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (permiso) {
            // 1. Vista de cámara previa de fondo con conmutación automática si falla CameraX
            key(motorActual) {
                if (motorActual == MotorCamara.COMPATIBLE) {
                    VistaLegado(
                        alFrame = alFrame,
                        alEstado = { estado ->
                            if (estado is EstadoCamara.Fallo) {
                                mensajeEstado = estado.motivo
                                esErrorMensaje = true
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    VistaCameraX(
                        alFrame = alFrame,
                        alEstado = { estado ->
                            if (estado is EstadoCamara.Fallo && motorActual == MotorCamara.CAMERAX && motorPreferido == MotorCamara.AUTOMATICO) {
                                Diagnostico.apuntar("camara", "PantallaCamaraQr: CameraX falló; conmutando al motor compatible")
                                motorActual = MotorCamara.COMPATIBLE
                            } else if (estado is EstadoCamara.Fallo) {
                                mensajeEstado = estado.motivo
                                esErrorMensaje = true
                            }
                        },
                        alMotorListo = { motor ->
                            motorCameraX = motor
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // 2. Máscara oscura con las 4 esquinas angulares de enfoque
            VisorMascaraQr(
                codigoDetectado = codigoDetectado,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Estado cuando el permiso no está otorgado
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TarjetaPermisoCamara(
                    estadoPermiso = estadoPermiso,
                    alPedirPermiso = { pedirPermiso.launch(Manifest.permission.CAMERA) },
                    alAvisar = { vm.avisar(it) }
                )
            }
        }

        // 3. Fila superior de navegación y acciones rápidas (Luz, Imagen, Manual, Ajustes)
        BarraSuperiorCamaraQr(
            flashEncendido = flashEncendido,
            alAlternarFlash = {
                val nuevoEstado = !flashEncendido
                flashEncendido = nuevoEstado
                motorCameraX?.alternarFlash(nuevoEstado)
                if (ajustes.hapticaApp) haptica.tic()
            },
            alEscanearImagen = {
                BovedaApp.salidaPendiente(contexto)
                try {
                    elegirImagen.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                } catch (_: Exception) {
                    BovedaApp.salidaTerminada(contexto)
                    mensajeEstado = "No se pudo abrir la galería."
                    esErrorMensaje = true
                }
            },
            alEntradaManual = {
                motorCameraX?.alternarFlash(false)
                vm.ir(Pantalla.Escaner(entradaDestino, soloManual = true))
            },
            alAbrirAjustes = {
                motorCameraX?.alternarFlash(false)
                vm.ir(Pantalla.AjustesCamara("04-HER-CAM"))
            },
            alVolver = {
                motorCameraX?.alternarFlash(false)
                vm.volverAtras()
            },
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // 4. Mensaje flotante de estado / error
        MensajeFlotanteQr(
            mensaje = mensajeEstado,
            esError = esErrorMensaje,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // 5. Controles inferiores de Zoom y feedback
        ControlZoomCamaraQr(
            zoomRatio = zoomRatio,
            alCambiarZoom = { valor ->
                zoomRatio = valor
                motorCameraX?.ajustarZoom(valor)
            },
            alAumentarZoom = {
                val nuevoZoom = (zoomRatio + 0.15f).coerceAtMost(1f)
                zoomRatio = nuevoZoom
                motorCameraX?.ajustarZoom(nuevoZoom)
                if (ajustes.hapticaApp) haptica.tic()
            },
            alReducirZoom = {
                val nuevoZoom = (zoomRatio - 0.15f).coerceAtLeast(0f)
                zoomRatio = nuevoZoom
                motorCameraX?.ajustarZoom(nuevoZoom)
                if (ajustes.hapticaApp) haptica.tic()
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
