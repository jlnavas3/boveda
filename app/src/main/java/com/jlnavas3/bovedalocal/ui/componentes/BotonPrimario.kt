package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAcento
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun BotonPrimario(
    texto: String,
    modifier: Modifier = Modifier,
    activo: Boolean = true,
    icono: ImageVector? = null,
    alPulsar: () -> Unit
) {
    val escala by animateFloatAsState(
        targetValue = if (activo) 1f else 0.98f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "escalaBoton"
    )
    val forma = FormaBoton
    val colorTexto = if (activo) ColorSobreAcento else TextoSecundario
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(38.dp)
            .clip(forma)
            .background(if (activo) DegradadoAcento else Brush.horizontalGradient(listOf(Borde, Borde)))
            .then(
                if (GrosorBorde > 0.dp && !activo) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual.copy(alpha = 0.5f), forma)
                } else Modifier
            )
            .clickable(enabled = activo) { alPulsar() },
        contentAlignment = Alignment.Center
    ) {
        if (icono != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorTexto,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = texto,
                    color = colorTexto,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (14 * escala * EscalaTexto).sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Text(
                text = texto,
                color = colorTexto,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = (14 * escala * EscalaTexto).sp,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

