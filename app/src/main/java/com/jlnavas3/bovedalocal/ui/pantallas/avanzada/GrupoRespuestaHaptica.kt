package com.jlnavas3.bovedalocal.ui.pantallas.avanzada

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        icono = Icons.Filled.Vibration,
        colorIcono = Color(0xFF00897B),
        alRestablecer = {
            haptica.tic()
            alCambiarHapticaApp(true)
            alCambiarIntensidad(0.7f)
            haptica.probar(0.7f)
        },
        idGrupo = "06.1.G3",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Vibración háptica general al interactuar con botones, switches y controles",
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Vibración háptica en la app",
            icono = null,
            idFila = "06.1.8",
            mostrarId = mostrarIdsAjustes,
            activo = hapticaApp,
            alCambiar = {
                alCambiarHapticaApp(it)
                if (it) haptica.probar(hapticaAppIntensidad)
            }
        )

        if (hapticaApp) {
            ComponenteSeparador(sangriaInicio = 16.dp)

            ComponenteSlider(
                titulo = "Intensidad de vibración",
                valor = hapticaAppIntensidad,
                valorTexto = "${(hapticaAppIntensidad * 100).roundToInt()}%",
                rango = 0.01f..1.0f,
                pasos = 99,
                etiquetaMin = "1% (Mínima)",
                etiquetaMax = "100%",
                idFila = "06.1.9",
                mostrarId = mostrarIdsAjustes,
                icono = null,
                colorAcento = Color(0xFF00897B),
                alCambiar = {
                    alCambiarIntensidad(it)
                    haptica.probar(it)
                }
            )

            ComponenteSeparador(sangriaInicio = 16.dp)

            ComponenteNavegacion(
                titulo = "Probar vibración",
                icono = null,
                idFila = "06.1.10",
                mostrarId = mostrarIdsAjustes,
                alPulsar = {
                    haptica.probar(hapticaAppIntensidad)
                }
            )
        }
    }
}
