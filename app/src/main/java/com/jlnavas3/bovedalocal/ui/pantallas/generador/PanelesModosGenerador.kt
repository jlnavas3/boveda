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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.Wordlist
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSeparadorDropdown
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAmbar
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun coloresSlider() = SliderDefaults.colors(
    thumbColor = Ambar,
    activeTrackColor = Ambar,
    inactiveTrackColor = Borde
)

@Composable
fun ColumnaInterruptor(
    simbolo: String,
    etiqueta: String,
    activo: Boolean,
    alCambiar: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { alCambiar(!activo) }
            .padding(vertical = 4.dp, horizontal = 2.dp)
    ) {
        Text(
            text = simbolo,
            color = if (activo) ColorTitulos else TextoSecundario,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = etiqueta,
            color = if (activo) TextoPrincipal else TextoSecundario,
            style = MaterialTheme.typography.labelSmall
        )
        Spacer(Modifier.height(4.dp))
        SwitchBoveda(
            checked = activo,
            onCheckedChange = alCambiar
        )
    }
}

@Composable
fun PanelModoAleatorio(
    opciones: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    haptica: Haptica
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccion("Longitud: ${opciones.longitud} caracteres")
        Slider(
            value = opciones.longitud.toFloat(),
            onValueChange = {
                val nuevo = it.roundToInt().coerceIn(8, 64)
                if (nuevo != opciones.longitud) {
                    haptica.tic()
                    alCambiarOpciones(opciones.copy(longitud = nuevo))
                }
            },
            valueRange = 8f..64f,
            colors = coloresSlider()
        )

        if (!opciones.mayusculas && !opciones.minusculas && !opciones.digitos && !opciones.simbolos) {
            Spacer(Modifier.height(8.dp))
            Text(
                "Activa al menos un tipo de carácter en el menú desplegable superior.",
                color = Peligro,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun PanelModoDiceware(
    opciones: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    haptica: Haptica
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccion("Número de palabras: ${opciones.palabras}")
        Slider(
            value = opciones.palabras.toFloat(),
            onValueChange = {
                val nuevo = it.roundToInt().coerceIn(3, 12)
                if (nuevo != opciones.palabras) {
                    haptica.tic()
                    alCambiarOpciones(opciones.copy(palabras = nuevo))
                }
            },
            valueRange = 3f..12f,
            steps = 8,
            colors = coloresSlider()
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
                        .background(if (activo) DegradadoAmbar else Brush.horizontalGradient(listOf(Superficie, Superficie)))
                        .then(
                            if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                                Modifier.border(GrosorBorde, if (activo) Ambar else ColorBordeActual, FormaCampo)
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

@Composable
fun PanelModoPatron(
    opciones: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    haptica: Haptica
) {
    SelectorPlantillaPatron(
        patronActual = opciones.patron,
        alSeleccionarPlantilla = { nuevaPlantilla ->
            haptica.tic()
            alCambiarOpciones(opciones.copy(patron = nuevaPlantilla))
        }
    )

    Spacer(Modifier.height(12.dp))

    CampoBoveda(
        valor = opciones.patron,
        etiqueta = "Máscara / Patrón personalizado",
        alCambiar = { alCambiarOpciones(opciones.copy(patron = it)) },
        monoespaciada = true
    )

    Spacer(Modifier.height(8.dp))
    Text(
        "X: alfanumérica (A-Z, 0-9) | A: mayúscula (A-Z) | a: minúscula (a-z) | 9 o d: dígito (0-9) | w: palabra Diceware",
        color = TextoSecundario,
        style = MaterialTheme.typography.bodySmall
    )
}
