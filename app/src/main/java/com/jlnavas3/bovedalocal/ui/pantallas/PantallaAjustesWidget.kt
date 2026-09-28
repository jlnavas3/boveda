package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Timer
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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.LocalCoordinadorResaltado
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.contenedorScrollAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaAjustesWidget(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        val coordinador = LocalCoordinadorResaltado.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Widgets",
                idEtiqueta = "04-HER-WGT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("04-HER-WGT-G01", "Widget de códigos 2FA"),
                            AccionSaltoGrupo("04-HER-WGT-G02", "Widget generador 1x1")
                        )
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .contenedorScrollAjustes(coordinador)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Grupo 1: Widget de códigos 2FA
                ComponenteGrupo(
                    etiqueta = "Widget de códigos 2FA",
                    icono = Icons.Filled.Timer,
                    colorIcono = Color2FA,
                    idGrupo = "04-HER-WGT-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Ajustes y respuesta táctil",
                        valorTexto = if (ajustes.widgetHaptica) "Vibración activa" else "Desactivada",
                        icono = null,
                        idFila = "04-HER-WGT-TOT",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.tic()
                            vm.ir(Pantalla.WidgetTotpAjustes("04-HER-WGT-TOT"))
                        }
                    )

                    ComponenteSeparador(sangriaInicio = 16.dp)

                    ComponenteNavegacion(
                        titulo = "Calibración visual",
                        valorTexto = "Opacidad y color",
                        icono = null,
                        idFila = "04-HER-WGT-CAL",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.tic()
                            vm.ir(Pantalla.CalibracionWidgetTotp("04-HER-WGT-CAL"))
                        }
                    )
                }

                Spacer(Modifier.height(14.dp))

                // Grupo 2: Widget generador 1x1
                ComponenteGrupo(
                    etiqueta = "Widget generador 1x1",
                    icono = Icons.Filled.Key,
                    colorIcono = ColorGenerador,
                    idGrupo = "04-HER-WGT-G02",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
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
                        idFila = "04-HER-WGT-1X1",
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
