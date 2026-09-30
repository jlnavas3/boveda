package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
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
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.util.Haptica
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun PantallaIndiceOla(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    var letraArrastrada by remember { mutableStateOf<Char?>(null) }

    ContenedorPrincipal(
        titulo = "Efecto de ola",
        idEtiqueta = "03-LST-AZX-OLA",
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
            etiqueta = "Efecto ola Niagara",
            icono = Icons.AutoMirrored.Filled.Sort,
            alRestablecer = {
                haptica.tic()
                vm.ajustarIndiceEfectoOla(AjustesDefaults.Indice.EFECTO_OLA)
                vm.restablecerAmplitudOla()
                vm.restablecerRadioOla()
                vm.restablecerEscalaLetrasIndice()
                vm.avisar("Valores de ola restablecidos")
            },
            idGrupo = "03-LST-AZX-OLA-G01",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSwitch(
                titulo = "Efecto de ola al deslizar",
                activo = ajustes.indiceEfectoOla,
                idFila = "03-LST-AZX-OLA-SWT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarIndiceEfectoOla(it)
                }
            )

            if (ajustes.indiceEfectoOla) {
                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteSlider(
                    titulo = "Amplitud de la ola",
                    valor = ajustes.indiceAmplitudOlaDp,
                    valorTexto = "${ajustes.indiceAmplitudOlaDp.roundToInt()} dp",
                    rango = 20f..160f,
                    idFila = "03-LST-AZX-OLA-AMP",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        haptica.tic()
                        vm.restablecerAmplitudOla()
                    },
                    alCambiar = {
                        haptica.tic()
                        vm.ajustarIndiceAmplitudOlaDp(it)
                    }
                )

                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteSlider(
                    titulo = "Alcance vertical de la ola",
                    valor = ajustes.indiceRadioOlaDp,
                    valorTexto = "${ajustes.indiceRadioOlaDp.roundToInt()} dp",
                    rango = 60f..300f,
                    idFila = "03-LST-AZX-OLA-RAD",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        haptica.tic()
                        vm.restablecerRadioOla()
                    },
                    alCambiar = {
                        haptica.tic()
                        vm.ajustarIndiceRadioOlaDp(it)
                    }
                )

                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteSlider(
                    titulo = "Escala máxima de letras",
                    valor = ajustes.indiceEscalaLetras,
                    valorTexto = String.format(Locale.US, "%.1fx", ajustes.indiceEscalaLetras),
                    rango = 1.0f..3.0f,
                    pasos = 20,
                    idFila = "03-LST-AZX-OLA-ESC",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        haptica.tic()
                        vm.restablecerEscalaLetrasIndice()
                    },
                    alCambiar = {
                        haptica.tic()
                        vm.ajustarIndiceEscalaLetras(it)
                    }
                )
            }
        }
    }
}
