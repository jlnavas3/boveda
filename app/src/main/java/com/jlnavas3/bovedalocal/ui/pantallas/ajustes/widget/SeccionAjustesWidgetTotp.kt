package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun SeccionAjustesWidgetTotp(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ComponenteGrupo(
            etiqueta = "Respuesta táctil",
            idGrupo = "03.3.G2",
            mostrarId = ajustes.mostrarIdsAjustes,
            descripcion = "Vibración háptica al tocar una cuenta para copiar el código TOTP"
        ) {
            ComponenteSwitch(
                titulo = "Vibración al pulsar",
                icono = Icons.Filled.Vibration,
                colorIcono = ColorIconosInternos,
                idFila = "03.3.4",
                mostrarId = ajustes.mostrarIdsAjustes,
                activo = ajustes.widgetHaptica,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidgetHaptica(it)
                }
            )

            if (ajustes.widgetHaptica) {
                ComponenteSeparador()

                ComponenteSlider(
                    titulo = "Intensidad de vibración",
                    valor = ajustes.widgetHapticaIntensidad,
                    valorTexto = "${(ajustes.widgetHapticaIntensidad * 100).roundToInt()}%",
                    rango = 0.01f..1.0f,
                    pasos = 99,
                    etiquetaMin = "1% (Mínima)",
                    etiquetaMax = "100%",
                    idFila = "03.3.5",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    icono = Icons.Filled.Vibration,
                    colorIcono = ColorIconosInternos,
                    alCambiar = {
                        vm.ajustarWidgetHapticaIntensidad(it)
                        haptica.probar(it)
                    }
                )

                ComponenteSeparador()

                ComponenteBotonFila(
                    titulo = "Probar vibración del widget",
                    icono = Icons.Filled.Vibration,
                    colorIcono = ColorIconosInternos,
                    alPulsar = {
                        haptica.probar(ajustes.widgetHapticaIntensidad)
                    }
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        ComponenteGrupo {
            ComponenteBotonFila(
                titulo = "Restablecer valores del widget 2FA",
                icono = Icons.AutoMirrored.Filled.RotateLeft,
                colorIcono = ColorIconosInternos,
                alPulsar = {
                    haptica.exito()
                    vm.restablecerAjustesWidget()
                    vm.avisar("Ajustes del widget 2FA restablecidos")
                }
            )
        }
    }
}
