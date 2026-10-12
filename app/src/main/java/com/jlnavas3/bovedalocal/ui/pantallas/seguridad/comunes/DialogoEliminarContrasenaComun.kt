/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.seguridad.comunes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun DialogoEliminarContrasenaComun(
    clave: String,
    alConfirmar: () -> Unit,
    alCerrar: () -> Unit
) {
    DialogoBoveda(
        onDismissRequest = alCerrar,
        confirmButton = {
            TextButton(onClick = alConfirmar) {
                Text("Excluir", color = Peligro, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = alCerrar) {
                Text("Cancelar", color = TextoSecundario)
            }
        },
        icon = { Icon(Icons.Filled.Delete, contentDescription = null, tint = Peligro) },
        title = { Text("¿Permitir esta contraseña?", color = TextoPrincipal, fontWeight = FontWeight.Bold) },
        text = {
            Text(
                "La contraseña \"$clave\" ya no será marcada como insegura ni común en la auditoría de salud.",
                color = TextoSecundario
            )
        }
    )
}
