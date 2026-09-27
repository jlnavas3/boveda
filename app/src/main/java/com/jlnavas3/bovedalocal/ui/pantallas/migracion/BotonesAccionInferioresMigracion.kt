package com.jlnavas3.bovedalocal.ui.pantallas.migracion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde

@Composable
fun BotonesAccionInferioresMigracion(
    mostrarBotonImportar: Boolean,
    cuantasSeleccionadas: Int,
    alImportar: () -> Unit,
    alCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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

        BotonBorde(
            texto = "Cancelar",
            icono = Icons.Filled.Close,
            alPulsar = alCancelar
        )
    }
}
