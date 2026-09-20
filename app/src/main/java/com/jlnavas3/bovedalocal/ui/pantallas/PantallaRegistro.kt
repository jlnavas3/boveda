package com.jlnavas3.bovedalocal.ui.pantallas

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltroActivo
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
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

private data class CategoriaOpcion(
    val nombre: String,
    val descripcion: String,
    val icono: ImageVector,
    val color: Color
)

private data class EventoRegistro(
    val timestamp: String,
    val area: String,
    val mensaje: String,
    val esError: Boolean,
    val textoCompleto: String
)

@Composable
fun PantallaRegistro(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    var registro by remember { mutableStateOf<List<String>>(emptyList()) }
    var refresco by remember { mutableIntStateOf(0) }
    var busquedaVisible by remember { mutableStateOf(false) }
    var filtroTexto by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    var criterioOrden by remember { mutableStateOf(CriterioOrdenRegistro.RECIENTES) }
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }
    var mostrarModalCategorias by remember { mutableStateOf(false) }
    var mostrarDialogoOrdenacion by remember { mutableStateOf(false) }

    val categoriasOpciones = remember {
        listOf(
            CategoriaOpcion("Todos", "Todos los eventos del sistema", Icons.Filled.SelectAll, ColorAcento),
            CategoriaOpcion("Bóveda", "Apertura, cifrado, cambios de clave y entradas", Icons.Filled.Lock, Menta),
            CategoriaOpcion("Papelera", "Entradas eliminadas, restauradas y vaciado", Icons.Filled.Delete, ColorPapelera),
            CategoriaOpcion("Portapapeles", "Elementos copiados y vaciado automático", Icons.Filled.ContentCopy, Ambar),
            CategoriaOpcion("2FA", "Códigos TOTP, sincronización y doble factor", Icons.Filled.Password, Color2FA),
            CategoriaOpcion("Huella", "Autenticación biométrica y Keystore de Android", Icons.Filled.Fingerprint, ColorSeguridad),
            CategoriaOpcion("Cámara", "Escaneo de QR y motores de cámara", Icons.Filled.CameraAlt, ColorAcento),
            CategoriaOpcion("Autofill", "Autocompletado, Passkeys y Credential Manager", Icons.Filled.Description, ColorPasskeys),
            CategoriaOpcion("Errores", "Fallos, excepciones y anomalías", Icons.Filled.ErrorOutline, Peligro)
        )
    }

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
                "Bóveda" -> ev.area.equals("bóveda", ignoreCase = true) ||
                    ev.area.equals("boveda", ignoreCase = true)
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
                    ev.area.contains("cámara", ignoreCase = true) ||
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

    val tieneFiltrosActivos = categoriaSeleccionada != "Todos" || criterioOrden != CriterioOrdenRegistro.RECIENTES || filtroTexto.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        // Cabecera superior moderna con acciones integradas
        BarraSuperiorPantalla(
            titulo = "Registro",
            alVolver = { vm.volverAtras() },
            colorFondo = ColorAjustesFondo,
            acciones = {
                // Botón Búsqueda (Lupa)
                IconButton(
                    onClick = {
                        haptica.tic()
                        busquedaVisible = !busquedaVisible
                        if (!busquedaVisible && filtroTexto.isNotBlank()) {
                            filtroTexto = ""
                        }
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (busquedaVisible || filtroTexto.isNotBlank()) ColorAcento.copy(alpha = 0.16f) else ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar en registro",
                        tint = if (busquedaVisible || filtroTexto.isNotBlank()) ColorAcento else ColorIconosInternos,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Botón Filtros (Categorías modal)
                IconButton(
                    onClick = {
                        haptica.tic()
                        mostrarModalCategorias = true
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (categoriaSeleccionada != "Todos") ColorAcento.copy(alpha = 0.16f) else ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = "Filtrar por categoría",
                        tint = if (categoriaSeleccionada != "Todos") ColorAcento else ColorIconosInternos,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Botón Menú 3 puntos (Opciones y Acciones)
                Box {
                    IconButton(
                        onClick = {
                            haptica.tic()
                            menuOpcionesDesplegado = true
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (tieneFiltrosActivos) ColorAcento.copy(alpha = 0.16f) else ColorTarjetaAjustes)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Más opciones",
                            tint = if (tieneFiltrosActivos) ColorAcento else ColorIconosInternos,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    MenuDesplegableBoveda(
                        expanded = menuOpcionesDesplegado,
                        onDismissRequest = { menuOpcionesDesplegado = false },
                        modifier = Modifier.widthIn(min = 220.dp)
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = null, tint = ColorAcento, modifier = Modifier.size(20.dp))
                            },
                            text = { Text("Ordenar por...", color = TextoPrincipal) },
                            onClick = {
                                menuOpcionesDesplegado = false
                                mostrarDialogoOrdenacion = true
                            }
                        )
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.ContentCopy, contentDescription = null, tint = ColorSeguridad, modifier = Modifier.size(20.dp))
                            },
                            text = { Text("Copiar registro", color = TextoPrincipal) },
                            onClick = {
                                menuOpcionesDesplegado = false
                                haptica.tic()
                                Portapapeles.copiar(contexto, "Registro Bóveda local", textoRegistroFiltrado())
                                vm.avisar("Registro copiado")
                            }
                        )
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.Share, contentDescription = null, tint = ColorAcento, modifier = Modifier.size(20.dp))
                            },
                            text = { Text("Compartir registro", color = TextoPrincipal) },
                            onClick = {
                                menuOpcionesDesplegado = false
                                haptica.tic()
                                val intent = Intent(Intent.ACTION_SEND)
                                    .setType("text/plain")
                                    .putExtra(Intent.EXTRA_SUBJECT, "Registro Bóveda local")
                                    .putExtra(Intent.EXTRA_TEXT, textoRegistroFiltrado())
                                if (!AjustesSistema.abrir(contexto, Intent.createChooser(intent, "Compartir registro"))) {
                                    vm.avisar("No hay ninguna app con la que compartirlo")
                                }
                            }
                        )
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.Delete, contentDescription = null, tint = Peligro, modifier = Modifier.size(20.dp))
                            },
                            text = { Text("Borrar registro", color = Peligro) },
                            onClick = {
                                menuOpcionesDesplegado = false
                                haptica.toque()
                                Diagnostico.borrar()
                                registro = emptyList()
                                vm.avisar("Registro borrado")
                            }
                        )
                        if (tieneFiltrosActivos) {
                            SeparadorOpcionMenu()
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(Icons.Filled.Close, contentDescription = null, tint = Peligro, modifier = Modifier.size(20.dp))
                                },
                                text = { Text("Restablecer filtros", color = Peligro) },
                                onClick = {
                                    menuOpcionesDesplegado = false
                                    haptica.tic()
                                    categoriaSeleccionada = "Todos"
                                    criterioOrden = CriterioOrdenRegistro.RECIENTES
                                    filtroTexto = ""
                                    busquedaVisible = false
                                }
                            )
                        }
                    }
                }
            }
        )

        // Barra de búsqueda animada One UI
        AnimatedVisibility(
            visible = busquedaVisible || filtroTexto.isNotBlank(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                BarraBusquedaAnimada(
                    valor = filtroTexto,
                    alCambiar = { filtroTexto = it },
                    alCerrar = {
                        busquedaVisible = false
                        filtroTexto = ""
                    }
                )
            }
        }

        // Chip indicador de categoría activa
        if (categoriaSeleccionada != "Todos") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChipFiltroActivo(
                    texto = "Categoría: $categoriaSeleccionada",
                    alLimpiar = {
                        haptica.tic()
                        categoriaSeleccionada = "Todos"
                    }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${eventosOrdenados.size} de ${registro.size} eventos",
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = criterioOrden.etiqueta,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(Modifier.height(8.dp))

            // Lista de eventos que aprovecha todo el espacio vertical
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (eventosOrdenados.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(fondoBadgeParaTema(ColorAcento)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FilterList,
                                contentDescription = null,
                                tint = colorLegibleParaTema(ColorAcento),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = if (registro.isEmpty()) "(Registro vacío todavía)" else "No hay eventos con los filtros actuales",
                            color = ColorAjusteGris,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(eventosOrdenados) { ev ->
                            val badgeColor = when {
                                ev.esError -> Peligro
                                ev.area.contains("huella", true) || ev.area.contains("keystore", true) || ev.area.contains("seguridad", true) -> ColorSeguridad
                                ev.area.contains("camara", true) || ev.area.contains("cámara", true) -> ColorAcento
                                ev.area.contains("autofill", true) || ev.area.contains("passkey", true) || ev.area.contains("credential", true) -> ColorPasskeys
                                ev.area.contains("portapapeles", true) -> Ambar
                                ev.area.contains("papelera", true) -> ColorPapelera
                                ev.area.contains("salud", true) -> ColorSalud
                                ev.area.contains("2fa", true) || ev.area.contains("totp", true) -> Color2FA
                                else -> Menta
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(ColorTarjetaAjustes)
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(fondoBadgeParaTema(badgeColor))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = ev.area.uppercase(),
                                            color = colorLegibleParaTema(badgeColor),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                    Text(
                                        text = ev.timestamp,
                                        color = ColorAjusteGris,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = ev.mensaje,
                                    color = if (ev.esError) Peligro else ColorTextoAjustes,
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
        }
    }

    // Modal de selección de categorías fijado abajo estilo One UI / MagicOS
    if (mostrarModalCategorias) {
        val esOscuro = isSystemInDarkTheme()
        val fondoModal = if (esOscuro) Color(0xFF222225) else Color.White

        Dialog(
            onDismissRequest = { mostrarModalCategorias = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { mostrarModalCategorias = false },
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(26.dp))
                        .background(fondoModal)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { /* Evita cerrar al pulsar dentro */ }
                        .padding(horizontal = 20.dp, vertical = 22.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ColorAcento),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.FilterList,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Filtrar por categoría",
                                    color = TextoPrincipal,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.5.sp
                                    )
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = "Muestra solo los eventos del área seleccionada",
                                    color = TextoSecundario,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp)
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 420.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            categoriasOpciones.forEach { opcion ->
                                val esSeleccionado = opcion.nombre == categoriaSeleccionada

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            if (esSeleccionado) opcion.color.copy(alpha = 0.12f)
                                            else Color.Transparent
                                        )
                                        .clickable {
                                            haptica.tic()
                                            categoriaSeleccionada = opcion.nombre
                                            mostrarModalCategorias = false
                                        }
                                        .padding(horizontal = 14.dp, vertical = 11.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = opcion.icono,
                                        contentDescription = null,
                                        tint = if (esSeleccionado) opcion.color else TextoSecundario,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = opcion.nombre,
                                            color = if (esSeleccionado) opcion.color else TextoPrincipal,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (esSeleccionado) FontWeight.SemiBold else FontWeight.Normal,
                                                fontSize = 15.sp
                                            )
                                        )
                                        Spacer(Modifier.height(2.dp))
                                        Text(
                                            text = opcion.descripcion,
                                            color = TextoSecundario,
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                                        )
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .border(
                                                width = if (esSeleccionado) 6.dp else 1.5.dp,
                                                color = if (esSeleccionado) opcion.color else ColorAjusteGris.copy(alpha = 0.45f),
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { mostrarModalCategorias = false }
                            ) {
                                Text(
                                    text = "Cerrar",
                                    color = TextoSecundario,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de ordenación modal estilo One UI
    if (mostrarDialogoOrdenacion) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoOrdenacion = false },
            containerColor = ColorTarjetaAjustes,
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 0.dp,
            title = {
                Text(
                    "Ordenar por",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = ColorTitulos
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CriterioOrdenRegistro.entries.forEach { criterio ->
                        val seleccionado = criterio == criterioOrden
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (seleccionado) ColorAcento.copy(alpha = 0.12f) else Color.Transparent)
                            .clickable {
                                haptica.tic()
                                criterioOrden = criterio
                                mostrarDialogoOrdenacion = false
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = null,
                                tint = if (seleccionado) ColorAcento else ColorIconosInternos,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = criterio.etiqueta,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
                                ),
                                color = if (seleccionado) ColorAcento else TextoPrincipal,
                                modifier = Modifier.weight(1f)
                            )
                            if (seleccionado) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = ColorAcento,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoOrdenacion = false }) {
                    Text("Cerrar", color = ColorAcento)
                }
            }
        )
    }
}