package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.util.Haptica

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

/**
 * Conecta y orquesta todos los diálogos modales de PantallaLista con VaultViewModel.
 */
@Composable
fun DialogosPantallaLista(
    dialogos: EstadoDialogosLista,
    vm: VaultViewModel,
    estado: EstadoBoveda,
    filtroActual: TipoEntrada?,
    criterioActual: CriterioOrdenacion,
    estadoSeleccion: EstadoSeleccionLista,
    entradas: List<Entrada>,
    colecciones: List<Coleccion>,
    haptica: Haptica
) {
    DialogosSeleccionAcciones(
        mostrarDialogoFiltros = dialogos.mostrarFiltros,
        filtroActual = filtroActual,
        alSeleccionarTipoFiltro = { tipo ->
            haptica.tic()
            vm.filtrarPorTipo(tipo)
        },
        alDescartarFiltros = { dialogos.mostrarFiltros = false },
        mostrarDialogoOrdenacion = dialogos.mostrarOrdenacion,
        criterioActual = criterioActual,
        alSeleccionarCriterioOrdenacion = { crit ->
            haptica.tic()
            vm.cambiarCriterioOrdenacion(crit)
        },
        alDescartarOrdenacion = { dialogos.mostrarOrdenacion = false },
        dialogoBorrarSeleccion = dialogos.borrarSeleccion,
        cantidadSeleccionados = estadoSeleccion.seleccionados.size,
        alConfirmarBorrarSeleccion = {
            dialogos.borrarSeleccion = false
            val idsABorrar = estadoSeleccion.seleccionados
            estadoSeleccion.salirDeSeleccion()
            vm.eliminarVarias(idsABorrar)
        },
        alDescartarBorrarSeleccion = { dialogos.borrarSeleccion = false },
        dialogoRenombrarSeleccion = dialogos.renombrarSeleccion,
        textoNuevoTitulo = dialogos.textoNuevoTitulo,
        alCambiarTextoRenombrar = { dialogos.textoNuevoTitulo = it },
        alConfirmarRenombrarSeleccion = {
            val nuevo = dialogos.textoNuevoTitulo.trim()
            if (nuevo.isNotBlank()) {
                dialogos.renombrarSeleccion = false
                val idsARenombrar = estadoSeleccion.seleccionados
                estadoSeleccion.salirDeSeleccion()
                vm.renombrarVarias(idsARenombrar, nuevo)
                haptica.exito()
            }
        },
        alDescartarRenombrarSeleccion = { dialogos.renombrarSeleccion = false },
        entradasParaTransferirCxf = dialogos.entradasParaTransferirCxf,
        alDescartarTransferirCxf = { dialogos.entradasParaTransferirCxf = null },
        mostrarDialogoExportarCxf = dialogos.exportarCxf,
        estado = estado,
        alDescartarExportarCxf = { dialogos.exportarCxf = false },
        mostrarDialogoAsignarColecciones = dialogos.asignarColecciones,
        seleccionados = estadoSeleccion.seleccionados,
        entradas = entradas,
        colecciones = colecciones,
        alCrearNuevaColeccion = { dialogos.crearColeccion = true },
        alGuardarAsignarColecciones = { idsAgregar, idsQuitar ->
            vm.asignarColeccionesAEntradas(estadoSeleccion.seleccionados, idsAgregar, idsQuitar)
            dialogos.asignarColecciones = false
            estadoSeleccion.salirDeSeleccion()
            haptica.exito()
        },
        alDescartarAsignarColecciones = { dialogos.asignarColecciones = false },
        mostrarDialogoCrearColeccion = dialogos.crearColeccion,
        coleccionParaEditar = dialogos.coleccionParaEditar,
        alGuardarColeccion = { nombre, icono, colorHex ->
            val colEdit = dialogos.coleccionParaEditar
            if (colEdit != null) {
                vm.actualizarColeccion(colEdit.id, nombre, icono, colorHex)
            } else {
                vm.crearColeccion(nombre, icono, colorHex)
            }
            dialogos.crearColeccion = false
            dialogos.coleccionParaEditar = null
            haptica.exito()
        },
        alDescartarEditarCrearColeccion = {
            dialogos.crearColeccion = false
            dialogos.coleccionParaEditar = null
        },
        coleccionParaEliminar = dialogos.coleccionParaEliminar,
        alConfirmarEliminarColeccion = { col ->
            val id = col.id
            dialogos.coleccionParaEliminar = null
            vm.eliminarColeccion(id)
            haptica.exito()
        },
        alDescartarEliminarColeccion = { dialogos.coleccionParaEliminar = null }
    )
}
