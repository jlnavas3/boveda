package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalTouchSlopOrCancellation
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
    segundosUnix: Long = System.currentTimeMillis() / 1000,
    alturaFila: Dp = 74.dp,
    tamanoMonograma: Int = 46,
    resaltado: Boolean = false,
    separarDigitosTotp: Boolean = false,
    mostrarIndicadores: Boolean = true,
    enGrupo: Boolean = false,
    esUltimoEnGrupo: Boolean = false
) {
    val compacta = alturaFila.value <= 48f
    val forma = if (enGrupo) {
        if (esUltimoEnGrupo) RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp) else RoundedCornerShape(0.dp)
    } else {
        RoundedCornerShape(18.dp)
    }
    val tamanoIcono = if (compacta) 32 else if (alturaFila.value <= 64f) 36 else 40

    val contenidoFila: @Composable () -> Unit = {
        val secreto = entrada.secretoTotp
        val tieneTotp = !seleccionActiva && !secreto.isNullOrBlank()

        val fondoFila = if (seleccionado) {
            Ambar.copy(alpha = 0.22f)
        } else if (resaltado) {
            Ambar.copy(alpha = 0.16f)
        } else if (enGrupo) {
            Color.Transparent
        } else {
            ColorTarjetaAjustes
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(alturaFila)
                .clip(forma)
                .background(fondoFila)
                .then(
                    if (resaltado) Modifier.border(1.5.dp, Ambar, forma)
                    else if (seleccionado) Modifier.border(1.dp, Ambar.copy(alpha = 0.5f), forma)
                    else Modifier
                )
                .combinedClickable(
                    onClick = { if (seleccionActiva) alAlternarSeleccion() else alAbrir() },
                    onLongClick = { if (!seleccionActiva) alPulsarLargo() }
                )
        ) {
            if (mostrarIndicadores) {
                IndicadorContenidoTarjeta(
                    entrada = entrada,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (seleccionActiva) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (seleccionado) Ambar else Borde),
                        contentAlignment = Alignment.Center
                    ) {
                        if (seleccionado) {
                            Icon(Icons.Filled.Check, contentDescription = null, tint = ColorSobreAcento, modifier = Modifier.size(15.dp))
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                }

                // Avatar Squircle / Icono representativo a la izquierda
                Box(
                    modifier = Modifier.size(tamanoIcono.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (entrada.tipo) {
                        TipoEntrada.PASSKEY -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(ColorPasskeys),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Fingerprint, contentDescription = null, tint = Color.White, modifier = Modifier.size((tamanoIcono * 0.52f).dp))
                            }
                        }
                        TipoEntrada.NOTA -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Color(0xFF0288D1)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Description, contentDescription = null, tint = Color.White, modifier = Modifier.size((tamanoIcono * 0.52f).dp))
                            }
                        }
                        TipoEntrada.TARJETA -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Color(0xFFE91E63)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.CreditCard, contentDescription = null, tint = Color.White, modifier = Modifier.size((tamanoIcono * 0.52f).dp))
                            }
                        }
                        TipoEntrada.WIFI -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Color(0xFF00897B)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Wifi, contentDescription = null, tint = Color.White, modifier = Modifier.size((tamanoIcono * 0.52f).dp))
                            }
                        }
                        TipoEntrada.CUENTA_BANCARIA -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Color(0xFF3949AB)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.AccountBalance, contentDescription = null, tint = Color.White, modifier = Modifier.size((tamanoIcono * 0.52f).dp))
                            }
                        }
                        TipoEntrada.SERVIDOR -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Color(0xFF546E7A)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Dns, contentDescription = null, tint = Color.White, modifier = Modifier.size((tamanoIcono * 0.52f).dp))
                            }
                        }
                        TipoEntrada.WALLET -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFB300)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.AccountBalanceWallet, contentDescription = null, tint = Color.White, modifier = Modifier.size((tamanoIcono * 0.52f).dp))
                            }
                        }
                        TipoEntrada.IDENTIDAD -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(Color(0xFF00ACC1)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Badge, contentDescription = null, tint = Color.White, modifier = Modifier.size((tamanoIcono * 0.52f).dp))
                            }
                        }
                        else -> {
                            Monograma(
                                titulo = entrada.titulo.ifBlank { "?" },
                                semilla = entrada.urls.firstOrNull() ?: entrada.usuario.ifBlank { entrada.titulo },
                                tamano = tamanoIcono
                            )
                        }
                    }
                }

                Spacer(Modifier.width(12.dp))

                // Columna central: Título con TOTP y Subtítulo
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = entrada.titulo.ifBlank { "Sin título" },
                            style = if (compacta) {
                                MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            } else {
                                MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                            },
                            color = if (resaltado) Ambar else TextoPrincipal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        if (tieneTotp) {
                            ContenidoTotpEnFila(
                                secreto = secreto,
                                segundosUnix = segundosUnix,
                                periodo = entrada.totpPeriodo.toLong().coerceAtLeast(10L),
                                digitos = entrada.totpDigitos,
                                algoritmo = entrada.totpAlgoritmo,
                                separarDigitosTotp = separarDigitosTotp,
                                compacta = compacta,
                                alCopiarCodigo = alCopiarCodigo
                            )
                        }
                    }

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = when (entrada.tipo) {
                            TipoEntrada.PASSKEY -> "Passkey · ${entrada.passkey?.rpId ?: ""}"
                            TipoEntrada.NOTA -> "Nota segura"
                            TipoEntrada.LOGIN -> entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "Sin usuario" }
                            else -> entrada.usuario.ifBlank {
                                entrada.camposPersonalizados.firstOrNull { it.valor.isNotBlank() }?.let { "${it.etiqueta}: ${if (it.esSensibleEfectivo) "••••" else it.valor}" }
                                    ?: entrada.tipo.etiqueta
                            }
                        },
                        style = if (compacta) MaterialTheme.typography.labelSmall else MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                        color = TextoSecundario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Bloque derecho: solo botón de favorito (si no está en selección)
                if (!seleccionActiva) {
                    Spacer(Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable(onClick = alFavorito),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Favorito",
                            tint = if (entrada.favorito) Ambar else ColorBordeActual.copy(alpha = 0.45f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
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
        var dioHapticaTope by remember { mutableStateOf(false) }

        LaunchedEffect(entrada.id) {
            animOffset.snapTo(0f)
        }

        val densidad = LocalDensity.current
        val topeMaximo = remember(densidad) { with(densidad) { 160.dp.toPx() } }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(alturaFila)
        ) {
            val offsetActual = animOffset.value
            val limite = topeMaximo
            val progreso = if (limite > 0f) (abs(offsetActual) / limite).coerceIn(0f, 1f) else 0f

            if (offsetActual > 0f) {
                FondoDeslizamientoUsuario(
                    forma = forma,
                    progreso = progreso
                )
            } else if (offsetActual < 0f) {
                FondoDeslizamientoContrasena(
                    forma = forma,
                    progreso = progreso
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(animOffset.value.roundToInt(), 0) }
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            val change = awaitHorizontalTouchSlopOrCancellation(down.id) { change, _ ->
                                change.consume()
                            }
                            if (change != null) {
                                dioHapticaTope = false
                                var dragOffset = animOffset.value
                                while (true) {
                                    val event = awaitPointerEvent()
                                    val dragChange = event.changes.firstOrNull { it.id == change.id } ?: break
                                    if (dragChange.pressed) {
                                        val dragAmount = dragChange.positionChange().x
                                        if (dragAmount != 0f) {
                                            dragChange.consume()
                                            val maximo = topeMaximo
                                            if (maximo > 0f) {
                                                dragOffset = (dragOffset + dragAmount).coerceIn(-maximo, maximo)
                                                scope.launch { animOffset.snapTo(dragOffset) }

                                                val enTope = abs(dragOffset) >= maximo * 0.96f
                                                if (enTope && !dioHapticaTope) {
                                                    haptica.tic()
                                                    dioHapticaTope = true
                                                } else if (!enTope && dioHapticaTope) {
                                                    dioHapticaTope = false
                                                }
                                            }
                                        }
                                    } else {
                                        break
                                    }
                                }
                                val maximo = topeMaximo
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
                            }
                        }
                    }
            ) {
                contenidoFila()
            }
        }
    }
}
