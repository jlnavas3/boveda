package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

/**
 * Tipos semánticos de alerta para mensajes y avisos contextuales.
 */
enum class TipoAlerta {
    PRIMARY,
    SECONDARY,
    SUCCESS,
    DANGER,
    WARNING,
    INFO
}

/**
 * Componente unificado de avisos y alertas siguiendo la línea de diseño de
 * Honor MagicOS y Samsung One UI.
 *
 * Características:
 * - Esquinas suaves de 16.dp (FormaTarjeta) sin sombras ni elevaciones duras.
 * - Fondos translúcidos dinámicos calculados según el tema Claro/Oscuro activo.
 * - Borde ultraligero y armónico que no genera ruido visual.
 * - Icono semántico automático o personalizable.
 * - Soporte para título, texto descriptivo o bloque composable personalizado.
 * - Slot de acción contextual en la parte inferior (por ejemplo, botones de acción).
 */
@Composable
fun ComponenteAlerta(
    modifier: Modifier = Modifier,
    tipo: TipoAlerta = TipoAlerta.INFO,
    titulo: String? = null,
    mensaje: String? = null,
    icono: ImageVector? = null,
    mostrarIcono: Boolean = true,
    accion: (@Composable () -> Unit)? = null,
    contenido: (@Composable ColumnScope.() -> Unit)? = null
) {
    val esOscuro = esOscuroActivo

    val colorBase = when (tipo) {
        TipoAlerta.PRIMARY -> ColorAcento
        TipoAlerta.SECONDARY -> if (esOscuro) Color(0xFF9E9EA4) else Color(0xFF5C6270)
        TipoAlerta.SUCCESS -> Menta
        TipoAlerta.DANGER -> Peligro
        TipoAlerta.WARNING -> Ambar
        TipoAlerta.INFO -> ColorSeguridad
    }

    val iconoFinal = icono ?: when (tipo) {
        TipoAlerta.PRIMARY -> Icons.Filled.Notifications
        TipoAlerta.SECONDARY -> Icons.Filled.Info
        TipoAlerta.SUCCESS -> Icons.Filled.CheckCircle
        TipoAlerta.DANGER -> Icons.Filled.Warning
        TipoAlerta.WARNING -> Icons.Filled.ReportProblem
        TipoAlerta.INFO -> Icons.Filled.Info
    }

    val colorLegible = colorLegibleParaTema(colorBase, esOscuro)
    val colorFondo = colorBase.copy(alpha = if (esOscuro) 0.12f else 0.08f)
    val colorBorde = colorBase.copy(alpha = if (esOscuro) 0.28f else 0.22f)
    val formaAlerta = RoundedCornerShape(18.dp)

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colorFondo,
        shape = formaAlerta,
        border = BorderStroke(1.dp, colorBorde)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(formaAlerta)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = if (accion != null) 14.dp else 16.dp
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = if (titulo != null) Alignment.CenterVertically else Alignment.Top
                ) {
                    if (mostrarIcono) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(colorBase.copy(alpha = if (esOscuro) 0.18f else 0.14f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = iconoFinal,
                                contentDescription = null,
                                tint = colorLegible,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        if (!titulo.isNullOrBlank()) {
                            Text(
                                text = titulo,
                                color = colorLegible,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp
                                )
                            )
                        }

                        if (!mensaje.isNullOrBlank() && titulo.isNullOrBlank()) {
                            Text(
                                text = mensaje,
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    lineHeight = 18.sp,
                                    fontSize = 13.5.sp
                                )
                            )
                        }
                    }
                }

                if (!titulo.isNullOrBlank() && !mensaje.isNullOrBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = mensaje,
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.bodySmall.copy(
                            lineHeight = 18.sp,
                            fontSize = 13.5.sp
                        )
                    )
                }

                if (contenido != null) {
                    if (titulo != null || mensaje != null) {
                        Spacer(Modifier.height(8.dp))
                    }
                    contenido()
                }
            }

            if (accion != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(colorBorde)
                )
                accion()
            }
        }
    }
}
