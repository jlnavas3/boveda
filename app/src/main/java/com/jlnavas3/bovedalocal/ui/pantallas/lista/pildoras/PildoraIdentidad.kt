package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Píldora compacta para el filtro de Identidades.
 * Muestra el icono [ 👤 Todas ▾ ] o [ 👤 <nombre> (N) ✕ ] cuando está activa.
 */
@Composable
fun PildoraIdentidad(
    identidades: List<Identidad>,
    identidadSeleccionadaId: String?,
    conteoPorIdentidad: Map<String, Int>,
    conteoSinIdentidad: Int,
    alAbrirSelector: () -> Unit,
    alSeleccionarIdentidad: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val identidadActiva = identidades.firstOrNull { it.id == identidadSeleccionadaId }
    val esSinIdentidad = identidadSeleccionadaId == "__SIN_IDENTIDAD__"
    val activo = identidadActiva != null || esSinIdentidad

    val colorBase = if (identidadActiva != null) {
        parsearColorO(identidadActiva.colorHex ?: "", ColorAcento)
    } else {
        ColorAcento
    }

    val texto = when {
        identidadActiva != null -> {
            val conteo = conteoPorIdentidad[identidadActiva.id]
            if (conteo != null && conteo > 0) "${identidadActiva.nombre} ($conteo)" else identidadActiva.nombre
        }
        esSinIdentidad -> {
            if (conteoSinIdentidad > 0) "Sin asignar ($conteoSinIdentidad)" else "Sin asignar"
        }
        else -> "Todas"
    }

    PildoraFiltroBase(
        icono = Icons.Filled.Person,
        texto = texto,
        activo = activo,
        colorAcento = colorBase,
        alPulsar = alAbrirSelector,
        alLimpiar = if (activo) { { alSeleccionarIdentidad(null) } } else null,
        modifier = modifier
    )
}
