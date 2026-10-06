package com.jlnavas3.bovedalocal.ui.pantallas.papelera

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Diálogo modal para seleccionar los días de retención de la papelera.
 */
@Composable
fun DialogoAjustesRetencionPapelera(
    diasActuales: Int,
    alDescartar: () -> Unit,
    alSeleccionarDias: (Int) -> Unit
) {
    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = "Retención de papelera",
        icono = Icons.Filled.DeleteSweep,
        colorIcono = ColorPapelera,
        botonConfirmar = {
            TextButton(onClick = alDescartar) {
                Text("Listo", color = ColorAcento)
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Selecciona cuánto tiempo se conservan las entradas eliminadas antes de su borrado definitivo:",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            AlmacenAjustes.OPCIONES_DIAS_RETENCION_PAPELERA.forEach { (dias, etiqueta) ->
                val seleccionado = diasActuales == dias
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { alSeleccionarDias(dias) }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = seleccionado,
                        onClick = { alSeleccionarDias(dias) },
                        colors = RadioButtonDefaults.colors(selectedColor = ColorAcento)
                    )
                    Text(
                        text = etiqueta,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (seleccionado) ColorAcento else TextoPrincipal,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@BovedaPreview
@Composable
private fun DialogoAjustesRetencionPapeleraPreview() {
    BovedaTheme {
        DialogoAjustesRetencionPapelera(
            diasActuales = 30,
            alDescartar = {},
            alSeleccionarDias = {}
        )
    }
}
