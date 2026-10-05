package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.DialogoAsignarCategorias
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.DialogoEditarCategoria
import com.jlnavas3.bovedalocal.ui.pantallas.cxf.DialogoExportacionDirectaCxf

/**
 * Gestor modular de diálogos de acción para la pantalla principal y modo de selección múltiple.
 */
@Composable
fun DialogosSeleccionAcciones(
    mostrarDialogoFiltros: Boolean,
    filtroActual: TipoEntrada?,
    alSeleccionarTipoFiltro: (TipoEntrada?) -> Unit,
    alDescartarFiltros: () -> Unit,
    mostrarDialogoOrdenacion: Boolean,
    criterioActual: CriterioOrdenacion,
    alSeleccionarCriterioOrdenacion: (CriterioOrdenacion) -> Unit,
    alDescartarOrdenacion: () -> Unit,
    dialogoBorrarSeleccion: Boolean,
    cantidadSeleccionados: Int,
    alConfirmarBorrarSeleccion: () -> Unit,
    alDescartarBorrarSeleccion: () -> Unit,
    dialogoRenombrarSeleccion: Boolean,
    textoNuevoTitulo: String,
    alCambiarTextoRenombrar: (String) -> Unit,
    alConfirmarRenombrarSeleccion: () -> Unit,
    alDescartarRenombrarSeleccion: () -> Unit,
    entradasParaTransferirCxf: List<Entrada>?,
    alDescartarTransferirCxf: () -> Unit,
    mostrarDialogoExportarCxf: Boolean,
    estado: EstadoBoveda,
    alDescartarExportarCxf: () -> Unit,
    mostrarDialogoAsignarCategorias: Boolean,
    seleccionados: Set<String>,
    entradas: List<Entrada>,
    categorias: List<Categoria>,
    alCrearNuevaCategoria: () -> Unit,
    alGuardarAsignarCategorias: (Set<String>, Set<String>) -> Unit,
    alDescartarAsignarCategorias: () -> Unit,
    mostrarDialogoCrearCategoria: Boolean,
    categoriaParaEditar: Categoria?,
    alGuardarCategoria: (String, String, String?) -> Unit,
    alDescartarEditarCrearCategoria: () -> Unit,
    categoriaParaEliminar: Categoria?,
    alConfirmarEliminarCategoria: (Categoria) -> Unit,
    alDescartarEliminarCategoria: () -> Unit
) {
    if (mostrarDialogoFiltros) {
        DialogoFiltrosLista(
            filtroActual = filtroActual,
            alSeleccionarTipo = alSeleccionarTipoFiltro,
            alCerrar = alDescartarFiltros
        )
    }

    if (mostrarDialogoOrdenacion) {
        DialogoOrdenacionLista(
            criterioActual = criterioActual,
            alSeleccionarCriterio = alSeleccionarCriterioOrdenacion,
            alCerrar = alDescartarOrdenacion
        )
    }

    if (dialogoBorrarSeleccion) {
        DialogoBorrarSeleccion(
            cantidad = cantidadSeleccionados,
            alConfirmar = alConfirmarBorrarSeleccion,
            alDescartar = alDescartarBorrarSeleccion
        )
    }

    if (dialogoRenombrarSeleccion) {
        DialogoRenombrarSeleccion(
            cantidad = cantidadSeleccionados,
            textoNuevoTitulo = textoNuevoTitulo,
            alCambiarTexto = alCambiarTextoRenombrar,
            alConfirmar = alConfirmarRenombrarSeleccion,
            alDescartar = alDescartarRenombrarSeleccion
        )
    }

    entradasParaTransferirCxf?.let { entradasSeleccionadas ->
        DialogoExportacionDirectaCxf(
            entradas = entradasSeleccionadas,
            esSeleccionPersonalizada = true,
            alCerrar = alDescartarTransferirCxf
        )
    }

    if (mostrarDialogoExportarCxf && estado is EstadoBoveda.Desbloqueada) {
        DialogoExportacionDirectaCxf(
            entradas = estado.entradas,
            esSeleccionPersonalizada = false,
            alCerrar = alDescartarExportarCxf
        )
    }

    if (mostrarDialogoAsignarCategorias) {
        val seleccionadosLista = remember(seleccionados, entradas) {
            entradas.filter { seleccionados.contains(it.id) }
        }
        DialogoAsignarCategorias(
            entradasSeleccionadas = seleccionadosLista,
            categoriasDisponibles = categorias,
            alCrearNuevaCategoria = alCrearNuevaCategoria,
            alGuardar = alGuardarAsignarCategorias,
            alDescartar = alDescartarAsignarCategorias
        )
    }

    if (mostrarDialogoCrearCategoria || categoriaParaEditar != null) {
        DialogoEditarCategoria(
            categoriaAEditar = categoriaParaEditar,
            alGuardar = alGuardarCategoria,
            alDescartar = alDescartarEditarCrearCategoria
        )
    }

    categoriaParaEliminar?.let { cat ->
        DialogoConfirmacionBoveda(
            titulo = "¿Eliminar categoría?",
            mensaje = "Se eliminará la categoría '${cat.nombre}'. Las credenciales asociadas no se borrarán.",
            textoConfirmar = "Eliminar",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = { alConfirmarEliminarCategoria(cat) },
            alDescartar = alDescartarEliminarCategoria
        )
    }
}
