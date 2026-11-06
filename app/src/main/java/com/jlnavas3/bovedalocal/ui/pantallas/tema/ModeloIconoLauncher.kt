/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Modelo que describe cada variante de icono disponible para el launcher del sistema.
 */
data class VarianteIconoLauncher(
    val clave: String,
    val etiqueta: String,
    val colorFondoPrimario: Color,
    val colorFondoSecundario: Color,
    val colorCandado: Color
) {
    val pincelFondo: Brush
        get() = Brush.linearGradient(listOf(colorFondoPrimario, colorFondoSecundario))
}

object CatalogoIconosLauncher {
    private val fondoObsidianaPrimario = Color(0xFF1E2638)
    private val fondoObsidianaSecundario = Color(0xFF0D111A)

    val bovedaOscura: List<VarianteIconoLauncher> = listOf(
        VarianteIconoLauncher(
            clave = "gris",
            etiqueta = "Predeterminado (Oro)",
            colorFondoPrimario = fondoObsidianaPrimario,
            colorFondoSecundario = fondoObsidianaSecundario,
            colorCandado = Color(0xFFFFB300)
        ),
        VarianteIconoLauncher(
            clave = "cian",
            etiqueta = "Cian Neón",
            colorFondoPrimario = fondoObsidianaPrimario,
            colorFondoSecundario = fondoObsidianaSecundario,
            colorCandado = Color(0xFF00E5FF)
        ),
        VarianteIconoLauncher(
            clave = "menta",
            etiqueta = "Verde Matrix",
            colorFondoPrimario = fondoObsidianaPrimario,
            colorFondoSecundario = fondoObsidianaSecundario,
            colorCandado = Color(0xFF00E676)
        ),
        VarianteIconoLauncher(
            clave = "azul",
            etiqueta = "Azul Eléctrico",
            colorFondoPrimario = fondoObsidianaPrimario,
            colorFondoSecundario = fondoObsidianaSecundario,
            colorCandado = Color(0xFF448AFF)
        ),
        VarianteIconoLauncher(
            clave = "purpura",
            etiqueta = "Amatista",
            colorFondoPrimario = fondoObsidianaPrimario,
            colorFondoSecundario = fondoObsidianaSecundario,
            colorCandado = Color(0xFFE040FB)
        ),
        VarianteIconoLauncher(
            clave = "rojo",
            etiqueta = "Rubí Carmesí",
            colorFondoPrimario = fondoObsidianaPrimario,
            colorFondoSecundario = fondoObsidianaSecundario,
            colorCandado = Color(0xFFFF5252)
        ),
        VarianteIconoLauncher(
            clave = "rosa",
            etiqueta = "Rosa Cyber",
            colorFondoPrimario = fondoObsidianaPrimario,
            colorFondoSecundario = fondoObsidianaSecundario,
            colorCandado = Color(0xFFFF4081)
        ),
        VarianteIconoLauncher(
            clave = "gris_azulado",
            etiqueta = "Titanio Plata",
            colorFondoPrimario = fondoObsidianaPrimario,
            colorFondoSecundario = fondoObsidianaSecundario,
            colorCandado = Color(0xFFECEFF1)
        )
    )

    val fondosVibrantes: List<VarianteIconoLauncher> = listOf(
        VarianteIconoLauncher(
            clave = "indigo",
            etiqueta = "Índigo",
            colorFondoPrimario = Color(0xFF3F51B5),
            colorFondoSecundario = Color(0xFF303F9F),
            colorCandado = Color(0xFFFFD54F)
        ),
        VarianteIconoLauncher(
            clave = "violeta",
            etiqueta = "Violeta",
            colorFondoPrimario = Color(0xFFB98BFF),
            colorFondoSecundario = Color(0xFF7C3AED),
            colorCandado = Color(0xFFFFE082)
        ),
        VarianteIconoLauncher(
            clave = "purpura_oscuro",
            etiqueta = "Púrpura Oscuro",
            colorFondoPrimario = Color(0xFF673AB7),
            colorFondoSecundario = Color(0xFF512DA8),
            colorCandado = Color(0xFFFFE082)
        ),
        VarianteIconoLauncher(
            clave = "ambar",
            etiqueta = "Ámbar Cálido",
            colorFondoPrimario = Color(0xFFFFC107),
            colorFondoSecundario = Color(0xFFFFA000),
            colorCandado = Color(0xFFFFFFFF)
        ),
        VarianteIconoLauncher(
            clave = "naranja",
            etiqueta = "Naranja",
            colorFondoPrimario = Color(0xFFFF9800),
            colorFondoSecundario = Color(0xFFF57C00),
            colorCandado = Color(0xFFFFFFFF)
        ),
        VarianteIconoLauncher(
            clave = "naranja_oscuro",
            etiqueta = "Naranja Fuego",
            colorFondoPrimario = Color(0xFFFF5722),
            colorFondoSecundario = Color(0xFFE64A19),
            colorCandado = Color(0xFFFFFFFF)
        ),
        VarianteIconoLauncher(
            clave = "amarillo",
            etiqueta = "Amarillo Sol",
            colorFondoPrimario = Color(0xFFFFEB3B),
            colorFondoSecundario = Color(0xFFFBC02D),
            colorCandado = Color(0xFF121622)
        ),
        VarianteIconoLauncher(
            clave = "lima",
            etiqueta = "Lima",
            colorFondoPrimario = Color(0xFFCDDC39),
            colorFondoSecundario = Color(0xFFAFB42B),
            colorCandado = Color(0xFF121622)
        ),
        VarianteIconoLauncher(
            clave = "verde_claro",
            etiqueta = "Verde Claro",
            colorFondoPrimario = Color(0xFF8BC34A),
            colorFondoSecundario = Color(0xFF689F38),
            colorCandado = Color(0xFF121622)
        ),
        VarianteIconoLauncher(
            clave = "verde",
            etiqueta = "Verde Bosque",
            colorFondoPrimario = Color(0xFF4CAF50),
            colorFondoSecundario = Color(0xFF2E7D32),
            colorCandado = Color(0xFFFFFFFF)
        ),
        VarianteIconoLauncher(
            clave = "celeste",
            etiqueta = "Celeste",
            colorFondoPrimario = Color(0xFF03A9F4),
            colorFondoSecundario = Color(0xFF0288D1),
            colorCandado = Color(0xFFFFFFFF)
        ),
        VarianteIconoLauncher(
            clave = "marron",
            etiqueta = "Cobre / Café",
            colorFondoPrimario = Color(0xFF8D6E63),
            colorFondoSecundario = Color(0xFF5D4037),
            colorCandado = Color(0xFFFFE082)
        )
    )

    val todas: List<VarianteIconoLauncher> = bovedaOscura + fondosVibrantes

    fun buscarPorClave(clave: String): VarianteIconoLauncher {
        return todas.firstOrNull { it.clave == clave } ?: bovedaOscura.first()
    }
}
