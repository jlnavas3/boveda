package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.IndicadorTotpTarta
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.pantallas.lista.BarraBusquedaAnimada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.ChipFiltroActivo
import com.jlnavas3.bovedalocal.ui.pantallas.lista.DialogoOrdenacionLista
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

@Composable
fun PantallaAutenticador(vm: VaultViewModel, estado: EstadoBoveda) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val conTotp = vm.entradasConTotp(entradas)

    var ahora by remember { mutableLongStateOf(System.currentTimeMillis() / 1000) }
    LaunchedEffect(Unit) {
        while (true) {
            ahora = System.currentTimeMillis() / 1000
            delay(500)
        }
    }

    val scrollState = rememberScrollState()

    var busquedaVisible by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf("") }
    var soloFavoritos by remember { mutableStateOf(false) }
    var criterioOrdenacion by remember { mutableStateOf(CriterioOrdenacion.NOMBRE_AZ) }
    var menuOpcionesDesplegado by remember { mutableStateOf(false) }
    var mostrarDialogoOrdenacion by remember { mutableStateOf(false) }
    var dialogoComoFunciona by remember { mutableStateOf(false) }

    val totpFiltrados = remember(conTotp, textoBusqueda, soloFavoritos, criterioOrdenacion) {
        val q = textoBusqueda.trim().lowercase()
        conTotp
            .filter { entrada ->
                val coincideTexto = if (q.isBlank()) true else {
                    entrada.titulo.lowercase().contains(q) ||
                    entrada.totpEmisor.lowercase().contains(q) ||
                    entrada.usuario.lowercase().contains(q)
                }
                val coincideFavorito = if (soloFavoritos) entrada.favorito else true
                coincideTexto && coincideFavorito
            }
            .sortedWith { a, b ->
                val nombreA = a.totpEmisor.ifBlank { a.titulo }
                val nombreB = b.totpEmisor.ifBlank { b.titulo }
                when (criterioOrdenacion) {
                    CriterioOrdenacion.NOMBRE_AZ -> nombreA.compareTo(nombreB, ignoreCase = true)
                    CriterioOrdenacion.NOMBRE_ZA -> nombreB.compareTo(nombreA, ignoreCase = true)
                    CriterioOrdenacion.MODIFICACION_RECIENTE -> b.modificadaEn.compareTo(a.modificadaEn)
                    CriterioOrdenacion.ANTIGUEDAD -> a.creadaEn.compareTo(b.creadaEn)
                    CriterioOrdenacion.CREACION_RECIENTE -> b.creadaEn.compareTo(a.creadaEn)
                }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        // Cabecera con título, buscador, escanear QR, agregar manual y menú de 3 puntos
        BarraSuperiorPantalla(
            titulo = "Autenticador 2FA",
            alVolver = { vm.volverAtras() },
            conSeparador = scrollState.value > 0,
            colorFondo = ColorAjustesFondo,
            acciones = {
                // Botón Búsqueda (Lupa)
                IconButton(
                    onClick = {
                        haptica.tic()
                        busquedaVisible = !busquedaVisible
                        if (!busquedaVisible) textoBusqueda = ""
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (busquedaVisible || textoBusqueda.isNotBlank()) Color2FA.copy(alpha = 0.16f) else ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar cuentas 2FA",
                        tint = if (busquedaVisible || textoBusqueda.isNotBlank()) Color2FA else ColorIconosInternos,
                        modifier = Modifier.size(19.dp)
                    )
                }

                Spacer(Modifier.width(5.dp))

                // Botón Escanear QR
                IconButton(
                    onClick = {
                        haptica.toque()
                        vm.ir(Pantalla.Escaner())
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.QrCodeScanner,
                        contentDescription = "Escanear código QR",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(5.dp))

                // Botón Añadir clave manual (+)
                IconButton(
                    onClick = {
                        haptica.toque()
                        vm.ir(Pantalla.Escaner(soloManual = true))
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Escribir clave manualmente",
                        tint = Color2FA,
                        modifier = Modifier.size(21.dp)
                    )
                }

                Spacer(Modifier.width(5.dp))

                // Botón Tres Puntos (Filtros, ordenación y cómo funciona)
                Box {
                    val tieneFiltrosActivos = soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ
                    IconButton(
                        onClick = {
                            haptica.tic()
                            menuOpcionesDesplegado = true
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (tieneFiltrosActivos) Color2FA.copy(alpha = 0.16f) else ColorTarjetaAjustes)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Más opciones",
                            tint = if (tieneFiltrosActivos) Color2FA else ColorIconosInternos,
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
                                Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = null, tint = Color2FA, modifier = Modifier.size(20.dp))
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
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = if (soloFavoritos) Ambar else ColorIconosInternos,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            text = {
                                Text(
                                    if (soloFavoritos) "Ver todas las cuentas" else "Solo favoritos",
                                    color = if (soloFavoritos) Ambar else TextoPrincipal
                                )
                            },
                            onClick = {
                                menuOpcionesDesplegado = false
                                haptica.tic()
                                soloFavoritos = !soloFavoritos
                            }
                        )
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.FileDownload, contentDescription = null, tint = Color2FA, modifier = Modifier.size(20.dp))
                            },
                            text = { Text("Exportación selectiva", color = TextoPrincipal) },
                            onClick = {
                                menuOpcionesDesplegado = false
                                haptica.tic()
                                vm.ir(Pantalla.ExportarSelectivo("2fa"))
                            }
                        )
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.FileUpload, contentDescription = null, tint = ColorExportacion, modifier = Modifier.size(20.dp))
                            },
                            text = { Text("Importar de Google Authenticator", color = TextoPrincipal) },
                            onClick = {
                                menuOpcionesDesplegado = false
                                haptica.tic()
                                vm.ir(Pantalla.Escaner())
                            }
                        )
                        SeparadorOpcionMenu()
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = null, tint = Color2FA, modifier = Modifier.size(20.dp))
                            },
                            text = { Text("¿Cómo funciona el 2FA?", color = TextoPrincipal) },
                            onClick = {
                                menuOpcionesDesplegado = false
                                haptica.tic()
                                dialogoComoFunciona = true
                            }
                        )
                        if (soloFavoritos || criterioOrdenacion != CriterioOrdenacion.NOMBRE_AZ || textoBusqueda.isNotBlank()) {
                            SeparadorOpcionMenu()
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(Icons.Filled.Close, contentDescription = null, tint = Peligro, modifier = Modifier.size(20.dp))
                                },
                                text = { Text("Restablecer filtros", color = Peligro) },
                                onClick = {
                                    menuOpcionesDesplegado = false
                                    haptica.tic()
                                    soloFavoritos = false
                                    criterioOrdenacion = CriterioOrdenacion.NOMBRE_AZ
                                    textoBusqueda = ""
                                    busquedaVisible = false
                                }
                            )
                        }
                    }
                }
            }
        )

        // Barra de búsqueda animada
        AnimatedVisibility(
            visible = busquedaVisible || textoBusqueda.isNotBlank(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                BarraBusquedaAnimada(
                    valor = textoBusqueda,
                    alCambiar = { textoBusqueda = it },
                    alCerrar = {
                        busquedaVisible = false
                        textoBusqueda = ""
                    }
                )
            }
        }

        // Chip de filtro activo
        if (soloFavoritos) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChipFiltroActivo(
                    texto = "★ Favoritos",
                    alLimpiar = { soloFavoritos = false }
                )
            }
        }

        // Contenido scrolleable
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            DescripcionPantalla(subtitulo = "Códigos de doble factor calculados en el dispositivo")
            Spacer(Modifier.height(12.dp))

            // Grupo: Códigos activos
            val etiquetaGrupo = if (totpFiltrados.size == conTotp.size) {
                "Códigos activos (${totpFiltrados.size})"
            } else {
                "Códigos activos (${totpFiltrados.size} de ${conTotp.size})"
            }

            GrupoAjustes(etiqueta = etiquetaGrupo) {
                if (totpFiltrados.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(FormaPequena)
                                .background(fondoBadgeParaTema(Color2FA)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Timer,
                                contentDescription = null,
                                tint = colorLegibleParaTema(Color2FA),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            if (conTotp.isEmpty()) "Todavía no hay dobles factores" else "No se encontraron códigos",
                            color = ColorTitulos,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            if (conTotp.isEmpty()) {
                                "Cuando una web te ofrezca activar el 2FA, escanea su QR o escribe su clave. Aquí verás el código actualizado cada 30 segundos."
                            } else {
                                "Ninguna cuenta 2FA coincide con la búsqueda o filtro aplicado."
                            },
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    totpFiltrados.forEachIndexed { index, entrada ->
                        val secreto = entrada.secretoTotp ?: return@forEachIndexed
                        val periodo = entrada.totpPeriodo.toLong().coerceAtLeast(10L)
                        val segundosRestantes = Totp.segundosRestantes(ahora, periodo)
                        val codigo = remember(ahora / periodo, secreto, entrada.totpDigitos) {
                            try {
                                Totp.codigo(
                                    secreto = com.jlnavas3.bovedalocal.crypto.Base32.decodificar(secreto),
                                    segundosUnix = ahora,
                                    digitos = entrada.totpDigitos,
                                    periodo = periodo
                                )
                            } catch (e: Exception) {
                                "------"
                            }
                        }
                        val codigoVisible = if (ajustes.totpSepararDigitos && codigo.length == 6) {
                            "${codigo.take(3)} ${codigo.drop(3)}"
                        } else {
                            codigo
                        }

                        if (index > 0) SeparadorFilaSimple()

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .fillMaxHeight()
                                    .align(Alignment.CenterStart)
                                    .background(ColorDatos2FA)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        haptica.exito()
                                        vm.copiar("Código 2FA", codigo, true)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = entrada.totpEmisor.ifBlank { entrada.titulo },
                                    color = ColorTitulos,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (entrada.usuario.isNotBlank()) {
                                    Text(
                                        text = entrada.usuario,
                                        color = TextoSecundario,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Spacer(Modifier.height(6.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = codigoVisible,
                                        color = ColorTitulos,
                                        style = EstiloMonoGrande.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp)
                                    )
                                    IndicadorTotpTarta(
                                        segundosRestantes = segundosRestantes,
                                        periodo = periodo,
                                        tamano = 18.dp
                                    )
                                }
                                Spacer(Modifier.height(3.dp))
                                Text(
                                    text = "Toca para copiar · $segundosRestantes s restantes",
                                    color = TextoSecundario,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Botón de favorito con ripple circular
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            haptica.tic()
                                            vm.alternarFavorito(entrada.id)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = if (entrada.favorito) "Quitar de favoritos" else "Marcar como favorito",
                                        tint = if (entrada.favorito) Ambar else ColorIconosInternos.copy(alpha = 0.25f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Filled.ContentCopy,
                                    contentDescription = "Copiar código",
                                    tint = ColorIconosInternos,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }

    // Diálogo de ordenación
    if (mostrarDialogoOrdenacion) {
        DialogoOrdenacionLista(
            criterioActual = criterioOrdenacion,
            alSeleccionarCriterio = { crit ->
                haptica.tic()
                criterioOrdenacion = crit
            },
            alCerrar = { mostrarDialogoOrdenacion = false }
        )
    }

    // Modal informativo: ¿Cómo funciona el 2FA?
    if (dialogoComoFunciona) {
        val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()
        val colorDialogo = if (esOscuro) androidx.compose.ui.graphics.Color(0xFF212023) else androidx.compose.ui.graphics.Color(0xFFFFFFFF)
        AlertDialog(
            onDismissRequest = { dialogoComoFunciona = false },
            containerColor = colorDialogo,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(22.dp),
            icon = {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(fondoBadgeParaTema(Color2FA)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Timer,
                        contentDescription = null,
                        tint = colorLegibleParaTema(Color2FA),
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            title = {
                Text(
                    "¿Cómo funciona el 2FA?",
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Bóveda local calcula los códigos de 6 u 8 dígitos usando el reloj interno de tu dispositivo y la clave secreta compartida (RFC 6238).",
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "No depende de servidores ni de conexión a internet para funcionar, manteniendo tus cuentas 100% protegidas y privadas.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Puedes activar 2FA escaneando el código QR del servicio o escribiendo la clave secreta manualmente con los botones de la cabecera superior.",
                        color = TextoSecundario.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { dialogoComoFunciona = false }) {
                    Text("Entendido", color = Color2FA, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

