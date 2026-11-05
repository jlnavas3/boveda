/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.transiciones

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Lienzo miniatura flotante que previsualiza en tiempo real
 * la animación de navegación configurada, sin título ni marco externo invasivo,
 * con un botón de prueba integrado directamente sobre el lienzo.
 */
@Composable
fun TarjetaPreviewTransicion(
    ajustes: AjustesApp,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var pasoMiniatura by remember { mutableIntStateOf(0) }
    var esRetrocesoMiniatura by remember { mutableStateOf(false) }

    val formaLienzo = RoundedCornerShape(CurvaturaEsquinas)
    val grosorLienzo = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 1.dp
    val colorBordeLienzo = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") ColorBordeActual else ColorSeparadorAjustes.copy(alpha = 0.7f)
    val formaIconoMiniatura = if (CurvaturaEsquinas == 0.dp) RoundedCornerShape(0.dp) else CircleShape

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height((115 * EscalaTexto).dp.coerceAtLeast(105.dp))
            .clip(formaLienzo)
            .background(ColorTarjetaAjustes)
            .border(grosorLienzo, colorBordeLienzo, formaLienzo)
    ) {
        val pantallaFicticia: Pantalla = if (pasoMiniatura == 0) Pantalla.Lista else Pantalla.Detalle("preview")

        AnimatedContent(
            targetState = pantallaFicticia,
            transitionSpec = {
                construirTransicionNavegacion(
                    ajustes = ajustes,
                    esRetroceso = esRetrocesoMiniatura,
                    initialState = initialState,
                    targetState = targetState
                )
            },
            label = "preview_animacion",
            modifier = Modifier.fillMaxSize()
        ) { destino ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorTarjetaAjustes)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.TopStart
            ) {
                if (destino is Pantalla.Lista) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(ColorSeparadorAjustes, formaIconoMiniatura),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Security, contentDescription = null, tint = Menta, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Bóveda Principal", color = ColorTextoAjustes, fontSize = (13 * EscalaTexto).sp, fontWeight = FontWeight.Bold)
                            Text("Vista de lista de cuentas", color = ColorAjusteGris, fontSize = (11 * EscalaTexto).sp, fontStyle = FontStyle.Italic)
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF1E88E5), formaIconoMiniatura),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.VpnKey, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Detalle de Cuenta", color = ColorTextoAjustes, fontSize = (13 * EscalaTexto).sp, fontWeight = FontWeight.Bold)
                            Text("github.com/usuario", color = ColorAjusteGris, fontSize = (11 * EscalaTexto).sp, fontStyle = FontStyle.Italic)
                        }
                    }
                }
            }
        }

        // Chip sutil en esquina superior con la duración actual
        Text(
            text = "${ajustes.duracionTransicionMs} ms",
            color = ColorAjusteGris.copy(alpha = 0.8f),
            fontSize = (11 * EscalaTexto).sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
        )

        // Botón compacto sobre la previsualización (adaptativo claro/oscuro y bordes dinámicos)
        val formaBoton = if (CurvaturaEsquinas == 0.dp) RoundedCornerShape(0.dp) else RoundedCornerShape(20.dp)
        val grosorBoton = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 1.dp
        val colorBordeBoton = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") ColorBordeActual else ColorSeparadorAjustes.copy(alpha = 0.8f)

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
                .clip(formaBoton)
                .background(ColorCampoAjustes)
                .border(grosorBoton, colorBordeBoton, formaBoton)
                .clickable {
                    haptica.toque()
                    esRetrocesoMiniatura = (pasoMiniatura == 1)
                    pasoMiniatura = if (pasoMiniatura == 0) 1 else 0
                }
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = ColorAcento,
                modifier = Modifier.size((14 * EscalaTexto).dp.coerceAtLeast(12.dp))
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = if (pasoMiniatura == 0) "Probar avance (Push)" else "Probar retroceso (Pop)",
                color = ColorTextoAjustes,
                fontSize = (11.5f * EscalaTexto).sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
