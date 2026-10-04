package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun ChipEtiqueta(texto: String, sugerida: Boolean = false, alPulsar: () -> Unit) {
    val forma = FormaPequena
    val fondoChip = ColorCampoAjustes
    val fondoSugerida = ColorTarjetaAjustes
    val bordeChip = ColorBordeActual

    Row(
        modifier = Modifier
            .clip(forma)
            .background(if (sugerida) fondoSugerida else fondoChip)
            .border(1.dp, bordeChip, forma)
            .clickable { alPulsar() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("#${normalizarEtiqueta(texto)}", color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
        if (!sugerida) {
            Spacer(Modifier.width(6.dp))
            Icon(Icons.Filled.Close, contentDescription = "Quitar etiqueta", tint = TextoSecundario, modifier = Modifier.size(16.dp))
        }
    }
}
