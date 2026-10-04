package com.jlnavas3.bovedalocal.ui.pantallas.tema

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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.colorContraste

/**
 * Tarjeta de vista previa compacta que permanece flotante/fijada arriba (Sticky Top)
 * permitiendo ver en tiempo real la combinación de colores mientras se hace scroll en los controles.
 * Sin títulos innecesarios para maximizar el área visible.
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colorFondo)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CurvaturaEsquinas))
                .background(colorTarjeta)
                .border(1.dp, colorBorde, RoundedCornerShape(CurvaturaEsquinas))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Fila 1: Credencial con Icono, Textos y Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
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
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Google Workspace",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = colorTextoPrincipal
                        ),
                        maxLines = 1
                    )
                    Spacer(Modifier.height(1.dp))
                    Text(
                        text = "usuario@empresa.com • TOTP activo",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = colorTextoSecundario
                        ),
                        maxLines = 1
                    )
                }

                Spacer(Modifier.width(8.dp))

                SwitchBoveda(
                    checked = switchActivo,
                    colorActivo = colorAcento,
                    colorInactivoTrack = colorCampo,
                    colorInactivoThumb = colorTextoSecundario,
                    onCheckedChange = onAlternarSwitch
                )
            }

            Spacer(Modifier.height(8.dp))

            // Divisor sutil
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.6.dp)
                    .background(colorBorde)
            )

            Spacer(Modifier.height(8.dp))

            // Fila 2: Campo interactivo (Capa 2) y Botón Primario
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Campo de texto simulado con Capa 2
                Row(
                    modifier = Modifier
                        .weight(1f)
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
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Contraseña segura...",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = colorTextoSecundario
                        ),
                        maxLines = 1
                    )
                }

                // Botón Primario compacto
                val colorContenidoBoton = if (colorAcento.alpha < 0.45f) {
                    colorTextoPrincipal
                } else {
                    colorContraste(colorAcento)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorAcento)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Guardar",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = colorContenidoBoton
                        )
                    )
                }
            }
        }
    }
}
