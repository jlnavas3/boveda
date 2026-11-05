/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Barra inferior fija de acciones para confirmar o cancelar la importación selectiva.
 */
@Composable
fun BarraAccionesImportarSelectivo(
    totalSeleccionados: Int,
    alImportar: () -> Unit,
    alCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = ColorAjustesFondo,
        modifier = modifier.fillMaxWidth()
    ) {
        androidx.compose.foundation.layout.Column {
            HorizontalDivider(
                thickness = 0.8.dp,
                color = ColorSeparadorAjustes
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = alCancelar,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = FormaBoton,
                    border = BorderStroke(1.dp, ColorSeparadorAjustes),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextoPrincipal
                    )
                ) {
                    Text(
                        text = "Cancelar",
                        fontSize = (14 * EscalaTexto).sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(
                    onClick = alImportar,
                    enabled = totalSeleccionados > 0,
                    modifier = Modifier
                        .weight(1.5f)
                        .height(44.dp),
                    shape = FormaBoton,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorAcento,
                        contentColor = ColorSobreAcento,
                        disabledContainerColor = ColorAcento.copy(alpha = 0.35f),
                        disabledContentColor = ColorSobreAcento.copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = if (totalSeleccionados > 0) "Importar ($totalSeleccionados)" else "Importar",
                        fontSize = (14 * EscalaTexto).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
