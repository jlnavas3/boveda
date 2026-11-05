/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.cxf.CxfExportador
import com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia
import com.jlnavas3.bovedalocal.cxf.ResultadoLimpiezaExportacion
import com.jlnavas3.bovedalocal.cxf.ResultadoRegistroExportacion
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Diálogo orquestador para la transferencia directa de credenciales mediante el estándar
 * FIDO CXF (Credential Exchange Format). Admite tanto exportación completa como selectiva.
 * Presenta cabecera compacta en una sola línea, selector de disponibilidad y estados claros.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DialogoExportacionDirectaCxf(
    entradas: List<Entrada>,
    esSeleccionPersonalizada: Boolean = false,
    alCerrar: () -> Unit,
    alAbrirAjustes: (() -> Unit)? = null
) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()
    val repo = remember { VaultRepository.obtener(contexto) }
    val ajustes = repo.ajustes.actual

    var enProgreso by remember { mutableStateOf(false) }
    var mensajeEstado by remember { mutableStateOf<String?>(null) }
    var tiempoSeleccionado by remember { mutableIntStateOf(ajustes.cxfTiempoVentanaManualSegundos) }
    var tiempoEnConflicto by remember { mutableStateOf<Int?>(null) }

    var transferenciaActiva by remember { mutableStateOf(CxfGestorTransferencia.estaTransferenciaActiva(ajustes)) }
    var segundosRestantes by remember { mutableIntStateOf(CxfGestorTransferencia.segundosRestantesVentana()) }

    val activas = remember(entradas) { entradas.filter { it.eliminadaEn == 0L } }
    val passkeysCount = remember(activas) { activas.count { it.passkey != null } }
    val contrasenasCount = remember(activas) { activas.count { it.contrasena.isNotBlank() } }
    val totpCount = remember(activas) { activas.count { !it.secretoTotp.isNullOrBlank() } }

    fun textoDuracion(segundos: Int): String = when {
        segundos == -1 -> "Hasta bloquear"
        segundos < 60 -> "$segundos seg"
        segundos % 60 == 0 -> {
            val m = segundos / 60
            "$m ${if (m == 1) "minuto" else "minutos"}"
        }
        else -> "$segundos s"
    }

    LaunchedEffect(transferenciaActiva) {
        while (isActive && transferenciaActiva) {
            val activa = CxfGestorTransferencia.estaTransferenciaActiva(ajustes)
            transferenciaActiva = activa
            segundosRestantes = CxfGestorTransferencia.segundosRestantesVentana()
            if (!activa) {
                withContext(Dispatchers.IO) {
                    CxfGestorTransferencia.limpiarExportacion(contexto)
                }
            }
            delay(1000L)
        }
    }

    DialogoBoveda(
        onDismissRequest = alCerrar,
        icon = null,
        title = {
            CabeceraDialogoTransferirCxf(
                totalSeleccionadas = activas.size,
                esSeleccionPersonalizada = esSeleccionPersonalizada,
                alAbrirAjustes = alAbrirAjustes
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo(
                    texto = if (esSeleccionPersonalizada) {
                        "Transfiere las credenciales seleccionadas directamente a otro gestor o dispositivo Android de forma local, cifrada y sin usar internet."
                    } else {
                        "Transfiere llaves de acceso, contraseñas y códigos 2FA directamente a otro gestor o dispositivo Android de forma local, cifrada y sin usar internet."
                    }
                )

                // Resumen de credenciales
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (passkeysCount > 0) {
                        InsigniaConteoCxf(
                            icono = Icons.Filled.VpnKey,
                            texto = "$passkeysCount llaves de acceso",
                            color = ColorPasskeys
                        )
                    }
                    if (contrasenasCount > 0) {
                        InsigniaConteoCxf(
                            icono = Icons.Filled.Password,
                            texto = "$contrasenasCount contraseñas",
                            color = ColorDatosContrasena
                        )
                    }
                    if (totpCount > 0) {
                        InsigniaConteoCxf(
                            icono = Icons.Filled.QrCode,
                            texto = "$totpCount verificación",
                            color = Color2FA
                        )
                    }
                }

                // Selector de disponibilidad (solo si no está activa la transferencia)
                if (!transferenciaActiva) {
                    SelectorDisponibilidadDialogoCxf(
                        segundosSeleccionados = tiempoSeleccionado,
                        autoBloqueoActualSegundos = ajustes.autoBloqueoSegundos,
                        alCambiarTiempo = { nuevoTiempo ->
                            tiempoSeleccionado = nuevoTiempo
                        },
                        alDetectarConflicto = { tiempoConflicto ->
                            tiempoEnConflicto = tiempoConflicto
                        }
                    )
                }

                // Banner de estado modular
                if (transferenciaActiva) {
                    val textoBanner = if (segundosRestantes == -1) {
                        "Listo para transferir: Disponible hasta bloquear la bóveda."
                    } else {
                        val min = segundosRestantes / 60
                        val seg = segundosRestantes % 60
                        val tiempoFmt = String.format("%02d:%02d", min, seg)
                        "Listo para transferir: Expira en $tiempoFmt. Inicia la importación en el otro gestor."
                    }
                    BannerEstadoExportacionCxf(
                        mensaje = textoBanner,
                        estaHabilitado = true
                    )
                } else if (mensajeEstado != null) {
                    BannerEstadoExportacionCxf(
                        mensaje = mensajeEstado ?: "",
                        estaHabilitado = false
                    )
                }

                if (enProgreso) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(28.dp), color = ColorPasskeys)
                    }
                }

                Spacer(Modifier.height(2.dp))

                // Botón compartir archivo CXF (texto conciso que no se trunca)
                BotonBoveda(
                    texto = "Compartir archivo .cxf",
                    icono = Icons.Filled.Share,
                    variante = VarianteBoton.SECUNDARIO,
                    alPulsar = {
                        val json = CxfExportador.exportarAJson(activas)
                        CompartidorArchivoCxf.compartir(contexto, json)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            if (!transferenciaActiva) {
                val textoBoton = if (tiempoSeleccionado == -1) {
                    "Habilitar hasta bloquear"
                } else {
                    "Habilitar por ${textoDuracion(tiempoSeleccionado)}"
                }
                BotonBoveda(
                    texto = textoBoton,
                    variante = VarianteBoton.PRIMARIO,
                    alPulsar = {
                        scope.launch {
                            enProgreso = true
                            when (val res = withContext(Dispatchers.IO) {
                                CxfGestorTransferencia.habilitarTransferenciaManual(
                                    context = contexto,
                                    entradas = activas,
                                    duracionSegundos = tiempoSeleccionado,
                                    esSeleccionPersonalizada = esSeleccionPersonalizada
                                )
                            }) {
                                is ResultadoRegistroExportacion.Exito -> {
                                    transferenciaActiva = true
                                    segundosRestantes = CxfGestorTransferencia.segundosRestantesVentana()
                                    mensajeEstado = null
                                }
                                is ResultadoRegistroExportacion.Error -> {
                                    transferenciaActiva = false
                                    mensajeEstado = res.mensaje
                                }
                            }
                            enProgreso = false
                        }
                    }
                )
            } else {
                BotonTextoBoveda(
                    texto = "Desactivar",
                    alPulsar = {
                        scope.launch {
                            enProgreso = true
                            when (val res = withContext(Dispatchers.IO) {
                                CxfGestorTransferencia.limpiarExportacion(contexto)
                            }) {
                                is ResultadoLimpiezaExportacion.Exito -> {
                                    transferenciaActiva = false
                                    mensajeEstado = null
                                }
                                is ResultadoLimpiezaExportacion.Error -> {
                                    mensajeEstado = res.mensaje
                                }
                            }
                            enProgreso = false
                        }
                    }
                )
            }
        },
        dismissButton = {
            BotonTextoBoveda(
                texto = "Cerrar",
                alPulsar = alCerrar
            )
        }
    )

    // Modal de conflicto si el tiempo elegido supera el auto-bloqueo
    tiempoEnConflicto?.let { tiempoConflicto ->
        DialogoConflictoTiempoTransferenciaCxf(
            tiempoTransferenciaTexto = textoDuracion(tiempoConflicto),
            tiempoBloqueoActualTexto = textoDuracion(ajustes.autoBloqueoSegundos),
            alAjustarBloqueo = {
                repo.ajustes.actualizar {
                    it.copy(
                        autoBloqueoSegundos = tiempoConflicto,
                        cxfTiempoVentanaManualSegundos = tiempoConflicto
                    )
                }
                tiempoSeleccionado = tiempoConflicto
                tiempoEnConflicto = null
            },
            alMantener = {
                tiempoSeleccionado = tiempoConflicto
                tiempoEnConflicto = null
            },
            alCancelar = {
                tiempoEnConflicto = null
            }
        )
    }
}
