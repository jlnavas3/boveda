/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.cxf.CxfGestorTransferencia
import com.jlnavas3.bovedalocal.cxf.ResultadoRegistroExportacion
import com.jlnavas3.bovedalocal.data.ModoTransferenciaCxf
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Pantalla de configuración para la transferencia directa de credenciales FIDO CXF.
 * Permite alternar entre transferencia Automática y Manual, fijar el tiempo de ventana
 * de disponibilidad y resolver conflictos con el bloqueo por inactividad.
 */
@Composable
fun PantallaAjustesTransferenciaCxf(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val modo = ajustes.modoTransferenciaCxfEfectivo
    var tiempoEnConflicto by remember { mutableStateOf<Int?>(null) }
    var segundosRestantes by remember { mutableIntStateOf(CxfGestorTransferencia.segundosRestantesVentana()) }
    var transferenciaActiva by remember { mutableStateOf(CxfGestorTransferencia.estaTransferenciaActiva(ajustes)) }

    // Actualizar cuenta regresiva en vivo si la transferencia manual está activa
    LaunchedEffect(modo, transferenciaActiva) {
        while (isActive) {
            val activa = CxfGestorTransferencia.estaTransferenciaActiva(ajustes)
            transferenciaActiva = activa
            segundosRestantes = CxfGestorTransferencia.segundosRestantesVentana()
            if (!activa && modo == ModoTransferenciaCxf.MANUAL && CxfGestorTransferencia.finVentanaManualMillis > 0L) {
                // Expiró la ventana manual
                withContext(Dispatchers.IO) {
                    CxfGestorTransferencia.limpiarExportacion(contexto)
                }
            }
            delay(1000L)
        }
    }

    fun textoDuracion(segundos: Int): String = when {
        segundos == -1 -> "Hasta bloquear la bóveda"
        segundos < 60 -> "$segundos segundos"
        segundos % 60 == 0 -> {
            val m = segundos / 60
            "$m ${if (m == 1) "minuto" else "minutos"}"
        }
        else -> "$segundos segundos"
    }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Transferencia de credenciales",
                idEtiqueta = "05-COP-CXF-AJU",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(
                    subtitulo = "Configura la disponibilidad para transferir credenciales con Google y otros gestores"
                )

                Spacer(Modifier.height(10.dp))

                TarjetaInformativaCxf()

                Spacer(Modifier.height(EspaciadoComponentes))

                GrupoModoTransferenciaCxf(
                    modoActual = modo,
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alCambiarModo = { nuevoModo ->
                        vm.repositorio.ajustes.actualizar { it.copy(cxfModoTransferencia = nuevoModo.clave) }
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                if (nuevoModo == ModoTransferenciaCxf.AUTOMATICA) {
                                    val entradas = vm.repositorio.entradas().filter { it.eliminadaEn == 0L }
                                    if (entradas.isNotEmpty()) {
                                        CxfGestorTransferencia.sincronizarExportacionAutomatica(
                                            context = contexto,
                                            entradas = entradas,
                                            ajustes = ajustes.copy(cxfModoTransferencia = nuevoModo.clave)
                                        )
                                    }
                                } else {
                                    CxfGestorTransferencia.limpiarExportacion(contexto)
                                }
                            }
                            transferenciaActiva = CxfGestorTransferencia.estaTransferenciaActiva(
                                ajustes.copy(cxfModoTransferencia = nuevoModo.clave)
                            )
                        }
                    }
                )

                if (modo == ModoTransferenciaCxf.MANUAL) {
                    Spacer(Modifier.height(EspaciadoComponentes))

                    GrupoVentanaManualCxf(
                        segundosSeleccionados = ajustes.cxfTiempoVentanaManualSegundos,
                        autoBloqueoActualSegundos = ajustes.autoBloqueoSegundos,
                        transferenciaActiva = transferenciaActiva,
                        segundosRestantes = segundosRestantes,
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiarTiempo = { nuevoTiempo ->
                            vm.repositorio.ajustes.actualizar { it.copy(cxfTiempoVentanaManualSegundos = nuevoTiempo) }
                            if (transferenciaActiva) {
                                scope.launch {
                                    withContext(Dispatchers.IO) {
                                        val entradas = vm.repositorio.entradas().filter { it.eliminadaEn == 0L }
                                        CxfGestorTransferencia.habilitarTransferenciaManual(
                                            context = contexto,
                                            entradas = entradas,
                                            duracionSegundos = nuevoTiempo
                                        )
                                    }
                                }
                            }
                        },
                        alDetectarConflicto = { tiempoConflicto ->
                            tiempoEnConflicto = tiempoConflicto
                        },
                        alAlternarDisponibilidad = { habilitar ->
                            scope.launch {
                                if (habilitar) {
                                    val res = withContext(Dispatchers.IO) {
                                        val entradas = vm.repositorio.entradas().filter { it.eliminadaEn == 0L }
                                        CxfGestorTransferencia.habilitarTransferenciaManual(
                                            context = contexto,
                                            entradas = entradas,
                                            duracionSegundos = ajustes.cxfTiempoVentanaManualSegundos
                                        )
                                    }
                                    if (res is ResultadoRegistroExportacion.Exito) {
                                        vm.mostrarAviso("Transferencia directa habilitada")
                                        transferenciaActiva = true
                                        segundosRestantes = CxfGestorTransferencia.segundosRestantesVentana()
                                    } else if (res is ResultadoRegistroExportacion.Error) {
                                        vm.mostrarError(res.mensaje)
                                    }
                                } else {
                                    withContext(Dispatchers.IO) {
                                        CxfGestorTransferencia.limpiarExportacion(contexto)
                                    }
                                    vm.mostrarAviso("Transferencia directa desactivada")
                                    transferenciaActiva = false
                                }
                            }
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    // Modal de conflicto si el tiempo elegido supera el auto-bloqueo
    tiempoEnConflicto?.let { tiempoConflicto ->
        DialogoConflictoTiempoTransferenciaCxf(
            tiempoTransferenciaTexto = textoDuracion(tiempoConflicto),
            tiempoBloqueoActualTexto = textoDuracion(ajustes.autoBloqueoSegundos),
            alAjustarBloqueo = {
                vm.repositorio.ajustes.actualizar {
                    it.copy(
                        autoBloqueoSegundos = tiempoConflicto,
                        cxfTiempoVentanaManualSegundos = tiempoConflicto
                    )
                }
                if (transferenciaActiva) {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            val entradas = vm.repositorio.entradas().filter { it.eliminadaEn == 0L }
                            CxfGestorTransferencia.habilitarTransferenciaManual(
                                context = contexto,
                                entradas = entradas,
                                duracionSegundos = tiempoConflicto
                            )
                        }
                    }
                }
                tiempoEnConflicto = null
            },
            alMantener = {
                vm.repositorio.ajustes.actualizar {
                    it.copy(cxfTiempoVentanaManualSegundos = tiempoConflicto)
                }
                if (transferenciaActiva) {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            val entradas = vm.repositorio.entradas().filter { it.eliminadaEn == 0L }
                            CxfGestorTransferencia.habilitarTransferenciaManual(
                                context = contexto,
                                entradas = entradas,
                                duracionSegundos = tiempoConflicto
                            )
                        }
                    }
                }
                tiempoEnConflicto = null
            },
            alCancelar = {
                tiempoEnConflicto = null
            }
        )
    }
}
