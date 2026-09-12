package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CambioContrasena
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepo
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TarjetaHistorialDetalle(
    entrada: Entrada,
    vm: VaultViewModel,
    haptica: Haptica
) {
    if (entrada.historialContrasenas.isEmpty()) return

    val reveladas = remember { mutableStateMapOf<Int, Boolean>() }
    var claveARestaurar by remember { mutableStateOf<String?>(null) }

    TarjetaPepo {
        EtiquetaSeccion("Contraseñas anteriores")
        Spacer(Modifier.height(6.dp))
        Text(
            "Historial cifrado de claves previas. Puedes visualizarlas, copiarlas o restaurarlas si cambiaste de clave por error.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(12.dp))

        entrada.historialContrasenas.forEachIndexed { index, cambio ->
            if (index > 0) {
                Spacer(Modifier.height(10.dp))
            }
            val estaRevelada = reveladas[index] == true
            FilaHistorialContrasena(
                cambio = cambio,
                revelada = estaRevelada,
                onToggleRevelar = {
                    haptica.toque()
                    reveladas[index] = !estaRevelada
                },
                onCopiar = {
                    haptica.exito()
                    vm.copiar("Contraseña anterior", cambio.contrasena, sensible = true)
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
            title = { Text("¿Restaurar esta contraseña?") },
            text = {
                Text("Esta contraseña pasará a ser la contraseña activa de \"${entrada.titulo}\". La que tienes actualmente no se perderá: se guardará en este mismo historial.")
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
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun FilaHistorialContrasena(
    cambio: CambioContrasena,
    revelada: Boolean,
    onToggleRevelar: () -> Unit,
    onCopiar: () -> Unit,
    onSolicitarRestaurar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FormaTarjeta)
            .background(SuperficieAlta)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = formatearFechaDetalle(cambio.cambiadaEn),
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${cambio.contrasena.length} caracteres",
                style = MaterialTheme.typography.labelSmall,
                color = TextoSecundario
            )
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
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonBorde(
                texto = "Restaurar",
                icono = Icons.Filled.Restore,
                color = Ambar,
                alPulsar = onSolicitarRestaurar
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggleRevelar) {
                    Icon(
                        imageVector = if (revelada) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (revelada) "Ocultar" else "Mostrar",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.width(4.dp))
                IconButton(onClick = onCopiar) {
                    Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = "Copiar contraseña",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

private fun formatearFechaDetalle(momento: Long): String {
    if (momento <= 0L) return "Fecha desconocida"
    val formato = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.forLanguageTag("es"))
    return formato.format(Date(momento))
}
