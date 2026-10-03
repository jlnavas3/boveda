package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget

import android.widget.Toast
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
import com.jlnavas3.bovedalocal.data.AjustesDefaults
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
        idEtiqueta = "04-HER-WGT-CMP",
        mostrarId = ajustes.mostrarIdsAjustes,
        alVolver = { vm.volverAtras() }
    ) {
        ComponenteGrupo(
            etiqueta = "Acciones automáticas",
            icono = Icons.Filled.Bolt,
            colorIcono = ColorGenerador,
            alRestablecer = {
                haptica.tic()
                vm.ajustarWidget1x1CopiarPortapapeles(AjustesDefaults.Widget1x1.COPIAR_PORTAPAPELES)
                vm.ajustarWidget1x1MostrarToast(AjustesDefaults.Widget1x1.MOSTRAR_TOAST)
                vm.avisar("Acciones automáticas restablecidas")
            },
            idGrupo = "04-HER-WGT-CMP-G01",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSwitch(
                titulo = "Copiar al portapapeles",
                icono = null,
                activo = ajustes.widget1x1CopiarPortapapeles,
                idFila = "04-HER-WGT-CMP-CLP",
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
                idFila = "04-HER-WGT-CMP-TST",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidget1x1MostrarToast(it)
                    if (it) {
                        Toast.makeText(contexto, "Notificación emergente (Toast) activada", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            if (ajustes.widget1x1MostrarToast) {
                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteBotonFila(
                    titulo = "Probar notificación emergente (Toast)",
                    icono = null,
                    alPulsar = {
                        Toast.makeText(contexto, "Prueba: Contraseña generada y copiada al portapapeles", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        ComponenteGrupo(
            etiqueta = "Respuesta táctil",
            icono = Icons.Filled.Vibration,
            colorIcono = ColorGenerador,
            alRestablecer = {
                haptica.tic()
                vm.ajustarWidget1x1Haptica(AjustesDefaults.Widget1x1.HAPTICA)
                vm.ajustarWidget1x1HapticaIntensidad(AjustesDefaults.Widget1x1.HAPTICA_INTENSIDAD)
                vm.avisar("Respuesta táctil restablecida")
            },
            idGrupo = "04-HER-WGT-CMP-G02",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSwitch(
                titulo = "Vibración al generar",
                icono = null,
                activo = ajustes.widget1x1Haptica,
                idFila = "04-HER-WGT-CMP-HAP",
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
                    idFila = "04-HER-WGT-CMP-HIN",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        vm.ajustarWidget1x1HapticaIntensidad(AjustesDefaults.Widget1x1.HAPTICA_INTENSIDAD)
                        Haptica.vibrarExterno(
                            context = contexto,
                            activo = true,
                            intensidad = AjustesDefaults.Widget1x1.HAPTICA_INTENSIDAD
                        )
                    },
                    alCambiar = {
                        vm.ajustarWidget1x1HapticaIntensidad(it)
                        Haptica.vibrarExterno(
                            context = contexto,
                            activo = true,
                            intensidad = it
                        )
                    }
                )

                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteBotonFila(
                    titulo = "Probar vibración del widget 1x1",
                    icono = null,
                    ejecutarHapticaAlPulsar = false,
                    alPulsar = {
                        Haptica.vibrarExterno(
                            context = contexto,
                            activo = true,
                            intensidad = ajustes.widget1x1HapticaIntensidad
                        )
                    }
                )
            }
        }
    }
}
