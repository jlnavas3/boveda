package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraInferiorSeleccion
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente de la barra inferior de selección por lote para cuentas de autenticador TOTP.
 */
@Composable
fun BarraAccionesSeleccionAutenticador(
    seleccionados: Set<String>,
    conTotp: List<Entrada>,
    haptica: Haptica,
    alAlternarFavoritos: (Set<String>) -> Unit,
    alComparar: (String, List<String>) -> Unit,
    alRespaldar: (String) -> Unit,
    alRenombrar: (String) -> Unit,
    alBorrar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val itemsSeleccionados = remember(conTotp, seleccionados) {
        conTotp.filter { seleccionados.contains(it.id) }
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
        alRenombrar = {
            haptica.tic()
            val primerSeleccionado = conTotp.find { it.id == seleccionados.firstOrNull() }
            val nuevoTitulo = primerSeleccionado?.titulo ?: ""
            alRenombrar(nuevoTitulo)
        },
        alBorrar = {
            haptica.error()
            alBorrar()
        },
        modifier = modifier
    )
}
