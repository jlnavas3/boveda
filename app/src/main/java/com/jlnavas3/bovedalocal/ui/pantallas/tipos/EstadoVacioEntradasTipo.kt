/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tipos

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.EstadoVacioPantalla

/**
 * Vista de estado vacío desacoplada para pantallas dedicadas por tipo de entrada.
 * Presentación neutra y sobria en escala de grises, delegando la creación al FAB (+).
 */
@Composable
fun EstadoVacioEntradasTipo(
    infoTipo: InfoTipoEntrada,
    sinEntradasEnTotal: Boolean,
    modifier: Modifier = Modifier
) {
    val titulo = if (sinEntradasEnTotal) {
        "Todavía no hay ${infoTipo.tituloPlural.lowercase()}"
    } else {
        "No se encontraron ${infoTipo.tituloPlural.lowercase()}"
    }

    val subtitulo = if (sinEntradasEnTotal) {
        infoTipo.textoVacioTotal
    } else {
        "Ninguna entrada coincide con la búsqueda o filtro aplicado."
    }

    EstadoVacioPantalla(
        icono = infoTipo.icono,
        titulo = titulo,
        subtitulo = subtitulo,
        modifier = modifier
    )
}
