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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1.AccionesWidget1x1
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1.SeccionAspectoWidget1x1
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1.SeccionColoresWidget1x1
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1.SeccionDimensionesWidget1x1
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1.SeccionPresetsWidget1x1
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1.SimuladorCeldaWidget1x1
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaCalibracionWidget1x1(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val color1x1BordeEfectivo = parsearColorO(ajustes.widget1x1ColorBorde, Ambar)
    val color1x1IconoEfectivo = parsearColorO(ajustes.widget1x1ColorIcono, Ambar)
    val color1x1FondoEfectivo = parsearColorO(ajustes.widget1x1ColorFondo, Color(0xFF1C1A17))

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            // 1. Barra superior
            BarraSuperiorPantalla(
                titulo = "Calibración Generador 1x1",
                idEtiqueta = "03.3.G1",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )

            // 2. Cabecera FLOTANTE fija: Simulador visual de la celda 1x1 del launcher
            SimuladorCeldaWidget1x1(
                ajustes = ajustes,
                colorBorde = color1x1BordeEfectivo,
                colorIcono = color1x1IconoEfectivo,
                colorFondo = color1x1FondoEfectivo,
                haptica = haptica
            )

            Spacer(Modifier.height(6.dp))

            // 3. Controles desplazables que se deslizan suavemente por debajo de la previa fija
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Presets de tamaño
                SeccionPresetsWidget1x1(
                    ajustes = ajustes,
                    vm = vm,
                    haptica = haptica
                )

                Spacer(Modifier.height(16.dp))

                // Dimensiones y posición
                SeccionDimensionesWidget1x1(
                    ajustes = ajustes,
                    vm = vm,
                    haptica = haptica
                )

                Spacer(Modifier.height(16.dp))

                // Forma y bordes + Transparencia
                SeccionAspectoWidget1x1(
                    ajustes = ajustes,
                    vm = vm,
                    haptica = haptica
                )

                Spacer(Modifier.height(16.dp))

                // Colores del widget 1x1
                SeccionColoresWidget1x1(
                    ajustes = ajustes,
                    vm = vm,
                    colorBorde = color1x1BordeEfectivo,
                    colorIcono = color1x1IconoEfectivo,
                    colorFondo = color1x1FondoEfectivo,
                    haptica = haptica
                )

                Spacer(Modifier.height(16.dp))

                // Restablecer aspecto
                AccionesWidget1x1(
                    vm = vm,
                    haptica = haptica
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
