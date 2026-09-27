package com.jlnavas3.bovedalocal.ui.pantallas.historial

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.RegistroClaveGenerada
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBoveda
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun FilaClaveHistorial(
    item: RegistroClaveGenerada,
    formatoFecha: SimpleDateFormat,
    ahora: Long,
    vaciadoAuto: Boolean,
    tiempoDestruccion: Long,
    alCopiar: () -> Unit,
    alEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }

    TarjetaBoveda(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Fila superior: Origen y Longitud a la izquierda; Fecha e Íconos a la derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Badges a la izquierda: Origen y Longitud ("30 car.")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(FormaPequena)
                            .background(fondoBadgeParaTema(ColorGenerador))
                            .padding(horizontal = 7.dp, vertical = 2.5.dp)
                    ) {
                        Text(
                            text = item.origen,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                            color = colorLegibleParaTema(ColorGenerador),
                            maxLines = 1
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(FormaPequena)
                            .background(SuperficieAlta)
                            .padding(horizontal = 7.dp, vertical = 2.5.dp)
                    ) {
                        Text(
                            text = "${item.clave.length} car.",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Medium),
                            color = TextoSecundario,
                            maxLines = 1
                        )
                    }
                }

                Spacer(Modifier.width(8.dp))

                // Esquina derecha superior: Fecha de creación y justo debajo los íconos (ver, copiar, eliminar)
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatoFecha.format(Date(item.generadaEn)),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextoSecundario,
                        maxLines = 1
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { visible = !visible },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Alternar visibilidad",
                                tint = ColorIconosInternos,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                        IconButton(
                            onClick = alCopiar,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ContentCopy,
                                contentDescription = "Copiar contraseña",
                                tint = ColorIconosInternos,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                        IconButton(
                            onClick = alEliminar,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Eliminar del historial",
                                tint = Peligro.copy(alpha = 0.85f),
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // La contraseña se muestra en una sola línea ajustando tamaño
            val tamanoTexto = when {
                item.clave.length > 32 -> 11.sp
                item.clave.length > 24 -> 12.sp
                item.clave.length > 18 -> 13.sp
                else -> 14.sp
            }
            Text(
                text = if (visible) contrasenaColoreada(item.clave) else buildAnnotatedString { append("•".repeat(item.clave.length.coerceIn(8, 26))) },
                style = EstiloMono.copy(fontWeight = FontWeight.SemiBold, fontSize = tamanoTexto),
                color = ColorTitulos,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            // Indicador de expiración en la parte inferior
            if (vaciadoAuto && tiempoDestruccion > 0) {
                val restanteMs = (item.generadaEn + tiempoDestruccion) - ahora
                val restanteMin = (restanteMs / 60000L).coerceAtLeast(0)
                val restanteSeg = ((restanteMs % 60000L) / 1000L).coerceAtLeast(0)
                Spacer(Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = null,
                        tint = TextoSecundario.copy(alpha = 0.7f),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = if (restanteMin > 60) "Expira en ${restanteMin / 60}h ${restanteMin % 60}m" else "Expira en ${restanteMin}m ${restanteSeg}s",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = TextoSecundario
                    )
                }
            }
        }
    }
}
