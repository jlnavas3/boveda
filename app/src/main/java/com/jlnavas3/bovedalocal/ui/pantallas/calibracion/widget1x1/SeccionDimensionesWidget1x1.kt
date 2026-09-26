package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
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
        idGrupo = "03.3.G11B",
        mostrarId = ajustes.mostrarIdsAjustes,
        descripcion = "Calibra ancho, alto, alineación y desplazamiento dentro del espacio 1x1"
    ) {
        ComponenteSwitch(
            titulo = "Bloquear proporción 1:1",
            icono = Icons.Filled.AspectRatio,
            colorIcono = ColorIconosInternos,
            idFila = "03.3.21",
            mostrarId = ajustes.mostrarIdsAjustes,
            activo = ajustes.widget1x1BloquearProporcion,
            alCambiar = {
                haptica.tic()
                vm.ajustarWidget1x1BloquearProporcion(it)
            }
        )

        ComponenteSeparador()

        ComponenteSlider(
            titulo = if (ajustes.widget1x1BloquearProporcion) "Tamaño del botón" else "Ancho del widget",
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
            idFila = "03.3.22",
            mostrarId = ajustes.mostrarIdsAjustes,
            icono = Icons.Filled.CropSquare,
            colorIcono = ColorIconosInternos
        )

        if (!ajustes.widget1x1BloquearProporcion) {
            ComponenteSeparador()
            ComponenteSlider(
                titulo = "Alto del widget",
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
                idFila = "03.3.23",
                mostrarId = ajustes.mostrarIdsAjustes,
                icono = Icons.Filled.CropSquare,
                colorIcono = ColorIconosInternos
            )
        }

        ComponenteSeparador()

        ComponenteSelectorModal(
            titulo = "Alineación base",
            icono = Icons.Filled.AutoAwesome,
            colorIcono = ColorIconosInternos,
            valorSeleccionado = ajustes.widget1x1Alineamiento,
            opciones = opcionesAlineacion,
            idFila = "03.3.24",
            mostrarId = ajustes.mostrarIdsAjustes,
            alSeleccionar = {
                haptica.tic()
                vm.ajustarWidget1x1Alineamiento(it)
            }
        )

        ComponenteSeparador()

        ComponenteSlider(
            titulo = "Ajuste fino vertical (Y)",
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
            idFila = "03.3.25A",
            mostrarId = ajustes.mostrarIdsAjustes,
            icono = Icons.Filled.VerticalAlignBottom,
            colorIcono = ColorIconosInternos
        )

        ComponenteSeparador()

        ComponenteSlider(
            titulo = "Ajuste fino horizontal (X)",
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
            idFila = "03.3.25B",
            mostrarId = ajustes.mostrarIdsAjustes,
            icono = Icons.AutoMirrored.Filled.FormatAlignLeft,
            colorIcono = ColorIconosInternos
        )
    }
}
