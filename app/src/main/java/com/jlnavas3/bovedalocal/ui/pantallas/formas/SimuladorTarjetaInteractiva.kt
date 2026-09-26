package com.jlnavas3.bovedalocal.ui.pantallas.formas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun SimuladorTarjetaInteractiva(
    ajustes: AjustesApp,
    textoPrueba: String,
    alCambiarTextoPrueba: (String) -> Unit,
    haptica: Haptica
) {
    ContenedorTarjeta(
        paddingInterno = 10.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tarjeta Interactiva",
                color = ColorTitulos,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "${ajustes.curvaturaEsquinasDp.roundToInt()} dp · ${String.format(Locale.US, "%.1f", ajustes.grosorBordeDp)} dp",
                color = ColorAcento,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(Modifier.height(6.dp))
        CampoBoveda(
            valor = textoPrueba,
            etiqueta = "Campo de entrada dinámico",
            alCambiar = alCambiarTextoPrueba
        )
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                BotonAmbar(
                    texto = "Principal",
                    icono = Icons.Filled.Star
                ) { haptica.tic() }
            }
            Box(modifier = Modifier.weight(1f)) {
                BotonBorde(
                    texto = "Secundario",
                    icono = Icons.Filled.Tune
                ) { haptica.tic() }
            }
        }
    }
}
