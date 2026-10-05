package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
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
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Sección dentro del formulario de edición para vincular una [Identidad] a la credencial.
 */
@Composable
fun SeccionIdentidadEdicion(
    identidadesDisponibles: List<Identidad>,
    identidadSeleccionadaId: String?,
    alSeleccionarIdentidad: (Identidad?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (identidadesDisponibles.isEmpty()) return

    val forma = RoundedCornerShape(14.dp)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Badge,
                contentDescription = null,
                tint = ColorAcento,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Identidad vinculada",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = TextoPrincipal
            )
        }

        Spacer(Modifier.padding(top = 8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Opción Automática / Inteligente
            val esAuto = identidadSeleccionadaId == null
            Box(
                modifier = Modifier
                    .clip(forma)
                    .background(if (esAuto) ColorAcento else ColorTarjetaAjustes)
                    .border(
                        width = 0.8.dp,
                        color = if (esAuto) ColorAcento else ColorSeparadorAjustes,
                        shape = forma
                    )
                    .clickable { alSeleccionarIdentidad(null) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (esAuto) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = ColorSobreAcento,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                    }
                    Text(
                        text = "Automática (por correo)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (esAuto) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp
                        ),
                        color = if (esAuto) ColorSobreAcento else TextoSecundario
                    )
                }
            }

            // Cada identidad disponible
            identidadesDisponibles.forEach { iden ->
                val seleccionado = identidadSeleccionadaId == iden.id
                val colorBase = parsearColorO(iden.colorHex ?: "", ColorAcento)

                Box(
                    modifier = Modifier
                        .clip(forma)
                        .background(if (seleccionado) colorBase else ColorTarjetaAjustes)
                        .border(
                            width = 0.8.dp,
                            color = if (seleccionado) colorBase else colorBase.copy(alpha = 0.45f),
                            shape = forma
                        )
                        .clickable {
                            if (seleccionado) alSeleccionarIdentidad(null) else alSeleccionarIdentidad(iden)
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(if (seleccionado) ColorSobreAcento else colorBase)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = iden.nombre,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            ),
                            color = if (seleccionado) ColorSobreAcento else TextoPrincipal
                        )
                    }
                }
            }
        }
    }
}
