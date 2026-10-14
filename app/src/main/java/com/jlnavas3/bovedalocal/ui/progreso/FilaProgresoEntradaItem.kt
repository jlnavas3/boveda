/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.progreso

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.ItemProgresoEntrada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.IconosMarcas

/**
 * Microcomponente que renderiza una fila individual con icono, nombre,
 * subtítulo descriptivo y barra lineal individual de progreso.
 */
@Composable
fun FilaProgresoEntradaItem(
    item: ItemProgresoEntrada,
    modifier: Modifier = Modifier
) {
    val infoMarca = remember(item.titulo) {
        IconosMarcas.buscarInfo(item.titulo.lowercase(), item.titulo)
    }

    val progresoAnimado by animateFloatAsState(
        targetValue = item.progreso.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 250),
        label = "progresoFilaItem"
    )

    val formaFila = FormaPequena
    val formaCirculoOEsquina = if (CurvaturaEsquinas == 0.dp) RoundedCornerShape(0.dp) else CircleShape

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(formaFila)
            .background(Superficie)
            .border(
                width = GrosorBorde.coerceAtMost(1.dp),
                color = ColorBordeActual.copy(alpha = 0.35f),
                shape = formaFila
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icono de la marca o candado
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(22.dp)
                    .clip(formaCirculoOEsquina)
                    .background(SuperficieAlta)
            ) {
                if (infoMarca != null) {
                    Icon(
                        painter = painterResource(infoMarca.drawableRes),
                        contentDescription = item.titulo,
                        tint = if (infoMarca.esMulticolor) androidx.compose.ui.graphics.Color.Unspecified else (infoMarca.colorOficial ?: ColorAcento),
                        modifier = Modifier.size(13.dp)
                    )
                } else if (item.esCripto) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Cifrado",
                        tint = ColorAcento,
                        modifier = Modifier.size(12.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Key,
                        contentDescription = "Cuenta",
                        tint = ColorAcento,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.titulo,
                    color = TextoPrincipal,
                    fontSize = 11.5.sp,
                    lineHeight = 13.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (item.subtitulo.isNotBlank()) {
                    Text(
                        text = item.subtitulo,
                        color = TextoSecundario,
                        fontSize = 9.5.sp,
                        lineHeight = 11.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            if (item.completado || progresoAnimado >= 0.99f) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(15.dp)
                        .clip(formaCirculoOEsquina)
                        .background(Menta.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Completado",
                        tint = Menta,
                        modifier = Modifier.size(10.dp)
                    )
                }
            } else {
                Text(
                    text = "${(progresoAnimado * 100).toInt()}%",
                    color = ColorAcento,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        LinearProgressIndicator(
            progress = { progresoAnimado },
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .clip(formaCirculoOEsquina),
            color = if (item.completado) Menta else ColorAcento,
            trackColor = SuperficieAlta
        )
    }
}
