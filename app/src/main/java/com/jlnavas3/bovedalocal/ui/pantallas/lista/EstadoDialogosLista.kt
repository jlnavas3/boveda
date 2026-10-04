package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jlnavas3.bovedalocal.data.Coleccion
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
    var asignarColecciones by mutableStateOf(false)
    var crearColeccion by mutableStateOf(false)
    var coleccionParaEditar by mutableStateOf<Coleccion?>(null)
    var coleccionParaEliminar by mutableStateOf<Coleccion?>(null)

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
