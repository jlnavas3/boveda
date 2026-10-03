package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.acercade.PantallaAcercaDeApp
import com.jlnavas3.bovedalocal.ui.pantallas.onboarding.PasoBienvenida
import com.jlnavas3.bovedalocal.ui.pantallas.onboarding.PasoCrearContrasena
import com.jlnavas3.bovedalocal.ui.pantallas.onboarding.PasoForjando
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla orquestadora del flujo de creación de la primera bóveda.
 * Gestiona la transición entre pasos:
 * - Paso 0: Bienvenida y pilares de seguridad ([PasoBienvenida]).
 * - Paso 1: Creación de contraseña maestra y perfil KDF ([PasoCrearContrasena]).
 * - Paso 2: Animación de forjado criptográfico ([PasoForjando]).
 * - Vista superpuesta: Acerca de la aplicación ([PantallaAcercaDeApp]).
 */
@Composable
fun PantallaOnboarding(vm: VaultViewModel, actividad: FragmentActivity) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes = vm.repositorio.ajustes.actual
    var perfilSeleccionado by remember {
        mutableStateOf(PerfilArgon2.desde(ajustes.perfilArgon2))
    }
    var paso by remember { mutableIntStateOf(0) }
    var contrasenaMaestra by remember { mutableStateOf("") }
    var mostrandoAcercaDe by remember { mutableStateOf(false) }

    BackHandler(enabled = mostrandoAcercaDe) {
        mostrandoAcercaDe = false
    }

    BackHandler(enabled = !mostrandoAcercaDe && paso == 1) {
        paso = 0
    }

    if (mostrandoAcercaDe) {
        PantallaAcercaDeApp(
            alVolver = {
                haptica.toque()
                mostrandoAcercaDe = false
            }
        )
        return
    }

    AnimatedContent(
        targetState = paso,
        transitionSpec = {
            (scaleIn(spring(dampingRatio = 0.6f), initialScale = 0.94f) + fadeIn(spring(dampingRatio = 0.6f))) togetherWith
                fadeOut(spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium))
        },
        label = "onboarding"
    ) { actual ->
        when (actual) {
            0 -> PasoBienvenida(
                ajustes = ajustes,
                alIniciarCreacion = {
                    haptica.toque()
                    paso = 1
                },
                alAbrirAcercaDe = {
                    haptica.toque()
                    mostrandoAcercaDe = true
                },
                alSalir = {
                    actividad.finishAffinity()
                }
            )

            1 -> PasoCrearContrasena(
                perfilSeleccionado = perfilSeleccionado,
                alCambiarPerfil = { nuevo ->
                    perfilSeleccionado = nuevo
                    vm.repositorio.ajustes.actualizar { it.copy(perfilArgon2 = nuevo.clave) }
                },
                alConfirmar = { pass ->
                    haptica.toque()
                    contrasenaMaestra = pass
                    paso = 2
                },
                alVolver = {
                    haptica.toque()
                    paso = 0
                },
                alAbrirAcercaDe = {
                    haptica.toque()
                    mostrandoAcercaDe = true
                }
            )

            else -> PasoForjando(
                perfil = perfilSeleccionado,
                ajustes = ajustes,
                alIniciarForja = {
                    vm.crearBoveda(contrasenaMaestra) { haptica.exito() }
                }
            )
        }
    }
}
