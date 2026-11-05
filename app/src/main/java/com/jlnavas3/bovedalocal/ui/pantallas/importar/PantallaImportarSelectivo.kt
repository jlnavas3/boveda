/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.OpcionesImportacionBvda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla dedicada de importación selectiva de archivos de respaldo (.bvda).
 * Permite al usuario elegir minuciosamente qué tipos de entradas y bloques de configuración restaurar.
 */
@Composable
fun PantallaImportarSelectivo(
    vm: VaultViewModel,
    contenido: ContenidoBoveda,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustesActuales by vm.ajustes.collectAsStateWithLifecycle()

    val conteoPorTipo = remember(contenido) {
        contenido.entradas.groupingBy { it.tipo }.eachCount()
    }
    val tiposPresentes = remember(conteoPorTipo) { conteoPorTipo.keys }

    val hayConfiguracion = contenido.configuracion != null
    val hayCategorias = contenido.categorias.isNotEmpty()
    val hayIdentidades = contenido.identidades.isNotEmpty()
    val hayPlantillas = contenido.configuracion?.plantillasCamposPersonalizadas?.isNotEmpty() == true

    var tiposSeleccionados by remember { mutableStateOf(tiposPresentes) }
    var importarCategorias by remember { mutableStateOf(hayCategorias) }
    var importarIdentidades by remember { mutableStateOf(hayIdentidades) }
    var importarReglasUrls by remember { mutableStateOf(hayConfiguracion) }
    var importarReglasHomelab by remember { mutableStateOf(hayConfiguracion) }
    var importarAutofillDeteccion by remember { mutableStateOf(hayConfiguracion) }
    var importarMapeosApps by remember { mutableStateOf(hayConfiguracion) }
    var importarPlantillas by remember { mutableStateOf(hayPlantillas) }
    var importarSeguridadTiempos by remember { mutableStateOf(hayConfiguracion) }
    var importarSeguridadCripto by remember { mutableStateOf(hayConfiguracion) }
    var importarSeguridadVisual by remember { mutableStateOf(hayConfiguracion) }
    var importarFormatosLista by remember { mutableStateOf(hayConfiguracion) }
    var importarGeneradorTotp by remember { mutableStateOf(hayConfiguracion) }
    var importarRetencionPapelera by remember { mutableStateOf(hayConfiguracion) }

    var modoDetallado by remember { mutableStateOf(false) }

    val totalEntradasSeleccionadas = remember(tiposSeleccionados, conteoPorTipo) {
        tiposSeleccionados.sumOf { conteoPorTipo[it] ?: 0 }
    }

    val opciones = remember(
        tiposSeleccionados,
        importarCategorias,
        importarIdentidades,
        importarReglasUrls,
        importarReglasHomelab,
        importarAutofillDeteccion,
        importarMapeosApps,
        importarPlantillas,
        importarSeguridadTiempos,
        importarSeguridadCripto,
        importarSeguridadVisual,
        importarFormatosLista,
        importarGeneradorTotp,
        importarRetencionPapelera
    ) {
        OpcionesImportacionBvda(
            tiposEntradaSeleccionados = tiposSeleccionados,
            importarCategorias = importarCategorias,
            importarIdentidades = importarIdentidades,
            importarReglasUrls = importarReglasUrls,
            importarReglasHomelab = importarReglasHomelab,
            importarAutofillDeteccion = importarAutofillDeteccion,
            importarMapeosApps = importarMapeosApps,
            importarPlantillas = importarPlantillas,
            importarSeguridadTiempos = importarSeguridadTiempos,
            importarSeguridadCripto = importarSeguridadCripto,
            importarSeguridadVisual = importarSeguridadVisual,
            importarFormatosLista = importarFormatosLista,
            importarGeneradorTotp = importarGeneradorTotp,
            importarRetencionPapelera = importarRetencionPapelera
        )
    }

    val totalSeleccionados = remember(opciones, totalEntradasSeleccionadas) {
        opciones.totalSeleccionados(totalEntradasSeleccionadas)
    }

    val subtitulo = remember(totalSeleccionados) {
        if (totalSeleccionados == 1) "1 elemento seleccionado"
        else "$totalSeleccionados elementos seleccionados"
    }

    val alSeleccionarTodo = {
        haptica.tic()
        tiposSeleccionados = tiposPresentes
        if (hayCategorias) importarCategorias = true
        if (hayIdentidades) importarIdentidades = true
        if (hayConfiguracion) {
            importarReglasUrls = true
            importarReglasHomelab = true
            importarAutofillDeteccion = true
            importarMapeosApps = true
            if (hayPlantillas) importarPlantillas = true
            importarSeguridadTiempos = true
            importarSeguridadCripto = true
            importarSeguridadVisual = true
            importarFormatosLista = true
            importarGeneradorTotp = true
            importarRetencionPapelera = true
        }
    }

    val alDeseleccionarTodo = {
        haptica.tic()
        tiposSeleccionados = emptySet()
        importarCategorias = false
        importarIdentidades = false
        importarReglasUrls = false
        importarReglasHomelab = false
        importarAutofillDeteccion = false
        importarMapeosApps = false
        importarPlantillas = false
        importarSeguridadTiempos = false
        importarSeguridadCripto = false
        importarSeguridadVisual = false
        importarFormatosLista = false
        importarGeneradorTotp = false
        importarRetencionPapelera = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorImportar(
            subtitulo = subtitulo,
            alVolver = { vm.volverAtras() },
            modoDetallado = modoDetallado,
            alAlternarModoDetallado = {
                haptica.tic()
                modoDetallado = !modoDetallado
            },
            alSeleccionarTodo = alSeleccionarTodo,
            alDeseleccionarTodo = alDeseleccionarTodo
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SeccionEntradasImportacion(
                conteoPorTipo = conteoPorTipo,
                tiposSeleccionados = tiposSeleccionados,
                alAlternarTipo = { tipo ->
                    haptica.tic()
                    tiposSeleccionados = if (tiposSeleccionados.contains(tipo)) {
                        tiposSeleccionados - tipo
                    } else {
                        tiposSeleccionados + tipo
                    }
                }
            )

            SeccionEstructuraImportacion(
                categoriasList = contenido.categorias,
                importarCategorias = importarCategorias,
                alAlternarCategorias = {
                    haptica.tic()
                    importarCategorias = !importarCategorias
                },
                identidadesList = contenido.identidades,
                importarIdentidades = importarIdentidades,
                alAlternarIdentidades = {
                    haptica.tic()
                    importarIdentidades = !importarIdentidades
                },
                modoDetallado = modoDetallado
            )

            SeccionReglasImportacion(
                config = contenido.configuracion,
                actual = ajustesActuales,
                importarReglasUrls = importarReglasUrls,
                alAlternarReglasUrls = {
                    haptica.tic()
                    importarReglasUrls = !importarReglasUrls
                },
                importarReglasHomelab = importarReglasHomelab,
                alAlternarReglasHomelab = {
                    haptica.tic()
                    importarReglasHomelab = !importarReglasHomelab
                },
                modoDetallado = modoDetallado
            )

            SeccionAutofillImportacion(
                config = contenido.configuracion,
                actual = ajustesActuales,
                importarAutofillDeteccion = importarAutofillDeteccion,
                alAlternarAutofillDeteccion = {
                    haptica.tic()
                    importarAutofillDeteccion = !importarAutofillDeteccion
                },
                importarMapeosApps = importarMapeosApps,
                alAlternarMapeosApps = {
                    haptica.tic()
                    importarMapeosApps = !importarMapeosApps
                },
                modoDetallado = modoDetallado
            )

            SeccionPlantillasImportacion(
                config = contenido.configuracion,
                importarPlantillas = importarPlantillas,
                alAlternarPlantillas = {
                    haptica.tic()
                    importarPlantillas = !importarPlantillas
                },
                modoDetallado = modoDetallado
            )

            SeccionSeguridadImportacion(
                config = contenido.configuracion,
                actual = ajustesActuales,
                importarSeguridadTiempos = importarSeguridadTiempos,
                alAlternarSeguridadTiempos = {
                    haptica.tic()
                    importarSeguridadTiempos = !importarSeguridadTiempos
                },
                importarSeguridadCripto = importarSeguridadCripto,
                alAlternarSeguridadCripto = {
                    haptica.tic()
                    importarSeguridadCripto = !importarSeguridadCripto
                },
                importarSeguridadVisual = importarSeguridadVisual,
                alAlternarSeguridadVisual = {
                    haptica.tic()
                    importarSeguridadVisual = !importarSeguridadVisual
                },
                modoDetallado = modoDetallado
            )

            SeccionFormatosImportacion(
                config = contenido.configuracion,
                actual = ajustesActuales,
                importarFormatosLista = importarFormatosLista,
                alAlternarFormatosLista = {
                    haptica.tic()
                    importarFormatosLista = !importarFormatosLista
                },
                importarGeneradorTotp = importarGeneradorTotp,
                alAlternarGeneradorTotp = {
                    haptica.tic()
                    importarGeneradorTotp = !importarGeneradorTotp
                },
                importarRetencionPapelera = importarRetencionPapelera,
                alAlternarRetencionPapelera = {
                    haptica.tic()
                    importarRetencionPapelera = !importarRetencionPapelera
                },
                modoDetallado = modoDetallado
            )
        }

        BarraAccionesImportarSelectivo(
            totalSeleccionados = totalSeleccionados,
            alImportar = {
                haptica.exito()
                vm.ejecutarImportacionSelectiva(contenido, opciones)
            },
            alCancelar = {
                haptica.tic()
                vm.volverAtras()
            }
        )
    }
}
