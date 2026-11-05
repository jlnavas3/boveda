/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.barra

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.hud.accionConHud
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Botón individual para la barra inferior fija de acciones (estilo TikTok / Bottom Bar:
 * Icono centrado superior con micro-texto centrado debajo, con soporte para HUD flotante en pulsación sostenida).
 */
@Composable
fun BotonAccionBarraInferior(
    item: ItemAccionBarraInferior,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = Haptica(contexto)
    val colorIconoEfectivo = item.colorIcono ?: ColorAcento
    val colorTextoEfectivo = item.colorTexto ?: TextoSecundario

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .accionConHud(
                etiqueta = item.descripcion ?: item.etiqueta,
                icono = item.icono,
                habilitado = item.activo,
                radioRipple = 28.dp,
                alPulsar = {
                    haptica.toque()
                    item.alPulsar()
                }
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = item.icono,
            contentDescription = item.descripcion ?: item.etiqueta,
            tint = if (item.activo) colorIconoEfectivo else TextoSecundario.copy(alpha = 0.35f),
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = item.etiqueta,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = (10 * EscalaTexto).sp,
                fontWeight = FontWeight.Medium
            ),
            color = if (item.activo) colorTextoEfectivo else TextoSecundario.copy(alpha = 0.35f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}
