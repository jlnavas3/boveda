package com.jlnavas3.bovedalocal.ui.pantallas.desbloqueo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador

/**
 * Botón fila especializado para invocar la autenticación biométrica en el desbloqueo.
 */
@Composable
fun BotonDesbloqueoBiometrico(
    etiqueta: String,
    habilitado: Boolean,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteSeparador(sangriaInicio = 60.dp)
    ComponenteBotonFila(
        titulo = etiqueta,
        alPulsar = alPulsar,
        icono = Icons.Filled.Fingerprint,
        colorIcono = Color(0xFF1E88E5),
        colorTinteIcono = Color.White,
        habilitado = habilitado,
        modifier = modifier
    )
}
