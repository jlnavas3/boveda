package com.jlnavas3.bovedalocal.ui.pantallas.colores

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IndicadorContenidoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie

@Composable
fun TarjetaStickyPreviaColores(
    entradaPrueba: Entrada,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ColorAjustesFondo)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        val modifierBordeTarjeta = if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
            Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
        } else {
            Modifier
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(FormaTarjeta)
                .background(Superficie)
                .then(modifierBordeTarjeta)
        ) {
            IndicadorContenidoTarjeta(
                entrada = entradaPrueba,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
