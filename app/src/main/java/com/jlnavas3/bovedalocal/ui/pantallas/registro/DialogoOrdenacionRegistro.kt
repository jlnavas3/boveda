package com.jlnavas3.bovedalocal.ui.pantallas.registro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.FilaOpcionModal
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun DialogoOrdenacionRegistro(
    criterioActual: CriterioOrdenRegistro,
    alSeleccionarCriterio: (CriterioOrdenRegistro) -> Unit,
    alCerrar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    DialogoBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Ordenar por",
        icono = Icons.AutoMirrored.Filled.Sort,
        botonConfirmar = {
            TextButton(onClick = alCerrar) {
                Text("Cerrar", color = ColorAcento)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CriterioOrdenRegistro.entries.forEach { criterio ->
                val seleccionado = criterio == criterioActual
                FilaOpcionModal(
                    titulo = criterio.etiqueta,
                    seleccionado = seleccionado,
                    icono = Icons.AutoMirrored.Filled.Sort,
                    alPulsar = {
                        haptica.tic()
                        alSeleccionarCriterio(criterio)
                        alCerrar()
                    },
                    controlFinal = if (seleccionado) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = ColorAcento,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null
                )
            }
        }
    }
}
