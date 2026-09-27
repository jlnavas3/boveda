package com.jlnavas3.bovedalocal.ui.pantallas.generador

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Pattern
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SelectorModoEstrategia(
    opciones: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    val modoActual = when {
        opciones.modoFrase -> "Frase Diceware"
        opciones.modoPatron -> "Por Patrón"
        else -> "Aleatoria"
    }

    val iconoModo = when {
        opciones.modoFrase -> Icons.AutoMirrored.Filled.MenuBook
        opciones.modoPatron -> Icons.Filled.Pattern
        else -> Icons.Filled.Shuffle
    }

    val opcionesModo = remember {
        listOf(
            OpcionSelectorModal("Aleatoria", "Aleatoria", "Aleatoria", "Caracteres alfanuméricos y símbolos", Icons.Filled.Shuffle),
            OpcionSelectorModal("Frase Diceware", "Frase Diceware", "Frase Diceware", "Palabras memorables de alta entropía", Icons.AutoMirrored.Filled.MenuBook),
            OpcionSelectorModal("Por Patrón", "Por Patrón", "Por Patrón", "Estructura y plantillas personalizadas", Icons.Filled.Pattern)
        )
    }

    ComponenteSelectorModal(
        titulo = "Modo de generación",
        tituloFila = "Modo",
        descripcionModal = "Elige la estrategia para forjar tu contraseña",
        icono = iconoModo,
        colorIcono = ColorGenerador,
        valorSeleccionado = modoActual,
        opciones = opcionesModo,
        alSeleccionar = { nuevoModo ->
            haptica.tic()
            val nuevas = when (nuevoModo) {
                "Frase Diceware" -> opciones.copy(modoFrase = true, modoPatron = false)
                "Por Patrón" -> opciones.copy(modoFrase = false, modoPatron = true)
                else -> opciones.copy(modoFrase = false, modoPatron = false)
            }
            alCambiarOpciones(nuevas)
        },
        fijarAbajo = true,
        modifier = modifier
    )
}
