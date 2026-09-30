package com.jlnavas3.bovedalocal.ui.pantallas.formas

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

private data class OpcionEspaciadoRapido(
    val nombre: String,
    val valorDp: Float
)

private val OPCIONES_ESPACIADO = listOf(
    OpcionEspaciadoRapido("Sin espacio (0 dp)", 0f),
    OpcionEspaciadoRapido("Compacto (6 dp)", 6f),
    OpcionEspaciadoRapido("Ajustado (10 dp)", 10f),
    OpcionEspaciadoRapido("Equilibrado (14 dp)", 14f),
    OpcionEspaciadoRapido("Cómodo (18 dp)", 18f),
    OpcionEspaciadoRapido("Amplio (24 dp)", 24f)
)

@Composable
fun PantallaFormasEspaciado(
    vm: VaultViewModel
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    ContenedorPrincipal(
        titulo = "Espaciado y separación",
        idEtiqueta = "02-APA-GEO-ESP",
        mostrarId = ajustes.mostrarIdsAjustes,
        alVolver = { vm.volverAtras() },
        conScroll = true,
        espaciado = EspaciadoComponentes,
        cabeceraFlotante = {
            SimuladorTarjetaInteractiva(
                ajustes = ajustes,
                haptica = haptica
            )
        }
    ) {
        ComponenteGrupo(
            etiqueta = "AJUSTE PRECISO",
            icono = Icons.Filled.SpaceDashboard,
            colorIcono = ColorIconosInternos,
            alRestablecer = {
                haptica.tic()
                vm.restablecerEspaciadoComponentes()
            },
            idGrupo = "02-APA-GEO-ESP-G01",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSlider(
                titulo = "Separación vertical",
                valor = ajustes.espaciadoComponentesDp,
                valorTexto = "${ajustes.espaciadoComponentesDp.roundToInt()} dp",
                rango = 0f..24f,
                pasos = 23,
                etiquetaMin = "0 dp (Sin espacio)",
                etiquetaMax = "24 dp (Amplio)",
                alCambiar = { vm.ajustarEspaciadoComponentes(it) },
                alRestablecer = {
                    haptica.tic()
                    vm.restablecerEspaciadoComponentes()
                }
            )
        }

        Spacer(Modifier.height(14.dp))

        ComponenteGrupo(
            etiqueta = "VALORES RÁPIDOS",
            idGrupo = "02-APA-GEO-ESP-G02",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            OPCIONES_ESPACIADO.forEachIndexed { indice, opcion ->
                val activo = ajustes.espaciadoComponentesDp.roundToInt() == opcion.valorDp.roundToInt()

                ComponenteRadio(
                    titulo = opcion.nombre,
                    icono = null,
                    seleccionado = activo,
                    alSeleccionar = {
                        haptica.tic()
                        vm.ajustarEspaciadoComponentes(opcion.valorDp)
                    }
                )

                if (indice < OPCIONES_ESPACIADO.lastIndex) {
                    ComponenteSeparador()
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
