package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad

@Composable
fun GrupoPortapapeles(
    portapapelesSegundos: Int,
    mostrarIdsAjustes: Boolean,
    alAjustarPortapapeles: (Int) -> Unit,
    alRestablecer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Portapapeles",
        icono = Icons.Filled.Timer,
        colorIcono = ColorSeguridad,
        alRestablecer = alRestablecer,
        idGrupo = "01-SEG-BIO-G03",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
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
            titulo = "Borrado automático",
            descripcionModal = "Tiempo tras el cual se limpiará la contraseña copiada en memoria",
            icono = null,
            idFila = "01-SEG-BIO-CLP",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = portapapelesSegundos,
            opciones = opcionesPortapapeles,
            alSeleccionar = alAjustarPortapapeles
        )
    }
}
