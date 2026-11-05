/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import android.app.Activity
import android.graphics.Bitmap
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jlnavas3.bovedalocal.BuildConfig
import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.sync.CodecDeltaOptico
import com.jlnavas3.bovedalocal.sync.InformeSincronizacion
import com.jlnavas3.bovedalocal.sync.PaqueteOpticoGenerado
import com.jlnavas3.bovedalocal.sync.ParserSyncQrUri
import com.jlnavas3.bovedalocal.sync.ServidorSyncP2P
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Advertencia
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.GeneradorQr
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

private enum class ModoAnfitrion {
    RED_LAN,
    OPTICO_QR
}

private enum class EstadoAnfitrion {
    ESPERANDO_ESCANEO,
    SINCRONIZANDO,
    FINALIZADO,
    ERROR
}

/**
 * Modal anfitrión que proyecta el código QR para que otro teléfono o PC se sincronice.
 * Implementa gestión energética inteligente: eleva el brillo al 85% y mantiene la pantalla
 * encendida ÚNICAMENTE mientras espera el escaneo, restaurando el brillo inmediatamente
 * al detectar la conexión.
 */
@Composable
fun ModalAnfitrionSyncP2P(
    bovedaActual: ContenidoBoveda,
    alGuardarBovedaFusionada: (ContenidoBoveda) -> Unit,
    alFinalizarConInforme: (InformeSincronizacion) -> Unit,
    alCerrar: () -> Unit
) {
    val contexto = LocalContext.current
    val actividad = contexto as? Activity
    val ambito = rememberCoroutineScope()

    val permiteLan = BuildConfig.PERMITE_RED_LAN
    var modoSeleccionado by remember {
        mutableStateOf(if (permiteLan) ModoAnfitrion.RED_LAN else ModoAnfitrion.OPTICO_QR)
    }

    var estado by remember { mutableStateOf(EstadoAnfitrion.ESPERANDO_ESCANEO) }
    var mensajeEstado by remember { mutableStateOf("Esperando escaneo...") }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    // --- Gestión Energética Inteligente ---
    DisposableEffect(estado) {
        val window = actividad?.window
        if (window != null) {
            if (estado == EstadoAnfitrion.ESPERANDO_ESCANEO) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                window.attributes = window.attributes.apply {
                    screenBrightness = 0.85f
                }
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                window.attributes = window.attributes.apply {
                    screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                }
            }
        }
        onDispose {
            actividad?.window?.let { w ->
                w.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                w.attributes = w.attributes.apply {
                    screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                }
            }
        }
    }

    // --- Estado para Modo Red LAN ---
    var servidorLan by remember { mutableStateOf<ServidorSyncP2P?>(null) }
    var qrBitmapLan by remember { mutableStateOf<Bitmap?>(null) }
    val canceladoLan = remember { AtomicBoolean(false) }

    // --- Estado para Modo Óptico ---
    var soloDeltaOptico by remember { mutableStateOf(true) }
    var paqueteOptico by remember { mutableStateOf<PaqueteOpticoGenerado?>(null) }
    var fotogramaActual by remember { mutableIntStateOf(0) }
    var qrBitmapOptico by remember { mutableStateOf<Bitmap?>(null) }

    // Inicializador del servidor LAN
    fun iniciarServidorLan() {
        estado = EstadoAnfitrion.ESPERANDO_ESCANEO
        mensajeEstado = "Esperando escaneo móvil o desktop..."
        errorMensaje = null
        canceladoLan.set(false)

        ambito.launch(Dispatchers.IO) {
            try {
                val s = ServidorSyncP2P.enlazar()
                val uri = ParserSyncQrUri.serializar(s.payloadQr)
                val bitmap = GeneradorQr.generarBitmap(uri, 512)

                withContext(Dispatchers.Main) {
                    servidorLan = s
                    qrBitmapLan = bitmap
                }

                val resultado = s.atenderSincronizacion(
                    bovedaLocal = bovedaActual,
                    timeoutSegundos = 180L,
                    cancelado = canceladoLan,
                    onClienteConectado = {
                        ambito.launch(Dispatchers.Main) {
                            estado = EstadoAnfitrion.SINCRONIZANDO
                            mensajeEstado = "Cliente conectado. Fusionando bóvedas..."
                        }
                    },
                    guardarCommitLocal = { fusionada ->
                        alGuardarBovedaFusionada(fusionada)
                    }
                )

                withContext(Dispatchers.Main) {
                    estado = EstadoAnfitrion.FINALIZADO
                    alFinalizarConInforme(resultado.informeSync)
                }
            } catch (e: Exception) {
                val esCierreVoluntario = canceladoLan.get() ||
                    e is kotlinx.coroutines.CancellationException ||
                    e is java.net.SocketException ||
                    e.cause is java.net.SocketException ||
                    e.message?.contains("Socket closed", ignoreCase = true) == true ||
                    e.message?.contains("EINVAL", ignoreCase = true) == true ||
                    e.message?.contains("cancelada por el usuario", ignoreCase = true) == true

                if (!esCierreVoluntario) {
                    withContext(Dispatchers.Main) {
                        estado = EstadoAnfitrion.ERROR
                        errorMensaje = e.message ?: "Fallo al atender sincronización"
                        Diagnostico.apuntar("sync", "Error en ServidorSyncP2P anfitrión: ${e.message}", e)
                    }
                }
            }
        }
    }

    // Generador de paquete óptico
    fun recalcularPaqueteOptico(soloDelta: Boolean) {
        val paquete = CodecDeltaOptico.empaquetar(bovedaActual, soloDelta = soloDelta)
        paqueteOptico = paquete
        fotogramaActual = 0

        ambito.launch(Dispatchers.Default) {
            val primeraTrama = paquete.tramas.first().serializarUri()
            val bmp = GeneradorQr.generarBitmap(primeraTrama, 512)
            withContext(Dispatchers.Main) {
                qrBitmapOptico = bmp
            }
        }
    }

    // Ciclo de animación si la ráfaga óptica tiene más de 1 trama
    LaunchedEffect(modoSeleccionado, paqueteOptico) {
        if (modoSeleccionado == ModoAnfitrion.OPTICO_QR) {
            val pkg = paqueteOptico ?: return@LaunchedEffect
            if (pkg.totalTramas > 1) {
                while (true) {
                    delay(120) // ~8 fotogramas por segundo
                    fotogramaActual = (fotogramaActual + 1) % pkg.totalTramas
                    val tramaUri = pkg.tramas[fotogramaActual].serializarUri()
                    val bmp = withContext(Dispatchers.Default) {
                        GeneradorQr.generarBitmap(tramaUri, 512)
                    }
                    qrBitmapOptico = bmp
                }
            }
        }
    }

    // Arranque inicial según el modo
    LaunchedEffect(modoSeleccionado) {
        if (modoSeleccionado == ModoAnfitrion.RED_LAN && permiteLan) {
            iniciarServidorLan()
        } else {
            canceladoLan.set(true)
            servidorLan?.close()
            servidorLan = null
            recalcularPaqueteOptico(soloDeltaOptico)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            canceladoLan.set(true)
            servidorLan?.close()
        }
    }

    Dialog(
        onDismissRequest = {
            canceladoLan.set(true)
            servidorLan?.close()
            alCerrar()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(CurvaturaEsquinas))
                .background(ColorTarjetaAjustes)
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, RoundedCornerShape(CurvaturaEsquinas))
                    } else Modifier
                )
                .padding(18.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Barra superior de cierre y título
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CellTower,
                            contentDescription = null,
                            tint = ColorAcento,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "Modo Anfitrión P2P",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextoPrincipal
                        )
                    }

                    IconButton(
                        onClick = {
                            canceladoLan.set(true)
                            servidorLan?.close()
                            alCerrar()
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Cerrar",
                            tint = TextoSecundario
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Selector de Modo (Red LAN vs QR Óptico)
                if (permiteLan) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(FormaCampo)
                            .background(ColorCampoAjustes)
                            .border(0.8.dp, ColorBordeActual, FormaCampo)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        PestanaModoAnfitrion(
                            etiqueta = "Red Local (Wi-Fi)",
                            icono = Icons.Filled.Wifi,
                            seleccionado = modoSeleccionado == ModoAnfitrion.RED_LAN,
                            alPulsar = { modoSeleccionado = ModoAnfitrion.RED_LAN },
                            modifier = Modifier.weight(1f)
                        )
                        PestanaModoAnfitrion(
                            etiqueta = "QR Óptico",
                            icono = Icons.Filled.QrCode,
                            seleccionado = modoSeleccionado == ModoAnfitrion.OPTICO_QR,
                            alPulsar = {
                                canceladoLan.set(true)
                                servidorLan?.close()
                                servidorLan = null
                                modoSeleccionado = ModoAnfitrion.OPTICO_QR
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                }

                // CONTENIDO SEGÚN MODO SELECCIONADO
                if (modoSeleccionado == ModoAnfitrion.RED_LAN && permiteLan) {
                    // MODO RED LOCAL / HOTSPOT
                    val server = servidorLan
                    if (server != null && qrBitmapLan != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = qrBitmapLan!!.asImageBitmap(),
                                contentDescription = "Código QR P2P LAN",
                                filterQuality = FilterQuality.None,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // Huella Visual Determinista
                        Text(
                            text = "HUELLA VISUAL DE EMPAREJAMIENTO",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoSecundario,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = server.huellaVisual,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Menta,
                            letterSpacing = 2.sp
                        )

                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "IP: ${server.ips.joinToString(", ")} • Puerto: ${server.puerto}",
                            fontSize = 11.sp,
                            color = TextoSecundario,
                            fontFamily = FontFamily.Monospace
                        )
                    } else if (errorMensaje != null) {
                        Text(
                            text = errorMensaje!!,
                            color = Peligro,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        CircularProgressIndicator(modifier = Modifier.size(36.dp), color = ColorAcento)
                    }
                } else {
                    // MODO QR ÓPTICO (AIRGAP)
                    val paquete = paqueteOptico
                    if (paquete != null && qrBitmapOptico != null) {
                        // Selector Delta vs Completa
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PildoraOpcionOptica(
                                etiqueta = "Cambios recientes",
                                activa = soloDeltaOptico,
                                alPulsar = {
                                    soloDeltaOptico = true
                                    recalcularPaqueteOptico(true)
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            PildoraOpcionOptica(
                                etiqueta = "Bóveda completa",
                                activa = !soloDeltaOptico,
                                alPulsar = {
                                    soloDeltaOptico = false
                                    recalcularPaqueteOptico(false)
                                }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = qrBitmapOptico!!.asImageBitmap(),
                                contentDescription = "Código QR Óptico",
                                filterQuality = FilterQuality.None,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(Modifier.height(10.dp))

                        // PIN ÓPTICO EFÍMERO
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(FormaCampo)
                                .background(ColorCampoAjustes)
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                        ) {
                            Text(
                                text = "PIN DE AUTORIZACIÓN ÓPTICA",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoSecundario,
                                letterSpacing = 1.sp
                            )
                            val pin = paquete.pinEfimero
                            val pinFormateado = "${pin.substring(0, 3)} ${pin.substring(3, 6)}"
                            Text(
                                text = pinFormateado,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                                color = Advertencia,
                                letterSpacing = 6.sp
                            )
                            Text(
                                text = "Escríbelo en el teléfono receptor para autorizar el descifrado",
                                fontSize = 11.sp,
                                color = TextoSecundario,
                                textAlign = TextAlign.Center
                            )
                        }

                        if (paquete.totalTramas > 1) {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Transmitiendo ráfaga: fotograma ${fotogramaActual + 1} de ${paquete.totalTramas}",
                                fontSize = 12.sp,
                                color = ColorAcento,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = if (paquete.esDelta) "Código QR estático listo (1/1)" else "Bóveda completa en 1 código QR",
                                fontSize = 12.sp,
                                color = Menta,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        CircularProgressIndicator(modifier = Modifier.size(36.dp), color = ColorAcento)
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Estado y barra inferior
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (estado == EstadoAnfitrion.SINCRONIZANDO) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = ColorAcento)
                    } else {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (estado == EstadoAnfitrion.ERROR) Peligro else Menta)
                        )
                    }
                    Text(
                        text = mensajeEstado,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
            }
        }
    }
}

@Composable
private fun PestanaModoAnfitrion(
    etiqueta: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    seleccionado: Boolean,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fondo = if (seleccionado) ColorAcento.copy(alpha = 0.2f) else Color.Transparent
    val colorTexto = if (seleccionado) TextoPrincipal else TextoSecundario

    Row(
        modifier = modifier
            .clip(FormaCampo)
            .background(fondo)
            .clickable(onClick = alPulsar)
            .padding(vertical = 8.dp, horizontal = 6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = if (seleccionado) ColorAcento else TextoSecundario,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = etiqueta,
            fontSize = 11.sp,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
            color = colorTexto,
            maxLines = 1
        )
    }
}

@Composable
private fun PildoraOpcionOptica(
    etiqueta: String,
    activa: Boolean,
    alPulsar: () -> Unit
) {
    val fondo = if (activa) ColorAcento.copy(alpha = 0.22f) else ColorCampoAjustes
    val borde = if (activa) ColorAcento else ColorBordeActual

    Box(
        modifier = Modifier
            .height(38.dp)
            .clip(RoundedCornerShape(19.dp))
            .background(fondo)
            .border(1.dp, borde, RoundedCornerShape(19.dp))
            .clickable(onClick = alPulsar)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = etiqueta,
            fontSize = 12.sp,
            fontWeight = if (activa) FontWeight.Bold else FontWeight.Normal,
            color = if (activa) TextoPrincipal else TextoSecundario,
            maxLines = 1
        )
    }
}
