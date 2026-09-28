package com.jlnavas3.bovedalocal.ui.pantallas.formas

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropSquare
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
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

private data class OpcionCurvaturaRapida(
    val nombre: String,
    val valorDp: Float
)

private val OPCIONES_CURVATURA = listOf(
    OpcionCurvaturaRapida("Recto (0 dp)", 0f),
    OpcionCurvaturaRapida("Sutil (6 dp)", 6f),
    OpcionCurvaturaRapida("Estándar (16 dp)", 16f),
    OpcionCurvaturaRapida("Redondeado (22 dp)", 22f),
    OpcionCurvaturaRapida("Píldora (32 dp)", 32f)
)

@Composable
fun PantallaFormasCurvatura(
    vm: VaultViewModel
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    ContenedorPrincipal(
        titulo = "Curvatura de esquinas",
        idEtiqueta = "02-APA-GEO-CRV",
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
            icono = Icons.Filled.CropSquare,
            colorIcono = ColorGenerador,
            alRestablecer = {
                haptica.tic()
                vm.ajustarCurvaturaEsquinas(16f)
            },
            idGrupo = "02-APA-GEO-CRV-G01",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSlider(
                titulo = "Radio de esquinas",
                valor = ajustes.curvaturaEsquinasDp,
                valorTexto = "${ajustes.curvaturaEsquinasDp.roundToInt()} dp",
                rango = 0f..32f,
                pasos = 31,
                etiquetaMin = "0 dp (Recto)",
                etiquetaMax = "32 dp (Píldora)",
                alCambiar = { vm.ajustarCurvaturaEsquinas(it) },
                alRestablecer = {
                    haptica.tic()
                    vm.ajustarCurvaturaEsquinas(16f)
                }
            )
        }

        Spacer(Modifier.height(14.dp))

        ComponenteGrupo(
            etiqueta = "VALORES RÁPIDOS",
            idGrupo = "02-APA-GEO-CRV-G02",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            OPCIONES_CURVATURA.forEachIndexed { indice, opcion ->
                val activo = ajustes.curvaturaEsquinasDp.roundToInt() == opcion.valorDp.roundToInt()

                ComponenteRadio(
                    titulo = opcion.nombre,
                    icono = null,
                    seleccionado = activo,
                    alSeleccionar = {
                        haptica.tic()
                        vm.ajustarCurvaturaEsquinas(opcion.valorDp)
                    }
                )

                if (indice < OPCIONES_CURVATURA.lastIndex) {
                    ComponenteSeparador()
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
