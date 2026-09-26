package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.jlnavas3.bovedalocal.ui.pantallas.tipografia.PrevisualizacionTipografia
import com.jlnavas3.bovedalocal.ui.pantallas.tipografia.SeccionEscalaTipografia
import com.jlnavas3.bovedalocal.ui.pantallas.tipografia.SeccionEspaciadoTipografia
import com.jlnavas3.bovedalocal.ui.pantallas.tipografia.SeccionFamiliaTipografia
import com.jlnavas3.bovedalocal.ui.pantallas.tipografia.SeccionPesoYEstiloTipografia
import com.jlnavas3.bovedalocal.ui.pantallas.tipografia.SeccionPresetsTipografia
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaTipografia(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val reqPreview = remember { BringIntoViewRequester() }
    val reqPresets = remember { BringIntoViewRequester() }
    val reqEscala = remember { BringIntoViewRequester() }
    val reqFamilia = remember { BringIntoViewRequester() }
    val reqPeso = remember { BringIntoViewRequester() }
    val reqEspaciado = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null) {
            when {
                seccionDestino == "09.5.1" -> reqPreview.bringIntoView()
                seccionDestino == "09.5.2" -> reqPresets.bringIntoView()
                seccionDestino == "09.5.3" -> reqEscala.bringIntoView()
                seccionDestino == "09.5.4" -> reqFamilia.bringIntoView()
                seccionDestino == "09.5.5" -> reqPeso.bringIntoView()
                seccionDestino == "09.5.6" -> reqEspaciado.bringIntoView()
                seccionDestino.startsWith("09.5.") && seccionDestino != "09.5" -> reqPresets.bringIntoView()
            }
        }
    }

    ContenedorPrincipal(
        titulo = "Tipografía y Textos",
        subtitulo = "Personaliza fuentes, escalas y pesos en tiempo real",
        alVolver = { vm.volverAtras() },
        conScroll = true,
        espaciado = EspaciadoComponentes,
        cabeceraFlotante = {
            PrevisualizacionTipografia()
        }
    ) {
        // 2. Presets rápidos de tipografía
        SeccionPresetsTipografia(
            ajustes = ajustes,
            vm = vm,
            haptica = haptica,
            seccionDestino = seccionDestino,
            requester = reqPresets
        )

        // 3. Slider de Escala de Texto
        SeccionEscalaTipografia(
            ajustes = ajustes,
            vm = vm,
            seccionDestino = seccionDestino,
            requester = reqEscala
        )

        // 4. Selector de Familia Tipográfica
        SeccionFamiliaTipografia(
            ajustes = ajustes,
            vm = vm,
            haptica = haptica,
            seccionDestino = seccionDestino,
            requester = reqFamilia
        )

        // 5. Selector de Grosor y Estilo
        SeccionPesoYEstiloTipografia(
            ajustes = ajustes,
            vm = vm,
            haptica = haptica,
            seccionDestino = seccionDestino,
            requester = reqPeso
        )

        // 6. Espaciado e Interlineado
        SeccionEspaciadoTipografia(
            ajustes = ajustes,
            vm = vm,
            seccionDestino = seccionDestino,
            requester = reqEspaciado
        )

        // 7. Botón de restauración
        ComponenteGrupo {
            ComponenteBotonFila(
                titulo = "Restablecer módulo",
                alPulsar = {
                    vm.restablecerTipografia()
                }
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}
