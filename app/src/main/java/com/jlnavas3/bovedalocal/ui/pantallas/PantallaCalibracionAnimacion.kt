/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion.SeccionCalibracionPuerta
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion.SeccionDisenoEngranajes
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion.SeccionGeometriaEngranajes
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion.SelectorModoTemaAnimacion
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion.VisorAnimacionFlotante
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion.crearPartesEngranajes
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.animacion.generarJsonConfiguracionAnimacion
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

@Composable
fun PantallaCalibracionAnimacion(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val esEngranajes = ajustes.animacionDesbloqueo == "engranajes"
    var modoOscuroSeleccionado by remember { mutableStateOf(esOscuroActivo) }
    var parteEngranajeElegida by remember { mutableIntStateOf(0) }
    val partesEngranajes = remember(ajustes, vm, modoOscuroSeleccionado) {
        crearPartesEngranajes(ajustes, vm, esOscuro = modoOscuroSeleccionado)
    }

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
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        alCopiarConfiguracion = {
                            val json = generarJsonConfiguracionAnimacion(ajustes)
                            val clipboard = contexto.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Configuración Animación", json))
                            vm.avisar("Configuración copiada al portapapeles", "Portapapeles")
                        },
                        alRestablecerPantalla = {
                            if (esEngranajes) {
                                vm.restablecerAjustesEngranajes()
                            } else {
                                vm.restablecerAjustesPuerta()
                            }
                        },
                        mensajeToastRestablecer = "Animación restablecida"
                    )
                }
            )

            // 2. Cabecera FLOTANTE fija: Muestra la animación en el tema seleccionado
            VisorAnimacionFlotante(
                esEngranajes = esEngranajes,
                ajustes = ajustes,
                esOscuro = modoOscuroSeleccionado
            )

            // Selector segmentado de Tema Claro / Tema Oscuro
            SelectorModoTemaAnimacion(
                esOscuro = modoOscuroSeleccionado,
                alCambiarModo = { modoOscuroSeleccionado = it },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )

            Spacer(Modifier.height(4.dp))

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
                        mostrarIds = ajustes.mostrarIdsAjustes,
                        alRestablecer = { vm.restablecerColoresEngranajes(modoOscuroSeleccionado) }
                    )
                } else {
                    // --- CALIBRACIÓN DE PUERTA DE BÓVEDA ---
                    SeccionCalibracionPuerta(
                        ajustes = ajustes,
                        vm = vm,
                        esOscuro = modoOscuroSeleccionado
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
