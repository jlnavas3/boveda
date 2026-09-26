package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileDownload
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
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star

@Composable
fun BarraSuperiorAutenticador(
    busquedaVisible: Boolean,
    textoBusqueda: String,
    soloFavoritos: Boolean,
    criterioOrdenacion: CriterioOrdenacion,
    conSeparador: Boolean,
    haptica: Haptica,
    alVolver: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alEscanearQr: () -> Unit,
    alAgregarManual: () -> Unit,
    alSolicitarOrdenacion: () -> Unit,
    alAlternarFavoritos: () -> Unit,
    alExportarSelectivo: () -> Unit,
    alImportarGoogleAuthenticator: () -> Unit,
    alMostrarComoFunciona: () -> Unit,
    alRestablecerFiltros: () -> Unit
) {
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }
    val tieneFiltrosActivos = soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ

    BarraSuperiorPantalla(
        titulo = "Autenticador 2FA",
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
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (busquedaVisible || textoBusqueda.isNotBlank()) Color2FA.copy(alpha = 0.16f) else ColorTarjetaAjustes)
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Buscar cuentas 2FA",
                    tint = if (busquedaVisible || textoBusqueda.isNotBlank()) Color2FA else ColorIconosInternos,
                    modifier = Modifier.size(19.dp)
                )
            }

            Spacer(Modifier.width(5.dp))

            // Botón Escanear QR
            IconButton(
                onClick = {
                    haptica.toque()
                    alEscanearQr()
                },
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(ColorTarjetaAjustes)
            ) {
                Icon(
                    imageVector = Icons.Filled.QrCodeScanner,
                    contentDescription = "Escanear código QR",
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(5.dp))

            // Botón Añadir clave manual (+)
            IconButton(
                onClick = {
                    haptica.toque()
                    alAgregarManual()
                },
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(ColorTarjetaAjustes)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Escribir clave manualmente",
                    tint = Color2FA,
                    modifier = Modifier.size(21.dp)
                )
            }

            Spacer(Modifier.width(5.dp))

            // Botón Tres Puntos (Menú desplegable)
            Box {
                IconButton(
                    onClick = {
                        haptica.tic()
                        menuOpcionesDesplegado = true
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (tieneFiltrosActivos) Color2FA.copy(alpha = 0.16f) else ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Más opciones",
                        tint = if (tieneFiltrosActivos) Color2FA else ColorIconosInternos,
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
                            Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = null, tint = Color2FA, modifier = Modifier.size(20.dp))
                        },
                        text = { Text("Ordenar por...", color = TextoPrincipal) },
                        onClick = {
                            menuOpcionesDesplegado = false
                            alSolicitarOrdenacion()
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
                            alAlternarFavoritos()
                        }
                    )
                    SeparadorOpcionMenu()
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(Icons.Filled.FileDownload, contentDescription = null, tint = Color2FA, modifier = Modifier.size(20.dp))
                        },
                        text = { Text("Exportación selectiva", color = TextoPrincipal) },
                        onClick = {
                            menuOpcionesDesplegado = false
                            haptica.tic()
                            alExportarSelectivo()
                        }
                    )
                    SeparadorOpcionMenu()
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(Icons.Filled.FileUpload, contentDescription = null, tint = ColorExportacion, modifier = Modifier.size(20.dp))
                        },
                        text = { Text("Importar de Google Authenticator", color = TextoPrincipal) },
                        onClick = {
                            menuOpcionesDesplegado = false
                            haptica.tic()
                            alImportarGoogleAuthenticator()
                        }
                    )
                    SeparadorOpcionMenu()
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = null, tint = Color2FA, modifier = Modifier.size(20.dp))
                        },
                        text = { Text("¿Cómo funciona el 2FA?", color = TextoPrincipal) },
                        onClick = {
                            menuOpcionesDesplegado = false
                            haptica.tic()
                            alMostrarComoFunciona()
                        }
                    )
                    if (soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ || textoBusqueda.isNotBlank()) {
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
    )
}
