package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada

/**
 * Mantiene el estado visible de los diversos diálogos modales de PantallaLista.
 */
@Stable
class EstadoDialogosLista {
    var mostrarFiltros by mutableStateOf(false)
    var mostrarOrdenacion by mutableStateOf(false)
    var borrarSeleccion by mutableStateOf(false)
    var exportarCxf by mutableStateOf(false)
    var entradasParaTransferirCxf by mutableStateOf<List<Entrada>?>(null)
    var renombrarSeleccion by mutableStateOf(false)
    var textoNuevoTitulo by mutableStateOf("")
    var asignarCategorias by mutableStateOf(false)
    var crearCategoria by mutableStateOf(false)
    var categoriaParaEditar by mutableStateOf<Categoria?>(null)
    var categoriaParaEliminar by mutableStateOf<Categoria?>(null)

    fun iniciarRenombrar(tituloActual: String) {
        textoNuevoTitulo = tituloActual
        renombrarSeleccion = true
    }

    fun prepararTransferirCxf(entradas: List<Entrada>) {
        entradasParaTransferirCxf = entradas
    }
}

@Composable
fun rememberEstadoDialogosLista(): EstadoDialogosLista {
    return remember { EstadoDialogosLista() }
}
