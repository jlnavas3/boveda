package com.jlnavas3.bovedalocal.ui.pantallas.lista

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
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun BarraSuperiorLista(
    nombreBoveda: String,
    totalEntradas: Int,
    busquedaVisible: Boolean,
    busquedaActiva: Boolean,
    tieneFiltrosActivos: Boolean,
    soloFavoritos: Boolean,
    agruparPorSitio: Boolean,
    mostrarIndicadoresContenido: Boolean,
    hayFiltrosParaRestablecer: Boolean,
    alAbrirMenu: () -> Unit,
    alBloquear: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alMostrarOrdenacion: () -> Unit,
    alMostrarFiltros: () -> Unit,
    alAlternarSoloFavoritos: () -> Unit,
    alIrOrganizacionGrupo: () -> Unit,
    alIrOrganizacionIndicadores: () -> Unit,
    alIrExportarSelectivo: () -> Unit,
    alIrCopiaSeguridad: () -> Unit,
    alIrCsvGoogle: () -> Unit,
    alRestablecerFiltros: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { haptica.toque(); alAbrirMenu() },
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(ColorTarjetaAjustes)
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menú",
                tint = ColorIconosInternos,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                nombreBoveda.ifBlank { "Bóveda local" },
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = ColorTitulos,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "$totalEntradas ${if (totalEntradas == 1) "entrada" else "entradas"} cifradas",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario,
                maxLines = 1
            )
        }

        // Botón Bloquear (Candado)
        IconButton(
            onClick = { haptica.toque(); alBloquear() },
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Peligro.copy(alpha = 0.12f))
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Bloquear bóveda",
                tint = Peligro,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(6.dp))

        // Botón Búsqueda (Lupa)
        IconButton(
            onClick = {
                haptica.tic()
                alAlternarBusqueda()
            },
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (busquedaVisible || busquedaActiva) Ambar.copy(alpha = 0.16f) else ColorTarjetaAjustes)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Buscar",
                tint = if (busquedaVisible || busquedaActiva) Ambar else ColorIconosInternos,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(6.dp))

        // Botón Tres Puntos (Filtros y Ordenación)
        Box {
            IconButton(
                onClick = {
                    haptica.tic()
                    menuOpcionesDesplegado = true
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (tieneFiltrosActivos) Ambar.copy(alpha = 0.16f) else ColorTarjetaAjustes)
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Más opciones",
                    tint = if (tieneFiltrosActivos) Ambar else ColorIconosInternos,
                    modifier = Modifier.size(20.dp)
                )
            }

            MenuDesplegableBoveda(
                expanded = menuOpcionesDesplegado,
                onDismissRequest = { menuOpcionesDesplegado = false },
                modifier = Modifier.widthIn(min = 210.dp)
            ) {
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
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
                        Icon(Icons.Filled.Tune, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
                    },
                    text = { Text("Filtrar por tipo...", color = TextoPrincipal) },
                    onClick = {
                        menuOpcionesDesplegado = false
                        alMostrarFiltros()
                    }
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
                            if (soloFavoritos) "Ver todas las cuentas" else "Solo favoritos",
                            color = if (soloFavoritos) Ambar else TextoPrincipal
                        )
                    },
                    onClick = {
                        menuOpcionesDesplegado = false
                        haptica.tic()
                        alAlternarSoloFavoritos()
                    }
                )
                SeparadorOpcionMenu()
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(Icons.Filled.Tune, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
                    },
                    text = {
                        Text(
                            if (agruparPorSitio) "Desagrupar cuentas" else "Agrupar cuentas",
                            color = TextoPrincipal
                        )
                    },
                    onClick = {
                        menuOpcionesDesplegado = false
                        haptica.tic()
                        alIrOrganizacionGrupo()
                    }
                )
                SeparadorOpcionMenu()
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(Icons.Filled.Tune, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
                    },
                    text = {
                        Text(
                            if (mostrarIndicadoresContenido) "Esconder indicadores" else "Mostrar indicadores",
                            color = TextoPrincipal
                        )
                    },
                    onClick = {
                        menuOpcionesDesplegado = false
                        haptica.tic()
                        alIrOrganizacionIndicadores()
                    }
                )
                SeparadorOpcionMenu()
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(Icons.Filled.FileDownload, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
                    },
                    text = { Text("Exportación selectiva", color = TextoPrincipal) },
                    onClick = {
                        menuOpcionesDesplegado = false
                        haptica.tic()
                        alIrExportarSelectivo()
                    }
                )
                SeparadorOpcionMenu()
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(Icons.Filled.FileUpload, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
                    },
                    text = { Text("Importar copia de seguridad", color = TextoPrincipal) },
                    onClick = {
                        menuOpcionesDesplegado = false
                        haptica.tic()
                        alIrCopiaSeguridad()
                    }
                )
                SeparadorOpcionMenu()
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(Icons.Filled.FileUpload, contentDescription = null, tint = Ambar, modifier = Modifier.size(20.dp))
                    },
                    text = { Text("Importar contraseñas de Google", color = TextoPrincipal) },
                    onClick = {
                        menuOpcionesDesplegado = false
                        haptica.tic()
                        alIrCsvGoogle()
                    }
                )
                if (hayFiltrosParaRestablecer) {
                    SeparadorOpcionMenu()
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(Icons.Filled.Close, contentDescription = null, tint = Peligro, modifier = Modifier.size(20.dp))
                        },
                        text = { Text("Restablecer filtros", color = Peligro) },
                        onClick = {
                            menuOpcionesDesplegado = false
                            haptica.tic()
                            alRestablecerFiltros()
                        }
                    )
                }
            }
        }
    }
}
