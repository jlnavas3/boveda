package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta

@Composable
fun FondoDeslizamientoUsuario(
    forma: Shape,
    progreso: Float,
    modifier: Modifier = Modifier,
    enGrupo: Boolean = false
) {
    val escala = 0.85f + 0.20f * progreso
    val opacidad = 0.4f + 0.6f * progreso

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(forma)
            .background(Menta.copy(alpha = 0.14f + 0.10f * progreso))
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && !enGrupo) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else Modifier
            )
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.scale(escala)
        ) {
            Text(
                text = "Copiar\nUsuario",
                color = Menta.copy(alpha = opacidad),
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.width(10.dp))
            Icon(
                Icons.Filled.Person,
                contentDescription = null,
                tint = Menta.copy(alpha = opacidad),
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
fun FondoDeslizamientoContrasena(
    forma: Shape,
    progreso: Float,
    modifier: Modifier = Modifier,
    enGrupo: Boolean = false
) {
    val escala = 0.85f + 0.20f * progreso
    val opacidad = 0.4f + 0.6f * progreso

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(forma)
            .background(Ambar.copy(alpha = 0.14f + 0.10f * progreso))
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && !enGrupo) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else Modifier
            )
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.scale(escala)
        ) {
            Text(
                text = "Copiar\nContraseña",
                color = Ambar.copy(alpha = opacidad),
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.width(10.dp))
            Icon(
                Icons.Filled.Key,
                contentDescription = null,
                tint = Ambar.copy(alpha = opacidad),
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
