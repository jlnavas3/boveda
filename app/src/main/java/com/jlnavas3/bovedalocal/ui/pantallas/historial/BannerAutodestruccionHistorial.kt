package com.jlnavas3.bovedalocal.ui.pantallas.historial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

@Composable
fun BannerAutodestruccionHistorial(
    autodestruccionActiva: Boolean,
    tiempoAutoDestruccion: Long,
    modifier: Modifier = Modifier
) {
    val colorEstado = if (autodestruccionActiva) Menta else Ambar
    val iconoEstado = if (autodestruccionActiva) Icons.Filled.Timer else Icons.Filled.WarningAmber
    val textoEstado = if (autodestruccionActiva) {
        val tiempoTexto = AlmacenAjustes.OPCIONES_AUTODESTRUCCION_HISTORIAL
            .find { it.first == tiempoAutoDestruccion }?.second ?: "30 minutos"
        "Autodestrucción activa ($tiempoTexto)"
    } else {
        "Autodestrucción desactivada"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(FormaCampo)
            .background(colorEstado.copy(alpha = if (esOscuroActivo) 0.12f else 0.10f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = iconoEstado,
                contentDescription = null,
                tint = colorLegibleParaTema(colorEstado),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = textoEstado,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 11.5.sp),
                color = colorLegibleParaTema(colorEstado)
            )
        }
    }
}
