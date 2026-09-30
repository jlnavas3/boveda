package com.jlnavas3.bovedalocal.ui.pantallas.migracion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar

@Composable
fun BotonesAccionInferioresMigracion(
    mostrarBotonImportar: Boolean,
    cuantasSeleccionadas: Int,
    alImportar: () -> Unit,
    modifier: Modifier = Modifier,
    alCancelar: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (mostrarBotonImportar) {
            BotonAmbar(
                texto = if (cuantasSeleccionadas > 0)
                    "Importar $cuantasSeleccionadas ${if (cuantasSeleccionadas == 1) "cuenta" else "cuentas"} a la Bóveda"
                else
                    "Selecciona al menos una cuenta",
                icono = Icons.Filled.Check,
                activo = cuantasSeleccionadas > 0,
                alPulsar = alImportar
            )
        }
    }
}
