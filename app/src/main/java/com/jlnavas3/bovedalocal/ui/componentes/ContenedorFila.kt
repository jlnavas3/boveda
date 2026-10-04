package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

/**
 * Contenedor para filas de elementos interactivos (equivalente a row container en UI).
 * Organiza icono inicial, textos y acción final (botón, switch, chevron, etc.).
 */
@Composable
fun ContenedorFila(
    titulo: String,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color = ColorIconosInternos,
    alPulsar: (() -> Unit)? = null,
    accionFinal: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (alPulsar != null) Modifier.clickable { alPulsar() } else Modifier)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            val colorLegible = colorLegibleParaTema(colorIcono)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(FormaPequena)
                .background(fondoBadgeParaTema(colorIcono)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorLegible,
                    modifier = Modifier.size(19.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
            if (!subtitulo.isNullOrBlank()) {
                Text(
                    text = subtitulo,
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        if (accionFinal != null) {
            Spacer(Modifier.width(8.dp))
            accionFinal()
        }
    }
}
