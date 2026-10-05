package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

/**
 * Diálogo de confirmación para eliminar una [Identidad].
 * Informa al usuario sobre cuántas cuentas están asociadas y aclara
 * que las credenciales no serán eliminadas, solo desvinculadas.
 */
@Composable
fun DialogoEliminarIdentidad(
    identidad: Identidad,
    cantidadEntradasVinculadas: Int,
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    val mensaje = if (cantidadEntradasVinculadas > 0) {
        "¿Deseas eliminar la identidad '${identidad.nombre}'?\n\nHay $cantidadEntradasVinculadas cuenta(s) vinculada(s). Las credenciales no se borrarán, solo quedarán sin identidad asociada."
    } else {
        "¿Deseas eliminar la identidad '${identidad.nombre}'?\n\nEsta acción no se puede deshacer."
    }

    DialogoConfirmacionBoveda(
        titulo = "Eliminar identidad",
        mensaje = mensaje,
        textoConfirmar = "Eliminar",
        textoCancelar = "Cancelar",
        alConfirmar = alConfirmar,
        alDescartar = alDescartar,
        tipoConfirmacion = TipoBotonTexto.PELIGRO,
        iconoHeader = Icons.Filled.Delete
    )
}
