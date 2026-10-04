package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.util.Haptica

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
