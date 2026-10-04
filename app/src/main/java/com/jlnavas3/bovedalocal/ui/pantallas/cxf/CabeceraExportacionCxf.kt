package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoSuperficie
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Cabecera para la pantalla de exportación interactiva CXF.
 * Muestra el nombre de la app receptora y la barra de controles con etiqueta a la izquierda
 * y botones de superficie para seleccionar/deseleccionar todo a la derecha.
 */
@Composable
fun CabeceraExportacionCxf(
    gestorReceptor: String,
    totalElementos: Int,
    seleccionados: Int,
    alSeleccionarTodas: () -> Unit,
    alDeseleccionarTodas: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextoTitulo(
            texto = "Solicitud de transferencia directa",
            estilo = EstiloTitulo.MEDIANO
        )

        TextoSubtitulo(
            texto = "La aplicación $gestorReceptor solicita credenciales de tu Bóveda."
        )

        ContenedorTarjeta(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextoCuerpo(
                    texto = "$seleccionados de $totalElementos seleccionadas",
                    color = ColorTitulos,
                    maxLineas = 1,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BotonIconoSuperficie(
                        icono = Icons.Filled.SelectAll,
                        descripcion = "Seleccionar todo",
                        colorIcono = ColorTitulos,
                        fondo = ColorTitulos.copy(alpha = 0.09f),
                        tamano = 36.dp,
                        tamanoIcono = 20.dp,
                        alPulsar = alSeleccionarTodas
                    )

                    BotonIconoSuperficie(
                        icono = Icons.Filled.Deselect,
                        descripcion = "Deseleccionar todo",
                        colorIcono = TextoSecundario,
                        fondo = ColorTitulos.copy(alpha = 0.09f),
                        tamano = 36.dp,
                        tamanoIcono = 20.dp,
                        alPulsar = alDeseleccionarTodas
                    )
                }
            }
        }
    }
}
