package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.pantallas.edicion.FilaSeccionColapsableEdicion
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Sección dentro del formulario de edición para vincular una [Identidad] a la credencial,
 * presentada como fila colapsable homogénea.
 */
@Composable
fun SeccionIdentidadEdicion(
    identidadesDisponibles: List<Identidad>,
    identidadSeleccionadaId: String?,
    alSeleccionarIdentidad: (Identidad?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (identidadesDisponibles.isEmpty()) return

    val identidadSeleccionada = remember(identidadesDisponibles, identidadSeleccionadaId) {
        identidadesDisponibles.firstOrNull { it.id == identidadSeleccionadaId }
    }
    val colorBase = parsearColorO(identidadSeleccionada?.colorHex ?: "", ColorAcento)
    val resumen = identidadSeleccionada?.nombre ?: "Automática (por correo)"

    var expandido by remember { mutableStateOf(false) }

    FilaSeccionColapsableEdicion(
        icono = Icons.Filled.AccountCircle,
        colorIcono = colorBase,
        titulo = "Identidad",
        resumen = resumen,
        expandido = expandido,
        alAlternarExpandido = { expandido = !expandido },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Opción Automática / Inteligente
            val esAuto = identidadSeleccionadaId == null
            ChipIdentidadEdicion(
                titulo = "Automática (por correo)",
                seleccionado = esAuto,
                colorBase = ColorAcento,
                mostrarPunto = false,
                mostrarIconoCheck = true,
                alPulsar = { alSeleccionarIdentidad(null) }
            )

            // Cada identidad disponible
            identidadesDisponibles.forEach { iden ->
                val seleccionado = identidadSeleccionadaId == iden.id
                val colorIden = parsearColorO(iden.colorHex ?: "", ColorAcento)

                ChipIdentidadEdicion(
                    titulo = iden.nombre,
                    seleccionado = seleccionado,
                    colorBase = colorIden,
                    mostrarPunto = true,
                    mostrarIconoCheck = false,
                    alPulsar = {
                        if (seleccionado) alSeleccionarIdentidad(null) else alSeleccionarIdentidad(iden)
                    }
                )
            }
        }
    }
}
