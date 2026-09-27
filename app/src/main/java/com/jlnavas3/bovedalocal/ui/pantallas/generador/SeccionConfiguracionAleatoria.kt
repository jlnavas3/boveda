package com.jlnavas3.bovedalocal.ui.pantallas.generador

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorMultipleModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorMultipleModal
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionConfiguracionAleatoria(
    opciones: OpcionesGenerador,
    alCambiarOpciones: (OpcionesGenerador) -> Unit,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val activos = buildList {
            if (opciones.mayusculas) add("A-Z")
            if (opciones.minusculas) add("a-z")
            if (opciones.digitos) add("0-9")
            if (opciones.simbolos) add("#$!")
        }
        val textoResumen = when {
            activos.isEmpty() -> "Ninguno"
            activos.size == 4 -> "Todos (4)"
            else -> activos.joinToString(" ")
        }
        val opcionesCaracteres = listOf(
            OpcionSelectorMultipleModal("mayusculas", "Mayúsculas (A-Z)", "Mayúsculas (A-Z)", "A, B, C... Z", Icons.Filled.TextFields, opciones.mayusculas),
            OpcionSelectorMultipleModal("minusculas", "Minúsculas (a-z)", "Minúsculas (a-z)", "a, b, c... z", Icons.Filled.TextFields, opciones.minusculas),
            OpcionSelectorMultipleModal("digitos", "Números (0-9)", "Números (0-9)", "0, 1, 2... 9", Icons.Filled.Numbers, opciones.digitos),
            OpcionSelectorMultipleModal("simbolos", "Símbolos (#$!)", "Símbolos (#$!)", "! @ # $ % & * - _ + =", Icons.Filled.Tag, opciones.simbolos)
        )

        ComponenteSelectorMultipleModal(
            titulo = "Caracteres permitidos",
            tituloFila = "Caracteres",
            valorTexto = textoResumen,
            opciones = opcionesCaracteres,
            alAlternar = { clave ->
                haptica.tic()
                val nuevas = when (clave) {
                    "mayusculas" -> opciones.copy(mayusculas = !opciones.mayusculas)
                    "minusculas" -> opciones.copy(minusculas = !opciones.minusculas)
                    "digitos" -> opciones.copy(digitos = !opciones.digitos)
                    "simbolos" -> opciones.copy(simbolos = !opciones.simbolos)
                    else -> opciones
                }
                alCambiarOpciones(nuevas)
            },
            icono = Icons.Filled.Tune,
            colorIcono = ColorGenerador,
            descripcionModal = "Conjuntos de caracteres incluidos en la clave",
            fijarAbajo = true
        )

        ComponenteSeparador()

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
            PanelModoAleatorio(
                opciones = opciones,
                alCambiarOpciones = alCambiarOpciones,
                haptica = haptica
            )
        }
    }
}
