package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import java.util.concurrent.TimeUnit

private const val DIAS_PAPELERA = 30L

@Composable
fun PantallaPapelera(
    vm: VaultViewModel,
    estado: EstadoBoveda,
    seccionDestino: String? = null
) {
    val papelera = (estado as? EstadoBoveda.Desbloqueada)?.papelera ?: emptyList()
    val entradasActivas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val ahora = remember { System.currentTimeMillis() }
    var confirmarVaciar by remember { mutableStateOf(false) }
    var aBorrarDefinitivo by remember { mutableStateOf<Entrada?>(null) }
    var conflictoRestaurar by remember { mutableStateOf<Entrada?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Papelera",
            alVolver = { vm.volverAtras() },
            colorFondo = ColorAjustesFondo,
            acciones = {
                if (papelera.isNotEmpty()) {
                    IconButton(onClick = { confirmarVaciar = true }) {
                        Icon(
                            imageVector = Icons.Filled.DeleteForever,
                            contentDescription = "Vaciar papelera",
                            tint = Peligro,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DescripcionPantalla(
                subtitulo = if (papelera.isEmpty()) "Sin elementos en la papelera" else "${papelera.size} entrada${if (papelera.size == 1) "" else "s"} · Se borran tras $DIAS_PAPELERA días"
            )

            Spacer(Modifier.height(10.dp))

            if (papelera.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    IlustracionPapeleraVacia()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Elementos eliminados en tarjeta agrupada nativa
                    item {
                        val ordenadas = papelera.sortedByDescending { it.eliminadaEn }
                        GrupoAjustes(etiqueta = "Elementos eliminados (${papelera.size})") {
                            ordenadas.forEachIndexed { index, entrada ->
                                if (index > 0) SeparadorFilaSimple()
                                val diasRestantes = (DIAS_PAPELERA - diasDesde(entrada.eliminadaEn, ahora)).coerceAtLeast(0)
                                FilaPapeleraNativa(
                                    entrada = entrada,
                                    diasRestantes = diasRestantes,
                                    alRestaurar = {
                                        val conflicto = entradasActivas.find {
                                            it.id == entrada.id || (it.titulo.trim().equals(entrada.titulo.trim(), ignoreCase = true) && it.usuario.trim() == entrada.usuario.trim())
                                        }
                                        if (conflicto != null) {
                                            conflictoRestaurar = entrada
                                        } else {
                                            vm.restaurarDeLaPapelera(entrada.id)
                                        }
                                    },
                                    alBorrarDefinitivo = { aBorrarDefinitivo = entrada }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (confirmarVaciar) {
        AlertDialog(
            onDismissRequest = { confirmarVaciar = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = ColorTarjetaAjustes,
            tonalElevation = 0.dp,
            title = {
                Text(
                    text = "¿Vaciar toda la papelera?",
                    color = ColorTextoAjustes,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Se destruirán definitivamente todas las ${papelera.size} entradas. Esta acción no se puede deshacer.",
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmarVaciar = false
                        vm.vaciarPapelera()
                        vm.avisar("Papelera vaciada")
                    }
                ) {
                    Text("Vaciar definitivamente", color = Peligro, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarVaciar = false }) {
                    Text("Cancelar", color = ColorAjusteGris)
                }
            }
        )
    }

    aBorrarDefinitivo?.let { entrada ->
        AlertDialog(
            onDismissRequest = { aBorrarDefinitivo = null },
            shape = RoundedCornerShape(20.dp),
            containerColor = ColorTarjetaAjustes,
            tonalElevation = 0.dp,
            title = {
                Text(
                    text = "¿Borrar definitivamente?",
                    color = ColorTextoAjustes,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Text(
                    text = "Se destruirá permanentemente \"${entrada.titulo.ifBlank { "Sin título" }}\". Esta acción no se puede deshacer.",
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val id = entrada.id
                        aBorrarDefinitivo = null
                        vm.borrarDefinitivamente(id)
                        vm.avisar("Entrada destruida")
                    }
                ) {
                    Text("Destruir", color = Peligro, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { aBorrarDefinitivo = null }) {
                    Text("Cancelar", color = ColorAjusteGris)
                }
            }
        )
    }

    conflictoRestaurar?.let { entrada ->
        AlertDialog(
            onDismissRequest = { conflictoRestaurar = null },
            shape = RoundedCornerShape(20.dp),
            containerColor = ColorTarjetaAjustes,
            tonalElevation = 0.dp,
            title = {
                Text(
                    text = "Entrada ya existente",
                    color = ColorTextoAjustes,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Ya existe una entrada activa con el nombre \"${entrada.titulo.ifBlank { "Sin título" }}\" en tu bóveda.",
                        color = ColorTextoAjustes,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "¿Deseas sustituir la existente o conservar ambas creando una copia independiente?",
                        color = ColorAjusteGris,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            val id = entrada.id
                            conflictoRestaurar = null
                            vm.restaurarDeLaPapelera(id, sustituir = true)
                        }
                    ) {
                        Text("Sustituir", color = Peligro, fontWeight = FontWeight.SemiBold)
                    }
                    TextButton(
                        onClick = {
                            val id = entrada.id
                            conflictoRestaurar = null
                            vm.restaurarDeLaPapelera(id, sustituir = false)
                        }
                    ) {
                        Text("Duplicar", color = ColorAcento, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { conflictoRestaurar = null }) {
                    Text("Cancelar", color = ColorAjusteGris)
                }
            }
        )
    }
}

@Composable
private fun FilaPapeleraNativa(
    entrada: Entrada,
    diasRestantes: Long,
    alRestaurar: () -> Unit,
    alBorrarDefinitivo: () -> Unit
) {
    val icono = when (entrada.tipo) {
        TipoEntrada.LOGIN -> Icons.Filled.Lock
        TipoEntrada.PASSKEY -> Icons.Filled.Fingerprint
        TipoEntrada.NOTA -> Icons.Filled.Description
        TipoEntrada.TARJETA -> Icons.Filled.CreditCard
        TipoEntrada.WIFI -> Icons.Filled.Wifi
        TipoEntrada.CUENTA_BANCARIA -> Icons.Filled.AccountBalance
        TipoEntrada.IDENTIDAD -> Icons.Filled.Badge
        TipoEntrada.SERVIDOR -> Icons.Filled.Dns
        TipoEntrada.WALLET -> Icons.Filled.AccountBalanceWallet
    }

    val colorIcono = when (entrada.tipo) {
        TipoEntrada.LOGIN -> ColorSeguridad
        TipoEntrada.PASSKEY -> ColorPasskeys
        TipoEntrada.NOTA -> ColorAcento
        TipoEntrada.TARJETA -> ColorGenerador
        TipoEntrada.WIFI -> ColorSalud
        TipoEntrada.CUENTA_BANCARIA -> ColorSeguridad
        TipoEntrada.IDENTIDAD -> ColorExportacion
        TipoEntrada.SERVIDOR -> ColorIconosInternos
        TipoEntrada.WALLET -> ColorAcento
    }

    val subtitulo = entrada.usuario.ifBlank {
        entrada.urls.firstOrNull() ?: entrada.tipo.etiqueta
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(fondoBadgeParaTema(colorIcono)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorLegibleParaTema(colorIcono),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = entrada.titulo.ifBlank { "Sin título" },
                color = ColorTextoAjustes,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitulo,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = null,
                    tint = if (diasRestantes <= 3) Peligro else ColorAjusteGris,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = if (diasRestantes > 0) "Expira en $diasRestantes días" else "Expira en cualquier momento",
                    color = if (diasRestantes <= 3) Peligro else ColorAjusteGris,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp)
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        // Botón Restaurar
        IconButton(
            onClick = alRestaurar,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(fondoBadgeParaTema(Menta))
        ) {
            Icon(
                imageVector = Icons.Filled.Restore,
                contentDescription = "Restaurar",
                tint = colorLegibleParaTema(Menta),
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(6.dp))

        // Botón Borrar Definitivo
        IconButton(
            onClick = alBorrarDefinitivo,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(fondoBadgeParaTema(Peligro))
        ) {
            Icon(
                imageVector = Icons.Filled.DeleteForever,
                contentDescription = "Borrar",
                tint = colorLegibleParaTema(Peligro),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

private fun diasDesde(momento: Long, ahora: Long): Long =
    TimeUnit.MILLISECONDS.toDays((ahora - momento).coerceAtLeast(0))

@Composable
fun IlustracionPapeleraVacia(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(ColorPapelera.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.DeleteSweep,
                contentDescription = null,
                tint = ColorPapelera,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "La papelera está vacía",
            style = MaterialTheme.typography.titleLarge,
            color = ColorTextoAjustes,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Los elementos eliminados se conservarán aquí durante 30 días antes de destruirse permanentemente.",
            style = MaterialTheme.typography.bodyMedium,
            color = ColorAjusteGris,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
