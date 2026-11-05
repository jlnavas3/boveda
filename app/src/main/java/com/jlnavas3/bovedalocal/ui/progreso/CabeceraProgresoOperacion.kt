/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.progreso

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.EstadoProgresoOperacion
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente que renderiza la cabecera del panel de operaciones
 * con título general, estado de la fase, contador y porcentaje,
 * barra de progreso global única y botones de control (minimizar y descartar).
 */
@Composable
fun CabeceraProgresoOperacion(
    estado: EstadoProgresoOperacion,
    alMinimizar: () -> Unit,
    alCerrar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progresoAnimado by animateFloatAsState(
        targetValue = estado.progresoGlobal.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 300),
        label = "progresoGlobalOperacion"
    )

    val formaCirculoOEsquina = if (CurvaturaEsquinas == 0.dp) RoundedCornerShape(0.dp) else CircleShape

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icono de estado general
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(formaCirculoOEsquina)
                    .background(
                        when {
                            estado.error != null -> Peligro.copy(alpha = 0.15f)
                            estado.completado -> Menta.copy(alpha = 0.15f)
                            else -> ColorAcento.copy(alpha = 0.15f)
                        }
                    )
            ) {
                Icon(
                    imageVector = when {
                        estado.error != null -> Icons.Filled.Error
                        estado.completado -> Icons.Filled.CheckCircle
                        else -> Icons.Filled.Sync
                    },
                    contentDescription = null,
                    tint = when {
                        estado.error != null -> Peligro
                        estado.completado -> Menta
                        else -> ColorAcento
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = estado.titulo,
                    color = TextoPrincipal,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = estado.fase,
                        color = if (estado.error != null) Peligro else TextoSecundario,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (estado.totalCuentas > 0 && estado.enEjecucion) {
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "${estado.cuentasProcesadas}/${estado.totalCuentas} (${(progresoAnimado * 100).toInt()}%)",
                            color = TextoSecundario,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Minimizar a píldora flotante
                if (estado.enEjecucion) {
                    IconButton(
                        onClick = alMinimizar,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Minimizar a píldora",
                            tint = TextoSecundario,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Cerrar / Descartar
                IconButton(
                    onClick = alCerrar,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cerrar",
                        tint = TextoSecundario,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Barra de progreso global
        LinearProgressIndicator(
            progress = { progresoAnimado },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(formaCirculoOEsquina),
            color = when {
                estado.error != null -> Peligro
                estado.completado -> Menta
                else -> ColorAcento
            },
            trackColor = SuperficieAlta
        )
    }
}
