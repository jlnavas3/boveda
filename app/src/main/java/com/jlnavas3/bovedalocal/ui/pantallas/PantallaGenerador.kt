package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Pattern
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.quicksettings.GeneradorRapidoHelper
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorMultipleModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorMultipleModal
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.generador.PLANTILLAS_PATRON_RAPIDO
import com.jlnavas3.bovedalocal.ui.pantallas.generador.PanelModoAleatorio
import com.jlnavas3.bovedalocal.ui.pantallas.generador.PanelModoDiceware
import com.jlnavas3.bovedalocal.ui.pantallas.generador.TarjetaResultadoGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaGenerador(vm: VaultViewModel) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    var opciones by remember { mutableStateOf(OpcionesGenerador()) }
    var generada by remember { mutableStateOf("") }
    var generacion by remember { mutableIntStateOf(0) }

    fun regenerar() {
        generada = PasswordGenerator.generar(opciones)
        generacion++
    }

    LaunchedEffect(opciones) { regenerar() }

    val bits = PasswordGenerator.entropiaBits(opciones)
    val scrollState = rememberScrollState()

    val modoActual = when {
        opciones.modoFrase -> "Frase Diceware"
        opciones.modoPatron -> "Por Patrón"
        else -> "Aleatoria"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        // Cabecera superior con botones redondeados
        BarraSuperiorPantalla(
            titulo = "Generador",
            alVolver = { vm.volverAtras() },
            conSeparador = scrollState.value > 0,
            colorFondo = ColorAjustesFondo,
            acciones = {
                // Botón Regenerar
                IconButton(
                    onClick = {
                        haptica.toque()
                        regenerar()
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Regenerar contraseña",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(5.dp))

                // Botón Copiar al portapapeles
                IconButton(
                    onClick = {
                        haptica.exito()
                        GeneradorRapidoHelper.registrarEnHistorial(contexto, generada, "Generador")
                        vm.copiar("Contraseña", generada, sensible = true)
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = "Copiar contraseña",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(5.dp))

                // Botón Crear nueva entrada con la clave
                IconButton(
                    onClick = {
                        GeneradorRapidoHelper.registrarEnHistorial(contexto, generada, "Generador")
                        vm.ir(Pantalla.Editar(null, generada))
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(ColorTarjetaAjustes)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Crear nueva entrada con esta contraseña",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            DescripcionPantalla(subtitulo = "Genera contraseñas fuertes o frases Diceware en local")
            Spacer(Modifier.height(10.dp))

            // 1. Tarjeta: Resultado y Entropía en tiempo real
            TarjetaResultadoGenerador(
                generada = generada,
                generacion = generacion,
                bits = bits,
                haptica = haptica
            )

            Spacer(Modifier.height(18.dp))

            // 2. Tarjeta agrupada: Configuración de Parámetros con Selectores Modales
            ComponenteGrupo(etiqueta = "Configuración del generador") {
                // Selector Modal: Modo de generación (fijado abajo para no tapar el resultado)
                val iconoModo = when {
                    opciones.modoFrase -> Icons.AutoMirrored.Filled.MenuBook
                    opciones.modoPatron -> Icons.Filled.Pattern
                    else -> Icons.Filled.Shuffle
                }
                val opcionesModo = remember {
                    listOf(
                        OpcionSelectorModal("Aleatoria", "Aleatoria", "Aleatoria", "Caracteres alfanuméricos y símbolos", Icons.Filled.Shuffle),
                        OpcionSelectorModal("Frase Diceware", "Frase Diceware", "Frase Diceware", "Palabras memorables de alta entropía", Icons.AutoMirrored.Filled.MenuBook),
                        OpcionSelectorModal("Por Patrón", "Por Patrón", "Por Patrón", "Estructura y plantillas personalizadas", Icons.Filled.Pattern)
                    )
                }
                ComponenteSelectorModal(
                    titulo = "Modo de generación",
                    tituloFila = "Modo",
                    descripcionModal = "Elige la estrategia para forjar tu contraseña",
                    icono = iconoModo,
                    colorIcono = ColorGenerador,
                    valorSeleccionado = modoActual,
                    opciones = opcionesModo,
                    alSeleccionar = { nuevoModo ->
                        haptica.tic()
                        opciones = when (nuevoModo) {
                            "Frase Diceware" -> opciones.copy(modoFrase = true, modoPatron = false)
                            "Por Patrón" -> opciones.copy(modoFrase = false, modoPatron = true)
                            else -> opciones.copy(modoFrase = false, modoPatron = false)
                        }
                    },
                    fijarAbajo = true
                )

                ComponenteSeparador()

                when {
                    opciones.modoFrase -> {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                            PanelModoDiceware(
                                opciones = opciones,
                                alCambiarOpciones = { opciones = it },
                                haptica = haptica
                            )
                        }
                    }
                    opciones.modoPatron -> {
                        val opcionesPlantilla = remember {
                            PLANTILLAS_PATRON_RAPIDO.map { (nombre, patron) ->
                                OpcionSelectorModal(
                                    valor = patron,
                                    etiquetaFila = nombre,
                                    etiquetaModal = nombre,
                                    descripcionModal = patron,
                                    icono = Icons.Filled.Pattern
                                )
                            }
                        }
                        ComponenteSelectorModal(
                            titulo = "Plantilla rápida",
                            tituloFila = "Plantilla",
                            descripcionModal = "Formatos y estructuras comunes predefinidas",
                            icono = Icons.Filled.AutoAwesome,
                            colorIcono = ColorGenerador,
                            valorSeleccionado = opciones.patron,
                            opciones = opcionesPlantilla,
                            alSeleccionar = { nuevaPlantilla ->
                                haptica.tic()
                                opciones = opciones.copy(patron = nuevaPlantilla)
                            },
                            fijarAbajo = true
                        )

                        ComponenteSeparador()

                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                            CampoBoveda(
                                valor = opciones.patron,
                                etiqueta = "Máscara / Patrón personalizado",
                                alCambiar = { opciones = opciones.copy(patron = it) },
                                monoespaciada = true
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "X: alfanumérica (A-Z, 0-9) | A: mayúscula (A-Z) | a: minúscula (a-z) | 9 o d: dígito (0-9) | w: palabra Diceware",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    else -> {
                        // Modo Aleatorio: Selector múltiple de caracteres fijado abajo
                        val activos = buildList {
                            if (opciones.mayusculas) add("A-Z")
                            if (opciones.minusculas) add("a-z")
                            if (opciones.digitos) add("0-9")
                            if (opciones.simbolos) add("#$!")
                        }
                        val textoResumen = when {
                            activos.isEmpty() -> "Ninguno"
                            activos.size == 4 -> "Todos (4)"
                            else -> activos.joinToString(" ")
                        }
                        val opcionesCaracteres = listOf(
                            OpcionSelectorMultipleModal("mayusculas", "Mayúsculas (A-Z)", "Mayúsculas (A-Z)", "A, B, C... Z", Icons.Filled.TextFields, opciones.mayusculas),
                            OpcionSelectorMultipleModal("minusculas", "Minúsculas (a-z)", "Minúsculas (a-z)", "a, b, c... z", Icons.Filled.TextFields, opciones.minusculas),
                            OpcionSelectorMultipleModal("digitos", "Números (0-9)", "Números (0-9)", "0, 1, 2... 9", Icons.Filled.Numbers, opciones.digitos),
                            OpcionSelectorMultipleModal("simbolos", "Símbolos (#$!)", "Símbolos (#$!)", "! @ # $ % & * - _ + =", Icons.Filled.Tag, opciones.simbolos)
                        )

                        ComponenteSelectorMultipleModal(
                            titulo = "Caracteres permitidos",
                            tituloFila = "Caracteres",
                            valorTexto = textoResumen,
                            opciones = opcionesCaracteres,
                            alAlternar = { clave ->
                                haptica.tic()
                                opciones = when (clave) {
                                    "mayusculas" -> opciones.copy(mayusculas = !opciones.mayusculas)
                                    "minusculas" -> opciones.copy(minusculas = !opciones.minusculas)
                                    "digitos" -> opciones.copy(digitos = !opciones.digitos)
                                    "simbolos" -> opciones.copy(simbolos = !opciones.simbolos)
                                    else -> opciones
                                }
                            },
                            icono = Icons.Filled.Tune,
                            colorIcono = ColorGenerador,
                            descripcionModal = "Conjuntos de caracteres incluidos en la clave",
                            fijarAbajo = true
                        )

                        ComponenteSeparador()

                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                            PanelModoAleatorio(
                                opciones = opciones,
                                alCambiarOpciones = { opciones = it },
                                haptica = haptica
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
