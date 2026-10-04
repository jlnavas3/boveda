package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.Menta
import java.util.concurrent.TimeUnit

const val DIAS_AVISO_ANTIGUEDAD = 180L

enum class PestanaSalud(val titulo: String) {
    REPETIDAS("Repetidas"),
    COMUNES("Filtradas"),
    DEBILES("Débiles"),
    ANTIGUAS("Antiguas"),
    IGNORADAS("Ignoradas")
}

fun diasDesde(momento: Long, ahora: Long): Long =
    TimeUnit.MILLISECONDS.toDays((ahora - momento).coerceAtLeast(0))

@Composable
fun FilaMetricaSalud(etiqueta: String, valor: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(etiqueta, color = ColorTextoAjustes, style = MaterialTheme.typography.bodyMedium)
        Text(valor, color = color, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
fun MensajeExitoPestana(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    colorIcono: Color = Menta
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(colorIcono.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = ColorTextoAjustes,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = subtitulo,
            style = MaterialTheme.typography.bodyMedium,
            color = ColorAjusteGris,
            textAlign = TextAlign.Center
        )
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun ComponentesSaludPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        TarjetaGrupoRepetido(
            grupo = listOf(
                com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo,
                com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo.copy(id = "mock-salud-2", titulo = "Twitter / X")
            ),
            expandido = true,
            alCambiarClave = {},
            alVerDetalle = {}
        )
    }
}
