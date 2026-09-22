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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.Vibration
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.recordarEstadoAlumbrado
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoBorrarBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoCambioMaestra
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaAvanzada(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var dialogoCambio by remember { mutableStateOf(false) }
    var dialogoBorrar by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Opciones avanzadas",
                idEtiqueta = "05.1",
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
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Gestión de credenciales maestras, diagnóstico y zona de peligro")
                Spacer(Modifier.height(10.dp))

                ComponenteGrupo(
                    etiqueta = "Credencial maestra",
                    idGrupo = "05.1.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Al cambiar la contraseña maestra, la base de datos se re-cifra en tiempo real con Argon2id"
                ) {
                    ComponenteNavegacion(
                        titulo = "Cambiar contraseña maestra",
                        icono = Icons.Filled.Lock,
                        colorIcono = ColorSeguridad,
                        idFila = "05.1.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { dialogoCambio = true }
                    )
                }

                Spacer(Modifier.height(18.dp))

                ComponenteGrupo(
                    etiqueta = "Desarrollo y referencia",
                    idGrupo = "05.1.G2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Muestra una etiqueta con el código ID jerárquico de cada opción para facilitar soporte y automatización"
                ) {
                    ComponenteSwitch(
                        titulo = "Identificadores de ajustes (IDs)",
                        icono = Icons.Filled.Tune,
                        colorIcono = Color(0xFFC2185B),
                        activo = ajustes.mostrarIdsAjustes,
                        idFila = "05.1.2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarMostrarIdsAjustes(it)
                        }
                    )
                    ComponenteSeparador()
                    ComponenteSwitch(
                        titulo = "Alumbrado de navegación",
                        icono = Icons.Filled.Highlight,
                        colorIcono = Color(0xFFE91E63),
                        activo = ajustes.alumbradoActivo,
                        idFila = "05.1.4",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarAlumbradoActivo(it)
                        }
                    )
                    if (ajustes.alumbradoActivo) {
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Intensidad del alumbrado",
                            valor = ajustes.alumbradoIntensidad,
                            valorTexto = "${(ajustes.alumbradoIntensidad * 100).roundToInt()}%",
                            rango = 0.1f..1.0f,
                            pasos = 8,
                            etiquetaMin = "10%",
                            etiquetaMax = "100%",
                            idFila = "05.1.5",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = {
                                vm.ajustarAlumbradoIntensidad(it)
                            }
                        )
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Número de alumbrados",
                            valor = ajustes.alumbradoRepeticiones.toFloat(),
                            valorTexto = if (ajustes.alumbradoRepeticiones == 1) "1 destello" else "${ajustes.alumbradoRepeticiones} destellos",
                            rango = 1f..5f,
                            pasos = 3,
                            etiquetaMin = "1",
                            etiquetaMax = "5",
                            idFila = "05.1.6",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = {
                                haptica.tic()
                                vm.ajustarAlumbradoRepeticiones(it.roundToInt())
                            }
                        )
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Duración por alumbrado",
                            valor = ajustes.alumbradoDuracionMs.toFloat(),
                            valorTexto = if (ajustes.alumbradoDuracionMs >= 1000) {
                                String.format(java.util.Locale.US, "%.1f s", ajustes.alumbradoDuracionMs / 1000f)
                            } else {
                                "${ajustes.alumbradoDuracionMs} ms"
                            },
                            rango = 300f..1500f,
                            pasos = 11,
                            etiquetaMin = "300 ms",
                            etiquetaMax = "1.5 s",
                            idFila = "05.1.7",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = {
                                vm.ajustarAlumbradoDuracionMs(it.roundToInt())
                            }
                        )
                        ComponenteSeparador()
                        val estadoPrueba = recordarEstadoAlumbrado("05.1.8")
                        val colorAcentoPrueba = ColorAcento
                        ComponenteNavegacion(
                            titulo = "Probar efecto de alumbrado",
                            icono = Icons.Filled.Highlight,
                            idFila = "05.1.8",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            estadoAlumbrado = estadoPrueba,
                            alPulsar = {
                                coroutineScope.launch {
                                    estadoPrueba.dispararEfectoAlumbrado(
                                        colorAcento = colorAcentoPrueba,
                                        activo = true,
                                        intensidad = ajustes.alumbradoIntensidad,
                                        repeticiones = ajustes.alumbradoRepeticiones,
                                        duracionMs = ajustes.alumbradoDuracionMs
                                    )
                                }
                            }
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                ComponenteGrupo(
                    etiqueta = "Respuesta táctil y vibración",
                    idGrupo = "05.1.G3",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Vibración háptica general al interactuar con botones, switches y controles"
                ) {
                    ComponenteSwitch(
                        titulo = "Vibración háptica en la app",
                        icono = Icons.Filled.Vibration,
                        colorIcono = ColorAcento,
                        idFila = "05.1.9",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        activo = ajustes.hapticaApp,
                        alCambiar = {
                            vm.ajustarHapticaApp(it)
                            if (it) haptica.probar(ajustes.hapticaAppIntensidad)
                        }
                    )

                    if (ajustes.hapticaApp) {
                        ComponenteSeparador()

                        ComponenteSlider(
                            titulo = "Intensidad de vibración",
                            valor = ajustes.hapticaAppIntensidad,
                            valorTexto = "${(ajustes.hapticaAppIntensidad * 100).roundToInt()}%",
                            rango = 0.01f..1.0f,
                            pasos = 99,
                            etiquetaMin = "1% (Mínima)",
                            etiquetaMax = "100%",
                            idFila = "05.1.10",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            icono = Icons.Filled.Vibration,
                            colorIcono = ColorAcento,
                            alCambiar = {
                                vm.ajustarHapticaAppIntensidad(it)
                                haptica.probar(it)
                            }
                        )

                        ComponenteSeparador()

                        ComponenteNavegacion(
                            titulo = "Probar vibración",
                            icono = Icons.Filled.Vibration,
                            colorIcono = ColorAcento,
                            idFila = "05.1.11",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alPulsar = {
                                haptica.probar(ajustes.hapticaAppIntensidad)
                            }
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                ComponenteGrupo(
                    etiqueta = "Zona de peligro",
                    idGrupo = "05.1.G4",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Eliminación irreversible e inmediata de todas las contraseñas, notas y configuraciones"
                ) {
                    ComponenteNavegacion(
                        titulo = "Borrar bóveda definitivamente",
                        icono = Icons.Filled.Delete,
                        colorIcono = ColorPapelera,
                        idFila = "05.1.12",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { dialogoBorrar = true }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (dialogoCambio) {
        DialogoCambioMaestra(
            alDescartar = { dialogoCambio = false },
            alConfirmar = { actual, nueva ->
                dialogoCambio = false
                vm.cambiarContrasenaMaestra(actual, nueva)
            }
        )
    }

    if (dialogoBorrar) {
        DialogoBorrarBoveda(
            alDescartar = { dialogoBorrar = false },
            alConfirmar = {
                dialogoBorrar = false
                vm.repositorio.borrarTodo()
                vm.ir(Pantalla.Onboarding)
            }
        )
    }
}
