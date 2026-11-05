/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.biometric.BiometricManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.data.modoBiometriaActivo
import com.jlnavas3.bovedalocal.ui.FlujoBiometria
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoModoCompatible
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Biometria
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla contenedora de Bloqueo y biometría que expone directamente los grupos
 * de Biometría (01-SEG-BIO-G01), Bloqueo de aplicación (01-SEG-BIO-G02)
 * y Protección contra fuerza bruta (01-SEG-BIO-G05).
 */
@Composable
fun PantallaBloqueoBiometria(
    vm: VaultViewModel,
    actividad: FragmentActivity,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val esSenuelo = vm.repositorio.esModoSenuelo

    val flujo = remember { FlujoBiometria(actividad, vm.repositorio) }
    var capacidad by remember { mutableStateOf(Biometria.capacidad(contexto)) }
    LifecycleResumeEffect(Unit) {
        capacidad = Biometria.capacidad(contexto)
        onPauseOrDispose { }
    }
    val nivel = Biometria.decidirNivel(capacidad)
    val modoActivo = ajustes.modoBiometriaActivo
    var dialogoCompatible by remember { mutableStateOf<String?>(null) }
    var confirmarDesactivarSecure by remember { mutableStateOf(false) }

    fun tratarActivacion(resultado: FlujoBiometria.ResultadoActivacion) {
        when (resultado) {
            is FlujoBiometria.ResultadoActivacion.Activada -> {
                haptica.exito()
                vm.avisar(
                    if (resultado.modo == BiometricKeyStore.Modo.FUERTE) "Huella activada en modo fuerte"
                    else "Huella activada en modo compatible"
                )
            }
            FlujoBiometria.ResultadoActivacion.Cancelada -> vm.avisar("Huella cancelada")
            is FlujoBiometria.ResultadoActivacion.FuerteRota -> {
                haptica.error()
                dialogoCompatible = "Android acepta tu huella, pero el Keystore de este móvil la rechaza al usarla " +
                    "(fallo típico de ROMs personalizadas). Detalle técnico: ${resultado.detalle}."
            }
            is FlujoBiometria.ResultadoActivacion.Error -> {
                haptica.error()
                vm.avisar(resultado.texto)
            }
        }
    }

    fun activarFuerte() = flujo.activar(BiometricKeyStore.Modo.FUERTE, ::tratarActivacion)
    fun ofrecerCompatible(motivo: String) { dialogoCompatible = motivo }

    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Bloqueo y biometría",
                idEtiqueta = "01-SEG-BIO-BLO",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("01-SEG-BIO-G01", "Biometría"),
                            AccionSaltoGrupo("01-SEG-BIO-G02", "Bloqueo de aplicación"),
                            AccionSaltoGrupo("01-SEG-BIO-G05", "Protección contra fuerza bruta")
                        )
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Políticas de acceso, autenticación biométrica y defensa progresiva")
                Spacer(Modifier.height(10.dp))

                // Grupo 1: Biometría (01-SEG-BIO-G01)
                GrupoBiometria(
                    esSenuelo = esSenuelo,
                    biometriaActiva = ajustes.biometriaActiva,
                    nivel = nivel,
                    capacidad = capacidad,
                    modoActivo = modoActivo,
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alCambiarBiometria = { activar ->
                        if (activar) {
                            when (nivel) {
                                Biometria.Nivel.FUERTE -> activarFuerte()
                                Biometria.Nivel.COMPATIBLE -> ofrecerCompatible(Biometria.explicarFaltaDeFuerte(capacidad))
                                Biometria.Nivel.NINGUNO -> vm.avisar("Este móvil no ofrece huella ni PIN utilizables ahora mismo")
                            }
                        } else {
                            flujo.desactivar()
                            haptica.tic()
                            vm.avisar("Huella desactivada")
                        }
                    },
                    alRegistrarHuellaAndroid = {
                        if (!AjustesSistema.abrirRegistroHuella(contexto)) {
                            vm.avisar("No se puede abrir los ajustes de huella de este móvil")
                        }
                    },
                    alOfrecerCompatible = {
                        dialogoCompatible = "Tu móvil no garantiza el estándar fuerte de Android para huella dactilar. " +
                            "Puedes activar el modo compatible: la huella se comprobará igual, pero la clave criptográfica se " +
                            "protegerá solo con la contraseña maestra en memoria."
                    },
                    alActivarFuerte = { activarFuerte() }
                )

                Spacer(Modifier.height(EspaciadoComponentes))

                // Grupo 2: Bloqueo de aplicación (01-SEG-BIO-G02)
                GrupoBloqueoApp(
                    autoBloqueoSegundos = ajustes.autoBloqueoSegundos,
                    proteccionPantalla = ajustes.proteccionPantalla,
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alAjustarAutoBloqueo = { valor ->
                        haptica.tic()
                        vm.ajustarAutoBloqueo(valor)
                    },
                    alCambiarProteccionPantalla = { activar ->
                        if (activar) {
                            vm.ajustarProteccionPantalla(true)
                            haptica.tic()
                        } else {
                            confirmarDesactivarSecure = true
                        }
                    },
                    proteccionTapjacking = ajustes.proteccionTapjacking,
                    alCambiarProteccionTapjacking = { activar ->
                        haptica.tic()
                        vm.ajustarProteccionTapjacking(activar)
                    },
                    alRestablecer = {
                        haptica.tic()
                        vm.restablecerBloqueoApp()
                        vm.avisar("Valores de bloqueo restablecidos")
                    }
                )

                Spacer(Modifier.height(EspaciadoComponentes))

                // Grupo 3: Protección contra fuerza bruta (01-SEG-BIO-G05)
                GrupoFrenoFuerzaBruta(
                    frenoIntentosGratis = ajustes.frenoIntentosGratis,
                    frenoSegundosMax = ajustes.frenoSegundosMax,
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alAjustarIntentos = { valor ->
                        haptica.tic()
                        vm.ajustarFrenoIntentosGratis(valor)
                    },
                    alAjustarMaxTiempo = { segundos ->
                        haptica.tic()
                        vm.ajustarFrenoSegundosMax(segundos)
                    },
                    alRestablecer = {
                        haptica.tic()
                        vm.restablecerFrenoIntentos()
                        vm.avisar("Protección contra fuerza bruta restablecida")
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    val motivoActual = dialogoCompatible
    if (motivoActual != null) {
        DialogoModoCompatible(
            motivo = motivoActual,
            alConfirmar = {
                dialogoCompatible = null
                flujo.activar(BiometricKeyStore.Modo.COMPATIBLE, ::tratarActivacion)
            },
            alDescartar = { dialogoCompatible = null }
        )
    }

    if (confirmarDesactivarSecure) {
        DialogoDesactivarSecure(
            alConfirmar = {
                confirmarDesactivarSecure = false
                vm.ajustarProteccionPantalla(false)
                haptica.tic()
            },
            alDescartar = { confirmarDesactivarSecure = false }
        )
    }
}

@Preview(name = "Pantalla Bloqueo y Biometría", showBackground = true)
@Composable
private fun PantallaBloqueoBiometriaPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
                .padding(16.dp)
        ) {
            DescripcionPantalla(subtitulo = "Políticas de acceso, autenticación biométrica y defensa progresiva")
        }
    }
}
