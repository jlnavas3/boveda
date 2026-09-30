package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.generador.BarraSuperiorGenerador
import com.jlnavas3.bovedalocal.ui.pantallas.generador.PanelModoDiceware
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SeccionConfiguracionAleatoria
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SeccionConfiguracionPatron
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SelectorModoEstrategia
import com.jlnavas3.bovedalocal.ui.pantallas.generador.TarjetaResultadoGenerador
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
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
    val formaFab = RoundedCornerShape(CurvaturaEsquinas)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BarraSuperiorGenerador(
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                alIrHistorial = { vm.ir(Pantalla.HistorialClaves("04-HER-HIS")) },
                alIrAjustesPortapapeles = { vm.ir(Pantalla.AjustesCopiaAutomatica("03.2.1")) },
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

                Spacer(Modifier.height(110.dp))
            }
        }

        // Botones de acción flotantes (FABs) en la esquina inferior derecha
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // FAB superior: Copiar contraseña
            SmallFloatingActionButton(
                onClick = {
                    haptica.exito()
                    GeneradorRapidoHelper.registrarEnHistorial(contexto, generada, "Generador")
                    vm.copiar("Contraseña", generada, sensible = true)
                },
                containerColor = ColorTarjetaAjustes,
                contentColor = Ambar,
                shape = formaFab,
                modifier = Modifier.then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                    } else Modifier
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = "Copiar contraseña",
                    modifier = Modifier.size(22.dp)
                )
            }

            // FAB inferior: Regenerar contraseña (Principal)
            FloatingActionButton(
                onClick = {
                    haptica.toque()
                    regenerar()
                },
                containerColor = ColorAcento,
                contentColor = ColorSobreAcento,
                shape = formaFab,
                modifier = Modifier.then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                    } else Modifier
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Regenerar contraseña",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

