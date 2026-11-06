/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.R

@Composable
fun MiniaturaIconoLauncher(
    variante: VarianteIconoLauncher,
    seleccionado: Boolean,
    modifier: Modifier = Modifier,
    tamano: Dp = 44.dp,
    mostrarBadgeCheck: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val radioCurva = (tamano.value * 0.26f).dp
    val formaSquircle = RoundedCornerShape(radioCurva)

    Box(
        modifier = modifier
            .size(tamano)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(formaSquircle)
                .background(variante.pincelFondo)
                .border(
                    width = if (seleccionado) 2.5.dp else 1.dp,
                    color = if (seleccionado) Color(0xFFFFB300) else Color(0x33FFFFFF),
                    shape = formaSquircle
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_candado_vector),
                contentDescription = variante.etiqueta,
                tint = variante.colorCandado,
                modifier = Modifier.size(tamano * 0.54f)
            )
        }

        if (seleccionado && mostrarBadgeCheck) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .size((tamano.value * 0.36f).dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFB300))
                    .border(1.5.dp, Color(0xFF0D111A), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size((tamano.value * 0.24f).dp)
                )
            }
        }
    }
}
