/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.draw.alpha

/**
 * Fila atómica de opción seleccionable con indicador circular idéntico al listado principal.
 * Soporta modo normal (descripción compacta) y modo detallado (valores reales del respaldo con colores diferenciados).
 */
@Composable
fun FilaOpcionImportacion(
    titulo: String,
    descripcion: String,
    seleccionado: Boolean,
    alAlternar: () -> Unit,
    modifier: Modifier = Modifier,
    contador: Int? = null,
    textoDetallado: AnnotatedString? = null,
    modoDetallado: Boolean = false,
    habilitado: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = habilitado, onClick = alAlternar)
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .alpha(if (habilitado) 1f else 0.55f),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.padding(top = 2.dp)) {
            CheckCircularImportacion(
                seleccionado = seleccionado,
                habilitado = habilitado
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = titulo,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoPrincipal,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (contador != null) {
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "($contador)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextoSecundario
                    )
                }
            }

            if (modoDetallado && textoDetallado != null && textoDetallado.isNotEmpty()) {
                val formaDetalle = FormaPequena
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                        .clip(formaDetalle)
                        .background(ColorCampoAjustes)
                        .border(0.8.dp, ColorSeparadorAjustes, formaDetalle)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = textoDetallado,
                        fontSize = (11 * EscalaTexto).sp,
                        lineHeight = (15 * EscalaTexto).sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextoPrincipal.copy(alpha = 0.9f)
                    )
                }
            } else {
                Text(
                    text = descripcion,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextoSecundario,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
