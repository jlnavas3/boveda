package com.jlnavas3.bovedalocal.ui.pantallas.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun BarraSuperiorRegistro(
    busquedaVisible: Boolean,
    filtroTexto: String,
    categoriaSeleccionada: String,
    tieneFiltrosActivos: Boolean,
    alVolver: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alMostrarCategorias: () -> Unit,
    alMostrarOrdenacion: () -> Unit,
    alCopiarRegistro: () -> Unit,
    alCompartirRegistro: () -> Unit,
    alBorrarRegistro: () -> Unit,
    alRestablecerFiltros: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }

    BarraSuperiorPantalla(
        titulo = "Registro",
        alVolver = alVolver,
        colorFondo = ColorAjustesFondo,
        acciones = {
            // Botón Búsqueda (Lupa)
            IconButton(
                onClick = {
                    haptica.tic()
                    alAlternarBusqueda()
                },
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (busquedaVisible || filtroTexto.isNotBlank()) ColorAcento.copy(alpha = 0.16f) else ColorTarjetaAjustes)
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Buscar en registro",
                    tint = if (busquedaVisible || filtroTexto.isNotBlank()) ColorAcento else ColorIconosInternos,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Botón Filtros (Categorías modal)
            IconButton(
                onClick = {
                    haptica.tic()
                    alMostrarCategorias()
                },
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (categoriaSeleccionada != "Todos") ColorAcento.copy(alpha = 0.16f) else ColorTarjetaAjustes)
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = "Filtrar por categoría",
                    tint = if (categoriaSeleccionada != "Todos") ColorAcento else ColorIconosInternos,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Botón Menú 3 puntos (Opciones y Acciones)
            Box {
                IconButton(
                    onClick = {
                        haptica.tic()
                        menuOpcionesDesplegado = true
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (tieneFiltrosActivos) ColorAcento.copy(alpha = 0.16f) else ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Más opciones",
                        tint = if (tieneFiltrosActivos) ColorAcento else ColorIconosInternos,
                        modifier = Modifier.size(20.dp)
                    )
                }

                MenuDesplegableBoveda(
                    expanded = menuOpcionesDesplegado,
                    onDismissRequest = { menuOpcionesDesplegado = false },
                    modifier = Modifier.widthIn(min = 220.dp)
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = null, tint = ColorIconosInternos, modifier = Modifier.size(20.dp))
                        },
                        text = { Text("Ordenar por...", color = TextoPrincipal) },
                        onClick = {
                            menuOpcionesDesplegado = false
                            alMostrarOrdenacion()
                        }
                    )
                    SeparadorOpcionMenu()
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(Icons.Filled.ContentCopy, contentDescription = null, tint = ColorSeguridad, modifier = Modifier.size(20.dp))
                        },
                        text = { Text("Copiar registro", color = TextoPrincipal) },
                        onClick = {
                            menuOpcionesDesplegado = false
                            alCopiarRegistro()
                        }
                    )
                    SeparadorOpcionMenu()
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(Icons.Filled.Share, contentDescription = null, tint = ColorIconosInternos, modifier = Modifier.size(20.dp))
                        },
                        text = { Text("Compartir registro", color = TextoPrincipal) },
                        onClick = {
                            menuOpcionesDesplegado = false
                            alCompartirRegistro()
                        }
                    )
                    SeparadorOpcionMenu()
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(Icons.Filled.Delete, contentDescription = null, tint = Peligro, modifier = Modifier.size(20.dp))
                        },
                        text = { Text("Borrar registro", color = Peligro) },
                        onClick = {
                            menuOpcionesDesplegado = false
                            alBorrarRegistro()
                        }
                    )
                    if (tieneFiltrosActivos) {
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.Close, contentDescription = null, tint = Peligro, modifier = Modifier.size(20.dp))
                            },
                            text = { Text("Restablecer filtros", color = Peligro) },
                            onClick = {
                                menuOpcionesDesplegado = false
                                alRestablecerFiltros()
                            }
                        )
                    }
                }
            }
        }
    )
}
