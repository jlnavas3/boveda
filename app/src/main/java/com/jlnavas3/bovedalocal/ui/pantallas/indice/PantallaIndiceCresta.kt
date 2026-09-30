package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lens
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
import com.jlnavas3.bovedalocal.data.AjustesDefaults
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
    var letraArrastrada by remember { mutableStateOf<Char?>(null) }

    ContenedorPrincipal(
        titulo = "Círculo y cresta",
        idEtiqueta = "03-LST-AZX-CRE",
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
            etiqueta = "Círculo en cresta",
            icono = Icons.Filled.Lens,
            alRestablecer = {
                haptica.tic()
                vm.ajustarIndiceMostrarCirculo(AjustesDefaults.Indice.MOSTRAR_CIRCULO)
                vm.restablecerTamanoCirculoIndice()
                vm.restablecerOffsetCirculoIndice()
                vm.avisar("Valores de cresta restablecidos")
            },
            idGrupo = "03-LST-AZX-CRE-G01",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSwitch(
                titulo = "Mostrar círculo en la cresta",
                activo = ajustes.indiceMostrarCirculo,
                idFila = "03-LST-AZX-CRE-SWT",
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
                    rango = 30f..90f,
                    idFila = "03-LST-AZX-CRE-DIM",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        haptica.tic()
                        vm.restablecerTamanoCirculoIndice()
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
                    rango = 40f..220f,
                    idFila = "03-LST-AZX-CRE-OFF",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        haptica.tic()
                        vm.restablecerOffsetCirculoIndice()
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
