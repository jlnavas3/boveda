package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.pantallas.formas.SeccionCurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.pantallas.formas.SeccionEspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.pantallas.formas.SeccionEstiloBorde
import com.jlnavas3.bovedalocal.ui.pantallas.formas.SeccionGrosorBorde
import com.jlnavas3.bovedalocal.ui.pantallas.formas.SeccionPresetsFormas
import com.jlnavas3.bovedalocal.ui.pantallas.formas.SimuladorTarjetaInteractiva
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaFormas(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var textoPrueba by remember { mutableStateOf("Texto de prueba") }

    val reqPreview = remember { BringIntoViewRequester() }
    val reqPresets = remember { BringIntoViewRequester() }
    val reqCurvatura = remember { BringIntoViewRequester() }
    val reqGrosor = remember { BringIntoViewRequester() }
    val reqEstilo = remember { BringIntoViewRequester() }
    val reqEspaciado = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null) {
            when {
                seccionDestino == "09.3.1" -> reqPreview.bringIntoView()
                seccionDestino == "09.3.2" -> reqPresets.bringIntoView()
                seccionDestino == "09.3.3" -> reqCurvatura.bringIntoView()
                seccionDestino == "09.3.4" -> reqGrosor.bringIntoView()
                seccionDestino == "09.3.5" -> reqEstilo.bringIntoView()
                seccionDestino == "09.3.6" -> reqEspaciado.bringIntoView()
                seccionDestino.startsWith("09.3.") && seccionDestino != "09.3" -> reqPresets.bringIntoView()
            }
        }
    }

    ContenedorPrincipal(
        titulo = "Bordes y Formas",
        subtitulo = "Personaliza curvaturas, trazos y espaciados en tiempo real",
        alVolver = { vm.volverAtras() },
        conScroll = true,
        espaciado = EspaciadoComponentes,
        cabeceraFlotante = {
            SimuladorTarjetaInteractiva(
                ajustes = ajustes,
                textoPrueba = textoPrueba,
                alCambiarTextoPrueba = { textoPrueba = it },
                haptica = haptica
            )
        }
    ) {
        // 2. Presets rápidos de diseño
        SeccionPresetsFormas(
            ajustes = ajustes,
            seccionDestino = seccionDestino,
            reqPresets = reqPresets,
            haptica = haptica,
            alAplicarPreset = { curvatura, grosor, estilo, espaciado ->
                vm.aplicarPresetFormas(curvatura, grosor, estilo, espaciado)
            }
        )

        // 3. Slider de Curvatura de Esquinas
        SeccionCurvaturaEsquinas(
            ajustes = ajustes,
            seccionDestino = seccionDestino,
            reqCurvatura = reqCurvatura,
            alAjustarCurvatura = { vm.ajustarCurvaturaEsquinas(it) }
        )

        // 4. Slider de Grosor de Bordes
        SeccionGrosorBorde(
            ajustes = ajustes,
            seccionDestino = seccionDestino,
            reqGrosor = reqGrosor,
            alAjustarGrosor = { vm.ajustarGrosorBorde(it) }
        )

        // 5. Selector de Estilo de Borde
        SeccionEstiloBorde(
            ajustes = ajustes,
            seccionDestino = seccionDestino,
            reqEstilo = reqEstilo,
            haptica = haptica,
            alAjustarEstilo = { vm.ajustarEstiloBorde(it) }
        )

        // 6. Slider de Espaciado entre Componentes
        SeccionEspaciadoComponentes(
            ajustes = ajustes,
            seccionDestino = seccionDestino,
            reqEspaciado = reqEspaciado,
            alAjustarEspaciado = { vm.ajustarEspaciadoComponentes(it) }
        )

        // 7. Botón de restauración
        ComponenteGrupo {
            ComponenteBotonFila(
                titulo = "Restablecer módulo",
                alPulsar = {
                    vm.restablecerFormas()
                }
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}
