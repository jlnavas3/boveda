package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lens
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun PantallaIndiceCresta(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    ContenedorPrincipal(
        titulo = "Círculo y cresta",
        alVolver = { vm.volverAtras() },
        cabeceraFlotante = {
            VistaPreviaIndiceInteractiva(ajustes = ajustes)
        }
    ) {
        ComponenteGrupo(
            etiqueta = "Círculo en cresta",
            icono = Icons.Filled.Lens,
            alRestablecer = {
                haptica.tic()
                vm.ajustarIndiceMostrarCirculo(true)
                vm.ajustarIndiceTamanoCirculoDp(42f)
                vm.ajustarIndiceOffsetCirculoDp(12f)
                vm.avisar("Valores de cresta restablecidos")
            },
            idGrupo = "03.2.2",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSwitch(
                titulo = "Mostrar círculo en la cresta",
                activo = ajustes.indiceMostrarCirculo,
                idFila = "03.2.2.1",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarIndiceMostrarCirculo(it)
                }
            )

            if (ajustes.indiceMostrarCirculo) {
                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteSlider(
                    titulo = "Diámetro de cresta",
                    valor = ajustes.indiceTamanoCirculoDp,
                    valorTexto = "${ajustes.indiceTamanoCirculoDp.roundToInt()} dp",
                    rango = 20f..64f,
                    idFila = "03.2.2.2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarIndiceTamanoCirculoDp(42f)
                    },
                    alCambiar = {
                        haptica.tic()
                        vm.ajustarIndiceTamanoCirculoDp(it)
                    }
                )

                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteSlider(
                    titulo = "Desplazamiento horizontal",
                    valor = ajustes.indiceOffsetCirculoDp,
                    valorTexto = "${ajustes.indiceOffsetCirculoDp.roundToInt()} dp",
                    rango = -20f..60f,
                    idFila = "03.2.2.3",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarIndiceOffsetCirculoDp(12f)
                    },
                    alCambiar = {
                        haptica.tic()
                        vm.ajustarIndiceOffsetCirculoDp(it)
                    }
                )
            }
        }
    }
}
