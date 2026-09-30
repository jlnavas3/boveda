package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun SeccionDimensionesWidget1x1(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica
) {
    val opcionesAlineacion = remember {
        listOf(
            OpcionSelectorModal(
                valor = "arriba",
                etiquetaFila = "Arriba (predeterminado)",
                etiquetaModal = "Arriba (predeterminado)",
                descripcionModal = "Alineado a la parte superior de la celda 1x1 del launcher",
                icono = Icons.Filled.VerticalAlignTop
            ),
            OpcionSelectorModal(
                valor = "centro",
                etiquetaFila = "Centro",
                etiquetaModal = "Centro",
                descripcionModal = "Centrado vertical y horizontalmente en la celda 1x1",
                icono = Icons.Filled.CropSquare
            ),
            OpcionSelectorModal(
                valor = "abajo",
                etiquetaFila = "Abajo",
                etiquetaModal = "Abajo",
                descripcionModal = "Alineado a la parte inferior de la celda 1x1 del launcher",
                icono = Icons.Filled.VerticalAlignBottom
            ),
            OpcionSelectorModal(
                valor = "izquierda",
                etiquetaFila = "Izquierda",
                etiquetaModal = "Izquierda",
                descripcionModal = "Alineado al margen izquierdo de la celda 1x1",
                icono = Icons.AutoMirrored.Filled.FormatAlignLeft
            ),
            OpcionSelectorModal(
                valor = "derecha",
                etiquetaFila = "Derecha",
                etiquetaModal = "Derecha",
                descripcionModal = "Alineado al margen derecho de la celda 1x1",
                icono = Icons.AutoMirrored.Filled.FormatAlignRight
            )
        )
    }

    ComponenteGrupo(
        etiqueta = "Dimensiones y posición",
        idGrupo = "04-HER-WGT-1X1-G02",
        mostrarId = ajustes.mostrarIdsAjustes,
        alRestablecer = {
            haptica.tic()
            vm.restablecerDimensionesWidget1x1()
        }
    ) {
        ComponenteSwitch(
            titulo = "Bloquear proporción 1:1",
            icono = null,
            idFila = "04-HER-WGT-1X1-PRP",
            mostrarId = ajustes.mostrarIdsAjustes,
            activo = ajustes.widget1x1BloquearProporcion,
            alCambiar = {
                haptica.tic()
                vm.ajustarWidget1x1BloquearProporcion(it)
            }
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteSlider(
            titulo = if (ajustes.widget1x1BloquearProporcion) "Tamaño del botón" else "Ancho del widget",
            icono = null,
            valor = ajustes.widget1x1AnchoDp,
            valorTexto = "${ajustes.widget1x1AnchoDp.roundToInt()} dp",
            alCambiar = {
                haptica.tic()
                vm.ajustarWidget1x1Ancho(it)
            },
            rango = 32f..80f,
            pasos = 47,
            etiquetaMin = "32 dp",
            etiquetaMax = "80 dp",
            idFila = "04-HER-WGT-1X1-ANC",
            mostrarId = ajustes.mostrarIdsAjustes
        )

        if (!ajustes.widget1x1BloquearProporcion) {
            ComponenteSeparador(sangriaInicio = 16.dp)
            ComponenteSlider(
                titulo = "Alto del widget",
                icono = null,
                valor = ajustes.widget1x1AltoDp,
                valorTexto = "${ajustes.widget1x1AltoDp.roundToInt()} dp",
                alCambiar = {
                    haptica.tic()
                    vm.ajustarWidget1x1Alto(it)
                },
                rango = 32f..80f,
                pasos = 47,
                etiquetaMin = "32 dp",
                etiquetaMax = "80 dp",
                idFila = "04-HER-WGT-1X1-ALT",
                mostrarId = ajustes.mostrarIdsAjustes
            )
        }

        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteSelectorModal(
            titulo = "Alineación base",
            icono = null,
            valorSeleccionado = ajustes.widget1x1Alineamiento,
            opciones = opcionesAlineacion,
            idFila = "04-HER-WGT-1X1-ALN",
            mostrarId = ajustes.mostrarIdsAjustes,
            alSeleccionar = {
                haptica.tic()
                vm.ajustarWidget1x1Alineamiento(it)
            }
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteSlider(
            titulo = "Ajuste fino vertical (Y)",
            icono = null,
            valor = ajustes.widget1x1OffsetY,
            valorTexto = "${ajustes.widget1x1OffsetY.roundToInt()} dp",
            alCambiar = {
                haptica.tic()
                vm.ajustarWidget1x1OffsetY(it)
            },
            rango = -30f..30f,
            pasos = 60,
            etiquetaMin = "-30 dp (subir)",
            etiquetaMax = "+30 dp (bajar)",
            idFila = "04-HER-WGT-1X1-OFY",
            mostrarId = ajustes.mostrarIdsAjustes
        )

        ComponenteSeparador(sangriaInicio = 16.dp)

        ComponenteSlider(
            titulo = "Ajuste fino horizontal (X)",
            icono = null,
            valor = ajustes.widget1x1OffsetX,
            valorTexto = "${ajustes.widget1x1OffsetX.roundToInt()} dp",
            alCambiar = {
                haptica.tic()
                vm.ajustarWidget1x1OffsetX(it)
            },
            rango = -30f..30f,
            pasos = 60,
            etiquetaMin = "-30 dp (izq)",
            etiquetaMax = "+30 dp (der)",
            idFila = "04-HER-WGT-1X1-OFX",
            mostrarId = ajustes.mostrarIdsAjustes
        )
    }
}
