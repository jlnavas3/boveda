package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad

/**
 * Grupo de configuración para el freno temporal y protección contra ataques de fuerza bruta.
 */
@Composable
fun GrupoFrenoFuerzaBruta(
    frenoIntentosGratis: Int,
    frenoSegundosMax: Long,
    mostrarIdsAjustes: Boolean,
    alAjustarIntentos: (Int) -> Unit,
    alAjustarMaxTiempo: (Long) -> Unit,
    alRestablecer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Protección contra fuerza bruta",
        icono = Icons.Filled.Shield,
        colorIcono = ColorSeguridad,
        alRestablecer = alRestablecer,
        idGrupo = "01-SEG-BIO-G05",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        val opcionesIntentos = remember {
            AlmacenAjustes.OPCIONES_FRENO_INTENTOS.map { (valor, etiqueta) ->
                val desc = when (valor) {
                    3 -> "Penalización rápida ante errores continuos"
                    5 -> "Margen estándar antes de iniciar la espera"
                    10 -> "Mayor tolerancia para evitar bloqueos por descuidos"
                    else -> "No impone tiempos de espera al equivocarse"
                }
                OpcionSelectorModal(
                    valor = valor,
                    etiquetaFila = etiqueta,
                    etiquetaModal = etiqueta,
                    descripcionModal = desc,
                    icono = Icons.Filled.LockClock
                )
            }
        }

        val opcionesMaxTiempo = remember {
            AlmacenAjustes.OPCIONES_FRENO_MAX_TIEMPO.map { (valor, etiqueta) ->
                val desc = "Límite máximo acumulable de espera exponencial"
                OpcionSelectorModal(
                    valor = valor,
                    etiquetaFila = etiqueta,
                    etiquetaModal = etiqueta,
                    descripcionModal = desc,
                    icono = Icons.Filled.HourglassBottom
                )
            }
        }

        ComponenteSelectorModal(
            titulo = "Intentos antes de bloqueo",
            descripcionModal = "Número de intentos fallidos permitidos antes de activar la espera exponencial",
            icono = null,
            idFila = "01-SEG-BIO-TRY",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = frenoIntentosGratis,
            opciones = opcionesIntentos,
            alSeleccionar = alAjustarIntentos
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteSelectorModal(
            titulo = "Tiempo máximo de espera",
            descripcionModal = "Tope máximo de penalización tras múltiples intentos erróneos consecutivos",
            icono = null,
            idFila = "01-SEG-BIO-MAX",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = frenoSegundosMax,
            opciones = opcionesMaxTiempo,
            alSeleccionar = alAjustarMaxTiempo
        )
    }
}

@BovedaPreview
@Composable
private fun GrupoFrenoFuerzaBrutaPreview() {
    BovedaTheme {
        GrupoFrenoFuerzaBruta(
            frenoIntentosGratis = 5,
            frenoSegundosMax = 300L,
            mostrarIdsAjustes = false,
            alAjustarIntentos = {},
            alAjustarMaxTiempo = {},
            alRestablecer = {}
        )
    }
}
