package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widgettotp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
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
                vm.ajustarWidgetGrosorBorde(AjustesDefaults.WidgetTotp.GROSOR_BORDE_DP)
                vm.ajustarWidgetCurvaturaEsquinas(AjustesDefaults.WidgetTotp.CURVATURA_ESQUINAS_DP)
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
                vm.ajustarWidgetTransparenciaFondo(AjustesDefaults.WidgetTotp.TRANSPARENCIA_FONDO)
                vm.ajustarWidgetTransparenciaFilas(AjustesDefaults.WidgetTotp.TRANSPARENCIA_FILAS)
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

        Spacer(Modifier.height(14.dp))

        // Vidrio esmerilado
        ComponenteGrupo(
            etiqueta = "Vidrio esmerilado",
            icono = Icons.Filled.AutoAwesome,
            colorIcono = Color2FA,
            idGrupo = "04-HER-WGT-CAL-G03",
            mostrarId = ajustes.mostrarIdsAjustes,
            alRestablecer = {
                haptica.tic()
                vm.ajustarWidgetTotpVidrioEsmerilado(AjustesDefaults.WidgetTotp.VIDRIO_ESMERILADO)
                vm.ajustarWidgetTotpEsmeriladoIntensidad(AjustesDefaults.WidgetTotp.ESMERILADO_INTENSIDAD)
                vm.ajustarWidgetTotpEsmeriladoLuz(AjustesDefaults.WidgetTotp.ESMERILADO_LUZ)
            }
        ) {
            ComponenteSwitch(
                titulo = "Efecto vidrio esmerilado",
                activo = ajustes.widgetTotpVidrioEsmerilado,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidgetTotpVidrioEsmerilado(it)
                },
                idFila = "04-HER-WGT-CAL-ESM",
                mostrarId = ajustes.mostrarIdsAjustes
            )
            if (ajustes.widgetTotpVidrioEsmerilado) {
                ComponenteSeparador(sangriaInicio = 16.dp)
                ComponenteSlider(
                    titulo = "Intensidad del esmerilado",
                    icono = null,
                    valor = ajustes.widgetTotpEsmeriladoIntensidad,
                    valorTexto = "${(ajustes.widgetTotpEsmeriladoIntensidad * 100).roundToInt()}%",
                    alCambiar = {
                        haptica.tic()
                        vm.ajustarWidgetTotpEsmeriladoIntensidad(it)
                    },
                    rango = 0.1f..1.0f,
                    pasos = 89,
                    etiquetaMin = "10%",
                    etiquetaMax = "100%",
                    idFila = "04-HER-WGT-CAL-EINT",
                    mostrarId = ajustes.mostrarIdsAjustes
                )
                ComponenteSeparador(sangriaInicio = 16.dp)
                ComponenteSlider(
                    titulo = "Reflejo de luz cenital",
                    icono = null,
                    valor = ajustes.widgetTotpEsmeriladoLuz,
                    valorTexto = "${(ajustes.widgetTotpEsmeriladoLuz * 100).roundToInt()}%",
                    alCambiar = {
                        haptica.tic()
                        vm.ajustarWidgetTotpEsmeriladoLuz(it)
                    },
                    rango = 0.0f..1.0f,
                    pasos = 99,
                    etiquetaMin = "0%",
                    etiquetaMax = "100%",
                    idFila = "04-HER-WGT-CAL-ELUZ",
                    mostrarId = ajustes.mostrarIdsAjustes
                )
            }
        }
    }
}
