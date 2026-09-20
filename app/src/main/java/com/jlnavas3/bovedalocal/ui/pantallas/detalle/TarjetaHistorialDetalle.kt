package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CambioContrasena
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TarjetaHistorialDetalle(
    entrada: Entrada,
    vm: VaultViewModel,
    haptica: Haptica
) {
    val historialUnico = remember(entrada.historialContrasenas, entrada.contrasena) {
        entrada.historialContrasenas
            .distinctBy { it.contrasena }
            .filterNot { it.contrasena == entrada.contrasena }
    }
    if (historialUnico.isEmpty()) return

    val reveladas = remember { mutableStateMapOf<String, Boolean>() }
    var claveCopiadaReciente by remember { mutableStateOf<String?>(null) }
    var claveARestaurar by remember { mutableStateOf<String?>(null) }
    var claveAEliminar by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(claveCopiadaReciente) {
        if (claveCopiadaReciente != null) {
            delay(1500)
            claveCopiadaReciente = null
        }
    }

    GrupoAjustes(etiqueta = "Contraseñas anteriores (${historialUnico.size})") {
        historialUnico.forEachIndexed { index, cambio ->
            if (index > 0) SeparadorFilaSimple()
            val estaRevelada = reveladas[cambio.contrasena] == true
            val fueCopiada = claveCopiadaReciente == cambio.contrasena

            FilaHistorialContrasena(
                cambio = cambio,
                revelada = estaRevelada,
                copiado = fueCopiada,
                onToggleRevelar = {
                    haptica.toque()
                    reveladas[cambio.contrasena] = !estaRevelada
                },
                onCopiar = {
                    haptica.exito()
                    vm.copiar("Contraseña anterior", cambio.contrasena, sensible = true)
                    claveCopiadaReciente = cambio.contrasena
                },
                onEliminar = {
                    haptica.toque()
                    claveAEliminar = cambio.contrasena
                },
                onSolicitarRestaurar = {
                    haptica.toque()
                    claveARestaurar = cambio.contrasena
                }
            )
        }
    }

    if (claveARestaurar != null) {
        AlertDialog(
            onDismissRequest = { claveARestaurar = null },
            shape = RoundedCornerShape(20.dp),
            containerColor = ColorTarjetaAjustes,
            tonalElevation = 0.dp,
            title = { Text("¿Restaurar esta contraseña?", color = TextoPrincipal) },
            text = {
                Text("Esta contraseña pasará a ser la contraseña activa de \"${entrada.titulo}\". La que tienes actualmente no se perderá: se conservará en este mismo historial.", color = TextoSecundario)
            },
            confirmButton = {
                TextButton(onClick = {
                    val nuevaClave = claveARestaurar
                    claveARestaurar = null
                    if (nuevaClave != null) {
                        haptica.exito()
                        vm.guardar(entrada.copy(contrasena = nuevaClave))
                    }
                }) {
                    Text("Restaurar", color = Ambar, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { claveARestaurar = null }) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        )
    }

    if (claveAEliminar != null) {
        AlertDialog(
            onDismissRequest = { claveAEliminar = null },
            shape = RoundedCornerShape(20.dp),
            containerColor = ColorTarjetaAjustes,
            tonalElevation = 0.dp,
            title = { Text("¿Eliminar esta contraseña del historial?", color = TextoPrincipal) },
            text = {
                Text("Esta contraseña anterior se eliminará permanentemente. Esta acción no se puede deshacer.", color = TextoSecundario)
            },
            confirmButton = {
                TextButton(onClick = {
                    val clave = claveAEliminar
                    claveAEliminar = null
                    if (clave != null) {
                        haptica.error()
                        val nuevoHistorial = entrada.historialContrasenas.filterNot { it.contrasena == clave }
                        vm.guardar(entrada.copy(historialContrasenas = nuevoHistorial))
                    }
                }) {
                    Text("Eliminar", color = Peligro, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { claveAEliminar = null }) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        )
    }
}

@Composable
private fun FilaHistorialContrasena(
    cambio: CambioContrasena,
    revelada: Boolean,
    copiado: Boolean,
    onToggleRevelar: () -> Unit,
    onCopiar: () -> Unit,
    onEliminar: () -> Unit,
    onSolicitarRestaurar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = formatearFechaDetalle(cambio.cambiadaEn),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${cambio.contrasena.length} caracteres",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextoSecundario.copy(alpha = 0.8f)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onEliminar,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar contraseña anterior",
                        tint = Peligro.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = onToggleRevelar,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (revelada) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (revelada) "Ocultar" else "Ver contraseña",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(22.dp)
                    )
                }
                IconButton(
                    onClick = onCopiar,
                    modifier = Modifier.size(36.dp)
                ) {
                    AnimatedVisibility(
                        visible = copiado,
                        enter = scaleIn(spring(dampingRatio = 0.5f)),
                        exit = scaleOut(spring(dampingRatio = 0.6f))
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = "Copiado", tint = Menta, modifier = Modifier.size(20.dp))
                    }
                    AnimatedVisibility(
                        visible = !copiado,
                        enter = scaleIn(spring(dampingRatio = 0.5f)),
                        exit = scaleOut(spring(dampingRatio = 0.6f))
                    ) {
                        Icon(Icons.Filled.ContentCopy, contentDescription = "Copiar", tint = ColorIconosInternos, modifier = Modifier.size(20.dp))
                    }
                }
                IconButton(
                    onClick = onSolicitarRestaurar,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Restore,
                        contentDescription = "Restaurar como activa",
                        tint = Ambar,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        if (revelada) {
            Text(
                text = contrasenaColoreada(cambio.contrasena),
                style = EstiloMono,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = "•".repeat(cambio.contrasena.length.coerceIn(8, 24)),
                style = EstiloMonoGrande.copy(letterSpacing = 2.sp),
                color = TextoSecundario,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun formatearFechaDetalle(ms: Long): String {
    if (ms <= 0L) return "Fecha no registrada"
    val sdf = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
    return sdf.format(Date(ms))
}
