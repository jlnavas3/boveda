package com.jlnavas3.bovedalocal.ui.pantallas

import android.content.Intent
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegablePepo
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.Portapapeles

enum class CriterioOrdenRegistro(val etiqueta: String) {
    RECIENTES("Más recientes"),
    ANTIGUOS("Más antiguos"),
    AREA_AZ("Área (A - Z)"),
    AREA_ZA("Área (Z - A)")
}

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
    val haptica = remember { Haptica(contexto) }
    var registro by remember { mutableStateOf<List<String>>(emptyList()) }
    var refresco by remember { mutableIntStateOf(0) }
    var filtroTexto by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    var criterioOrden by remember { mutableStateOf(CriterioOrdenRegistro.RECIENTES) }

    val categorias = listOf("Todos", "Bóveda", "Papelera", "Portapapeles", "2FA", "Huella", "Cámara", "Autofill", "Errores")

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
                "Bóveda" -> ev.area.contains("bóveda", ignoreCase = true) ||
                    ev.area.contains("boveda", ignoreCase = true) ||
                    ev.area.contains("salud", ignoreCase = true) ||
                    ev.area.contains("seguridad", ignoreCase = true) ||
                    ev.mensaje.contains("bóveda", ignoreCase = true) ||
                    ev.mensaje.contains("entrada", ignoreCase = true) ||
                    ev.mensaje.contains("cifrad", ignoreCase = true) ||
                    ev.mensaje.contains("csv", ignoreCase = true)
                "Papelera" -> ev.area.contains("papelera", ignoreCase = true) ||
                    ev.mensaje.contains("papelera", ignoreCase = true)
                "Portapapeles" -> ev.area.contains("portapapeles", ignoreCase = true) ||
                    ev.mensaje.contains("copiad", ignoreCase = true) ||
                    ev.mensaje.contains("portapapeles", ignoreCase = true)
                "2FA" -> ev.area.contains("2fa", ignoreCase = true) ||
                    ev.area.contains("totp", ignoreCase = true) ||
                    ev.mensaje.contains("2fa", ignoreCase = true) ||
                    ev.mensaje.contains("totp", ignoreCase = true) ||
                    ev.mensaje.contains("doble factor", ignoreCase = true)
                "Huella" -> ev.area.contains("huella", ignoreCase = true) ||
                    ev.area.contains("keystore", ignoreCase = true) ||
                    ev.mensaje.contains("biometr", ignoreCase = true)
                "Cámara" -> ev.area.contains("camara", ignoreCase = true) ||
                    ev.area.contains("qr", ignoreCase = true) ||
                    ev.mensaje.contains("motor", ignoreCase = true)
                "Autofill" -> ev.area.contains("autofill", ignoreCase = true) ||
                    ev.area.contains("credential", ignoreCase = true) ||
                    ev.area.contains("passkey", ignoreCase = true) ||
                    ev.mensaje.contains("relleno", ignoreCase = true) ||
                    ev.mensaje.contains("passkey", ignoreCase = true)
                "Errores" -> ev.esError
                else -> true
            }
            val coincideTexto = if (filtroTexto.isBlank()) true else {
                ev.textoCompleto.contains(filtroTexto, ignoreCase = true)
            }
            coincideCategoria && coincideTexto
        }
    }

    val eventosOrdenados = remember(eventosFiltrados, criterioOrden) {
        when (criterioOrden) {
            CriterioOrdenRegistro.RECIENTES -> eventosFiltrados.reversed()
            CriterioOrdenRegistro.ANTIGUOS -> eventosFiltrados
            CriterioOrdenRegistro.AREA_AZ -> eventosFiltrados.sortedWith(
                compareBy({ it.area.lowercase() }, { it.timestamp })
            )
            CriterioOrdenRegistro.AREA_ZA -> eventosFiltrados.sortedWith(
                compareByDescending<EventoRegistro> { it.area.lowercase() }.thenByDescending { it.timestamp }
            )
        }
    }

    fun textoRegistroFiltrado(): String = eventosOrdenados.joinToString("\n") { it.textoCompleto }

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
            etiqueta = "Buscar en eventos (${eventosOrdenados.size} de ${registro.size})…",
            alCambiar = { filtroTexto = it }
        )

        Spacer(Modifier.height(10.dp))

        // Fila de Filtro de Categoría y Ordenación
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SelectorCategoriaRegistro(
                    categoriaSeleccionada = categoriaSeleccionada,
                    categorias = categorias,
                    alSeleccionar = {
                        haptica.tic()
                        categoriaSeleccionada = it
                    }
                )
            }
            SelectorOrdenRegistro(
                criterio = criterioOrden,
                alCambiar = {
                    haptica.tic()
                    criterioOrden = it
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        // Lista de Eventos
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (eventosOrdenados.isEmpty()) {
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
                    items(eventosOrdenados) { ev ->
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
                                        ev.area.contains("autofill", true) || ev.area.contains("passkey", true) || ev.area.contains("credential", true) -> ColorPasskeys
                                        ev.area.contains("portapapeles", true) -> Ambar
                                        ev.area.contains("papelera", true) -> ColorPapelera
                                        ev.area.contains("salud", true) -> ColorSalud
                                        ev.area.contains("2fa", true) || ev.area.contains("totp", true) -> Color2FA
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

@Composable
private fun SelectorCategoriaRegistro(
    categoriaSeleccionada: String,
    categorias: List<String>,
    alSeleccionar: (String) -> Unit
) {
    var desplegado by remember { mutableStateOf(false) }
    val esTodos = categoriaSeleccionada == "Todos"
    val colorActivo = if (esTodos) Ambar else colorCategoriaRegistro(categoriaSeleccionada)
    val forma = FormaCampo

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .clip(forma)
                .background(Superficie)
                .then(
                    if (GrosorBorde > 0.dp) {
                        Modifier.border(
                            GrosorBorde,
                            if (desplegado || !esTodos) colorActivo else ColorBordeActual,
                            forma
                        )
                    } else {
                        Modifier
                    }
                )
                .clickable { desplegado = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                iconoCategoriaRegistro(categoriaSeleccionada),
                contentDescription = null,
                tint = if (desplegado || !esTodos) colorActivo else TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (esTodos) "Categorías" else categoriaSeleccionada,
                color = if (!esTodos) colorActivo else TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Icon(
                if (desplegado) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Abrir categorías",
                tint = TextoSecundario
            )
        }

        MenuDesplegablePepo(
            expanded = desplegado,
            onDismissRequest = { desplegado = false }
        ) {
            categorias.forEachIndexed { index, cat ->
                if (index > 0) {
                    SeparadorOpcionMenu()
                }
                val activo = cat == categoriaSeleccionada
                val colorCat = if (cat == "Todos") ColorAcento else colorCategoriaRegistro(cat)
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            iconoCategoriaRegistro(cat),
                            contentDescription = null,
                            tint = if (activo) colorCat else TextoSecundario,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = if (activo) {
                        {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                tint = colorCat,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null,
                    text = {
                        Text(
                            cat,
                            color = if (activo) colorCat else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        alSeleccionar(cat)
                        desplegado = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SelectorOrdenRegistro(
    criterio: CriterioOrdenRegistro,
    alCambiar: (CriterioOrdenRegistro) -> Unit
) {
    var desplegado by remember { mutableStateOf(false) }
    val forma = FormaCampo

    Box {
        Row(
            modifier = Modifier
                .height(46.dp)
                .clip(forma)
                .background(Superficie)
                .then(
                    if (GrosorBorde > 0.dp) {
                        Modifier.border(GrosorBorde, if (desplegado) ColorAcento else ColorBordeActual, forma)
                    } else {
                        Modifier
                    }
                )
                .clickable { desplegado = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Ordenar eventos",
                tint = if (desplegado) ColorAcento else TextoSecundario,
                modifier = Modifier.size(20.dp)
            )
        }

        MenuDesplegablePepo(
            expanded = desplegado,
            onDismissRequest = { desplegado = false }
        ) {
            CriterioOrdenRegistro.entries.forEachIndexed { index, op ->
                if (index > 0) {
                    SeparadorOpcionMenu()
                }
                val activo = op == criterio
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.Sort,
                            contentDescription = null,
                            tint = if (activo) ColorAcento else TextoSecundario
                        )
                    },
                    trailingIcon = if (activo) {
                        {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                tint = ColorAcento,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null,
                    text = {
                        Text(
                            op.etiqueta,
                            color = if (activo) ColorTitulos else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        alCambiar(op)
                        desplegado = false
                    }
                )
            }
        }
    }
}

private fun iconoCategoriaRegistro(cat: String): ImageVector = when (cat) {
    "Todos" -> Icons.Filled.Tune
    "Bóveda" -> Icons.Filled.Lock
    "Papelera" -> Icons.Filled.Delete
    "Portapapeles" -> Icons.Filled.ContentCopy
    "2FA" -> Icons.Filled.Timer
    "Huella" -> Icons.Filled.Fingerprint
    "Cámara" -> Icons.Filled.PhotoCamera
    "Autofill" -> Icons.Filled.Key
    "Errores" -> Icons.Filled.Warning
    else -> Icons.Filled.FilterList
}

private fun colorCategoriaRegistro(cat: String): Color = when (cat) {
    "Errores" -> Peligro
    "Huella" -> ColorSeguridad
    "Cámara" -> ColorAcento
    "Bóveda" -> Menta
    "Autofill" -> ColorPasskeys
    "Papelera" -> ColorPapelera
    "2FA" -> Color2FA
    "Portapapeles" -> Ambar
    else -> Ambar
}