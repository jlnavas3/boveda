package com.jlnavas3.bovedalocal.ui.pantallas.formatos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun FilaEjemploChip(
    etiqueta: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(FormaPequena)
            .background(SuperficieAlta)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaPequena)
                else Modifier
            )
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = etiqueta,
                color = TextoSecundario,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = valor,
                color = TextoPrincipal,
                style = EstiloMono.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}
