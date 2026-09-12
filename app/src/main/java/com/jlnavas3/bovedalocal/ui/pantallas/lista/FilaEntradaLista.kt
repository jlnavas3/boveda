package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.AnilloTotp
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun FilaGrupoSitio(
    clave: String,
    cantidad: Int,
    expandido: Boolean,
    alturaFila: Dp = 74.dp,
    tamanoMonograma: Int = 46,
    resaltado: Boolean = false,
    alAlternar: () -> Unit
) {
    val forma = FormaTarjeta
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(alturaFila)
            .clip(forma)
            .background(if (resaltado) Ambar.copy(alpha = 0.16f) else ColorTarjetas)
            .then(
                if (resaltado) {
                    Modifier.border(1.5.dp, Ambar, forma)
                } else if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else {
                    Modifier
                }
            )
            .clickable { alAlternar() }
            .padding(horizontal = 12.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Monograma(titulo = clave.ifBlank { "?" }, semilla = clave, tamano = tamanoMonograma)
        Spacer(Modifier.width(if (alturaFila.value <= 48f) 10.dp else 14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                clave,
                style = if (alturaFila.value <= 48f) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium,
                color = if (resaltado) Ambar else TextoPrincipal,
                maxLines = 1
            )
            Text(
                "$cantidad ${if (cantidad == 1) "cuenta" else "cuentas"}",
                style = if (alturaFila.value <= 48f) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
        }
        Icon(
            if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = if (expandido) "Contraer" else "Expandir",
            tint = if (resaltado) Ambar else ColorIconosInternos
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun FilaEntrada(
    entrada: Entrada,
    seleccionActiva: Boolean,
    seleccionado: Boolean,
    alAbrir: () -> Unit,
    alCopiarUsuario: () -> Unit,
    alCopiarContrasena: () -> Unit,
    alFavorito: () -> Unit,
    alCopiarCodigo: (String) -> Unit = {},
    alPulsarLargo: () -> Unit,
    alAlternarSeleccion: () -> Unit,
    alturaFila: Dp = 74.dp,
    tamanoMonograma: Int = 46,
    resaltado: Boolean = false
) {
    val compacta = alturaFila.value <= 48f
    val forma = FormaTarjeta
    val contenidoFila: @Composable () -> Unit = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(alturaFila)
                .clip(forma)
                .background(if (seleccionado) Ambar.copy(alpha = 0.22f) else if (resaltado) Ambar.copy(alpha = 0.16f) else ColorTarjetas)
                .then(
                    if (resaltado) {
                        Modifier.border(1.5.dp, Ambar, forma)
                    } else if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent) {
                        Modifier.border(GrosorBorde, ColorBordeActual, forma)
                    } else {
                        Modifier
                    }
                )
                .combinedClickable(
                    onClick = { if (seleccionActiva) alAlternarSeleccion() else alAbrir() },
                    onLongClick = { if (!seleccionActiva) alPulsarLargo() }
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (seleccionActiva) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(if (seleccionado) Ambar else Borde),
                    contentAlignment = Alignment.Center
                ) {
                    if (seleccionado) {
                        Icon(Icons.Filled.Check, contentDescription = null, tint = ColorSobreAcento, modifier = Modifier.size(if (compacta) 16.dp else 18.dp))
                    }
                }
            } else {
                Monograma(
                    titulo = entrada.titulo.ifBlank { "?" },
                    semilla = entrada.urls.firstOrNull() ?: entrada.passkey?.rpId ?: entrada.titulo,
                    tamano = tamanoMonograma
                )
            }
            Spacer(Modifier.width(if (compacta) 10.dp else 14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    entrada.titulo.ifBlank { "Sin título" },
                    style = if (compacta) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium,
                    color = if (resaltado) Ambar else TextoPrincipal,
                    maxLines = 1
                )
                Text(
                    when (entrada.tipo) {
                        TipoEntrada.PASSKEY -> "Passkey · ${entrada.passkey?.rpId ?: ""}"
                        TipoEntrada.NOTA -> "Nota segura"
                        TipoEntrada.LOGIN -> entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "Sin usuario" }
                        else -> entrada.usuario.ifBlank {
                            entrada.camposPersonalizados.firstOrNull { it.valor.isNotBlank() }?.let { "${it.etiqueta}: ${if (it.esSensibleEfectivo) "••••" else it.valor}" }
                                ?: entrada.tipo.etiqueta
                        }
                    },
                    style = if (compacta) MaterialTheme.typography.labelMedium else MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario,
                    maxLines = 1
                )
            }
            val secreto = entrada.secretoTotp
            if (!seleccionActiva && !secreto.isNullOrBlank()) {
                var ahora by remember { mutableStateOf(System.currentTimeMillis() / 1000) }
                LaunchedEffect(secreto) {
                    while (true) {
                        ahora = System.currentTimeMillis() / 1000
                        kotlinx.coroutines.delay(1000)
                    }
                }
                val periodo = entrada.totpPeriodo.toLong()
                val codigo = remember(ahora / periodo, secreto) {
                    try {
                        Totp.codigo(
                            secreto = Base32.decodificar(secreto),
                            segundosUnix = ahora,
                            digitos = entrada.totpDigitos,
                            periodo = periodo
                        )
                    } catch (e: Exception) {
                        "------"
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { alCopiarCodigo(codigo) }.padding(horizontal = 4.dp)
                ) {
                    Text(codigo, style = EstiloMono, color = ColorTitulos)
                    Spacer(Modifier.width(if (compacta) 4.dp else 8.dp))
                    AnilloTotp(
                        codigo = "",
                        segundosRestantes = Totp.segundosRestantes(ahora, periodo),
                        tamano = if (compacta) 28 else 34,
                        periodo = periodo
                    )
                }
            }
            if (!seleccionActiva) {
                IconButton(onClick = alFavorito, modifier = Modifier.size(40.dp)) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Favorito",
                        tint = if (entrada.favorito) Ambar else ColorBordeActual.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }

    if (seleccionActiva) {
        contenidoFila()
    } else {
        val contexto = LocalContext.current
        val haptica = remember { Haptica(contexto) }
        val scope = rememberCoroutineScope()
        val animOffset = remember { Animatable(0f) }
        var anchoFilaPx by remember { mutableFloatStateOf(0f) }
        var dioHapticaTope by remember { mutableStateOf(false) }

        LaunchedEffect(entrada.id) {
            animOffset.snapTo(0f)
        }

        val topeMaximo = anchoFilaPx * 0.45f
        val topeActual by rememberUpdatedState(topeMaximo)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(alturaFila)
                .onSizeChanged { anchoFilaPx = it.width.toFloat() }
        ) {
            val offsetActual = animOffset.value
            val limite = topeActual
            val progreso = if (limite > 0f) (abs(offsetActual) / limite).coerceIn(0f, 1f) else 0f
            val escala = 0.85f + 0.20f * progreso
            val opacidad = 0.4f + 0.6f * progreso

            if (offsetActual > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(forma)
                        .background(Menta.copy(alpha = 0.14f + 0.10f * progreso))
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.scale(escala)
                    ) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = null,
                            tint = Menta.copy(alpha = opacidad),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Copiar usuario",
                            color = Menta.copy(alpha = opacidad),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else if (offsetActual < 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(forma)
                        .background(Ambar.copy(alpha = 0.14f + 0.10f * progreso))
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.scale(escala)
                    ) {
                        Text(
                            "Copiar contraseña",
                            color = Ambar.copy(alpha = opacidad),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            Icons.Filled.Key,
                            contentDescription = null,
                            tint = Ambar.copy(alpha = opacidad),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(animOffset.value.roundToInt(), 0) }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragStart = {
                                dioHapticaTope = false
                            },
                            onDragEnd = {
                                val maximo = topeActual
                                if (maximo > 0f) {
                                    val alcanzado = abs(animOffset.value) >= maximo * 0.94f
                                    if (alcanzado) {
                                        if (animOffset.value > 0f) {
                                            alCopiarUsuario()
                                        } else {
                                            alCopiarContrasena()
                                        }
                                    }
                                }
                                dioHapticaTope = false
                                scope.launch {
                                    animOffset.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioLowBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    )
                                }
                            },
                            onDragCancel = {
                                dioHapticaTope = false
                                scope.launch {
                                    animOffset.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioLowBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    )
                                }
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                val maximo = topeActual
                                if (maximo > 0f) {
                                    change.consume()
                                    val nuevoOffset = (animOffset.value + dragAmount).coerceIn(-maximo, maximo)
                                    scope.launch { animOffset.snapTo(nuevoOffset) }

                                    val enTope = abs(nuevoOffset) >= maximo * 0.96f
                                    if (enTope && !dioHapticaTope) {
                                        haptica.tic()
                                        dioHapticaTope = true
                                    } else if (!enTope && dioHapticaTope) {
                                        dioHapticaTope = false
                                    }
                                }
                            }
                        )
                    }
            ) {
                contenidoFila()
            }
        }
    }
}
