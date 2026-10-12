/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.categorias

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

/**
 * Dialogo de confirmacion para eliminar una [Categoria].
 */
@Composable
fun DialogoEliminarCategoria(
    categoria: Categoria,
    cantidadEntradasVinculadas: Int,
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    val mensaje = if (cantidadEntradasVinculadas > 0) {
        "¿Deseas eliminar la categoría '${categoria.nombre}'?\n\nHay $cantidadEntradasVinculadas entrada(s) en esta categoría. Las credenciales no se borrarán, solo quedarán sin esta categoría."
    } else {
        "¿Deseas eliminar la categoría '${categoria.nombre}'?\n\nEsta acción no se puede deshacer."
    }

    DialogoConfirmacionBoveda(
        titulo = "Eliminar categoría",
        mensaje = mensaje,
        textoConfirmar = "Eliminar",
        textoCancelar = "Cancelar",
        alConfirmar = alConfirmar,
        alDescartar = alDescartar,
        tipoConfirmacion = TipoBotonTexto.PELIGRO,
        iconoHeader = Icons.Filled.Delete
    )
}
