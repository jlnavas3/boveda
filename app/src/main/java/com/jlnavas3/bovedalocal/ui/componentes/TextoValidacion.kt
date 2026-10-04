package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.jlnavas3.bovedalocal.ui.theme.Advertencia
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Texto reactivo para indicar el estado de validación de campos y contraseñas.
 */
@Composable
fun TextoValidacion(
    texto: String,
    estado: EstadoValidacion,
    modifier: Modifier = Modifier
) {
    val colorTexto = when (estado) {
        EstadoValidacion.NEUTRO -> TextoSecundario
        EstadoValidacion.EXITO -> Menta
        EstadoValidacion.ERROR -> Peligro
        EstadoValidacion.ADVERTENCIA -> Advertencia
    }

    Text(
        text = texto,
        color = colorTexto,
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
        modifier = modifier
    )
}
