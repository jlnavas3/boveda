package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionAnimacionDesbloqueo(
    ajustes: AjustesApp,
    haptica: Haptica,
    alCambiarAnimacion: (String) -> Unit,
    alIrACalibracion: () -> Unit
) {
    ComponenteGrupo(
        etiqueta = "PANTALLA DE DESBLOQUEO",
        icono = Icons.Filled.Lock,
        colorIcono = Color(0xFF5C6BC0),
        alRestablecer = { alCambiarAnimacion("engranajes") },
        idGrupo = "02-APA-THM-G02",
        mostrarId = ajustes.mostrarIdsAjustes
    ) {
        ComponenteRadio(
            titulo = "Mecanismo de engranajes",
            icono = null,
            seleccionado = ajustes.animacionDesbloqueo == "engranajes",
            idFila = "02-APA-THM-ENG",
            mostrarId = ajustes.mostrarIdsAjustes,
            alSeleccionar = {
                haptica.tic()
                alCambiarAnimacion("engranajes")
            }
        )
        ComponenteSeparador()
        ComponenteRadio(
            titulo = "Puerta de bóveda",
            icono = null,
            seleccionado = ajustes.animacionDesbloqueo != "engranajes",
            idFila = "02-APA-THM-PRT",
            mostrarId = ajustes.mostrarIdsAjustes,
            alSeleccionar = {
                haptica.tic()
                alCambiarAnimacion("puerta")
            }
        )
        ComponenteSeparador()
        ComponenteNavegacion(
            titulo = "Calibrar animación",
            icono = null,
            idFila = "02-APA-THM-CAL",
            mostrarId = ajustes.mostrarIdsAjustes,
            alPulsar = {
                haptica.tic()
                alIrACalibracion()
            }
        )
    }
}
