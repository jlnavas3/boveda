/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Panel desplegable completo organizado por categorías que muestra todos los modificadores disponibles y acceso a Ayuda.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PanelSugerenciasExpandido(
    textoBusqueda: String,
    alSeleccionarSugerencia: (ItemSugerenciaBusqueda) -> Unit,
    alAbrirAyuda: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorTarjetaAjustes.copy(alpha = 0.85f))
            .border(1.dp, ColorBordeActual.copy(alpha = 0.4f), forma)
            .padding(8.dp)
    ) {
        ProveedorSugerenciasBusqueda.todasPorCategoria.forEach { (categoria, items) ->
            Text(
                text = categoria.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextoSecundario,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(start = 2.dp, bottom = 4.dp, top = 2.dp)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items.forEach { item ->
                    val activo = ManejadorInsercionFiltro.estaActivo(textoBusqueda, item.comando)
                    ChipFiltroBusqueda(
                        item = item,
                        estaActivo = activo,
                        alPulsar = { alSeleccionarSugerencia(item) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))
        }

        // Enlace al manual completo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .clickable(onClick = alAbrirAyuda)
                .padding(vertical = 6.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                contentDescription = null,
                tint = ColorAcento,
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Ver guía de sintaxis en Ayuda →",
                fontSize = 12.sp,
                color = ColorAcento,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
