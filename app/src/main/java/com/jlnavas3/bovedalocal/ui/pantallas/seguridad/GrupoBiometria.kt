package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.biometric.BiometricManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.BiometricKeyStore
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.util.Biometria

@Composable
fun GrupoBiometria(
    esSenuelo: Boolean,
    biometriaActiva: Boolean,
    nivel: Biometria.Nivel,
    capacidad: Biometria.Capacidad,
    modoActivo: BiometricKeyStore.Modo?,
    mostrarIdsAjustes: Boolean,
    alCambiarBiometria: (Boolean) -> Unit,
    alRegistrarHuellaAndroid: () -> Unit,
    alOfrecerCompatible: () -> Unit,
    alActivarFuerte: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (esSenuelo) return

    ComponenteGrupo(
        etiqueta = "Biometría",
        icono = Icons.Filled.Fingerprint,
        colorIcono = Color(0xFF1E88E5),
        idGrupo = "01-SEG-BIO-G01",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Desbloqueo con huella",
            icono = null,
            activo = biometriaActiva,
            habilitado = nivel != Biometria.Nivel.NINGUNO || biometriaActiva,
            idFila = "01-SEG-BIO-SWT",
            mostrarId = mostrarIdsAjustes,
            alCambiar = alCambiarBiometria
        )

        if (capacidad.fuerte == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED &&
            capacidad.debil != BiometricManager.BIOMETRIC_SUCCESS
        ) {
            ComponenteSeparador(sangriaInicio = 16.dp)
            ComponenteNavegacion(
                titulo = "Registrar huella en Android",
                icono = null,
                idFila = "01-SEG-BIO-REG",
                mostrarId = mostrarIdsAjustes,
                alPulsar = alRegistrarHuellaAndroid
            )
        }

        when {
            biometriaActiva && modoActivo == BiometricKeyStore.Modo.FUERTE && Biometria.hayCompatible(capacidad) -> {
                ComponenteSeparador(sangriaInicio = 16.dp)
                ComponenteNavegacion(
                    titulo = "Cambiar a modo compatible",
                    icono = null,
                    idFila = "01-SEG-BIO-MOD",
                    mostrarId = mostrarIdsAjustes,
                    alPulsar = alOfrecerCompatible
                )
            }
            biometriaActiva && modoActivo == BiometricKeyStore.Modo.COMPATIBLE && Biometria.hayFuerte(capacidad) -> {
                ComponenteSeparador(sangriaInicio = 16.dp)
                ComponenteNavegacion(
                    titulo = "Volver al modo fuerte",
                    icono = null,
                    idFila = "01-SEG-BIO-MOD",
                    mostrarId = mostrarIdsAjustes,
                    alPulsar = alActivarFuerte
                )
            }
        }
    }
}
