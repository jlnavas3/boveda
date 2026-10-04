package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Columna vertical de acciones flotantes para la pantalla de exportación selectiva:
 * - Deseleccionar todo
 * - Seleccionar todo
 * - Botón principal de exportación con contador numérico bajo el icono
 */
@Composable
fun ColumnaAccionesFlotantesExportar(
    idsSeleccionadosCount: Int,
    todoMarcado: Boolean,
    exportarHabilitado: Boolean,
    alDeseleccionarTodo: () -> Unit,
    alSeleccionarTodo: () -> Unit,
    alExportar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formaFab = RoundedCornerShape(CurvaturaEsquinas)

    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Deseleccionar todo (arriba)
        SmallFloatingActionButton(
            onClick = alDeseleccionarTodo,
            containerColor = ColorTarjetaAjustes,
            contentColor = if (idsSeleccionadosCount > 0) TextoPrincipal else TextoSecundario.copy(alpha = 0.35f),
            shape = formaFab,
            modifier = Modifier.then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Deselect,
                contentDescription = "Deseleccionar todo",
                modifier = Modifier.size(20.dp)
            )
        }

        // 2. Seleccionar todo (al medio)
        SmallFloatingActionButton(
            onClick = alSeleccionarTodo,
            containerColor = ColorTarjetaAjustes,
            contentColor = if (todoMarcado) ColorAcento else TextoPrincipal,
            shape = formaFab,
            modifier = Modifier.then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
        ) {
            Icon(
                imageVector = Icons.Filled.SelectAll,
                contentDescription = "Seleccionar todo",
                modifier = Modifier.size(20.dp)
            )
        }

        // 3. Botón flotante principal: Exportar (con contador debajo del ícono)
        FloatingActionButton(
            onClick = alExportar,
            containerColor = if (exportarHabilitado) ColorAcento else ColorTarjetaAjustes,
            contentColor = if (exportarHabilitado) ColorSobreAcento else TextoSecundario.copy(alpha = 0.4f),
            shape = formaFab,
            modifier = Modifier.then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Backup,
                    contentDescription = "Exportar",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "$idsSeleccionadosCount",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
