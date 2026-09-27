package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pattern
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SelectorPlantillaPatron
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun PantallaWidget1x1Modo(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    ContenedorPrincipal(
        titulo = "Modo de generación 1x1",
        alVolver = { vm.volverAtras() }
    ) {
        ComponenteGrupo(
            etiqueta = "Estrategia de generación",
            icono = Icons.Filled.Pattern,
            colorIcono = ColorGenerador,
            alRestablecer = {
                haptica.tic()
                vm.ajustarWidget1x1Modo("aleatoria")
                vm.ajustarWidget1x1Longitud(16)
                vm.avisar("Modo de generación 1x1 restablecido")
            },
            idGrupo = "04.4.3.G1",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteRadio(
                titulo = "Generación aleatoria",
                icono = null,
                seleccionado = ajustes.widget1x1Modo == "aleatoria",
                idFila = "04.4.3.1",
                mostrarId = ajustes.mostrarIdsAjustes,
                alSeleccionar = {
                    haptica.tic()
                    vm.ajustarWidget1x1Modo("aleatoria")
                }
            )

            if (ajustes.widget1x1Modo == "aleatoria") {
                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteSlider(
                    titulo = "Longitud de la clave",
                    icono = null,
                    valor = ajustes.widget1x1Longitud.toFloat(),
                    valorTexto = "${ajustes.widget1x1Longitud} caracteres",
                    rango = 6f..64f,
                    pasos = 57,
                    idFila = "04.4.3.2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarWidget1x1Longitud(16)
                    },
                    alCambiar = {
                        vm.ajustarWidget1x1Longitud(it.roundToInt())
                    }
                )
            }

            ComponenteSeparador(sangriaInicio = 16.dp)

            ComponenteRadio(
                titulo = "Por patrón personalizado",
                icono = null,
                seleccionado = ajustes.widget1x1Modo == "patron",
                idFila = "04.4.3.3",
                mostrarId = ajustes.mostrarIdsAjustes,
                alSeleccionar = {
                    haptica.tic()
                    vm.ajustarWidget1x1Modo("patron")
                }
            )

            if (ajustes.widget1x1Modo == "patron") {
                ComponenteSeparador(sangriaInicio = 16.dp)
                Column(modifier = Modifier.padding(16.dp)) {
                    SelectorPlantillaPatron(
                        patronActual = ajustes.widget1x1Patron,
                        alSeleccionarPlantilla = {
                            haptica.tic()
                            vm.ajustarWidget1x1Patron(it)
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                    CampoBoveda(
                        valor = ajustes.widget1x1Patron,
                        etiqueta = "Patrón 1x1 (ej. XXXXX-XXXXX)",
                        alCambiar = { vm.ajustarWidget1x1Patron(it) },
                        monoespaciada = true
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "X: alfanum | A: mayús | a: minús | 9: dígito",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
