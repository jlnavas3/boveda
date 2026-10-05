package com.jlnavas3.bovedalocal.ui.pantallas.categorias

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BarraCategoriasLista(
    categorias: List<Categoria>,
    categoriaSeleccionadaId: String?,
    totalEntradas: Int,
    conteoPorCategoria: Map<String, Int>,
    alSeleccionarCategoria: (String?) -> Unit,
    alCrearCategoria: () -> Unit,
    alEditarCategoria: (Categoria) -> Unit,
    alEliminarCategoria: (Categoria) -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var menuCategoriaAbierto by remember { mutableStateOf<Categoria?>(null) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Chip "Todas"
        val esTodas = categoriaSeleccionadaId == null
        val formaChip = RoundedCornerShape(12.dp)
        Box(
            modifier = Modifier
                .clip(formaChip)
                .background(if (esTodas) ColorAcento else ColorTarjetaAjustes)
                .then(
                    if (esTodas) {
                        Modifier.border(0.8.dp, ColorAcento, formaChip)
                    } else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, formaChip)
                    } else {
                        Modifier.border(0.8.dp, ColorSeparadorAjustes, formaChip)
                    }
                )
                .combinedClickable(
                    onClick = {
                        haptica.tic()
                        alSeleccionarCategoria(null)
                    }
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Layers,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    tint = if (esTodas) ColorSobreAcento else TextoSecundario
                )
                Text(
                    text = "Todas",
                    color = if (esTodas) ColorSobreAcento else TextoPrincipal,
                    fontSize = 13.sp,
                    fontWeight = if (esTodas) FontWeight.Bold else FontWeight.Medium
                )
                Text(
                    text = "$totalEntradas",
                    color = if (esTodas) ColorSobreAcento.copy(alpha = 0.8f) else TextoSecundario,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Chips de cada categoría
        categorias.forEach { cat ->
            val seleccionada = categoriaSeleccionadaId == cat.id
            val colorCat = IconosCategorias.parsearColorHex(cat.colorHex) ?: ColorAcento
            val conteo = conteoPorCategoria[cat.id] ?: 0

            Box(
                modifier = Modifier
                    .clip(formaChip)
                    .background(if (seleccionada) colorCat else ColorTarjetaAjustes)
                    .then(
                        if (seleccionada) {
                            Modifier.border(0.8.dp, colorCat, formaChip)
                        } else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                            Modifier.border(GrosorBorde, ColorBordeActual, formaChip)
                        } else {
                            Modifier.border(0.8.dp, ColorSeparadorAjustes, formaChip)
                        }
                    )
                    .combinedClickable(
                        onClick = {
                            haptica.tic()
                            alSeleccionarCategoria(if (seleccionada) null else cat.id)
                        },
                        onLongClick = {
                            haptica.tic()
                            menuCategoriaAbierto = cat
                        }
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = IconosCategorias.obtenerIcono(cat.icono),
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = if (seleccionada) ColorSobreAcento else colorCat
                    )
                    Text(
                        text = cat.nombre,
                        color = if (seleccionada) ColorSobreAcento else TextoPrincipal,
                        fontSize = 13.sp,
                        fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium
                    )
                    Text(
                        text = "$conteo",
                        color = if (seleccionada) ColorSobreAcento.copy(alpha = 0.8f) else TextoSecundario,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Menú contextual en pulsación prolongada
                MenuDesplegableBoveda(
                    expanded = menuCategoriaAbierto == cat,
                    onDismissRequest = { menuCategoriaAbierto = null },
                    modifier = Modifier.widthIn(min = 180.dp)
                ) {
                    ElementoMenuCompacto(
                        texto = "Editar categoría",
                        icono = Icons.Filled.Edit,
                        colorTexto = TextoPrincipal,
                        colorIcono = ColorAcento,
                        onClick = {
                            menuCategoriaAbierto = null
                            alEditarCategoria(cat)
                        }
                    )
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = "Eliminar",
                        icono = Icons.Filled.Delete,
                        colorTexto = Peligro,
                        colorIcono = Peligro,
                        onClick = {
                            menuCategoriaAbierto = null
                            alEliminarCategoria(cat)
                        }
                    )
                }
            }
        }

        // Botón añadir categoría
        Box(
            modifier = Modifier
                .clip(formaChip)
                .background(ColorTarjetaAjustes)
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, formaChip)
                    } else {
                        Modifier.border(0.8.dp, ColorSeparadorAjustes, formaChip)
                    }
                )
                .combinedClickable(
                    onClick = {
                        haptica.tic()
                        alCrearCategoria()
                    }
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Nueva categoría",
                    modifier = Modifier.size(15.dp),
                    tint = ColorAcento
                )
                Text(
                    text = "Nueva",
                    color = ColorAcento,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
