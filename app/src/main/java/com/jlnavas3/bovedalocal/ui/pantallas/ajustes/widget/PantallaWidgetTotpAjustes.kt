package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun PantallaWidgetTotpAjustes(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    ContenedorPrincipal(
        titulo = "Widget 2FA",
        idEtiqueta = "04-HER-WGT-TOT",
        mostrarId = ajustes.mostrarIdsAjustes,
        alVolver = { vm.volverAtras() }
    ) {
        ComponenteGrupo(
            etiqueta = "Respuesta táctil",
            icono = Icons.Filled.Vibration,
            colorIcono = Color2FA,
            alRestablecer = {
                haptica.tic()
                vm.ajustarWidgetHaptica(AjustesDefaults.WidgetTotp.HAPTICA)
                vm.ajustarWidgetHapticaIntensidad(AjustesDefaults.WidgetTotp.HAPTICA_INTENSIDAD)
                vm.avisar("Ajustes de respuesta táctil restablecidos")
            },
            idGrupo = "04-HER-WGT-TOT-G01",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSwitch(
                titulo = "Vibración al pulsar",
                icono = null,
                activo = ajustes.widgetHaptica,
                idFila = "04-HER-WGT-TOT-SWT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidgetHaptica(it)
                }
            )

            if (ajustes.widgetHaptica) {
                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteSlider(
                    titulo = "Intensidad de vibración",
                    icono = null,
                    valor = ajustes.widgetHapticaIntensidad,
                    valorTexto = "${(ajustes.widgetHapticaIntensidad * 100).roundToInt()}%",
                    rango = 0.01f..1.0f,
                    pasos = 99,
                    idFila = "04-HER-WGT-TOT-HIN",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        vm.ajustarWidgetHapticaIntensidad(AjustesDefaults.WidgetTotp.HAPTICA_INTENSIDAD)
                        Haptica.vibrarExterno(
                            context = contexto,
                            activo = true,
                            intensidad = AjustesDefaults.WidgetTotp.HAPTICA_INTENSIDAD
                        )
                    },
                    alCambiar = {
                        vm.ajustarWidgetHapticaIntensidad(it)
                        Haptica.vibrarExterno(
                            context = contexto,
                            activo = true,
                            intensidad = it
                        )
                    }
                )

                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteBotonFila(
                    titulo = "Probar vibración del widget",
                    icono = null,
                    ejecutarHapticaAlPulsar = false,
                    alPulsar = {
                        Haptica.vibrarExterno(
                            context = contexto,
                            activo = true,
                            intensidad = ajustes.widgetHapticaIntensidad
                        )
                    }
                )
            }
        }
    }
}
