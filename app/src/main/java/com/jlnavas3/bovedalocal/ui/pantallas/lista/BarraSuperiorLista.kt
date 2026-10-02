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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VpnKey
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
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.ElementoRetornoSubmenu
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

private enum class SubmenuLista {
    PRINCIPAL,
    IMPORTAR,
    EXPORTAR
}

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
    alAlternarBusqueda: () -> Unit,
    alMostrarOrdenacion: () -> Unit,
    alMostrarFiltros: () -> Unit,
    alAlternarSoloFavoritos: () -> Unit,
    alIrOrganizacionGrupo: () -> Unit,
    alIrOrganizacionIndicadores: () -> Unit,
    alIrExportarSelectivo: () -> Unit,
    alIrCopiaSeguridadManual: () -> Unit,
    alIrCopiaSeguridad: () -> Unit,
    alIrCsvGoogle: () -> Unit,
    alImportarDirectoCxf: () -> Unit = {},
    alExportarDirectoCxf: () -> Unit = {},
    alRestablecerFiltros: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }
    var submenuActivo by remember { mutableStateOf(SubmenuLista.PRINCIPAL) }

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

        // Botón Tres Puntos (Filtros, Ordenación y Ajustes Contextuales)
        Box {
            IconButton(
                onClick = {
                    haptica.tic()
                    submenuActivo = SubmenuLista.PRINCIPAL
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
                onDismissRequest = {
                    menuOpcionesDesplegado = false
                    submenuActivo = SubmenuLista.PRINCIPAL
                },
                modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
            ) {
                when (submenuActivo) {
                    SubmenuLista.PRINCIPAL -> {
                        ElementoMenuCompacto(
                            texto = "Ordenar por...",
                            icono = Icons.AutoMirrored.Filled.Sort,
                            onClick = {
                                menuOpcionesDesplegado = false
                                alMostrarOrdenacion()
                            }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Filtrar por tipo...",
                            icono = Icons.Filled.Tune,
                            onClick = {
                                menuOpcionesDesplegado = false
                                alMostrarFiltros()
                            }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = if (soloFavoritos) "Ver todas las cuentas" else "Solo favoritos",
                            icono = Icons.Filled.Star,
                            colorIcono = if (soloFavoritos) Ambar else ColorIconosInternos,
                            colorTexto = if (soloFavoritos) Ambar else TextoPrincipal,
                            onClick = {
                                menuOpcionesDesplegado = false
                                alAlternarSoloFavoritos()
                            }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = if (agruparPorSitio) "Ajustes de agrupación..." else "Agrupar cuentas...",
                            icono = androidx.compose.material.icons.Icons.Filled.Layers,
                            colorIcono = Ambar,
                            onClick = {
                                menuOpcionesDesplegado = false
                                alIrOrganizacionGrupo()
                            }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = if (mostrarIndicadoresContenido) "Ajustes de indicadores..." else "Mostrar indicadores...",
                            icono = androidx.compose.material.icons.Icons.Filled.Tune,
                            colorIcono = Ambar,
                            onClick = {
                                menuOpcionesDesplegado = false
                                alIrOrganizacionIndicadores()
                            }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Importar...",
                            icono = androidx.compose.material.icons.Icons.Filled.FileUpload,
                            colorIcono = Ambar,
                            iconoFinal = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowForward,
                            onClick = {
                                submenuActivo = SubmenuLista.IMPORTAR
                            }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Exportar...",
                            icono = androidx.compose.material.icons.Icons.Filled.FileDownload,
                            colorIcono = Ambar,
                            iconoFinal = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowForward,
                            onClick = {
                                submenuActivo = SubmenuLista.EXPORTAR
                            }
                        )
                        if (hayFiltrosParaRestablecer) {
                            SeparadorOpcionMenu()
                            ElementoMenuCompacto(
                                texto = "Restablecer filtros",
                                icono = Icons.Filled.Close,
                                colorIcono = Peligro,
                                colorTexto = Peligro,
                                onClick = {
                                    menuOpcionesDesplegado = false
                                    alRestablecerFiltros()
                                }
                            )
                        }
                    }
                    SubmenuLista.IMPORTAR -> {
                        ElementoRetornoSubmenu(
                            titulo = "Importar",
                            alVolver = { submenuActivo = SubmenuLista.PRINCIPAL }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Copia de seguridad (.bvda)",
                            icono = Icons.Filled.FileUpload,
                            onClick = {
                                menuOpcionesDesplegado = false
                                alIrCopiaSeguridad()
                            }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Contraseñas de Google (.csv)",
                            icono = Icons.Filled.FileUpload,
                            onClick = {
                                menuOpcionesDesplegado = false
                                alIrCsvGoogle()
                            }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Importación directa de llaves de paso y contraseñas",
                            icono = androidx.compose.material.icons.Icons.Filled.VpnKey,
                            onClick = {
                                menuOpcionesDesplegado = false
                                alImportarDirectoCxf()
                            }
                        )
                    }
                    SubmenuLista.EXPORTAR -> {
                        ElementoRetornoSubmenu(
                            titulo = "Exportar",
                            alVolver = { submenuActivo = SubmenuLista.PRINCIPAL }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Copia de seguridad completa (.bvda)",
                            icono = androidx.compose.material.icons.Icons.Filled.Backup,
                            onClick = {
                                menuOpcionesDesplegado = false
                                alIrCopiaSeguridadManual()
                            }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Exportación selectiva (.bvda)",
                            icono = Icons.Filled.FileDownload,
                            onClick = {
                                menuOpcionesDesplegado = false
                                alIrExportarSelectivo()
                            }
                        )
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Exportación directa de llaves de paso y contraseñas",
                            icono = androidx.compose.material.icons.Icons.Filled.VpnKey,
                            onClick = {
                                menuOpcionesDesplegado = false
                                alExportarDirectoCxf()
                            }
                        )
                    }
                }
            }
        }
    }
}

@BovedaPreview
@Composable
private fun PreviewBarraSuperiorLista() {
    PreviewTemaBoveda {
        BarraSuperiorLista(
            nombreBoveda = "Mi Bóveda Personal",
            totalEntradas = 42,
            busquedaVisible = false,
            busquedaActiva = false,
            tieneFiltrosActivos = false,
            soloFavoritos = false,
            agruparPorSitio = false,
            mostrarIndicadoresContenido = true,
            hayFiltrosParaRestablecer = false,
            alAbrirMenu = {},
            alAlternarBusqueda = {},
            alMostrarOrdenacion = {},
            alMostrarFiltros = {},
            alAlternarSoloFavoritos = {},
            alIrOrganizacionGrupo = {},
            alIrOrganizacionIndicadores = {},
            alIrExportarSelectivo = {},
            alIrCopiaSeguridadManual = {},
            alIrCopiaSeguridad = {},
            alIrCsvGoogle = {},
            alRestablecerFiltros = {}
        )
    }
}

