/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.IconosMarcas

@Composable
fun FilaGrupoSitio(
    clave: String,
    cantidad: Int,
    expandido: Boolean,
    alturaFila: Dp = 74.dp,
    tamanoMonograma: Int = 46,
    resaltado: Boolean = false,
    alAlternar: () -> Unit
) {
    val compacta = alturaFila.value <= 48f
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val tamanoIcono = if (compacta) 28 else if (alturaFila.value <= 64f) 34 else 38
    val infoMarca = remember(clave) {
        IconosMarcas.buscarInfo(clave, clave)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(alturaFila)
            .clip(forma)
            .background(if (resaltado) ColorAcento.copy(alpha = 0.16f) else ColorTarjetaAjustes)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") Modifier.border(GrosorBorde, ColorBordeActual, forma)
                else Modifier
            )
            .clickable { alAlternar() }
            .padding(horizontal = 14.dp, vertical = if (compacta) 3.dp else 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(tamanoIcono.dp)
                .clip(CircleShape)
                .background(ColorCampoAjustes),
            contentAlignment = Alignment.Center
        ) {
            if (infoMarca != null) {
                Icon(
                    painter = painterResource(infoMarca.drawableRes),
                    contentDescription = clave,
                    tint = if (infoMarca.esMulticolor) Color.Unspecified else (infoMarca.colorOficial ?: ColorIconosInternos),
                    modifier = Modifier.size((tamanoIcono * 0.52f).dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Dns,
                    contentDescription = null,
                    tint = ColorIconosInternos,
                    modifier = Modifier.size((tamanoIcono * 0.52f).dp)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = clave,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = if (resaltado) ColorAcento else TextoPrincipal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "$cantidad ${if (cantidad == 1) "cuenta" else "cuentas"}",
            style = MaterialTheme.typography.labelSmall,
            color = TextoSecundario,
            maxLines = 1
        )
        Spacer(Modifier.width(16.dp))
        Icon(
            imageVector = if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = if (expandido) "Contraer" else "Expandir",
            tint = if (resaltado) ColorAcento else ColorIconosInternos
        )
    }
}
