package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO
import java.util.Locale

@Composable
fun SeccionCalibracionPuerta(
    ajustes: AjustesApp,
    vm: VaultViewModel
) {
    val contexto = LocalContext.current

    // Grupo 1: Movimiento y geometría
    ComponenteGrupo(
        etiqueta = "Movimiento y geometría",
        icono = Icons.Filled.Speed,
        alRestablecer = {
            vm.restablecerGeometriaPuerta()
            Toast.makeText(contexto, "Geometría de puerta restablecida", Toast.LENGTH_SHORT).show()
        },
        idGrupo = "02-APA-THM-ANI-G03",
        mostrarId = ajustes.mostrarIdsAjustes
    ) {
        ComponenteSlider(
            titulo = "Velocidad de rotación",
            icono = null,
            valor = ajustes.puertaVelocidad,
            valorTexto = "${String.format(Locale.US, "%.1f", ajustes.puertaVelocidad)}x",
            rango = 0.2f..3.0f,
            alCambiar = { vm.ajustarPuertaVelocidad(it) },
            etiquetaMin = "0.2x (Lenta)",
            etiquetaMax = "3.0x (Rápida)"
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSlider(
            titulo = "Grosor de los anillos",
            icono = null,
            valor = ajustes.puertaGrosorAnillos,
            valorTexto = "${String.format(Locale.US, "%.1f", ajustes.puertaGrosorAnillos)}x",
            rango = 0.5f..2.5f,
            alCambiar = { vm.ajustarPuertaGrosorAnillos(it) },
            etiquetaMin = "0.5x (Fino)",
            etiquetaMax = "2.5x (Grueso)"
        )
    }

    Spacer(Modifier.height(14.dp))

    // Grupo 2: Tonalidad cromática
    val colorActualPuerta = if (ajustes.puertaColor.isNotBlank()) {
        parsearColorO(ajustes.puertaColor, ColorAcento)
    } else ColorAcento

    ComponenteGrupo(
        etiqueta = "Color de la puerta de bóveda",
        alRestablecer = {
            vm.restablecerColorPuerta()
        },
        idGrupo = "02-APA-THM-ANI-G04",
        mostrarId = ajustes.mostrarIdsAjustes
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (ajustes.puertaColor.isBlank()) "Color actual: Dorado ámbar predeterminado" else "Color actual: ${ajustes.puertaColor}",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))
            SelectorColorEnTiempoReal(
                colorInicial = colorActualPuerta,
                titulo = "Anillos de la bóveda"
            ) { nuevoColor ->
                vm.ajustarPuertaColor(nuevoColor.aHex())
            }
        }
    }
}
