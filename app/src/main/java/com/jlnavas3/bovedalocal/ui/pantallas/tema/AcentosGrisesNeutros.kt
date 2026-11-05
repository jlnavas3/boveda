/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.ui.graphics.Color

fun obtenerAcentosGrisesNeutros(modoOscuro: Boolean): List<Pair<Color, String>> {
    return if (modoOscuro) {
        listOf(
            // Fila 1: Fríos y plateados (incluye default oscuro #959AA7)
            Color(0xFFE1E1E6) to "Titanio Platino",
            Color(0xFFB0B0B8) to "Gris Espacial",
            Color(0xFF959AA7) to "Pizarra Fría", // Default Oscuro exacto
            Color(0xFFA2A8B6) to "Plata Polar",
            Color(0xFFC8C8CE) to "Plata Niebla",
            // Fila 2: Cálidos y carbón
            Color(0xFFB8B2AA) to "Piedra Cálida",
            Color(0xFFA8A29E) to "Humo Urbano",
            Color(0xFF8E8883) to "Ceniza Suave",
            Color(0xFF78716C) to "Basalto Neutro",
            Color(0xFF57534E) to "Obsidiana Carbón"
        )
    } else {
        listOf(
            // Fila 1: Fríos y pizarra (incluye default claro #323842)
            Color(0xFF323842) to "Pizarra Fría", // Default Claro exacto
            Color(0xFF2C2C2E) to "Titanio Carbón",
            Color(0xFF3A3A3C) to "Gris Espacial",
            Color(0xFF242A35) to "Azul Acero",
            Color(0xFF48484A) to "Plata Grafito",
            // Fila 2: Cálidos y piedra
            Color(0xFF3E3A36) to "Piedra Cálida",
            Color(0xFF44403C) to "Arcilla Neutra",
            Color(0xFF524A42) to "Tierra Sombra",
            Color(0xFF57534E) to "Humo Profundo",
            Color(0xFF292524) to "Café Obsidiana"
        )
    }
}
