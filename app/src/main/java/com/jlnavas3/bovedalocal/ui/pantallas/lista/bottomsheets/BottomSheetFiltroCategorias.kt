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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.heightIn
import androidx.compose.ui.platform.LocalConfiguration
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.ui.componentes.barraDesplazamientoVertical
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorModalAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.IconosCategorias
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Panel inferior deslizante para seleccionar y filtrar por Categoría con buscador rápido y creación directa.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetFiltroCategorias(
    visible: Boolean,
    categorias: List<Categoria>,
    categoriaSeleccionadaId: String?,
    conteoPorCategoria: Map<String, Int>,
    totalEntradas: Int,
    alSeleccionarCategoria: (String?) -> Unit,
    alCrearCategoria: () -> Unit,
    alGestionarCategorias: () -> Unit,
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Folder,
                        contentDescription = null,
                        tint = ColorAcento,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Filtrar por Categoría",
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                IconButton(
                    onClick = {
                        alCerrar()
                        alCrearCategoria()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Crear categoría",
                        tint = ColorAcento,
                        modifier = Modifier.size(20.dp)
                    )
                }
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
                // Opción: Todas las categorías
                item {
                    val seleccionado = categoriaSeleccionadaId == null
                    FilaOpcionCategoria(
                        titulo = "Todas las categorías",
                        subtitulo = "$totalEntradas entradas en total",
                        icono = Icons.Filled.Layers,
                        colorIcono = ColorIconosInternos,
                        seleccionado = seleccionado,
                        alPulsar = {
                            alSeleccionarCategoria(null)
                            alCerrar()
                        }
                    )
                }

                // Lista de categorías
                items(categorias, key = { it.id }) { cat ->
                    val colorBase = IconosCategorias.parsearColorHex(cat.colorHex) ?: ColorAcento
                    val icono = IconosCategorias.obtenerIcono(cat.icono)
                    val conteo = conteoPorCategoria[cat.id] ?: 0
                    val seleccionado = categoriaSeleccionadaId == cat.id

                    FilaOpcionCategoria(
                        titulo = cat.nombre,
                        subtitulo = "$conteo entradas",
                        conteo = conteo,
                        icono = icono,
                        colorIcono = colorBase,
                        seleccionado = seleccionado,
                        alPulsar = {
                            alSeleccionarCategoria(cat.id)
                            alCerrar()
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Botón Administrar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        alCerrar()
                        alGestionarCategorias()
                    }
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Administrar categorías...",
                    color = ColorAcento,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Composable
private fun FilaOpcionCategoria(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    colorIcono: Color,
    seleccionado: Boolean,
    conteo: Int? = null,
    alPulsar: () -> Unit
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val tieneBorde = GrosorBorde > 0.dp && EstiloBorde != "ninguno"
    val anchoBorde = if (seleccionado) GrosorBorde.coerceAtLeast(1.5.dp) else (if (tieneBorde) GrosorBorde else 0.dp)
    val colorBorde = if (seleccionado) colorIcono else ColorBordeActual
    val fondo = if (seleccionado) colorIcono.copy(alpha = 0.16f) else ColorTarjetaAjustes

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
                .background(colorIcono.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.SemiBold
                )
            )
            Text(
                text = subtitulo,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
            )
        }

        if (conteo != null) {
            Text(
                text = "$conteo",
                color = if (seleccionado) colorIcono else TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(horizontal = 6.dp)
            )
        }

        if (seleccionado) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Seleccionado",
                tint = colorIcono,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
