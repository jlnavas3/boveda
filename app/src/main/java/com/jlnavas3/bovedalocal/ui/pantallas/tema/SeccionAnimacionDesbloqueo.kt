package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionAnimacionDesbloqueo(
    ajustes: AjustesApp,
    haptica: Haptica,
    alCambiarAnimacion: (String) -> Unit,
    alIrACalibracion: () -> Unit
) {
    ComponenteGrupo(
        etiqueta = "Animación de pantalla bloqueada",
        idGrupo = "03.2.G2",
        mostrarId = ajustes.mostrarIdsAjustes
    ) {
        ComponenteRadio(
            titulo = "Mecanismo de engranajes",
            icono = Icons.Filled.Memory,
            colorIcono = Color(0xFF5C6BC0),
            seleccionado = ajustes.animacionDesbloqueo == "engranajes",
            idFila = "03.2.4",
            mostrarId = ajustes.mostrarIdsAjustes,
            alSeleccionar = {
                haptica.tic()
                alCambiarAnimacion("engranajes")
            }
        )
        ComponenteSeparador()
        ComponenteRadio(
            titulo = "Puerta de bóveda",
            icono = Icons.Filled.Security,
            colorIcono = Color(0xFF1E88E5),
            seleccionado = ajustes.animacionDesbloqueo != "engranajes",
            idFila = "03.2.5",
            mostrarId = ajustes.mostrarIdsAjustes,
            alSeleccionar = {
                haptica.tic()
                alCambiarAnimacion("puerta")
            }
        )
        ComponenteSeparador()
        ComponenteBotonFila(
            titulo = "Calibración",
            icono = Icons.Filled.Tune,
            colorIcono = ColorIconosInternos,
            idFila = "03.2.6",
            mostrarId = ajustes.mostrarIdsAjustes,
            alPulsar = {
                haptica.tic()
                alIrACalibracion()
            }
        )
    }
}
