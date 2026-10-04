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
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.Wordlist
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.SliderBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun PanelModoDiceware(
    opciones: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    haptica: Haptica
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccion("Número de palabras: ${opciones.palabras}")
        SliderBoveda(
            value = opciones.palabras.toFloat(),
            onValueChange = {
                val nuevo = it.roundToInt().coerceIn(3, 12)
                if (nuevo != opciones.palabras) {
                    haptica.tic()
                    alCambiarOpciones(opciones.copy(palabras = nuevo))
                }
            },
            valueRange = 3f..12f,
            steps = 8
        )
        Spacer(Modifier.height(10.dp))
        EtiquetaSeccion("Separador")
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("-" to "Guion (-)", " " to "Espacio", "." to "Punto (.)", "_" to "Guion bajo (_)", "" to "Sin separador").forEach { (sep, label) ->
                val activo = opciones.separadorFrase == sep
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
                            haptica.tic()
                            alCambiarOpciones(opciones.copy(separadorFrase = sep))
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        label,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (activo) ColorSobreAcento else TextoPrincipal
                    )
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            "Diccionario local de ${Wordlist.TAMANO} palabras en español, integrado dentro de la app sin conexión.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
