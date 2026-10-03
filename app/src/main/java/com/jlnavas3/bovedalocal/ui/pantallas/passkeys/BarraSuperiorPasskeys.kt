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
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VpnKey
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
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
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
    haptica: Haptica = Haptica(androidx.compose.ui.platform.LocalContext.current),
    alVolver: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alCambiarTextoBusqueda: (String) -> Unit,
    alCerrarBusqueda: () -> Unit,
    alAbrirMenu: () -> Unit,
    alCerrarMenu: () -> Unit,
    alAbrirOrdenacion: () -> Unit,
    alAlternarFavoritos: () -> Unit,
    alIrExportacionSelectiva: () -> Unit,
    alIrSeguridadBiometria: () -> Unit,
    alIrCopiaSeguridad: () -> Unit,
    alImportarPasskeys: () -> Unit = {},
    alExportarDirectoCxf: () -> Unit = {},
    alRestablecerFiltros: () -> Unit,
    modifier: Modifier = Modifier,
    idEtiqueta: String = "04-HER-PSK",
    mostrarId: Boolean = false
) {
    Column(modifier = modifier) {
        BarraSuperiorPantalla(
            titulo = "Llaves de paso",
            idEtiqueta = idEtiqueta,
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
                        .background(if (busquedaVisible || textoBusqueda.isNotBlank()) ColorPasskeys.copy(alpha = 0.16f) else ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar llaves de paso",
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
                        modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
                    ) {
                        com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                            texto = "Ordenar por...",
                            icono = Icons.AutoMirrored.Filled.Sort,
                            colorIcono = ColorPasskeys,
                            onClick = alAbrirOrdenacion
                        )
                        SeparadorOpcionMenu()
                        com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                            texto = if (soloFavoritos) "Ver todas las llaves" else "Solo favoritos",
                            icono = Icons.Filled.Star,
                            colorIcono = if (soloFavoritos) ColorAcento else ColorIconosInternos,
                            colorTexto = if (soloFavoritos) ColorAcento else TextoPrincipal,
                            onClick = alAlternarFavoritos
                        )
                        SeparadorOpcionMenu()
                        com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                            texto = "Exportación selectiva",
                            icono = Icons.Filled.FileUpload,
                            colorIcono = ColorPasskeys,
                            onClick = alIrExportacionSelectiva
                        )
                        SeparadorOpcionMenu()
                        com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                            texto = "Seguridad biométrica...",
                            icono = androidx.compose.material.icons.Icons.Filled.Fingerprint,
                            colorIcono = ColorPasskeys,
                            onClick = {
                                alCerrarMenu()
                                alIrSeguridadBiometria()
                            }
                        )
                        SeparadorOpcionMenu()
                        com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                            texto = "Copia de seguridad...",
                            icono = androidx.compose.material.icons.Icons.Filled.Backup,
                            colorIcono = ColorPasskeys,
                            onClick = {
                                alCerrarMenu()
                                alIrCopiaSeguridad()
                            }
                        )
                        SeparadorOpcionMenu()
                        com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                            texto = "Importar llaves de paso...",
                            icono = androidx.compose.material.icons.Icons.Filled.VpnKey,
                            colorIcono = ColorPasskeys,
                            onClick = {
                                alCerrarMenu()
                                alImportarPasskeys()
                            }
                        )
                        SeparadorOpcionMenu()
                        com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                            texto = "Exportación directa de llaves de paso...",
                            icono = androidx.compose.material.icons.Icons.Filled.VpnKey,
                            colorIcono = ColorPasskeys,
                            onClick = {
                                alCerrarMenu()
                                alExportarDirectoCxf()
                            }
                        )
                        if (soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ || textoBusqueda.isNotBlank()) {
                            SeparadorOpcionMenu()
                            com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
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

@BovedaPreview
@Composable
private fun PreviewBarraSuperiorPasskeys() {
    PreviewTemaBoveda {
        BarraSuperiorPasskeys(
            conSeparador = true,
            busquedaVisible = false,
            textoBusqueda = "",
            soloFavoritos = false,
            criterioOrdenacion = CriterioOrdenacion.NOMBRE_AZ,
            menuOpcionesDesplegado = false,
            alVolver = {},
            alAlternarBusqueda = {},
            alCambiarTextoBusqueda = {},
            alCerrarBusqueda = {},
            alAbrirMenu = {},
            alCerrarMenu = {},
            alAbrirOrdenacion = {},
            alAlternarFavoritos = {},
            alIrExportacionSelectiva = {},
            alIrSeguridadBiometria = {},
            alIrCopiaSeguridad = {},
            alRestablecerFiltros = {}
        )
    }
}

