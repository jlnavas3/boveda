/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.Locale

@Composable
fun TarjetaEntropiaDiccionario(
    entropiaPorPalabra: Double,
    totalActivas: Int,
    totalExcluidas: Int,
    haptica: Haptica?,
    modifier: Modifier = Modifier
) {
    var expandido by rememberSaveable { mutableStateOf(false) }
    val formaBorde = RoundedCornerShape(CurvaturaEsquinas)
    val modificadorBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        Modifier.border(GrosorBorde, ColorBordeActual, formaBorde)
    } else Modifier

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(formaBorde)
            .clickable {
                haptica?.toque()
                expandido = !expandido
            }
            .then(modificadorBorde),
        shape = formaBorde,
        color = ColorTarjetaAjustes
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Entropía Diceware",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextoPrincipal
                    )
                    Text(
                        text = String.format(Locale.ROOT, "%.2f bits/palabra", entropiaPorPalabra),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = ColorGenerador
                    )
                }

                Icon(
                    imageVector = if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expandido) "Colapsar detalles" else "Expandir detalles",
                    tint = TextoSecundario
                )
            }

            AnimatedVisibility(
                visible = expandido,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Palabras activas en catálogo",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextoSecundario
                            )
                            Text(
                                text = String.format(Locale.ROOT, "%,d", totalActivas),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
                            )
                        }
                        if (totalExcluidas > 0) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Palabras excluidas",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextoSecundario
                                )
                                Text(
                                    text = String.format(Locale.ROOT, "%,d", totalExcluidas),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Peligro
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "La entropía mide la resistencia matemática ante ataques de fuerza bruta. Cada palabra adicional elegida al azar suma aproximadamente ${String.format(Locale.ROOT, "%.1f", entropiaPorPalabra)} bits de seguridad.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
            }
        }
    }
}
