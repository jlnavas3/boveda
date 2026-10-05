package com.jlnavas3.bovedalocal.ui.pantallas.formas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Tune
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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.Locale
import kotlin.math.abs

private data class OpcionGrosorRapido(
    val nombre: String,
    val valorDp: Float
)

private val OPCIONES_GROSOR_RAPIDO = listOf(
    OpcionGrosorRapido("Fino (0.8 dp)", 0.8f),
    OpcionGrosorRapido("Estándar (1.0 dp)", 1.0f),
    OpcionGrosorRapido("Medio (1.5 dp)", 1.5f),
    OpcionGrosorRapido("Grueso (2.5 dp)", 2.5f)
)

@Composable
fun PantallaFormasBorde(
    vm: VaultViewModel
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val tieneBordeActivo = ajustes.estiloBorde != "ninguno" && ajustes.grosorBordeDp > 0f

    ContenedorPrincipal(
        titulo = "Borde",
        idEtiqueta = "02-APA-GEO-BOR",
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
            etiqueta = "ESTILO Y TONO",
            icono = Icons.Filled.Tune,
            colorIcono = ColorSeguridad,
            alRestablecer = {
                haptica.tic()
                vm.restablecerBorde()
            },
            idGrupo = "02-APA-GEO-BOR-G01",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            AlmacenAjustes.OPCIONES_ESTILO_BORDE.forEachIndexed { indice, (clave, etiqueta) ->
                val activo = if (clave == "ninguno") {
                    !tieneBordeActivo
                } else {
                    ajustes.estiloBorde == clave && tieneBordeActivo
                }

                ComponenteRadio(
                    titulo = if (clave == "ninguno") "$etiqueta (Predeterminado)" else etiqueta,
                    icono = null,
                    seleccionado = activo,
                    alSeleccionar = {
                        haptica.tic()
                        vm.ajustarEstiloBorde(clave)
                    }
                )

                if (indice < AlmacenAjustes.OPCIONES_ESTILO_BORDE.lastIndex) {
                    ComponenteSeparador()
                }
            }
        }

        AnimatedVisibility(
            visible = tieneBordeActivo,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ComponenteGrupo(
                etiqueta = "GROSOR DEL TRAZO",
                icono = Icons.Filled.LineWeight,
                colorIcono = ColorPasskeys,
                alRestablecer = {
                    haptica.tic()
                    vm.ajustarGrosorBorde(1.0f)
                },
                idGrupo = "02-APA-GEO-BOR-G02",
                mostrarId = ajustes.mostrarIdsAjustes
            ) {
                val valorTexto = "${String.format(Locale.US, "%.1f", ajustes.grosorBordeDp)} dp"
                ComponenteSlider(
                    titulo = "Ancho de trazo",
                    valor = ajustes.grosorBordeDp.coerceIn(0.5f, 4f),
                    valorTexto = valorTexto,
                    rango = 0.5f..4f,
                    pasos = 13,
                    etiquetaMin = "0.5 dp (Fino)",
                    etiquetaMax = "4 dp (Grueso)",
                    alCambiar = { vm.ajustarGrosorBorde(it) },
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarGrosorBorde(1.0f)
                    }
                )

                ComponenteSeparador()

                OPCIONES_GROSOR_RAPIDO.forEachIndexed { indice, opcion ->
                    val seleccionado = abs(ajustes.grosorBordeDp - opcion.valorDp) < 0.05f

                    ComponenteRadio(
                        titulo = opcion.nombre,
                        icono = null,
                        seleccionado = seleccionado,
                        alSeleccionar = {
                            haptica.tic()
                            vm.ajustarGrosorBorde(opcion.valorDp)
                        }
                    )

                    if (indice < OPCIONES_GROSOR_RAPIDO.lastIndex) {
                        ComponenteSeparador()
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
