package com.jlnavas3.bovedalocal.ui.pantallas.registro

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.componentes.FilaOpcionModal
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun DialogoCapacidadRegistro(
    capacidadActual: Int,
    alSeleccionarCapacidad: (Int) -> Unit,
    alCerrar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    ModalInferiorBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Capacidad del registro",
        descripcion = "Eventos conservados en la memoria rápida",
        icono = Icons.Filled.Storage,
        colorIcono = ColorAcento,
        fondoIcono = ColorAcento.copy(alpha = 0.15f),
        mostrarBotonCerrar = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 420.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AjustesDefaults.DiagnosticoConfig.OPCIONES_MAX_EVENTOS.forEach { opcion ->
                val esSeleccionado = opcion == capacidadActual
                val descripcion = when (opcion) {
                    100 -> "Mínimo consumo de memoria"
                    200 -> "Predeterminado balanceado"
                    500 -> "Recomendado para auditoría intensiva"
                    1000 -> "Historial extendido de eventos"
                    else -> "$opcion eventos recientes"
                }

                FilaOpcionModal(
                    titulo = "$opcion eventos",
                    descripcion = descripcion,
                    icono = Icons.Filled.Storage,
                    seleccionado = esSeleccionado,
                    colorAcento = ColorAcento,
                    alPulsar = {
                        haptica.tic()
                        alSeleccionarCapacidad(opcion)
                        alCerrar()
                    },
                    controlFinal = {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .border(
                                    width = if (esSeleccionado) 6.dp else 1.5.dp,
                                    color = if (esSeleccionado) ColorAcento else ColorAjusteGris.copy(alpha = 0.45f),
                                    shape = CircleShape
                                )
                        )
                    }
                )
            }
        }
    }
}

@BovedaPreview
@Composable
private fun DialogoCapacidadRegistroPreview() {
    DialogoCapacidadRegistro(
        capacidadActual = 200,
        alSeleccionarCapacidad = {},
        alCerrar = {}
    )
}
