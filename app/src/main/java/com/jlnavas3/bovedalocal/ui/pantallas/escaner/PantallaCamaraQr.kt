/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

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
    var tamanoVentanaDp by remember(ajustes.escanerQrTamanoVentanaDp) { mutableStateOf(ajustes.escanerQrTamanoVentanaDp.dp) }

    var codigoDetectado by remember { mutableStateOf(false) }
    var mensajeEstado by remember { mutableStateOf<String?>(null) }
    var esErrorMensaje by remember { mutableStateOf(false) }
    var procesandoLectura by remember { mutableStateOf(false) }
    var entradaConfirmar by remember { mutableStateOf<com.jlnavas3.bovedalocal.data.Entrada?>(null) }
    var payloadP2pConfirmar by remember { mutableStateOf<com.jlnavas3.bovedalocal.sync.SyncQrPayload?>(null) }
    var sincronizandoP2p by remember { mutableStateOf(false) }
    var errorSincronizacionP2p by remember { mutableStateOf<String?>(null) }
    var informeP2pMostrar by remember { mutableStateOf<com.jlnavas3.bovedalocal.sync.InformeSincronizacion?>(null) }

    val ensambladorOptico = remember { com.jlnavas3.bovedalocal.sync.EnsambladorRafagaOptica() }
    var mostrarDialogoPinOptico by remember { mutableStateOf(false) }
    var errorPinOptico by remember { mutableStateOf<String?>(null) }
    var mostrandoModalAnfitrion by remember { mutableStateOf(false) }

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
                    if (ajustes.escanerQrVibracion && ajustes.hapticaApp) haptica.exito()
                    if (ajustes.escanerQrSonido) {
                        try {
                            val tg = android.media.ToneGenerator(android.media.AudioManager.STREAM_NOTIFICATION, 100)
                            tg.startTone(android.media.ToneGenerator.TONE_PROP_BEEP)
                            principal.postDelayed({ tg.release() }, 200)
                        } catch (_: Exception) {}
                    }

                    principal.postDelayed({
                        val res = procesarLecturaQr(texto, "la cámara", entradaDestino, vm)
                        when (res) {
                            is ResultadoProcesoQr.EntradaDetectada -> {
                                entradaConfirmar = vm.resolverTituloDuplicado(res.entrada)
                            }
                            is ResultadoProcesoQr.P2pSyncDetectado -> {
                                if (com.jlnavas3.bovedalocal.BuildConfig.PERMITE_RED_LAN) {
                                    payloadP2pConfirmar = res.payload
                                } else {
                                    if (ajustes.hapticaApp) haptica.error()
                                    mensajeEstado = "Esta variante es 100% Air-Gapped (aislada sin permisos de red). La sincronización Wi-Fi requiere la variante LanSync."
                                    esErrorMensaje = true
                                    principal.postDelayed({
                                        codigoDetectado = false
                                        procesandoLectura = false
                                        yaLeido.set(false)
                                        mensajeEstado = null
                                    }, 4_000)
                                }
                            }
                            is ResultadoProcesoQr.TramaOpticaDetectada -> {
                                val esNueva = ensambladorOptico.registrarTrama(res.trama)
                                if (esNueva && ajustes.hapticaApp) haptica.tic()

                                if (ensambladorOptico.estaCompleto) {
                                    if (ajustes.hapticaApp) haptica.exito()
                                    errorPinOptico = null
                                    mostrarDialogoPinOptico = true
                                } else {
                                    mensajeEstado = "Ráfaga óptica: ${ensambladorOptico.progreso} / ${ensambladorOptico.total} fragmentos"
                                    esErrorMensaje = false
                                    codigoDetectado = false
                                    procesandoLectura = false
                                    yaLeido.set(false)
                                }
                            }
                            is ResultadoProcesoQr.Error -> {
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
                            is ResultadoProcesoQr.Exito -> {
                                // Redirige o cierra directamente
                            }
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
                    when (res) {
                        is ResultadoProcesoQr.EntradaDetectada -> {
                            entradaConfirmar = vm.resolverTituloDuplicado(res.entrada)
                        }
                        is ResultadoProcesoQr.P2pSyncDetectado -> {
                            if (com.jlnavas3.bovedalocal.BuildConfig.PERMITE_RED_LAN) {
                                payloadP2pConfirmar = res.payload
                            } else {
                                if (ajustes.hapticaApp) haptica.error()
                                mensajeEstado = "Esta variante es 100% Air-Gapped (aislada sin permisos de red). La sincronización Wi-Fi requiere la variante LanSync."
                                esErrorMensaje = true
                            }
                        }
                        is ResultadoProcesoQr.TramaOpticaDetectada -> {
                            ensambladorOptico.registrarTrama(res.trama)
                            if (ensambladorOptico.estaCompleto) {
                                if (ajustes.hapticaApp) haptica.exito()
                                errorPinOptico = null
                                mostrarDialogoPinOptico = true
                            } else {
                                mensajeEstado = "Fragmento óptico ${ensambladorOptico.progreso} / ${ensambladorOptico.total} registrado."
                                esErrorMensaje = false
                            }
                        }
                        is ResultadoProcesoQr.Exito -> {
                            if (ajustes.hapticaApp) haptica.exito()
                        }
                        is ResultadoProcesoQr.Error -> {
                            if (ajustes.hapticaApp) haptica.error()
                            mensajeEstado = res.mensaje
                            esErrorMensaje = true
                        }
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
                tamanoVentanaDp = tamanoVentanaDp,
                alCambiarTamano = {
                    tamanoVentanaDp = it
                    vm.ajustarEscanerQrTamanoVentanaDp(it.value)
                },
                alRestablecerTamano = {
                    tamanoVentanaDp = com.jlnavas3.bovedalocal.data.AjustesDefaults.CamaraQr.TAMANO_VENTANA_DP.dp
                    vm.ajustarEscanerQrTamanoVentanaDp(com.jlnavas3.bovedalocal.data.AjustesDefaults.CamaraQr.TAMANO_VENTANA_DP)
                    if (ajustes.hapticaApp) haptica.tic()
                },
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
            alMostrarAnfitrion = {
                motorCameraX?.alternarFlash(false)
                flashEncendido = false
                mostrandoModalAnfitrion = true
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

    if (entradaConfirmar != null) {
        DialogoConfirmarImportacionQr(
            entrada = entradaConfirmar!!,
            alConfirmar = { entradaAGuardar ->
                vm.guardar(entradaAGuardar)
                vm.avisar("«${entradaAGuardar.titulo}» guardada en la bóveda")
                entradaConfirmar = null
                vm.volverAtras()
            },
            alDescartar = {
                entradaConfirmar = null
                codigoDetectado = false
                procesandoLectura = false
                yaLeido.set(false)
                mensajeEstado = null
            }
        )
    }

    if (payloadP2pConfirmar != null) {
        DialogoConfirmarSincronizacionP2P(
            payload = payloadP2pConfirmar!!,
            sincronizando = sincronizandoP2p,
            errorMensaje = errorSincronizacionP2p,
            alConfirmar = {
                val payload = payloadP2pConfirmar ?: return@DialogoConfirmarSincronizacionP2P
                sincronizandoP2p = true
                errorSincronizacionP2p = null
                ambito.launch {
                    try {
                        val previa = vm.obtenerContenidoActual()
                        val fusionada = withContext(Dispatchers.IO) {
                            com.jlnavas3.bovedalocal.sync.ClienteSyncP2P.sincronizarConServidor(
                                payloadQr = payload,
                                bovedaLocal = previa
                            )
                        }
                        val dispositivoRemoto = fusionada.aliasDispositivo
                        val informe = com.jlnavas3.bovedalocal.sync.CalculadorMascaraDiff.generarInforme(
                            bovedaPrevia = previa,
                            bovedaFusionada = fusionada,
                            dispositivoRemoto = dispositivoRemoto
                        )
                        vm.aplicarBovedaFusionada(fusionada.copy(aliasDispositivo = null))
                        Diagnostico.apuntar("sync", "Sincronización P2P exitosa: ${informe.textoResumen()}")
                        if (ajustes.hapticaApp) haptica.exito()
                        val mensajeAviso = if (informe.hayCambios) {
                            "¡Sincronización exitosa! (${informe.totalModificadas} cambios aplicados)"
                        } else {
                            "¡Bóvedas al día! Sin cambios pendientes"
                        }
                        vm.avisar(mensajeAviso)
                        payloadP2pConfirmar = null
                        sincronizandoP2p = false
                        informeP2pMostrar = informe
                    } catch (e: Exception) {
                        Diagnostico.apuntar("sync", "Error en sincronización P2P con Desktop: ${e.message}", e)
                        if (ajustes.hapticaApp) haptica.error()
                        errorSincronizacionP2p = e.message ?: "Error desconocido al conectar con Desktop"
                        sincronizandoP2p = false
                    }
                }
            },
            alDescartar = {
                payloadP2pConfirmar = null
                sincronizandoP2p = false
                errorSincronizacionP2p = null
                codigoDetectado = false
                procesandoLectura = false
                yaLeido.set(false)
                mensajeEstado = null
            }
        )
    }

    if (mostrarDialogoPinOptico) {
        DialogoPinOptico(
            totalTramas = ensambladorOptico.total,
            errorMensaje = errorPinOptico,
            aliasEmisor = ensambladorOptico.aliasEmisor,
            alConfirmarPin = { pin ->
                ambito.launch {
                    try {
                        val previa = vm.obtenerContenidoActual()
                        val bovedaRemota = withContext(Dispatchers.Default) {
                            ensambladorOptico.descifrarBoveda(pin)
                        }
                        val fusionadaConInforme = withContext(Dispatchers.Default) {
                            com.jlnavas3.bovedalocal.sync.FusionadorBovedas.fusionarBovedasConInforme(
                                bovedaLocal = previa,
                                bovedaRemota = bovedaRemota
                            )
                        }
                        val fusionada = fusionadaConInforme.boveda
                        val informe = fusionadaConInforme.informe
                        vm.aplicarBovedaFusionada(fusionada)
                        Diagnostico.apuntar("sync", "Sincronización Óptica QR exitosa: ${informe.textoResumen()}")
                        if (ajustes.hapticaApp) haptica.exito()
                        val mensajeAviso = if (informe.hayCambios) {
                            "¡Sincronización óptica exitosa! (${informe.totalModificadas} cambios aplicados)"
                        } else {
                            "¡Bóvedas al día! Sin cambios pendientes"
                        }
                        vm.avisar(mensajeAviso)
                        mostrarDialogoPinOptico = false
                        ensambladorOptico.reiniciar()
                        informeP2pMostrar = informe
                    } catch (e: Exception) {
                        Diagnostico.apuntar("sync", "Error al descifrar ráfaga óptica: ${e.message}", e)
                        if (ajustes.hapticaApp) haptica.error()
                        errorPinOptico = e.message ?: "PIN incorrecto o datos corruptos"
                    }
                }
            },
            alDescartar = {
                mostrarDialogoPinOptico = false
                errorPinOptico = null
                ensambladorOptico.reiniciar()
                codigoDetectado = false
                procesandoLectura = false
                yaLeido.set(false)
                mensajeEstado = null
            }
        )
    }

    if (mostrandoModalAnfitrion) {
        ModalAnfitrionSyncP2P(
            bovedaActual = vm.obtenerContenidoActual(),
            alGuardarBovedaFusionada = { bovedaFusionada ->
                vm.aplicarBovedaFusionada(bovedaFusionada)
            },
            alFinalizarConInforme = { informe ->
                Diagnostico.apuntar("sync", "Sincronización P2P como anfitrión exitosa: ${informe.textoResumen()}")
                val mensajeAviso = if (informe.hayCambios) {
                    "¡Sincronización exitosa! (${informe.totalModificadas} cambios aplicados)"
                } else {
                    "¡Bóvedas al día! Sin cambios pendientes"
                }
                vm.avisar(mensajeAviso)
                mostrandoModalAnfitrion = false
                informeP2pMostrar = informe
            },
            alCerrar = {
                mostrandoModalAnfitrion = false
            }
        )
    }

    if (informeP2pMostrar != null) {
        DialogoInformeSincronizacion(
            informe = informeP2pMostrar!!,
            alCerrar = {
                informeP2pMostrar = null
                vm.volverAtras()
            }
        )
    }
}
