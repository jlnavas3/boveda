package com.jlnavas3.bovedalocal.ui.pantallas

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
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.DialogoDesactivarSecure
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.GrupoBiometria
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.GrupoBloqueoApp
import com.jlnavas3.bovedalocal.ui.pantallas.seguridad.GrupoPortapapeles
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
                idEtiqueta = "01.1",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("01.1.G1", "Biometría"),
                            AccionSaltoGrupo("01.1.G2", "Bloqueo de aplicación"),
                            AccionSaltoGrupo("01.1.G3", "Portapapeles")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.ajustarAutoBloqueo(60)
                            vm.ajustarProteccionPantalla(true)
                            vm.ajustarPortapapeles(30)
                            vm.avisar("Ajustes de biometría restablecidos")
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Autenticación, bloqueo automático y portapapeles")
                Spacer(Modifier.height(10.dp))

                // Grupo 1: Biometría
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
                        if (!AjustesSistema.abrirRegistroHuella(contexto)) vm.avisar("No se puede abrir los ajustes de huella de este móvil")
                    },
                    alOfrecerCompatible = {
                        ofrecerCompatible("Si la huella falla en este dispositivo aunque Android la acepte, el modo compatible suele funcionar.")
                    },
                    alActivarFuerte = { activarFuerte() }
                )

                Spacer(Modifier.height(18.dp))

                // Grupo 2: Bloqueo de aplicación
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
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarAutoBloqueo(60)
                        vm.ajustarProteccionPantalla(true)
                        vm.avisar("Valores de bloqueo restablecidos")
                    }
                )

                Spacer(Modifier.height(18.dp))

                // Grupo 3: Portapapeles
                GrupoPortapapeles(
                    portapapelesSegundos = ajustes.portapapelesSegundos,
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alAjustarPortapapeles = { valor ->
                        haptica.tic()
                        vm.ajustarPortapapeles(valor)
                    },
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarPortapapeles(30)
                        vm.avisar("Tiempo de portapapeles restablecido a 30s")
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    dialogoCompatible?.let { motivo ->
        DialogoModoCompatible(
            motivo = motivo,
            alDescartar = { dialogoCompatible = null },
            alConfirmar = {
                dialogoCompatible = null
                flujo.activar(BiometricKeyStore.Modo.COMPATIBLE, ::tratarActivacion)
            }
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
