package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Resolución de icono por defecto para campos de texto de ajustes.
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
}
