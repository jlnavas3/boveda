package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Píldora compacta para el filtro de Etiquetas.
 * Muestra el icono [ 🏷️ Todas ▾ ] o [ 🏷️ <etiqueta> ✕ ] cuando está activa.
 */
@Composable
fun PildoraEtiqueta(
    filtroEtiqueta: String?,
    alAbrirSelector: () -> Unit,
    alSeleccionarEtiqueta: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val activo = !filtroEtiqueta.isNullOrBlank()
    val texto = if (activo) normalizarEtiqueta(filtroEtiqueta) else "Todas"

    PildoraFiltroBase(
        icono = Icons.AutoMirrored.Filled.Label,
        texto = texto,
        activo = activo,
        colorAcento = ColorAcento,
        alPulsar = alAbrirSelector,
        alLimpiar = if (activo) { { alSeleccionarEtiqueta(null) } } else null,
        modifier = modifier
    )
}
