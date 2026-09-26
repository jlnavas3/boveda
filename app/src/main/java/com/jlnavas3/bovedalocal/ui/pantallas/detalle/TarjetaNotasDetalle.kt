package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

@Composable
fun TarjetaNotasDetalle(
    notas: String,
    ultimaCopia: String?,
    alCopiarNotas: () -> Unit
) {
    if (notas.isBlank()) return

    GrupoAjustes(etiqueta = "Notas") {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(fondoBadgeParaTema(ColorAcento))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = notas,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoPrincipal
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    BotonCopiar(copiado = ultimaCopia == "notas", alPulsar = alCopiarNotas)
                }
            }
            Box(modifier = Modifier.matchParentSize()) {
                Box(
                    modifier = Modifier
                        .width(4.5.dp)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart)
                        .background(ColorAcento)
                )
            }
        }
    }
}
