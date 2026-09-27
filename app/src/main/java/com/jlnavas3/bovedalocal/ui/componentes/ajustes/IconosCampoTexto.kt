package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema

/**
 * Componentes de iconos principales y secundarios para campos de texto de ajustes.
 */
internal object IconosCampoTexto {

    fun resolverIconoPorDefecto(tipo: TipoCampoTexto): ImageVector {
        return when (tipo) {
            TipoCampoTexto.TEXTO -> Icons.Filled.Edit
            TipoCampoTexto.NUMERICO -> Icons.Filled.Pin
            TipoCampoTexto.CONTRASENA -> Icons.Filled.Lock
            TipoCampoTexto.FECHA -> Icons.Filled.DateRange
            TipoCampoTexto.HORA -> Icons.Filled.Schedule
            TipoCampoTexto.ENLACE -> Icons.Filled.Link
            TipoCampoTexto.MULTILINEA -> Icons.Filled.Description
        }
    }

    @Composable
    fun IconoInicio(
        icono: ImageVector,
        colorIcono: Color?,
        esOscuro: Boolean
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = colorLegibleParaTema(colorIcono ?: ColorAcento, esOscuro),
            modifier = Modifier.size(20.dp)
        )
    }

    @Composable
    fun IconoAlternarContrasena(
        esVisible: Boolean,
        onToggle: () -> Unit
    ) {
        IconButton(onClick = onToggle) {
            Icon(
                imageVector = if (esVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                contentDescription = if (esVisible) "Ocultar contraseña" else "Mostrar contraseña",
                tint = TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    @Composable
    fun BotonLimpiarTexto(
        onLimpiar: () -> Unit
    ) {
        IconButton(onClick = onLimpiar) {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = "Limpiar texto",
                tint = TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
