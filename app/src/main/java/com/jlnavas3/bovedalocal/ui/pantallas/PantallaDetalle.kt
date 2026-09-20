package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.DialogoCompartirQr
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaCamposDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaHistorialDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaPasskeyDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaTotpDetalle
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaDetalle(vm: VaultViewModel, id: String) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val estado by vm.estado.collectAsStateWithLifecycle()
    val entrada = (estado as? EstadoBoveda.Desbloqueada)?.entradas?.find { it.id == id } ?: vm.entrada(id)
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    var revelada by remember { mutableStateOf(false) }
    var confirmarBorrado by remember { mutableStateOf(false) }
    var mostrarDialogoQr by remember { mutableStateOf(false) }
    var ultimaCopia by remember { mutableStateOf<String?>(null) }

    val formatoFechaCompacta = remember {
        SimpleDateFormat("dd/MM/yy HH:mm", Locale.forLanguageTag("es-ES"))
    }

    if (entrada == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Esta entrada ya no está en la bóveda", color = TextoSecundario)
            Spacer(Modifier.height(16.dp))
            BotonColorido(
                texto = "Volver",
                color = ColorAcento,
                icono = Icons.AutoMirrored.Filled.ArrowBack
            ) { vm.volverAtras() }
        }
        return
    }

    LaunchedEffect(entrada.id) {
        val tipoDesc = entrada.tipo.etiqueta.lowercase()
        Diagnostico.apuntar("bóveda", "Detalle de entrada consultado ($tipoDesc)")
    }

    LaunchedEffect(revelada) {
        if (revelada) {
            val tipoDesc = entrada.tipo.etiqueta.lowercase()
            Diagnostico.apuntar("seguridad", "Contraseña revelada en pantalla ($tipoDesc)")
        }
    }

    LaunchedEffect(ultimaCopia) {
        if (ultimaCopia != null) {
            delay(1500)
            ultimaCopia = null
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        // Barra superior plana nativa
        BarraSuperiorPantalla(
            titulo = "",
            alVolver = { vm.volverAtras() },
            conSeparador = scrollState.value > 0,
            colorFondo = ColorAjustesFondo,
            acciones = {
                IconButton(onClick = { haptica.tic(); mostrarDialogoQr = true }) {
                    Icon(
                        imageVector = Icons.Filled.QrCode,
                        contentDescription = "Compartir por código QR",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(
                    onClick = { haptica.tic(); vm.alternarFavorito(entrada.id) },
                    modifier = Modifier.clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = if (entrada.favorito) "Quitar de favoritos" else "Marcar como favorito",
                        tint = if (entrada.favorito) Ambar else ColorIconosInternos.copy(alpha = 0.35f),
                        modifier = Modifier.size(26.dp)
                    )
                }
                IconButton(onClick = { haptica.tic(); vm.ir(Pantalla.Editar(entrada.id)) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = { haptica.tic(); confirmarBorrado = true }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Mover a papelera",
                        tint = Peligro,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Cabecera Hero de identidad
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Monograma(
                    titulo = entrada.titulo.ifBlank { "?" },
                    semilla = entrada.urls.firstOrNull() ?: entrada.passkey?.rpId ?: entrada.titulo,
                    tamano = 60
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = entrada.titulo.ifBlank { "Sin título" },
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = ColorTitulos,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(FormaPequena)
                        .background(ColorAcento.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = entrada.tipo.etiqueta,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = ColorAcento
                    )
                }
            }

            if (entrada.tipo == com.jlnavas3.bovedalocal.data.TipoEntrada.WIFI) {
                Spacer(Modifier.height(8.dp))
                BotonColorido(
                    texto = "Compartir Wi-Fi por código QR",
                    color = ColorAcento,
                    icono = Icons.Filled.QrCode,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    haptica.tic()
                    mostrarDialogoQr = true
                }
            }

            Spacer(Modifier.height(14.dp))

            // Grupo 1: Credenciales principales (siempre visibles, sin acordeones)
            val tieneCredenciales = entrada.usuario.isNotBlank() || entrada.contrasena.isNotBlank()
            if (tieneCredenciales) {
                GrupoAjustes(etiqueta = "Credenciales") {
                    if (entrada.usuario.isNotBlank()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Usuario o correo",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = TextoSecundario
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = entrada.usuario,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                    color = TextoPrincipal
                                )
                            }
                            BotonCopiar(copiado = ultimaCopia == "usuario") {
                                haptica.toque()
                                vm.copiar("Usuario", entrada.usuario, sensible = false)
                                ultimaCopia = "usuario"
                            }
                        }
                    }

                    if (entrada.usuario.isNotBlank() && entrada.contrasena.isNotBlank()) {
                        SeparadorFilaSimple()
                    }

                    if (entrada.contrasena.isNotBlank()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Contraseña · ${entrada.contrasena.length} caracteres",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = TextoSecundario
                                )
                                Spacer(Modifier.height(4.dp))
                                if (revelada) {
                                    Text(
                                        text = contrasenaColoreada(entrada.contrasena),
                                        style = EstiloMono,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    Text(
                                        text = "•".repeat(entrada.contrasena.length.coerceIn(8, 24)),
                                        style = EstiloMonoGrande.copy(letterSpacing = 2.sp),
                                        color = TextoSecundario,
                                        maxLines = 1,
                                        softWrap = false,
                                        overflow = TextOverflow.Clip,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = {
                                    haptica.toque()
                                    revelada = !revelada
                                }) {
                                    Icon(
                                        imageVector = if (revelada) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (revelada) "Ocultar contraseña" else "Mostrar contraseña",
                                        tint = ColorIconosInternos,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                BotonCopiar(copiado = ultimaCopia == "contrasena") {
                                    haptica.exito()
                                    vm.copiar("Contraseña", entrada.contrasena, sensible = true)
                                    ultimaCopia = "contrasena"
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 2: 2FA / TOTP modular
            TarjetaTotpDetalle(
                entrada = entrada,
                ajustes = ajustes,
                haptica = haptica,
                alCopiarTotp = { codigo ->
                    vm.copiar("Código TOTP", codigo, sensible = true)
                }
            )
            if (!entrada.secretoTotp.isNullOrBlank()) {
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 3: Sitios y apps asociados (si existen)
            val listaUrls = remember(entrada.urls) {
                entrada.urls.flatMap { it.split(",", "\n", ";") }.map { it.trim() }.filter { it.isNotEmpty() }
            }
            if (listaUrls.isNotEmpty()) {
                GrupoAjustes(etiqueta = "Sitios web y apps (${listaUrls.size})") {
                    listaUrls.forEachIndexed { index, url ->
                        val paquete = remember(url) { LanzadorEnlaces.extraerPaquete(url) }
                        val esApp = paquete != null
                        val estaInstalada = remember(url, contexto) {
                            if (paquete != null) LanzadorEnlaces.estaInstalada(contexto, paquete) else false
                        }
                        val nombreApp = remember(url, contexto) {
                            if (paquete != null && estaInstalada) LanzadorEnlaces.obtenerNombreApp(contexto, paquete) else null
                        }

                        if (index > 0) SeparadorFilaSimple()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    haptica.toque()
                                    LanzadorEnlaces.abrir(contexto, url, onAviso = { vm.avisar(it) })
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(FormaPequena)
                                    .background(fondoBadgeParaTema(if (esApp) ColorPasskeys else ColorGenerador)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (esApp) Icons.Filled.Android else Icons.Filled.Language,
                                    contentDescription = null,
                                    tint = colorLegibleParaTema(if (esApp) ColorPasskeys else ColorGenerador),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = nombreApp ?: paquete ?: url,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = TextoPrincipal,
                                    maxLines = 1,
                                    softWrap = false,
                                    overflow = TextOverflow.Clip
                                )
                                Spacer(Modifier.height(2.dp))
                                val subtitulo = when {
                                    esApp && estaInstalada -> "App instalada · Toca para abrir"
                                    esApp -> "App no instalada · Ver en Google Play"
                                    else -> "Sitio web · Toca para abrir"
                                }
                                Text(
                                    text = subtitulo,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = if (esApp && estaInstalada) Menta else TextoSecundario,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(Modifier.width(4.dp))
                            IconButton(
                                onClick = {
                                    haptica.toque()
                                    vm.copiar("Enlace", url, sensible = false)
                                },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ContentCopy,
                                    contentDescription = "Copiar enlace",
                                    tint = ColorIconosInternos,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    haptica.toque()
                                    LanzadorEnlaces.abrir(contexto, url, onAviso = { vm.avisar(it) })
                                },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                                    contentDescription = "Abrir enlace",
                                    tint = ColorAcento,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 4: Campos personalizados
            TarjetaCamposDetalle(
                campos = entrada.camposPersonalizados,
                vm = vm,
                haptica = haptica,
                ultimaCopia = ultimaCopia,
                alCopiarCampo = { idCampo -> ultimaCopia = idCampo }
            )
            if (entrada.camposPersonalizados.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 5: Notas
            if (entrada.notas.isNotBlank()) {
                GrupoAjustes(etiqueta = "Notas") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = entrada.notas,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoPrincipal
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            BotonCopiar(copiado = ultimaCopia == "notas") {
                                haptica.toque()
                                vm.copiar("Notas", entrada.notas, sensible = false)
                                ultimaCopia = "notas"
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 6: Etiquetas
            if (entrada.etiquetas.isNotEmpty()) {
                GrupoAjustes(etiqueta = "Etiquetas (${entrada.etiquetas.size})") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        entrada.etiquetas.forEach { etiqueta ->
                            val formaChip = FormaPequena
                            Box(
                                modifier = Modifier
                                    .clip(formaChip)
                                    .background(Borde)
                                    .then(
                                        if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                                            Modifier.border(GrosorBorde, ColorBordeActual, formaChip)
                                        else Modifier
                                    )
                                    .clickable {
                                        vm.filtrarPorEtiqueta(etiqueta)
                                        vm.volverALista()
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("#${normalizarEtiqueta(etiqueta)}", color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 7: Passkey
            entrada.passkey?.let { passkey ->
                TarjetaPasskeyDetalle(passkey = passkey, usuarioEntrada = entrada.usuario)
                Spacer(Modifier.height(16.dp))
            }

            // Grupo 8: Historial de contraseñas anteriores
            TarjetaHistorialDetalle(
                entrada = entrada,
                vm = vm,
                haptica = haptica
            )
            if (entrada.historialContrasenas.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
            }

            // Metadatos: Fechas de creación y edición (apiladas verticalmente y compactas para no colisionar)
            if (entrada.creadaEn > 0L || entrada.modificadaEn > 0L) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    if (entrada.creadaEn > 0L) {
                        Text(
                            text = "Creada: ${formatoFechaCompacta.format(Date(entrada.creadaEn))}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextoSecundario
                        )
                    }
                    if (entrada.modificadaEn > 0L && entrada.modificadaEn != entrada.creadaEn) {
                        Text(
                            text = "Editada: ${formatoFechaCompacta.format(Date(entrada.modificadaEn))}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextoSecundario
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (confirmarBorrado) {
        val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()
        val colorDialogo = if (esOscuro) Color(0xFF212023) else Color(0xFFFFFFFF)
        AlertDialog(
            onDismissRequest = { confirmarBorrado = false },
            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
            containerColor = colorDialogo,
            tonalElevation = 0.dp,
            title = { Text("¿Mover a la papelera?", color = TextoPrincipal) },
            text = { Text("Se puede restaurar desde Ajustes > Papelera durante 30 días; pasado ese tiempo se borra permanentemente.", color = TextoSecundario) },
            confirmButton = {
                TextButton(onClick = {
                    confirmarBorrado = false
                    haptica.error()
                    vm.eliminar(entrada.id)
                }) { Text("Mover a la papelera", color = Peligro) }
            },
            dismissButton = {
                TextButton(onClick = { confirmarBorrado = false }) { Text("Cancelar", color = TextoSecundario) }
            }
        )
    }

    if (mostrarDialogoQr) {
        DialogoCompartirQr(
            entrada = entrada,
            alCerrar = { mostrarDialogoQr = false }
        )
    }
}

@Composable
private fun BotonCopiar(copiado: Boolean, alPulsar: () -> Unit) {
    IconButton(onClick = alPulsar) {
        AnimatedVisibility(
            visible = copiado,
            enter = scaleIn(spring(dampingRatio = 0.5f)),
            exit = scaleOut(spring(dampingRatio = 0.6f))
        ) {
            Icon(Icons.Filled.Check, contentDescription = "Copiado", tint = Menta)
        }
        AnimatedVisibility(
            visible = !copiado,
            enter = scaleIn(spring(dampingRatio = 0.5f)),
            exit = scaleOut(spring(dampingRatio = 0.6f))
        ) {
            Icon(Icons.Filled.ContentCopy, contentDescription = "Copiar", tint = ColorIconosInternos)
        }
    }
}
