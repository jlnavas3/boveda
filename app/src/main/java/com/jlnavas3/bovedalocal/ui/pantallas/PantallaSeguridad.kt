package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.data.modoBiometriaActivo
import com.jlnavas3.bovedalocal.ui.FlujoBiometria
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.ContenidoGruposSeguridad
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.DialogosSeguridad
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Biometria
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaSeguridad(
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

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Biometría",
                idEtiqueta = "01-SEG-BIO",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("01-SEG-BIO-G01", "Biometría"),
                            AccionSaltoGrupo("01-SEG-BIO-G02", "Bloqueo de aplicación"),
                            AccionSaltoGrupo("01-SEG-BIO-G05", "Fuerza bruta"),
                            AccionSaltoGrupo("01-SEG-BIO-G03", "Portapapeles")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.restablecerBloqueoApp()
                            vm.restablecerFrenoIntentos()
                            vm.restablecerPortapapeles()
                            vm.avisar("Ajustes de seguridad restablecidos")
                        }
                    )
                }
            )

            ContenidoGruposSeguridad(
                scrollState = scrollState,
                ajustes = ajustes,
                esSenuelo = esSenuelo,
                nivel = nivel,
                capacidad = capacidad,
                modoActivo = modoActivo,
                vm = vm,
                haptica = haptica,
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
                    ofrecerCompatible("Si la huella falla en este dispositivo aunque Android la acepte, el modo compatible suele funcionar.")
                },
                alActivarFuerte = { activarFuerte() },
                alPedirDesactivarSecure = { confirmarDesactivarSecure = true },
                modifier = Modifier.weight(1f)
            )
        }
    }

    DialogosSeguridad(
        dialogoCompatible = dialogoCompatible,
        confirmarDesactivarSecure = confirmarDesactivarSecure,
        alDescartarCompatible = { dialogoCompatible = null },
        alConfirmarCompatible = {
            dialogoCompatible = null
            flujo.activar(BiometricKeyStore.Modo.COMPATIBLE, ::tratarActivacion)
        },
        alConfirmarDesactivarSecure = {
            confirmarDesactivarSecure = false
            vm.ajustarProteccionPantalla(false)
            haptica.tic()
        },
        alDescartarDesactivarSecure = { confirmarDesactivarSecure = false }
    )
}
