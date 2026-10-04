package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Barra horizontal deslizable de chips de categorías con el modelo visual y dimensiones
 * de las colecciones (RoundedCornerShape(12.dp), icono representativo de 15.dp, colores propios y bordes configurables).
 */
@Composable
fun ChipsCategoriasExportacion(
    categorias: List<CategoriaExportacion>,
    categoriaActivaId: String,
    alSeleccionarCategoria: (String) -> Unit,
    totalPorCategoria: (CategoriaExportacion) -> Int,
    onTic: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (categorias.size <= 1) return

    val formaChip = RoundedCornerShape(12.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categorias.forEach { cat ->
            val seleccionada = cat.id == categoriaActivaId
            val cantCat = totalPorCategoria(cat)
            val colorPropio = cat.color
            val icono = cat.icono

            Box(
                modifier = Modifier
                    .clip(formaChip)
                    .background(if (seleccionada) colorPropio else ColorTarjetaAjustes)
                    .then(
                        if (seleccionada) {
                            Modifier.border(0.8.dp, colorPropio, formaChip)
                        } else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                            Modifier.border(GrosorBorde, ColorBordeActual, formaChip)
                        } else {
                            Modifier.border(0.8.dp, ColorSeparadorAjustes, formaChip)
                        }
                    )
                    .clickable {
                        onTic()
                        alSeleccionarCategoria(cat.id)
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = if (seleccionada) {
                            if (colorPropio == ColorAcento) ColorSobreAcento else Color.White
                        } else colorPropio,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "${cat.etiqueta} ($cantCat)",
                        color = if (seleccionada) {
                            if (colorPropio == ColorAcento) ColorSobreAcento else Color.White
                        } else TextoPrincipal,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}
