package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.camara.MotorCamara
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionCamara(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    haptica: Haptica
) {
    TarjetaAjuste("Cámara del escáner", Icons.Filled.CameraAlt, "Elige cómo se leen los códigos QR de autenticación.") {
        Spacer(Modifier.height(8.dp))
        Text(
            "Automático prueba CameraX y, si falla, pasa solo al motor compatible. Si la imagen sale negra o no lee nada, fuerza el compatible: usa la API antigua de cámara, que funciona hasta en los móviles más raros. Y si nada va, siempre puedes leer el QR desde una captura.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(10.dp))
        SelectorAjuste(
            titulo = "Motor de cámara",
            icono = Icons.Filled.CameraAlt,
            seleccionado = MotorCamara.entries.first { it.clave == ajustes.motorCamara }.etiqueta,
            opciones = MotorCamara.entries.map { motor ->
                OpcionAjuste(motor.clave, motor.etiqueta, Icons.Filled.CameraAlt)
            },
            alSeleccionar = { valor -> haptica.tic(); vm.ajustarMotorCamara(valor) }
        )
    }
}
