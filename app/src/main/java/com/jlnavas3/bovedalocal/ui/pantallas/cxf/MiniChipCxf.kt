package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

@Composable
fun MiniChipCxf(texto: String, color: Color) {
    val colorLegible = colorLegibleParaTema(color)
    val fondo = fondoBadgeParaTema(color)
    Text(
        text = texto,
        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold),
        color = colorLegible,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(fondo)
            .padding(horizontal = 5.dp, vertical = 2.dp)
    )
}
