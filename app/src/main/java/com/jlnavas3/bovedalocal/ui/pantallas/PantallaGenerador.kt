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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.quicksettings.GeneradorRapidoHelper
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.generador.BarraSuperiorGenerador
import com.jlnavas3.bovedalocal.ui.pantallas.generador.PanelModoDiceware
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SeccionConfiguracionAleatoria
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SeccionConfiguracionPatron
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SelectorModoEstrategia
import com.jlnavas3.bovedalocal.ui.pantallas.generador.TarjetaResultadoGenerador
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

    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val bits = PasswordGenerator.entropiaBits(opciones)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorGenerador(
            alVolver = { vm.volverAtras() },
            conSeparador = scrollState.value > 0,
            alRegenerar = {
                haptica.toque()
                regenerar()
            },
            alCopiar = {
                haptica.exito()
                GeneradorRapidoHelper.registrarEnHistorial(contexto, generada, "Generador")
                vm.copiar("Contraseña", generada, sensible = true)
            },
            alCrearEntrada = {
                GeneradorRapidoHelper.registrarEnHistorial(contexto, generada, "Generador")
                vm.ir(Pantalla.Editar(null, generada))
            },
            idEtiqueta = "04-HER-GEN",
            mostrarId = ajustes.mostrarIdsAjustes
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
                SelectorModoEstrategia(
                    opciones = opciones,
                    alCambiarOpciones = { opciones = it },
                    haptica = haptica
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
                        SeccionConfiguracionPatron(
                            opciones = opciones,
                            alCambiarOpciones = { opciones = it },
                            haptica = haptica
                        )
                    }
                    else -> {
                        SeccionConfiguracionAleatoria(
                            opciones = opciones,
                            alCambiarOpciones = { opciones = it },
                            haptica = haptica
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
