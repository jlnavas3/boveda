package com.jlnavas3.bovedalocal.ui.pantallas.formas

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LineWeight
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
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.Locale
import kotlin.math.abs

private data class OpcionGrosorRapido(
    val nombre: String,
    val valorDp: Float
)

private val OPCIONES_GROSOR = listOf(
    OpcionGrosorRapido("Sin borde (0 dp)", 0f),
    OpcionGrosorRapido("Fino (0.8 dp)", 0.8f),
    OpcionGrosorRapido("Estándar (1.0 dp)", 1.0f),
    OpcionGrosorRapido("Medio (1.5 dp)", 1.5f),
    OpcionGrosorRapido("Grueso (2.5 dp)", 2.5f)
)

@Composable
fun PantallaFormasGrosor(
    vm: VaultViewModel
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    ContenedorPrincipal(
        titulo = "Grosor del borde",
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
            icono = Icons.Filled.LineWeight,
            colorIcono = ColorPasskeys,
            alRestablecer = {
                haptica.tic()
                vm.ajustarGrosorBorde(1.0f)
            }
        ) {
            val valorTexto = if (ajustes.grosorBordeDp == 0f) "Sin borde" else "${String.format(Locale.US, "%.1f", ajustes.grosorBordeDp)} dp"
            ComponenteSlider(
                titulo = "Ancho de trazo",
                valor = ajustes.grosorBordeDp,
                valorTexto = valorTexto,
                rango = 0f..4f,
                pasos = 15,
                etiquetaMin = "0 dp (Plano)",
                etiquetaMax = "4 dp (Grueso)",
                alCambiar = { vm.ajustarGrosorBorde(it) },
                alRestablecer = {
                    haptica.tic()
                    vm.ajustarGrosorBorde(1.0f)
                }
            )
        }

        Spacer(Modifier.height(14.dp))

        ComponenteGrupo(
            etiqueta = "VALORES RÁPIDOS"
        ) {
            OPCIONES_GROSOR.forEachIndexed { indice, opcion ->
                val activo = abs(ajustes.grosorBordeDp - opcion.valorDp) < 0.05f

                ComponenteRadio(
                    titulo = opcion.nombre,
                    icono = null,
                    seleccionado = activo,
                    alSeleccionar = {
                        haptica.tic()
                        vm.ajustarGrosorBorde(opcion.valorDp)
                    }
                )

                if (indice < OPCIONES_GROSOR.lastIndex) {
                    ComponenteSeparador()
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
