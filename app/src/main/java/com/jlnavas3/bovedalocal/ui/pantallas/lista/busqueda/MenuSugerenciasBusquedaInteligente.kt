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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente que gestiona el menú desplegable contextual inteligente con las sugerencias
 * de comandos de búsqueda y autocompletado en capa flotante (overlay).
 */
@Composable
fun MenuSugerenciasBusquedaInteligente(
    expandido: Boolean,
    alCerrar: () -> Unit,
    textoBusqueda: String,
    sugerencias: List<ItemSugerenciaBusqueda>,
    alSeleccionar: (ItemSugerenciaBusqueda) -> Unit,
    alAbrirAyuda: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = FormaTarjeta
    val tieneBorde = GrosorBorde > 0.dp && EstiloBorde != "ninguno"
    val grosorEfectivo = if (tieneBorde) GrosorBorde else if (EstiloBorde == "ninguno") 0.dp else 1.dp

    DropdownMenu(
        expanded = expandido,
        onDismissRequest = alCerrar,
        modifier = modifier
            .widthIn(min = 280.dp, max = 340.dp)
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .then(
                if (grosorEfectivo > 0.dp) Modifier.border(grosorEfectivo, ColorBordeActual.copy(alpha = 0.6f), forma)
                else Modifier
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Cabecera del menú flotante
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "COMANDOS DE BÚSQUEDA",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = TextoSecundario
                )
            }

            HorizontalDivider(
                color = ColorSeparadorAjustes.copy(alpha = 0.5f),
                thickness = 0.5.dp
            )

            // Contenedor scrolleable con las opciones
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 280.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (sugerencias.isEmpty()) {
                    Text(
                        text = "Sin sugerencias para este término",
                        color = TextoSecundario,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(14.dp)
                    )
                } else {
                    sugerencias.forEach { item ->
                        val activo = ManejadorInsercionFiltro.estaActivo(textoBusqueda, item.comando)
                        ItemMenuSugerenciaBusqueda(
                            item = item,
                            estaActivo = activo,
                            alPulsar = { alSeleccionar(item) }
                        )
                    }
                }
            }

            HorizontalDivider(
                color = ColorSeparadorAjustes.copy(alpha = 0.5f),
                thickness = 0.5.dp
            )

            // Acceso al manual completo en Ayuda
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        alCerrar()
                        alAbrirAyuda()
                    })
                    .padding(horizontal = 14.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Ver guía de sintaxis en Ayuda →",
                    color = ColorAcento,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
