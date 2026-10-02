package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

@Composable
fun BarraBusquedaAjustes(
    texto: String,
    alCambiarTexto: (String) -> Unit,
    placeholder: String = "Buscar en ajustes...",
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") Modifier.border(GrosorBorde, ColorBordeActual, forma)
                else Modifier
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Buscar",
            tint = ColorAjusteGris,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (texto.isEmpty()) {
                Text(
                    text = placeholder,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            BasicTextField(
                value = texto,
                onValueChange = alCambiarTexto,
                singleLine = true,
                cursorBrush = SolidColor(ColorTextoAjustes),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = ColorTextoAjustes
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (texto.isNotEmpty()) {
            IconButton(
                onClick = { alCambiarTexto("") },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Limpiar búsqueda",
                    tint = ColorAjusteGris,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@BovedaPreview
@Composable
private fun PreviewBarraBusquedaAjustes() {
    PreviewTemaBoveda {
        Column(modifier = Modifier.padding(16.dp)) {
            BarraBusquedaAjustes(texto = "", alCambiarTexto = {})
            Spacer(Modifier.height(12.dp))
            BarraBusquedaAjustes(texto = "Biometría", alCambiarTexto = {})
        }
    }
}

