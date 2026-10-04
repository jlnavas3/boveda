package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import android.Manifest
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.camara.LectorImagenes
import com.jlnavas3.bovedalocal.camara.LectorQr
import com.jlnavas3.bovedalocal.camara.MotorCamara
import com.jlnavas3.bovedalocal.camara.MotorCameraX
import com.jlnavas3.bovedalocal.camara.PermisoCamara
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Pantalla completa e inmersiva de escaneo QR.
 */
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

    val alFrame: (ByteArray, Int, Int) -> Unit = { datos, ancho, alto ->
        if (!yaLeido.get() && !procesandoLectura) {
            val texto = LectorQr.decodificarLuminancia(datos, ancho, alto, probarInvertido = true)
            if (texto != null && yaLeido.compareAndSet(false, true)) {
                principal.post {
                    procesandoLectura = true
                    codigoDetectado = true
                    if (ajustes.hapticaApp) haptica.exito()

                    principal.postDelayed({
                        val res = procesarLecturaQr(texto, "la cámara", entradaDestino, vm)
                        if (res is ResultadoProcesoQr.Error) {
                            if (ajustes.hapticaApp) haptica.error()
                            mensajeEstado = res.mensaje
                            esErrorMensaje = true
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
                else -> {
                    val res = procesarLecturaQr(texto, "una imagen", entradaDestino, vm)
                    if (res is ResultadoProcesoQr.Exito) {
                        if (ajustes.hapticaApp) haptica.exito()
                    } else if (res is ResultadoProcesoQr.Error) {
                        if (ajustes.hapticaApp) haptica.error()
                        mensajeEstado = res.mensaje
                        esErrorMensaje = true
                    }
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
            VisorCamaraEnfoque(
                motorActual = motorActual,
                motorPreferido = motorPreferido,
                codigoDetectado = codigoDetectado,
                alFrame = alFrame,
                alConmutarMotorCompatible = { motorActual = MotorCamara.COMPATIBLE },
                alErrorCamara = { motivo ->
                    mensajeEstado = motivo
                    esErrorMensaje = true
                },
                alMotorListo = { motorCameraX = it },
                modifier = Modifier.fillMaxSize()
            )
        } else {
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

        MensajeFlotanteQr(
            mensaje = mensajeEstado,
            esError = esErrorMensaje,
            modifier = Modifier.align(Alignment.TopCenter)
        )

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
