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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.recordarEstadoAlumbrado
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt

/**
 * Pantalla atómica dedicada a la calibración del efecto de alumbrado (glow) de navegación.
 */
@Composable
fun PantallaAvanzadaAlumbrado(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Alumbrado y navegación",
                idEtiqueta = "06-SIS-AVZ-LGT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("06-SIS-AVZ-G01", "Alumbrado")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.ajustarAlumbradoActivo(true)
                            vm.ajustarAlumbradoIntensidad(0.7f)
                            vm.ajustarAlumbradoRepeticiones(2)
                            vm.ajustarAlumbradoDuracionMs(600)
                            vm.avisar("Alumbrado restablecido")
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
                DescripcionPantalla(subtitulo = "Efecto de resplandor visual al retroceder entre niveles de ajustes")
                Spacer(Modifier.height(10.dp))

                val estadoPrueba = recordarEstadoAlumbrado("06-SIS-AVZ-TEST")

                ComponenteGrupo(
                    etiqueta = "Calibración del resplandor",
                    idGrupo = "06-SIS-AVZ-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSwitch(
                        titulo = "Alumbrado activo",
                        icono = null,
                        activo = ajustes.alumbradoActivo,
                        idFila = "06-SIS-AVZ-ALU",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarAlumbradoActivo(it)
                        }
                    )

                    if (ajustes.alumbradoActivo) {
                        ComponenteSeparador(sangriaInicio = 16.dp)

                        ComponenteSlider(
                            titulo = "Intensidad del resplandor",
                            valorTexto = "${(ajustes.alumbradoIntensidad * 100).roundToInt()}% de opacidad",
                            valor = ajustes.alumbradoIntensidad,
                            rango = 0.2f..1.0f,
                            pasos = 8,
                            icono = null,
                            idFila = "06-SIS-AVZ-INT",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = {
                                vm.ajustarAlumbradoIntensidad(it)
                                coroutineScope.launch {
                                    estadoPrueba.dispararEfectoAlumbrado(
                                        colorAcento = ColorAcento,
                                        activo = true,
                                        intensidad = it,
                                        repeticiones = 1,
                                        duracionMs = ajustes.alumbradoDuracionMs
                                    )
                                }
                            }
                        )

                        ComponenteSeparador(sangriaInicio = 16.dp)

                        ComponenteSlider(
                            titulo = "Pulsos de repetición",
                            valorTexto = when (ajustes.alumbradoRepeticiones) {
                                1 -> "1 pulso"
                                else -> "${ajustes.alumbradoRepeticiones} pulsos"
                            },
                            valor = ajustes.alumbradoRepeticiones.toFloat(),
                            rango = 1f..5f,
                            pasos = 3,
                            icono = null,
                            idFila = "06-SIS-AVZ-REP",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = {
                                val rep = it.roundToInt()
                                vm.ajustarAlumbradoRepeticiones(rep)
                                coroutineScope.launch {
                                    estadoPrueba.dispararEfectoAlumbrado(
                                        colorAcento = ColorAcento,
                                        activo = true,
                                        intensidad = ajustes.alumbradoIntensidad,
                                        repeticiones = rep,
                                        duracionMs = ajustes.alumbradoDuracionMs
                                    )
                                }
                            }
                        )

                        ComponenteSeparador(sangriaInicio = 16.dp)

                        ComponenteSlider(
                            titulo = "Duración de cada pulso",
                            valorTexto = String.format(Locale.ROOT, "%.1f segundos", ajustes.alumbradoDuracionMs / 1000f),
                            valor = ajustes.alumbradoDuracionMs.toFloat(),
                            rango = 300f..1500f,
                            pasos = 11,
                            icono = null,
                            idFila = "06-SIS-AVZ-DUR",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = {
                                val dur = it.roundToInt()
                                vm.ajustarAlumbradoDuracionMs(dur)
                                coroutineScope.launch {
                                    estadoPrueba.dispararEfectoAlumbrado(
                                        colorAcento = ColorAcento,
                                        activo = true,
                                        intensidad = ajustes.alumbradoIntensidad,
                                        repeticiones = 1,
                                        duracionMs = dur
                                    )
                                }
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
private fun PantallaAvanzadaAlumbradoPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Alumbrado y navegación",
                idEtiqueta = "06-SIS-AVZ-LGT",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Efecto de resplandor visual al retroceder entre niveles de ajustes")
        }
    }
}
