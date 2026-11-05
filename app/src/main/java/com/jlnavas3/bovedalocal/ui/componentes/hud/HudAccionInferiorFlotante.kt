/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.hud

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

/**
 * Pastilla flotante (HUD) proyectada en la parte inferior de la pantalla,
 * centrada sobre el panel de acciones o barra de navegación.
 *
 * Muestra el nombre e icono de la acción mantenida pulsada durante 2.5 segundos,
 * adaptándose automáticamente a las 'Formas y bordes' configuradas por el usuario
 * y al tema claro u oscuro activo.
 * Se muestra siempre, con independencia de si los avisos emergentes generales están desactivados.
 */
@Composable
fun HudAccionInferiorFlotante(
    gestor: GestorHudAccion,
    modifier: Modifier = Modifier
) {
    val datosActivos by gestor.estado.collectAsStateWithLifecycle()
    val forma = RoundedCornerShape(CurvaturaEsquinas)

    Box(
        modifier = modifier
            .zIndex(99f)
            .navigationBarsPadding()
            .padding(bottom = 68.dp, start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = datosActivos != null,
            enter = fadeIn(tween(160)) + scaleIn(tween(160), initialScale = 0.90f),
            exit = fadeOut(tween(220)) + scaleOut(tween(220), targetScale = 0.92f)
        ) {
            val datos = datosActivos ?: return@AnimatedVisibility
            val esOscuro = esOscuroActivo

            val fondoHud = if (esOscuro) Color(0xEE1A1A1C) else Color(0xF8FFFFFF)
            val textoHud = if (esOscuro) Color(0xFFF3F4F8) else Color(0xFF11141A)

            val modifierBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                Modifier.border(GrosorBorde, ColorBordeActual.copy(alpha = 0.75f), forma)
            } else {
                Modifier.border(0.5.dp, if (esOscuro) Color(0x2AFFFFFF) else Color(0x18000000), forma)
            }

            Row(
                modifier = Modifier
                    .shadow(elevation = 10.dp, shape = forma)
                    .clip(forma)
                    .background(fondoHud)
                    .then(modifierBorde)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { gestor.ocultar() }
                    )
                    .padding(horizontal = 16.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (datos.icono != null) {
                    Icon(
                        imageVector = datos.icono,
                        contentDescription = null,
                        tint = ColorAcento,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = datos.etiqueta,
                    color = textoHud,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
