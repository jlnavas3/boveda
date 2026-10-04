package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.colorContraste

@Composable
fun SwatchColor(color: Color, seleccionado: Boolean, descripcion: String, alPulsar: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(color)
            .border(width = if (seleccionado) 3.dp else 0.dp, color = TextoPrincipal, shape = CircleShape)
            .clickable { alPulsar() },
        contentAlignment = Alignment.Center
    ) {
        if (seleccionado) {
            Icon(Icons.Filled.Check, contentDescription = descripcion, tint = colorContraste(color))
        }
    }
}
