package com.jlnavas3.bovedalocal.ui.pantallas.passkeys

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltroActivo
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun BarraSuperiorPasskeys(
    conSeparador: Boolean,
    busquedaVisible: Boolean,
    textoBusqueda: String,
    soloFavoritos: Boolean,
    criterioOrdenacion: CriterioOrdenacion,
    menuOpcionesDesplegado: Boolean,
    haptica: Haptica,
    alVolver: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alCambiarTextoBusqueda: (String) -> Unit,
    alCerrarBusqueda: () -> Unit,
    alAbrirMenu: () -> Unit,
    alCerrarMenu: () -> Unit,
    alAbrirOrdenacion: () -> Unit,
    alAlternarFavoritos: () -> Unit,
    alIrExportacionSelectiva: () -> Unit,
    alRestablecerFiltros: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        BarraSuperiorPantalla(
            titulo = "Passkeys",
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
                        .background(if (busquedaVisible || textoBusqueda.isNotBlank()) ColorPasskeys.copy(alpha = 0.16f) else ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar passkeys",
                        tint = if (busquedaVisible || textoBusqueda.isNotBlank()) ColorPasskeys else ColorIconosInternos,
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
                            .background(if (tieneFiltrosActivos) ColorPasskeys.copy(alpha = 0.16f) else ColorTarjetaAjustes)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Más opciones",
                            tint = if (tieneFiltrosActivos) ColorPasskeys else ColorIconosInternos,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    MenuDesplegableBoveda(
                        expanded = menuOpcionesDesplegado,
                        onDismissRequest = alCerrarMenu,
                        modifier = Modifier.widthIn(min = 210.dp)
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = null, tint = ColorPasskeys, modifier = Modifier.size(20.dp))
                            },
                            text = { Text("Ordenar por...", color = TextoPrincipal) },
                            onClick = alAbrirOrdenacion
                        )
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = if (soloFavoritos) Ambar else ColorIconosInternos,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            text = {
                                Text(
                                    if (soloFavoritos) "Ver todas las llaves" else "Solo favoritos",
                                    color = if (soloFavoritos) Ambar else TextoPrincipal
                                )
                            },
                            onClick = alAlternarFavoritos
                        )
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.FileDownload, contentDescription = null, tint = ColorPasskeys, modifier = Modifier.size(20.dp))
                            },
                            text = { Text("Exportación selectiva", color = TextoPrincipal) },
                            onClick = alIrExportacionSelectiva
                        )
                        if (soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ || textoBusqueda.isNotBlank()) {
                            SeparadorOpcionMenu()
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(Icons.Filled.Close, contentDescription = null, tint = Peligro, modifier = Modifier.size(20.dp))
                                },
                                text = { Text("Restablecer filtros", color = Peligro) },
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                BarraBusquedaAnimada(
                    valor = textoBusqueda,
                    alCambiar = alCambiarTextoBusqueda,
                    alCerrar = alCerrarBusqueda
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
