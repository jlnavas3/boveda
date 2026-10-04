package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta

/**
 * Fila horizontal desplazable de filtros activos (favoritos, tipo, etiqueta) y chips de etiquetas disponibles.
 */
@Composable
fun FilaFiltrosYEtiquetasLista(
    soloFavoritos: Boolean,
    filtroTipo: TipoEntrada?,
    filtroEtiqueta: String?,
    etiquetasDisponibles: List<String>,
    alAlternarFavoritos: () -> Unit,
    alLimpiarTipo: () -> Unit,
    alLimpiarEtiqueta: () -> Unit,
    alSeleccionarEtiqueta: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val hayFiltroActivo = filtroTipo != null || soloFavoritos || filtroEtiqueta != null
    if (!hayFiltroActivo && etiquetasDisponibles.isEmpty()) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (soloFavoritos) {
            ChipFiltroActivo(
                texto = "Favoritos",
                icono = Icons.Filled.Star,
                alLimpiar = alAlternarFavoritos
            )
        }
        if (filtroTipo != null) {
            ChipFiltroActivo(
                texto = filtroTipo.etiqueta,
                icono = Icons.Filled.FilterList,
                alLimpiar = alLimpiarTipo
            )
        }
        if (filtroEtiqueta != null) {
            ChipFiltroActivo(
                texto = normalizarEtiqueta(filtroEtiqueta),
                icono = Icons.Filled.Sell,
                alLimpiar = alLimpiarEtiqueta
            )
        }
        etiquetasDisponibles.filter { it != filtroEtiqueta }.forEach { etiqueta ->
            ChipFiltro(
                texto = normalizarEtiqueta(etiqueta),
                activo = false,
                icono = Icons.Filled.Sell
            ) {
                alSeleccionarEtiqueta(etiqueta)
            }
        }
    }
}
