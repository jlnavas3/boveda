package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.indice.VistaPreviaIndiceInteractiva
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Hub de calibración del índice A-Z lateral.
 * Ofrece vista previa interactiva en vivo y accesos directos a las subpáginas
 * especializadas de parametrización.
 */
@Composable
fun PantallaAjustesIndice(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

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
                    titulo = "Índice A-Z",
                    idEtiqueta = "03-LST-AZX",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alVolver = { vm.volverAtras() },
                    conSeparador = scrollState.value > 0,
                    colorFondo = ColorAjustesFondo,
                    acciones = {
                        BotonMenuOpcionesPantalla(
                            grupos = listOf(
                                AccionSaltoGrupo("03-LST-AZX-G02", "Calibración lateral")
                            ),
                            alRestablecerPantalla = {
                                haptica.tic()
                                vm.restablecerAjustesIndiceAlfabetico()
                                vm.avisar("Índice A-Z restablecido")
                            }
                        )
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 48.dp)
                ) {
                    // Vista previa interactiva
                    VistaPreviaIndiceInteractiva(ajustes = ajustes)

                    Spacer(Modifier.height(14.dp))

                    // Grupo de navegación
                    ComponenteGrupo(
                        etiqueta = "Calibración lateral",
                        icono = Icons.AutoMirrored.Filled.Sort,
                        alRestablecer = {
                            haptica.tic()
                            vm.restablecerAjustesIndiceAlfabetico()
                            vm.avisar("Índice A-Z restablecido")
                        },
                        idGrupo = "03-LST-AZX-G02",
                        mostrarId = ajustes.mostrarIdsAjustes
                    ) {
                        ComponenteNavegacion(
                            titulo = "Efecto de ola",
                            icono = null,
                            idFila = "03.2.1",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            valorTexto = if (ajustes.indiceEfectoOla) "${ajustes.indiceAmplitudOlaDp}dp" else "Desactivado",
                            alPulsar = {
                                haptica.tic()
                                vm.ir(Pantalla.IndiceOla("03.2.1"))
                            }
                        )

                        ComponenteSeparador(sangriaInicio = 16.dp)

                        ComponenteNavegacion(
                            titulo = "Círculo y cresta",
                            icono = null,
                            idFila = "03.2.2",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            valorTexto = if (ajustes.indiceMostrarCirculo) "${ajustes.indiceTamanoCirculoDp}dp" else "Desactivado",
                            alPulsar = {
                                haptica.tic()
                                vm.ir(Pantalla.IndiceCresta("03.2.2"))
                            }
                        )

                        ComponenteSeparador(sangriaInicio = 16.dp)

                        ComponenteNavegacion(
                            titulo = "Tacto y háptica",
                            icono = null,
                            idFila = "03.2.3",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            valorTexto = if (ajustes.indiceHaptica) "Activa" else "Muda",
                            alPulsar = {
                                haptica.tic()
                                vm.ir(Pantalla.IndiceHaptica("03.2.3"))
                            }
                        )

                        ComponenteSeparador(sangriaInicio = 16.dp)

                        ComponenteNavegacion(
                            titulo = "Resaltado y selección",
                            icono = null,
                            idFila = "03.2.4",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            valorTexto = if (ajustes.indiceResaltarEntradas) "Activo" else "Desactivado",
                            alPulsar = {
                                haptica.tic()
                                vm.ir(Pantalla.IndiceResaltado("03.2.4"))
                            }
                        )
                    }

                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}
