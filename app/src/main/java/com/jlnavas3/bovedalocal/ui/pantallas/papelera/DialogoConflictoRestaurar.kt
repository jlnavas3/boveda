package com.jlnavas3.bovedalocal.ui.pantallas.papelera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.BotonTextoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TipoBotonTexto
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

/**
 * Diálogo modal para resolver conflictos al restaurar una entrada con el mismo título/usuario.
 */
@Composable
fun DialogoConflictoRestaurar(
    entrada: Entrada,
    alCerrar: () -> Unit,
    alSustituir: () -> Unit,
    alDuplicar: () -> Unit
) {
    val fondoDialogo = if (esOscuroActivo) Color(0xFF212023) else Color(0xFFFFFFFF)
    AlertDialog(
        onDismissRequest = alCerrar,
        shape = RoundedCornerShape(20.dp),
        containerColor = fondoDialogo,
        tonalElevation = 0.dp,
        title = {
            Text(
                text = "Entrada ya existente",
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column {
                Text(
                    text = "Ya existe una entrada activa con el nombre \"${entrada.titulo.ifBlank { "Sin título" }}\" en tu bóveda.",
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "¿Deseas sustituir la existente o conservar ambas creando una copia independiente?",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonTextoBoveda(
                    texto = "Sustituir",
                    tipo = TipoBotonTexto.PELIGRO,
                    alPulsar = alSustituir
                )
                BotonTextoBoveda(
                    texto = "Duplicar",
                    tipo = TipoBotonTexto.PRIMARIO,
                    alPulsar = alDuplicar
                )
            }
        },
        dismissButton = {
            BotonTextoBoveda(
                texto = "Cancelar",
                tipo = TipoBotonTexto.SECUNDARIO,
                alPulsar = alCerrar
            )
        }
    )
}
