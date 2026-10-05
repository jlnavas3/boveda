package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.data.Categoria
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
    categorias: List<Categoria>,
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
        mostrarDialogoAsignarCategorias = dialogos.asignarCategorias,
        seleccionados = estadoSeleccion.seleccionados,
        entradas = entradas,
        categorias = categorias,
        alCrearNuevaCategoria = { dialogos.crearCategoria = true },
        alGuardarAsignarCategorias = { idsAgregar, idsQuitar ->
            vm.asignarCategoriasAEntradas(estadoSeleccion.seleccionados, idsAgregar, idsQuitar)
            dialogos.asignarCategorias = false
            estadoSeleccion.salirDeSeleccion()
            haptica.exito()
        },
        alDescartarAsignarCategorias = { dialogos.asignarCategorias = false },
        mostrarDialogoCrearCategoria = dialogos.crearCategoria,
        categoriaParaEditar = dialogos.categoriaParaEditar,
        alGuardarCategoria = { nombre, icono, colorHex ->
            val catEdit = dialogos.categoriaParaEditar
            if (catEdit != null) {
                vm.actualizarCategoria(catEdit.id, nombre, icono, colorHex)
            } else {
                vm.crearCategoria(nombre, icono, colorHex)
            }
            dialogos.crearCategoria = false
            dialogos.categoriaParaEditar = null
            haptica.exito()
        },
        alDescartarEditarCrearCategoria = {
            dialogos.crearCategoria = false
            dialogos.categoriaParaEditar = null
        },
        categoriaParaEliminar = dialogos.categoriaParaEliminar,
        alConfirmarEliminarCategoria = { cat ->
            val id = cat.id
            dialogos.categoriaParaEliminar = null
            vm.eliminarCategoria(id)
            haptica.exito()
        },
        alDescartarEliminarCategoria = { dialogos.categoriaParaEliminar = null }
    )
}
