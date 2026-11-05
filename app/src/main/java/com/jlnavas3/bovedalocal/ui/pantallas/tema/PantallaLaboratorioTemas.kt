/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tema

import com.jlnavas3.bovedalocal.ui.componentes.LocalAvisador
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda
import com.jlnavas3.bovedalocal.ui.theme.GestorPaletaSobria
import androidx.compose.foundation.ExperimentalFoundationApi
import com.jlnavas3.bovedalocal.ui.theme.ParametrosLaboratorio
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Laboratorio de Temas y Paleta Sobria:
 * Herramienta visual interactiva para ajustar la escala neutra de grises y el acento esencial,
 * aplicando la filosofía 90% neutro + 10% acento con restricción inteligente de luminancia.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaLaboratorioTemas(
    vm: VaultViewModel? = null,
    seccionDestino: String? = null,
    alVolver: () -> Unit = { vm?.volverAtras() }
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val portapapeles = LocalClipboardManager.current
    val avisador = LocalAvisador.current
    val scrollState = rememberScrollState()
    val ajustes = vm?.ajustes?.collectAsStateWithLifecycle()?.value ?: remember { AjustesApp() }
    val reqDinamico = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null && (seccionDestino == "02-APA-THM-DYN" || seccionDestino == "09.2.2")) {
            reqDinamico.bringIntoView()
        }
    }

    val estadoLab = remember { EstadoLaboratorioTemas(contexto) }
    var mostrarModalConfirmacionGuardado by remember { mutableStateOf(false) }

    with(estadoLab) {
        val tonoEfectivoFondo = if (unificarTonos) tonoGlobal else tonoFondo
        val tonoEfectivoTarjeta = if (unificarTonos) tonoGlobal else tonoTarjeta
        val tonoEfectivoCampo = if (unificarTonos) tonoGlobal else tonoCampo
        val tonoEfectivoModal = if (unificarTonos) tonoGlobal else tonoModal
        val tonoEfectivoBorde = if (unificarTonos) tonoGlobal else tonoBorde
        val tonoEfectivoTextoPrincipal = if (unificarTonos) tonoGlobal else tonoTextoPrincipal
        val tonoEfectivoTextoSecundario = if (unificarTonos) tonoGlobal else tonoTextoSecundario

        val colorFondo = remember(lumFondo, tonoEfectivoFondo, saturacionTinte, modoOscuro) {
            calcularColorCapaLab(tonoEfectivoFondo, lumFondo, saturacionTinte, modoOscuro, esSuperficie = true)
        }
        val colorTarjeta = remember(lumTarjeta, tonoEfectivoTarjeta, saturacionTinte, modoOscuro) {
            calcularColorCapaLab(tonoEfectivoTarjeta, lumTarjeta, saturacionTinte, modoOscuro, esSuperficie = true)
        }
        val colorCampo = remember(lumCampo, tonoEfectivoCampo, saturacionTinte, modoOscuro) {
            calcularColorCapaLab(tonoEfectivoCampo, lumCampo, saturacionTinte, modoOscuro, esSuperficie = true)
        }
        val colorModal = remember(lumModal, tonoEfectivoModal, saturacionTinte, modoOscuro) {
            calcularColorCapaLab(tonoEfectivoModal, lumModal, saturacionTinte, modoOscuro, esSuperficie = true)
        }
        val colorBorde = remember(lumBorde, tonoEfectivoBorde, saturacionTinte, modoOscuro) {
            calcularColorCapaLab(tonoEfectivoBorde, lumBorde, saturacionTinte, modoOscuro, esSuperficie = true)
        }
        val colorTextoPrincipal = remember(lumTextoPrincipal, tonoEfectivoTextoPrincipal, saturacionTinte, modoOscuro) {
            calcularColorCapaLab(tonoEfectivoTextoPrincipal, lumTextoPrincipal, saturacionTinte, modoOscuro, esSuperficie = false)
        }
        val colorTextoSecundario = remember(lumTextoSecundario, tonoEfectivoTextoSecundario, saturacionTinte, modoOscuro) {
            calcularColorCapaLab(tonoEfectivoTextoSecundario, lumTextoSecundario, saturacionTinte, modoOscuro, esSuperficie = false)
        }

        val acentosPredefinidos = remember { ACENTOS_PREDEFINIDOS_LAB }
        val acentosGrisesNeutros = remember(modoOscuro) { obtenerAcentosGrisesNeutros(modoOscuro) }

        fun marcarModificado(esPreset: Boolean = false) {
            estadoLab.marcarModificado(esPreset)
        }

        fun ejecutarGuardado() {
            val paletaOsc = construirPaletaDesdeEstado(estadoLab, true)
            val paletaCla = construirPaletaDesdeEstado(estadoLab, false)

            val paramsOsc = ParametrosLaboratorio(
                lumFondo = lumFondoOscuro,
                lumTarjeta = lumTarjetaOscuro,
                lumCampo = lumCampoOscuro,
                lumBorde = lumBordeOscuro,
                lumTextoPrincipal = lumTextoPrincipalOscuro,
                lumTextoSecundario = lumTextoSecundarioOscuro,
                tonoGlobal = tonoGlobalOscuro,
                saturacionTinte = saturacionTinteOscuro,
                tonoFondo = tonoFondoOscuro,
                tonoTarjeta = tonoTarjetaOscuro,
                tonoCampo = tonoCampoOscuro,
                tonoBorde = tonoBordeOscuro,
                tonoTextoPrincipal = tonoTextoPrincipalOscuro,
                tonoTextoSecundario = tonoTextoSecundarioOscuro,
                acentoHue = huePersonalizadoOscuro,
                acentoSat = satPersonalizadoOscuro,
                acentoVal = valPersonalizadoOscuro,
                acentoAlfa = alfaPersonalizadoOscuro,
                esPersonalizado = esPersonalizadoActivoOscuro,
                lumModal = lumModalOscuro,
                tonoModal = tonoModalOscuro
            )

            val paramsCla = ParametrosLaboratorio(
                lumFondo = lumFondoClaro,
                lumTarjeta = lumTarjetaClaro,
                lumCampo = lumCampoClaro,
                lumBorde = lumBordeClaro,
                lumTextoPrincipal = lumTextoPrincipalClaro,
                lumTextoSecundario = lumTextoSecundarioClaro,
                tonoGlobal = tonoGlobalClaro,
                saturacionTinte = saturacionTinteClaro,
                tonoFondo = tonoFondoClaro,
                tonoTarjeta = tonoTarjetaClaro,
                tonoCampo = tonoCampoClaro,
                tonoBorde = tonoBordeClaro,
                tonoTextoPrincipal = tonoTextoPrincipalClaro,
                tonoTextoSecundario = tonoTextoSecundarioClaro,
                acentoHue = huePersonalizadoClaro,
                acentoSat = satPersonalizadoClaro,
                acentoVal = valPersonalizadoClaro,
                acentoAlfa = alfaPersonalizadoClaro,
                esPersonalizado = esPersonalizadoActivoClaro,
                lumModal = lumModalClaro,
                tonoModal = tonoModalClaro
            )

            GestorPaletaSobria.guardar(
                context = contexto,
                paletaOscura = paletaOsc,
                paletaClara = paletaCla,
                unificarTonos = unificarTonos,
                paramsOscuro = paramsOsc,
                paramsClaro = paramsCla
            )
            modificadoOscuro = false
            modificadoClaro = false
            avisador("¡Tema guardado con éxito!", "Tema")
        }

        fun solicitarGuardar() {
            if (!modificadoOscuro && !modificadoClaro) {
                avisador("No hay cambios pendientes por guardar", "Tema")
                return
            }
            if (modificadoOscuro && modificadoClaro) {
                ejecutarGuardado()
            } else {
                mostrarModalConfirmacionGuardado = true
            }
        }

        fun copiarPaletaAlPortapapeles() {
            ExportadorPaletaLaboratorio.copiarAlPortapapeles(
                contexto = contexto,
                haptica = haptica,
                portapapeles = portapapeles,
                modoOscuro = modoOscuro,
                colorFondo = colorFondo,
                colorTarjeta = colorTarjeta,
                colorCampo = colorCampo,
                colorModal = colorModal,
                colorBorde = colorBorde,
                colorTextoPrincipal = colorTextoPrincipal,
                colorTextoSecundario = colorTextoSecundario,
                colorAcentoActual = colorAcentoActual,
                unificarTonos = unificarTonos,
                tonoGlobal = tonoGlobal,
                saturacionTinte = saturacionTinte,
                lumFondo = lumFondo,
                tonoEfectivoFondo = tonoEfectivoFondo,
                lumTarjeta = lumTarjeta,
                tonoEfectivoTarjeta = tonoEfectivoTarjeta,
                lumCampo = lumCampo,
                tonoEfectivoCampo = tonoEfectivoCampo,
                lumModal = lumModal,
                tonoEfectivoModal = tonoEfectivoModal,
                lumBorde = lumBorde,
                tonoEfectivoBorde = tonoEfectivoBorde,
                lumTextoPrincipal = lumTextoPrincipal,
                tonoEfectivoTextoPrincipal = tonoEfectivoTextoPrincipal,
                lumTextoSecundario = lumTextoSecundario,
                tonoEfectivoTextoSecundario = tonoEfectivoTextoSecundario,
                alAvisar = avisador
            )
        }

        var switchDemoActivo by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Laboratorio de Temas",
                alVolver = alVolver
            )

            SelectorModoColorLab(
                modoOscuro = modoOscuro,
                modificadoOscuro = modificadoOscuro,
                modificadoClaro = modificadoClaro,
                colorAcentoActual = colorAcentoActual,
                alSeleccionarModo = { modoOscuro = it },
                haptica = haptica
            )

            TarjetaFlotantePreviaLaboratorio(
                colorFondo = colorFondo,
                colorTarjeta = colorTarjeta,
                colorModal = colorModal,
                colorBorde = colorBorde,
                colorCampo = colorCampo,
                colorTextoPrincipal = colorTextoPrincipal,
                colorTextoSecundario = colorTextoSecundario,
                colorAcento = colorAcentoActual,
                switchActivo = switchDemoActivo,
                onAlternarSwitch = {
                    haptica.tic()
                    switchDemoActivo = it
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                FilaPresetsRapidosLab(
                    modoOscuro = modoOscuro,
                    presetActivoId = estadoLab.presetActivoId,
                    alSeleccionarPreset = { preset ->
                        lumFondo = preset.lumFondo
                        lumTarjeta = preset.lumTarjeta
                        lumCampo = preset.lumCampo
                        lumModal = preset.lumModal
                        lumBorde = preset.lumBorde
                        lumTextoPrincipal = preset.lumTextoPrincipal
                        lumTextoSecundario = preset.lumTextoSecundario
                        tonoGlobal = preset.tonoGlobal
                        saturacionTinte = preset.saturacionTinte
                        estadoLab.presetActivoId = preset.id
                        marcarModificado(esPreset = true)
                    },
                    haptica = haptica
                )

                // Color dinámico del sistema (Material You - Android 12+)
                SeccionColorDinamicoYSistema(
                    ajustes = ajustes,
                    haptica = haptica,
                    reqDinamico = reqDinamico,
                    alAlternarColorDinamico = { vm?.alternarColorDinamicoSistema(it) }
                )

                Spacer(Modifier.height(14.dp))

                SeccionEscalaCapasLab(
                    estadoLab = estadoLab,
                    colorFondo = colorFondo,
                    colorTarjeta = colorTarjeta,
                    colorCampo = colorCampo,
                    colorModal = colorModal,
                    colorBorde = colorBorde,
                    colorTextoPrincipal = colorTextoPrincipal,
                    colorTextoSecundario = colorTextoSecundario,
                    colorAcentoActual = colorAcentoActual,
                    haptica = haptica,
                    marcarModificado = { marcarModificado(false) }
                )

                Spacer(Modifier.height(14.dp))

                SeccionAcentoEsencialLab(
                    estadoLab = estadoLab,
                    colorCampo = colorCampo,
                    colorBorde = colorBorde,
                    colorTextoPrincipal = colorTextoPrincipal,
                    colorTextoSecundario = colorTextoSecundario,
                    acentosPredefinidos = acentosPredefinidos,
                    acentosGrisesNeutros = acentosGrisesNeutros,
                    haptica = haptica,
                    marcarModificado = { marcarModificado(false) }
                )

                Spacer(Modifier.height(24.dp))
            }

            BarraAccionesDockedLab(
                colorAcentoActual = colorAcentoActual,
                alRestablecer = {
                    haptica.toque()
                    restablecerValores(contexto)
                    avisador("Valores de fábrica restablecidos", "Tema")
                },
                alCopiarPaleta = ::copiarPaletaAlPortapapeles,
                alGuardar = ::solicitarGuardar
            )
        }

        DialogoConfirmacionGuardarTema(
            visible = mostrarModalConfirmacionGuardado,
            modificadoOscuro = modificadoOscuro,
            alConfirmar = {
                mostrarModalConfirmacionGuardado = false
                ejecutarGuardado()
            },
            alDescartar = {
                mostrarModalConfirmacionGuardado = false
            }
        )
    }
}

@BovedaPantallaPreview
@Composable
private fun PreviewPantallaLaboratorioTemas() {
    PreviewTemaBoveda {
        PantallaLaboratorioTemas()
    }
}
