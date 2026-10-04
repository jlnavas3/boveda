package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

/**
 * Contenedor de sección temática (equivalente a section en HTML/UI).
 * Proporciona cabecera con icono, título estilizado y subtítulo explicativo opcional.
 */
@Composable
fun ContenedorSeccion(
    titulo: String,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color = ColorIconosInternos,
    colorTitulo: Color = ColorTitulos,
    espaciado: Dp = 10.dp,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(espaciado)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (icono != null) {
                val colorLegible = colorLegibleParaTema(colorIcono)
                Box(
                    modifier = Modifier
                        .size(34.dp)
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
            }
            Text(
                text = titulo,
                color = colorTitulo,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
        if (!subtitulo.isNullOrBlank()) {
            Text(
                text = subtitulo,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        contenido()
    }
}
