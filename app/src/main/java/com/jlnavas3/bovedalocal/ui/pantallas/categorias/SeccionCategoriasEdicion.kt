package com.jlnavas3.bovedalocal.ui.pantallas.categorias

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.ui.componentes.ChipBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SeccionCategoriasEdicion(
    categoriasDisponibles: List<Categoria>,
    categoriasSeleccionadas: List<String>,
    alCambiarCategorias: (List<String>) -> Unit,
    alCrearNuevaCategoria: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TextoTitulo(texto = "Categorías")
        TextoSubtitulo(texto = "Organiza esta entrada en categorías personalizadas")

        Spacer(Modifier.height(8.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoriasDisponibles.forEach { cat ->
                val estaSeleccionada = categoriasSeleccionadas.contains(cat.id)
                val colorPropio = IconosCategorias.parsearColorHex(cat.colorHex) ?: ColorAcento
                val icono = IconosCategorias.obtenerIcono(cat.icono)

                ChipBoveda(
                    texto = cat.nombre,
                    seleccionado = estaSeleccionada,
                    colorBase = colorPropio,
                    icono = icono,
                    mostrarCheck = true,
                    alPulsar = {
                        val nuevaLista = if (estaSeleccionada) {
                            categoriasSeleccionadas - cat.id
                        } else {
                            categoriasSeleccionadas + cat.id
                        }
                        alCambiarCategorias(nuevaLista)
                    }
                )
            }

            // Chip para crear nueva categoría directamente
            ChipBoveda(
                texto = "Nueva",
                icono = Icons.Filled.Add,
                colorTextoPersonalizado = ColorAcento,
                alPulsar = alCrearNuevaCategoria
            )
        }
    }
}

