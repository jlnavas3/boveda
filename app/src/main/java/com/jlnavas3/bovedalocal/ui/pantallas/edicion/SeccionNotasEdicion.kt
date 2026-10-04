package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Microcomponente para el bloque de notas y observaciones de una credencial.
 */
@Composable
fun SeccionNotasEdicion(
    notas: String,
    alCambiarNotas: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    GrupoAjustes(
        etiqueta = "Notas",
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            ComponenteCampoTexto(
                valor = notas,
                etiqueta = "Notas y detalles",
                alCambiar = alCambiarNotas,
                tipo = TipoCampoTexto.MULTILINEA,
                colorBordeIzquierdo = ColorAcento
            )
        }
    }
}
