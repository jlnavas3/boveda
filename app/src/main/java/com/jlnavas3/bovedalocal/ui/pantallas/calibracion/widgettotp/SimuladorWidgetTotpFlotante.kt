package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widgettotp

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.IndicadorTotpTarta
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SimuladorWidgetTotpFlotante(
    ajustes: AjustesApp,
    segundosRestantes: Long,
    colorBordeEfectivo: Color,
    colorContadorEfectivo: Color,
    colorCodigoEfectivo: Color,
    colorTituloIconoEfectivo: Color,
    vistaBloqueadaEnPreview: Boolean,
    haptica: Haptica,
    alAlternarBloqueo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF101216))
                .padding(10.dp)
        ) {
            val formaWidget = RoundedCornerShape(ajustes.widgetCurvaturaEsquinasDp.dp)
            val colorFondoWidget = Color(0xFF1A1815).copy(alpha = ajustes.widgetTransparenciaFondo.coerceIn(0f, 1f))
            val grosorDp = ajustes.widgetGrosorBordeDp.dp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(formaWidget)
                    .background(colorFondoWidget)
                    .then(
                        if (ajustes.widgetGrosorBordeDp > 0.1f) {
                            Modifier.border(
                                grosorDp,
                                colorBordeEfectivo.copy(alpha = (ajustes.widgetTransparenciaFondo.coerceAtLeast(0.6f))),
                                formaWidget
                            )
                        } else {
                            Modifier
                        }
                    )
                    .padding(10.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Filled.Timer,
                            contentDescription = null,
                            tint = colorTituloIconoEfectivo,
                            modifier = Modifier.size(16.dp)
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

                    if (vistaBloqueadaEnPreview) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF26231E))
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.Lock,
                                    contentDescription = null,
                                    tint = ColorSeguridad,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "Bóveda bloqueada",
                                    color = TextoSecundario,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    } else {
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
                                        .background(Color(0xFF26231E))
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
                                        segundosRestantes = segundosRestantes,
                                        periodo = 30L,
                                        tamano = 15.dp,
                                        colorPersonalizado = colorContadorEfectivo
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Alternar bloqueado / desbloqueado en la previa
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.55f))
                    .clickable {
                        haptica.tic()
                        alAlternarBloqueo()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (vistaBloqueadaEnPreview) Icons.Filled.Lock else Icons.Filled.LockOpen,
                    contentDescription = if (vistaBloqueadaEnPreview) "Bloqueado" else "Desbloqueado",
                    tint = if (vistaBloqueadaEnPreview) ColorSeguridad else ColorAcento,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
