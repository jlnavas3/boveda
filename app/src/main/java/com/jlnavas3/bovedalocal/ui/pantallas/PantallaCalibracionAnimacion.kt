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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion.AccionesCalibracionEngranajes
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion.SeccionCalibracionPuerta
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion.SeccionDisenoEngranajes
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion.SeccionGeometriaEngranajes
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion.VisorAnimacionFlotante
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion.crearPartesEngranajes

@Composable
fun PantallaCalibracionAnimacion(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val esEngranajes = ajustes.animacionDesbloqueo == "engranajes"
    var parteEngranajeElegida by remember { mutableIntStateOf(0) }
    val partesEngranajes = remember(ajustes, vm) { crearPartesEngranajes(ajustes, vm) }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            // 1. Barra superior
            BarraSuperiorPantalla(
                titulo = if (esEngranajes) "Mecanismo de engranajes" else "Puerta de bóveda",
                idEtiqueta = "02-APA-THM-ANI",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )

            // 2. Cabecera FLOTANTE fija: Muestra solo la animación sin textos ni títulos
            VisorAnimacionFlotante(
                esEngranajes = esEngranajes,
                ajustes = ajustes
            )

            Spacer(Modifier.height(6.dp))

            // 3. Controles scrollables que se deslizan por debajo de la animación flotante
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (esEngranajes) {
                    // --- CALIBRACIÓN DE ENGRANAJES ---
                    SeccionGeometriaEngranajes(
                        ajustes = ajustes,
                        vm = vm
                    )

                    Spacer(Modifier.height(18.dp))

                    SeccionDisenoEngranajes(
                        partesEngranajes = partesEngranajes,
                        parteEngranajeElegida = parteEngranajeElegida,
                        alSeleccionarParte = { parteEngranajeElegida = it },
                        mostrarIds = ajustes.mostrarIdsAjustes
                    )
                } else {
                    // --- CALIBRACIÓN DE PUERTA DE BÓVEDA ---
                    SeccionCalibracionPuerta(
                        ajustes = ajustes,
                        vm = vm
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
