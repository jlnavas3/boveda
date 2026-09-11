package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.IlustracionVacio
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import java.util.concurrent.TimeUnit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera

private const val DIAS_PAPELERA = 30L

@Composable
fun PantallaPapelera(vm: VaultViewModel, estado: EstadoBoveda) {
    val papelera = (estado as? EstadoBoveda.Desbloqueada)?.papelera ?: emptyList()
    val ahora = remember { System.currentTimeMillis() }
    var confirmarVaciar by remember { mutableStateOf(false) }
    var aBorrarDefinitivo by remember { mutableStateOf<Entrada?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        CabeceraPantalla(
            titulo = "Papelera",
            subtitulo = "Entradas eliminadas en los últimos $DIAS_PAPELERA días",
            alVolver = { vm.volverAtras() }
        )

        if (papelera.isEmpty()) {
            IlustracionVacio()
        } else {
            papelera.sortedByDescending { it.eliminadaEn }.forEach { entrada ->
                FilaPapelera(
                    entrada = entrada,
                    diasRestantes = (DIAS_PAPELERA - diasDesde(entrada.eliminadaEn, ahora)).coerceAtLeast(0),
                    alRestaurar = { vm.restaurarDeLaPapelera(entrada.id) },
                    alBorrarDefinitivo = { aBorrarDefinitivo = entrada }
                )
                Spacer(Modifier.height(10.dp))
            }
            Spacer(Modifier.height(10.dp))
            BotonColorido(
                texto = "Vaciar papelera",
                color = ColorPapelera,
                icono = Icons.Filled.Delete
            ) { confirmarVaciar = true }
        }

        Spacer(Modifier.height(32.dp))
    }

    if (confirmarVaciar) {
        AlertDialog(
            onDismissRequest = { confirmarVaciar = false },
            containerColor = ColorTarjetas,
            title = { Text("¿Vaciar la papelera?", color = TextoPrincipal) },
            text = { Text("Borra para siempre las ${papelera.size} entradas de aquí. No hay vuelta atrás.", color = TextoSecundario) },
            confirmButton = {
                TextButton(onClick = { confirmarVaciar = false; vm.vaciarPapelera() }) { Text("Vaciar", color = Peligro) }
            },
            dismissButton = {
                TextButton(onClick = { confirmarVaciar = false }) { Text("Cancelar", color = TextoSecundario) }
            }
        )
    }

    aBorrarDefinitivo?.let { entrada ->
        AlertDialog(
            onDismissRequest = { aBorrarDefinitivo = null },
            containerColor = ColorTarjetas,
            title = { Text("¿Borrar para siempre?", color = TextoPrincipal) },
            text = { Text("\"${entrada.titulo.ifBlank { "Sin título" }}\" desaparece sin posibilidad de recuperarla.", color = TextoSecundario) },
            confirmButton = {
                TextButton(onClick = {
                    val id = entrada.id
                    aBorrarDefinitivo = null
                    vm.borrarDefinitivamente(id)
                }) { Text("Borrar para siempre", color = Peligro) }
            },
            dismissButton = {
                TextButton(onClick = { aBorrarDefinitivo = null }) { Text("Cancelar", color = TextoSecundario) }
            }
        )
    }
}

@Composable
private fun FilaPapelera(
    entrada: Entrada,
    diasRestantes: Long,
    alRestaurar: () -> Unit,
    alBorrarDefinitivo: () -> Unit
) {
    com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepo {
        Text(entrada.titulo.ifBlank { "Sin título" }, color = ColorTitulos, style = MaterialTheme.typography.bodyLarge)
        Text(
            entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "Sin usuario" },
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            if (diasRestantes > 0) "Se borra sola en $diasRestantes días" else "Se borrará sola en cualquier momento",
            color = if (diasRestantes <= 3) Peligro else ColorAcento,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TextButton(onClick = alRestaurar) { Text("Restaurar", color = Menta) }
            TextButton(onClick = alBorrarDefinitivo) { Text("Borrar para siempre", color = Peligro) }
        }
    }
}

private fun diasDesde(momento: Long, ahora: Long): Long =
    TimeUnit.MILLISECONDS.toDays((ahora - momento).coerceAtLeast(0))
