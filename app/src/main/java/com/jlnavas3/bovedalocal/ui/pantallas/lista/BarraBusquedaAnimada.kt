/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda.BotonAsistenteBusqueda
import com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda.ManejadorInsercionFiltro
import com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda.MenuSugerenciasBusquedaInteligente
import com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda.resolverSugerenciasBusquedaInteligentes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import kotlinx.coroutines.delay

/**
 * Barra de búsqueda animada desplegable con asistente inteligente de comandos en capa flotante (overlay).
 */
@Composable
fun BarraBusquedaAnimada(
    valor: String,
    alCambiar: (String) -> Unit,
    alCerrar: () -> Unit,
    modifier: Modifier = Modifier,
    entradas: List<Entrada> = emptyList(),
    categorias: List<Categoria> = emptyList(),
    etiquetasDisponibles: List<String> = emptyList(),
    alAbrirAyuda: (() -> Unit)? = null
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    var menuAsistenteAbierto by remember { mutableStateOf(false) }

    val sugerencias = remember(entradas, categorias, etiquetasDisponibles, valor) {
        resolverSugerenciasBusquedaInteligentes(
            entradas = entradas,
            categorias = categorias,
            etiquetasDisponibles = etiquetasDisponibles,
            textoBusqueda = valor
        )
    }

    val forma = RoundedCornerShape(CurvaturaEsquinas)

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(forma)
                .background(ColorTarjetaAjustes)
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") Modifier.border(GrosorBorde, ColorBordeActual, forma)
                    else Modifier
                )
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = if (valor.isBlank()) TextoSecundario else ColorAcento,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(10.dp))
            BasicTextField(
                value = valor,
                onValueChange = { nuevo ->
                    alCambiar(nuevo)
                    val ultimoToken = nuevo.trim().split(Regex("\\s+")).lastOrNull() ?: ""
                    if (ultimoToken.startsWith("cat:") || ultimoToken.startsWith("tag:") || ultimoToken.startsWith("t:")) {
                        menuAsistenteAbierto = true
                    }
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = TextoPrincipal, fontSize = 15.sp),
                cursorBrush = SolidColor(ColorAcento),
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
            )

            if (sugerencias.isNotEmpty() && alAbrirAyuda != null) {
                BotonAsistenteBusqueda(
                    activo = menuAsistenteAbierto,
                    alPulsar = { menuAsistenteAbierto = !menuAsistenteAbierto }
                )
                Spacer(Modifier.width(2.dp))
            }

            IconButton(
                onClick = alCerrar,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Cerrar búsqueda",
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        if (alAbrirAyuda != null) {
            MenuSugerenciasBusquedaInteligente(
                expandido = menuAsistenteAbierto,
                alCerrar = { menuAsistenteAbierto = false },
                textoBusqueda = valor,
                sugerencias = sugerencias,
                alSeleccionar = { item ->
                    val nuevoTexto = ManejadorInsercionFiltro.alternarFiltro(valor, item.comando)
                    alCambiar(nuevoTexto)
                    menuAsistenteAbierto = false
                },
                alAbrirAyuda = alAbrirAyuda,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}
