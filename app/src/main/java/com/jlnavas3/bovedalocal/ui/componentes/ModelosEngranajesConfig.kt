/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.obtenerEngranajesColor
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobriaDefaults
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaClara
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaGuardadaOscura
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
    val sombraIntensidad: Float = 0.52f,
    val colorBrillo: Color = Color(0xFF43433F),
    val colorPrincipal: Color = Color(0xFF37393A),
    val colorSombraMedio: Color = Color(0xFF42423D),
    val colorSombraOscuro: Color = Color(0xFF2D2D2B),
    val colorBisel: Color = Color(0xFF545450),
    val colorInterior: Color = Color.Transparent,
    val colorCubo: Color = Color(0xFFD3D1C8),
    val colorEje: Color = Color(0xFF363630),
    val esOscuro: Boolean = true,
    val colorFondo: Color = Color(0xFF09090A)
)

/**
 * Convierte los ajustes persistidos de la aplicación al modelo [EngranajesConfig],
 * resolviendo la paleta de colores según si el tema activo o solicitado es oscuro o claro.
 */
fun AjustesApp.aEngranajesConfig(
    esOscuro: Boolean = esOscuroActivo,
    colorFondo: Color? = null
): EngranajesConfig {
    val paleta = if (esOscuro) (paletaSobriaGuardadaOscura ?: PaletaSobriaDefaults.OSCURA) else (paletaSobriaGuardadaClara ?: PaletaSobriaDefaults.CLARA)
    val fondoEfectivo = colorFondo ?: paleta.fondo
    return EngranajesConfig(
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
        colorBrillo = parsearColorO(obtenerEngranajesColor("brillo", esOscuro), if (esOscuro) Color(0xFF43433F) else Color(0xFFF3F2EB)),
        colorPrincipal = parsearColorO(obtenerEngranajesColor("principal", esOscuro), if (esOscuro) Color(0xFF37393A) else Color(0xFFF2F1E6)),
        colorSombraMedio = parsearColorO(obtenerEngranajesColor("sombra_medio", esOscuro), if (esOscuro) Color(0xFF42423D) else Color(0xFFF1F1EA)),
        colorSombraOscuro = parsearColorO(obtenerEngranajesColor("sombra_oscuro", esOscuro), if (esOscuro) Color(0xFF2D2D2B) else Color(0xFFEDEDE1)),
        colorBisel = parsearColorO(obtenerEngranajesColor("bisel", esOscuro), if (esOscuro) Color(0xFF545450) else Color(0xFFCAC9C0)),
        colorInterior = parsearColorO(obtenerEngranajesColor("interior", esOscuro), Color.Transparent),
        colorCubo = parsearColorO(obtenerEngranajesColor("cubo", esOscuro), if (esOscuro) Color(0xFFD3D1C8) else Color(0xFFFFFFF1)),
        colorEje = parsearColorO(obtenerEngranajesColor("eje", esOscuro), if (esOscuro) Color(0xFF363630) else Color(0xFFFFFDF2)),
        esOscuro = esOscuro,
        colorFondo = fondoEfectivo
    )
}
