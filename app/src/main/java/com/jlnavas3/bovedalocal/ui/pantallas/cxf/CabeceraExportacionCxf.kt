package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Cabecera para la pantalla de exportación interactiva CXF.
 * Muestra el nombre de la app receptora y la barra de controles con etiqueta a la izquierda
 * y botones circulares translúcidos de seleccionar/deseleccionar todo a la derecha.
 * Respeta la geometría y bordes configurados en 02-APA-GEO y 02-APA-TYP.
 */
@Composable
fun CabeceraExportacionCxf(
    gestorReceptor: String,
    totalElementos: Int,
    seleccionados: Int,
    alSeleccionarTodas: () -> Unit,
    alDeseleccionarTodas: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Solicitud de transferencia directa",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = (16 * EscalaTexto).sp
            ),
            color = ColorTitulos
        )

        Text(
            text = "La aplicación $gestorReceptor solicita credenciales de tu Bóveda.",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = (12 * EscalaTexto).sp
            ),
            color = TextoSecundario
        )

        val forma = FormaTarjeta
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(ColorTarjetas)
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && ColorBordeActual != Color.Transparent) {
                        Modifier.border(GrosorBorde, ColorBordeActual, forma)
                    } else {
                        Modifier
                    }
                )
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$seleccionados de $totalElementos seleccionadas",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = (13 * EscalaTexto).sp
                ),
                color = ColorTitulos,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón circular translúcido: Seleccionar todo
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(ColorTitulos.copy(alpha = 0.09f))
                        .clickable(onClick = alSeleccionarTodas),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.SelectAll,
                        contentDescription = "Seleccionar todo",
                        tint = ColorTitulos,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Botón circular translúcido: Deseleccionar todo
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(ColorTitulos.copy(alpha = 0.09f))
                        .clickable(onClick = alDeseleccionarTodas),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Deselect,
                        contentDescription = "Deseleccionar todo",
                        tint = TextoSecundario,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
