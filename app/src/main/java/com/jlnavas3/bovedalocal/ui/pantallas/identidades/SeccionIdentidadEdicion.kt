package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Sección dentro del formulario de edición para vincular una [Identidad] a la credencial.
 */
@Composable
fun SeccionIdentidadEdicion(
    identidadesDisponibles: List<Identidad>,
    identidadSeleccionadaId: String?,
    alSeleccionarIdentidad: (Identidad?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (identidadesDisponibles.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ContenedorIconoInsignia(
                icono = Icons.Filled.AccountCircle,
                tamano = TamanoInsignia.PEQUENO,
                colorFondo = ColorAcento.copy(alpha = 0.15f),
                colorIcono = ColorAcento
            )
            Spacer(Modifier.width(8.dp))
            TextoTitulo(
                texto = "Identidad vinculada",
                estilo = EstiloTitulo.PEQUENO
            )
        }

        Spacer(Modifier.padding(top = 8.dp))

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
                val colorBase = parsearColorO(iden.colorHex ?: "", ColorAcento)

                ChipIdentidadEdicion(
                    titulo = iden.nombre,
                    seleccionado = seleccionado,
                    colorBase = colorBase,
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
