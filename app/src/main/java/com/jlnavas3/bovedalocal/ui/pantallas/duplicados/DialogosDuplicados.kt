package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

/**
 * Orquestador microgranular de diálogos modales para la pantalla de duplicados:
 * limpieza masiva en 1 toque y borrado de selección múltiple.
 */
@Composable
fun DialogosDuplicados(
    confirmarLimpiezaMasiva: Boolean,
    totalSobrantesIdenticas: Int,
    alConfirmarLimpiezaMasiva: () -> Unit,
    alDescartarLimpiezaMasiva: () -> Unit,
    confirmarBorradoSeleccion: Boolean,
    cantidadSeleccionados: Int,
    alConfirmarBorradoSeleccion: () -> Unit,
    alDescartarBorradoSeleccion: () -> Unit
) {
    if (confirmarLimpiezaMasiva) {
        DialogoConfirmacionBoveda(
            titulo = "Limpiar $totalSobrantesIdenticas copias idénticas",
            mensaje = "Se enviarán $totalSobrantesIdenticas entradas duplicadas a la papelera, conservando automáticamente la copia más completa y reciente de cada servicio. Podrás recuperarlas de la papelera en los próximos 30 días si lo necesitas.",
            textoConfirmar = "Limpiar ahora",
            tipoConfirmacion = TipoBotonTexto.PRIMARIO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = alConfirmarLimpiezaMasiva,
            alDescartar = alDescartarLimpiezaMasiva
        )
    }

    if (confirmarBorradoSeleccion) {
        DialogoConfirmacionBoveda(
            titulo = "¿Eliminar $cantidadSeleccionados entrada${if (cantidadSeleccionados > 1) "s" else ""}?",
            mensaje = "Las entradas seleccionadas se enviarán a la papelera. Podrás recuperarlas en los próximos 30 días si lo necesitas.",
            textoConfirmar = "Eliminar",
            tipoConfirmacion = TipoBotonTexto.PELIGRO,
            iconoHeader = Icons.Filled.Delete,
            alConfirmar = alConfirmarBorradoSeleccion,
            alDescartar = alDescartarBorradoSeleccion
        )
    }
}
