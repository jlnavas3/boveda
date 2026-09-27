package com.jlnavas3.bovedalocal.ui.pantallas.tipografia

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatLineSpacing
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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.Locale

@Composable
fun PantallaTipografiaEspaciado(
    vm: VaultViewModel
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val textoKerning = if (ajustes.espaciadoLetrasSp == 0f) "0.0 sp (Normal)" else "${String.format(Locale.US, "%.1f", ajustes.espaciadoLetrasSp)} sp"
    val textoInterlineado = if (ajustes.interlineadoFactor == 1.0f) "1.00x (Normal)" else "${String.format(Locale.US, "%.2f", ajustes.interlineadoFactor)}x"

    ContenedorPrincipal(
        titulo = "Espaciado y separación",
        subtitulo = "Separación entre caracteres y líneas",
        alVolver = { vm.volverAtras() },
        conScroll = true,
        espaciado = EspaciadoComponentes,
        cabeceraFlotante = {
            PrevisualizacionTipografia()
        }
    ) {
        ComponenteGrupo(
            etiqueta = "SEPARACIÓN ENTRE CARACTERES",
            icono = Icons.Filled.FormatLineSpacing,
            colorIcono = ColorGenerador,
            alRestablecer = {
                haptica.tic()
                vm.ajustarEspaciadoLetras(0.0f)
            }
        ) {
            ComponenteSlider(
                titulo = "Espaciado de letras (Kerning)",
                valor = ajustes.espaciadoLetrasSp,
                valorTexto = textoKerning,
                rango = -1.0f..2.0f,
                pasos = 30,
                etiquetaMin = "-1.0 sp (Compacto)",
                etiquetaMax = "+2.0 sp (Espacioso)",
                alCambiar = { vm.ajustarEspaciadoLetras(it) },
                alRestablecer = {
                    haptica.tic()
                    vm.ajustarEspaciadoLetras(0.0f)
                }
            )
        }

        Spacer(Modifier.height(14.dp))

        ComponenteGrupo(
            etiqueta = "ALTURA DE LÍNEA",
            alRestablecer = {
                haptica.tic()
                vm.ajustarInterlineadoFactor(1.0f)
            }
        ) {
            ComponenteSlider(
                titulo = "Interlineado vertical",
                valor = ajustes.interlineadoFactor,
                valorTexto = textoInterlineado,
                rango = 0.80f..1.40f,
                pasos = 12,
                etiquetaMin = "0.80x (Denso)",
                etiquetaMax = "1.40x (Aireado)",
                alCambiar = { vm.ajustarInterlineadoFactor(it) },
                alRestablecer = {
                    haptica.tic()
                    vm.ajustarInterlineadoFactor(1.0f)
                }
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}
