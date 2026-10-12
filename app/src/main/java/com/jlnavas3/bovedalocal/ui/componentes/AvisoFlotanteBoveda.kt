/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.DatosAviso
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

/**
 * Proveedor de contexto Compose para disparar avisos flotantes internos desde cualquier subcomponente
 * sin tener que pasar el ViewModel ni recurrir al Toast nativo del sistema operativo.
 */
val LocalAvisador = staticCompositionLocalOf<(String, String?) -> Unit> {
    { _, _ -> }
}

/**
 * Componente flotante de notificación (Toast / Aviso) personalizado para toda la app.
 *
 * Características:
 * - Tarjeta blanca/clara flotante con sombra suave y esquinas redondeadas.
 * - Modo Descriptivo: Cabecera sutilmente contrastada con título, icono y botón '✕'.
 * - Modo Minimalista: Fila compacta de un solo nivel con icono, mensaje y botón '✕'.
 * - Botón '✕' táctil para cierre instantáneo con respuesta háptica.
 * - Temporizador automático de 3.2 segundos si no se pulsa '✕'.
 */
@Composable
fun AvisoFlotanteBoveda(
    aviso: DatosAviso?,
    toastDescriptivo: Boolean,
    alCerrar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    // Temporizador de auto-cierre tras 3.2 segundos
    LaunchedEffect(aviso) {
        if (aviso != null) {
            delay(3200L)
            alCerrar()
        }
    }

    AnimatedVisibility(
        visible = aviso != null,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 220)
        ) + fadeIn(animationSpec = tween(200)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 180)
        ) + fadeOut(animationSpec = tween(150)),
        modifier = modifier
    ) {
        if (aviso != null) {
            val icono = deducirIconoAviso(aviso)
            val tituloEfectivo = deducirTituloAviso(aviso)
            val esOscuro = esOscuroActivo

            val fondo = ColorTarjetaAjustes
            val borde = ColorSeparadorAjustes
            val colorTitulo = ColorTextoAjustes
            val colorMensaje = if (esOscuro) ColorTextoAjustes.copy(alpha = 0.92f) else ColorTextoAjustes.copy(alpha = 0.86f)
            val colorIcono = ColorAcento
            val colorCerrar = ColorAjusteGris
            val fondoCabecera = if (esOscuro) Color.Black.copy(alpha = 0.32f) else Color.Black.copy(alpha = 0.07f)
            val formaDescriptivo = RoundedCornerShape(CurvaturaEsquinas.coerceIn(12.dp, 18.dp))
            val formaMinimalista = RoundedCornerShape(CurvaturaEsquinas.coerceIn(10.dp, 14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 420.dp),
                contentAlignment = Alignment.Center
            ) {
                if (toastDescriptivo) {
                    // =========================================================
                    // MODO DESCRIPTIVO: Cabecera contrastada + Cuerpo
                    // =========================================================
                    Surface(
                        shape = formaDescriptivo,
                        color = fondo,
                        border = BorderStroke(1.dp, borde),
                        shadowElevation = if (esOscuro) 6.dp else 8.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Cabecera ligeramente más oscura
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(fondoCabecera)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = icono,
                                    contentDescription = null,
                                    tint = colorIcono,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = tituloEfectivo,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colorTitulo,
                                    modifier = Modifier.weight(1f)
                                )
                                // Botón '✕' de cierre rápido
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            haptica.tic()
                                            alCerrar()
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Cerrar",
                                        tint = colorCerrar,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }

                            HorizontalDivider(
                                thickness = 0.8.dp,
                                color = borde.copy(alpha = 0.65f)
                            )

                            // Cuerpo con mensaje descriptivo
                            Text(
                                text = aviso.mensaje,
                                fontSize = 13.sp,
                                color = colorMensaje,
                                lineHeight = 18.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }
                    }
                } else {
                    // =========================================================
                    // MODO MINIMALISTA: Barra / píldora compacta
                    // =========================================================
                    Surface(
                        shape = formaMinimalista,
                        color = fondo,
                        border = BorderStroke(1.dp, borde),
                        shadowElevation = if (esOscuro) 4.dp else 6.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = icono,
                                contentDescription = null,
                                tint = colorIcono,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = aviso.mensaje,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorTitulo,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(6.dp))
                            // Botón '✕' de cierre rápido
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        haptica.tic()
                                        alCerrar()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Cerrar",
                                    tint = colorCerrar,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Deduce un título contextual inteligente si no se pasó uno explícito.
 */
private fun deducirTituloAviso(aviso: DatosAviso): String {
    if (!aviso.titulo.isNullOrBlank()) return aviso.titulo

    val txt = aviso.mensaje.lowercase()
    return when {
        txt.contains("restablecid") || txt.contains("restaurad") -> "Ajustes restablecidos"
        txt.contains("copiad") -> "Portapapeles"
        txt.contains("guardad") -> "Bóveda Local"
        txt.contains("papelera") || txt.contains("borrad") -> "Papelera"
        txt.contains("exportad") || txt.contains("importad") -> "Copia y sincronización"
        txt.contains("cerrad") || txt.contains("bloquead") || txt.contains("biometr") -> "Seguridad"
        txt.contains("activad") || txt.contains("desactivad") -> "Configuración"
        else -> "Bóveda Local"
    }
}

/**
 * Selecciona un icono armónico acorde al mensaje o título.
 */
private fun deducirIconoAviso(aviso: DatosAviso): ImageVector {
    val combinado = "${aviso.titulo.orEmpty()} ${aviso.mensaje}".lowercase()
    return when {
        combinado.contains("restablecid") || combinado.contains("restaurad") -> Icons.Filled.Refresh
        combinado.contains("copiad") -> Icons.Filled.ContentCopy
        combinado.contains("papelera") || combinado.contains("borrad") -> Icons.Filled.Delete
        combinado.contains("seguridad") || combinado.contains("cerrad") || combinado.contains("bloquead") -> Icons.Filled.Security
        combinado.contains("guardad") || combinado.contains("éxito") || combinado.contains("exito") || combinado.contains("correct") -> Icons.Filled.CheckCircle
        combinado.contains("ajuste") || combinado.contains("configura") -> Icons.Filled.Notifications
        else -> Icons.Filled.Info
    }
}
