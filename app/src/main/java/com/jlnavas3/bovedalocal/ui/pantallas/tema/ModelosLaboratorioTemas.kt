package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobria
import com.jlnavas3.bovedalocal.ui.theme.ParametrosLaboratorio
import com.jlnavas3.bovedalocal.ui.theme.crearGris
import com.jlnavas3.bovedalocal.ui.theme.restringirLuminancia

val ACENTOS_PREDEFINIDOS_LAB: List<Pair<Color, String>> = listOf(
    Color(0xFFE5A93C) to "Ámbar Bóveda",
    Color(0xFFD4AF37) to "Oro Clásico",
    Color(0xFFC87D55) to "Bronce Cálido",
    Color(0xFF3B82F6) to "Zafiro Seguridad",
    Color(0xFF10B981) to "Esmeralda",
    Color(0xFF8B5CF6) to "Amatista"
)

fun obtenerAcentosGrisesNeutros(modoOscuro: Boolean): List<Pair<Color, String>> {
    return if (modoOscuro) {
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

fun calcularColorCapaLab(
    tono: Float,
    lum: Float,
    saturacionTinte: Float,
    modoOscuro: Boolean,
    esSuperficie: Boolean
): Color {
    val lumRestringida = restringirLuminancia(lum, modoOscuro, esSuperficie)
    return if (saturacionTinte <= 0.001f) {
        crearGris(lumRestringida)
    } else {
        Color.hsv(tono, saturacionTinte.coerceIn(0f, 1f), lumRestringida)
    }
}

fun construirPaletaDesdeEstado(
    estado: EstadoLaboratorioTemas,
    esOscuro: Boolean
): PaletaSobria {
    val sat = if (esOscuro) estado.saturacionTinteOscuro else estado.saturacionTinteClaro
    val tonoG = if (esOscuro) estado.tonoGlobalOscuro else estado.tonoGlobalClaro
    val effFondo = if (estado.unificarTonos) tonoG else (if (esOscuro) estado.tonoFondoOscuro else estado.tonoFondoClaro)
    val effTarjeta = if (estado.unificarTonos) tonoG else (if (esOscuro) estado.tonoTarjetaOscuro else estado.tonoTarjetaClaro)
    val effCampo = if (estado.unificarTonos) tonoG else (if (esOscuro) estado.tonoCampoOscuro else estado.tonoCampoClaro)
    val effBorde = if (estado.unificarTonos) tonoG else (if (esOscuro) estado.tonoBordeOscuro else estado.tonoBordeClaro)
    val effTextoP = if (estado.unificarTonos) tonoG else (if (esOscuro) estado.tonoTextoPrincipalOscuro else estado.tonoTextoPrincipalClaro)
    val effTextoS = if (estado.unificarTonos) tonoG else (if (esOscuro) estado.tonoTextoSecundarioOscuro else estado.tonoTextoSecundarioClaro)

    val lumF = if (esOscuro) estado.lumFondoOscuro else estado.lumFondoClaro
    val lumT = if (esOscuro) estado.lumTarjetaOscuro else estado.lumTarjetaClaro
    val lumC = if (esOscuro) estado.lumCampoOscuro else estado.lumCampoClaro
    val lumB = if (esOscuro) estado.lumBordeOscuro else estado.lumBordeClaro
    val lumTP = if (esOscuro) estado.lumTextoPrincipalOscuro else estado.lumTextoPrincipalClaro
    val lumTS = if (esOscuro) estado.lumTextoSecundarioOscuro else estado.lumTextoSecundarioClaro

    return PaletaSobria(
        esOscuro = esOscuro,
        fondo = calcularColorCapaLab(effFondo, lumF, sat, esOscuro, true),
        tarjeta = calcularColorCapaLab(effTarjeta, lumT, sat, esOscuro, true),
        campo = calcularColorCapaLab(effCampo, lumC, sat, esOscuro, true),
        borde = calcularColorCapaLab(effBorde, lumB, sat, esOscuro, true),
        textoPrincipal = calcularColorCapaLab(effTextoP, lumTP, sat, esOscuro, false),
        textoSecundario = calcularColorCapaLab(effTextoS, lumTS, sat, esOscuro, false),
        acento = if (esOscuro) estado.colorAcentoOscuro else estado.colorAcentoClaro
    )
}
