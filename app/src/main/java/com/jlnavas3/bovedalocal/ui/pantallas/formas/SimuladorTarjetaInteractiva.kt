package com.jlnavas3.bovedalocal.ui.pantallas.formas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun SimuladorTarjetaInteractiva(
    ajustes: AjustesApp,
    textoPrueba: String = "Vista previa",
    alCambiarTextoPrueba: ((String) -> Unit)? = null,
    haptica: Haptica? = null
) {
    ContenedorTarjeta(
        paddingInterno = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                CampoBoveda(
                    valor = textoPrueba,
                    etiqueta = "Campo interactivo",
                    alCambiar = { alCambiarTextoPrueba?.invoke(it) }
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ColorAcento.copy(alpha = 0.16f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${ajustes.curvaturaEsquinasDp.roundToInt()} dp · ${String.format(Locale.US, "%.1f", ajustes.grosorBordeDp)} dp · ${ajustes.espaciadoComponentesDp.roundToInt()} dp",
                    color = ColorAcento,
                    style = EstiloMono.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )
            }
        }
        Spacer(Modifier.height((EspaciadoComponentes * 0.45f).coerceIn(4.dp, 16.dp)))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                BotonAmbar(
                    texto = "Acento",
                    icono = Icons.Filled.Star
                ) { haptica?.tic() }
            }
            Box(modifier = Modifier.weight(1f)) {
                BotonBorde(
                    texto = "Borde",
                    icono = Icons.Filled.Tune
                ) { haptica?.tic() }
            }
        }
    }
}
