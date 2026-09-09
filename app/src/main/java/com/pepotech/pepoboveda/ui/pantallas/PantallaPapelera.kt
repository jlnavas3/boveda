package com.pepotech.pepoboveda.ui.pantallas

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
import com.pepotech.pepoboveda.data.Entrada
import com.pepotech.pepoboveda.data.EstadoBoveda
import com.pepotech.pepoboveda.ui.VaultViewModel
import com.pepotech.pepoboveda.ui.componentes.BotonBorde
import com.pepotech.pepoboveda.ui.componentes.IlustracionVacio
import com.pepotech.pepoboveda.ui.theme.Ambar
import com.pepotech.pepoboveda.ui.theme.Menta
import com.pepotech.pepoboveda.ui.theme.Peligro
import com.pepotech.pepoboveda.ui.theme.Superficie
import com.pepotech.pepoboveda.ui.theme.SuperficieAlta
import com.pepotech.pepoboveda.ui.theme.TextoPrincipal
import com.pepotech.pepoboveda.ui.theme.TextoSecundario
import java.util.concurrent.TimeUnit

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
        Text("Papelera", style = MaterialTheme.typography.headlineMedium, color = TextoPrincipal)
        Text(
            "Lo que borras se queda aquí $DIAS_PAPELERA días por si te hace falta, y luego se borra solo.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario
        )
        Spacer(Modifier.height(18.dp))

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
            BotonBorde("Vaciar papelera", color = Peligro) { confirmarVaciar = true }
        }

        Spacer(Modifier.height(20.dp))
        BotonBorde("Volver") { vm.volverALista() }
        Spacer(Modifier.height(40.dp))
    }

    if (confirmarVaciar) {
        AlertDialog(
            onDismissRequest = { confirmarVaciar = false },
            containerColor = SuperficieAlta,
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
            containerColor = SuperficieAlta,
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Superficie)
            .padding(14.dp)
    ) {
        Text(entrada.titulo.ifBlank { "Sin título" }, color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
        Text(
            entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "Sin usuario" },
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            if (diasRestantes > 0) "Se borra sola en $diasRestantes días" else "Se borrará sola en cualquier momento",
            color = if (diasRestantes <= 3) Peligro else Ambar,
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
