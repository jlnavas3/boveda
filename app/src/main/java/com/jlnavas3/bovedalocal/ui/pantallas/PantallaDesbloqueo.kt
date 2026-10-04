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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.ui.FlujoBiometria
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.EngranajesBoveda
import com.jlnavas3.bovedalocal.ui.componentes.aEngranajesConfig
import com.jlnavas3.bovedalocal.ui.pantallas.desbloqueo.CabeceraDesbloqueo
import com.jlnavas3.bovedalocal.ui.pantallas.desbloqueo.TarjetaFormularioDesbloqueo
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

    val esEngranajes = ajustes.animacionDesbloqueo == "engranajes"

    Box(modifier = Modifier.fillMaxSize()) {
        if (esEngranajes) {
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
            CabeceraDesbloqueo(
                abriendo = abriendo,
                tipoAnimacion = ajustes.animacionDesbloqueo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(24.dp))

                TarjetaFormularioDesbloqueo(
                    contrasena = contrasena,
                    alCambiarContrasena = { contrasena = it },
                    mostrarContrasena = mostrar,
                    alAlternarMostrarContrasena = { mostrar = !mostrar },
                    abriendo = abriendo,
                    desplazamientoSacudidaX = sacudida.value,
                    mensajeBiometria = mensajeBiometria,
                    biometriaUsable = biometriaUsable,
                    etiquetaBotonBiometria = flujo.etiquetaBoton(),
                    alDesbloquear = { ejecutarDesbloqueo() },
                    alLanzarBiometria = { lanzarBiometria() }
                )
            }
        }
    }

    LaunchedEffect(fallos) {
        if (fallos > 0) {
            com.jlnavas3.bovedalocal.ui.componentes.dispararSacudidaHorizontal(sacudida)
        }
    }
}
