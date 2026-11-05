/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.util.ResolverSubtituloEntrada

/**
 * Microcomponente para renderizar la información de dominio relevante de entradas nativas
 * especializadas en la lista de duplicados (SSID, número enmascarado, IBAN, etc.).
 */
@Composable
fun FilaDatoNativoDuplicado(
    entrada: Entrada,
    modifier: Modifier = Modifier
) {
    val subtitulo = ResolverSubtituloEntrada.resolver(entrada)
    if (subtitulo.isBlank()) return

    Text(
        text = subtitulo,
        color = ColorTextoAjustes.copy(alpha = 0.9f),
        style = MaterialTheme.typography.bodySmall.copy(
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Medium
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.fillMaxWidth()
    )
}
