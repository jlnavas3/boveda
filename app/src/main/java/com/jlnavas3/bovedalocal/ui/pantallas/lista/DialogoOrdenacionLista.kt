package com.jlnavas3.bovedalocal.ui.pantallas.lista

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.FilaOpcionModal
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

@Composable
fun DialogoOrdenacionLista(
    criterioActual: CriterioOrdenacion,
    alSeleccionarCriterio: (CriterioOrdenacion) -> Unit,
    alCerrar: () -> Unit
) {
    DialogoBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Ordenar por",
        icono = Icons.AutoMirrored.Filled.Sort,
        colorIcono = ColorAcento,
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
            CriterioOrdenacion.entries.forEach { criterio ->
                val seleccionado = criterio == criterioActual
                FilaOpcionModal(
                    titulo = criterio.etiqueta,
                    icono = Icons.AutoMirrored.Filled.Sort,
                    seleccionado = seleccionado,
                    colorAcento = ColorAcento,
                    alPulsar = {
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
