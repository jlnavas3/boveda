package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraInferiorSeleccion
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente de la barra inferior de selección para la pantalla de Salud.
 */
@Composable
fun BarraAccionesSeleccionSalud(
    seleccionados: Set<String>,
    entradas: List<Entrada>,
    haptica: Haptica,
    alAlternarFavoritos: (Set<String>) -> Unit,
    alComparar: (String, List<String>) -> Unit,
    alAbrirBorrado: () -> Unit,
    modifier: Modifier = Modifier
) {
    val itemsSeleccionados = remember(entradas, seleccionados) {
        entradas.filter { seleccionados.contains(it.id) }
    }
    val todosSonFavoritos = remember(itemsSeleccionados) {
        itemsSeleccionados.isNotEmpty() && itemsSeleccionados.all { it.favorito }
    }

    BarraInferiorSeleccion(
        cantidad = seleccionados.size,
        todosSonFavoritos = todosSonFavoritos,
        alAlternarFavoritos = {
            haptica.exito()
            alAlternarFavoritos(seleccionados)
        },
        alComparar = {
            haptica.toque()
            val primera = seleccionados.first()
            alComparar(primera, seleccionados.toList())
        },
        alBorrar = {
            haptica.error()
            alAbrirBorrado()
        },
        modifier = modifier
    )
}
