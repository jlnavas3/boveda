package com.jlnavas3.bovedalocal.ui.pantallas.tipografia

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatSize
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
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

private data class OpcionEscalaRapida(
    val nombre: String,
    val valor: Float
)

private val OPCIONES_ESCALA = listOf(
    OpcionEscalaRapida("Pequeña (0.85x)", 0.85f),
    OpcionEscalaRapida("Predeterminada (1.00x)", 1.00f),
    OpcionEscalaRapida("Mediana (1.15x)", 1.15f),
    OpcionEscalaRapida("Grande (1.30x)", 1.30f)
)

@Composable
fun PantallaTipografiaEscala(
    vm: VaultViewModel
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val porcentaje = ((ajustes.escalaTexto - 1.0f) * 100).roundToInt()
    val valorTexto = if (porcentaje == 0) "1.00x (Normal)" else if (porcentaje > 0) "${String.format(Locale.US, "%.2f", ajustes.escalaTexto)}x (+$porcentaje%)" else "${String.format(Locale.US, "%.2f", ajustes.escalaTexto)}x ($porcentaje%)"

    ContenedorPrincipal(
        titulo = "Tamaño de fuente",
        idEtiqueta = "02-APA-TYP-ESC",
        mostrarId = ajustes.mostrarIdsAjustes,
        alVolver = { vm.volverAtras() },
        conScroll = true,
        espaciado = EspaciadoComponentes,
        cabeceraFlotante = {
            PrevisualizacionTipografia()
        }
    ) {
        ComponenteGrupo(
            etiqueta = "AJUSTE PRECISO",
            icono = Icons.Filled.FormatSize,
            colorIcono = ColorGenerador,
            alRestablecer = {
                haptica.tic()
                vm.ajustarEscalaTexto(1.0f)
            },
            idGrupo = "02-APA-TYP-ESC-G01",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSlider(
                titulo = "Escalado de interfaz",
                valor = ajustes.escalaTexto,
                valorTexto = valorTexto,
                rango = 0.80f..1.35f,
                pasos = 10,
                etiquetaMin = "0.80x (-20%)",
                etiquetaMax = "1.35x (+35%)",
                alCambiar = { vm.ajustarEscalaTexto(it) },
                alRestablecer = {
                    haptica.tic()
                    vm.ajustarEscalaTexto(1.0f)
                }
            )
        }

        Spacer(Modifier.height(14.dp))

        ComponenteGrupo(
            etiqueta = "VALORES RÁPIDOS",
            idGrupo = "02-APA-TYP-ESC-G02",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            OPCIONES_ESCALA.forEachIndexed { indice, opcion ->
                val activo = abs(ajustes.escalaTexto - opcion.valor) < 0.04f

                ComponenteRadio(
                    titulo = opcion.nombre,
                    icono = null,
                    seleccionado = activo,
                    alSeleccionar = {
                        haptica.tic()
                        vm.ajustarEscalaTexto(opcion.valor)
                    }
                )

                if (indice < OPCIONES_ESCALA.lastIndex) {
                    ComponenteSeparador()
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
