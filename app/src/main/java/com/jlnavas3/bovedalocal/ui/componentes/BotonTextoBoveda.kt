package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

enum class TipoBotonTexto { PRIMARIO, SECUNDARIO, PELIGRO }

/**
 * Botón de texto unificado para diálogos, modales y acciones secundarias.
 * Encapsula la háptica, tipografía y colores según la variante semántica.
 */
@Composable
fun BotonTextoBoveda(
    texto: String,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier,
    tipo: TipoBotonTexto = TipoBotonTexto.PRIMARIO,
    colorPersonalizado: Color? = null,
    habilitado: Boolean = true
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    val colorEfectivo = colorPersonalizado ?: when (tipo) {
        TipoBotonTexto.PRIMARIO -> ColorAcento
        TipoBotonTexto.SECUNDARIO -> TextoSecundario
        TipoBotonTexto.PELIGRO -> Peligro
    }

    TextButton(
        onClick = {
            if (tipo == TipoBotonTexto.PELIGRO) haptica.exito() else haptica.tic()
            alPulsar()
        },
        enabled = habilitado,
        modifier = modifier
    ) {
        Text(
            text = texto,
            color = if (habilitado) colorEfectivo else ColorAjusteGris,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}
