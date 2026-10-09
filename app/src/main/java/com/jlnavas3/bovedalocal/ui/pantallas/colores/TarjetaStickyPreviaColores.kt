package com.jlnavas3.bovedalocal.ui.pantallas.colores

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IndicadorContenidoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.AlturaIndicadores
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.OpacidadIndicadores
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun TarjetaStickyPreviaColores(
    entradaPrueba: Entrada,
    modifier: Modifier = Modifier,
    alturaIndicadores: Dp = AlturaIndicadores,
    opacidadIndicadores: Float = OpacidadIndicadores
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
                .height(64.dp)
                .clip(FormaTarjeta)
                .background(Superficie)
                .then(modifierBordeTarjeta)
        ) {
            // Capa de fondo: Línea o fondo segmentado de indicadores (Z-order detrás)
            IndicadorContenidoTarjeta(
                entrada = entradaPrueba,
                altura = alturaIndicadores,
                opacidad = opacidadIndicadores,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            // Contenido simulado en primer plano para validar legibilidad y contraste
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(FormaCampo)
                        .background(SuperficieAlta),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = ColorAcento,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entradaPrueba.titulo,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = TextoPrincipal,
                        maxLines = 1
                    )
                    Text(
                        text = entradaPrueba.usuario,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp
                        ),
                        color = TextoSecundario,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
