package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.IlustracionVacio
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepoDesplegable
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
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
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        CabeceraPantalla(
            titulo = "Papelera",
            subtitulo = if (papelera.isEmpty()) "Sin elementos en la papelera" else "${papelera.size} entrada${if (papelera.size == 1) "" else "s"} · Se borran tras $DIAS_PAPELERA días",
            alVolver = { vm.volverAtras() }
        )

        Spacer(Modifier.height(8.dp))

        if (papelera.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                IlustracionVacio()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(EspaciadoComponentes.coerceAtLeast(8.dp))
            ) {
                items(papelera.sortedByDescending { it.eliminadaEn }, key = { it.id }) { entrada ->
                    val diasRestantes = (DIAS_PAPELERA - diasDesde(entrada.eliminadaEn, ahora)).coerceAtLeast(0)
                    FilaPapeleraDesplegable(
                        entrada = entrada,
                        diasRestantes = diasRestantes,
                        alRestaurar = {
                            vm.restaurarDeLaPapelera(entrada.id)
                            vm.avisar("Entrada restaurada")
                        },
                        alBorrarDefinitivo = { aBorrarDefinitivo = entrada }
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            BotonColorido(
                texto = "Vaciar papelera (${papelera.size})",
                color = ColorPapelera,
                icono = Icons.Filled.Delete,
                modifier = Modifier.fillMaxWidth()
            ) {
                confirmarVaciar = true
            }
        }
    }

    if (confirmarVaciar) {
        AlertDialog(
            onDismissRequest = { confirmarVaciar = false },
            containerColor = ColorTarjetas,
            title = { Text("¿Vaciar la papelera?", color = ColorTitulos) },
            text = { Text("Borra para siempre las ${papelera.size} entradas de aquí. No hay vuelta atrás.", color = TextoSecundario) },
            confirmButton = {
                TextButton(onClick = {
                    confirmarVaciar = false
                    vm.vaciarPapelera()
                    vm.avisar("Papelera vaciada")
                }) { Text("Vaciar", color = Peligro) }
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
            title = { Text("¿Borrar para siempre?", color = ColorTitulos) },
            text = { Text("\"${entrada.titulo.ifBlank { "Sin título" }}\" desaparece sin posibilidad de recuperarla.", color = TextoSecundario) },
            confirmButton = {
                TextButton(onClick = {
                    val id = entrada.id
                    aBorrarDefinitivo = null
                    vm.borrarDefinitivamente(id)
                    vm.avisar("Entrada eliminada definitivamente")
                }) { Text("Borrar para siempre", color = Peligro) }
            },
            dismissButton = {
                TextButton(onClick = { aBorrarDefinitivo = null }) { Text("Cancelar", color = TextoSecundario) }
            }
        )
    }
}

@Composable
private fun FilaPapeleraDesplegable(
    entrada: Entrada,
    diasRestantes: Long,
    alRestaurar: () -> Unit,
    alBorrarDefinitivo: () -> Unit
) {
    val icono = when (entrada.tipo) {
        TipoEntrada.LOGIN -> Icons.Filled.Lock
        TipoEntrada.PASSKEY -> Icons.Filled.Fingerprint
        TipoEntrada.NOTA -> Icons.Filled.Description
    }

    val subtitulo = entrada.usuario.ifBlank {
        entrada.urls.firstOrNull() ?: if (entrada.tipo == TipoEntrada.NOTA) "Nota segura" else "Sin credencial"
    }

    TarjetaPepoDesplegable(
        titulo = entrada.titulo.ifBlank { "Sin título" },
        descripcion = subtitulo,
        icono = icono,
        colorIcono = ColorIconosInternos,
        inicialmenteAbierta = false
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Schedule,
                contentDescription = null,
                tint = if (diasRestantes <= 3) Peligro else ColorAcento,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = if (diasRestantes > 0) "Se borra sola en $diasRestantes días" else "Se borrará sola en cualquier momento",
                color = if (diasRestantes <= 3) Peligro else ColorAcento,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (entrada.urls.isNotEmpty() && entrada.usuario.isNotBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = entrada.urls.joinToString(", "),
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BotonColorido(
                texto = "Restaurar",
                color = Menta,
                icono = Icons.Filled.Restore,
                modifier = Modifier.weight(1f)
            ) {
                alRestaurar()
            }

            BotonColorido(
                texto = "Borrar",
                color = Peligro,
                icono = Icons.Filled.DeleteForever,
                modifier = Modifier.weight(1f)
            ) {
                alBorrarDefinitivo()
            }
        }
    }
}

private fun diasDesde(momento: Long, ahora: Long): Long =
    TimeUnit.MILLISECONDS.toDays((ahora - momento).coerceAtLeast(0))
