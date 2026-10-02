package com.jlnavas3.bovedalocal.ui.componentes.seleccion

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Botón de acción granular para transferir credenciales seleccionadas directamente
 * mediante el estándar FIDO CXF / Credential Transfer API hacia otro gestor o dispositivo.
 */
@Composable
fun BotonTransferirSeleccion(
    habilitado: Boolean,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = Haptica(contexto)

    IconButton(
        onClick = {
            haptica.toque()
            alPulsar()
        },
        enabled = habilitado,
        modifier = modifier.size(46.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.VpnKey,
            contentDescription = "Transferir credenciales seleccionadas",
            tint = if (habilitado) ColorPasskeys else TextoSecundario.copy(alpha = 0.35f),
            modifier = Modifier.size(23.dp)
        )
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun BotonTransferirSeleccionPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        androidx.compose.foundation.layout.Row {
            BotonTransferirSeleccion(habilitado = true, alPulsar = {})
            BotonTransferirSeleccion(habilitado = false, alPulsar = {})
        }
    }
}

