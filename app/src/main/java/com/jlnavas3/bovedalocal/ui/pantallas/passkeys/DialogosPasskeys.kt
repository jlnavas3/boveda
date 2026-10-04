package com.jlnavas3.bovedalocal.ui.pantallas.passkeys

import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.pantallas.cxf.DialogoExportacionDirectaCxf
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoBorrarSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoOrdenacionLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoRenombrarSeleccion
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Orquestador microgranular de diálogos modales para la pantalla de Passkeys.
 * Maneja ordenación, borrado masivo, renombrado por lote y exportación CXF.
 */
@Composable
fun DialogosPasskeys(
    mostrarDialogoOrdenacion: Boolean,
    criterioOrdenacion: CriterioOrdenacion,
    alSeleccionarCriterio: (CriterioOrdenacion) -> Unit,
    alCerrarOrdenacion: () -> Unit,
    dialogoBorrarSeleccion: Boolean,
    cantidadSeleccionados: Int,
    alConfirmarBorrado: () -> Unit,
    alDescartarBorrado: () -> Unit,
    dialogoRenombrarSeleccion: Boolean,
    textoNuevoTitulo: String,
    alCambiarTextoRenombrar: (String) -> Unit,
    alConfirmarRenombrado: () -> Unit,
    alDescartarRenombrado: () -> Unit,
    entradasParaTransferirCxf: List<Entrada>?,
    alCerrarTransferirCxf: () -> Unit,
    mostrarDialogoExportarCxf: Boolean,
    todasLasPasskeys: List<Entrada>,
    alCerrarExportarCxf: () -> Unit,
    haptica: Haptica
) {
    if (mostrarDialogoOrdenacion) {
        DialogoOrdenacionLista(
            criterioActual = criterioOrdenacion,
            alSeleccionarCriterio = { criterio ->
                haptica.tic()
                alSeleccionarCriterio(criterio)
            },
            alCerrar = alCerrarOrdenacion
        )
    }

    if (dialogoBorrarSeleccion) {
        DialogoBorrarSeleccion(
            cantidad = cantidadSeleccionados,
            alConfirmar = {
                haptica.exito()
                alConfirmarBorrado()
            },
            alDescartar = alDescartarBorrado
        )
    }

    if (dialogoRenombrarSeleccion) {
        DialogoRenombrarSeleccion(
            cantidad = cantidadSeleccionados,
            textoNuevoTitulo = textoNuevoTitulo,
            alCambiarTexto = alCambiarTextoRenombrar,
            alConfirmar = {
                haptica.exito()
                alConfirmarRenombrado()
            },
            alDescartar = alDescartarRenombrado
        )
    }

    entradasParaTransferirCxf?.let { passkeysSeleccionadas ->
        DialogoExportacionDirectaCxf(
            entradas = passkeysSeleccionadas,
            esSeleccionPersonalizada = true,
            alCerrar = alCerrarTransferirCxf
        )
    }

    if (mostrarDialogoExportarCxf) {
        DialogoExportacionDirectaCxf(
            entradas = todasLasPasskeys,
            esSeleccionPersonalizada = false,
            alCerrar = alCerrarExportarCxf
        )
    }
}
