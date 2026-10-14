/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.seguridad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris

/**
 * Microcomponente que renderiza un dato sensible protegido visualmente con
 * opción de botón de revelado individual (ojo 👁️) y blindaje semántico.
 */
@Composable
fun DatoSensibleConRevelado(
    texto: String,
    oculto: Boolean,
    estilo: String = "desenfoque",
    alAlternarRevelado: (() -> Unit)? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    estiloTexto: TextStyle = LocalTextStyle.current,
    colorTexto: Color = Color.Unspecified,
    colorIcono: Color = ColorAjusteGris,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    tamanoIconoDp: Dp = 22.dp,
    descripcionAccesible: String = "Alternar visibilidad del dato sensible"
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextoSeguroVisual(
            texto = texto,
            oculto = oculto,
            estilo = estilo,
            estiloTexto = estiloTexto,
            colorTexto = colorTexto,
            maxLines = maxLines,
            overflow = overflow,
            modifier = Modifier.weight(1f)
        )

        if (alAlternarRevelado != null) {
            val contexto = androidx.compose.ui.platform.LocalContext.current
            val haptica = androidx.compose.runtime.remember { com.jlnavas3.bovedalocal.util.Haptica(contexto) }
            IconButton(
                onClick = {
                    haptica.toque()
                    alAlternarRevelado()
                },
                modifier = Modifier.size(tamanoIconoDp)
            ) {
                Icon(
                    imageVector = if (oculto) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (oculto) "Mostrar $descripcionAccesible" else "Ocultar $descripcionAccesible",
                    tint = colorIcono,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
