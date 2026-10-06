package com.jlnavas3.bovedalocal.ui.pantallas.titulos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun FilaPreviaCuentaNormalizada(
    usuario: String,
    tituloActual: String,
    tituloNuevo: String,
    modifier: Modifier = Modifier
) {
    val haCambiado = tituloActual != tituloNuevo

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SuperficieAlta)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            if (usuario.isNotBlank()) {
                Text(
                    text = usuario,
                    color = ColorAcento,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
            if (haCambiado) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = tituloActual,
                        color = TextoSecundario,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = TextDecoration.LineThrough,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "cambia a",
                        tint = ColorAcento,
                        modifier = Modifier.height(10.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tituloNuevo,
                        color = TextoPrincipal,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
            } else {
                Text(
                    text = tituloNuevo,
                    color = TextoPrincipal,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaFilaPreviaCuentaNormalizada() {
    BovedaTheme {
        FilaPreviaCuentaNormalizada(
            usuario = "jolunavi",
            tituloActual = "account.lenovo.com",
            tituloNuevo = "Lenovo (jolunavi)"
        )
    }
}
