/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

@Composable
fun DialogoPostImportacionTitulos(
    totalImportadas: Int,
    cantidadComprometidas: Int,
    alNormalizar: () -> Unit,
    alDescartar: () -> Unit
) {
    DialogoConfirmacionBoveda(
        titulo = "¡Importación completada!",
        mensaje = "Se agregaron $totalImportadas credenciales. Notamos que $cantidadComprometidas títulos son enlaces crudos o poco legibles. ¿Deseas revisarlos con el normalizador ahora?",
        textoConfirmar = "Normalizar títulos",
        textoCancelar = "Más tarde",
        alConfirmar = alNormalizar,
        alDescartar = alDescartar,
        tipoConfirmacion = TipoBotonTexto.PRIMARIO,
        iconoHeader = Icons.Filled.Spellcheck
    )
}
