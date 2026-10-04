package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun BotonSeleccionRapida(
    texto: String,
    activo: Boolean,
    alPulsar: () -> Unit
) {
    val forma = FormaPequena
    Box(
        modifier = Modifier
            .clip(forma)
            .background(
                if (activo) DegradadoAcento else Brush.horizontalGradient(listOf(ColorTarjetaAjustes, ColorTarjetaAjustes))
            )
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, if (activo) ColorAcento else ColorBordeActual, forma)
                } else Modifier
            )
            .clickable(onClick = alPulsar)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo(
            texto = texto,
            tamano = com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo.PEQUENO,
            color = if (activo) ColorSobreAcento else TextoSecundario
        )
    }
}
