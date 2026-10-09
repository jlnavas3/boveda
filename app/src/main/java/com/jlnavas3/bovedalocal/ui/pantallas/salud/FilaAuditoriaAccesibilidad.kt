package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.util.DiagnosticoAccesibilidad

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon

/**
 * Microcomponente que muestra el diagnóstico de servicios de accesibilidad activos
 * dentro del resumen de auditoría de la pantalla Salud de la Bóveda.
 */
@Composable
fun FilaAuditoriaAccesibilidad(
    diagnostico: DiagnosticoAccesibilidad,
    modifier: Modifier = Modifier,
    alPulsar: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (alPulsar != null) Modifier.clickable { alPulsar() } else Modifier)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Accesibilidad del sistema",
                color = ColorTextoAjustes,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f, fill = false)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (diagnostico.esSeguro) {
                        if (diagnostico.totalActivos == 0) "Seguro" else "Seguro (${diagnostico.totalActivos})"
                    } else {
                        "⚠️ ${diagnostico.serviciosSospechosos.size} en riesgo"
                    },
                    color = if (diagnostico.esSeguro) Menta else Peligro,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp
                    )
                )
                if (alPulsar != null) {
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Gestionar",
                        tint = ColorTextoAjustes.copy(alpha = 0.6f)
                    )
                }
            }
        }

        if (!diagnostico.esSeguro) {
            Spacer(Modifier.height(4.dp))
            val nombres = diagnostico.serviciosSospechosos.joinToString(", ") { it.etiquetaApp }
            Text(
                text = "Alerta: $nombres tiene permisos para inspeccionar la pantalla y el árbol de vistas de otras aplicaciones.",
                color = Peligro.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
            )
        }
    }
}
