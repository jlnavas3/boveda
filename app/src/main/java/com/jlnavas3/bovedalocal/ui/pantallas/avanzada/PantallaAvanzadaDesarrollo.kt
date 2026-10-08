package com.jlnavas3.bovedalocal.ui.pantallas.avanzada

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla atómica dedicada a los identificadores jerárquicos de desarrollo y colores de bloque.
 */
@Composable
fun PantallaAvanzadaDesarrollo(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Desarrollo e identificadores",
                idEtiqueta = "06-SIS-AVZ-DES",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("06-SIS-AVZ-G01", "Identificadores")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.ajustarMostrarIdsAjustes(false)
                            vm.avisar("Identificadores restablecidos")
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Identificadores de ajustes y personalización de colores por bloque")
                Spacer(Modifier.height(10.dp))

                ComponenteGrupo(
                    etiqueta = "Identificadores y referencia",
                    idGrupo = "06-SIS-AVZ-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSwitch(
                        titulo = "Identificadores de ajustes (IDs)",
                        icono = null,
                        activo = ajustes.mostrarIdsAjustes,
                        idFila = "06-SIS-AVZ-IDS",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarMostrarIdsAjustes(it)
                        }
                    )

                    if (ajustes.mostrarIdsAjustes) {
                        ComponenteSeparador(sangriaInicio = 16.dp)
                        ComponenteNavegacion(
                            titulo = "Colores de identificadores",
                            icono = null,
                            idFila = "06-SIS-AVZ-COL",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alPulsar = {
                                haptica.tic()
                                vm.ir(Pantalla.ColoresIdentificadores())
                            }
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@BovedaPantallaPreview
@Composable
private fun PantallaAvanzadaDesarrolloPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Desarrollo e identificadores",
                idEtiqueta = "06-SIS-AVZ-DES",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Identificadores de ajustes y personalización de colores por bloque")
        }
    }
}
