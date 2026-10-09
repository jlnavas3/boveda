package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad

@Composable
fun GrupoBloqueoApp(
    autoBloqueoSegundos: Int,
    proteccionPantalla: Boolean,
    mostrarIdsAjustes: Boolean,
    alAjustarAutoBloqueo: (Int) -> Unit,
    alCambiarProteccionPantalla: (Boolean) -> Unit,
    alRestablecer: () -> Unit,
    modifier: Modifier = Modifier,
    proteccionTapjacking: Boolean = true,
    alCambiarProteccionTapjacking: ((Boolean) -> Unit)? = null
) {
    ComponenteGrupo(
        etiqueta = "Bloqueo de aplicación",
        icono = Icons.Filled.Lock,
        colorIcono = ColorSeguridad,
        alRestablecer = alRestablecer,
        idGrupo = "01-SEG-BIO-G02",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        val opcionesAutoBloqueo = remember {
            AlmacenAjustes.OPCIONES_AUTO_BLOQUEO.map { (valor, etiqueta) ->
                val desc = when (valor) {
                    0 -> "Bloquea la bóveda en cuanto sales de la aplicación"
                    15, 30 -> "Ideal si consultas claves frecuentemente"
                    60, 120 -> "Equilibrio estándar entre comodidad y protección"
                    300, 600 -> "Mayor margen de tiempo para sesiones largas"
                    else -> "La aplicación no se bloqueará por inactividad"
                }
                OpcionSelectorModal(valor, etiqueta, etiqueta, desc, Icons.Filled.Lock)
            }
        }
        ComponenteSelectorModal(
            titulo = "Bloqueo por inactividad",
            descripcionModal = "Tiempo transcurrido en segundo plano antes de requerir autenticación",
            icono = null,
            idFila = "01-SEG-BIO-TIM",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = autoBloqueoSegundos,
            opciones = opcionesAutoBloqueo,
            alSeleccionar = alAjustarAutoBloqueo
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteSwitch(
            titulo = "Protección de pantalla (FLAG_SECURE)",
            icono = null,
            activo = proteccionPantalla,
            idFila = "01-SEG-BIO-SEC",
            mostrarId = mostrarIdsAjustes,
            alCambiar = alCambiarProteccionPantalla
        )

        if (alCambiarProteccionTapjacking != null) {
            ComponenteSeparador(sangriaInicio = 16.dp)

            ComponenteSwitch(
                titulo = "Protección contra tapjacking",
                icono = null,
                activo = proteccionTapjacking,
                idFila = "01-SEG-BIO-TAP",
                mostrarId = mostrarIdsAjustes,
                alCambiar = alCambiarProteccionTapjacking
            )
        }
    }
}
