package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
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
        icono = Icons.Filled.CropSquare,
        colorIcono = ColorExportacion,
        idGrupo = "04-HER-WGT-1X1-G03",
        mostrarId = ajustes.mostrarIdsAjustes,
        alRestablecer = {
            haptica.tic()
            vm.restablecerAspectoWidget1x1()
        }
    ) {
        ComponenteSlider(
            titulo = "Grosor del borde",
            icono = null,
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
            idFila = "04-HER-WGT-1X1-GRO",
            mostrarId = ajustes.mostrarIdsAjustes
        )
        ComponenteSeparador(sangriaInicio = 16.dp)
        ComponenteSlider(
            titulo = "Radio de esquinas",
            icono = null,
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
            idFila = "04-HER-WGT-1X1-CRV",
            mostrarId = ajustes.mostrarIdsAjustes
        )
    }

    Spacer(Modifier.height(14.dp))

    // Transparencia del fondo
    ComponenteGrupo(
        etiqueta = "Transparencia del fondo",
        icono = Icons.Filled.LineWeight,
        colorIcono = ColorExportacion,
        idGrupo = "04-HER-WGT-1X1-G04",
        mostrarId = ajustes.mostrarIdsAjustes,
        alRestablecer = {
            haptica.tic()
            vm.ajustarWidget1x1TransparenciaFondo(AjustesDefaults.Widget1x1.TRANSPARENCIA_FONDO)
        }
    ) {
        ComponenteSlider(
            titulo = "Opacidad del fondo",
            icono = null,
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
            idFila = "04-HER-WGT-1X1-TRA",
            mostrarId = ajustes.mostrarIdsAjustes
        )
    }
}
