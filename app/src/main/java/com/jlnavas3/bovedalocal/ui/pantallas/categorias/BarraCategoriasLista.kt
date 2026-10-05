package com.jlnavas3.bovedalocal.ui.pantallas.categorias

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Layers
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.ui.componentes.ChipBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica


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
        ChipBoveda(
            texto = "Todas",
            conteo = totalEntradas,
            seleccionado = esTodas,
            icono = Icons.Filled.Layers,
            alPulsar = {
                haptica.tic()
                alSeleccionarCategoria(null)
            }
        )

        // Chips de cada categoría
        categorias.forEach { cat ->
            val seleccionada = categoriaSeleccionadaId == cat.id
            val colorCat = IconosCategorias.parsearColorHex(cat.colorHex) ?: ColorAcento
            val conteo = conteoPorCategoria[cat.id] ?: 0

            Box {
                ChipBoveda(
                    texto = cat.nombre,
                    conteo = conteo,
                    seleccionado = seleccionada,
                    colorBase = colorCat,
                    icono = IconosCategorias.obtenerIcono(cat.icono),
                    alPulsar = {
                        haptica.tic()
                        alSeleccionarCategoria(if (seleccionada) null else cat.id)
                    },
                    alPulsarProlongado = {
                        haptica.tic()
                        menuCategoriaAbierto = cat
                    }
                )

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
        ChipBoveda(
            texto = "Nueva",
            icono = Icons.Filled.Add,
            colorTextoPersonalizado = ColorAcento,
            alPulsar = {
                haptica.tic()
                alCrearCategoria()
            }
        )
    }
}
