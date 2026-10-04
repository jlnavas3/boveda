package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.data.Entrada

data class CategoriaExportacion(
    val id: String,
    val etiqueta: String,
    val icono: ImageVector,
    val color: Color,
    val filtro: (Entrada) -> Boolean
)
