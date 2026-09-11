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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import com.jlnavas3.bovedalocal.data.CampoPersonalizado
import com.jlnavas3.bovedalocal.data.TipoCampo
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.componentes.AnilloTotp
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepo
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera

@Composable
fun PantallaDetalle(vm: VaultViewModel, id: String) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val entrada = vm.entrada(id)
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    var revelada by remember { mutableStateOf(false) }
    var confirmarBorrado by remember { mutableStateOf(false) }
    var ultimaCopia by remember { mutableStateOf<String?>(null) }

    if (entrada == null) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
            Text("Esta entrada ya no está en la bóveda", color = TextoSecundario)
            Spacer(Modifier.height(16.dp))
            BotonColorido("Volver", color = ColorAcento) { vm.volverAtras() }
        }
        return
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
            IconButton(onClick = { haptica.tic(); vm.alternarFavorito(entrada.id) }) {
                Icon(Icons.Filled.Star, contentDescription = "Favorito", tint = if (entrada.favorito) ColorTitulos else Borde)
            }
        }

        Spacer(Modifier.height(20.dp))

        if (entrada.usuario.isNotBlank()) {
            TarjetaPepo {
                EtiquetaSeccion("Usuario")
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(entrada.usuario, style = EstiloMono, color = TextoPrincipal, modifier = Modifier.weight(1f))
                    BotonCopiar(copiado = ultimaCopia == "usuario") {
                        haptica.toque()
                        vm.copiar("Usuario", entrada.usuario, sensible = false)
                        ultimaCopia = "usuario"
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        if (entrada.contrasena.isNotBlank()) {
            TarjetaPepo {
                EtiquetaSeccion("Contraseña")
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = contrasenaColoreada(entrada.contrasena),
                        style = EstiloMono,
                        modifier = Modifier
                            .weight(1f)
                            .then(if (revelada) Modifier else Modifier.blur(9.dp))
                            .clickable {
                                haptica.toque()
                                revelada = !revelada
                            }
                    )
                    BotonCopiar(copiado = ultimaCopia == "contrasena") {
                        haptica.exito()
                        vm.copiar("Contraseña", entrada.contrasena, sensible = true)
                        ultimaCopia = "contrasena"
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    if (revelada) "Pulsa el texto para volver a ocultarla" else "Pulsa el texto para revelarla",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }
            Spacer(Modifier.height(12.dp))
        }

        entrada.secretoTotp?.takeIf { it.isNotBlank() }?.let { secreto ->
            val periodo = entrada.totpPeriodo.toLong().coerceAtLeast(10L)
            var ahora by remember { mutableLongStateOf(System.currentTimeMillis() / 1000) }
            LaunchedEffect(secreto) {
                while (true) {
                    ahora = System.currentTimeMillis() / 1000
                    delay(500)
                }
            }
            val codigo = remember(ahora / periodo, secreto, entrada.totpDigitos, entrada.totpAlgoritmo) {
                try {
                    Totp.codigo(
                        secreto = com.jlnavas3.bovedalocal.crypto.Base32.decodificar(secreto),
                        segundosUnix = ahora,
                        digitos = entrada.totpDigitos,
                        periodo = periodo,
                        algoritmo = entrada.totpAlgoritmo
                    )
                } catch (e: Exception) {
                    "------"
                }
            }
            TarjetaPepo {
                EtiquetaSeccion("Código de verificación (TOTP)")
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AnilloTotp(
                        codigo = if (ajustes.totpSepararDigitos && codigo.length == 6) "${codigo.take(3)} ${codigo.drop(3)}" else codigo,
                        segundosRestantes = Totp.segundosRestantes(ahora, periodo),
                        periodo = periodo
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Se renueva cada ${periodo} s · ${entrada.totpDigitos} dígitos", color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        BotonBorde("Copiar código", color = Menta) {
                            haptica.toque()
                            vm.copiar("Código TOTP", codigo, sensible = true)
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        if (entrada.camposPersonalizados.isNotEmpty()) {
            TarjetaPepo {
                EtiquetaSeccion("Campos personalizados")
                Spacer(Modifier.height(8.dp))
                entrada.camposPersonalizados.forEachIndexed { index, campo ->
                    if (index > 0) {
                        Spacer(Modifier.height(10.dp))
                    }
                    FilaCampoPersonalizadoDetalle(
                        campo = campo,
                        vm = vm,
                        haptica = haptica,
                        copiado = ultimaCopia == "campo_${campo.id}",
                        alCopiar = { ultimaCopia = "campo_${campo.id}" }
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        if (entrada.urls.isNotEmpty()) {
            TarjetaPepo {
                EtiquetaSeccion("Sitios y apps asociados")
                Spacer(Modifier.height(8.dp))
                entrada.urls.forEach { url ->
                    Text(url, style = MaterialTheme.typography.bodyLarge, color = TextoPrincipal)
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        if (entrada.notas.isNotBlank()) {
            TarjetaPepo {
                EtiquetaSeccion("Notas")
                Spacer(Modifier.height(8.dp))
                Text(entrada.notas, style = MaterialTheme.typography.bodyLarge, color = TextoPrincipal)
            }
            Spacer(Modifier.height(12.dp))
        }

        if (entrada.etiquetas.isNotEmpty()) {
            TarjetaPepo {
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

        if (entrada.historialContrasenas.isNotEmpty()) {
            TarjetaPepo {
                EtiquetaSeccion("Contraseñas anteriores")
                Spacer(Modifier.height(6.dp))
                Text(
                    "No se muestran en pantalla, solo se pueden copiar por si te hace falta recordar una.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(10.dp))
                entrada.historialContrasenas.forEach { cambio ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            formatearFecha(cambio.cambiadaEn),
                            color = TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = {
                            haptica.toque()
                            vm.copiar("Contraseña anterior", cambio.contrasena, sensible = true)
                        }) { Text("Copiar", color = ColorIconosInternos) }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        entrada.passkey?.let { passkey ->
            TarjetaPepo {
                EtiquetaSeccion("Passkey")
                Spacer(Modifier.height(8.dp))
                Text("Servicio: ${passkey.rpName.ifBlank { passkey.rpId }}", color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
                Text("Dominio: ${passkey.rpId}", color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
                if (passkey.usuario.isNotBlank() || entrada.usuario.isNotBlank()) {
                    Text(
                        "Cuenta: ${passkey.usuario.ifBlank { entrada.usuario }}",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text("Algoritmo: ${passkey.algoritmo} (P-256)", color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text("La clave privada permanece cifrada dentro de la bóveda y nunca se muestra.", color = Menta, style = MaterialTheme.typography.bodyMedium)
            }
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

@Composable
private fun IconoEditar() {
    Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = ColorIconosInternos)
}

@Composable
private fun IconoBorrar() {
    Icon(Icons.Filled.Delete, contentDescription = "Borrar", tint = Peligro)
}

private fun formatearFecha(momento: Long): String {
    if (momento <= 0L) return "fecha desconocida"
    val formato = java.text.SimpleDateFormat("d MMM yyyy", java.util.Locale("es"))
    return formato.format(java.util.Date(momento))
}

@Composable
private fun FilaCampoPersonalizadoDetalle(
    campo: CampoPersonalizado,
    vm: VaultViewModel,
    haptica: com.jlnavas3.bovedalocal.util.Haptica,
    copiado: Boolean,
    alCopiar: () -> Unit
) {
    var revelado by remember { mutableStateOf(false) }
    val esOculto = campo.tipo != TipoCampo.TEXTO

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta)
            .background(com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                campo.etiqueta.ifBlank { "Campo adicional" },
                style = MaterialTheme.typography.bodySmall,
                color = com.jlnavas3.bovedalocal.ui.theme.TextoSecundario,
                fontWeight = FontWeight.Medium
            )
            Box(
                modifier = Modifier
                    .clip(com.jlnavas3.bovedalocal.ui.theme.FormaPequena)
                    .background(Ambar.copy(alpha = 0.15f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    campo.tipo.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    color = Ambar
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when {
                    !esOculto -> campo.valor
                    revelado -> campo.valor
                    campo.tipo == TipoCampo.PIN -> "• ".repeat(campo.valor.length).trim()
                    else -> "••••••••••••"
                },
                style = if (esOculto) com.jlnavas3.bovedalocal.ui.theme.EstiloMono else MaterialTheme.typography.bodyLarge,
                color = com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal,
                modifier = Modifier
                    .weight(1f)
                    .then(if (!esOculto || revelado) Modifier else Modifier.clickable { revelado = true })
            )
            if (esOculto) {
                IconButton(onClick = {
                    haptica.toque()
                    revelado = !revelado
                }) {
                    Icon(
                        if (revelado) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (revelado) "Ocultar" else "Revelar",
                        tint = com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
                    )
                }
            }
            BotonCopiar(copiado = copiado) {
                haptica.exito()
                vm.copiar(campo.etiqueta.ifBlank { "Campo personalizado" }, campo.valor, sensible = esOculto)
                alCopiar()
            }
        }
    }
}
