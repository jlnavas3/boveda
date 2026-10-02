package com.jlnavas3.bovedalocal.ui.pantallas.avanzada

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
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
        colorIcono = ColorAcento,
        alRestablecer = {
            haptica.tic()
            alCambiarHapticaApp(AjustesDefaults.Interaccion.HAPTICA_APP)
            alCambiarIntensidad(AjustesDefaults.Interaccion.HAPTICA_APP_INTENSIDAD)
            haptica.probar(AjustesDefaults.Interaccion.HAPTICA_APP_INTENSIDAD)
        },
        idGrupo = "06-SIS-AVZ-G02",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Vibración háptica general al interactuar con botones, switches y controles",
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Vibración háptica en la app",
            icono = null,
            idFila = "06-SIS-AVZ-HAP",
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
                idFila = "06-SIS-AVZ-HIN",
                mostrarId = mostrarIdsAjustes,
                icono = null,
                colorAcento = ColorAcento,
                alCambiar = {
                    alCambiarIntensidad(it)
                    haptica.probar(it)
                }
            )

            ComponenteSeparador(sangriaInicio = 16.dp)

            ComponenteNavegacion(
                titulo = "Probar vibración",
                icono = null,
                idFila = "06-SIS-AVZ-VIB",
                mostrarId = mostrarIdsAjustes,
                alPulsar = {
                    haptica.probar(hapticaAppIntensidad)
                }
            )
        }
    }
}
