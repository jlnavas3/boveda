package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.BuildConfig
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

/**
 * Pie de página del menú lateral con cápsulas de seguridad (Argon2id, AES-256, 100% Offline) y versión.
 */
@Composable
fun PieMenuLateral(
    perfilArgon2: PerfilArgon2 = PerfilArgon2.ESTANDAR,
    modifier: Modifier = Modifier
) {
    val fondoVerdeInsignia = if (esOscuroActivo) Color(0xFF14291B) else Color(0xFFE8F5E9)
    val textoVerdeInsignia = if (esOscuroActivo) Color(0xFF81C784) else Color(0xFF1B5E20)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(fondoVerdeInsignia)
                    .padding(horizontal = 7.dp, vertical = 2.5.dp)
            ) {
                Text(
                    text = "Argon2id · ${perfilArgon2.memoriaKiB / 1024}M",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 9.sp),
                    color = textoVerdeInsignia,
                    maxLines = 1
                )
            }
            Spacer(Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(fondoVerdeInsignia)
                    .padding(horizontal = 7.dp, vertical = 2.5.dp)
            ) {
                Text(
                    text = "AES-256",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 9.sp),
                    color = textoVerdeInsignia,
                    maxLines = 1
                )
            }
            Spacer(Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(fondoVerdeInsignia)
                    .padding(horizontal = 7.dp, vertical = 2.5.dp)
            ) {
                Text(
                    text = "100% Offline",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 9.sp),
                    color = textoVerdeInsignia,
                    maxLines = 1
                )
            }
        }

        Text(
            text = "Bóveda Local · v${BuildConfig.VERSION_NAME} · by: jlnavas3",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
            color = ColorAjusteGris.copy(alpha = 0.85f),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}
