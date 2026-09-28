package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun PantallaIndiceHaptica(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var letraArrastrada by remember { mutableStateOf<Char?>(null) }

    ContenedorPrincipal(
        titulo = "Tacto y háptica",
        idEtiqueta = "03-LST-AZX-HAP",
        mostrarId = ajustes.mostrarIdsAjustes,
        alVolver = { vm.volverAtras() },
        cabeceraFlotante = {
            VistaPreviaIndiceInteractiva(
                ajustes = ajustes,
                letraArrastrada = letraArrastrada
            )
        },
        overlayLateral = {
            IndiceAlfabeticoCalibracion(
                ajustes = ajustes,
                alCambiarLetra = { letraArrastrada = it },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 64.dp, bottom = 100.dp)
            )
        }
    ) {
        ComponenteGrupo(
            etiqueta = "Respuesta táctil",
            icono = Icons.Filled.Vibration,
            alRestablecer = {
                haptica.tic()
                vm.ajustarIndiceHaptica(true)
                vm.ajustarIndiceIncluirEnie(true)
                vm.ajustarIndiceAnchoTactilDp(28f)
                vm.ajustarIndiceTonoLetras(0.7f)
                vm.avisar("Valores de tacto restablecidos")
            },
            idGrupo = "03-LST-AZX-HAP-G01",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSwitch(
                titulo = "Vibración háptica al deslizar",
                activo = ajustes.indiceHaptica,
                idFila = "03-LST-AZX-HAP-SWT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarIndiceHaptica(it)
                }
            )

            ComponenteSeparador(sangriaInicio = 16.dp)

            ComponenteSwitch(
                titulo = "Incluir letra Ñ en el abecedario",
                activo = ajustes.indiceIncluirEnie,
                idFila = "03-LST-AZX-HAP-ENI",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarIndiceIncluirEnie(it)
                }
            )

            ComponenteSeparador(sangriaInicio = 16.dp)

            ComponenteSlider(
                titulo = "Ancho de la zona táctil",
                valor = ajustes.indiceAnchoTactilDp,
                valorTexto = "${ajustes.indiceAnchoTactilDp.roundToInt()} dp",
                rango = 16f..60f,
                idFila = "03-LST-AZX-HAP-ZON",
                mostrarId = ajustes.mostrarIdsAjustes,
                alRestablecer = {
                    haptica.tic()
                    vm.ajustarIndiceAnchoTactilDp(28f)
                },
                alCambiar = {
                    haptica.tic()
                    vm.ajustarIndiceAnchoTactilDp(it)
                }
            )

            ComponenteSeparador(sangriaInicio = 16.dp)

            ComponenteSlider(
                titulo = "Tono y luminosidad de letras",
                valor = ajustes.indiceTonoLetras,
                valorTexto = String.format(Locale.US, "%.2f", ajustes.indiceTonoLetras),
                rango = 0.2f..1.0f,
                pasos = 16,
                idFila = "03-LST-AZX-HAP-LUM",
                mostrarId = ajustes.mostrarIdsAjustes,
                alRestablecer = {
                    haptica.tic()
                    vm.ajustarIndiceTonoLetras(0.7f)
                },
                alCambiar = {
                    haptica.tic()
                    vm.ajustarIndiceTonoLetras(it)
                }
            )
        }
    }
}
