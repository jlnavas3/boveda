package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.componentes.IndicadorTotpTarta
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

@Composable
fun PreviaWidgetTotpCompacta(
    ajustes: AjustesApp,
    modifier: Modifier = Modifier
) {
    val colorBordeEfectivo = parsearColorO(ajustes.widgetColorBorde, parsearColorO(AjustesDefaults.WidgetTotp.COLOR_BORDE, ColorAcento))
    val colorContadorEfectivo = parsearColorO(ajustes.widgetColorContador, Color.White)
    val colorCodigoEfectivo = parsearColorO(ajustes.widgetColorCodigo, parsearColorO(AjustesDefaults.WidgetTotp.COLOR_CODIGO, ColorAcento))
    val colorTituloIconoEfectivo = parsearColorO(ajustes.widgetColorTituloIcono, Color.White)
    val colorFilasEfectivo = parsearColorO(
        if (ajustes.widgetColorFilas.isBlank() || ajustes.widgetColorFilas == AjustesDefaults.WidgetTotp.COLOR_FILAS)
            AjustesDefaults.WidgetTotp.COLOR_FILAS_DEFECTO
        else
            ajustes.widgetColorFilas,
        ColorCampoAjustes
    )

    val formaWidget = RoundedCornerShape(ajustes.widgetCurvaturaEsquinasDp.dp)
    val alphaFondo = ajustes.widgetTransparenciaFondo.coerceIn(0f, 1f)
    val colorFondoBase = Color(0xFF1C1C1E)
    val grosorDp = ajustes.widgetGrosorBordeDp.dp

    val brushFondoWidget = if (ajustes.widgetTotpVidrioEsmerilado) {
        val luz = ajustes.widgetTotpEsmeriladoLuz
        val intensidad = ajustes.widgetTotpEsmeriladoIntensidad
        val brilloSuperior = (0.24f * luz * intensidad).coerceIn(0f, 0.40f)
        val brilloInferior = (0.04f * luz * intensidad).coerceIn(0f, 0.15f)
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = (alphaFondo * 0.35f + brilloSuperior).coerceIn(0f, 1f)),
                colorFondoBase.copy(alpha = alphaFondo),
                colorFondoBase.copy(alpha = (alphaFondo * 0.95f + brilloInferior).coerceIn(0f, 1f))
            )
        )
    } else {
        SolidColor(colorFondoBase.copy(alpha = alphaFondo))
    }

    val brushBordeWidget = if (ajustes.widgetTotpVidrioEsmerilado && ajustes.widgetGrosorBordeDp > 0.1f) {
        val luz = ajustes.widgetTotpEsmeriladoLuz
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = (0.45f + 0.50f * luz).coerceIn(0f, 0.95f)),
                colorBordeEfectivo.copy(alpha = (alphaFondo.coerceAtLeast(0.5f))),
                colorBordeEfectivo.copy(alpha = (alphaFondo.coerceAtLeast(0.3f)))
            )
        )
    } else {
        SolidColor(colorBordeEfectivo.copy(alpha = (alphaFondo.coerceAtLeast(0.6f))))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(formaWidget)
            .background(brushFondoWidget)
            .then(
                if (ajustes.widgetGrosorBordeDp > 0.1f) {
                    Modifier.border(grosorDp, brushBordeWidget, formaWidget)
                } else {
                    Modifier
                }
            )
            .padding(10.dp)
    ) {
        if (ajustes.widgetTotpVidrioEsmerilado && ajustes.widgetTotpEsmeriladoLuz > 0.05f) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(formaWidget)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = (0.24f * ajustes.widgetTotpEsmeriladoLuz * ajustes.widgetTotpEsmeriladoIntensidad).coerceIn(0f, 0.40f)),
                                Color.Transparent
                            ),
                            startY = 0f,
                            endY = 38f
                        )
                    )
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Filled.Timer,
                    contentDescription = null,
                    tint = colorTituloIconoEfectivo,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "Códigos 2FA",
                    color = colorTituloIconoEfectivo,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            val fondoFilaEfectivo = if (ajustes.widgetTransparenciaFilas > 0.001f) {
                colorFilasEfectivo.copy(alpha = ajustes.widgetTransparenciaFilas)
            } else {
                Color.Transparent
            }

            val itemsEjemplo = listOf(
                Triple("GitHub", "jlnavas3", "482 190"),
                Triple("Google", "correo@gmail.com", "731 564")
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                itemsEjemplo.forEach { (cuenta, usuario, codigo) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(fondoFilaEfectivo)
                            .padding(horizontal = 9.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                cuenta,
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                usuario,
                                color = TextoSecundario,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            codigo,
                            color = colorCodigoEfectivo,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(Modifier.width(6.dp))
                        IndicadorTotpTarta(
                            segundosRestantes = 24L,
                            periodo = 30L,
                            tamano = 14.dp,
                            colorPersonalizado = colorContadorEfectivo
                        )
                    }
                }
            }
        }
    }
}
