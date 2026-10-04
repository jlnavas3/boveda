package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

@Composable
fun PreviaWidget1x1Compacta(
    ajustes: AjustesApp,
    modifier: Modifier = Modifier
) {
    val colorBorde = parsearColorO(ajustes.widget1x1ColorBorde, parsearColorO(AjustesDefaults.Widget1x1.COLOR_BORDE, ColorAcento))
    val colorIcono = parsearColorO(ajustes.widget1x1ColorIcono, parsearColorO(AjustesDefaults.Widget1x1.COLOR_ICONO, ColorAcento))
    val colorFondo = parsearColorO(ajustes.widget1x1ColorFondo, ColorCampoAjustes)

    val forma1x1 = RoundedCornerShape(ajustes.widget1x1CurvaturaEsquinasDp.dp)
    val alphaFondo = ajustes.widget1x1TransparenciaFondo.coerceIn(0f, 1f)
    val grosor1x1Dp = ajustes.widget1x1GrosorBordeDp.dp
    val ancho1x1Dp = ajustes.widget1x1AnchoDp.dp
    val alto1x1Dp = ajustes.widget1x1AltoDp.dp
    val minDimDp = minOf(ancho1x1Dp, alto1x1Dp)

    val brushFondo = if (ajustes.widget1x1VidrioEsmerilado) {
        val luz = ajustes.widget1x1EsmeriladoLuz
        val intensidad = ajustes.widget1x1EsmeriladoIntensidad
        val brilloSuperior = (0.28f * luz * intensidad).coerceIn(0f, 0.45f)
        val brilloInferior = (0.05f * luz * intensidad).coerceIn(0f, 0.20f)
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = (alphaFondo * 0.40f + brilloSuperior).coerceIn(0f, 1f)),
                colorFondo.copy(alpha = alphaFondo),
                colorFondo.copy(alpha = (alphaFondo * 0.95f + brilloInferior).coerceIn(0f, 1f))
            )
        )
    } else {
        SolidColor(colorFondo.copy(alpha = alphaFondo))
    }

    val brushBorde = if (ajustes.widget1x1VidrioEsmerilado && ajustes.widget1x1GrosorBordeDp > 0.1f) {
        val luz = ajustes.widget1x1EsmeriladoLuz
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = (0.45f + 0.50f * luz).coerceIn(0f, 0.95f)),
                colorBorde.copy(alpha = (alphaFondo.coerceAtLeast(0.5f))),
                colorBorde.copy(alpha = (alphaFondo.coerceAtLeast(0.3f)))
            )
        )
    } else {
        SolidColor(colorBorde.copy(alpha = (alphaFondo.coerceAtLeast(0.6f))))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = ancho1x1Dp, height = alto1x1Dp)
                .clip(forma1x1)
                .background(brushFondo)
                .then(
                    if (ajustes.widget1x1GrosorBordeDp > 0.1f) {
                        Modifier.border(grosor1x1Dp, brushBorde, forma1x1)
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (ajustes.widget1x1VidrioEsmerilado && ajustes.widget1x1EsmeriladoLuz > 0.05f) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(forma1x1)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = (0.28f * ajustes.widget1x1EsmeriladoLuz * ajustes.widget1x1EsmeriladoIntensidad).coerceIn(0f, 0.45f)),
                                    Color.Transparent
                                ),
                                startY = 0f,
                                endY = 28f
                            )
                        )
                )
            }

            Icon(
                imageVector = Icons.Filled.Key,
                contentDescription = "Generador Rápido 1x1",
                tint = colorIcono,
                modifier = Modifier.size((minDimDp * 0.52f).coerceAtLeast(16.dp))
            )
        }
    }
}
