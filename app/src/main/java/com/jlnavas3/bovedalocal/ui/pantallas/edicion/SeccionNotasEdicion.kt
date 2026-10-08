package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Microcomponente para el bloque de notas y observaciones de una credencial,
 * presentado en una fila compacta colapsable.
 */
@Composable
fun SeccionNotasEdicion(
    notas: String,
    alCambiarNotas: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(notas.isNotBlank()) }

    val resumen = when {
        notas.isBlank() -> "Sin notas añadidas"
        else -> {
            val primeraLinea = notas.trim().lines().firstOrNull() ?: ""
            if (primeraLinea.length > 35) "${primeraLinea.take(35)}..." else primeraLinea
        }
    }

    FilaSeccionColapsableEdicion(
        icono = Icons.Filled.Description,
        colorIcono = ColorAcento,
        titulo = "Notas y detalles",
        resumen = resumen,
        expandido = expandido,
        alAlternarExpandido = { expandido = !expandido },
        modifier = modifier
    ) {
        ComponenteCampoTexto(
            valor = notas,
            etiqueta = "Notas y detalles",
            alCambiar = alCambiarNotas,
            tipo = TipoCampoTexto.MULTILINEA,
            varias = true
        )
    }
}
