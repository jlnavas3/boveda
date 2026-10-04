package com.jlnavas3.bovedalocal.ui.pantallas.passkeys

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraInferiorSeleccion
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente que gestiona la barra inferior contextual de acciones cuando el usuario
 * selecciona una o más llaves de paso (Passkeys) en la lista.
 */
@Composable
fun BarraAccionesSeleccionPasskeys(
    seleccionados: Set<String>,
    todasLasPasskeys: List<Entrada>,
    haptica: Haptica,
    alAlternarFavoritos: (Set<String>) -> Unit,
    alComparar: (String, List<String>) -> Unit,
    alRespaldar: (String) -> Unit,
    alTransferirCxf: (List<Entrada>) -> Unit,
    alAbrirRenombrar: (String) -> Unit,
    alAbrirBorrado: () -> Unit
) {
    val itemsSeleccionados = remember(todasLasPasskeys, seleccionados) {
        todasLasPasskeys.filter { seleccionados.contains(it.id) }
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
        alRespaldar = {
            haptica.tic()
            val idsParam = seleccionados.joinToString(",")
            alRespaldar(idsParam)
        },
        alTransferirCxf = {
            haptica.tic()
            val copia = itemsSeleccionados.toList()
            alTransferirCxf(copia)
        },
        alRenombrar = {
            haptica.tic()
            val primerSeleccionado = todasLasPasskeys.find { it.id == seleccionados.firstOrNull() }
            val nuevoTitulo = primerSeleccionado?.titulo ?: ""
            alAbrirRenombrar(nuevoTitulo)
        },
        alBorrar = {
            haptica.error()
            alAbrirBorrado()
        }
    )
}
