package com.jlnavas3.bovedalocal.ui.pantallas.papelera

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

/**
 * Diálogos modales para la papelera: vaciar todo, borrar definitivamente y resolver conflictos de restauración.
 */
@Composable
fun DialogosPapelera(
    confirmarVaciar: Boolean,
    cantidadPapelera: Int,
    aBorrarDefinitivo: Entrada?,
    conflictoRestaurar: Entrada?,
    alDescartarVaciar: () -> Unit,
    alConfirmarVaciar: () -> Unit,
    alDescartarBorrarDefinitivo: () -> Unit,
    alConfirmarBorrarDefinitivo: (Entrada) -> Unit,
    alDescartarConflicto: () -> Unit,
    alSustituirConflicto: (Entrada) -> Unit,
    alDuplicarConflicto: (Entrada) -> Unit
) {
    if (confirmarVaciar) {
        DialogoConfirmacionBoveda(
            titulo = "¿Vaciar toda la papelera?",
            mensaje = "Se destruirán definitivamente todas las $cantidadPapelera entradas. Esta acción no se puede deshacer.",
            textoConfirmar = "Vaciar definitivamente",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = alConfirmarVaciar,
            alDescartar = alDescartarVaciar
        )
    }

    aBorrarDefinitivo?.let { entrada ->
        DialogoConfirmacionBoveda(
            titulo = "¿Borrar definitivamente?",
            mensaje = "Se destruirá permanentemente \"${entrada.titulo.ifBlank { "Sin título" }}\". Esta acción no se puede deshacer.",
            textoConfirmar = "Destruir",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = { alConfirmarBorrarDefinitivo(entrada) },
            alDescartar = alDescartarBorrarDefinitivo
        )
    }

    conflictoRestaurar?.let { entrada ->
        DialogoConflictoRestaurar(
            entrada = entrada,
            alCerrar = alDescartarConflicto,
            alSustituir = { alSustituirConflicto(entrada) },
            alDuplicar = { alDuplicarConflicto(entrada) }
        )
    }
}
