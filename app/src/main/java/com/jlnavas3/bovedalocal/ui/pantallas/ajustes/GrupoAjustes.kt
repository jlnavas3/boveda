package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/**
 * Contenedor redondeado para agrupar filas de ajustes bajo una etiqueta común al estilo Samsung One UI.
 * Responde dinámicamente a la configuración de "Formas y bordes".
 */
@Composable
fun GrupoAjustes(
    etiqueta: String? = null,
    modifier: Modifier = Modifier,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val formaTarjeta = RoundedCornerShape(CurvaturaEsquinas)
    Column(modifier = modifier.fillMaxWidth()) {
        if (!etiqueta.isNullOrBlank()) {
            Text(
                text = etiqueta.uppercase(),
                color = ColorAjusteGris,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 4.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                        Modifier.border(
                            width = GrosorBorde,
                            color = ColorBordeActual,
                            shape = formaTarjeta
                        )
                    } else Modifier
                )
                .clip(formaTarjeta)
                .background(ColorTarjetaAjustes)
        ) {
            contenido()
        }
    }
}
