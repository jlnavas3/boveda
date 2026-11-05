/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.bottomsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.platform.LocalConfiguration
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.componentes.barraDesplazamientoVertical
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorModalAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Panel inferior deslizante para seleccionar y filtrar por Etiqueta con buscador rápido.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetFiltroEtiquetas(
    visible: Boolean,
    etiquetas: List<String>,
    filtroEtiqueta: String?,
    alSeleccionarEtiqueta: (String?) -> Unit,
    alCerrar: () -> Unit
) {
    if (!visible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val configuracion = LocalConfiguration.current
    val alturaMaxima = (configuracion.screenHeightDp * 0.60f).dp
    val formaSheet = RoundedCornerShape(topStart = CurvaturaEsquinas, topEnd = CurvaturaEsquinas)

    val lazyListState = rememberLazyListState()

    ModalBottomSheet(
        onDismissRequest = alCerrar,
        sheetState = sheetState,
        shape = formaSheet,
        containerColor = ColorModalAjustes,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = alturaMaxima)
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            // Cabecera
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Label,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Filtrar por Etiqueta",
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(Modifier.height(14.dp))

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .barraDesplazamientoVertical(
                        state = lazyListState,
                        ancho = 3.dp,
                        margenFin = 1.dp
                    ),
                contentPadding = PaddingValues(end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Opción: Todas las etiquetas
                item {
                    val seleccionado = filtroEtiqueta.isNullOrBlank()
                    FilaOpcionEtiqueta(
                        titulo = "Todas las etiquetas",
                        seleccionado = seleccionado,
                        alPulsar = {
                            alSeleccionarEtiqueta(null)
                            alCerrar()
                        }
                    )
                }

                // Lista de etiquetas
                items(etiquetas, key = { it }) { et ->
                    val seleccionado = filtroEtiqueta == et
                    val normalizada = normalizarEtiqueta(et)

                    FilaOpcionEtiqueta(
                        titulo = normalizada,
                        seleccionado = seleccionado,
                        alPulsar = {
                            alSeleccionarEtiqueta(et)
                            alCerrar()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilaOpcionEtiqueta(
    titulo: String,
    seleccionado: Boolean,
    alPulsar: () -> Unit
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val tieneBorde = GrosorBorde > 0.dp && EstiloBorde != "ninguno"
    val anchoBorde = if (seleccionado) GrosorBorde.coerceAtLeast(1.5.dp) else (if (tieneBorde) GrosorBorde else 0.dp)
    val colorBorde = if (seleccionado) ColorAcento else ColorBordeActual
    val fondo = if (seleccionado) ColorAcento.copy(alpha = 0.16f) else ColorTarjetaAjustes

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(forma)
            .background(fondo)
            .then(
                if (anchoBorde > 0.dp) {
                    Modifier.border(anchoBorde, colorBorde, forma)
                } else Modifier
            )
            .clickable { alPulsar() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(ColorAcento.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (titulo == "Todas las etiquetas") Icons.Filled.Layers else Icons.AutoMirrored.Filled.Label,
                contentDescription = null,
                tint = ColorAcento,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = titulo,
            color = TextoPrincipal,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.SemiBold
            ),
            modifier = Modifier.weight(1f)
        )

        if (seleccionado) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Seleccionado",
                tint = ColorAcento,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
