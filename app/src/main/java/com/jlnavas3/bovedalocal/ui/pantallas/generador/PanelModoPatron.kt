package com.jlnavas3.bovedalocal.ui.pantallas.generador

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PanelModoPatron(
    opciones: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    haptica: Haptica
) {
    SelectorPlantillaPatron(
        patronActual = opciones.patron,
        alSeleccionarPlantilla = { nuevaPlantilla ->
            haptica.tic()
            alCambiarOpciones(opciones.copy(patron = nuevaPlantilla))
        }
    )

    Spacer(Modifier.height(12.dp))

    CampoBoveda(
        valor = opciones.patron,
        etiqueta = "Máscara / Patrón personalizado",
        alCambiar = { alCambiarOpciones(opciones.copy(patron = it)) },
        monoespaciada = true
    )

    Spacer(Modifier.height(8.dp))
    Text(
        "X: alfanumérica (A-Z, 0-9) | A: mayúscula (A-Z) | a: minúscula (a-z) | 9 o d: dígito (0-9) | w: palabra Diceware",
        color = TextoSecundario,
        style = MaterialTheme.typography.bodySmall
    )
}
