package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PieMetadatosDetalle(
    creadaEn: Long,
    modificadaEn: Long,
    modifier: Modifier = Modifier
) {
    val mostrarCreada = creadaEn > 0L
    val mostrarEditada = modificadaEn > 0L && modificadaEn != creadaEn

    if (!mostrarCreada && !mostrarEditada) return

    val formatoFecha = remember {
        SimpleDateFormat("dd/MM/yy HH:mm", Locale.forLanguageTag("es-ES"))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = when {
            mostrarCreada && mostrarEditada -> Arrangement.SpaceBetween
            mostrarCreada -> Arrangement.Start
            else -> Arrangement.End
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (mostrarCreada) {
            EtiquetaFechaMetadato(
                titulo = "Creada:",
                fecha = formatoFecha.format(Date(creadaEn)),
                icono = Icons.Filled.CalendarToday,
                colorBase = ColorDatosUsuario
            )
        }
        if (mostrarEditada) {
            EtiquetaFechaMetadato(
                titulo = "Editada:",
                fecha = formatoFecha.format(Date(modificadaEn)),
                icono = Icons.Filled.Edit,
                colorBase = Ambar
            )
        }
    }
}

@Composable
private fun EtiquetaFechaMetadato(
    titulo: String,
    fecha: String,
    icono: ImageVector,
    colorBase: Color,
    modifier: Modifier = Modifier
) {
    val formaBadge = RoundedCornerShape((CurvaturaEsquinas * 0.5f).coerceIn(6.dp, 12.dp))
    Box(
        modifier = modifier
            .clip(formaBadge)
            .background(fondoBadgeParaTema(colorBase))
            .border(1.dp, colorBase.copy(alpha = 0.35f), formaBadge)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorLegibleParaTema(colorBase),
                modifier = Modifier.size(15.dp)
            )
            Column {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = colorLegibleParaTema(colorBase)
                )
                Text(
                    text = fecha,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = colorLegibleParaTema(colorBase).copy(alpha = 0.9f)
                )
            }
        }
    }
}
