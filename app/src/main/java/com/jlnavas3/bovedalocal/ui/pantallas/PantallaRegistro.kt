package com.jlnavas3.bovedalocal.ui.pantallas

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.CampoPepo
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Portapapeles

private data class EventoRegistro(
    val timestamp: String,
    val area: String,
    val mensaje: String,
    val esError: Boolean,
    val textoCompleto: String
)

@Composable
fun PantallaRegistro(vm: VaultViewModel) {
    val contexto = LocalContext.current
    var registro by remember { mutableStateOf<List<String>>(emptyList()) }
    var refresco by remember { mutableIntStateOf(0) }
    var filtroTexto by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    val categorias = listOf("Todos", "Bóveda", "Huella", "Cámara", "Autofill", "Errores")

    LifecycleResumeEffect(Unit) {
        refresco++
        onPauseOrDispose { }
    }
    LaunchedEffect(refresco) {
        registro = Diagnostico.ultimas(Diagnostico.MAX_LINEAS_MEMORIA)
    }

    val eventosParseados = remember(registro) {
        registro.map { linea ->
            val timestamp = if (linea.length >= 14) linea.take(14) else ""
            val resto = if (linea.length > 15) linea.drop(15) else linea
            val area = if (resto.contains(':')) resto.substringBefore(':').trim() else "app"
            val mensaje = if (resto.contains(':')) resto.substringAfter(':').trim() else resto
            val esError = linea.contains("error", ignoreCase = true) ||
                linea.contains("fallo", ignoreCase = true) ||
                linea.contains("exception", ignoreCase = true) ||
                linea.contains("[NO]", ignoreCase = true)
            EventoRegistro(
                timestamp = timestamp,
                area = area,
                mensaje = mensaje,
                esError = esError,
                textoCompleto = linea
            )
        }
    }

    val eventosFiltrados = remember(eventosParseados, filtroTexto, categoriaSeleccionada) {
        eventosParseados.filter { ev ->
            val coincideCategoria = when (categoriaSeleccionada) {
                "Todos" -> true
                "Bóveda" -> ev.area.contains("boveda", ignoreCase = true) || ev.mensaje.contains("bóveda", ignoreCase = true) || ev.mensaje.contains("cifrado", ignoreCase = true)
                "Huella" -> ev.area.contains("huella", ignoreCase = true) || ev.area.contains("keystore", ignoreCase = true) || ev.mensaje.contains("biometr", ignoreCase = true)
                "Cámara" -> ev.area.contains("camara", ignoreCase = true) || ev.area.contains("qr", ignoreCase = true) || ev.mensaje.contains("motor", ignoreCase = true)
                "Autofill" -> ev.area.contains("autofill", ignoreCase = true) || ev.mensaje.contains("relleno", ignoreCase = true)
                "Errores" -> ev.esError
                else -> true
            }
            val coincideTexto = if (filtroTexto.isBlank()) true else {
                ev.textoCompleto.contains(filtroTexto, ignoreCase = true)
            }
            coincideCategoria && coincideTexto
        }
    }

    fun textoRegistroFiltrado(): String = eventosFiltrados.joinToString("\n") { it.textoCompleto }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Obsidiana)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        CabeceraPantalla(
            titulo = "Registro de Eventos",
            subtitulo = "Historial técnico local auditado (sin datos privados)",
            alVolver = { vm.volverAtras() }
        )

        // Buscador
        CampoPepo(
            valor = filtroTexto,
            etiqueta = "Buscar en eventos (${eventosFiltrados.size} de ${registro.size})…",
            alCambiar = { filtroTexto = it }
        )

        Spacer(Modifier.height(10.dp))

        // Fila de Chips de Categorías
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categorias.forEach { cat ->
                val activa = cat == categoriaSeleccionada
                val colorChip = when (cat) {
                    "Errores" -> Peligro
                    "Huella" -> ColorSeguridad
                    "Cámara" -> ColorAcento
                    "Bóveda" -> Menta
                    "Autofill" -> ColorPasskeys
                    else -> ColorTitulos
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(CurvaturaEsquinas))
                        .background(if (activa) colorChip.copy(alpha = 0.22f) else ColorTarjetas)
                        .clickable { categoriaSeleccionada = cat }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = cat,
                        color = if (activa) colorChip else TextoSecundario,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (activa) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Lista de Eventos
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (eventosFiltrados.isEmpty()) {
                ContenedorTarjeta(
                    modifier = Modifier.align(Alignment.Center),
                    paddingInterno = 24.dp
                ) {
                    Text(
                        text = if (registro.isEmpty()) "(Registro vacío todavía)" else "No hay eventos con los filtros actuales",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(eventosFiltrados) { ev ->
                        ContenedorTarjeta(paddingInterno = 12.dp) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val badgeColor = when {
                                        ev.esError -> Peligro
                                        ev.area.contains("huella", true) || ev.area.contains("keystore", true) -> ColorSeguridad
                                        ev.area.contains("camara", true) -> ColorAcento
                                        ev.area.contains("autofill", true) -> ColorPasskeys
                                        else -> Menta
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(badgeColor.copy(alpha = 0.18f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = ev.area.uppercase(),
                                            color = badgeColor,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                                Text(
                                    text = ev.timestamp,
                                    color = TextoSecundario,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                            Text(
                                text = ev.mensaje,
                                color = if (ev.esError) Peligro else TextoPrincipal,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // Botones de acción inferiores con colores semánticos normalizados
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BotonColorido(
                texto = "Copiar",
                color = ColorSeguridad,
                icono = Icons.Filled.ContentCopy,
                modifier = Modifier.weight(1f)
            ) {
                Portapapeles.copiar(contexto, "Registro Bóveda local", textoRegistroFiltrado())
                vm.avisar("Registro copiado")
            }

            BotonColorido(
                texto = "Compartir",
                color = ColorAcento,
                icono = Icons.Filled.Share,
                modifier = Modifier.weight(1f)
            ) {
                val intent = Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_SUBJECT, "Registro Bóveda local")
                    .putExtra(Intent.EXTRA_TEXT, textoRegistroFiltrado())
                if (!AjustesSistema.abrir(contexto, Intent.createChooser(intent, "Compartir registro"))) {
                    vm.avisar("No hay ninguna app con la que compartirlo")
                }
            }

            BotonColorido(
                texto = "Borrar",
                color = ColorPapelera,
                icono = Icons.Filled.Delete,
                modifier = Modifier.weight(1f)
            ) {
                Diagnostico.borrar()
                registro = emptyList()
                vm.avisar("Registro borrado")
            }
        }
    }
}