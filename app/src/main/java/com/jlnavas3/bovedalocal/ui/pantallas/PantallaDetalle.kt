package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.DialogoCompartirQr
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBoveda
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaCamposDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaHistorialDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaPasskeyDetalle
import com.jlnavas3.bovedalocal.ui.pantallas.detalle.TarjetaTotpDetalle
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

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

    if (entrada == null) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Cabecera con título, monograma y acciones rápidas
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { vm.volverAtras() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver atrás",
                    tint = ColorIconosInternos
                )
            }
            Spacer(Modifier.width(4.dp))
            Monograma(
                titulo = entrada.titulo.ifBlank { "?" },
                semilla = entrada.urls.firstOrNull() ?: entrada.passkey?.rpId ?: entrada.titulo,
                tamano = 50
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    entrada.titulo.ifBlank { "Sin título" },
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = ColorTitulos
                )
                Text(entrada.tipo.etiqueta, style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
            }
            IconButton(onClick = { haptica.tic(); mostrarDialogoQr = true }) {
                Icon(
                    imageVector = Icons.Filled.QrCode,
                    contentDescription = "Compartir por código QR",
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = { haptica.tic(); vm.alternarFavorito(entrada.id) }) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = if (entrada.favorito) "Quitar de favoritos" else "Marcar como favorito",
                    tint = if (entrada.favorito) Ambar else ColorIconosInternos.copy(alpha = 0.35f),
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        if (entrada.tipo == com.jlnavas3.bovedalocal.data.TipoEntrada.WIFI) {
            BotonColorido(
                texto = "Compartir Wi-Fi por código QR",
                color = ColorAcento,
                icono = Icons.Filled.QrCode,
                modifier = Modifier.fillMaxWidth()
            ) {
                haptica.tic()
                mostrarDialogoQr = true
            }
            Spacer(Modifier.height(14.dp))
        }

        // Usuario / Correo
        if (entrada.usuario.isNotBlank()) {
            TarjetaBoveda {
                EtiquetaSeccion("Usuario o correo")
                Spacer(Modifier.height(8.dp))
                Text(
                    text = entrada.usuario,
                    style = EstiloMono,
                    color = TextoPrincipal,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BotonCopiar(copiado = ultimaCopia == "usuario") {
                        haptica.toque()
                        vm.copiar("Usuario", entrada.usuario, sensible = false)
                        ultimaCopia = "usuario"
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // Contraseña principal
        if (entrada.contrasena.isNotBlank()) {
            TarjetaBoveda {
                EtiquetaSeccion("Contraseña")
                Spacer(Modifier.height(8.dp))
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${entrada.contrasena.length} caracteres",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            haptica.toque()
                            revelada = !revelada
                        }) {
                            Icon(
                                imageVector = if (revelada) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (revelada) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = ColorIconosInternos,
                                modifier = Modifier.size(26.dp)
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
            Spacer(Modifier.height(12.dp))
        }

        // 2FA / TOTP modular
        TarjetaTotpDetalle(
            entrada = entrada,
            ajustes = ajustes,
            haptica = haptica,
            alCopiarTotp = { codigo ->
                vm.copiar("Código TOTP", codigo, sensible = true)
            }
        )
        if (!entrada.secretoTotp.isNullOrBlank()) {
            Spacer(Modifier.height(12.dp))
        }

        // Campos personalizados modulares
        TarjetaCamposDetalle(
            campos = entrada.camposPersonalizados,
            vm = vm,
            haptica = haptica,
            ultimaCopia = ultimaCopia,
            alCopiarCampo = { idCampo -> ultimaCopia = idCampo }
        )
        if (entrada.camposPersonalizados.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
        }

        // URLs y sitios asociados
        if (entrada.urls.isNotEmpty()) {
            TarjetaBoveda {
                EtiquetaSeccion("Sitios y apps asociados")
                Spacer(Modifier.height(8.dp))
                entrada.urls.forEach { url ->
                    Text(url, style = MaterialTheme.typography.bodyLarge, color = TextoPrincipal)
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // Notas
        if (entrada.notas.isNotBlank()) {
            TarjetaBoveda {
                EtiquetaSeccion("Notas")
                Spacer(Modifier.height(8.dp))
                Text(entrada.notas, style = MaterialTheme.typography.bodyLarge, color = TextoPrincipal)
            }
            Spacer(Modifier.height(12.dp))
        }

        // Etiquetas
        if (entrada.etiquetas.isNotEmpty()) {
            TarjetaBoveda {
                EtiquetaSeccion("Etiquetas")
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
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
            Spacer(Modifier.height(12.dp))
        }

        // Historial de contraseñas modular con visor y restauración
        TarjetaHistorialDetalle(
            entrada = entrada,
            vm = vm,
            haptica = haptica
        )
        if (entrada.historialContrasenas.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
        }

        // Passkey modular
        entrada.passkey?.let { passkey ->
            TarjetaPasskeyDetalle(passkey = passkey, usuarioEntrada = entrada.usuario)
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            BotonColorido(
                texto = "Editar",
                color = ColorAcento,
                icono = Icons.Filled.Edit,
                modifier = Modifier.weight(1f)
            ) { vm.ir(Pantalla.Editar(entrada.id)) }
            BotonColorido(
                texto = "Borrar",
                color = ColorPapelera,
                icono = Icons.Filled.Delete,
                modifier = Modifier.weight(1f)
            ) { confirmarBorrado = true }
        }
        Spacer(Modifier.height(32.dp))
    }

    if (confirmarBorrado) {
        AlertDialog(
            onDismissRequest = { confirmarBorrado = false },
            title = { Text("¿Mover a la papelera?") },
            text = { Text("Se puede restaurar desde Ajustes > Papelera durante 30 días; pasado ese tiempo se borra sola.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmarBorrado = false
                    haptica.error()
                    vm.eliminar(entrada.id)
                }) { Text("Mover a la papelera", color = Peligro) }
            },
            dismissButton = {
                TextButton(onClick = { confirmarBorrado = false }) { Text("Cancelar") }
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
