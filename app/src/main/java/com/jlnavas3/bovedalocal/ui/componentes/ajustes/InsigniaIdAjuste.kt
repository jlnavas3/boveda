package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIdApariencia
import com.jlnavas3.bovedalocal.ui.theme.ColorIdCopias
import com.jlnavas3.bovedalocal.ui.theme.ColorIdHerramientas
import com.jlnavas3.bovedalocal.ui.theme.ColorIdLista
import com.jlnavas3.bovedalocal.ui.theme.ColorIdSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorIdSistema
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Insignia centralizada para renderizar los identificadores jerárquicos de Ajustes
 * (ej. 01.1.1 o 05-ATM-PAS-KEY). Permite copiar el ID al pulsar exclusivamente sobre el micro-ícono.
 */
@Composable
fun InsigniaIdAjuste(
    id: String,
    modifier: Modifier = Modifier,
    ajustes: AjustesApp? = null,
    colorForzado: Color? = null
) {
    if (id.isBlank()) return

    val contexto = LocalContext.current
    val prefijo = id.trimStart().take(2)
    val colorBase = colorForzado ?: when {
        ajustes != null -> when (prefijo) {
            "01" -> parsearColorO(ajustes.colorIdSeguridad, ColorIdSeguridad)
            "02" -> parsearColorO(ajustes.colorIdApariencia, ColorIdApariencia)
            "03" -> parsearColorO(ajustes.colorIdLista, ColorIdLista)
            "04" -> parsearColorO(ajustes.colorIdHerramientas, ColorIdHerramientas)
            "05" -> parsearColorO(ajustes.colorIdCopias, ColorIdCopias)
            "06" -> parsearColorO(ajustes.colorIdSistema, ColorIdSistema)
            else -> ColorAcento
        }
        else -> when (prefijo) {
            "01" -> ColorIdSeguridad
            "02" -> ColorIdApariencia
            "03" -> ColorIdLista
            "04" -> ColorIdHerramientas
            "05" -> ColorIdCopias
            "06" -> ColorIdSistema
            else -> ColorAcento
        }
    }
    val colorTexto = colorLegibleParaTema(colorBase)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(fondoBadgeParaTema(colorBase))
            .padding(start = 2.dp, end = 5.dp, top = 1.5.dp, bottom = 1.5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(3.dp))
                .clickable {
                    val clipboard = contexto.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("ID Ajuste", id))
                    Toast.makeText(contexto, "ID copiado: $id", Toast.LENGTH_SHORT).show()
                }
                .padding(2.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.ContentCopy,
                contentDescription = "Copiar ID $id",
                tint = colorTexto.copy(alpha = 0.8f),
                modifier = Modifier.size(10.5.dp)
            )
        }

        Spacer(modifier = Modifier.width(2.dp))

        Text(
            text = id,
            color = colorTexto,
            style = EstiloMono.copy(
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            softWrap = false
        )
    }
}
