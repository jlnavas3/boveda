/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMerge
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.ItemAccionSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Barra inferior de acciones específica para la pantalla de duplicados.
 * Ofrece tres acciones directas y ergonómicas sin sobrecarga:
 * - Comparar: disponible cuando se seleccionan 2 o más entradas.
 * - Unificar: disponible cuando se seleccionan 2 o más entradas (fusión sin pérdida).
 * - Eliminar: disponible cuando hay 1 o más entradas seleccionadas.
 */
@Composable
fun BarraAccionesDuplicados(
    cantidad: Int,
    alComparar: () -> Unit,
    alUnificar: () -> Unit,
    alEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = Haptica(contexto)
    val puedeCompararOUnificar = cantidad >= 2
    val puedeEliminar = cantidad >= 1

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .shadow(16.dp, RoundedCornerShape(CurvaturaEsquinas))
            .clip(RoundedCornerShape(CurvaturaEsquinas))
            .background(ColorTarjetaAjustes)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, RoundedCornerShape(CurvaturaEsquinas))
                } else Modifier
            )
            .padding(horizontal = 8.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Comparar
            ItemAccionSeleccion(
                icono = Icons.AutoMirrored.Filled.CompareArrows,
                texto = "Comparar",
                habilitado = puedeCompararOUnificar,
                colorIcono = ColorAcento,
                alPulsar = {
                    haptica.toque()
                    alComparar()
                }
            )

            // 2. Unificar (Acción principal destacada)
            ItemAccionSeleccion(
                icono = Icons.AutoMirrored.Filled.CallMerge,
                texto = "Unificar",
                habilitado = puedeCompararOUnificar,
                colorIcono = ColorAcento,
                alPulsar = {
                    haptica.toque()
                    alUnificar()
                }
            )

            // 3. Eliminar
            ItemAccionSeleccion(
                icono = Icons.Filled.Delete,
                texto = "Eliminar",
                habilitado = puedeEliminar,
                colorIcono = Peligro,
                colorTexto = Peligro,
                alPulsar = {
                    haptica.error()
                    alEliminar()
                }
            )
        }
    }
}
