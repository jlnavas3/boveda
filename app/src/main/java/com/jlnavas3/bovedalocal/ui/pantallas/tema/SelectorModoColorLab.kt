package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
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
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente para alternar entre el editor de Modo Oscuro y Modo Claro en el Laboratorio de Temas.
 */
@Composable
fun SelectorModoColorLab(
    modoOscuro: Boolean,
    modificadoOscuro: Boolean,
    modificadoClaro: Boolean,
    colorAcentoActual: Color,
    alSeleccionarModo: (Boolean) -> Unit,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(CurvaturaEsquinas))
            .background(ColorTarjetaAjustes)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Píldora Oscuro
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                .background(if (modoOscuro) colorAcentoActual else Color.Transparent)
                .clickable {
                    haptica.tic()
                    alSeleccionarModo(true)
                }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.DarkMode,
                contentDescription = null,
                tint = if (modoOscuro) colorContraste(colorAcentoActual) else ColorAjusteGris,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Modo Oscuro" + if (modificadoOscuro) " (*)" else "",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (modoOscuro) FontWeight.Bold else FontWeight.Normal,
                    color = if (modoOscuro) colorContraste(colorAcentoActual) else ColorTextoAjustes
                )
            )
        }

        // Píldora Claro
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                .background(if (!modoOscuro) colorAcentoActual else Color.Transparent)
                .clickable {
                    haptica.tic()
                    alSeleccionarModo(false)
                }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.LightMode,
                contentDescription = null,
                tint = if (!modoOscuro) colorContraste(colorAcentoActual) else ColorAjusteGris,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Modo Claro" + if (modificadoClaro) " (*)" else "",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (!modoOscuro) FontWeight.Bold else FontWeight.Normal,
                    color = if (!modoOscuro) colorContraste(colorAcentoActual) else ColorTextoAjustes
                )
            )
        }
    }
}
