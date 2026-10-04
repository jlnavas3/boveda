package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Campo básico de búsqueda para listas.
 */
@Composable
fun CampoBusquedaLista(valor: String, alCambiar: (String) -> Unit) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") Modifier.border(GrosorBorde, ColorBordeActual, forma)
                else Modifier
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Search, contentDescription = null, tint = if (valor.isBlank()) TextoSecundario else ColorAcento, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        BasicTextField(
            value = valor,
            onValueChange = alCambiar,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextoPrincipal),
            cursorBrush = SolidColor(ColorAcento),
            modifier = Modifier.weight(1f)
        )
        if (valor.isNotBlank()) {
            IconButton(
                onClick = { alCambiar("") },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Limpiar",
                    tint = TextoSecundario,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
