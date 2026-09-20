package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionFormatosCampos(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    haptica: Haptica,
    inicialmenteAbierta: Boolean = false,
    seccionDestino: String? = null
) {
    TarjetaAjuste(
        titulo = "Formatos de campos",
        icono = Icons.Filled.FormatShapes,
        descripcion = "Máscaras de teléfono, fechas, horas y separadores numéricos.",
        colorIcono = ColorSalud,
        inicialmenteAbierta = inicialmenteAbierta || (seccionDestino != null && seccionDestino.startsWith("10")),
        idEtiqueta = "10",
        mostrarId = ajustes.mostrarIdsAjustes
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            "Configura cómo se presentan y formatean los campos de fecha, hora, teléfono y decimales en tus entradas y tarjetas.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(12.dp))
        ComponenteBotonFila(
            titulo = "Personalizar formatos de campos",
            icono = Icons.Filled.FormatShapes,
            colorIcono = ColorSalud,
            idFila = "10",
            mostrarId = ajustes.mostrarIdsAjustes,
            alPulsar = {
                haptica.tic()
                vm.irPorId("10")
            }
        )
    }
}
