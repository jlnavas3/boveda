package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys

/**
 * Microcomponente para mostrar la fila de una Passkey asociada dentro de una credencial duplicada.
 */
@Composable
fun FilaPasskeyDuplicada(
    passkey: DatosPasskey?,
    modifier: Modifier = Modifier
) {
    if (passkey == null) return

    val primerGrupoPasskey = remember(passkey.credId) {
        obtenerPrimerGrupoPasskey(passkey.credId)
    }

    Spacer(Modifier.height(2.dp))
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Fingerprint,
            contentDescription = null,
            tint = ColorPasskeys,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "Passkey: $primerGrupoPasskey...",
            color = ColorTextoAjustes.copy(alpha = 0.85f),
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun obtenerPrimerGrupoPasskey(credId: String): String {
    val limpio = credId.trim()
    return when {
        limpio.isBlank() -> "—"
        limpio.contains("-") -> limpio.substringBefore("-")
        limpio.contains(":") -> limpio.substringBefore(":")
        limpio.length > 10 -> limpio.take(10)
        else -> limpio
    }
}
