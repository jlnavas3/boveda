package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad

@Composable
fun GrupoPrivacidadPortapapeles(
    proteccionPantalla: Boolean,
    portapapelesSegundos: Int,
    mostrarIdsAjustes: Boolean,
    alCambiarProteccionPantalla: (Boolean) -> Unit,
    alAjustarPortapapeles: (Int) -> Unit,
    alRestablecer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Privacidad y portapapeles",
        idGrupo = "01.1.G2",
        mostrarId = mostrarIdsAjustes,
        descripcion = "Bloqueo de capturas de pantalla y borrado automático de claves copiadas",
        modifier = modifier
    ) {
        ComponenteSwitch(
            titulo = "Protección de pantalla (FLAG_SECURE)",
            icono = Icons.Filled.Shield,
            colorIcono = Color(0xFFFB8C00),
            activo = proteccionPantalla,
            idFila = "01.1.5",
            mostrarId = mostrarIdsAjustes,
            alCambiar = alCambiarProteccionPantalla
        )

        ComponenteSeparador()

        val opcionesPortapapeles = remember {
            AlmacenAjustes.OPCIONES_PORTAPAPELES.map { (valor, etiqueta) ->
                val desc = when (valor) {
                    0 -> "Las contraseñas copiadas permanecerán en el portapapeles"
                    10, 15, 30 -> "Recomendado para evitar filtraciones por otras apps"
                    else -> "Borrado automático diferido de credenciales copiadas"
                }
                OpcionSelectorModal(valor, etiqueta, etiqueta, desc, Icons.Filled.Timer)
            }
        }
        ComponenteSelectorModal(
            titulo = "Borrado del portapapeles",
            descripcionModal = "Tiempo tras el cual se limpiará la contraseña copiada en memoria",
            icono = Icons.Filled.Timer,
            colorIcono = ColorSeguridad,
            idFila = "01.1.6",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = portapapelesSegundos,
            opciones = opcionesPortapapeles,
            alSeleccionar = alAjustarPortapapeles
        )

        ComponenteSeparador()

        ComponenteBotonFila(
            titulo = "Restablecer grupo",
            alPulsar = alRestablecer
        )
    }
}
