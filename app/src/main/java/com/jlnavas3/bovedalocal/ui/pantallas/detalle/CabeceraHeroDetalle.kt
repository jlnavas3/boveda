package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena

@Composable
fun CabeceraHeroDetalle(
    entrada: Entrada,
    alMostrarQr: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Monograma(
            titulo = entrada.titulo.ifBlank { "?" },
            semilla = entrada.urls.firstOrNull() ?: entrada.passkey?.rpId ?: entrada.titulo,
            tamano = 60
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = entrada.titulo.ifBlank { "Sin título" },
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = ColorTitulos,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .clip(FormaPequena)
                .background(ColorAcento.copy(alpha = 0.12f))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text(
                text = entrada.tipo.etiqueta,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = ColorAcento
            )
        }
    }

    if (entrada.tipo == TipoEntrada.WIFI) {
        Spacer(Modifier.height(8.dp))
        BotonColorido(
            texto = "Compartir Wi-Fi por código QR",
            color = ColorAcento,
            icono = Icons.Filled.QrCode,
            modifier = Modifier.fillMaxWidth(),
            alPulsar = alMostrarQr
        )
    }
}
