package com.jlnavas3.bovedalocal.ui.pantallas.tema

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.colorAhsv
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda
import com.jlnavas3.bovedalocal.ui.theme.GestorPaletaSobria
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobria
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobriaDefaults
import com.jlnavas3.bovedalocal.ui.theme.ParametrosLaboratorio
import com.jlnavas3.bovedalocal.ui.theme.aHexConAlfa
import com.jlnavas3.bovedalocal.ui.theme.crearGris
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaClara
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaOscura
import com.jlnavas3.bovedalocal.ui.theme.restringirLuminancia
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Laboratorio de Temas y Paleta Sobria:
 * Herramienta visual interactiva para ajustar la escala neutra de grises y el acento esencial,
 * aplicando la filosofía 90% neutro + 10% acento con restricción inteligente de luminancia.
 */
@Composable
fun PantallaLaboratorioTemas(
    vm: VaultViewModel? = null,
    seccionDestino: String? = null,
    alVolver: () -> Unit = { vm?.volverAtras() }
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val portapapeles = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    val estadoLab = remember { EstadoLaboratorioTemas(contexto) }

    with(estadoLab) {
        val tonoEfectivoFondo = if (unificarTonos) tonoGlobal else tonoFondo
        val tonoEfectivoTarjeta = if (unificarTonos) tonoGlobal else tonoTarjeta
        val tonoEfectivoCampo = if (unificarTonos) tonoGlobal else tonoCampo
        val tonoEfectivoBorde = if (unificarTonos) tonoGlobal else tonoBorde
        val tonoEfectivoTextoPrincipal = if (unificarTonos) tonoGlobal else tonoTextoPrincipal
        val tonoEfectivoTextoSecundario = if (unificarTonos) tonoGlobal else tonoTextoSecundario

        fun calcularColorCapa(tono: Float, lum: Float, esSuperficie: Boolean): Color {
            val lumRestringida = restringirLuminancia(lum, modoOscuro, esSuperficie)
            return if (saturacionTinte <= 0.001f) {
                crearGris(lumRestringida)
            } else {
                Color.hsv(tono, saturacionTinte.coerceIn(0f, 1f), lumRestringida)
            }
        }

        // Colores calculados reactivamente
        val colorFondo = remember(lumFondo, tonoEfectivoFondo, saturacionTinte, modoOscuro) {
            calcularColorCapa(tonoEfectivoFondo, lumFondo, esSuperficie = true)
        }
        val colorTarjeta = remember(lumTarjeta, tonoEfectivoTarjeta, saturacionTinte, modoOscuro) {
            calcularColorCapa(tonoEfectivoTarjeta, lumTarjeta, esSuperficie = true)
        }
        val colorCampo = remember(lumCampo, tonoEfectivoCampo, saturacionTinte, modoOscuro) {
            calcularColorCapa(tonoEfectivoCampo, lumCampo, esSuperficie = true)
        }
        val colorBorde = remember(lumBorde, tonoEfectivoBorde, saturacionTinte, modoOscuro) {
            calcularColorCapa(tonoEfectivoBorde, lumBorde, esSuperficie = true)
        }
        val colorTextoPrincipal = remember(lumTextoPrincipal, tonoEfectivoTextoPrincipal, saturacionTinte, modoOscuro) {
            calcularColorCapa(tonoEfectivoTextoPrincipal, lumTextoPrincipal, esSuperficie = false)
        }
        val colorTextoSecundario = remember(lumTextoSecundario, tonoEfectivoTextoSecundario, saturacionTinte, modoOscuro) {
            calcularColorCapa(tonoEfectivoTextoSecundario, lumTextoSecundario, esSuperficie = false)
        }

        val acentosPredefinidos = remember {
            listOf(
                Color(0xFFE5A93C) to "Ámbar Bóveda",
                Color(0xFFD4AF37) to "Oro Clásico",
                Color(0xFFC87D55) to "Bronce Cálido",
                Color(0xFF3B82F6) to "Zafiro Seguridad",
                Color(0xFF10B981) to "Esmeralda",
                Color(0xFF8B5CF6) to "Amatista"
            )
        }

        val acentosGrisesNeutros = remember(modoOscuro) {
            if (modoOscuro) {
                listOf(
                    Color(0xFFE1E1E6) to "Titanio Platino",
                    Color(0xFFB0B0B8) to "Gris Espacial",
                    Color(0xFF9FA4B2) to "Pizarra Fría",
                    Color(0xFFB8B2AA) to "Piedra Cálida",
                    Color(0xFFC8C8CE) to "Plata Niebla"
                )
            } else {
                listOf(
                    Color(0xFF2C2C2E) to "Titanio Carbón",
                    Color(0xFF3A3A3C) to "Gris Espacial",
                    Color(0xFF323842) to "Pizarra Fría",
                    Color(0xFF3E3A36) to "Piedra Cálida",
                    Color(0xFF48484A) to "Plata Grafito"
                )
            }
        }

        fun marcarModificado() {
            if (modoOscuro) modificadoOscuro = true else modificadoClaro = true
        }

        fun construirPaleta(esOscuro: Boolean): PaletaSobria {
            val sat = if (esOscuro) saturacionTinteOscuro else saturacionTinteClaro
            val tonoG = if (esOscuro) tonoGlobalOscuro else tonoGlobalClaro
            val effFondo = if (unificarTonos) tonoG else (if (esOscuro) tonoFondoOscuro else tonoFondoClaro)
            val effTarjeta = if (unificarTonos) tonoG else (if (esOscuro) tonoTarjetaOscuro else tonoTarjetaClaro)
            val effCampo = if (unificarTonos) tonoG else (if (esOscuro) tonoCampoOscuro else tonoCampoClaro)
            val effBorde = if (unificarTonos) tonoG else (if (esOscuro) tonoBordeOscuro else tonoBordeClaro)
            val effTextoP = if (unificarTonos) tonoG else (if (esOscuro) tonoTextoPrincipalOscuro else tonoTextoPrincipalClaro)
            val effTextoS = if (unificarTonos) tonoG else (if (esOscuro) tonoTextoSecundarioOscuro else tonoTextoSecundarioClaro)

            fun calc(tono: Float, lum: Float, esSuperficie: Boolean): Color {
                val lumRestringida = restringirLuminancia(lum, esOscuro, esSuperficie)
                return if (sat <= 0.001f) {
                    crearGris(lumRestringida)
                } else {
                    Color.hsv(tono, sat.coerceIn(0f, 1f), lumRestringida)
                }
            }

            return PaletaSobria(
                esOscuro = esOscuro,
                fondo = calc(effFondo, if (esOscuro) lumFondoOscuro else lumFondoClaro, true),
                tarjeta = calc(effTarjeta, if (esOscuro) lumTarjetaOscuro else lumTarjetaClaro, true),
                campo = calc(effCampo, if (esOscuro) lumCampoOscuro else lumCampoClaro, true),
                borde = calc(effBorde, if (esOscuro) lumBordeOscuro else lumBordeClaro, true),
                textoPrincipal = calc(effTextoP, if (esOscuro) lumTextoPrincipalOscuro else lumTextoPrincipalClaro, false),
                textoSecundario = calc(effTextoS, if (esOscuro) lumTextoSecundarioOscuro else lumTextoSecundarioClaro, false),
                acento = if (esOscuro) colorAcentoOscuro else colorAcentoClaro
            )
        }

        var mostrarModalConfirmacionGuardado by remember { mutableStateOf(false) }

        fun ejecutarGuardado() {
            haptica.exito()
            val paletaOsc = if (modificadoOscuro) construirPaleta(true) else paletaSobriaGuardadaOscura ?: PaletaSobriaDefaults.OSCURA
            val paletaCla = if (modificadoClaro) construirPaleta(false) else paletaSobriaGuardadaClara ?: PaletaSobriaDefaults.CLARA

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
                esPersonalizado = esPersonalizadoActivoOscuro
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
                esPersonalizado = esPersonalizadoActivoClaro
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
            Toast.makeText(contexto, "¡Tema guardado con éxito!", Toast.LENGTH_SHORT).show()
        }

        fun solicitarGuardar() {
            if (!modificadoOscuro && !modificadoClaro) {
                Toast.makeText(contexto, "No hay cambios pendientes por guardar", Toast.LENGTH_SHORT).show()
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
                lumBorde = lumBorde,
                tonoEfectivoBorde = tonoEfectivoBorde,
                lumTextoPrincipal = lumTextoPrincipal,
                tonoEfectivoTextoPrincipal = tonoEfectivoTextoPrincipal,
                lumTextoSecundario = lumTextoSecundario,
                tonoEfectivoTextoSecundario = tonoEfectivoTextoSecundario
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

            // Selector de Modo (Oscuro / Claro) FIJO / VISIBLE ARRIBA
            SelectorModoColorLab(
                modoOscuro = modoOscuro,
                modificadoOscuro = modificadoOscuro,
                modificadoClaro = modificadoClaro,
                colorAcentoActual = colorAcentoActual,
                alSeleccionarModo = { modoOscuro = it },
                haptica = haptica
            )

            // Tarjeta de Vista Previa en Vivo (Sticky Top)
            TarjetaFlotantePreviaLaboratorio(
                colorFondo = colorFondo,
                colorTarjeta = colorTarjeta,
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

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // SECCIÓN 1: Escala de Capas y Tonos (90% de la interfaz)
                    SeccionEscalaCapasLab(
                        estadoLab = estadoLab,
                        colorFondo = colorFondo,
                        colorTarjeta = colorTarjeta,
                        colorCampo = colorCampo,
                        colorBorde = colorBorde,
                        colorTextoPrincipal = colorTextoPrincipal,
                        colorTextoSecundario = colorTextoSecundario,
                        colorAcentoActual = colorAcentoActual,
                        haptica = haptica,
                        marcarModificado = ::marcarModificado
                    )

                    Spacer(Modifier.height(18.dp))

                    // SECCIÓN 2: Acento Esencial (10% de color)
                    SeccionAcentoEsencialLab(
                        estadoLab = estadoLab,
                        colorCampo = colorCampo,
                        colorBorde = colorBorde,
                        colorTextoPrincipal = colorTextoPrincipal,
                        colorTextoSecundario = colorTextoSecundario,
                        acentosPredefinidos = acentosPredefinidos,
                        acentosGrisesNeutros = acentosGrisesNeutros,
                        haptica = haptica,
                        marcarModificado = ::marcarModificado
                    )

                    Spacer(Modifier.height(180.dp))
                }

                // Botones flotantes alineados verticalmente abajo a la derecha
                ColumnaAccionesFlotantesLab(
                    colorAcentoActual = colorAcentoActual,
                    alRestablecer = {
                        haptica.toque()
                        restablecerValores(contexto)
                        Toast.makeText(contexto, "Valores de fábrica restablecidos", Toast.LENGTH_SHORT).show()
                    },
                    alCopiarPaleta = ::copiarPaletaAlPortapapeles,
                    alGuardar = ::solicitarGuardar,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }

        // Diálogo de confirmación de guardado
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
