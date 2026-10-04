package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.colorContraste

/**
 * Microcomponente para los botones flotantes de acción en el Laboratorio de Temas:
 * Restablecer valores de fábrica, Copiar paleta al portapapeles y Guardar tema.
 */
@Composable
fun ColumnaAccionesFlotantesLab(
    colorAcentoActual: Color,
    alRestablecer: () -> Unit,
    alCopiarPaleta: () -> Unit,
    alGuardar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formaFab = RoundedCornerShape(CurvaturaEsquinas)

    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // FAB 1 (superior): Restablecer valores predeterminados
        SmallFloatingActionButton(
            onClick = alRestablecer,
            containerColor = ColorTarjetaAjustes,
            contentColor = colorAcentoActual,
            shape = formaFab,
            modifier = Modifier.then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
        ) {
            Icon(
                imageVector = Icons.Filled.RestartAlt,
                contentDescription = "Restablecer valores predeterminados",
                modifier = Modifier.size(20.dp)
            )
        }

        // FAB 2 (intermedio): Copiar paleta al portapapeles
        SmallFloatingActionButton(
            onClick = alCopiarPaleta,
            containerColor = ColorTarjetaAjustes,
            contentColor = colorAcentoActual,
            shape = formaFab,
            modifier = Modifier.then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
        ) {
            Icon(
                imageVector = Icons.Filled.ContentCopy,
                contentDescription = "Copiar paleta",
                modifier = Modifier.size(20.dp)
            )
        }

        // FAB 3 (inferior): Guardar tema
        FloatingActionButton(
            onClick = alGuardar,
            containerColor = colorAcentoActual,
            contentColor = colorContraste(colorAcentoActual),
            shape = formaFab,
            modifier = Modifier.then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Save,
                contentDescription = "Guardar tema",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
