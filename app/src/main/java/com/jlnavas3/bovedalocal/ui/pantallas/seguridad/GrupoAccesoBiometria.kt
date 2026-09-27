package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.biometric.BiometricManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.util.Biometria

@Composable
fun GrupoAccesoBiometria(
    esSenuelo: Boolean,
    biometriaActiva: Boolean,
    nivel: Biometria.Nivel,
    capacidad: Biometria.Capacidad,
    modoActivo: BiometricKeyStore.Modo?,
    mostrarIdsAjustes: Boolean,
    autoBloqueoSegundos: Int,
    alCambiarBiometria: (Boolean) -> Unit,
    alRegistrarHuellaAndroid: () -> Unit,
    alOfrecerCompatible: () -> Unit,
    alActivarFuerte: () -> Unit,
    alAjustarAutoBloqueo: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Acceso y biometría",
        idGrupo = "01.1.G1",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Autenticación por huella dactilar y temporizador de bloqueo de sesión",
        modifier = modifier
    ) {
        if (!esSenuelo) {
            ComponenteSwitch(
                titulo = "Abrir con huella dactilar",
                icono = Icons.Filled.Fingerprint,
                colorIcono = Color(0xFF1E88E5),
                activo = biometriaActiva,
                habilitado = nivel != Biometria.Nivel.NINGUNO || biometriaActiva,
                idFila = "01.1.1",
                mostrarId = mostrarIdsAjustes,
                alCambiar = alCambiarBiometria
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
                    mostrarId = mostrarIdsAjustes,
                    alPulsar = alRegistrarHuellaAndroid
                )
            }

            when {
                biometriaActiva && modoActivo == BiometricKeyStore.Modo.FUERTE && Biometria.hayCompatible(capacidad) -> {
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Cambiar a modo compatible",
                        icono = Icons.Filled.Fingerprint,
                        colorIcono = ColorSeguridad,
                        idFila = "01.1.3",
                        mostrarId = mostrarIdsAjustes,
                        alPulsar = alOfrecerCompatible
                    )
                }
                biometriaActiva && modoActivo == BiometricKeyStore.Modo.COMPATIBLE && Biometria.hayFuerte(capacidad) -> {
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Volver al modo fuerte",
                        icono = Icons.Filled.Security,
                        colorIcono = ColorSeguridad,
                        idFila = "01.1.3",
                        mostrarId = mostrarIdsAjustes,
                        alPulsar = alActivarFuerte
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
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = autoBloqueoSegundos,
            opciones = opcionesAutoBloqueo,
            alSeleccionar = alAjustarAutoBloqueo
        )
    }
}
