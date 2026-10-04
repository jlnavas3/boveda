package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.camara.EstadoCamara
import com.jlnavas3.bovedalocal.camara.MotorCamara
import com.jlnavas3.bovedalocal.camara.MotorCameraX
import com.jlnavas3.bovedalocal.util.Diagnostico

/**
 * Conmuta entre la vista de cámara (CameraX o compatible) y dibuja el visor con máscara angular.
 */
@Composable
fun VisorCamaraEnfoque(
    motorActual: MotorCamara,
    motorPreferido: MotorCamara,
    codigoDetectado: Boolean,
    alFrame: (ByteArray, Int, Int) -> Unit,
    alConmutarMotorCompatible: () -> Unit,
    alErrorCamara: (String) -> Unit,
    alMotorListo: (MotorCameraX) -> Unit,
    modifier: Modifier = Modifier
) {
    key(motorActual) {
        if (motorActual == MotorCamara.COMPATIBLE) {
            VistaLegado(
                alFrame = alFrame,
                alEstado = { estado ->
                    if (estado is EstadoCamara.Fallo) {
                        alErrorCamara(estado.motivo)
                    }
                },
                modifier = modifier.fillMaxSize()
            )
        } else {
            VistaCameraX(
                alFrame = alFrame,
                alEstado = { estado ->
                    if (estado is EstadoCamara.Fallo && motorActual == MotorCamara.CAMERAX && motorPreferido == MotorCamara.AUTOMATICO) {
                        Diagnostico.apuntar("camara", "VisorCamaraEnfoque: CameraX falló; conmutando al motor compatible")
                        alConmutarMotorCompatible()
                    } else if (estado is EstadoCamara.Fallo) {
                        alErrorCamara(estado.motivo)
                    }
                },
                alMotorListo = alMotorListo,
                modifier = modifier.fillMaxSize()
            )
        }
    }

    VisorMascaraQr(
        codigoDetectado = codigoDetectado,
        modifier = modifier.fillMaxSize()
    )
}
