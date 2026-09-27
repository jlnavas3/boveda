package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun PantallaWidget1x1Comportamiento(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    ContenedorPrincipal(
        titulo = "Comportamiento 1x1",
        alVolver = { vm.volverAtras() }
    ) {
        ComponenteGrupo(
            etiqueta = "Acciones automáticas",
            icono = Icons.Filled.Bolt,
            colorIcono = ColorGenerador,
            alRestablecer = {
                haptica.tic()
                vm.ajustarWidget1x1CopiarPortapapeles(true)
                vm.ajustarWidget1x1MostrarToast(true)
                vm.avisar("Acciones automáticas restablecidas")
            },
            idGrupo = "04.4.4.G1",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSwitch(
                titulo = "Copiar al portapapeles",
                icono = null,
                activo = ajustes.widget1x1CopiarPortapapeles,
                idFila = "04.4.4.1",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidget1x1CopiarPortapapeles(it)
                }
            )

            ComponenteSeparador(sangriaInicio = 16.dp)

            ComponenteSwitch(
                titulo = "Mostrar notificación emergente (Toast)",
                icono = null,
                activo = ajustes.widget1x1MostrarToast,
                idFila = "04.4.4.2",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidget1x1MostrarToast(it)
                }
            )
        }

        Spacer(Modifier.height(14.dp))

        ComponenteGrupo(
            etiqueta = "Respuesta táctil",
            icono = Icons.Filled.Vibration,
            colorIcono = ColorGenerador,
            alRestablecer = {
                haptica.tic()
                vm.ajustarWidget1x1Haptica(true)
                vm.ajustarWidget1x1HapticaIntensidad(0.35f)
                vm.avisar("Respuesta táctil restablecida")
            },
            idGrupo = "04.4.4.G2",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSwitch(
                titulo = "Vibración al generar",
                icono = null,
                activo = ajustes.widget1x1Haptica,
                idFila = "04.4.4.3",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidget1x1Haptica(it)
                }
            )

            if (ajustes.widget1x1Haptica) {
                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteSlider(
                    titulo = "Intensidad de vibración",
                    icono = null,
                    valor = ajustes.widget1x1HapticaIntensidad,
                    valorTexto = "${(ajustes.widget1x1HapticaIntensidad * 100).roundToInt()}%",
                    rango = 0.01f..1.0f,
                    pasos = 99,
                    idFila = "04.4.4.4",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarWidget1x1HapticaIntensidad(0.35f)
                        haptica.probar(0.35f)
                    },
                    alCambiar = {
                        vm.ajustarWidget1x1HapticaIntensidad(it)
                        haptica.probar(it)
                    }
                )

                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteBotonFila(
                    titulo = "Probar vibración del widget 1x1",
                    alPulsar = { haptica.probar(ajustes.widget1x1HapticaIntensidad) }
                )
            }
        }
    }
}
