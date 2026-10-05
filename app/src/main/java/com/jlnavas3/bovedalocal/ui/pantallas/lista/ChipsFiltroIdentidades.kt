package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Barra horizontal con chips de filtro rápido por [Identidad] para el listado principal.
 */
@Composable
fun ChipsFiltroIdentidades(
    identidades: List<Identidad>,
    identidadSeleccionadaId: String?,
    conteoPorIdentidad: Map<String, Int>,
    totalEntradas: Int,
    conteoSinIdentidad: Int,
    alSeleccionarIdentidad: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (identidades.isEmpty()) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Chip "Todas"
        val todasSeleccionado = identidadSeleccionadaId == null
        ChipFiltroIdentidad(
            titulo = "Todas",
            conteo = totalEntradas,
            seleccionado = todasSeleccionado,
            colorBase = ColorAcento,
            icono = Icons.Filled.Layers,
            alPulsar = { alSeleccionarIdentidad(null) }
        )

        // Chips por cada Identidad configurada
        identidades.forEach { iden ->
            val seleccionado = identidadSeleccionadaId == iden.id
            val colorBase = parsearColorO(iden.colorHex ?: "", ColorAcento)
            val conteo = conteoPorIdentidad[iden.id] ?: 0

            ChipFiltroIdentidad(
                titulo = iden.nombre,
                conteo = conteo,
                seleccionado = seleccionado,
                colorBase = colorBase,
                icono = Icons.Filled.Person,
                alPulsar = {
                    if (seleccionado) alSeleccionarIdentidad(null) else alSeleccionarIdentidad(iden.id)
                }
            )
        }

        // Chip "Sin identidad" / "Otras" (si hay cuentas sin vincular)
        if (conteoSinIdentidad > 0) {
            val sinIdentidadSeleccionado = identidadSeleccionadaId == "__SIN_IDENTIDAD__"
            ChipFiltroIdentidad(
                titulo = "Otras",
                conteo = conteoSinIdentidad,
                seleccionado = sinIdentidadSeleccionado,
                colorBase = TextoSecundario,
                icono = Icons.Filled.Person,
                alPulsar = {
                    if (sinIdentidadSeleccionado) alSeleccionarIdentidad(null) else alSeleccionarIdentidad("__SIN_IDENTIDAD__")
                }
            )
        }
    }
}
