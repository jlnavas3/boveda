package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud

@Composable
fun GrupoAntiguedadSalud(
    umbralAntiguedadDias: Int,
    mostrarIdsAjustes: Boolean,
    alAjustarUmbral: (Int) -> Unit,
    alRestablecer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ComponenteGrupo(
        etiqueta = "Auditoría de contraseñas",
        icono = Icons.Filled.HealthAndSafety,
        colorIcono = ColorSalud,
        alRestablecer = alRestablecer,
        idGrupo = "01-SEG-BIO-G04",
        mostrarId = mostrarIdsAjustes,
        modifier = modifier
    ) {
        val opcionesUmbral = remember {
            AlmacenAjustes.OPCIONES_UMBRAL_ANTIGUEDAD.map { (valor, etiqueta) ->
                val desc = when (valor) {
                    0 -> "No alertar sobre contraseñas antiguas"
                    180 -> "Recomendado (aviso si no se renueva en 6 meses)"
                    else -> "Aviso si la contraseña supera $etiqueta de antigüedad"
                }
                OpcionSelectorModal(valor, etiqueta, etiqueta, desc, Icons.Filled.History)
            }
        }
        ComponenteSelectorModal(
            titulo = "Umbral de antigüedad",
            descripcionModal = "Tiempo límite sin actualizar antes de clasificar una contraseña como antigua en Salud",
            icono = null,
            idFila = "01-SEG-BIO-ANT",
            mostrarId = mostrarIdsAjustes,
            valorSeleccionado = umbralAntiguedadDias,
            opciones = opcionesUmbral,
            alSeleccionar = alAjustarUmbral
        )
    }
}
