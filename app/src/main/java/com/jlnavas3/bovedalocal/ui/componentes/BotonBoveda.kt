package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class VarianteBoton { PRIMARIO, SECUNDARIO, PELIGRO }

/**
 * Componente Botón centralizado de la bóveda que delega a la variante de botón correspondiente.
 */
@Composable
fun BotonBoveda(
    texto: String,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier,
    variante: VarianteBoton = VarianteBoton.PRIMARIO,
    activo: Boolean = true,
    icono: ImageVector? = null
) {
    when (variante) {
        VarianteBoton.PRIMARIO -> BotonPrimario(texto = texto, modifier = modifier, activo = activo, icono = icono, alPulsar = alPulsar)
        VarianteBoton.SECUNDARIO -> BotonBorde(texto = texto, modifier = modifier, icono = icono, alPulsar = alPulsar)
        VarianteBoton.PELIGRO -> BotonPeligro(texto = texto, modifier = modifier, icono = icono, alPulsar = alPulsar)
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun BotonesBovedaPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BotonBoveda(texto = "Botón Primario", alPulsar = {}, variante = VarianteBoton.PRIMARIO)
            BotonBoveda(texto = "Botón Secundario", alPulsar = {}, variante = VarianteBoton.SECUNDARIO)
            BotonBoveda(texto = "Botón Peligro", alPulsar = {}, variante = VarianteBoton.PELIGRO)
            BotonBorde(texto = "Botón con Borde", alPulsar = {})
        }
    }
}
