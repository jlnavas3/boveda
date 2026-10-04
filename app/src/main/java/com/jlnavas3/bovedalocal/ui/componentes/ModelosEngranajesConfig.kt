package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Parámetros de configuración visual y física para el tren de engranajes mecánicos de la bóveda.
 */
data class EngranajesConfig(
    val velocidad: Float = 24f,
    val grosorBorde: Float = 0.7f,
    val alturaDientes: Float = 0.76f,
    val anchoDientes: Float = 1.00f,
    val grosorRadios: Float = 1.40f,
    val curvaturaRadios: Float = 1.00f,
    val cantidadRadios: Int = 6,
    val radioInterior: Float = 0.80f,
    val tamanoEje: Float = 1.23f,
    val sombraIntensidad: Float = 0.95f,
    val colorBrillo: Color = Color(0xFFABA799),
    val colorPrincipal: Color = Color(0xFF918D7E),
    val colorSombraMedio: Color = Color(0xFF635C57),
    val colorSombraOscuro: Color = Color(0xFF404038),
    val colorBisel: Color = Color(0xFFA5A19D),
    val colorInterior: Color = Color.Transparent,
    val colorCubo: Color = Color(0xFFD3D1C8),
    val colorEje: Color = Color(0xFF141316)
)

/**
 * Convierte los ajustes persistidos de la aplicación al modelo [EngranajesConfig].
 */
fun AjustesApp.aEngranajesConfig(): EngranajesConfig = EngranajesConfig(
    velocidad = engranajesVelocidad,
    grosorBorde = engranajesGrosorBorde,
    alturaDientes = engranajesAlturaDientes,
    anchoDientes = engranajesAnchoDientes,
    grosorRadios = engranajesGrosorRadios,
    curvaturaRadios = engranajesCurvaturaRadios,
    cantidadRadios = engranajesCantidadRadios,
    radioInterior = engranajesRadioInterior,
    tamanoEje = engranajesTamanoEje,
    sombraIntensidad = engranajesSombraIntensidad,
    colorBrillo = parsearColorO(engranajesColorBrillo, Color(0xFFABA799)),
    colorPrincipal = parsearColorO(engranajesColorPrincipal, Color(0xFF918D7E)),
    colorSombraMedio = parsearColorO(engranajesColorSombraMedio, Color(0xFF635C57)),
    colorSombraOscuro = parsearColorO(engranajesColorSombraOscuro, Color(0xFF404038)),
    colorBisel = parsearColorO(engranajesColorBisel, Color(0xFFA5A19D)),
    colorInterior = parsearColorO(engranajesColorInterior, Color.Transparent),
    colorCubo = parsearColorO(engranajesColorCubo, Color(0xFFD3D1C8)),
    colorEje = parsearColorO(engranajesColorEje, Color(0xFF141316))
)
