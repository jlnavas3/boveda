package com.jlnavas3.bovedalocal.ui.pantallas.senuelo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteAlerta
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoAlerta
import com.jlnavas3.bovedalocal.ui.theme.Peligro

@Composable
fun AlertaBiometriaSenuelo(
    biometriaActiva: Boolean,
    alDesactivarBiometria: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (biometriaActiva) {
        ComponenteAlerta(
            tipo = TipoAlerta.DANGER,
            titulo = "Riesgo de coacción: Huella activada",
            mensaje = "El sensor biométrico de Android abrirá SIEMPRE tu bóveda ORIGINAL (el hardware del móvil no distingue situaciones de coacción física). Si un atacante te fuerza a poner el dedo, se revelarán tus claves reales.\n\nPara garantizar la máxima protección de la Bóveda Señuelo, te recomendamos desactivar el acceso por huella.",
            icono = Icons.Filled.Warning,
            modifier = modifier,
            accion = {
                ComponenteBotonFila(
                    titulo = "Desactivar acceso por huella dactilar",
                    icono = Icons.Filled.Fingerprint,
                    colorIcono = Peligro,
                    alPulsar = alDesactivarBiometria
                )
            }
        )
    } else {
        ComponenteAlerta(
            tipo = TipoAlerta.SUCCESS,
            titulo = "Huella desactivada: Seguridad óptima",
            mensaje = "Solo se puede acceder mediante contraseña/PIN. La bóveda señuelo protegerá tus datos reales frente a coacción física.",
            icono = Icons.Filled.CheckCircle,
            modifier = modifier
        )
    }
}
