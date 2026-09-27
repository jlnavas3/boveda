package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Vibration
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
fun PantallaIndiceHaptica(
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
        titulo = "Tacto y háptica",
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
            etiqueta = "Respuesta táctil y alfabeto",
            icono = Icons.Filled.Vibration,
            idGrupo = "03.2.3",
            mostrarId = ajustes.mostrarIdsAjustes
        ) {
            ComponenteSwitch(
                titulo = "Vibración háptica al deslizar",
                activo = ajustes.indiceHaptica,
                idFila = "03.2.3.1",
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
                idFila = "03.2.3.2",
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
                idFila = "03.2.3.3",
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
                idFila = "03.2.3.4",
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

        Spacer(Modifier.height(14.dp))

        ComponenteGrupo {
            ComponenteBotonFila(
                titulo = "Restablecer tacto",
                alPulsar = {
                    haptica.tic()
                    vm.ajustarIndiceHaptica(true)
                    vm.ajustarIndiceIncluirEnie(true)
                    vm.ajustarIndiceAnchoTactilDp(28f)
                    vm.ajustarIndiceTonoLetras(0.7f)
                    vm.avisar("Valores de tacto restablecidos")
                }
            )
        }
    }
}
