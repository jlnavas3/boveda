/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.herramientas.diccionario

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
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
fun DialogoRestablecerDiccionario(
    idiomaSeleccionado: String,
    alConfirmar: () -> Unit,
    alCerrar: () -> Unit
) {
    DialogoBoveda(
        onDismissRequest = alCerrar,
        confirmButton = {
            TextButton(onClick = alConfirmar) {
                Text("Restablecer", color = Peligro, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = alCerrar) {
                Text("Cancelar", color = TextoSecundario)
            }
        },
        icon = { Icon(Icons.Filled.RestartAlt, contentDescription = null, tint = Peligro) },
        title = { Text("Restablecer diccionario", color = TextoPrincipal, fontWeight = FontWeight.Bold) },
        text = {
            Text(
                "Se borrarán las palabras añadidas y excluidas para el idioma " +
                    (if (idiomaSeleccionado == "es") "Español" else "Inglés") +
                    ", volviendo a la lista oficial original de la EFF.",
                color = TextoSecundario
            )
        }
    )
}
