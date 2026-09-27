package com.jlnavas3.bovedalocal.ui.pantallas.generador

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Pattern
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionConfiguracionPatron(
    opciones: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val opcionesPlantilla = remember {
            PLANTILLAS_PATRON_RAPIDO.map { (nombre, patron) ->
                OpcionSelectorModal(
                    valor = patron,
                    etiquetaFila = nombre,
                    etiquetaModal = nombre,
                    descripcionModal = patron,
                    icono = Icons.Filled.Pattern
                )
            }
        }
        ComponenteSelectorModal(
            titulo = "Plantilla rápida",
            tituloFila = "Plantilla",
            descripcionModal = "Formatos y estructuras comunes predefinidas",
            icono = Icons.Filled.AutoAwesome,
            colorIcono = ColorGenerador,
            valorSeleccionado = opciones.patron,
            opciones = opcionesPlantilla,
            alSeleccionar = { nuevaPlantilla ->
                haptica.tic()
                alCambiarOpciones(opciones.copy(patron = nuevaPlantilla))
            },
            fijarAbajo = true
        )

        ComponenteSeparador()

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
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
    }
}
