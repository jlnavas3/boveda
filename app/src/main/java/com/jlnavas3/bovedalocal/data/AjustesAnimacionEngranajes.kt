/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable

/**
 * Agrupa la configuración visual y física del mecanismo de engranajes relojeros de desbloqueo.
 * Encapsulado para mantener los slots del constructor de [AjustesApp] dentro de los límites del JVM.
 */
@Immutable
data class AjustesAnimacionEngranajes(
    val velocidad: Float = AjustesDefaults.Animacion.Engranajes.VELOCIDAD,
    val grosorBorde: Float = AjustesDefaults.Animacion.Engranajes.GROSOR_BORDE,
    val alturaDientes: Float = AjustesDefaults.Animacion.Engranajes.ALTURA_DIENTES,
    val anchoDientes: Float = AjustesDefaults.Animacion.Engranajes.ANCHO_DIENTES,
    val grosorRadios: Float = AjustesDefaults.Animacion.Engranajes.GROSOR_RADIOS,
    val curvaturaRadios: Float = AjustesDefaults.Animacion.Engranajes.CURVATURA_RADIOS,
    val cantidadRadios: Int = AjustesDefaults.Animacion.Engranajes.CANTIDAD_RADIOS,
    val radioInterior: Float = AjustesDefaults.Animacion.Engranajes.RADIO_INTERIOR,
    val tamanoEje: Float = AjustesDefaults.Animacion.Engranajes.TAMANO_EJE,
    val sombraIntensidad: Float = AjustesDefaults.Animacion.Engranajes.SOMBRA_INTENSIDAD,
    val colorBrillo: String = AjustesDefaults.Animacion.Engranajes.COLOR_BRILLO,
    val colorPrincipal: String = AjustesDefaults.Animacion.Engranajes.COLOR_PRINCIPAL,
    val colorSombraMedio: String = AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_MEDIO,
    val colorSombraOscuro: String = AjustesDefaults.Animacion.Engranajes.COLOR_SOMBRA_OSCURO,
    val colorBisel: String = AjustesDefaults.Animacion.Engranajes.COLOR_BISEL,
    val colorInterior: String = AjustesDefaults.Animacion.Engranajes.COLOR_INTERIOR,
    val colorCubo: String = AjustesDefaults.Animacion.Engranajes.COLOR_CUBO,
    val colorEje: String = AjustesDefaults.Animacion.Engranajes.COLOR_EJE,
    val coloresClaroJson: String = "",
    val coloresOscuroJson: String = ""
)
