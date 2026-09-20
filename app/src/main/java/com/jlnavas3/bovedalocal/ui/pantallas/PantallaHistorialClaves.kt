package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.data.RegistroClaveGenerada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBoveda
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaHistorialClaves(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    var confirmarVaciar by remember { mutableStateOf(false) }
    var ahora by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        vm.recargarAjustes()
        while (true) {
            ahora = System.currentTimeMillis()
            delay(1000)
        }
    }

    val clavesVigentes = remember(ajustes.historialClaves, ahora, ajustes.historialClavesVaciadoAuto, ajustes.historialClavesTiempoAutoDestruccion) {
        if (ajustes.historialClavesVaciadoAuto && ajustes.historialClavesTiempoAutoDestruccion > 0) {
            ajustes.historialClaves.filter { ahora - it.generadaEn < ajustes.historialClavesTiempoAutoDestruccion }
        } else {
            ajustes.historialClaves
        }
    }

    val formatoFecha = remember { SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.getDefault()) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            BarraSuperiorPantalla(
                titulo = "Historial de claves",
                alVolver = { vm.volverAtras() },
                acciones = {
                    if (clavesVigentes.isNotEmpty()) {
                        IconButton(onClick = {
                            haptica.toque()
                            confirmarVaciar = true
                        }) {
                            Icon(
                                imageVector = Icons.Filled.DeleteSweep,
                                contentDescription = "Vaciar historial",
                                tint = Peligro,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            )
            DescripcionPantalla(
                subtitulo = if (clavesVigentes.isEmpty()) "Sin contraseñas recientes" else "${clavesVigentes.size} contraseña${if (clavesVigentes.size == 1) "" else "s"} generada${if (clavesVigentes.size == 1) "" else "s"}"
            )

            Spacer(Modifier.height(8.dp))

            // Cuadro compacto de estado de autodestrucción con ícono respetando tema claro/oscuro
            val autodestruccionActiva = ajustes.historialClavesVaciadoAuto
            val colorEstado = if (autodestruccionActiva) Menta else Ambar
            val iconoEstado = if (autodestruccionActiva) Icons.Filled.Timer else Icons.Filled.WarningAmber
            val textoEstado = if (autodestruccionActiva) {
                val tiempoTexto = AlmacenAjustes.OPCIONES_AUTODESTRUCCION_HISTORIAL
                    .find { it.first == ajustes.historialClavesTiempoAutoDestruccion }?.second ?: "30 minutos"
                "Autodestrucción activa ($tiempoTexto)"
            } else {
                "Autodestrucción desactivada"
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FormaCampo)
                    .background(colorEstado.copy(alpha = if (esOscuroActivo) 0.12f else 0.10f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = iconoEstado,
                        contentDescription = null,
                        tint = colorLegibleParaTema(colorEstado),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = textoEstado,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 11.5.sp),
                        color = colorLegibleParaTema(colorEstado)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            if (clavesVigentes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(FormaPequena)
                                .background(fondoBadgeParaTema(ColorGenerador)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.History,
                                contentDescription = null,
                                tint = colorLegibleParaTema(ColorGenerador),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(Modifier.height(14.dp))
                        Text(
                            text = "Historial vacío",
                            style = MaterialTheme.typography.titleMedium,
                            color = ColorTitulos
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Las contraseñas que generes con el Tile rápido, el Widget 1x1 o desde la app se registrarán aquí temporalmente para que no las pierdas.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoSecundario,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(clavesVigentes, key = { it.id }) { item ->
                        FilaClaveHistorial(
                            item = item,
                            formatoFecha = formatoFecha,
                            ahora = ahora,
                            vaciadoAuto = ajustes.historialClavesVaciadoAuto,
                            tiempoDestruccion = ajustes.historialClavesTiempoAutoDestruccion,
                            alCopiar = { vm.copiar("Contraseña", item.clave, true) },
                            alEliminar = { vm.eliminarDeHistorialClaves(item.id) }
                        )
                    }
                }
            }
        }
    }

    if (confirmarVaciar) {
        val colorDialogo = if (esOscuroActivo) Color(0xFF212023) else Color(0xFFFFFFFF)
        AlertDialog(
            onDismissRequest = { confirmarVaciar = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = colorDialogo,
            tonalElevation = 0.dp,
            title = { Text("¿Vaciar historial de contraseñas?", color = ColorTitulos) },
            text = {
                Text(
                    "Se borrarán permanentemente todas las contraseñas generadas registradas en el historial.",
                    color = TextoPrincipal
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    confirmarVaciar = false
                    vm.vaciarHistorialClaves()
                }) {
                    Text("Vaciar todo", color = Peligro, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmarVaciar = false }) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        )
    }
}

@Composable
private fun FilaClaveHistorial(
    item: RegistroClaveGenerada,
    formatoFecha: SimpleDateFormat,
    ahora: Long,
    vaciadoAuto: Boolean,
    tiempoDestruccion: Long,
    alCopiar: () -> Unit,
    alEliminar: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    TarjetaBoveda {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Fila superior: Origen y Longitud a la izquierda; Fecha e Íconos a la derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Badges a la izquierda: Origen y Longitud ("30 car.")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(FormaPequena)
                            .background(fondoBadgeParaTema(ColorGenerador))
                            .padding(horizontal = 7.dp, vertical = 2.5.dp)
                    ) {
                        Text(
                            text = item.origen,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold),
                            color = colorLegibleParaTema(ColorGenerador),
                            maxLines = 1
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(FormaPequena)
                            .background(SuperficieAlta)
                            .padding(horizontal = 7.dp, vertical = 2.5.dp)
                    ) {
                        Text(
                            text = "${item.clave.length} car.",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Medium),
                            color = TextoSecundario,
                            maxLines = 1
                        )
                    }
                }

                Spacer(Modifier.width(8.dp))

                // Esquina derecha superior: Fecha de creación y justo debajo los íconos (ver, copiar, eliminar)
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatoFecha.format(Date(item.generadaEn)),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextoSecundario,
                        maxLines = 1
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { visible = !visible },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Alternar visibilidad",
                                tint = ColorIconosInternos,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                        IconButton(
                            onClick = alCopiar,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ContentCopy,
                                contentDescription = "Copiar contraseña",
                                tint = ColorIconosInternos,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                        IconButton(
                            onClick = alEliminar,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Eliminar del historial",
                                tint = Peligro.copy(alpha = 0.85f),
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // La contraseña se muestra en una sola línea ajustando tamaño
            val tamanoTexto = when {
                item.clave.length > 32 -> 11.sp
                item.clave.length > 24 -> 12.sp
                item.clave.length > 18 -> 13.sp
                else -> 14.sp
            }
            Text(
                text = if (visible) item.clave else "•".repeat(item.clave.length.coerceIn(8, 26)),
                style = EstiloMono.copy(fontWeight = FontWeight.SemiBold, fontSize = tamanoTexto),
                color = ColorTitulos,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            // Indicador de expiración en la parte inferior
            if (vaciadoAuto && tiempoDestruccion > 0) {
                val restanteMs = (item.generadaEn + tiempoDestruccion) - ahora
                val restanteMin = (restanteMs / 60000L).coerceAtLeast(0)
                val restanteSeg = ((restanteMs % 60000L) / 1000L).coerceAtLeast(0)
                Spacer(Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = null,
                        tint = TextoSecundario.copy(alpha = 0.7f),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = if (restanteMin > 60) "Expira en ${restanteMin / 60}h ${restanteMin % 60}m" else "Expira en ${restanteMin}m ${restanteSeg}s",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = TextoSecundario
                    )
                }
            }
        }
    }
}
