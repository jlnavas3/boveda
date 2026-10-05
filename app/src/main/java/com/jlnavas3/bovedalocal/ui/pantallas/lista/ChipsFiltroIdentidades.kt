package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * Barra horizontal con chips de filtro rápido por [Identidad] para el listado principal.
 */
@Composable
fun ChipsFiltroIdentidades(
    identidades: List<Identidad>,
    identidadSeleccionadaId: String?,
    conteoPorIdentidad: Map<String, Int>,
    totalEntradas: Int,
    conteoSinIdentidad: Int,
    alSeleccionarIdentidad: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (identidades.isEmpty()) return

    val forma = RoundedCornerShape(16.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Chip "Todas"
        val todasSeleccionado = identidadSeleccionadaId == null
        Box(
            modifier = Modifier
                .clip(forma)
                .background(if (todasSeleccionado) ColorAcento else ColorTarjetaAjustes)
                .border(
                    width = 0.8.dp,
                    color = if (todasSeleccionado) ColorAcento else ColorSeparadorAjustes,
                    shape = forma
                )
                .clickable { alSeleccionarIdentidad(null) }
                .padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Todas",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (todasSeleccionado) FontWeight.Bold else FontWeight.Normal
                    ),
                    color = if (todasSeleccionado) ColorSobreAcento else TextoPrincipal
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = "$totalEntradas",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = if (todasSeleccionado) ColorSobreAcento.copy(alpha = 0.85f) else TextoSecundario
                )
            }
        }

        // Chips por cada Identidad configurada
        identidades.forEach { iden ->
            val seleccionado = identidadSeleccionadaId == iden.id
            val colorBase = parsearColorO(iden.colorHex ?: "", ColorAcento)
            val conteo = conteoPorIdentidad[iden.id] ?: 0

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
                        if (seleccionado) alSeleccionarIdentidad(null) else alSeleccionarIdentidad(iden.id)
                    }
                    .padding(horizontal = 11.dp, vertical = 6.dp),
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
                            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (seleccionado) ColorSobreAcento else TextoPrincipal
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = "$conteo",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = if (seleccionado) ColorSobreAcento.copy(alpha = 0.85f) else TextoSecundario
                    )
                }
            }
        }

        // Chip "Sin identidad" / "Otras" (si hay cuentas sin vincular)
        if (conteoSinIdentidad > 0) {
            val sinIdSeleccionado = identidadSeleccionadaId == "__SIN_IDENTIDAD__"
            Box(
                modifier = Modifier
                    .clip(forma)
                    .background(if (sinIdSeleccionado) ColorAcento else ColorTarjetaAjustes)
                    .border(
                        width = 0.8.dp,
                        color = if (sinIdSeleccionado) ColorAcento else ColorSeparadorAjustes,
                        shape = forma
                    )
                    .clickable {
                        if (sinIdSeleccionado) alSeleccionarIdentidad(null) else alSeleccionarIdentidad("__SIN_IDENTIDAD__")
                    }
                    .padding(horizontal = 11.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Otras",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (sinIdSeleccionado) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (sinIdSeleccionado) ColorSobreAcento else TextoPrincipal
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = "$conteoSinIdentidad",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = if (sinIdSeleccionado) ColorSobreAcento.copy(alpha = 0.85f) else TextoSecundario
                    )
                }
            }
        }
    }
}
