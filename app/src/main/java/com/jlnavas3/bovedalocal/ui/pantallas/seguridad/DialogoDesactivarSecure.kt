/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.DialogoConfirmacionBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto

@Composable
fun DialogoDesactivarSecure(
    alConfirmar: () -> Unit,
    alDescartar: () -> Unit
) {
    DialogoConfirmacionBoveda(
        titulo = "¿Desactivar protección de pantalla?",
        mensaje = "Al desactivar FLAG_SECURE, las capturas de pantalla y la vista en aplicaciones recientes estarán permitidas. Tus datos sensibles podrían quedar expuestos ante aplicaciones espía o grabaciones de pantalla.",
        textoConfirmar = "Desactivar",
        tipoConfirmacion = TipoBotonTexto.PELIGRO,
        iconoHeader = Icons.Filled.Security,
        alConfirmar = alConfirmar,
        alDescartar = alDescartar
    )
}
