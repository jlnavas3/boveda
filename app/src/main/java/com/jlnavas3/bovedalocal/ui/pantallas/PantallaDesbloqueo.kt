package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LockOpen
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.ui.FlujoBiometria
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.EngranajesBoveda
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.componentes.aEngranajesConfig
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaDesbloqueo(vm: VaultViewModel, actividad: FragmentActivity) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var contrasena by remember { mutableStateOf("") }
    var mostrar by remember { mutableStateOf(false) }
    var abriendo by remember { mutableStateOf(false) }
    var mensajeBiometria by remember { mutableStateOf<String?>(null) }
    var fallos by remember { mutableIntStateOf(0) }
    val sacudida = remember { Animatable(0f) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val flujo = remember { FlujoBiometria(actividad, vm.repositorio) }

    // Se pregunta cada vez que la pantalla vuelve a primer plano, no una sola vez: un sensor
    // ocupado por otra app o una huella recién registrada cambian la respuesta.
    var biometriaUsable by remember { mutableStateOf(false) }
    LifecycleResumeEffect(ajustes.biometriaActiva, ajustes.biometriaModo) {
        biometriaUsable = flujo.disponible()
        onPauseOrDispose { }
    }

    fun lanzarBiometria() {
        mensajeBiometria = null
        val compatible = flujo.modoActivo == BiometricKeyStore.Modo.COMPATIBLE
        flujo.desbloquear(
            titulo = "Abrir Bóveda local",
            subtitulo = if (compatible) "Confirma con tu huella o con el PIN del móvil" else "Usa tu huella para descifrar la clave maestra",
            alClave = { clave ->
                abriendo = true
                haptica.exito()
                vm.desbloquearConClave(clave) { correcto -> if (!correcto) abriendo = false }
            },
            alFallo = { fallo ->
                if (fallo.cambiaDisponibilidad) biometriaUsable = flujo.disponible()
                mensajeBiometria = fallo.texto
                if (fallo.texto != null) haptica.error()
            },
            alIntentoFallido = { haptica.error() }
        )
    }

    var biometriaLanzada by remember { mutableStateOf(false) }
    LaunchedEffect(biometriaUsable) {
        if (biometriaUsable && !biometriaLanzada) {
            biometriaLanzada = true
            lanzarBiometria()
        }
    }

    fun ejecutarDesbloqueo() {
        if (contrasena.isEmpty() || abriendo) return
        vm.desbloquear(contrasena) { correcto ->
            if (correcto) {
                abriendo = true
                haptica.exito()
            } else {
                haptica.error()
                fallos++
                contrasena = ""
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (ajustes.animacionDesbloqueo == "engranajes") {
            EngranajesBoveda(
                abierta = abriendo,
                modifier = Modifier.fillMaxSize(),
                config = ajustes.aEngranajesConfig()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (ajustes.animacionDesbloqueo != "engranajes") {
                PuertaBoveda(abierta = abriendo, tamano = 180)
            } else {
                Spacer(Modifier.height(250.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(18.dp))
                Text(
                    text = "Bóveda cerrada",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = ColorTitulos
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Todo sigue cifrado en este dispositivo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))

                // Tarjeta de autenticación agrupada estilo Samsung One UI / Honor MagicOS
                ComponenteGrupo(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset { IntOffset(sacudida.value.toInt(), 0) },
                    etiqueta = "Acceso a la bóveda",
                    descripcion = mensajeBiometria,
                    descripcionComoPie = true
                ) {
                    Box(modifier = Modifier.padding(14.dp)) {
                        ComponenteCampoTexto(
                            valor = contrasena,
                            etiqueta = "Contraseña maestra",
                            alCambiar = { if (!abriendo) contrasena = it },
                            tipo = TipoCampoTexto.CONTRASENA,
                            mostrarIcono = true,
                            colorIcono = ColorAcento,
                            mostrarContrasena = mostrar,
                            alAlternarMostrarContrasena = { if (!abriendo) mostrar = !mostrar },
                            monoespaciada = mostrar,
                            imeAction = ImeAction.Done,
                            keyboardActions = KeyboardActions(onDone = { ejecutarDesbloqueo() }),
                            readOnly = abriendo
                        )
                    }

                    ComponenteSeparador(sangriaInicio = 16.dp)

                    val botonActivo = contrasena.isNotEmpty() && !abriendo

                    ComponenteBotonFila(
                        titulo = if (abriendo) "Abriendo..." else "Abrir bóveda",
                        alPulsar = { ejecutarDesbloqueo() },
                        icono = Icons.Filled.LockOpen,
                        colorIcono = if (botonActivo) ColorAcento else ColorAjusteGris.copy(alpha = 0.35f),
                        colorTinteIcono = if (botonActivo) ColorSobreAcento else ColorAjusteGris,
                        habilitado = botonActivo
                    )

                    if (biometriaUsable) {
                        ComponenteSeparador(sangriaInicio = 60.dp)
                        ComponenteBotonFila(
                            titulo = flujo.etiquetaBoton(),
                            alPulsar = { lanzarBiometria() },
                            icono = Icons.Filled.Fingerprint,
                            colorIcono = ColorAcento,
                            colorTinteIcono = ColorSobreAcento,
                            habilitado = !abriendo
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(fallos) {
        if (fallos > 0) {
            sacudida.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 260
                    0f at 0
                    -14f at 40
                    12f at 90
                    -8f at 140
                    5f at 190
                    0f at 260
                }
            )
        }
    }
}
