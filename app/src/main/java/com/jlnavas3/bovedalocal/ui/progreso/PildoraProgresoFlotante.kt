/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.progreso

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.EstadoProgresoOperacion
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente que renderiza una píldora flotante compacta cuando el panel
 * de progreso se encuentra minimizado por el usuario.
 */
@Composable
fun PildoraProgresoFlotante(
    estado: EstadoProgresoOperacion,
    visible: Boolean,
    alReabrir: () -> Unit,
    alCerrar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formaPildora = RoundedCornerShape(CurvaturaEsquinas)

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .shadow(elevation = 6.dp, shape = formaPildora)
                    .clip(formaPildora)
                    .background(SuperficieAlta)
                    .border(
                        width = GrosorBorde.coerceAtMost(1.5.dp),
                        color = ColorBordeActual.copy(alpha = 0.55f),
                        shape = formaPildora
                    )
                    .clickable(onClick = alReabrir)
                    .padding(start = 14.dp, end = 8.dp, top = 6.dp, bottom = 6.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(20.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { estado.progresoGlobal.coerceIn(0f, 1f) },
                        modifier = Modifier.size(20.dp),
                        color = ColorAcento,
                        trackColor = ColorAcento.copy(alpha = 0.2f),
                        strokeWidth = 2.dp
                    )
                    Icon(
                        imageVector = Icons.Filled.Sync,
                        contentDescription = null,
                        tint = ColorAcento,
                        modifier = Modifier.size(10.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                val porcentajeEntero = (estado.progresoGlobal.coerceIn(0f, 1f) * 100).toInt()
                Text(
                    text = "${estado.titulo} · $porcentajeEntero%",
                    color = TextoPrincipal,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = alCerrar,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Cerrar",
                        tint = TextoSecundario,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
