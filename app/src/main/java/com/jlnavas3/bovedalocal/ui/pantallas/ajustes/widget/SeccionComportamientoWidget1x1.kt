package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Notifications
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
fun SeccionComportamientoWidget1x1(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ComponenteGrupo(
            etiqueta = "Respuesta táctil",
            idGrupo = "03.3.G3",
            mostrarId = ajustes.mostrarIdsAjustes,
            descripcion = "Vibración háptica al pulsar el widget en la pantalla de inicio"
        ) {
            ComponenteSwitch(
                titulo = "Vibración al generar",
                icono = Icons.Filled.Vibration,
                colorIcono = ColorIconosInternos,
                idFila = "03.3.9",
                mostrarId = ajustes.mostrarIdsAjustes,
                activo = ajustes.widget1x1Haptica,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidget1x1Haptica(it)
                }
            )

            if (ajustes.widget1x1Haptica) {
                ComponenteSeparador()

                ComponenteSlider(
                    titulo = "Intensidad de vibración",
                    valor = ajustes.widget1x1HapticaIntensidad,
                    valorTexto = "${(ajustes.widget1x1HapticaIntensidad * 100).roundToInt()}%",
                    rango = 0.01f..1.0f,
                    pasos = 99,
                    etiquetaMin = "1% (Mínima)",
                    etiquetaMax = "100%",
                    idFila = "03.3.10",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    icono = Icons.Filled.Vibration,
                    colorIcono = ColorIconosInternos,
                    alCambiar = {
                        vm.ajustarWidget1x1HapticaIntensidad(it)
                        haptica.probar(it)
                    }
                )

                ComponenteSeparador()

                ComponenteBotonFila(
                    titulo = "Probar vibración del widget 1x1",
                    icono = Icons.Filled.Vibration,
                    colorIcono = ColorIconosInternos,
                    alPulsar = {
                        haptica.probar(ajustes.widget1x1HapticaIntensidad)
                    }
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        ComponenteGrupo(
            etiqueta = "Acciones automáticas",
            idGrupo = "03.3.G4",
            mostrarId = ajustes.mostrarIdsAjustes,
            descripcion = "Comportamiento del sistema tras generar la clave"
        ) {
            ComponenteSwitch(
                titulo = "Copiar al portapapeles",
                icono = Icons.Filled.ContentCopy,
                colorIcono = ColorIconosInternos,
                idFila = "03.3.11",
                mostrarId = ajustes.mostrarIdsAjustes,
                activo = ajustes.widget1x1CopiarPortapapeles,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidget1x1CopiarPortapapeles(it)
                }
            )

            ComponenteSeparador()

            ComponenteSwitch(
                titulo = "Mostrar notificación toast",
                icono = Icons.Filled.Notifications,
                colorIcono = ColorIconosInternos,
                idFila = "03.3.12",
                mostrarId = ajustes.mostrarIdsAjustes,
                activo = ajustes.widget1x1MostrarToast,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidget1x1MostrarToast(it)
                }
            )
        }

        Spacer(Modifier.height(18.dp))

        ComponenteGrupo {
            ComponenteBotonFila(
                titulo = "Restablecer valores del widget 1x1",
                icono = Icons.AutoMirrored.Filled.RotateLeft,
                colorIcono = ColorIconosInternos,
                alPulsar = {
                    haptica.exito()
                    vm.restablecerAjustesWidget1x1()
                    vm.avisar("Ajustes del widget 1x1 restablecidos")
                }
            )
        }
    }
}
