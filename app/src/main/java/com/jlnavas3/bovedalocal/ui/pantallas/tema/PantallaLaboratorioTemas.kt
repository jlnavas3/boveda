package com.jlnavas3.bovedalocal.ui.pantallas.tema

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.theme.GestorPaletaSobria
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaClara
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaOscura
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.BotonPeligro
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.componentes.colorAhsv
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.SliderBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.FilaAjusteMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaAjuste
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.preview.PreviewMocks
import com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobria
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobriaDefaults
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobriaParametrosDefaults
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.ui.theme.aHexConAlfa
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.ui.theme.crearGris
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaEnVivo
import com.jlnavas3.bovedalocal.ui.theme.restringirLuminancia
import com.jlnavas3.bovedalocal.util.Haptica

private class EstadoLaboratorioTemas {
    var modoOscuro by mutableStateOf(esOscuroActivo)

    val paramOscuro = PaletaSobriaParametrosDefaults.OSCURO
    val defOscuro = PaletaSobriaDefaults.OSCURA
    val paramClaro = PaletaSobriaParametrosDefaults.CLARO
    val defClaro = PaletaSobriaDefaults.CLARA

    var lumFondoOscuro by mutableFloatStateOf(paramOscuro.lumFondo)
    var lumTarjetaOscuro by mutableFloatStateOf(paramOscuro.lumTarjeta)
    var lumCampoOscuro by mutableFloatStateOf(paramOscuro.lumCampo)
    var lumBordeOscuro by mutableFloatStateOf(paramOscuro.lumBorde)
    var lumTextoPrincipalOscuro by mutableFloatStateOf(paramOscuro.lumTextoPrincipal)
    var lumTextoSecundarioOscuro by mutableFloatStateOf(paramOscuro.lumTextoSecundario)
    var colorAcentoOscuro by mutableStateOf(defOscuro.acento)
    var tonoGlobalOscuro by mutableFloatStateOf(paramOscuro.tonoGlobal)
    var saturacionTinteOscuro by mutableFloatStateOf(paramOscuro.saturacionTinte)
    var tonoFondoOscuro by mutableFloatStateOf(paramOscuro.tonoGlobal)
    var tonoTarjetaOscuro by mutableFloatStateOf(paramOscuro.tonoGlobal)
    var tonoCampoOscuro by mutableFloatStateOf(paramOscuro.tonoGlobal)
    var tonoBordeOscuro by mutableFloatStateOf(paramOscuro.tonoGlobal)
    var tonoTextoPrincipalOscuro by mutableFloatStateOf(paramOscuro.tonoGlobal)
    var tonoTextoSecundarioOscuro by mutableFloatStateOf(paramOscuro.tonoGlobal)
    var huePersonalizadoOscuro by mutableFloatStateOf(paramOscuro.acentoHue)
    var satPersonalizadoOscuro by mutableFloatStateOf(paramOscuro.acentoSat)
    var valPersonalizadoOscuro by mutableFloatStateOf(paramOscuro.acentoVal)
    var alfaPersonalizadoOscuro by mutableFloatStateOf(paramOscuro.acentoAlfa)
    var esPersonalizadoActivoOscuro by mutableStateOf(false)
    var mostrarAjustePersonalizadoOscuro by mutableStateOf(false)

    var lumFondoClaro by mutableFloatStateOf(paramClaro.lumFondo)
    var lumTarjetaClaro by mutableFloatStateOf(paramClaro.lumTarjeta)
    var lumCampoClaro by mutableFloatStateOf(paramClaro.lumCampo)
    var lumBordeClaro by mutableFloatStateOf(paramClaro.lumBorde)
    var lumTextoPrincipalClaro by mutableFloatStateOf(paramClaro.lumTextoPrincipal)
    var lumTextoSecundarioClaro by mutableFloatStateOf(paramClaro.lumTextoSecundario)
    var colorAcentoClaro by mutableStateOf(defClaro.acento)
    var tonoGlobalClaro by mutableFloatStateOf(paramClaro.tonoGlobal)
    var saturacionTinteClaro by mutableFloatStateOf(paramClaro.saturacionTinte)
    var tonoFondoClaro by mutableFloatStateOf(paramClaro.tonoGlobal)
    var tonoTarjetaClaro by mutableFloatStateOf(paramClaro.tonoGlobal)
    var tonoCampoClaro by mutableFloatStateOf(paramClaro.tonoGlobal)
    var tonoBordeClaro by mutableFloatStateOf(paramClaro.tonoGlobal)
    var tonoTextoPrincipalClaro by mutableFloatStateOf(paramClaro.tonoGlobal)
    var tonoTextoSecundarioClaro by mutableFloatStateOf(paramClaro.tonoGlobal)
    var huePersonalizadoClaro by mutableFloatStateOf(paramClaro.acentoHue)
    var satPersonalizadoClaro by mutableFloatStateOf(paramClaro.acentoSat)
    var valPersonalizadoClaro by mutableFloatStateOf(paramClaro.acentoVal)
    var alfaPersonalizadoClaro by mutableFloatStateOf(paramClaro.acentoAlfa)
    var esPersonalizadoActivoClaro by mutableStateOf(false)
    var mostrarAjustePersonalizadoClaro by mutableStateOf(false)

