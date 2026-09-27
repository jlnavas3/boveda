package com.jlnavas3.bovedalocal.ui.pantallas.avanzada

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun GrupoRespuestaHaptica(
    mostrarIdsAjustes: Boolean,
    hapticaApp: Boolean,
    hapticaAppIntensidad: Float,
    alCambiarHapticaApp: (Boolean) -> Unit,
    alCambiarIntensidad: (Float) -> Unit,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Respuesta táctil y vibración",
        idGrupo = "05.1.G3",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Vibración háptica general al interactuar con botones, switches y controles",
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Vibración háptica en la app",
            icono = Icons.Filled.Vibration,
            colorIcono = Color(0xFF00897B),
            idFila = "05.1.9",
            mostrarId = mostrarIdsAjustes,
            activo = hapticaApp,
            alCambiar = {
                alCambiarHapticaApp(it)
                if (it) haptica.probar(hapticaAppIntensidad)
            }
        )

        if (hapticaApp) {
            ComponenteSeparador()

            ComponenteSlider(
                titulo = "Intensidad de vibración",
                valor = hapticaAppIntensidad,
                valorTexto = "${(hapticaAppIntensidad * 100).roundToInt()}%",
                rango = 0.01f..1.0f,
                pasos = 99,
                etiquetaMin = "1% (Mínima)",
                etiquetaMax = "100%",
                idFila = "05.1.10",
                mostrarId = mostrarIdsAjustes,
                icono = Icons.Filled.Vibration,
                colorIcono = Color(0xFF00897B),
                colorAcento = Color(0xFF00897B),
                alCambiar = {
                    alCambiarIntensidad(it)
                    haptica.probar(it)
                }
            )

            ComponenteSeparador()

            ComponenteNavegacion(
                titulo = "Probar vibración",
                icono = Icons.Filled.Vibration,
                colorIcono = Color(0xFF00897B),
                idFila = "05.1.11",
                mostrarId = mostrarIdsAjustes,
                alPulsar = {
                    haptica.probar(hapticaAppIntensidad)
                }
            )
        }
    }
}
