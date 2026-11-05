/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Fila compacta con selector de disponibilidad para el diálogo de transferencia CXF:
 * muestra el tiempo actual y despliega opciones con icono neutro.
 */
@Composable
fun SelectorDisponibilidadDialogoCxf(
    segundosSeleccionados: Int,
    autoBloqueoActualSegundos: Int,
    alCambiarTiempo: (Int) -> Unit,
    alDetectarConflicto: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var dialogoAbierto by remember { mutableStateOf(false) }

    val etiquetaActual = AlmacenAjustes.OPCIONES_VENTANA_TRANSFERENCIA
        .firstOrNull { it.first == segundosSeleccionados }?.second
        ?: if (segundosSeleccionados == -1) "Hasta bloquear" else "$segundosSeleccionados s"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CurvaturaEsquinas))
            .background(ColorTarjetaAjustes)
            .border(1.dp, ColorBordeActual, RoundedCornerShape(CurvaturaEsquinas))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Timer,
                contentDescription = null,
                tint = TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "Disponibilidad:",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
        }

        Box(
            modifier = Modifier
                .clip(FormaPequena)
                .clickable { dialogoAbierto = true }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$etiquetaActual ▾",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                ),
                color = TextoPrincipal
            )
        }
    }

    if (dialogoAbierto) {
        DialogoBoveda(
            onDismissRequest = { dialogoAbierto = false },
            title = {
                TextoTitulo(
                    texto = "Tiempo de disponibilidad",
                    estilo = EstiloTitulo.MEDIANO
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    AlmacenAjustes.OPCIONES_VENTANA_TRANSFERENCIA.forEach { (segundos, etiqueta) ->
                        val seleccionado = segundos == segundosSeleccionados
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(FormaPequena)
                                .clickable {
                                    dialogoAbierto = false
                                    if (segundos > 0 && autoBloqueoActualSegundos > 0 && segundos > autoBloqueoActualSegundos) {
                                        alDetectarConflicto(segundos)
                                    } else {
                                        alCambiarTiempo(segundos)
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = etiqueta,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
                                ),
                                color = if (seleccionado) ColorAcento else TextoPrincipal
                            )
                            if (seleccionado) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = ColorAcento,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                BotonTextoBoveda(
                    texto = "Cerrar",
                    alPulsar = { dialogoAbierto = false }
                )
            }
        )
    }
}
