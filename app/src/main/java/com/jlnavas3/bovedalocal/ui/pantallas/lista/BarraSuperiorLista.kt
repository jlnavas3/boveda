/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import com.jlnavas3.bovedalocal.ui.componentes.hud.accionConHud
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
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
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
    criterioOrdenacionActual: com.jlnavas3.bovedalocal.ui.CriterioOrdenacion = com.jlnavas3.bovedalocal.ui.CriterioOrdenacion.NOMBRE_AZ,
    filtroTipoActual: com.jlnavas3.bovedalocal.data.TipoEntrada? = null,
    alAbrirMenu: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alSeleccionarCriterioOrdenacion: (com.jlnavas3.bovedalocal.ui.CriterioOrdenacion) -> Unit = {},
    alSeleccionarFiltroTipo: (com.jlnavas3.bovedalocal.data.TipoEntrada?) -> Unit = {},
    alCrearTipo: (com.jlnavas3.bovedalocal.data.TipoEntrada) -> Unit = {},
    alAlternarSoloFavoritos: () -> Unit,
    alIrNormalizadorTitulos: () -> Unit = {},
    alIrTema: () -> Unit = {},
    alIrFormas: () -> Unit = {},
    alIrTipografia: () -> Unit = {},
    alIrOrganizacionGrupo: () -> Unit,
    alIrOrganizacionIndicadores: () -> Unit,
    alIrIndiceAz: () -> Unit = {},
    alIrTransiciones: () -> Unit = {},
    alIrExportarSelectivo: () -> Unit,
    alIrCopiaSeguridadManual: () -> Unit,
    alIrCopiaSeguridad: () -> Unit,
    alIrCsvGoogle: () -> Unit,
    alImportarGoogleAuthenticator: () -> Unit = {},
    alImportarDirectoCxf: () -> Unit = {},
    alImportarContactos: () -> Unit = {},
    alExportarDirectoCxf: () -> Unit = {},
    alIrCopiaRapida: () -> Unit = {},
    alIrFormatosCampos: () -> Unit = {},
    alIrSeguridadDatos: () -> Unit = {},
    alIrColoresIds: () -> Unit = {},
    alIrDatosTecnicosPasskey: () -> Unit = {},
    alIrUmbralContrasenas: () -> Unit = {},
    alIrContrasenasComunes: () -> Unit = {},
    alIrPlantillasPersonalizadas: () -> Unit = {},
    alIrAjustesGenerales: () -> Unit = {},
    alAbrirAyuda: () -> Unit = {},
    alRestablecerFiltros: () -> Unit,
    mostrarCampanaAvisos: Boolean = true,
    cantidadAvisos: Int = 0,
    alAbrirAvisos: () -> Unit = {},
    alAbrirSalud: () -> Unit = {},
    cantidadProblemasSalud: Int = 0
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
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .accionConHud(
                    etiqueta = "Menú lateral",
                    icono = Icons.Filled.Menu,
                    radioRipple = 20.dp,
                    alPulsar = { haptica.toque(); alAbrirMenu() }
                ),
            contentAlignment = Alignment.Center
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
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .then(
                    if (busquedaVisible || busquedaActiva) {
                        Modifier.background(ColorAcento.copy(alpha = 0.16f))
                    } else Modifier
                )
                .accionConHud(
                    etiqueta = "Buscar en la bóveda",
                    icono = Icons.Filled.Search,
                    radioRipple = 20.dp,
                    alPulsar = {
                        haptica.tic()
                        alAlternarBusqueda()
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Buscar",
                tint = if (busquedaVisible || busquedaActiva) ColorAcento else ColorIconosInternos,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(6.dp))

        // Botón Tres Puntos (Filtros, Ordenación y Ajustes Contextuales)
        Box {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .then(
                        if (tieneFiltrosActivos) {
                            Modifier.background(ColorAcento.copy(alpha = 0.16f))
                        } else Modifier
                    )
                    .accionConHud(
                        etiqueta = "Más opciones",
                        icono = Icons.Filled.MoreVert,
                        radioRipple = 20.dp,
                        alPulsar = {
                            haptica.tic()
                            menuOpcionesDesplegado = true
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Más opciones",
                    tint = if (tieneFiltrosActivos) ColorAcento else ColorIconosInternos,
                    modifier = Modifier.size(20.dp)
                )
            }

            MenuOpcionesLista(
                expandido = menuOpcionesDesplegado,
                soloFavoritos = soloFavoritos,
                hayFiltrosParaRestablecer = hayFiltrosParaRestablecer,
                criterioOrdenacionActual = criterioOrdenacionActual,
                filtroTipoActual = filtroTipoActual,
                alDescartar = { menuOpcionesDesplegado = false },
                alCrearTipo = alCrearTipo,
                alSeleccionarCriterioOrdenacion = alSeleccionarCriterioOrdenacion,
                alSeleccionarFiltroTipo = alSeleccionarFiltroTipo,
                alAlternarSoloFavoritos = alAlternarSoloFavoritos,
                alIrNormalizadorTitulos = alIrNormalizadorTitulos,
                alIrExportarSelectivo = alIrExportarSelectivo,
                alIrCopiaSeguridadManual = alIrCopiaSeguridadManual,
                alIrCopiaSeguridad = alIrCopiaSeguridad,
                alIrCsvGoogle = alIrCsvGoogle,
                alImportarGoogleAuthenticator = alImportarGoogleAuthenticator,
                alImportarDirectoCxf = alImportarDirectoCxf,
                alImportarContactos = alImportarContactos,
                alExportarDirectoCxf = alExportarDirectoCxf,
                alIrTema = alIrTema,
                alIrFormas = alIrFormas,
                alIrTipografia = alIrTipografia,
                alIrOrganizacionGrupo = alIrOrganizacionGrupo,
                alIrOrganizacionIndicadores = alIrOrganizacionIndicadores,
                alIrIndiceAz = alIrIndiceAz,
                alIrTransiciones = alIrTransiciones,
                alIrSeguridadDatos = alIrSeguridadDatos,
                alIrAjustesGenerales = alIrAjustesGenerales,
                alAbrirAyuda = alAbrirAyuda,
                alRestablecerFiltros = alRestablecerFiltros
            )
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
