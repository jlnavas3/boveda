package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun SeccionAspectoWidget1x1(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica
) {
    // Forma y bordes
    ComponenteGrupo(
        etiqueta = "Forma y bordes",
        idGrupo = "03.3.G10",
        mostrarId = ajustes.mostrarIdsAjustes,
        descripcion = "Grosor de trazo perimetral y radio de curvatura de esquinas"
    ) {
        ComponenteSlider(
            titulo = "Grosor del borde",
            valor = ajustes.widget1x1GrosorBordeDp,
            valorTexto = if (ajustes.widget1x1GrosorBordeDp <= 0.1f) "0 dp" else "%.1f dp".format(ajustes.widget1x1GrosorBordeDp),
            alCambiar = {
                haptica.tic()
                vm.ajustarWidget1x1GrosorBorde(it)
            },
            rango = 0f..5f,
            pasos = 49,
            etiquetaMin = "Sin borde",
            etiquetaMax = "5 dp",
            idFila = "03.3.11",
            mostrarId = ajustes.mostrarIdsAjustes,
            icono = Icons.Filled.LineWeight,
            colorIcono = ColorIconosInternos
        )
        ComponenteSeparador()
        ComponenteSlider(
            titulo = "Radio de esquinas",
            valor = ajustes.widget1x1CurvaturaEsquinasDp,
            valorTexto = if (ajustes.widget1x1CurvaturaEsquinasDp <= 0.1f) "0 dp" else "%.0f dp".format(ajustes.widget1x1CurvaturaEsquinasDp),
            alCambiar = {
                haptica.tic()
                vm.ajustarWidget1x1CurvaturaEsquinas(it)
            },
            rango = 0f..32f,
            pasos = 31,
            etiquetaMin = "Recto",
            etiquetaMax = "32 dp",
            idFila = "03.3.12",
            mostrarId = ajustes.mostrarIdsAjustes,
            icono = Icons.Filled.CropSquare,
            colorIcono = ColorIconosInternos
        )
    }

    Spacer(Modifier.height(16.dp))

    // Transparencia del fondo
    ComponenteGrupo(
        etiqueta = "Transparencia del fondo",
        idGrupo = "03.3.G12",
        mostrarId = ajustes.mostrarIdsAjustes,
        descripcion = "Nivel de opacidad o transparencia del fondo del botón"
    ) {
        ComponenteSlider(
            titulo = "Opacidad del fondo",
            valor = ajustes.widget1x1TransparenciaFondo,
            valorTexto = "${(ajustes.widget1x1TransparenciaFondo * 100).roundToInt()}%",
            alCambiar = {
                haptica.tic()
                vm.ajustarWidget1x1TransparenciaFondo(it)
            },
            rango = 0.0f..1.0f,
            pasos = 99,
            etiquetaMin = "0% (Transparente)",
            etiquetaMax = "100% (Sólido)",
            idFila = "03.3.26",
            mostrarId = ajustes.mostrarIdsAjustes,
            icono = Icons.Filled.Opacity,
            colorIcono = ColorIconosInternos
        )
    }
}
