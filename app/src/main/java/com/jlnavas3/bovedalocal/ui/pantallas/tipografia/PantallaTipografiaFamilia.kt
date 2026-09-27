package com.jlnavas3.bovedalocal.ui.pantallas.tipografia

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaTipografiaFamilia(
    vm: VaultViewModel
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    ContenedorPrincipal(
        titulo = "Familia tipográfica",
        alVolver = { vm.volverAtras() },
        conScroll = true,
        espaciado = EspaciadoComponentes,
        cabeceraFlotante = {
            PrevisualizacionTipografia()
        }
    ) {
        ComponenteGrupo(
            etiqueta = "FUENTES DISPONIBLES",
            icono = Icons.Filled.FontDownload,
            colorIcono = ColorIconosInternos,
            alRestablecer = {
                haptica.tic()
                vm.ajustarFamiliaFuente("sans")
            }
        ) {
            AlmacenAjustes.OPCIONES_FAMILIA_FUENTE.forEachIndexed { indice, (clave, etiqueta) ->
                val activo = ajustes.familiaFuente == clave

                ComponenteRadio(
                    titulo = etiqueta,
                    icono = null,
                    seleccionado = activo,
                    alSeleccionar = {
                        haptica.tic()
                        vm.ajustarFamiliaFuente(clave)
                    }
                )

                if (indice < AlmacenAjustes.OPCIONES_FAMILIA_FUENTE.lastIndex) {
                    ComponenteSeparador()
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
