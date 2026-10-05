package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ChipBoveda

/**
 * Barra horizontal deslizable de chips de categorías para exportación selectiva,
 * estandarizada con el componente molecular ChipBoveda.
 */
@Composable
fun ChipsCategoriasExportacion(
    categorias: List<CategoriaExportacion>,
    categoriaActivaId: String,
    alSeleccionarCategoria: (String) -> Unit,
    totalPorCategoria: (CategoriaExportacion) -> Int,
    onTic: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (categorias.size <= 1) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categorias.forEach { cat ->
            val seleccionada = cat.id == categoriaActivaId
            val cantCat = totalPorCategoria(cat)

            ChipBoveda(
                texto = cat.etiqueta,
                conteo = cantCat,
                seleccionado = seleccionada,
                colorBase = cat.color,
                icono = cat.icono,
                alPulsar = {
                    onTic()
                    alSeleccionarCategoria(cat.id)
                }
            )
        }
    }
}

