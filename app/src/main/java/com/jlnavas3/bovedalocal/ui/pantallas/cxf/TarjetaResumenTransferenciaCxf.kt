package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.cxf.ResultadoConversionCxf
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.DegradadoAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

/**
 * Microcomponente de resumen para la transferencia entrante CXF:
 * cabecera de proveedor, conteo de entradas encontradas, insignias por tipo y selectores rápidos.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TarjetaResumenTransferenciaCxf(
    exportadorNombre: String,
    totalItems: Int,
    resultadoCxf: ResultadoConversionCxf,
    soloNuevasSeleccionadas: Boolean,
    todasSeleccionadas: Boolean,
    ningunaSeleccionada: Boolean,
    alSeleccionarSoloNuevas: () -> Unit,
    alSeleccionarTodas: () -> Unit,
    alSeleccionarNinguna: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Transferencia directa desde $exportadorNombre",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = ColorTitulos
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Se encontraron $totalItems credenciales listas para incorporar a tu bóveda:",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario
        )

        Spacer(Modifier.height(10.dp))

        // Resumen de insignias con estadísticas adaptadas al tema
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (resultadoCxf.totalPasskeys > 0) {
                InsigniaResumenCxf(
                    icono = Icons.Filled.VpnKey,
                    texto = "${resultadoCxf.totalPasskeys} llaves de paso",
                    color = ColorPasskeys
                )
            }
            if (resultadoCxf.totalContrasenas > 0) {
                InsigniaResumenCxf(
                    icono = Icons.Filled.Lock,
                    texto = "${resultadoCxf.totalContrasenas} contraseñas",
                    color = Color(0xFF2196F3)
                )
            }
            if (resultadoCxf.totalTotp > 0) {
                InsigniaResumenCxf(
                    icono = Icons.Filled.QrCode,
                    texto = "${resultadoCxf.totalTotp} verificación en dos pasos",
                    color = Color2FA
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Botones rápidos de selección adaptados al tema
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonSeleccionRapida(
                texto = "Solo nuevas",
                activo = soloNuevasSeleccionadas,
                alPulsar = alSeleccionarSoloNuevas
            )
            BotonSeleccionRapida(
                texto = "Todas ($totalItems)",
                activo = todasSeleccionadas,
                alPulsar = alSeleccionarTodas
            )
            BotonSeleccionRapida(
                texto = "Ninguna",
                activo = ningunaSeleccionada,
                alPulsar = alSeleccionarNinguna
            )
        }
    }
}

@Composable
fun InsigniaResumenCxf(
    icono: ImageVector,
    texto: String,
    color: Color
) {
    val colorLegible = colorLegibleParaTema(color)
    val fondo = fondoBadgeParaTema(color)
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(fondo)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, colorLegible.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                } else Modifier
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = colorLegible,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = texto,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = colorLegible
        )
    }
}

@Composable
fun BotonSeleccionRapida(
    texto: String,
    activo: Boolean,
    alPulsar: () -> Unit
) {
    val forma = FormaPequena
    Box(
        modifier = Modifier
            .clip(forma)
            .background(
                if (activo) DegradadoAcento else Brush.horizontalGradient(listOf(ColorTarjetaAjustes, ColorTarjetaAjustes))
            )
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, if (activo) ColorAcento else ColorBordeActual, forma)
                } else Modifier
            )
            .clickable(onClick = alPulsar)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (activo) FontWeight.Bold else FontWeight.Medium
            ),
            color = if (activo) ColorSobreAcento else TextoSecundario
        )
    }
}
