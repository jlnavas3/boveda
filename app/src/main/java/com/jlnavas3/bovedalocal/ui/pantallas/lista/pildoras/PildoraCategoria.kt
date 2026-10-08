package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.IconosCategorias
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Píldora compacta para el filtro de Categorías.
 * Muestra el icono [ 📁 Todas ▾ ] o [ 📁 <nombre> (N) ✕ ] cuando está activa.
 */
@Composable
fun PildoraCategoria(
    categorias: List<Categoria>,
    categoriaSeleccionadaId: String?,
    conteoPorCategoria: Map<String, Int>,
    alAbrirSelector: () -> Unit,
    alSeleccionarCategoria: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoriaActiva = categorias.firstOrNull { it.id == categoriaSeleccionadaId }
    val activo = categoriaActiva != null

    val colorBase = if (categoriaActiva != null) {
        IconosCategorias.parsearColorHex(categoriaActiva.colorHex) ?: ColorAcento
    } else {
        ColorAcento
    }

    val icono = if (categoriaActiva != null) {
        IconosCategorias.obtenerIcono(categoriaActiva.icono)
    } else {
        Icons.Filled.Folder
    }

    val texto = if (categoriaActiva != null) {
        val conteo = conteoPorCategoria[categoriaActiva.id]
        if (conteo != null && conteo > 0) "${categoriaActiva.nombre} ($conteo)" else categoriaActiva.nombre
    } else {
        "Todas"
    }

    PildoraFiltroBase(
        icono = icono,
        texto = texto,
        activo = activo,
        colorAcento = colorBase,
        alPulsar = alAbrirSelector,
        alLimpiar = if (activo) { { alSeleccionarCategoria(null) } } else null,
        modifier = modifier
    )
}
