package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widgettotp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
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
            icono = Icons.Filled.CropSquare,
            colorIcono = Color2FA,
            idGrupo = "04-HER-WGT-CAL-G01",
            mostrarId = ajustes.mostrarIdsAjustes,
            alRestablecer = {
                haptica.tic()
                vm.ajustarWidgetGrosorBorde(0f)
                vm.ajustarWidgetCurvaturaEsquinas(0f)
            }
        ) {
            ComponenteSlider(
                titulo = "Grosor del borde",
                icono = null,
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
                idFila = "04-HER-WGT-CAL-GRO",
                mostrarId = ajustes.mostrarIdsAjustes
            )
            ComponenteSeparador(sangriaInicio = 16.dp)
            ComponenteSlider(
                titulo = "Radio de esquinas",
                icono = null,
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
                idFila = "04-HER-WGT-CAL-CRV",
                mostrarId = ajustes.mostrarIdsAjustes
            )
        }

        Spacer(Modifier.height(14.dp))

        // Transparencia del fondo
        ComponenteGrupo(
            etiqueta = "Transparencia del fondo",
            icono = Icons.Filled.LineWeight,
            colorIcono = Color2FA,
            idGrupo = "04-HER-WGT-CAL-G02",
            mostrarId = ajustes.mostrarIdsAjustes,
            alRestablecer = {
                haptica.tic()
                vm.ajustarWidgetTransparenciaFondo(0.50f)
                vm.ajustarWidgetTransparenciaFilas(0.0f)
            }
        ) {
            ComponenteSlider(
                titulo = "Opacidad del fondo",
                icono = null,
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
                idFila = "04-HER-WGT-CAL-TRA",
                mostrarId = ajustes.mostrarIdsAjustes
            )

            ComponenteSeparador(sangriaInicio = 16.dp)

            ComponenteSlider(
                titulo = "Opacidad de las filas",
                icono = null,
                valor = ajustes.widgetTransparenciaFilas,
                valorTexto = "${(ajustes.widgetTransparenciaFilas * 100).roundToInt()}%",
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidgetTransparenciaFilas(it)
                },
                rango = 0f..1f,
                pasos = 99,
                etiquetaMin = "0% (Transparente)",
                etiquetaMax = "100% (Sólido)",
                idFila = "04-HER-WGT-CAL-FIL",
                mostrarId = ajustes.mostrarIdsAjustes
            )
        }
    }
}
