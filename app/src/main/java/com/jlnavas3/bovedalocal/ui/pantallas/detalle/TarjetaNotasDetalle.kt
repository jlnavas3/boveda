package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

@Composable
fun TarjetaNotasDetalle(
    notas: String,
    ultimaCopia: String?,
    alCopiarNotas: () -> Unit
) {
    if (notas.isBlank()) return

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Notas")
        TarjetaDatoDetalle(colorBorde = ColorAcento) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 10.dp, top = 14.dp, bottom = 10.dp)
            ) {
                Text(
                    text = notas,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoPrincipal
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    BotonCopiarDetalle(copiado = ultimaCopia == "notas", alPulsar = alCopiarNotas)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaNotasDetallePreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        TarjetaNotasDetalle(
            notas = "Recuerda renovar la suscripción anual en noviembre.\nNúmero de contrato: CT-884920.",
            ultimaCopia = null,
            alCopiarNotas = {}
        )
    }
}

