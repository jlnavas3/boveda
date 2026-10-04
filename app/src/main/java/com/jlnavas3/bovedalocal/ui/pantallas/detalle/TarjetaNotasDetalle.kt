package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

@Composable
fun TarjetaNotasDetalle(
    notas: String,
    ultimaCopia: String?,
    alCopiarNotas: () -> Unit,
    ajustes: AjustesApp? = null
) {
    if (notas.isBlank()) return

    val protegerNotas = ajustes?.seguridadVisualActiva == true && ajustes.ocultarNotas
    var notasReveladas by remember(notas, protegerNotas) { mutableStateOf(!protegerNotas) }

    TemporizadorAutoOcultar(
        revelado = notasReveladas && protegerNotas,
        tiempoSegundos = if (ajustes?.seguridadVisualActiva == true) ajustes.tiempoAutoOcultarSegundos else 0,
        alAutoOcultar = { notasReveladas = false }
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Notas")
        TarjetaDatoDetalle(colorBorde = ColorAcento) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 10.dp, top = 14.dp, bottom = 10.dp)
            ) {
                TextoSeguroVisual(
                    texto = notas,
                    oculto = protegerNotas && !notasReveladas,
                    estilo = ajustes?.estiloOcultamientoVisual ?: "desenfoque",
                    estiloTexto = MaterialTheme.typography.bodyMedium,
                    colorTexto = TextoPrincipal,
                    maxLines = 20
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (protegerNotas) {
                        BotonIconoDetalle(
                            icono = if (notasReveladas) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            descripcion = if (notasReveladas) "Ocultar notas" else "Mostrar notas",
                            alPulsar = { notasReveladas = !notasReveladas }
                        )
                    }
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

