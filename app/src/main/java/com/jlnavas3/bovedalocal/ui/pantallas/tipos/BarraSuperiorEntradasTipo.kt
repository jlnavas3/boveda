/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tipos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltroActivo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Barra superior desacoplada para la pantalla dedicada de tipo de entrada.
 */
@Composable
fun BarraSuperiorEntradasTipo(
    infoTipo: InfoTipoEntrada,
    cantidadEntradas: Int,
    conSeparador: Boolean,
    busquedaVisible: Boolean,
    textoBusqueda: String,
    soloFavoritos: Boolean,
    criterioOrdenacion: CriterioOrdenacion,
    menuOpcionesDesplegado: Boolean,
    haptica: Haptica,
    mostrarId: Boolean,
    alVolver: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alCambiarTextoBusqueda: (String) -> Unit,
    alCerrarBusqueda: () -> Unit,
    alAbrirMenu: () -> Unit,
    alCerrarMenu: () -> Unit,
    alAbrirOrdenacion: () -> Unit,
    alAlternarFavoritos: () -> Unit,
    alRestablecerFiltros: () -> Unit,
    alAbrirAyuda: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        BarraSuperiorPantalla(
            titulo = infoTipo.tituloPlural,
            idEtiqueta = infoTipo.idEtiqueta,
            mostrarId = mostrarId,
            alVolver = alVolver,
            conSeparador = conSeparador,
            colorFondo = ColorAjustesFondo,
            acciones = {
                // Botón Búsqueda (Lupa)
                IconButton(
                    onClick = {
                        haptica.tic()
                        alAlternarBusqueda()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .then(
                            if (busquedaVisible || textoBusqueda.isNotBlank()) {
                                Modifier.background(infoTipo.color.copy(alpha = 0.16f))
                            } else Modifier
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar ${infoTipo.tituloPlural.lowercase()}",
                        tint = if (busquedaVisible || textoBusqueda.isNotBlank()) infoTipo.color else ColorIconosInternos,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(6.dp))

                // Botón Tres Puntos (Filtros y ordenación)
                Box {
                    val tieneFiltrosActivos = soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ
                    IconButton(
                        onClick = {
                            haptica.tic()
                            alAbrirMenu()
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .then(
                                if (tieneFiltrosActivos) {
                                    Modifier.background(infoTipo.color.copy(alpha = 0.16f))
                                } else Modifier
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Más opciones",
                            tint = if (tieneFiltrosActivos) infoTipo.color else ColorIconosInternos,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    MenuDesplegableBoveda(
                        expanded = menuOpcionesDesplegado,
                        onDismissRequest = alCerrarMenu,
                        modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
                    ) {
                        ElementoMenuCompacto(
                            texto = "Ordenar por...",
                            icono = Icons.AutoMirrored.Filled.Sort,
                            colorIcono = infoTipo.color,
                            onClick = alAbrirOrdenacion
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = if (soloFavoritos) "Ver todas" else "Solo favoritos",
                            icono = Icons.Filled.Star,
                            colorIcono = if (soloFavoritos) ColorAcento else ColorIconosInternos,
                            colorTexto = if (soloFavoritos) ColorAcento else TextoPrincipal,
                            onClick = alAlternarFavoritos
                        )
                        if (soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ || textoBusqueda.isNotBlank()) {
                            SeparadorOpcionMenu()
                            ElementoMenuCompacto(
                                texto = "Restablecer filtros",
                                icono = Icons.Filled.Close,
                                colorIcono = Peligro,
                                colorTexto = Peligro,
                                onClick = alRestablecerFiltros
                            )
                        }
                    }
                }
            }
        )

        // Barra de búsqueda animada
        AnimatedVisibility(
            visible = busquedaVisible || textoBusqueda.isNotBlank(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 3.dp)
            ) {
                BarraBusquedaAnimada(
                    valor = textoBusqueda,
                    alCambiar = alCambiarTextoBusqueda,
                    alCerrar = alCerrarBusqueda
                )

                Spacer(Modifier.height(3.dp))

                com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda.ContenedorSugerenciasBusqueda(
                    textoBusqueda = textoBusqueda,
                    alCambiarTextoBusqueda = alCambiarTextoBusqueda,
                    alAbrirAyuda = alAbrirAyuda
                )
            }
        }

        // Chip de filtro activo
        if (soloFavoritos) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChipFiltroActivo(
                    texto = "★ Favoritos",
                    alLimpiar = alAlternarFavoritos
                )
            }
        }
    }
}
