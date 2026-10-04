package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoBorrarSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoOrdenacionLista
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoRenombrarSeleccion
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Orquestador microgranular de diálogos modales para la pantalla del autenticador TOTP.
 */
@Composable
fun DialogosAutenticador(
    mostrarDialogoOrdenacion: Boolean,
    criterioOrdenacion: CriterioOrdenacion,
    alSeleccionarCriterio: (CriterioOrdenacion) -> Unit,
    alCerrarOrdenacion: () -> Unit,
    dialogoComoFunciona: Boolean,
    alDescartarComoFunciona: () -> Unit,
    dialogoBorrarSeleccion: Boolean,
    cantidadSeleccionados: Int,
    alConfirmarBorrado: () -> Unit,
    alDescartarBorrado: () -> Unit,
    dialogoRenombrarSeleccion: Boolean,
    textoNuevoTitulo: String,
    alCambiarTextoRenombrar: (String) -> Unit,
    alConfirmarRenombrado: () -> Unit,
    alDescartarRenombrado: () -> Unit,
    haptica: Haptica
) {
    if (mostrarDialogoOrdenacion) {
        DialogoOrdenacionLista(
            criterioActual = criterioOrdenacion,
            alSeleccionarCriterio = { crit ->
                haptica.tic()
                alSeleccionarCriterio(crit)
            },
            alCerrar = alCerrarOrdenacion
        )
    }

    if (dialogoComoFunciona) {
        DialogoComoFuncionaTotp(
            alDescartar = alDescartarComoFunciona
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
}