    var unificarTonos by mutableStateOf(true)
    var modificadoOscuro by mutableStateOf(false)
    var modificadoClaro by mutableStateOf(false)

    var lumFondo: Float
        get() = if (modoOscuro) lumFondoOscuro else lumFondoClaro
        set(v) { if (modoOscuro) lumFondoOscuro = v else lumFondoClaro = v }

    var lumTarjeta: Float
        get() = if (modoOscuro) lumTarjetaOscuro else lumTarjetaClaro
        set(v) { if (modoOscuro) lumTarjetaOscuro = v else lumTarjetaClaro = v }

    var lumCampo: Float
        get() = if (modoOscuro) lumCampoOscuro else lumCampoClaro
        set(v) { if (modoOscuro) lumCampoOscuro = v else lumCampoClaro = v }

    var lumBorde: Float
        get() = if (modoOscuro) lumBordeOscuro else lumBordeClaro
        set(v) { if (modoOscuro) lumBordeOscuro = v else lumBordeClaro = v }

    var lumTextoPrincipal: Float
        get() = if (modoOscuro) lumTextoPrincipalOscuro else lumTextoPrincipalClaro
        set(v) { if (modoOscuro) lumTextoPrincipalOscuro = v else lumTextoPrincipalClaro = v }

    var lumTextoSecundario: Float
        get() = if (modoOscuro) lumTextoSecundarioOscuro else lumTextoSecundarioClaro
        set(v) { if (modoOscuro) lumTextoSecundarioOscuro = v else lumTextoSecundarioClaro = v }

    var colorAcentoActual: Color
        get() = if (modoOscuro) colorAcentoOscuro else colorAcentoClaro
        set(v) { if (modoOscuro) colorAcentoOscuro = v else colorAcentoClaro = v }

    var tonoGlobal: Float
        get() = if (modoOscuro) tonoGlobalOscuro else tonoGlobalClaro
        set(v) { if (modoOscuro) tonoGlobalOscuro = v else tonoGlobalClaro = v }

    var saturacionTinte: Float
        get() = if (modoOscuro) saturacionTinteOscuro else saturacionTinteClaro
        set(v) { if (modoOscuro) saturacionTinteOscuro = v else saturacionTinteClaro = v }

    var tonoFondo: Float
        get() = if (modoOscuro) tonoFondoOscuro else tonoFondoClaro
        set(v) { if (modoOscuro) tonoFondoOscuro = v else tonoFondoClaro = v }

    var tonoTarjeta: Float
        get() = if (modoOscuro) tonoTarjetaOscuro else tonoTarjetaClaro
        set(v) { if (modoOscuro) tonoTarjetaOscuro = v else tonoTarjetaClaro = v }

    var tonoCampo: Float
        get() = if (modoOscuro) tonoCampoOscuro else tonoCampoClaro
        set(v) { if (modoOscuro) tonoCampoOscuro = v else tonoCampoClaro = v }

    var tonoBorde: Float
        get() = if (modoOscuro) tonoBordeOscuro else tonoBordeClaro
        set(v) { if (modoOscuro) tonoBordeOscuro = v else tonoBordeClaro = v }

    var tonoTextoPrincipal: Float
        get() = if (modoOscuro) tonoTextoPrincipalOscuro else tonoTextoPrincipalClaro
        set(v) { if (modoOscuro) tonoTextoPrincipalOscuro = v else tonoTextoPrincipalClaro = v }

    var tonoTextoSecundario: Float
        get() = if (modoOscuro) tonoTextoSecundarioOscuro else tonoTextoSecundarioClaro
        set(v) { if (modoOscuro) tonoTextoSecundarioOscuro = v else tonoTextoSecundarioClaro = v }

    var huePersonalizado: Float
        get() = if (modoOscuro) huePersonalizadoOscuro else huePersonalizadoClaro
        set(v) { if (modoOscuro) huePersonalizadoOscuro = v else huePersonalizadoClaro = v }

    var satPersonalizado: Float
        get() = if (modoOscuro) satPersonalizadoOscuro else satPersonalizadoClaro
        set(v) { if (modoOscuro) satPersonalizadoOscuro = v else satPersonalizadoClaro = v }

    var valPersonalizado: Float
        get() = if (modoOscuro) valPersonalizadoOscuro else valPersonalizadoClaro
        set(v) { if (modoOscuro) valPersonalizadoOscuro = v else valPersonalizadoClaro = v }

    var alfaPersonalizado: Float
        get() = if (modoOscuro) alfaPersonalizadoOscuro else alfaPersonalizadoClaro
        set(v) { if (modoOscuro) alfaPersonalizadoOscuro = v else alfaPersonalizadoClaro = v }

