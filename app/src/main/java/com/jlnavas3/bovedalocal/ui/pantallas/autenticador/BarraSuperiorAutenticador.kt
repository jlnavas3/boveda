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
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
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
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda

@Composable
fun BarraSuperiorAutenticador(
    busquedaVisible: Boolean,
    textoBusqueda: String,
    soloFavoritos: Boolean,
    criterioOrdenacion: CriterioOrdenacion,
    conSeparador: Boolean,
    haptica: Haptica = Haptica(androidx.compose.ui.platform.LocalContext.current),
    alVolver: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alSolicitarOrdenacion: () -> Unit,
    alAlternarFavoritos: () -> Unit,
    alExportarSelectivo: () -> Unit,
    alImportarGoogleAuthenticator: () -> Unit,
    alMostrarComoFunciona: () -> Unit,
    alIrAjustesAutenticador: () -> Unit,
    alIrAjustesWidgetTotp: () -> Unit,
    alRestablecerFiltros: () -> Unit,
    idEtiqueta: String = "04-HER-2FA",
    mostrarId: Boolean = false
) {
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }
    val tieneFiltrosActivos = soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ

    BarraSuperiorPantalla(
        titulo = "Verificación en dos pasos",
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
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (busquedaVisible || textoBusqueda.isNotBlank()) Color2FA.copy(alpha = 0.16f) else ColorTarjetaAjustes)
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Buscar cuentas",
                    tint = if (busquedaVisible || textoBusqueda.isNotBlank()) Color2FA else ColorIconosInternos,
                    modifier = Modifier.size(19.dp)
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
                    modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
                ) {
                    com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                        texto = "Ordenar por...",
                        icono = Icons.AutoMirrored.Filled.Sort,
                        colorIcono = Color2FA,
                        onClick = {
                            menuOpcionesDesplegado = false
                            alSolicitarOrdenacion()
                        }
                    )
                    SeparadorOpcionMenu()
                    com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                        texto = if (soloFavoritos) "Ver todas las cuentas" else "Solo favoritos",
                        icono = Icons.Filled.Star,
                        colorIcono = if (soloFavoritos) ColorAcento else ColorIconosInternos,
                        colorTexto = if (soloFavoritos) ColorAcento else TextoPrincipal,
                        onClick = {
                            menuOpcionesDesplegado = false
                            haptica.tic()
                            alAlternarFavoritos()
                        }
                    )
                    SeparadorOpcionMenu()
                    com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                        texto = "Exportación selectiva",
                        icono = Icons.Filled.FileUpload,
                        colorIcono = Color2FA,
                        onClick = {
                            menuOpcionesDesplegado = false
                            haptica.tic()
                            alExportarSelectivo()
                        }
                    )
                    SeparadorOpcionMenu()
                    com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                        texto = "Importar de Google Auth",
                        icono = Icons.Filled.FileDownload,
                        colorIcono = ColorExportacion,
                        onClick = {
                            menuOpcionesDesplegado = false
                            haptica.tic()
                            alImportarGoogleAuthenticator()
                        }
                    )
                    SeparadorOpcionMenu()
                    com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                        texto = "Ajustes de dos pasos...",
                        icono = androidx.compose.material.icons.Icons.Filled.Tune,
                        colorIcono = Color2FA,
                        onClick = {
                            menuOpcionesDesplegado = false
                            alIrAjustesAutenticador()
                        }
                    )
                    SeparadorOpcionMenu()
                    com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                        texto = "Ajustes de widget TOTP...",
                        icono = androidx.compose.material.icons.Icons.Filled.Widgets,
                        colorIcono = Color2FA,
                        onClick = {
                            menuOpcionesDesplegado = false
                            alIrAjustesWidgetTotp()
                        }
                    )
                    SeparadorOpcionMenu()
                    com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                        texto = "¿Cómo funciona el 2FA?",
                        icono = Icons.AutoMirrored.Filled.HelpOutline,
                        colorIcono = Color2FA,
                        onClick = {
                            menuOpcionesDesplegado = false
                            haptica.tic()
                            alMostrarComoFunciona()
                        }
                    )
                    if (soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ || textoBusqueda.isNotBlank()) {
                        SeparadorOpcionMenu()
                        com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto(
                            texto = "Restablecer filtros",
                            icono = Icons.Filled.Close,
                            colorIcono = Peligro,
                            colorTexto = Peligro,
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

@BovedaPreview
@Composable
private fun PreviewBarraSuperiorAutenticador() {
    PreviewTemaBoveda {
        BarraSuperiorAutenticador(
            busquedaVisible = false,
            textoBusqueda = "",
            soloFavoritos = false,
            criterioOrdenacion = CriterioOrdenacion.NOMBRE_AZ,
            conSeparador = true,
            alVolver = {},
            alAlternarBusqueda = {},
            alSolicitarOrdenacion = {},
            alAlternarFavoritos = {},
            alExportarSelectivo = {},
            alImportarGoogleAuthenticator = {},
            alMostrarComoFunciona = {},
            alIrAjustesAutenticador = {},
            alIrAjustesWidgetTotp = {},
            alRestablecerFiltros = {}
        )
    }
}

