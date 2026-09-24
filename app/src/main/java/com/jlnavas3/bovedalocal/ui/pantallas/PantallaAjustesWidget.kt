package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Pattern
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.LocalCoordinadorResaltado
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.contenedorScrollAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SelectorPlantillaPatron
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun PantallaAjustesWidget(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var pestanaWidget by remember { mutableIntStateOf(vm.ultimoWidgetAjustesSeleccionado) }

    LaunchedEffect(pestanaWidget) {
        vm.ultimoWidgetAjustesSeleccionado = pestanaWidget
    }

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        val coordinador = LocalCoordinadorResaltado.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Widgets de escritorio",
                idEtiqueta = "03.3",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .contenedorScrollAjustes(coordinador)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Personalización, calibración visual y respuesta táctil de widgets")
                Spacer(Modifier.height(10.dp))

                // Selector de Widgets al estilo de Animación de pantalla bloqueada
                ComponenteGrupo(
                    etiqueta = "Widgets de escritorio",
                    idGrupo = "03.3.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Elige el widget para ajustar su comportamiento y calibrar su aspecto"
                ) {
                    ComponenteRadio(
                        titulo = "Códigos 2FA",
                        icono = Icons.Filled.Timer,
                        colorIcono = Color2FA,
                        seleccionado = pestanaWidget == 0,
                        idFila = "03.3.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alSeleccionar = {
                            haptica.tic()
                            pestanaWidget = 0
                            vm.ultimoWidgetAjustesSeleccionado = 0
                        }
                    )
                    ComponenteSeparador()
                    ComponenteRadio(
                        titulo = "Generador 1x1",
                        icono = Icons.Filled.Key,
                        colorIcono = ColorGenerador,
                        seleccionado = pestanaWidget == 1,
                        idFila = "03.3.2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alSeleccionar = {
                            haptica.tic()
                            pestanaWidget = 1
                            vm.ultimoWidgetAjustesSeleccionado = 1
                        }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Calibración",
                        icono = Icons.Filled.Tune,
                        colorIcono = ColorIconosInternos,
                        idFila = "03.3.3",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.tic()
                            if (pestanaWidget == 0) {
                                vm.ir(Pantalla.CalibracionWidgetTotp())
                            } else {
                                vm.ir(Pantalla.CalibracionWidget1x1())
                            }
                        }
                    )
                }

                Spacer(Modifier.height(18.dp))

                if (pestanaWidget == 0) {
                    // ==========================================
                    // CONFIGURACIÓN FUNCIONAL: CÓDIGOS 2FA
                    // ==========================================
                    ComponenteGrupo(
                        etiqueta = "Respuesta táctil",
                        idGrupo = "03.3.G2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        descripcion = "Vibración háptica al tocar una cuenta para copiar el código TOTP"
                    ) {
                        ComponenteSwitch(
                            titulo = "Vibración al pulsar",
                            icono = Icons.Filled.Vibration,
                            colorIcono = ColorIconosInternos,
                            idFila = "03.3.4",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            activo = ajustes.widgetHaptica,
                            alCambiar = {
                                haptica.tic()
                                vm.ajustarWidgetHaptica(it)
                            }
                        )

                        if (ajustes.widgetHaptica) {
                            ComponenteSeparador()

                            ComponenteSlider(
                                titulo = "Intensidad de vibración",
                                valor = ajustes.widgetHapticaIntensidad,
                                valorTexto = "${(ajustes.widgetHapticaIntensidad * 100).roundToInt()}%",
                                rango = 0.01f..1.0f,
                                pasos = 99,
                                etiquetaMin = "1% (Mínima)",
                                etiquetaMax = "100%",
                                idFila = "03.3.5",
                                mostrarId = ajustes.mostrarIdsAjustes,
                                icono = Icons.Filled.Vibration,
                                colorIcono = ColorIconosInternos,
                                alCambiar = {
                                    vm.ajustarWidgetHapticaIntensidad(it)
                                    haptica.probar(it)
                                }
                            )

                            ComponenteSeparador()

                            ComponenteBotonFila(
                                titulo = "Probar vibración del widget",
                                icono = Icons.Filled.Vibration,
                                colorIcono = ColorIconosInternos,
                                alPulsar = {
                                    haptica.probar(ajustes.widgetHapticaIntensidad)
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    ComponenteGrupo {
                        ComponenteBotonFila(
                            titulo = "Restablecer valores del widget 2FA",
                            icono = Icons.AutoMirrored.Filled.RotateLeft,
                            colorIcono = ColorIconosInternos,
                            alPulsar = {
                                haptica.exito()
                                vm.restablecerAjustesWidget()
                                vm.avisar("Ajustes del widget 2FA restablecidos")
                            }
                        )
                    }
                } else {
                    // ==========================================
                    // CONFIGURACIÓN FUNCIONAL: GENERADOR 1X1
                    // ==========================================
                    ComponenteGrupo(
                        etiqueta = "Modo de generación",
                        idGrupo = "03.3.G2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        descripcion = "Elige cómo se creará la clave al pulsar el widget"
                    ) {
                        ComponenteRadio(
                            titulo = "Generación aleatoria",
                            icono = Icons.Filled.Refresh,
                            colorIcono = ColorIconosInternos,
                            idFila = "03.3.6",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            seleccionado = ajustes.widget1x1Modo == "aleatoria",
                            alSeleccionar = {
                                haptica.tic()
                                vm.ajustarWidget1x1Modo("aleatoria")
                            }
                        )

                        ComponenteSeparador()

                        ComponenteRadio(
                            titulo = "Por patrón",
                            icono = Icons.Filled.Pattern,
                            colorIcono = ColorIconosInternos,
                            idFila = "03.3.7",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            seleccionado = ajustes.widget1x1Modo == "patron",
                            alSeleccionar = {
                                haptica.tic()
                                vm.ajustarWidget1x1Modo("patron")
                            }
                        )

                        if (ajustes.widget1x1Modo == "aleatoria") {
                            ComponenteSeparador()

                            ComponenteSlider(
                                titulo = "Longitud de contraseña",
                                valor = ajustes.widget1x1Longitud.toFloat(),
                                valorTexto = "${ajustes.widget1x1Longitud} caracteres",
                                rango = 4f..64f,
                                pasos = 59,
                                etiquetaMin = "4",
                                etiquetaMax = "64",
                                idFila = "03.3.8",
                                mostrarId = ajustes.mostrarIdsAjustes,
                                icono = Icons.Filled.Numbers,
                                colorIcono = ColorIconosInternos,
                                alCambiar = {
                                    vm.ajustarWidget1x1Longitud(it.roundToInt())
                                    haptica.tic()
                                }
                            )

                            ComponenteSeparador()

                            var mostrarCampoSimbolos1x1 by remember { mutableStateOf(false) }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            haptica.tic()
                                            mostrarCampoSimbolos1x1 = !mostrarCampoSimbolos1x1
                                        }
                                        .padding(vertical = 4.dp, horizontal = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (mostrarCampoSimbolos1x1) "Ocultar símbolos personalizados" else "Personalizar símbolos permitidos",
                                        color = ColorTitulos,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                                    )
                                    Icon(
                                        imageVector = if (mostrarCampoSimbolos1x1) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                        contentDescription = if (mostrarCampoSimbolos1x1) "Ocultar" else "Mostrar",
                                        tint = TextoSecundario,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                if (mostrarCampoSimbolos1x1) {
                                    Spacer(Modifier.height(6.dp))
                                    CampoBoveda(
                                        valor = ajustes.widget1x1Simbolos,
                                        etiqueta = "Símbolos permitidos (#$!)",
                                        alCambiar = { vm.ajustarWidget1x1Simbolos(it) },
                                        monoespaciada = true,
                                        trailingIcon = {
                                            Icon(
                                                imageVector = Icons.Filled.RestartAlt,
                                                contentDescription = "Restaurar símbolos por defecto",
                                                tint = ColorIconosInternos,
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .clip(CircleShape)
                                                    .clickable {
                                                        haptica.exito()
                                                        vm.ajustarWidget1x1Simbolos(PasswordGenerator.SIMBOLOS)
                                                    }
                                            )
                                        }
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        "Edita o excluye símbolos no soportados por ciertos servicios",
                                        color = TextoSecundario,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        } else {
                            ComponenteSeparador()

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Plantillas rápidas:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextoSecundario
                                )
                                Spacer(Modifier.height(6.dp))
                                SelectorPlantillaPatron(
                                    patronActual = ajustes.widget1x1Patron,
                                    alSeleccionarPlantilla = { nuevoPatron ->
                                        haptica.tic()
                                        vm.ajustarWidget1x1Patron(nuevoPatron)
                                    }
                                )
                                Spacer(Modifier.height(12.dp))
                                CampoBoveda(
                                    valor = ajustes.widget1x1Patron,
                                    etiqueta = "Patrón personalizado (C: mayús, c: minús, d: dígito, s: símb)",
                                    alCambiar = { vm.ajustarWidget1x1Patron(it) },
                                    monoespaciada = true
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    ComponenteGrupo(
                        etiqueta = "Respuesta táctil",
                        idGrupo = "03.3.G3",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        descripcion = "Vibración háptica al pulsar el widget en la pantalla de inicio"
                    ) {
                        ComponenteSwitch(
                            titulo = "Vibración al generar",
                            icono = Icons.Filled.Vibration,
                            colorIcono = ColorIconosInternos,
                            idFila = "03.3.9",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            activo = ajustes.widget1x1Haptica,
                            alCambiar = {
                                haptica.tic()
                                vm.ajustarWidget1x1Haptica(it)
                            }
                        )

                        if (ajustes.widget1x1Haptica) {
                            ComponenteSeparador()

                            ComponenteSlider(
                                titulo = "Intensidad de vibración",
                                valor = ajustes.widget1x1HapticaIntensidad,
                                valorTexto = "${(ajustes.widget1x1HapticaIntensidad * 100).roundToInt()}%",
                                rango = 0.01f..1.0f,
                                pasos = 99,
                                etiquetaMin = "1% (Mínima)",
                                etiquetaMax = "100%",
                                idFila = "03.3.10",
                                mostrarId = ajustes.mostrarIdsAjustes,
                                icono = Icons.Filled.Vibration,
                                colorIcono = ColorIconosInternos,
                                alCambiar = {
                                    vm.ajustarWidget1x1HapticaIntensidad(it)
                                    haptica.probar(it)
                                }
                            )

                            ComponenteSeparador()

                            ComponenteBotonFila(
                                titulo = "Probar vibración del widget 1x1",
                                icono = Icons.Filled.Vibration,
                                colorIcono = ColorIconosInternos,
                                alPulsar = {
                                    haptica.probar(ajustes.widget1x1HapticaIntensidad)
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    ComponenteGrupo(
                        etiqueta = "Acciones automáticas",
                        idGrupo = "03.3.G4",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        descripcion = "Comportamiento del sistema tras generar la clave"
                    ) {
                        ComponenteSwitch(
                            titulo = "Copiar al portapapeles",
                            icono = Icons.Filled.ContentCopy,
                            colorIcono = ColorIconosInternos,
                            idFila = "03.3.11",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            activo = ajustes.widget1x1CopiarPortapapeles,
                            alCambiar = {
                                haptica.tic()
                                vm.ajustarWidget1x1CopiarPortapapeles(it)
                            }
                        )

                        ComponenteSeparador()

                        ComponenteSwitch(
                            titulo = "Mostrar notificación toast",
                            icono = Icons.Filled.Notifications,
                            colorIcono = ColorIconosInternos,
                            idFila = "03.3.12",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            activo = ajustes.widget1x1MostrarToast,
                            alCambiar = {
                                haptica.tic()
                                vm.ajustarWidget1x1MostrarToast(it)
                            }
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    ComponenteGrupo {
                        ComponenteBotonFila(
                            titulo = "Restablecer valores del widget 1x1",
                            icono = Icons.AutoMirrored.Filled.RotateLeft,
                            colorIcono = ColorIconosInternos,
                            alPulsar = {
                                haptica.exito()
                                vm.restablecerAjustesWidget1x1()
                                vm.avisar("Ajustes del widget 1x1 restablecidos")
                            }
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
