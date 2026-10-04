package com.jlnavas3.bovedalocal.ui.pantallas.migracion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoSuperficie
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

/**
 * Barra superior de controles de selección para la pantalla de migración 2FA.
 * Permite seleccionar solo nuevas cuentas, todas o ninguna mediante botones táctiles de superficie.
 */
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
        TextoSubtitulo(
            texto = "$cuantasSeleccionadas de $totalCuentas seleccionadas",
            color = ColorAcento,
            maxLineas = 1,
            modifier = Modifier.weight(1f, fill = false)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonIconoSuperficie(
                icono = Icons.Filled.AutoAwesome,
                descripcion = "Solo nuevas",
                colorIcono = ColorAcento,
                fondo = ColorAcento.copy(alpha = 0.16f),
                tamano = 34.dp,
                tamanoIcono = 19.dp,
                alPulsar = alSeleccionarSoloNuevas
            )

            BotonIconoSuperficie(
                icono = Icons.Filled.SelectAll,
                descripcion = "Seleccionar todas",
                colorIcono = TextoPrincipal,
                fondo = fondoBotonNeutro,
                tamano = 34.dp,
                tamanoIcono = 19.dp,
                alPulsar = alSeleccionarTodas
            )

            BotonIconoSuperficie(
                icono = Icons.Filled.Deselect,
                descripcion = "Deseleccionar todas",
                colorIcono = TextoSecundario,
                fondo = fondoBotonNeutro,
                tamano = 34.dp,
                tamanoIcono = 19.dp,
                alPulsar = alSeleccionarNinguna
            )
        }
    }
}
