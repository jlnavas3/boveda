package com.jlnavas3.bovedalocal.ui.pantallas.ajustes.widget

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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.widget.WidgetGeneradorRapido
import com.jlnavas3.bovedalocal.widget.WidgetPinHelper

/**
 * Pantalla atómica dedicada al Widget Generador 1x1.
 */
@Composable
fun PantallaAjustesWidget1x1(
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
                titulo = "Widget Generador 1x1",
                idEtiqueta = "04-HER-WGT-1X1",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("04-HER-WGT-G02", "Widget generador 1x1")
                        )
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Generador rápido en pantalla de inicio, modo de clave y respuesta táctil")
                Spacer(Modifier.height(10.dp))

                ComponenteGrupo(
                    etiqueta = "Widget generador 1x1",
                    idGrupo = "04-HER-WGT-G02",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    PreviaWidget1x1Compacta(ajustes = ajustes)

                    ComponenteSeparador()

                    ComponenteNavegacion(
                        titulo = "Añadir al escritorio",
                        icono = null,
                        idFila = "04-HER-WGT-PIN-1X1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.tic()
                            WidgetPinHelper.solicitarColocarWidget(contexto, WidgetGeneradorRapido::class.java)
                        }
                    )

                    ComponenteSeparador(sangriaInicio = 16.dp)

                    ComponenteNavegacion(
                        titulo = "Modo de generación",
                        valorTexto = if (ajustes.widget1x1Modo == "aleatoria") "${ajustes.widget1x1Longitud} car." else "Patrón",
                        icono = null,
                        idFila = "04-HER-WGT-MOD",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.tic()
                            vm.ir(Pantalla.Widget1x1Modo("04-HER-WGT-MOD"))
                        }
                    )

                    ComponenteSeparador(sangriaInicio = 16.dp)

                    ComponenteNavegacion(
                        titulo = "Comportamiento y vibración",
                        valorTexto = if (ajustes.widget1x1Haptica) "Vibración activa" else "Silencioso",
                        icono = null,
                        idFila = "04-HER-WGT-CMP",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.tic()
                            vm.ir(Pantalla.Widget1x1Comportamiento("04-HER-WGT-CMP"))
                        }
                    )

                    ComponenteSeparador(sangriaInicio = 16.dp)

                    ComponenteNavegacion(
                        titulo = "Calibración visual",
                        valorTexto = "Opacidad y formato",
                        icono = null,
                        idFila = "04-HER-WGT-1X1-CAL",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.tic()
                            vm.ir(Pantalla.CalibracionWidget1x1("04-HER-WGT-1X1"))
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@BovedaPantallaPreview
@Composable
private fun PantallaAjustesWidget1x1Preview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Widget Generador 1x1",
                idEtiqueta = "04-HER-WGT-1X1",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Generador rápido en pantalla de inicio, modo de clave y respuesta táctil")
        }
    }
}
