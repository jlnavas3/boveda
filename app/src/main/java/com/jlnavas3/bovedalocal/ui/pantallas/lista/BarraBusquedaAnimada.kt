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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import kotlinx.coroutines.delay

/**
 * Barra de búsqueda animada desplegable con enfoque automático, limpieza y botón de cierre.
 */
@Composable
fun BarraBusquedaAnimada(
    valor: String,
    alCambiar: (String) -> Unit,
    alCerrar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") Modifier.border(GrosorBorde, ColorBordeActual, forma)
                else Modifier
            )
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = if (valor.isBlank()) TextoSecundario else ColorAcento,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        BasicTextField(
            value = valor,
            onValueChange = alCambiar,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = TextoPrincipal, fontSize = 15.sp),
            cursorBrush = SolidColor(ColorAcento),
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
        )
        if (valor.isNotBlank()) {
            IconButton(
                onClick = { alCambiar("") },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Limpiar búsqueda",
                    tint = TextoSecundario,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        IconButton(
            onClick = alCerrar,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Cerrar búsqueda",
                tint = ColorIconosInternos,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
