/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.autocompletado

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Composable micro desacoplado para activar o desactivar la copia automática del código TOTP (2FA)
 * al portapapeles tras realizar un autocompletado en el sistema Android.
 */
@Composable
fun FilaAjusteAutofillCopiarTotp(
    activo: Boolean,
    mostrarIdsAjustes: Boolean,
    alCambiar: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ComponenteSwitch(
            titulo = "Copiar código 2FA / TOTP automáticamente",
            activo = activo,
            alCambiar = alCambiar,
            icono = Icons.Filled.Timer,
            colorIcono = Color(0xFF10B981),
            idFila = "04-HER-PSK-TOTP",
            mostrarId = mostrarIdsAjustes
        )
        Text(
            text = "Al rellenar usuario o contraseña en una app o sitio que requiera segundo factor, copia el código TOTP al portapapeles automáticamente y programa su limpieza segura.",
            style = MaterialTheme.typography.bodySmall,
            color = TextoSecundario,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}
