package com.jlnavas3.bovedalocal.ui.pantallas.migracion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

@Composable
fun BarraControlesSeleccionMigracion(
    cuantasSeleccionadas: Int,
    totalCuentas: Int,
    alSeleccionarSoloNuevas: () -> Unit,
    alSeleccionarTodas: () -> Unit,
    alSeleccionarNinguna: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fondoBotonNeutro = if (esOscuroActivo) Color(0xFF28272C) else Color(0xFFEAEAEE)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$cuantasSeleccionadas de $totalCuentas seleccionadas",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            ),
            color = ColorAcento,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonIconoAccionMigracion(
                icono = Icons.Filled.AutoAwesome,
                descripcion = "Solo nuevas",
                colorIcono = ColorAcento,
                fondo = ColorAcento.copy(alpha = 0.16f),
                alPulsar = alSeleccionarSoloNuevas
            )

            BotonIconoAccionMigracion(
                icono = Icons.Filled.SelectAll,
                descripcion = "Seleccionar todas",
                colorIcono = TextoPrincipal,
                fondo = fondoBotonNeutro,
                alPulsar = alSeleccionarTodas
            )

            BotonIconoAccionMigracion(
                icono = Icons.Filled.Deselect,
                descripcion = "Deseleccionar todas",
                colorIcono = TextoSecundario,
                fondo = fondoBotonNeutro,
                alPulsar = alSeleccionarNinguna
            )
        }
    }
}

@Composable
private fun BotonIconoAccionMigracion(
    icono: ImageVector,
    descripcion: String,
    colorIcono: Color,
    fondo: Color,
    alPulsar: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(FormaPequena)
            .background(fondo)
            .clickable(onClick = alPulsar),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = colorIcono,
            modifier = Modifier.size(19.dp)
        )
    }
}

