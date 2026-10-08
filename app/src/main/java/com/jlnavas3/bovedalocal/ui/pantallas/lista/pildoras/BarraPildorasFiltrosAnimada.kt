package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * Envoltorio animado para la barra de píldoras de filtrado.
 * Permite el auto-ocultamiento suave detrás de la barra de búsqueda al hacer scroll.
 */
@Composable
fun BarraPildorasFiltrosAnimada(
    visible: Boolean,
    soloFavoritos: Boolean,
    filtroTipo: TipoEntrada?,
    alAlternarFavoritos: () -> Unit,
    alLimpiarTipo: () -> Unit,
    mostrarPildoraIdentidad: Boolean,
    identidades: List<Identidad>,
    identidadSeleccionadaId: String?,
    conteoPorIdentidad: Map<String, Int>,
    conteoSinIdentidad: Int,
    alAbrirSelectorIdentidad: () -> Unit,
    alSeleccionarIdentidad: (String?) -> Unit,
    mostrarPildoraCategoria: Boolean,
    categorias: List<Categoria>,
    categoriaSeleccionadaId: String?,
    conteoPorCategoria: Map<String, Int>,
    alAbrirSelectorCategoria: () -> Unit,
    alSeleccionarCategoria: (String?) -> Unit,
    mostrarPildoraEtiqueta: Boolean,
    filtroEtiqueta: String?,
    alAbrirSelectorEtiqueta: () -> Unit,
    alSeleccionarEtiqueta: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) + fadeIn(),
        exit = shrinkVertically(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) + fadeOut(),
        modifier = modifier
    ) {
        FilaPildorasFiltros(
            soloFavoritos = soloFavoritos,
            filtroTipo = filtroTipo,
            alAlternarFavoritos = alAlternarFavoritos,
            alLimpiarTipo = alLimpiarTipo,
            mostrarPildoraIdentidad = mostrarPildoraIdentidad,
            identidades = identidades,
            identidadSeleccionadaId = identidadSeleccionadaId,
            conteoPorIdentidad = conteoPorIdentidad,
            conteoSinIdentidad = conteoSinIdentidad,
            alAbrirSelectorIdentidad = alAbrirSelectorIdentidad,
            alSeleccionarIdentidad = alSeleccionarIdentidad,
            mostrarPildoraCategoria = mostrarPildoraCategoria,
            categorias = categorias,
            categoriaSeleccionadaId = categoriaSeleccionadaId,
            conteoPorCategoria = conteoPorCategoria,
            alAbrirSelectorCategoria = alAbrirSelectorCategoria,
            alSeleccionarCategoria = alSeleccionarCategoria,
            mostrarPildoraEtiqueta = mostrarPildoraEtiqueta,
            filtroEtiqueta = filtroEtiqueta,
            alAbrirSelectorEtiqueta = alAbrirSelectorEtiqueta,
            alSeleccionarEtiqueta = alSeleccionarEtiqueta
        )
    }
}
