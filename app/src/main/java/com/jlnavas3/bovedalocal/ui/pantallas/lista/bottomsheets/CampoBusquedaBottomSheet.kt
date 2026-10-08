package com.jlnavas3.bovedalocal.ui.pantallas.lista.bottomsheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Campo de búsqueda rápido y ergonómico para los selectores en Bottom Sheet.
 */
@Composable
fun CampoBusquedaBottomSheet(
    valor: String,
    alCambiar: (String) -> Unit,
    pista: String,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(12.dp)
    val colorBorde = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
        ColorBordeActual
    } else {
        ColorSeparadorAjustes.copy(alpha = 0.5f)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .border(
                width = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") GrosorBorde else 0.8.dp,
                color = colorBorde,
                shape = forma
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = TextoSecundario,
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (valor.isEmpty()) {
                Text(
                    text = pista,
                    color = TextoSecundario.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
                )
            }
            BasicTextField(
                value = valor,
                onValueChange = alCambiar,
                singleLine = true,
                cursorBrush = SolidColor(ColorAcento),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = TextoPrincipal,
                    fontSize = 13.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (valor.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(TextoSecundario.copy(alpha = 0.2f))
                    .clickable { alCambiar("") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Limpiar texto",
                    tint = TextoPrincipal,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}
