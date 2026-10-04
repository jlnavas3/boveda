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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAcento
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.util.Haptica

enum class VarianteBoton { PRIMARIO, SECUNDARIO, PELIGRO }

@Composable
fun BotonBoveda(
    texto: String,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier,
    variante: VarianteBoton = VarianteBoton.PRIMARIO,
    activo: Boolean = true,
    icono: ImageVector? = null
) {
    when (variante) {
        VarianteBoton.PRIMARIO -> BotonPrimario(texto = texto, modifier = modifier, activo = activo, icono = icono, alPulsar = alPulsar)
        VarianteBoton.SECUNDARIO -> BotonBorde(texto = texto, modifier = modifier, icono = icono, alPulsar = alPulsar)
        VarianteBoton.PELIGRO -> BotonPeligro(texto = texto, modifier = modifier, icono = icono, alPulsar = alPulsar)
    }
}

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
            .height(42.dp)
            .clip(forma)
            .background(if (activo) DegradadoAcento else Brush.horizontalGradient(listOf(Borde, Borde)))
            .then(
                if (GrosorBorde > 0.dp && !activo) {
                    Modifier.border(GrosorBorde, Borde, forma)
                } else {
                    Modifier
                }
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

@Composable
@Deprecated("Usar BotonPrimario en su lugar", ReplaceWith("BotonPrimario(texto, modifier, activo, icono, alPulsar)"))
fun BotonAmbar(
    texto: String,
    modifier: Modifier = Modifier,
    activo: Boolean = true,
    icono: ImageVector? = null,
    alPulsar: () -> Unit
) = BotonPrimario(texto, modifier, activo, icono, alPulsar)

@Composable
fun BotonBorde(
    texto: String,
    modifier: Modifier = Modifier,
    color: Color = TextoPrincipal,
    icono: ImageVector? = null,
    alPulsar: () -> Unit
) {
    val forma = FormaBoton
    val fondoBoton = ColorCampoAjustes
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .clip(forma)
            .background(fondoBoton)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else Modifier
            )
            .clickable { alPulsar() },
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
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = texto,
                    color = color,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (14 * EscalaTexto).sp,
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
                color = color,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = (14 * EscalaTexto).sp,
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

@Composable
fun BotonPeligro(
    texto: String,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    alPulsar: () -> Unit
) {
    BotonBorde(
        texto = texto,
        modifier = modifier,
        color = Peligro,
        icono = icono,
        alPulsar = alPulsar
    )
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun BotonesBovedaPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BotonBoveda(texto = "Botón Primario", alPulsar = {}, variante = VarianteBoton.PRIMARIO)
            BotonBoveda(texto = "Botón Secundario", alPulsar = {}, variante = VarianteBoton.SECUNDARIO)
            BotonBoveda(texto = "Botón Peligro", alPulsar = {}, variante = VarianteBoton.PELIGRO)
            BotonBorde(texto = "Botón con Borde", alPulsar = {})
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                BotonTextoBoveda(texto = "Cancelar", alPulsar = {}, tipo = TipoBotonTexto.SECUNDARIO)
                Spacer(modifier = Modifier.width(8.dp))
                BotonTextoBoveda(texto = "Confirmar", alPulsar = {}, tipo = TipoBotonTexto.PRIMARIO)
            }
        }
    }
}

