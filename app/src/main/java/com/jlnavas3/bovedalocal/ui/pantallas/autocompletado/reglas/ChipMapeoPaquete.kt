package com.jlnavas3.bovedalocal.ui.pantallas.autocompletado.reglas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun ChipMapeoPaquete(
    paquete: String,
    dominio: String,
    alEliminar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SuperficieAlta)
            .border(1.dp, ColorAcento.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = paquete,
            color = TextoSecundario,
            fontSize = 12.sp
        )
        Text(
            text = " → ",
            color = ColorAcento,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = dominio,
            color = TextoPrincipal,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .clickable { alEliminar(paquete) }
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Eliminar mapeo $paquete",
                tint = TextoSecundario,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaChipMapeoPaquete() {
    BovedaTheme {
        ChipMapeoPaquete(
            paquete = "com.bancopichincha.banca",
            dominio = "pichincha.com",
            alEliminar = {}
        )
    }
}
