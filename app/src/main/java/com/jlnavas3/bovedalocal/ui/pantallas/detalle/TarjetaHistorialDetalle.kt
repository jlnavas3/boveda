package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.material.icons.filled.History
import com.jlnavas3.bovedalocal.data.CambioContrasena
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
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

    LaunchedEffect(claveCopiadaReciente) {
        if (claveCopiadaReciente != null) {
            delay(1500)
            claveCopiadaReciente = null
        }
    }

    TarjetaBovedaDesplegable(
        titulo = "Contraseñas anteriores",
        descripcion = "${historialUnico.size} clave${if (historialUnico.size == 1) "" else "s"} previas",
        icono = Icons.Filled.History,
        colorIcono = ColorExportacion,
        inicialmenteAbierta = false
    ) {
        Text(
            "Historial cifrado de claves previas. Puedes visualizarlas, copiarlas o restaurarlas si cambiaste de clave por error.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(12.dp))

        historialUnico.forEachIndexed { index, cambio ->
            if (index > 0) {
                Spacer(Modifier.height(12.dp))
            }
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
                Text("Esta contraseña pasará a ser la contraseña activa de \"${entrada.titulo}\". La que tienes actualmente no se perderá: se conservará en este mismo historial.")
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
    copiado: Boolean,
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
        // Cabecera de la clave anterior: Fecha + Iconos de Ver y Copiar claramente visibles
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
                // Botón Revelar / Ocultar
                IconButton(
                    onClick = onToggleRevelar,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = if (revelada) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (revelada) "Ocultar" else "Visualizar contraseña",
                        tint = if (revelada) Ambar else ColorIconosInternos,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.width(4.dp))
                // Botón Copiar con animación
                IconButton(
                    onClick = onCopiar,
                    modifier = Modifier.size(40.dp)
                ) {
                    AnimatedVisibility(
                        visible = copiado,
                        enter = scaleIn(spring(dampingRatio = 0.5f)),
                        exit = scaleOut(spring(dampingRatio = 0.6f))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Copiada",
                            tint = Menta,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    AnimatedVisibility(
                        visible = !copiado,
                        enter = scaleIn(spring(dampingRatio = 0.5f)),
                        exit = scaleOut(spring(dampingRatio = 0.6f))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = "Copiar contraseña",
                            tint = ColorIconosInternos,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Contenedor visual de la contraseña (pulsable para revelar/ocultar)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(FormaPequena)
                .background(ColorTarjetas)
                .clickable { onToggleRevelar() }
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
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
        }

        Spacer(Modifier.height(10.dp))

        // Botón Restaurar como activa bien demarcado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(FormaBoton)
                .border(
                    width = if (GrosorBorde > 0.dp) GrosorBorde else 1.dp,
                    color = Ambar.copy(alpha = 0.7f),
                    shape = FormaBoton
                )
                .clickable { onSolicitarRestaurar() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Restore,
                    contentDescription = null,
                    tint = Ambar,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Restaurar como contraseña activa",
                    color = Ambar,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

private fun formatearFechaDetalle(momento: Long): String {
    if (momento <= 0L) return "Fecha desconocida"
    val formato = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.forLanguageTag("es"))
    return formato.format(Date(momento))
}
