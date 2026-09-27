package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.IndiceAlfabetico
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.indice.SeccionCirculoCresta
import com.jlnavas3.bovedalocal.ui.pantallas.indice.SeccionEfectoOla
import com.jlnavas3.bovedalocal.ui.pantallas.indice.SeccionResaltadoDeslizar
import com.jlnavas3.bovedalocal.ui.pantallas.indice.SeccionTactoYHapticaIndice
import com.jlnavas3.bovedalocal.ui.pantallas.indice.VistaPreviaIndiceInteractiva
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla dedicada a la configuración y personalización del abecedario lateral.
 * Dispone de la barra de letras real montada en el extremo derecho de la pantalla a altura completa,
 * permitiendo calibrar la ola, las escalas, el alcance, los tonos y la 'Ñ' con interacción táctil en vivo.
 */
@Composable
fun PantallaAjustesIndice(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    var letraArrastrada by remember { mutableStateOf<Char?>('G') }
    val scrollState = rememberScrollState()

    val mockItems = remember {
        listOf(
            "Amazon" to "Compras y suscripción",
            "Apple" to "ID de Apple y iCloud",
            "GitHub" to "Cuenta de desarrollo",
            "Google" to "admin@gmail.com",
            "Netflix" to "Suscripción familiar",
            "Ñandú" to "Cuenta de prueba en español",
            "Spotify" to "Música y podcasts",
            "Twitter" to "@usuario_boveda",
            "Zara" to "Moda y calzado"
        )
    }

    ProveedorResaltadoAjustes(seccionId) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                BarraSuperiorPantalla(
                    titulo = "Abecedario lateral",
                    idEtiqueta = "03.4",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alVolver = { vm.volverAtras() },
                    conSeparador = scrollState.value > 0,
                    colorFondo = ColorAjustesFondo
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(start = 16.dp, end = 48.dp, top = 12.dp, bottom = 48.dp)
                ) {
                    DescripcionPantalla(subtitulo = "Personaliza la ola, escalas, háptica y apariencia")
                    Spacer(Modifier.height(10.dp))

                    // 1. Vista previa interactiva
                    VistaPreviaIndiceInteractiva(
                        ajustes = ajustes,
                        letraArrastrada = letraArrastrada,
                        mockItems = mockItems
                    )

                    Spacer(Modifier.height(14.dp))

                    // 2. Efecto de ola Niagara
                    SeccionEfectoOla(
                        ajustes = ajustes,
                        vm = vm,
                        haptica = haptica
                    )

                    Spacer(Modifier.height(14.dp))

                    // 3. Círculo en la cresta
                    SeccionCirculoCresta(
                        ajustes = ajustes,
                        vm = vm,
                        haptica = haptica
                    )

                    Spacer(Modifier.height(14.dp))

                    // 4. Tacto, háptica y contraste
                    SeccionTactoYHapticaIndice(
                        ajustes = ajustes,
                        vm = vm,
                        haptica = haptica
                    )

                    Spacer(Modifier.height(14.dp))

                    // 5. Resaltado de entradas al arrastrar
                    SeccionResaltadoDeslizar(
                        ajustes = ajustes,
                        vm = vm,
                        haptica = haptica
                    )

                    Spacer(Modifier.height(14.dp))

                    ComponenteGrupo {
                        ComponenteBotonFila(
                            titulo = "Restablecer módulo",
                            alPulsar = {
                                vm.restablecerAjustesIndiceAlfabetico()
                            }
                        )
                    }

                    Spacer(Modifier.height(32.dp))
                }
            }

            // Barra lateral real en vivo
            IndiceAlfabetico(
                alSeleccionarLetra = { },
                alCambiarLetraActiva = { letraArrastrada = it },
                incluirEnie = ajustes.indiceIncluirEnie,
                efectoOla = ajustes.indiceEfectoOla,
                amplitudOlaDp = ajustes.indiceAmplitudOlaDp,
                radioOlaDp = ajustes.indiceRadioOlaDp,
                escalaMaximaLetras = ajustes.indiceEscalaLetras,
                mostrarCirculo = ajustes.indiceMostrarCirculo,
                tamanoCirculoDp = ajustes.indiceTamanoCirculoDp,
                offsetCirculoDp = ajustes.indiceOffsetCirculoDp,
                hapticaActiva = ajustes.indiceHaptica,
                anchoZonaTactilDp = ajustes.indiceAnchoTactilDp,
                tonoLetras = ajustes.indiceTonoLetras,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 150.dp, bottom = 100.dp)
                    .fillMaxHeight()
            )
        }
    }
}
