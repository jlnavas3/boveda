package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

@Composable
fun TarjetaEtiquetasDetalle(
    etiquetas: List<String>,
    alFiltrarPorEtiqueta: (String) -> Unit
) {
    if (etiquetas.isEmpty()) return
    val formaChip = FormaPequena

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Etiquetas (${etiquetas.size})")
        TarjetaDatoDetalle(colorBorde = ColorAjusteGris) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                etiquetas.forEach { etiqueta ->
                    Box(
                        modifier = Modifier
                            .clip(formaChip)
                            .background(Borde)
                            .then(
                                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                                    Modifier.border(GrosorBorde, ColorBordeActual, formaChip)
                                else Modifier
                            )
                            .clickable { alFiltrarPorEtiqueta(etiqueta) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "#${normalizarEtiqueta(etiqueta)}",
                            color = TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
