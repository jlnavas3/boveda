package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Insignia centralizada para renderizar los identificadores jerárquicos de Ajustes
 * (ej. 01.1.1 o 05-ATM-PAS-KEY).
 */
@Composable
fun InsigniaIdAjuste(
    id: String,
    modifier: Modifier = Modifier,
    ajustes: AjustesApp? = null,
    colorForzado: Color? = null
) {
    if (id.isBlank()) return

    val prefijo = id.trimStart().take(2)
    val colorBase = colorForzado ?: when {
        ajustes != null -> when (prefijo) {
            "01" -> parsearColorO(ajustes.colorIdSeguridad, Color(0xFF3F51B5))
            "02" -> parsearColorO(ajustes.colorIdApariencia, Color(0xFF8E24AA))
            "03" -> parsearColorO(ajustes.colorIdLista, Color(0xFF00897B))
            "04" -> parsearColorO(ajustes.colorIdHerramientas, Color(0xFFFB8C00))
            "05" -> parsearColorO(ajustes.colorIdCopias, Color(0xFF1E88E5))
            "06" -> parsearColorO(ajustes.colorIdSistema, Color(0xFF607D8B))
            else -> ColorAcento
        }
        else -> when (prefijo) {
            "01" -> Color(0xFF3F51B5)
            "02" -> Color(0xFF8E24AA)
            "03" -> Color(0xFF00897B)
            "04" -> Color(0xFFFB8C00)
            "05" -> Color(0xFF1E88E5)
            "06" -> Color(0xFF607D8B)
            else -> ColorAcento
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(fondoBadgeParaTema(colorBase))
            .padding(horizontal = 5.dp, vertical = 1.5.dp)
    ) {
        Text(
            text = id,
            color = colorLegibleParaTema(colorBase),
            style = EstiloMono.copy(
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            softWrap = false
        )
    }
}
