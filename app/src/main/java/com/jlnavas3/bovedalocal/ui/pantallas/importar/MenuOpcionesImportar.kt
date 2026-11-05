/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Menú desplegable de 3 puntos en la barra superior de importación selectiva.
 */
@Composable
fun MenuOpcionesImportar(
    expandido: Boolean,
    alCerrar: () -> Unit,
    modoDetallado: Boolean,
    alAlternarModoDetallado: () -> Unit,
    alSeleccionarTodo: () -> Unit,
    alDeseleccionarTodo: () -> Unit,
    modifier: Modifier = Modifier
) {
    MenuDesplegableBoveda(
        expanded = expandido,
        onDismissRequest = alCerrar,
        modifier = modifier
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = if (modoDetallado) "Ocultar valores de la copia" else "Ver valores de la copia",
                    fontSize = 14.sp,
                    color = TextoPrincipal
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = if (modoDetallado) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = null,
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(20.dp)
                )
            },
            onClick = {
                alCerrar()
                alAlternarModoDetallado()
            }
        )

        SeparadorOpcionMenu()

        DropdownMenuItem(
            text = {
                Text(
                    text = "Seleccionar todo",
                    fontSize = 14.sp,
                    color = TextoPrincipal
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.SelectAll,
                    contentDescription = null,
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(20.dp)
                )
            },
            onClick = {
                alCerrar()
                alSeleccionarTodo()
            }
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = "Deseleccionar todo",
                    fontSize = 14.sp,
                    color = TextoPrincipal
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Deselect,
                    contentDescription = null,
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(20.dp)
                )
            },
            onClick = {
                alCerrar()
                alDeseleccionarTodo()
            }
        )
    }
}
