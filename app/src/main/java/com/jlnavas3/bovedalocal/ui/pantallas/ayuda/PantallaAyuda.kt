/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.ayuda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.reboteElastico
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Pantalla principal del Centro de Ayuda (Hub de Documentación).
 * Muestra el buscador en tiempo real y todos los temas agrupados en tarjetas con estética MagicOS / One UI.
 */
@Composable
fun PantallaAyuda(
    vm: VaultViewModel,
    alVolver: () -> Unit,
    seccionInicial: String? = null,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var consultaBusqueda by remember { mutableStateOf("") }

    val articulosFiltrados = remember(consultaBusqueda) {
        CatalogoArticulosAyuda.buscar(consultaBusqueda)
    }

    val agrupadosPorCategoria = remember(articulosFiltrados) {
        articulosFiltrados.groupBy { it.grupo }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Ayuda y manual de uso",
            alVolver = alVolver,
            conSeparador = scrollState.value > 0
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .reboteElastico()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Buscador integrado dentro de la ayuda
            BarraBusquedaAyuda(
                consulta = consultaBusqueda,
                alCambiarConsulta = { consultaBusqueda = it }
            )

            if (articulosFiltrados.isEmpty()) {
                Text(
                    text = "No se encontraron temas para \"$consultaBusqueda\".",
                    color = TextoSecundario,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 16.dp, start = 4.dp)
                )
            } else {
                agrupadosPorCategoria.forEach { (grupo, articulos) ->
                    SeccionGrupoAyuda(
                        tituloGrupo = grupo,
                        articulos = articulos,
                        alSeleccionarArticulo = { articulo ->
                            vm.ir(Pantalla.ArticuloAyudaDetalle(articulo.id))
                        }
                    )
                }
            }

            Spacer(Modifier.height(36.dp))
        }
    }
}
