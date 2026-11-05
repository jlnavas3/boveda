/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Micro-componente desacoplado para agrupar controles o ajustes dentro de una tarjeta
 * colapsable/expandible animada, con cabecera táctil, chevron rotatorio y respeto total
 * por la curvatura y grosor de bordes del sistema de diseño.
 */
@Composable
fun ComponenteGrupoColapsable(
    etiqueta: String,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color = ColorIconosInternos,
    expandidoInicial: Boolean = false,
    resumen: String? = null,
    alAlternar: ((Boolean) -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var expandido by remember { mutableStateOf(expandidoInicial) }
    val rotacionChevron by animateFloatAsState(
        targetValue = if (expandido) 180f else 0f,
        label = "rotacion_chevron_grupo"
    )

    val formaTarjeta = RoundedCornerShape(CurvaturaEsquinas)
    val colorBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        ColorBordeActual
    } else {
        ColorSeparadorAjustes.copy(alpha = 0.5f)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(formaTarjeta)
            .background(ColorTarjetaAjustes)
            .border(
                width = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 0.8.dp,
                color = colorBorde,
                shape = formaTarjeta
            )
    ) {
        // Cabecera clickeable que actúa como interruptor del acordeón
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    haptica.tic()
                    expandido = !expandido
                    alAlternar?.invoke(expandido)
                }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icono != null) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(colorIcono.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = colorIcono,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = etiqueta,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                        color = TextoPrincipal
                    )
                    if (!subtitulo.isNullOrBlank()) {
                        Text(
                            text = subtitulo,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.5.sp,
                                lineHeight = 15.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Normal
                            ),
                            color = ColorAjusteGris
                        )
                    }
                    if (!resumen.isNullOrBlank()) {
                        Text(
                            text = resumen,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = ColorAjusteGris
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(ColorCampoAjustes),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expandido) "Colapsar sección" else "Expandir sección",
                    tint = ColorIconosInternos,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(rotacionChevron)
                )
            }
        }

        // Contenido desplegable animado
        AnimatedVisibility(
            visible = expandido,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.6.dp)
                        .background(ColorSeparadorAjustes.copy(alpha = 0.5f))
                )
                contenido()
            }
        }
    }
}
