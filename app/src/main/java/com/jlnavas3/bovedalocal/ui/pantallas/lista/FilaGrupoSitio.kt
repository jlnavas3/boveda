package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun FilaGrupoSitio(
    clave: String,
    cantidad: Int,
    expandido: Boolean,
    alturaFila: Dp = 74.dp,
    tamanoMonograma: Int = 46,
    resaltado: Boolean = false,
    alAlternar: () -> Unit
) {
    val compacta = alturaFila.value <= 48f
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val tamanoIcono = if (compacta) 32 else if (alturaFila.value <= 64f) 36 else 40

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(alturaFila)
            .clip(forma)
            .background(if (resaltado) Ambar.copy(alpha = 0.16f) else ColorTarjetaAjustes)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") Modifier.border(GrosorBorde, ColorBordeActual, forma)
                else Modifier
            )
            .clickable { alAlternar() }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(tamanoIcono.dp)
                .clip(CircleShape)
                .background(ColorCampoAjustes),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Dns,
                contentDescription = null,
                tint = ColorAcento,
                modifier = Modifier.size((tamanoIcono * 0.52f).dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                clave,
                style = if (compacta) MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold) else MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = if (resaltado) Ambar else TextoPrincipal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "$cantidad ${if (cantidad == 1) "cuenta" else "cuentas"}",
                style = if (compacta) MaterialTheme.typography.labelSmall else MaterialTheme.typography.bodySmall,
                color = TextoSecundario,
                maxLines = 1
            )
        }
        Icon(
            if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = if (expandido) "Contraer" else "Expandir",
            tint = if (resaltado) Ambar else ColorIconosInternos
        )
    }
}
