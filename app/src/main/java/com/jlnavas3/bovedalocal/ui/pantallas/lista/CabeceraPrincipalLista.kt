package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion
import com.jlnavas3.bovedalocal.data.ModoVisualizacionIdentidades
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.seleccion.BarraSuperiorSeleccion
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.BarraCategoriasLista

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
    soloFavoritos: Boolean,
    filtroEtiqueta: String?,
    criterioOrdenacion: CriterioOrdenacion,
    agruparPorSitio: Boolean,
    mostrarIndicadoresContenido: Boolean,
    categorias: List<Categoria>,
    categoriaSeleccionadaId: String?,
    conteoPorCategoria: Map<String, Int>,
    alAbrirMenu: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alCambiarBusqueda: (String) -> Unit,
    alCerrarBusqueda: () -> Unit,
    alMostrarOrdenacion: () -> Unit,
    alMostrarFiltros: () -> Unit,
    alAlternarSoloFavoritos: () -> Unit,
    alIrOrganizacionGrupo: () -> Unit,
    alIrOrganizacionIndicadores: () -> Unit,
    alIrExportarSelectivo: () -> Unit,
    alIrCopiaSeguridadManual: () -> Unit,
    alIrCopiaSeguridad: () -> Unit,
    alIrCsvGoogle: () -> Unit,
    alImportarDirectoCxf: () -> Unit,
    alExportarDirectoCxf: () -> Unit,
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
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
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
                tieneFiltrosActivos = filtro != null || soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ,
                soloFavoritos = soloFavoritos,
                agruparPorSitio = agruparPorSitio,
                mostrarIndicadoresContenido = mostrarIndicadoresContenido,
                hayFiltrosParaRestablecer = filtro != null || soloFavoritos || filtroEtiqueta != null,
                alAbrirMenu = alAbrirMenu,
                alAlternarBusqueda = alAlternarBusqueda,
                alMostrarOrdenacion = alMostrarOrdenacion,
                alMostrarFiltros = alMostrarFiltros,
                alAlternarSoloFavoritos = alAlternarSoloFavoritos,
                alIrOrganizacionGrupo = alIrOrganizacionGrupo,
                alIrOrganizacionIndicadores = alIrOrganizacionIndicadores,
                alIrExportarSelectivo = alIrExportarSelectivo,
                alIrCopiaSeguridadManual = alIrCopiaSeguridadManual,
                alIrCopiaSeguridad = alIrCopiaSeguridad,
                alIrCsvGoogle = alIrCsvGoogle,
                alImportarDirectoCxf = alImportarDirectoCxf,
                alExportarDirectoCxf = alExportarDirectoCxf,
                alRestablecerFiltros = alRestablecerFiltros
            )

            AnimatedVisibility(
                visible = busquedaVisible || busqueda.isNotBlank(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 2.dp)
                ) {
                    BarraBusquedaAnimada(
                        valor = busqueda,
                        alCambiar = alCambiarBusqueda,
                        alCerrar = alCerrarBusqueda
                    )
                }
            }

            val esModoSecciones = modoVisualizacionIdentidades == ModoVisualizacionIdentidades.SECCIONES
            val mostrarChipsIdentidad = modoVisualizacionIdentidades == ModoVisualizacionIdentidades.CHIPS && identidades.isNotEmpty()

            if (!esModoSecciones) {
                if (jerarquiaOrganizacion == JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA) {
                    // Nivel 1: Identidades
                    if (mostrarChipsIdentidad) {
                        ChipsFiltroIdentidades(
                            identidades = identidades,
                            identidadSeleccionadaId = identidadSeleccionadaId,
                            conteoPorIdentidad = conteoPorIdentidad,
                            totalEntradas = totalEntradas,
                            conteoSinIdentidad = conteoSinIdentidad,
                            alSeleccionarIdentidad = alSeleccionarIdentidad
                        )
                        Spacer(Modifier.height(2.dp))
                    }

                    // Nivel 2 (Subordinado): Categorías
                    BarraCategoriasLista(
                        categorias = categorias,
                        categoriaSeleccionadaId = categoriaSeleccionadaId,
                        totalEntradas = totalEntradas,
                        conteoPorCategoria = conteoPorCategoria,
                        alSeleccionarCategoria = alSeleccionarCategoria,
                        alCrearCategoria = alCrearCategoria,
                        alEditarCategoria = alEditarCategoria,
                        alEliminarCategoria = alEliminarCategoria
                    )
                } else {
                    // Nivel 1: Categorías
                    BarraCategoriasLista(
                        categorias = categorias,
                        categoriaSeleccionadaId = categoriaSeleccionadaId,
                        totalEntradas = totalEntradas,
                        conteoPorCategoria = conteoPorCategoria,
                        alSeleccionarCategoria = alSeleccionarCategoria,
                        alCrearCategoria = alCrearCategoria,
                        alEditarCategoria = alEditarCategoria,
                        alEliminarCategoria = alEliminarCategoria
                    )

                    // Nivel 2 (Subordinado): Identidades
                    if (mostrarChipsIdentidad) {
                        Spacer(Modifier.height(2.dp))
                        ChipsFiltroIdentidades(
                            identidades = identidades,
                            identidadSeleccionadaId = identidadSeleccionadaId,
                            conteoPorIdentidad = conteoPorIdentidad,
                            totalEntradas = totalEntradas,
                            conteoSinIdentidad = conteoSinIdentidad,
                            alSeleccionarIdentidad = alSeleccionarIdentidad
                        )
                    }
                }
            }
        }
    }
}
