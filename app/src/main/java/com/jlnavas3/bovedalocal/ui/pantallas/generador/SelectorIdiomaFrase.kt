package com.jlnavas3.bovedalocal.ui.pantallas.generador

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SelectorIdiomaFrase(
    idiomaActual: String,
    alCambiarIdioma: (String) -> Unit,
    haptica: Haptica?,
    modifier: Modifier = Modifier
) {
    val opciones = listOf(
        "es" to "Español (1,290 palabras)",
        "en" to "Inglés (2,048 palabras)"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        EtiquetaSeccion("Diccionario de palabras")
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            opciones.forEach { (codigo, etiqueta) ->
                val activo = idiomaActual.lowercase() == codigo
                Box(
                    modifier = Modifier
                        .clip(FormaCampo)
                        .background(if (activo) DegradadoAcento else Brush.horizontalGradient(listOf(Superficie, Superficie)))
                        .then(
                            if (GrosorBorde > 0.dp && ColorBordeActual != androidx.compose.ui.graphics.Color.Transparent)
                                Modifier.border(GrosorBorde, if (activo) ColorAcento else ColorBordeActual, FormaCampo)
                            else Modifier
                        )
                        .clickable {
                            haptica?.tic()
                            alCambiarIdioma(codigo)
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = etiqueta,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (activo) ColorSobreAcento else TextoPrincipal
                    )
                }
            }
        }
    }
}

@BovedaPreview
@Composable
private fun SelectorIdiomaFrasePreview() {
    SelectorIdiomaFrase(
        idiomaActual = "es",
        alCambiarIdioma = {},
        haptica = null
    )
}
