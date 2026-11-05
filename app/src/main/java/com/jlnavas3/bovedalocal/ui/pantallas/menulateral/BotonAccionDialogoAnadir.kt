/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.menulateral

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Barra inferior de acciones del diálogo de añadir a la barra lateral:
 * - Si no hay elementos seleccionados: muestra "Cerrar".
 * - Si hay elementos seleccionados con '+': muestra botón primario "Agregar" con "(N)" más pequeño y centrado,
 *   junto al botón "Cancelar".
 */
@Composable
fun BotonAccionDialogoAnadir(
    cantidadSeleccionados: Int,
    alConfirmar: () -> Unit,
    alCerrar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (cantidadSeleccionados > 0) {
            TextButton(onClick = alCerrar) {
                Text(
                    text = "Cancelar",
                    color = ColorAjusteGris,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.width(8.dp))

            ElevatedButton(
                onClick = alConfirmar,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = ColorAcento,
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Agregar",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "($cantidadSeleccionados)",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        } else {
            TextButton(onClick = alCerrar) {
                Text(
                    text = "Cerrar",
                    color = ColorAjusteGris,
                    fontSize = 13.5.sp
                )
            }
        }
    }
}
