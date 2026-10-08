package com.jlnavas3.bovedalocal.ui.pantallas.categorias

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Categoria
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.ChipBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FilaSeccionColapsableEdicion
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

@Composable
fun SeccionCategoriasEdicion(
    categoriasDisponibles: List<Categoria>,
    categoriasSeleccionadas: List<String>,
    alCambiarCategorias: (List<String>) -> Unit,
    alCrearNuevaCategoria: () -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarBottomSheet by remember { mutableStateOf(false) }

    val seleccionadas = remember(categoriasDisponibles, categoriasSeleccionadas) {
        categoriasDisponibles.filter { categoriasSeleccionadas.contains(it.id) }
    }

    var expandido by remember { mutableStateOf(seleccionadas.isNotEmpty()) }

    val resumen = when {
        seleccionadas.isEmpty() -> "Sin categorías asignadas"
        seleccionadas.size == 1 -> seleccionadas[0].nombre
        else -> "${seleccionadas.size} categorías asignadas"
    }

    FilaSeccionColapsableEdicion(
        icono = Icons.Filled.Folder,
        colorIcono = Color(0xFF4CAF50),
        titulo = "Categorías",
        resumen = resumen,
        insigniaTexto = if (seleccionadas.isNotEmpty()) "${seleccionadas.size}" else null,
        expandido = expandido,
        alAlternarExpandido = { expandido = !expandido },
        modifier = modifier
    ) {
        if (seleccionadas.isEmpty()) {
            BotonBorde(
                texto = if (categoriasDisponibles.isEmpty()) "Crear categoría" else "Asignar categorías (${categoriasDisponibles.size})",
                icono = Icons.Filled.Folder,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (categoriasDisponibles.isEmpty()) {
                    alCrearNuevaCategoria()
                } else {
                    mostrarBottomSheet = true
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                seleccionadas.forEach { cat ->
                    val colorPropio = IconosCategorias.parsearColorHex(cat.colorHex) ?: ColorAcento
                    val icono = IconosCategorias.obtenerIcono(cat.icono)

                    ChipBoveda(
                        texto = cat.nombre,
                        seleccionado = true,
                        colorBase = colorPropio,
                        icono = icono,
                        alRemover = {
                            alCambiarCategorias(categoriasSeleccionadas - cat.id)
                        },
                        alPulsar = {
                            mostrarBottomSheet = true
                        }
                    )
                }

                ChipBoveda(
                    texto = "Añadir",
                    icono = Icons.Filled.Add,
                    colorTextoPersonalizado = ColorAcento,
                    alPulsar = { mostrarBottomSheet = true }
                )
            }
        }
    }

    BottomSheetSeleccionCategorias(
        visible = mostrarBottomSheet,
        categoriasDisponibles = categoriasDisponibles,
        categoriasSeleccionadas = categoriasSeleccionadas,
        alCambiarCategorias = alCambiarCategorias,
        alCrearNuevaCategoria = {
            mostrarBottomSheet = false
            alCrearNuevaCategoria()
        },
        alCerrar = { mostrarBottomSheet = false }
    )
}

