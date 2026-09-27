package com.jlnavas3.bovedalocal.ui.pantallas.tipografia

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaTipografiaPeso(
    vm: VaultViewModel
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    ContenedorPrincipal(
        titulo = "Grosor y estilo",
        subtitulo = "Peso visual de trazo y variante cursiva",
        alVolver = { vm.volverAtras() },
        conScroll = true,
        espaciado = EspaciadoComponentes,
        cabeceraFlotante = {
            PrevisualizacionTipografia()
        }
    ) {
        ComponenteGrupo(
            etiqueta = "PESO TIPOGRÁFICO",
            icono = Icons.Filled.FormatBold,
            colorIcono = ColorIconosInternos,
            alRestablecer = {
                haptica.tic()
                vm.ajustarPesoTexto("normal")
                vm.ajustarCursivaTexto(false)
            }
        ) {
            AlmacenAjustes.OPCIONES_PESO_TEXTO.forEachIndexed { indice, (clave, etiqueta) ->
                val activo = ajustes.pesoTexto == clave

                ComponenteRadio(
                    titulo = etiqueta,
                    icono = null,
                    seleccionado = activo,
                    alSeleccionar = {
                        haptica.tic()
                        vm.ajustarPesoTexto(clave)
                    }
                )

                if (indice < AlmacenAjustes.OPCIONES_PESO_TEXTO.lastIndex) {
                    ComponenteSeparador()
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        ComponenteGrupo(
            etiqueta = "ESTILO ITÁLICA"
        ) {
            ComponenteSwitch(
                titulo = "Texto en cursiva",
                icono = null,
                activo = ajustes.cursivaTexto,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarCursivaTexto(it)
                }
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}