    var esPersonalizadoActivo: Boolean
        get() = if (modoOscuro) esPersonalizadoActivoOscuro else esPersonalizadoActivoClaro
        set(v) { if (modoOscuro) esPersonalizadoActivoOscuro = v else esPersonalizadoActivoClaro = v }

    var mostrarAjustePersonalizado: Boolean
        get() = if (modoOscuro) mostrarAjustePersonalizadoOscuro else mostrarAjustePersonalizadoClaro
        set(v) { if (modoOscuro) mostrarAjustePersonalizadoOscuro = v else mostrarAjustePersonalizadoClaro = v }

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

    fun restablecerValores() {
        if (modoOscuro) {
            lumFondoOscuro = paramOscuro.lumFondo
            lumTarjetaOscuro = paramOscuro.lumTarjeta
            lumCampoOscuro = paramOscuro.lumCampo
            lumBordeOscuro = paramOscuro.lumBorde
            lumTextoPrincipalOscuro = paramOscuro.lumTextoPrincipal
            lumTextoSecundarioOscuro = paramOscuro.lumTextoSecundario
            tonoGlobalOscuro = paramOscuro.tonoGlobal
            saturacionTinteOscuro = paramOscuro.saturacionTinte
            tonoFondoOscuro = paramOscuro.tonoGlobal
            tonoTarjetaOscuro = paramOscuro.tonoGlobal
            tonoCampoOscuro = paramOscuro.tonoGlobal
            tonoBordeOscuro = paramOscuro.tonoGlobal
            tonoTextoPrincipalOscuro = paramOscuro.tonoGlobal
            tonoTextoSecundarioOscuro = paramOscuro.tonoGlobal
            colorAcentoOscuro = defOscuro.acento
            huePersonalizadoOscuro = paramOscuro.acentoHue
            satPersonalizadoOscuro = paramOscuro.acentoSat
            valPersonalizadoOscuro = paramOscuro.acentoVal
            alfaPersonalizadoOscuro = paramOscuro.acentoAlfa
            esPersonalizadoActivoOscuro = false
            mostrarAjustePersonalizadoOscuro = false
            modificadoOscuro = false
        } else {
            lumFondoClaro = paramClaro.lumFondo
            lumTarjetaClaro = paramClaro.lumTarjeta
            lumCampoClaro = paramClaro.lumCampo
            lumBordeClaro = paramClaro.lumBorde
            lumTextoPrincipalClaro = paramClaro.lumTextoPrincipal
            lumTextoSecundarioClaro = paramClaro.lumTextoSecundario
            tonoGlobalClaro = paramClaro.tonoGlobal
            saturacionTinteClaro = paramClaro.saturacionTinte
            tonoFondoClaro = paramClaro.tonoGlobal
            tonoTarjetaClaro = paramClaro.tonoGlobal
            tonoCampoClaro = paramClaro.tonoGlobal
            tonoBordeClaro = paramClaro.tonoGlobal
            tonoTextoPrincipalClaro = paramClaro.tonoGlobal
            tonoTextoSecundarioClaro = paramClaro.tonoGlobal
            colorAcentoClaro = defClaro.acento
            huePersonalizadoClaro = paramClaro.acentoHue
            satPersonalizadoClaro = paramClaro.acentoSat
            valPersonalizadoClaro = paramClaro.acentoVal
            alfaPersonalizadoClaro = paramClaro.acentoAlfa
            esPersonalizadoActivoClaro = false
            mostrarAjustePersonalizadoClaro = false
            modificadoClaro = false
        }
        unificarTonos = true
    }
}

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

    val estadoLab = remember { EstadoLaboratorioTemas() }

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

    val paletaActual = remember(
        modoOscuro, colorFondo, colorTarjeta, colorCampo,
        colorBorde, colorTextoPrincipal, colorTextoSecundario, colorAcentoActual
    ) {
        PaletaSobria(
            esOscuro = modoOscuro,
            fondo = colorFondo,
            tarjeta = colorTarjeta,
            campo = colorCampo,
            borde = colorBorde,
            textoPrincipal = colorTextoPrincipal,
            textoSecundario = colorTextoSecundario,
            acento = colorAcentoActual
        )
    }

    // Inyectar en vivo para que los componentes y la pantalla se actualicen en tiempo real
    LaunchedEffect(paletaActual) {
        paletaSobriaEnVivo = paletaActual
    }

    // Limpiar al salir de la pantalla si se desea o conservar
    DisposableEffect(Unit) {
        onDispose {
            // Se puede limpiar o mantener; mantener permite ver el resultado en toda la app mientras esté abierta
        }
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

    val acentosGrisesApple = remember(modoOscuro) {
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
        GestorPaletaSobria.guardar(contexto, paletaOsc, paletaCla)
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
        haptica.exito()
        val (hAcento, sAcento, vAcento) = colorAhsv(colorAcentoActual)
        val jsonDetallado = """
        {
          "modo": "${if (modoOscuro) "oscuro" else "claro"}",
          "fondo": "${colorFondo.aHexConAlfa()}",
          "tarjeta": "${colorTarjeta.aHexConAlfa()}",
          "campo": "${colorCampo.aHexConAlfa()}",
          "borde": "${colorBorde.aHexConAlfa()}",
          "textoPrincipal": "${colorTextoPrincipal.aHexConAlfa()}",
          "textoSecundario": "${colorTextoSecundario.aHexConAlfa()}",
          "acento": "${colorAcentoActual.aHexConAlfa()}",
          "parametrosLaboratorio": {
            "unificarTonos": $unificarTonos,
            "tonoGlobal": "${tonoGlobal.toInt()}°",
            "saturacionTinteGrises": "${(saturacionTinte * 100).toInt()}%",
            "capas": {
              "fondo": {
                "hex": "${colorFondo.aHexConAlfa()}",
                "luminancia": "${(lumFondo * 100).toInt()}%",
                "tono": "${tonoEfectivoFondo.toInt()}°"
              },
              "tarjeta": {
                "hex": "${colorTarjeta.aHexConAlfa()}",
                "luminancia": "${(lumTarjeta * 100).toInt()}%",
                "tono": "${tonoEfectivoTarjeta.toInt()}°"
              },
              "campo": {
                "hex": "${colorCampo.aHexConAlfa()}",
                "luminancia": "${(lumCampo * 100).toInt()}%",
                "tono": "${tonoEfectivoCampo.toInt()}°"
              },
              "borde": {
                "hex": "${colorBorde.aHexConAlfa()}",
                "luminancia": "${(lumBorde * 100).toInt()}%",
                "tono": "${tonoEfectivoBorde.toInt()}°"
              },
              "textoPrincipal": {
                "hex": "${colorTextoPrincipal.aHexConAlfa()}",
                "luminancia": "${(lumTextoPrincipal * 100).toInt()}%",
                "tono": "${tonoEfectivoTextoPrincipal.toInt()}°"
              },
              "textoSecundario": {
                "hex": "${colorTextoSecundario.aHexConAlfa()}",
                "luminancia": "${(lumTextoSecundario * 100).toInt()}%",
                "tono": "${tonoEfectivoTextoSecundario.toInt()}°"
              }
            },
            "acento": {
              "hex": "${colorAcentoActual.aHexConAlfa()}",
              "tono": "${hAcento.toInt()}°",
              "saturacion": "${(sAcento * 100).toInt()}%",
              "brillo": "${(vAcento * 100).toInt()}%",
              "opacidad": "${(colorAcentoActual.alpha * 100).toInt()}%",
              "canalAlfa": ${colorAcentoActual.alpha}
            }
          }
        }
        """.trimIndent()
        portapapeles.setText(AnnotatedString(jsonDetallado))
        Toast.makeText(contexto, "¡Paleta y parámetros copiados! Pégala en el chat.", Toast.LENGTH_LONG).show()
    }

    fun restablecerValores() {
        haptica.toque()
        if (modoOscuro) {
            lumFondoOscuro = paramOscuro.lumFondo
            lumTarjetaOscuro = paramOscuro.lumTarjeta
            lumCampoOscuro = paramOscuro.lumCampo
            lumBordeOscuro = paramOscuro.lumBorde
            lumTextoPrincipalOscuro = paramOscuro.lumTextoPrincipal
            lumTextoSecundarioOscuro = paramOscuro.lumTextoSecundario
            tonoGlobalOscuro = paramOscuro.tonoGlobal
            saturacionTinteOscuro = paramOscuro.saturacionTinte
            tonoFondoOscuro = paramOscuro.tonoGlobal
            tonoTarjetaOscuro = paramOscuro.tonoGlobal
            tonoCampoOscuro = paramOscuro.tonoGlobal
            tonoBordeOscuro = paramOscuro.tonoGlobal
            tonoTextoPrincipalOscuro = paramOscuro.tonoGlobal
            tonoTextoSecundarioOscuro = paramOscuro.tonoGlobal
            colorAcentoOscuro = defOscuro.acento
            huePersonalizadoOscuro = paramOscuro.acentoHue
            satPersonalizadoOscuro = paramOscuro.acentoSat
            valPersonalizadoOscuro = paramOscuro.acentoVal
            alfaPersonalizadoOscuro = paramOscuro.acentoAlfa
            esPersonalizadoActivoOscuro = false
            mostrarAjustePersonalizadoOscuro = false
            modificadoOscuro = false
        } else {
            lumFondoClaro = paramClaro.lumFondo
            lumTarjetaClaro = paramClaro.lumTarjeta
            lumCampoClaro = paramClaro.lumCampo
            lumBordeClaro = paramClaro.lumBorde
            lumTextoPrincipalClaro = paramClaro.lumTextoPrincipal
            lumTextoSecundarioClaro = paramClaro.lumTextoSecundario
            tonoGlobalClaro = paramClaro.tonoGlobal
            saturacionTinteClaro = paramClaro.saturacionTinte
            tonoFondoClaro = paramClaro.tonoGlobal
            tonoTarjetaClaro = paramClaro.tonoGlobal
            tonoCampoClaro = paramClaro.tonoGlobal
            tonoBordeClaro = paramClaro.tonoGlobal
            tonoTextoPrincipalClaro = paramClaro.tonoGlobal
            tonoTextoSecundarioClaro = paramClaro.tonoGlobal
            colorAcentoClaro = defClaro.acento
            huePersonalizadoClaro = paramClaro.acentoHue
            satPersonalizadoClaro = paramClaro.acentoSat
            valPersonalizadoClaro = paramClaro.acentoVal
            alfaPersonalizadoClaro = paramClaro.acentoAlfa
            esPersonalizadoActivoClaro = false
            mostrarAjustePersonalizadoClaro = false
            modificadoClaro = false
        }
        unificarTonos = true
        Toast.makeText(contexto, "Valores sobrios restablecidos", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Laboratorio de Temas",
            alVolver = alVolver,
            acciones = {
                IconButton(onClick = { solicitarGuardar() }) {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = "Guardar tema",
                        tint = colorAcentoActual
                    )
                }
                IconButton(onClick = { copiarPaletaAlPortapapeles() }) {
                    Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = "Copiar paleta",
                        tint = colorAcentoActual
                    )
                }
            }
        )

        var switchMuestraActivo by remember { mutableStateOf(true) }

        // VISTA PREVIA FLOTANTE SUPERIOR (STICKY TOP)
        TarjetaFlotantePreviaLaboratorio(
            colorFondo = colorFondo,
            colorTarjeta = colorTarjeta,
            colorBorde = colorBorde,
            colorCampo = colorCampo,
            colorTextoPrincipal = colorTextoPrincipal,
            colorTextoSecundario = colorTextoSecundario,
            colorAcento = colorAcentoActual,
            switchActivo = switchMuestraActivo,
            onAlternarSwitch = {
                haptica.tic()
                switchMuestraActivo = it
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Selector de Modo (Oscuro / Claro) con indicador (*) de cambios
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(CurvaturaEsquinas))
                    .background(ColorTarjetaAjustes)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Píldora Oscuro
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                        .background(if (modoOscuro) colorAcentoActual else Color.Transparent)
                        .clickable {
                            haptica.tic()
                            modoOscuro = true
                        }
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.DarkMode,
                        contentDescription = null,
                        tint = if (modoOscuro) colorContraste(colorAcentoActual) else colorTextoSecundario,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Modo Oscuro" + if (modificadoOscuro) " (*)" else "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (modoOscuro) FontWeight.Bold else FontWeight.Normal,
                            color = if (modoOscuro) colorContraste(colorAcentoActual) else colorTextoPrincipal
                        )
                    )
                }

                // Píldora Claro
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                        .background(if (!modoOscuro) colorAcentoActual else Color.Transparent)
                        .clickable {
                            haptica.tic()
                            modoOscuro = false
                        }
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.LightMode,
                        contentDescription = null,
                        tint = if (!modoOscuro) colorContraste(colorAcentoActual) else colorTextoSecundario,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Modo Claro" + if (modificadoClaro) " (*)" else "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (!modoOscuro) FontWeight.Bold else FontWeight.Normal,
                            color = if (!modoOscuro) colorContraste(colorAcentoActual) else colorTextoPrincipal
                        )
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // SECCIÓN 1: Escala de Capas y Tonos (90% de la interfaz)
            ComponenteGrupo(
                etiqueta = "Escala de Capas y Tonos (90% Interfaz)",
                icono = Icons.Filled.Palette
            ) {
                // Control Maestro de Tono y Switch de Unificación
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Aplicar el mismo tono a todos",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = colorTextoPrincipal
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = if (unificarTonos) "Todas las capas comparten el mismo tono" else "Tono independiente por cada capa",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorTextoSecundario
                            )
                        }
                        SwitchBoveda(
                            checked = unificarTonos,
                            colorActivo = colorAcentoActual,
                            colorInactivoTrack = colorCampo,
                            colorInactivoThumb = colorTextoSecundario,
                            onCheckedChange = {
                                haptica.tic()
                                unificarTonos = it
                            }
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    if (unificarTonos) {
                        // Slider de Tono Unificado
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color.hsv(tonoGlobal, 0.8f, 0.9f))
                                    .border(1.dp, colorBorde, CircleShape)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Tono cromático unificado: ${tonoGlobal.toInt()}°",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = colorTextoPrincipal,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        SliderBoveda(
                            value = tonoGlobal,
                            onValueChange = {
                                tonoGlobal = it
                                marcarModificado()
                                if (unificarTonos) {
                                    val (_, s, v) = colorAhsv(colorAcentoActual)
                                    colorAcentoActual = Color.hsv(it, if (s < 0.05f) 0.75f else s, v, colorAcentoActual.alpha)
                                    huePersonalizado = it
                                }
                            },
                            valueRange = 0f..360f
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    // Slider de Saturación / Intensidad de tinte en grises
                    Text(
                        text = "Intensidad de tinte en grises: ${(saturacionTinte * 100).toInt()}% ${if (saturacionTinte <= 0.001f) "(Gris puro)" else if (saturacionTinte <= 0.12f) "(Matiz Apple)" else ""}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = colorTextoSecundario
                    )
                    SliderBoveda(
                        value = saturacionTinte,
                        onValueChange = {
                            saturacionTinte = it
                            marcarModificado()
                        },
                        valueRange = 0f..0.35f
                    )
                }

                SeparadorFilaAjuste()

                // Capa 0: Fondo de Pantalla
                ControlCapaFila(
                    etiqueta = "Fondo general (Capa 0)",
                    colorActual = colorFondo,
                    luminancia = lumFondo,
                    alCambiarLuminancia = {
                        lumFondo = it
                        marcarModificado()
                    },
                    mostrarControlTono = !unificarTonos,
                    tono = tonoFondo,
                    alCambiarTono = {
                        tonoFondo = it
                        marcarModificado()
                    }
                )
                SeparadorFilaAjuste()

                // Capa 1: Tarjetas y Grupos
                ControlCapaFila(
                    etiqueta = "Tarjetas y grupos (Capa 1)",
                    colorActual = colorTarjeta,
                    luminancia = lumTarjeta,
                    alCambiarLuminancia = {
                        lumTarjeta = it
                        marcarModificado()
                    },
                    mostrarControlTono = !unificarTonos,
                    tono = tonoTarjeta,
                    alCambiarTono = {
                        tonoTarjeta = it
                        marcarModificado()
                    }
                )
                SeparadorFilaAjuste()

                // Capa 2: Campos de entrada y chips
                ControlCapaFila(
                    etiqueta = "Campos y chips (Capa 2)",
                    colorActual = colorCampo,
                    luminancia = lumCampo,
                    alCambiarLuminancia = {
                        lumCampo = it
                        marcarModificado()
                    },
                    mostrarControlTono = !unificarTonos,
                    tono = tonoCampo,
                    alCambiarTono = {
                        tonoCampo = it
                        marcarModificado()
                    }
                )
                SeparadorFilaAjuste()

                // Bordes y separadores
                ControlCapaFila(
                    etiqueta = "Bordes y líneas divisorias",
                    colorActual = colorBorde,
                    luminancia = lumBorde,
                    alCambiarLuminancia = {
                        lumBorde = it
                        marcarModificado()
                    },
                    mostrarControlTono = !unificarTonos,
                    tono = tonoBorde,
                    alCambiarTono = {
                        tonoBorde = it
                        marcarModificado()
                    }
                )
                SeparadorFilaAjuste()

                // Texto Principal
                ControlCapaFila(
                    etiqueta = "Texto principal (Títulos y datos)",
                    colorActual = colorTextoPrincipal,
                    luminancia = lumTextoPrincipal,
                    alCambiarLuminancia = {
                        lumTextoPrincipal = it
                        marcarModificado()
                    },
                    mostrarControlTono = !unificarTonos,
                    tono = tonoTextoPrincipal,
                    alCambiarTono = {
                        tonoTextoPrincipal = it
                        marcarModificado()
                    }
                )
                SeparadorFilaAjuste()

                // Texto Secundario
                ControlCapaFila(
                    etiqueta = "Texto secundario (Subtítulos)",
                    colorActual = colorTextoSecundario,
                    luminancia = lumTextoSecundario,
                    alCambiarLuminancia = {
                        lumTextoSecundario = it
                        marcarModificado()
                    },
                    mostrarControlTono = !unificarTonos,
                    tono = tonoTextoSecundario,
                    alCambiarTono = {
                        tonoTextoSecundario = it
                        marcarModificado()
                    }
                )
            }

            Spacer(Modifier.height(18.dp))

            // SECCIÓN 2: Acento Esencial (10% de color)
            ComponenteGrupo(
                etiqueta = "Acento Esencial (10% Color)",
                icono = Icons.Filled.Security
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Elige el acento protagonista para acciones primarias, botones y estados activos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorTextoSecundario
                    )

                    Spacer(Modifier.height(14.dp))

                    // Fila 1: Acentos Cromáticos
                    Text(
                        text = "Acentos Cromáticos",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = colorTextoSecundario
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        acentosPredefinidos.forEach { (color, nombre) ->
                            val seleccionado = !esPersonalizadoActivo && colorAcentoActual == color
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (seleccionado) 3.dp else 1.dp,
                                        color = if (seleccionado) colorTextoPrincipal else colorBorde,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        haptica.tic()
                                        colorAcentoActual = color
                                        esPersonalizadoActivo = false
                                        val (h, s, v) = colorAhsv(color)
                                        huePersonalizado = h
                                        satPersonalizado = s
                                        valPersonalizado = v
                                        alfaPersonalizado = color.alpha
                                        marcarModificado()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (seleccionado) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = nombre,
                                        tint = colorContraste(color),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Fila 2: Matices Neutros (Estilo Apple) y Personalizado
                    Text(
                        text = "Matices Neutros (Estilo Apple) y Personalizado",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = colorTextoSecundario
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        acentosGrisesApple.forEach { (color, nombre) ->
                            val seleccionado = !esPersonalizadoActivo && colorAcentoActual == color
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (seleccionado) 3.dp else 1.dp,
                                        color = if (seleccionado) colorTextoPrincipal else colorBorde,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        haptica.tic()
                                        colorAcentoActual = color
                                        esPersonalizadoActivo = false
                                        val (h, s, v) = colorAhsv(color)
                                        huePersonalizado = h
                                        satPersonalizado = s
                                        valPersonalizado = v
                                        alfaPersonalizado = color.alpha
                                        marcarModificado()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (seleccionado) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = nombre,
                                        tint = colorContraste(color),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        // Botón Personalizado
                        val seleccionadoPersonalizado = esPersonalizadoActivo
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    if (seleccionadoPersonalizado) colorAcentoActual else colorCampo
                                )
                                .border(
                                    width = if (seleccionadoPersonalizado) 3.dp else 1.dp,
                                    color = if (seleccionadoPersonalizado) colorTextoPrincipal else colorBorde,
                                    shape = CircleShape
                                )
                                .clickable {
                                    haptica.tic()
                                    esPersonalizadoActivo = true
                                    mostrarAjustePersonalizado = true
                                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                    marcarModificado()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Tune,
                                contentDescription = "Personalizado",
                                tint = if (seleccionadoPersonalizado) colorContraste(colorAcentoActual) else colorTextoPrincipal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Panel expandible para controles personalizados con sliders y transparencias
                    if (mostrarAjustePersonalizado || esPersonalizadoActivo) {
                        Spacer(Modifier.height(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                                .background(colorCampo)
                                .border(1.dp, colorBorde, RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(colorAcentoActual)
                                        .border(1.dp, colorBorde, CircleShape)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Acento Personalizado",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = colorTextoPrincipal,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = colorAcentoActual.aHexConAlfa(),
                                    style = EstiloMono.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
                                    color = colorTextoPrincipal
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            // Control 1: Tono / Color
                            Text(
                                text = "Tono: ${huePersonalizado.toInt()}°",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = colorTextoSecundario
                            )
                            SliderBoveda(
                                value = huePersonalizado,
                                onValueChange = {
                                    huePersonalizado = it
                                    if (unificarTonos) {
                                        tonoGlobal = it
                                    }
                                    esPersonalizadoActivo = true
                                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                    marcarModificado()
                                },
                                valueRange = 0f..360f
                            )

                            Spacer(Modifier.height(8.dp))

                            // Control 2: Saturación (0% es Gris puro)
                            Text(
                                text = "Saturación: ${(satPersonalizado * 100).toInt()}% ${if (satPersonalizado < 0.05f) "(Gris Puro)" else ""}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = colorTextoSecundario
                            )
                            SliderBoveda(
                                value = satPersonalizado,
                                onValueChange = {
                                    satPersonalizado = it
                                    esPersonalizadoActivo = true
                                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                    marcarModificado()
                                },
                                valueRange = 0f..1f
                            )

                            Spacer(Modifier.height(8.dp))

                            // Control 3: Brillo / Luminosidad
                            Text(
                                text = "Brillo: ${(valPersonalizado * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = colorTextoSecundario
                            )
                            SliderBoveda(
                                value = valPersonalizado,
                                onValueChange = {
                                    valPersonalizado = it
                                    esPersonalizadoActivo = true
                                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                    marcarModificado()
                                },
                                valueRange = 0.10f..1f
                            )

                            Spacer(Modifier.height(8.dp))

                            // Control 4: Transparencia / Opacidad (Alfa)
                            Text(
                                text = "Transparencia / Opacidad: ${(alfaPersonalizado * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (alfaPersonalizado < 1.0f) colorAcentoActual else colorTextoSecundario
                                )
                            )
                            SliderBoveda(
                                value = alfaPersonalizado,
                                onValueChange = {
                                    alfaPersonalizado = it
                                    esPersonalizadoActivo = true
                                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                    marcarModificado()
                                },
                                valueRange = 0.10f..1f
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // SECCIÓN 3: Acciones Finales
            BotonBoveda(
                texto = "💾 Guardar Tema",
                alPulsar = { solicitarGuardar() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            BotonBoveda(
                texto = "📋 Copiar Paleta para el Asistente",
                alPulsar = { copiarPaletaAlPortapapeles() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            BotonPeligro(
                texto = "🔄 Restablecer Valores Predeterminados",
                alPulsar = { restablecerValores() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))
        }
    }

    if (mostrarModalConfirmacionGuardado) {
        val modoModificadoTexto = if (modificadoOscuro) "Modo Oscuro" else "Modo Claro"
        val modoSinModificarTexto = if (modificadoOscuro) "Modo Claro" else "Modo Oscuro"
        DialogoConfirmacionBoveda(
            titulo = "Guardar Tema",
            mensaje = "Has modificado el $modoModificadoTexto, pero el $modoSinModificarTexto se mantendrá con su diseño actual.\n\n¿Deseas guardar los cambios?",
            textoConfirmar = "Guardar",
            alConfirmar = {
                mostrarModalConfirmacionGuardado = false
                ejecutarGuardado()
            },
            alDescartar = {
                mostrarModalConfirmacionGuardado = false
            },
            textoCancelar = "Seguir editando",
            iconoHeader = Icons.Filled.Save
        )
    }
    }
}

/**
 * Tarjeta de vista previa compacta que permanece flotante/fijada arriba (Sticky Top)
 * permitiendo ver en tiempo real la combinación de colores mientras se hace scroll en los controles.
 * Sin títulos innecesarios para maximizar el área visible.
 */
@Composable
private fun TarjetaFlotantePreviaLaboratorio(
    colorFondo: Color,
    colorTarjeta: Color,
    colorBorde: Color,
    colorCampo: Color,
    colorTextoPrincipal: Color,
    colorTextoSecundario: Color,
    colorAcento: Color,
    switchActivo: Boolean,
    onAlternarSwitch: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colorFondo)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CurvaturaEsquinas))
                .background(colorTarjeta)
                .border(1.dp, colorBorde, RoundedCornerShape(CurvaturaEsquinas))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Fila 1: Credencial con Icono, Textos y Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(colorAcento),
                    contentAlignment = Alignment.Center
                ) {
                    val colorContenidoAcento = if (colorAcento.alpha < 0.45f) {
                        colorTextoPrincipal
                    } else {
                        colorContraste(colorAcento)
                    }
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = colorContenidoAcento,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Google Workspace",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = colorTextoPrincipal
                        ),
                        maxLines = 1
                    )
                    Spacer(Modifier.height(1.dp))
                    Text(
                        text = "usuario@empresa.com • TOTP activo",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = colorTextoSecundario
                        ),
                        maxLines = 1
                    )
                }

                Spacer(Modifier.width(8.dp))

                SwitchBoveda(
                    checked = switchActivo,
                    colorActivo = colorAcento,
                    colorInactivoTrack = colorCampo,
                    colorInactivoThumb = colorTextoSecundario,
                    onCheckedChange = onAlternarSwitch
                )
            }

            Spacer(Modifier.height(8.dp))

            // Divisor sutil
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.6.dp)
                    .background(colorBorde)
            )

            Spacer(Modifier.height(8.dp))

            // Fila 2: Campo interactivo (Capa 2) y Botón Primario
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Campo de texto simulado con Capa 2
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorCampo)
                        .border(0.8.dp, colorBorde, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Security,
                        contentDescription = null,
                        tint = colorTextoSecundario,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Contraseña segura...",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = colorTextoSecundario
                        ),
                        maxLines = 1
                    )
                }

                // Botón Primario compacto
                val colorContenidoBoton = if (colorAcento.alpha < 0.45f) {
                    colorTextoPrincipal
                } else {
                    colorContraste(colorAcento)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorAcento)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Guardar",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = colorContenidoBoton
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ControlCapaFila(
    etiqueta: String,
    colorActual: Color,
    luminancia: Float,
    alCambiarLuminancia: (Float) -> Unit,
    mostrarControlTono: Boolean = false,
    tono: Float = 0f,
    alCambiarTono: ((Float) -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(colorActual)
                    .border(1.dp, Color.Gray.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = colorActual.aHexConAlfa(),
                style = EstiloMono.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
        }
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Luminancia / Brillo",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = Color.Gray
            )
            Text(
                text = "${(luminancia * 100).toInt()}%",
                style = EstiloMono.copy(fontSize = 11.sp),
                color = Color.Gray
            )
        }
        SliderBoveda(
            value = luminancia,
            onValueChange = alCambiarLuminancia,
            valueRange = 0.0f..1.0f
        )

        if (mostrarControlTono && alCambiarTono != null) {
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color.hsv(tono, 0.8f, 0.9f))
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Tono individual: ${tono.toInt()}°",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
            }
            SliderBoveda(
                value = tono,
                onValueChange = alCambiarTono,
                valueRange = 0.0f..360.0f
            )
        }
    }
}

@BovedaPantallaPreview
@Composable
private fun PreviewPantallaLaboratorioTemas() {
    PreviewTemaBoveda {
        PantallaLaboratorioTemas()
    }
}
