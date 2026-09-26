package com.jlnavas3.bovedalocal.ui.pantallas.papelera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import java.util.concurrent.TimeUnit

fun diasDesde(momento: Long, ahora: Long): Long =
    TimeUnit.MILLISECONDS.toDays((ahora - momento).coerceAtLeast(0))

@Composable
fun FilaPapeleraNativa(
    entrada: Entrada,
    diasRestantes: Long,
    alRestaurar: () -> Unit,
    alBorrarDefinitivo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val icono = when (entrada.tipo) {
        TipoEntrada.LOGIN -> Icons.Filled.Lock
        TipoEntrada.PASSKEY -> Icons.Filled.Fingerprint
        TipoEntrada.NOTA -> Icons.Filled.Description
        TipoEntrada.TARJETA -> Icons.Filled.CreditCard
        TipoEntrada.WIFI -> Icons.Filled.Wifi
        TipoEntrada.CUENTA_BANCARIA -> Icons.Filled.AccountBalance
        TipoEntrada.IDENTIDAD -> Icons.Filled.Badge
        TipoEntrada.SERVIDOR -> Icons.Filled.Dns
        TipoEntrada.WALLET -> Icons.Filled.AccountBalanceWallet
    }

    val colorIcono = when (entrada.tipo) {
        TipoEntrada.LOGIN -> ColorSeguridad
        TipoEntrada.PASSKEY -> ColorPasskeys
        TipoEntrada.NOTA -> ColorAcento
        TipoEntrada.TARJETA -> ColorGenerador
        TipoEntrada.WIFI -> ColorSalud
        TipoEntrada.CUENTA_BANCARIA -> ColorSeguridad
        TipoEntrada.IDENTIDAD -> ColorExportacion
        TipoEntrada.SERVIDOR -> ColorIconosInternos
        TipoEntrada.WALLET -> ColorAcento
    }

    val subtitulo = entrada.usuario.ifBlank {
        entrada.urls.firstOrNull() ?: entrada.tipo.etiqueta
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(fondoBadgeParaTema(colorIcono)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorLegibleParaTema(colorIcono),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = entrada.titulo.ifBlank { "Sin título" },
                color = ColorTextoAjustes,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitulo,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = if (diasRestantes <= 3) Peligro else ColorAjusteGris,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = if (diasRestantes > 0) "Expira en $diasRestantes días" else "Expira en cualquier momento",
                    color = if (diasRestantes <= 3) Peligro else ColorAjusteGris,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp)
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        // Botón Restaurar
        IconButton(
            onClick = alRestaurar,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(fondoBadgeParaTema(Menta))
        ) {
            Icon(
                imageVector = Icons.Filled.Restore,
                contentDescription = "Restaurar",
                tint = colorLegibleParaTema(Menta),
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(6.dp))

        // Botón Borrar Definitivo
        IconButton(
            onClick = alBorrarDefinitivo,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(fondoBadgeParaTema(Peligro))
        ) {
            Icon(
                imageVector = Icons.Filled.DeleteForever,
                contentDescription = "Borrar",
                tint = colorLegibleParaTema(Peligro),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
