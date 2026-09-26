package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widgettotp

import androidx.compose.foundation.layout.Column
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
fun SeccionFormaYTransparenciaWidgetTotp(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Forma y bordes
        ComponenteGrupo(
            etiqueta = "Forma y bordes",
            idGrupo = "03.3.G1",
            mostrarId = ajustes.mostrarIdsAjustes,
            descripcion = "Grosor de trazo exterior y radio de redondeo"
        ) {
            ComponenteSlider(
                titulo = "Grosor del borde",
                valor = ajustes.widgetGrosorBordeDp,
                valorTexto = if (ajustes.widgetGrosorBordeDp <= 0.1f) "0 dp" else "%.1f dp".format(ajustes.widgetGrosorBordeDp),
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidgetGrosorBorde(it)
                },
                rango = 0f..5f,
                pasos = 49,
                etiquetaMin = "Sin borde",
                etiquetaMax = "5 dp",
                idFila = "03.3.1",
                mostrarId = ajustes.mostrarIdsAjustes,
                icono = Icons.Filled.LineWeight,
                colorIcono = ColorIconosInternos
            )
            ComponenteSeparador()
            ComponenteSlider(
                titulo = "Radio de esquinas",
                valor = ajustes.widgetCurvaturaEsquinasDp,
                valorTexto = if (ajustes.widgetCurvaturaEsquinasDp <= 0.1f) "0 dp" else "%.0f dp".format(ajustes.widgetCurvaturaEsquinasDp),
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidgetCurvaturaEsquinas(it)
                },
                rango = 0f..32f,
                pasos = 31,
                etiquetaMin = "Recto",
                etiquetaMax = "32 dp",
                idFila = "03.3.2",
                mostrarId = ajustes.mostrarIdsAjustes,
                icono = Icons.Filled.CropSquare,
                colorIcono = ColorIconosInternos
            )
        }

        Spacer(Modifier.height(16.dp))

        // Transparencia del fondo
        ComponenteGrupo(
            etiqueta = "Transparencia del fondo",
            idGrupo = "03.3.G2",
            mostrarId = ajustes.mostrarIdsAjustes,
            descripcion = "Nivel de translucidez para combinar con tu fondo de pantalla"
        ) {
            ComponenteSlider(
                titulo = "Opacidad del fondo",
                valor = ajustes.widgetTransparenciaFondo,
                valorTexto = "${(ajustes.widgetTransparenciaFondo * 100).roundToInt()}%",
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidgetTransparenciaFondo(it)
                },
                rango = 0f..1f,
                pasos = 99,
                etiquetaMin = "0%",
                etiquetaMax = "100%",
                idFila = "03.3.3",
                mostrarId = ajustes.mostrarIdsAjustes,
                icono = Icons.Filled.Opacity,
                colorIcono = ColorIconosInternos
            )
        }
    }
}
