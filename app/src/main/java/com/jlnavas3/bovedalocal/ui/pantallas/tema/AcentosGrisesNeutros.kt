package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.ui.graphics.Color

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
