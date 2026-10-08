package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonPrimario
import com.jlnavas3.bovedalocal.ui.componentes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.colorContraste

/**
 * Tarjeta de vista previa compacta que permanece flotante/fijada arriba (Sticky Top)
 * permitiendo ver en tiempo real la combinación de colores mientras se hace scroll en los controles.
 * Cuenta con alternador de colapsado para maximizar el área visible de ajuste cuando se requiera.
 */
@Composable
fun TarjetaFlotantePreviaLaboratorio(
    colorFondo: Color,
    colorTarjeta: Color,
    colorBorde: Color,
    colorCampo: Color,
    colorTextoPrincipal: Color,
    colorTextoSecundario: Color,
    colorAcento: Color,
    switchActivo: Boolean,
    onAlternarSwitch: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var colapsado by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colorFondo)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CurvaturaEsquinas))
                .background(colorTarjeta)
                .border(1.dp, colorBorde, RoundedCornerShape(CurvaturaEsquinas))
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            // Cabecera compacta: Icono, Título y botón colapsar/expandir
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(colorAcento),
                    contentAlignment = Alignment.Center
                ) {
                    val colorContenidoAcento = if (colorAcento.alpha < 0.45f) {
                        colorTextoPrincipal
                    } else {
                        colorContraste(colorAcento)
                    }
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = colorContenidoAcento,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Google Workspace",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.5.sp,
                            color = colorTextoPrincipal
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = if (colapsado) "Previa colapsada" else "usuario@empresa.com • TOTP activo",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.5.sp,
                            color = colorTextoSecundario
                        ),
                        maxLines = 1
                    )
                }

                Spacer(Modifier.width(6.dp))

                SwitchBoveda(
                    checked = switchActivo,
                    colorActivo = colorAcento,
                    colorInactivoTrack = colorCampo,
                    colorInactivoThumb = colorTextoSecundario,
                    onCheckedChange = onAlternarSwitch
                )

                Spacer(Modifier.width(4.dp))

                IconButton(
                    onClick = { colapsado = !colapsado },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (colapsado) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                        contentDescription = if (colapsado) "Expandir previa" else "Colapsar previa",
                        tint = colorTextoSecundario,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = !colapsado,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(Modifier.height(8.dp))

                    // Divisor sutil
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.6.dp)
                            .background(colorBorde)
                    )

                    Spacer(Modifier.height(8.dp))

                    // Fila 2: Campo interactivo (Capa 2)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(colorCampo)
                            .border(0.8.dp, colorBorde, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Security,
                            contentDescription = null,
                            tint = colorTextoSecundario,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Contraseña segura de ejemplo...",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                color = colorTextoSecundario
                            ),
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = null,
                            tint = colorTextoSecundario,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // Fila 3: Componentes de Botones Reales (BotonPrimario y BotonBorde)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BotonPrimario(
                            texto = "Primario",
                            modifier = Modifier.weight(1f),
                            alPulsar = {}
                        )
                        BotonBorde(
                            texto = "Secundario",
                            modifier = Modifier.weight(1f),
                            color = colorTextoPrincipal,
                            alPulsar = {}
                        )
                    }
                }
            }
        }
    }
}
