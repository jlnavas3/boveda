package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/**
 * Microcomponente para el botón flotante de guardado en la pantalla de edición.
 */
@Composable
fun BotonGuardarEdicion(
    alGuardar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formaFab = RoundedCornerShape(CurvaturaEsquinas)

    FloatingActionButton(
        onClick = alGuardar,
        containerColor = ColorAcento,
        contentColor = ColorSobreAcento,
        shape = formaFab,
        modifier = modifier
            .navigationBarsPadding()
            .imePadding()
            .padding(20.dp)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Guardar",
            modifier = Modifier.size(24.dp)
        )
    }
}
