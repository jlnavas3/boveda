package com.jlnavas3.bovedalocal.ui.pantallas.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.Advertencia
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

@Composable
fun TarjetaEventoRegistro(ev: EventoRegistro) {
    val badgeColor = when {
        ev.esError -> Peligro
        ev.area.contains("huella", true) || ev.area.contains("keystore", true) || ev.area.contains("seguridad", true) -> ColorSeguridad
        ev.area.contains("camara", true) || ev.area.contains("cámara", true) -> ColorAcento
        ev.area.contains("autofill", true) || ev.area.contains("passkey", true) || ev.area.contains("credential", true) -> ColorPasskeys
        ev.area.contains("portapapeles", true) -> Advertencia
        ev.area.contains("papelera", true) -> ColorPapelera
        ev.area.contains("salud", true) -> ColorSalud
        ev.area.contains("2fa", true) || ev.area.contains("totp", true) -> Color2FA
        else -> Menta
    }

    val formaTarjeta = FormaTarjeta
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(formaTarjeta)
            .background(ColorTarjetaAjustes)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaTarjeta)
                } else Modifier
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(FormaPequena)
                    .background(fondoBadgeParaTema(badgeColor))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = ev.area.uppercase(),
                    color = colorLegibleParaTema(badgeColor),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Text(
                text = ev.timestamp,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                )
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = ev.mensaje,
            color = if (ev.esError) Peligro else ColorTextoAjustes,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            )
        )
    }
}
