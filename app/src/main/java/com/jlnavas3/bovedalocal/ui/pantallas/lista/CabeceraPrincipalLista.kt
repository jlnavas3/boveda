/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion
import com.jlnavas3.bovedalocal.data.ModoVisualizacionIdentidades
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraSuperiorSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras.BarraPildorasFiltrosAnimada

/**
 * Cabecera superior de PantallaLista:
 * Alterna entre la barra de selección múltiple y la barra superior estándar con buscador animado,
 * identidades y categorías organizadas jerárquicamente.
 */
@Composable
fun CabeceraPrincipalLista(
    modoSeleccion: Boolean,
    cantidadSeleccionados: Int,
    todoSeleccionado: Boolean,
    alCancelarSeleccion: () -> Unit,
    alSeleccionarTodo: () -> Unit,
    alDeseleccionarTodo: () -> Unit,
    nombreBoveda: String,
    totalEntradas: Int,
    busquedaVisible: Boolean,
    busqueda: String,
    filtro: TipoEntrada?,
    conteoPorTipo: Map<TipoEntrada, Int> = emptyMap(),
    soloFavoritos: Boolean,
    solo2fa: Boolean = false,
    conteo2fa: Int = 0,
    alSeleccionar2fa: () -> Unit = {},
    filtroEtiqueta: String?,
    criterioOrdenacion: CriterioOrdenacion,
    agruparPorSitio: Boolean,
    mostrarIndicadoresContenido: Boolean,
    categorias: List<Categoria>,
    categoriaSeleccionadaId: String?,
    conteoPorCategoria: Map<String, Int>,
    alSeleccionarCriterioOrdenacion: (CriterioOrdenacion) -> Unit = {},
    alSeleccionarFiltroTipo: (TipoEntrada?) -> Unit = {},
    alCrearTipo: (TipoEntrada) -> Unit = {},
    alAbrirMenu: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alCambiarBusqueda: (String) -> Unit,
    alCerrarBusqueda: () -> Unit,
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
    alImportarDirectoCxf: () -> Unit,
    alImportarContactos: () -> Unit = {},
    alExportarDirectoCxf: () -> Unit,
    alIrCopiaRapida: () -> Unit = {},
    alIrFormatosCampos: () -> Unit = {},
    alIrSeguridadDatos: () -> Unit = {},
    alIrColoresIds: () -> Unit = {},
    alIrDatosTecnicosPasskey: () -> Unit = {},
    alIrUmbralContrasenas: () -> Unit = {},
    alIrContrasenasComunes: () -> Unit = {},
    alIrPlantillasPersonalizadas: () -> Unit = {},
    alIrAjustesGenerales: () -> Unit = {},
    alRestablecerFiltros: () -> Unit,
    alSeleccionarCategoria: (String?) -> Unit,
    alCrearCategoria: () -> Unit,
    alEditarCategoria: (Categoria) -> Unit,
    alEliminarCategoria: (Categoria) -> Unit,
    jerarquiaOrganizacion: JerarquiaOrganizacion = JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA,
    modoVisualizacionIdentidades: ModoVisualizacionIdentidades = ModoVisualizacionIdentidades.CHIPS,
    identidades: List<Identidad> = emptyList(),
    identidadSeleccionadaId: String? = null,
    conteoPorIdentidad: Map<String, Int> = emptyMap(),
    conteoSinIdentidad: Int = 0,
    alSeleccionarIdentidad: (String?) -> Unit = {},
    barraPildorasVisible: Boolean = true,
    etiquetasDisponibles: List<String> = emptyList(),
    alAbrirSelectorIdentidad: () -> Unit = {},
    alAbrirSelectorCategoria: () -> Unit = {},
    alAbrirSelectorEtiqueta: () -> Unit = {},
    alGestionarIdentidades: () -> Unit = {},
    alGestionarCategorias: () -> Unit = {},
    conteoPorEtiqueta: Map<String, Int> = emptyMap(),
    alSeleccionarEtiqueta: (String?) -> Unit = {},
    alLimpiarTipo: () -> Unit = {},
    mostrarCampanaAvisos: Boolean = true,
    cantidadAvisos: Int = 0,
    alAbrirAvisos: () -> Unit = {},
    alAbrirSalud: () -> Unit = {},
    cantidadProblemasSalud: Int = 0,
    alAbrirAyuda: () -> Unit = {},
    entradas: List<Entrada> = emptyList(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorAjustesFondo)
            .zIndex(10f)
    ) {
        if (modoSeleccion) {
            BarraSuperiorSeleccion(
                cantidad = cantidadSeleccionados,
                todoSeleccionado = todoSeleccionado,
                alCancelar = alCancelarSeleccion,
                alSeleccionarTodo = alSeleccionarTodo,
                alDeseleccionarTodo = alDeseleccionarTodo
            )
        } else {
            BarraSuperiorLista(
                nombreBoveda = nombreBoveda,
                totalEntradas = totalEntradas,
                busquedaVisible = busquedaVisible,
                busquedaActiva = busqueda.isNotBlank(),
                tieneFiltrosActivos = filtro != null || solo2fa || soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ,
                soloFavoritos = soloFavoritos,
                agruparPorSitio = agruparPorSitio,
                mostrarIndicadoresContenido = mostrarIndicadoresContenido,
                hayFiltrosParaRestablecer = filtro != null || solo2fa || soloFavoritos || filtroEtiqueta != null,
                criterioOrdenacionActual = criterioOrdenacion,
                filtroTipoActual = filtro,
                alAbrirMenu = alAbrirMenu,
                alAlternarBusqueda = alAlternarBusqueda,
                alSeleccionarCriterioOrdenacion = alSeleccionarCriterioOrdenacion,
                alSeleccionarFiltroTipo = alSeleccionarFiltroTipo,
                alCrearTipo = alCrearTipo,
                alAlternarSoloFavoritos = alAlternarSoloFavoritos,
                alIrNormalizadorTitulos = alIrNormalizadorTitulos,
                alIrTema = alIrTema,
                alIrFormas = alIrFormas,
                alIrTipografia = alIrTipografia,
                alIrOrganizacionGrupo = alIrOrganizacionGrupo,
                alIrOrganizacionIndicadores = alIrOrganizacionIndicadores,
                alIrIndiceAz = alIrIndiceAz,
                alIrTransiciones = alIrTransiciones,
                alIrExportarSelectivo = alIrExportarSelectivo,
                alIrCopiaSeguridadManual = alIrCopiaSeguridadManual,
                alIrCopiaSeguridad = alIrCopiaSeguridad,
                alIrCsvGoogle = alIrCsvGoogle,
                alImportarGoogleAuthenticator = alImportarGoogleAuthenticator,
                alImportarDirectoCxf = alImportarDirectoCxf,
                alImportarContactos = alImportarContactos,
                alExportarDirectoCxf = alExportarDirectoCxf,
                alIrCopiaRapida = alIrCopiaRapida,
                alIrFormatosCampos = alIrFormatosCampos,
                alIrSeguridadDatos = alIrSeguridadDatos,
                alIrColoresIds = alIrColoresIds,
                alIrDatosTecnicosPasskey = alIrDatosTecnicosPasskey,
                alIrUmbralContrasenas = alIrUmbralContrasenas,
                alIrContrasenasComunes = alIrContrasenasComunes,
                alIrPlantillasPersonalizadas = alIrPlantillasPersonalizadas,
                alIrAjustesGenerales = alIrAjustesGenerales,
                alAbrirAyuda = alAbrirAyuda,
                alRestablecerFiltros = alRestablecerFiltros,
                mostrarCampanaAvisos = mostrarCampanaAvisos,
                cantidadAvisos = cantidadAvisos,
                alAbrirAvisos = alAbrirAvisos,
                alAbrirSalud = alAbrirSalud,
                cantidadProblemasSalud = cantidadProblemasSalud
            )

            AnimatedVisibility(
                visible = busquedaVisible || busqueda.isNotBlank(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 4.dp)
                ) {
                    BarraBusquedaAnimada(
                        valor = busqueda,
                        alCambiar = alCambiarBusqueda,
                        alCerrar = alCerrarBusqueda,
                        entradas = entradas,
                        categorias = categorias,
                        etiquetasDisponibles = etiquetasDisponibles,
                        alAbrirAyuda = alAbrirAyuda
                    )
                }
            }

            val mostrarPildoraIdentidad = modoVisualizacionIdentidades != ModoVisualizacionIdentidades.DESACTIVADO && identidades.isNotEmpty()
            val mostrarPildoraCategoria = categorias.isNotEmpty()
            val mostrarPildoraEtiqueta = etiquetasDisponibles.isNotEmpty()

            BarraPildorasFiltrosAnimada(
                visible = barraPildorasVisible,
                soloFavoritos = soloFavoritos,
                filtroTipo = filtro,
                solo2fa = solo2fa,
                alAlternarFavoritos = alAlternarSoloFavoritos,
                alLimpiarTipo = alLimpiarTipo,
                alSeleccionarTipo = alSeleccionarFiltroTipo,
                alSeleccionar2fa = alSeleccionar2fa,
                conteoPorTipo = conteoPorTipo,
                conteo2fa = conteo2fa,
                mostrarPildoraIdentidad = mostrarPildoraIdentidad,
                identidades = identidades,
                identidadSeleccionadaId = identidadSeleccionadaId,
                conteoPorIdentidad = conteoPorIdentidad,
                conteoSinIdentidad = conteoSinIdentidad,
                alAbrirSelectorIdentidad = alAbrirSelectorIdentidad,
                alSeleccionarIdentidad = alSeleccionarIdentidad,
                alGestionarIdentidades = alGestionarIdentidades,
                mostrarPildoraCategoria = mostrarPildoraCategoria,
                categorias = categorias,
                categoriaSeleccionadaId = categoriaSeleccionadaId,
                conteoPorCategoria = conteoPorCategoria,
                alAbrirSelectorCategoria = alAbrirSelectorCategoria,
                alSeleccionarCategoria = alSeleccionarCategoria,
                alGestionarCategorias = alGestionarCategorias,
                mostrarPildoraEtiqueta = mostrarPildoraEtiqueta,
                filtroEtiqueta = filtroEtiqueta,
                etiquetasDisponibles = etiquetasDisponibles,
                conteoPorEtiqueta = conteoPorEtiqueta,
                totalEntradas = totalEntradas,
                alAbrirSelectorEtiqueta = alAbrirSelectorEtiqueta,
                alSeleccionarEtiqueta = alSeleccionarEtiqueta
            )
        }
    }
}
