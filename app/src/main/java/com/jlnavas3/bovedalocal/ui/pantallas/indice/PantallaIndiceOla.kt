package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
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

    val mockItems = remember {
        listOf(
            "Amazon" to "Compras y suscripción",
            "Apple" to "ID de Apple y iCloud",
            "GitHub" to "Cuenta de desarrollo",
            "Google" to "admin@gmail.com",
            "Netflix" to "Suscripción familiar"
        )
    }

    ContenedorPrincipal(
        titulo = "Efecto de ola",
        alVolver = { vm.volverAtras() },
        cabeceraFlotante = {
            VistaPreviaIndiceInteractiva(
                ajustes = ajustes,
                letraArrastrada = 'G',
                mockItems = mockItems
            )
        }
    ) {
        ComponenteGrupo(
            etiqueta = "Efecto ola Niagara",
            icono = Icons.AutoMirrored.Filled.Sort,
            idGrupo = "03.2.1",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSwitch(
                titulo = "Efecto de ola al deslizar",
                activo = ajustes.indiceEfectoOla,
                idFila = "03.2.1.1",
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
                    rango = 0f..80f,
                    idFila = "03.2.1.2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarIndiceAmplitudOlaDp(32f)
                    },
                    alCambiar = {
                        haptica.tic()
                        vm.ajustarIndiceAmplitudOlaDp(it)
                    }
                )

                ComponenteSeparador(sangriaInicio = 16.dp)

                ComponenteSlider(
                    titulo = "Alcance vertical",
                    valor = ajustes.indiceRadioOlaDp,
                    valorTexto = "${ajustes.indiceRadioOlaDp.roundToInt()} letras",
                    rango = 1f..8f,
                    idFila = "03.2.1.3",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarIndiceRadioOlaDp(3f)
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
                    idFila = "03.2.1.4",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alRestablecer = {
                        haptica.tic()
                        vm.ajustarIndiceEscalaLetras(1.8f)
                    },
                    alCambiar = {
                        haptica.tic()
                        vm.ajustarIndiceEscalaLetras(it)
                    }
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        ComponenteGrupo {
            ComponenteBotonFila(
                titulo = "Restablecer ola",
                alPulsar = {
                    haptica.tic()
                    vm.ajustarIndiceEfectoOla(true)
                    vm.ajustarIndiceAmplitudOlaDp(32f)
                    vm.ajustarIndiceRadioOlaDp(3f)
                    vm.ajustarIndiceEscalaLetras(1.8f)
                    vm.avisar("Valores de ola restablecidos")
                }
            )
        }
    }
}
