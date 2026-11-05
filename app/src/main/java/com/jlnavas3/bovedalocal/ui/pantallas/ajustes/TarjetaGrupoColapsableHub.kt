/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.InsigniaIdAjuste
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Tarjeta colapsable para cada grupo raíz de Ajustes (Nivel 1).
 *
 * Muestra el squircle neutro, título en mayúsculas sobrias o título principal,
 * descripción de orientación, cápsula con el recuento de opciones y flecha animada.
 * Al tocarla, despliega sus elementos hijos con una animación suave sin perder
 * la uniformidad visual de Google Material 3 / One UI.
 */
@Composable
fun TarjetaGrupoColapsableHub(
    grupo: NodoAjuste,
    elementos: List<ElementoMenuAjustes>,
    expandido: Boolean,
    alAlternarExpandido: () -> Unit,
    mostrarId: Boolean,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val rotacionChevron by animateFloatAsState(
        targetValue = if (expandido) 180f else 0f,
        label = "rotacion_chevron_grupo_${grupo.id}"
    )

    val formaTarjeta = RoundedCornerShape(CurvaturaEsquinas)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (GrosorBorde > 0.dp) {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaTarjeta)
                } else Modifier
            )
            .clip(formaTarjeta)
            .background(ColorTarjetaAjustes)
    ) {
        // Cabecera interactiva del grupo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    haptica.tic()
                    alAlternarExpandido()
                }
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Squircle neutro con icono de categoría con la misma dimensión que las opciones internas
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorCampoAjustes),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = grupo.icono ?: Icons.Filled.Settings,
                    contentDescription = null,
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(Modifier.width(10.dp))

            // Título e ID
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = grupo.titulo,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = TextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (mostrarId) {
                    Spacer(Modifier.width(6.dp))
                    InsigniaIdAjuste(id = grupo.id)
                }
            }

            Spacer(Modifier.width(8.dp))

            // Contador neutro de opciones dentro del grupo
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ColorCampoAjustes)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "${elementos.size}",
                    color = TextoSecundario,
                    style = EstiloMono.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(Modifier.width(6.dp))

            // Chevron interactivo con rotación suave
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = if (expandido) "Colapsar sección" else "Expandir sección",
                tint = ColorIconosInternos,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(rotacionChevron)
            )
        }

        // Contenido desplegable animado
        AnimatedVisibility(
            visible = expandido,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                ComponenteSeparador(sangriaInicio = 0.dp)

                elementos.forEachIndexed { index, elem ->
                    if (index > 0) ComponenteSeparador(sangriaInicio = 56.dp)
                    ComponenteNavegacion(
                        titulo = elem.titulo,
                        subtitulo = null,
                        icono = elem.icono,
                        colorIcono = elem.colorIcono,
                        idFila = elem.idEtiqueta,
                        mostrarId = mostrarId,
                        valorTexto = elem.valorTexto,
                        alPulsar = elem.alPulsar
                    )
                }

                Spacer(Modifier.height(4.dp))
            }
        }
    }
}
