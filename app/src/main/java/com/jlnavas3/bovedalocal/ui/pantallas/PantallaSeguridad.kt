package com.jlnavas3.bovedalocal.ui.pantallas

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.data.modoBiometriaActivo
import com.jlnavas3.bovedalocal.ui.FlujoBiometria
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoModoCompatible
import com.jlnavas3.bovedalocal.ui.theme.ColorArgon2
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
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
                titulo = "Datos biométricos y contraseña",
                idEtiqueta = "01.1",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Protección de acceso, biometría y bloqueo automático")
                Spacer(Modifier.height(10.dp))

                // Grupo 1: Acceso y Biometría
                ComponenteGrupo(
                    etiqueta = "Acceso y biometría",
                    idGrupo = "01.1.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Autenticación por huella dactilar y temporizador de bloqueo de sesión"
                ) {
                    if (!esSenuelo) {
                        ComponenteSwitch(
                            titulo = "Abrir con huella dactilar",
                            icono = Icons.Filled.Fingerprint,
                            colorIcono = Color(0xFF1E88E5),
                            activo = ajustes.biometriaActiva,
                            habilitado = nivel != Biometria.Nivel.NINGUNO || ajustes.biometriaActiva,
                            idFila = "01.1.1",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = { activar ->
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
                            }
                        )

                        if (capacidad.fuerte == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED &&
                            capacidad.debil != BiometricManager.BIOMETRIC_SUCCESS
                        ) {
                            ComponenteSeparador()
                            ComponenteNavegacion(
                                titulo = "Registrar huella en Android",
                                icono = Icons.Filled.Fingerprint,
                                colorIcono = ColorSeguridad,
                                idFila = "01.1.2",
                                mostrarId = ajustes.mostrarIdsAjustes,
                                alPulsar = {
                                    if (!AjustesSistema.abrirRegistroHuella(contexto)) vm.avisar("No se puede abrir los ajustes de huella de este móvil")
                                }
                            )
                        }

                        when {
                            ajustes.biometriaActiva && modoActivo == BiometricKeyStore.Modo.FUERTE && Biometria.hayCompatible(capacidad) -> {
                                ComponenteSeparador()
                                ComponenteNavegacion(
                                    titulo = "Cambiar a modo compatible",
                                    icono = Icons.Filled.Fingerprint,
                                    colorIcono = ColorSeguridad,
                                    idFila = "01.1.3",
                                    mostrarId = ajustes.mostrarIdsAjustes,
                                    alPulsar = {
                                        ofrecerCompatible("Si la huella falla en este dispositivo aunque Android la acepte, el modo compatible suele funcionar.")
                                    }
                                )
                            }
                            ajustes.biometriaActiva && modoActivo == BiometricKeyStore.Modo.COMPATIBLE && Biometria.hayFuerte(capacidad) -> {
                                ComponenteSeparador()
                                ComponenteNavegacion(
                                    titulo = "Volver al modo fuerte",
                                    icono = Icons.Filled.Security,
                                    colorIcono = ColorSeguridad,
                                    idFila = "01.1.3",
                                    mostrarId = ajustes.mostrarIdsAjustes,
                                    alPulsar = {
                                        activarFuerte()
                                    }
                                )
                            }
                        }
                        ComponenteSeparador()
                    }

                    val opcionesAutoBloqueo = remember {
                        AlmacenAjustes.OPCIONES_AUTO_BLOQUEO.map { (valor, etiqueta) ->
                            val desc = when (valor) {
                                0 -> "Bloquea la bóveda en cuanto sales de la aplicación"
                                15, 30 -> "Ideal si consultas claves frecuentemente"
                                60, 120 -> "Equilibrio estándar entre comodidad y protección"
                                300, 600 -> "Mayor margen de tiempo para sesiones largas"
                                else -> "La aplicación no se bloqueará por inactividad"
                            }
                            OpcionSelectorModal(valor, etiqueta, etiqueta, desc, Icons.Filled.Lock)
                        }
                    }
                    ComponenteSelectorModal(
                        titulo = "Tiempo de bloqueo automático",
                        descripcionModal = "Tiempo transcurrido en segundo plano antes de requerir autenticación",
                        icono = Icons.Filled.Lock,
                        colorIcono = ColorSeguridad,
                        idFila = "01.1.4",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.autoBloqueoSegundos,
                        opciones = opcionesAutoBloqueo,
                        alSeleccionar = { valor ->
                            haptica.tic()
                            vm.ajustarAutoBloqueo(valor)
                        }
                    )
                }

                Spacer(Modifier.height(18.dp))

                // Grupo 2: Privacidad y Portapapeles
                ComponenteGrupo(
                    etiqueta = "Privacidad y portapapeles",
                    idGrupo = "01.1.G2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Bloqueo de capturas de pantalla y borrado automático de claves copiadas"
                ) {
                    ComponenteSwitch(
                        titulo = "Protección de pantalla (FLAG_SECURE)",
                        icono = Icons.Filled.Shield,
                        colorIcono = Color(0xFFFB8C00),
                        activo = ajustes.proteccionPantalla,
                        idFila = "01.1.5",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = { activar ->
                            if (activar) {
                                vm.ajustarProteccionPantalla(true)
                                haptica.tic()
                            } else {
                                confirmarDesactivarSecure = true
                            }
                        }
                    )

                    ComponenteSeparador()

                    val opcionesPortapapeles = remember {
                        AlmacenAjustes.OPCIONES_PORTAPAPELES.map { (valor, etiqueta) ->
                            val desc = when (valor) {
                                0 -> "Las contraseñas copiadas permanecerán en el portapapeles"
                                10, 15, 30 -> "Recomendado para evitar filtraciones por otras apps"
                                else -> "Borrado automático diferido de credenciales copiadas"
                            }
                            OpcionSelectorModal(valor, etiqueta, etiqueta, desc, Icons.Filled.Timer)
                        }
                    }
                    ComponenteSelectorModal(
                        titulo = "Borrado del portapapeles",
                        descripcionModal = "Tiempo tras el cual se limpiará la contraseña copiada en memoria",
                        icono = Icons.Filled.Timer,
                        colorIcono = ColorSeguridad,
                        idFila = "01.1.6",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.portapapelesSegundos,
                        opciones = opcionesPortapapeles,
                        alSeleccionar = { valor ->
                            haptica.tic()
                            vm.ajustarPortapapeles(valor)
                        }
                    )

                    ComponenteSeparador()

                    ComponenteBotonFila(
                        titulo = "Restablecer grupo",
                        alPulsar = {
                            vm.ajustarAutoBloqueo(60)
                            vm.ajustarPortapapeles(30)
                            vm.ajustarProteccionPantalla(true)
                        }
                    )
                }

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
        val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()
        val colorDialogo = if (esOscuro) Color(0xFF212023) else Color(0xFFFFFFFF)
        AlertDialog(
            onDismissRequest = { confirmarDesactivarSecure = false },
            containerColor = colorDialogo,
            tonalElevation = 0.dp,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
            title = { Text("¿Desactivar protección de pantalla?", color = TextoPrincipal) },
            text = {
                Text(
                    "Al desactivar FLAG_SECURE, las capturas de pantalla y la vista en aplicaciones recientes estarán permitidas. Tus datos sensibles podrían quedar expuestos ante aplicaciones espía o grabaciones de pantalla.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    confirmarDesactivarSecure = false
                    vm.ajustarProteccionPantalla(false)
                    haptica.tic()
                }) { Text("Desactivar", color = Peligro) }
            },
            dismissButton = {
                TextButton(onClick = { confirmarDesactivarSecure = false }) {
                    Text("Cancelar", color = TextoSecundario)
                }
            }
        )
    }
}
