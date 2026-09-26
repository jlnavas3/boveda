package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.LocalCoordinadorResaltado
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.contenedorScrollAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget.SeccionAjustesWidgetTotp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget.SeccionComportamientoWidget1x1
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget.SeccionModoGenerador1x1
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget.SelectorTipoWidgetAjustes
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaAjustesWidget(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var pestanaWidget by remember { mutableIntStateOf(vm.ultimoWidgetAjustesSeleccionado) }

    LaunchedEffect(pestanaWidget) {
        vm.ultimoWidgetAjustesSeleccionado = pestanaWidget
    }

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        val coordinador = LocalCoordinadorResaltado.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Widgets de escritorio",
                idEtiqueta = "03.3",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .contenedorScrollAjustes(coordinador)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Personalización, calibración visual y respuesta táctil de widgets")
                Spacer(Modifier.height(10.dp))

                SelectorTipoWidgetAjustes(
                    pestanaWidget = pestanaWidget,
                    mostrarId = ajustes.mostrarIdsAjustes,
                    haptica = haptica,
                    vm = vm,
                    alSeleccionarWidget = {
                        pestanaWidget = it
                        vm.ultimoWidgetAjustesSeleccionado = it
                    }
                )

                Spacer(Modifier.height(18.dp))

                if (pestanaWidget == 0) {
                    SeccionAjustesWidgetTotp(
                        ajustes = ajustes,
                        vm = vm,
                        haptica = haptica
                    )
                } else {
                    SeccionModoGenerador1x1(
                        ajustes = ajustes,
                        vm = vm,
                        haptica = haptica
                    )

                    Spacer(Modifier.height(18.dp))

                    SeccionComportamientoWidget1x1(
                        ajustes = ajustes,
                        vm = vm,
                        haptica = haptica
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
