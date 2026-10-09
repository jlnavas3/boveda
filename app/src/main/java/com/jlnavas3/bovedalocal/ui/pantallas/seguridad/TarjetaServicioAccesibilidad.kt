package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.util.ServicioAccesibilidadDetectado

/**
 * Tarjeta microcomponente que representa un servicio de accesibilidad detectado,
 * permitiendo al usuario conocer su riesgo y añadirlo o quitarlo de la lista blanca.
 */
@Composable
fun TarjetaServicioAccesibilidad(
    servicio: ServicioAccesibilidadDetectado,
    alAlternarListaBlanca: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)
    val colorBorde = when {
        servicio.esSospechoso -> Peligro.copy(alpha = 0.5f)
        servicio.esEnListaBlanca -> Menta.copy(alpha = 0.5f)
        else -> ColorAjusteGris.copy(alpha = 0.2f)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .border(1.dp, colorBorde, forma)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = when {
                        servicio.esSospechoso -> Icons.Filled.Warning
                        servicio.esEnListaBlanca -> Icons.Filled.CheckCircle
                        else -> Icons.Filled.Security
                    },
                    contentDescription = null,
                    tint = when {
                        servicio.esSospechoso -> Peligro
                        servicio.esEnListaBlanca -> Menta
                        else -> ColorAjusteGris
                    },
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = servicio.etiquetaApp,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        ),
                        color = ColorTextoAjustes,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = servicio.nombrePaquete,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                        color = ColorAjusteGris,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (servicio.esAppSistema) {
                val textoEstado = if (servicio.estaActivo) "Activo" else "Inactivo"
                val colorEstado = if (servicio.estaActivo) Color(0xFF10B981) else ColorAjusteGris
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = colorEstado.copy(alpha = 0.15f),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = textoEstado,
                        color = colorEstado,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Explicación de estado
        val estadoTexto = when {
            servicio.esSospechoso -> "Esta aplicación tiene permisos para inspeccionar la pantalla y leer campos de texto de otras aplicaciones."
            servicio.esEnListaBlanca -> "Aplicación marcada como de confianza por ti. Sus alertas están silenciadas."
            servicio.esAppSistema && servicio.estaActivo -> "Servicio oficial del sistema activo. Puede interactuar con la pantalla y eventos de accesibilidad."
            servicio.esAppSistema && !servicio.estaActivo -> "Servicio oficial del sistema instalado en el dispositivo (actualmente inactivo)."
            else -> "Servicio oficial del sistema o fabricante verificado."
        }
        Text(
            text = estadoTexto,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
            color = if (servicio.esSospechoso) Peligro.copy(alpha = 0.9f) else ColorAjusteGris
        )

        if (!servicio.esAppSistema) {
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (servicio.esEnListaBlanca) {
                    TextButton(
                        onClick = { alAlternarListaBlanca(servicio.nombrePaquete, false) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.RemoveCircleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Peligro
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Quitar de lista blanca",
                            color = Peligro,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    TextButton(
                        onClick = { alAlternarListaBlanca(servicio.nombrePaquete, true) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Menta
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Confiar (Lista blanca)",
                            color = Menta,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
