/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoSuperficie
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.pantallas.generador.PanelModoDiceware
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SeccionConfiguracionAleatoria
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SeccionConfiguracionPatron
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SelectorModoEstrategia
import com.jlnavas3.bovedalocal.ui.pantallas.generador.TarjetaResultadoGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Modal interactivo completo para la generación asistida de la contraseña maestra durante el onboarding.
 * Ofrece selección entre contraseña aleatoria (hasta 128 caracteres), frase Diceware bilingüe o patrón,
 * visualización en vivo, cálculo de entropía, copia y resguardo mediante compartir.
 */
@Composable
fun ModalGeneradorAsistidoOnboarding(
    alSeleccionarContrasena: (String) -> Unit,
    alCerrar: () -> Unit
) {
    val contexto = LocalContext.current
    val portapapeles = LocalClipboardManager.current
    val haptica = remember { Haptica(contexto) }

    var opciones by remember {
        mutableStateOf(
            OpcionesGenerador(
                longitud = 20,
                mayusculas = true,
                minusculas = true,
                digitos = true,
                simbolos = true
            )
        )
    }

    var generada by remember { mutableStateOf("") }
    var generacion by remember { mutableIntStateOf(0) }

    fun regenerar() {
        generada = PasswordGenerator.generar(opciones)
        generacion++
    }

    LaunchedEffect(opciones) {
        regenerar()
    }

    val bits = PasswordGenerator.entropiaBits(opciones)

    ModalInferiorBoveda(
        abierto = true,
        alCerrar = alCerrar,
        fijarAbajo = false,
        titulo = "Generador Asistido",
        descripcion = "Elige la estrategia para tu contraseña maestra",
        icono = Icons.Filled.AutoFixHigh,
        colorIcono = ColorSobreAcento,
        fondoIcono = ColorAcento,
        mostrarBotonCerrar = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 480.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Resultado en vivo con medidor de entropía
            TarjetaResultadoGenerador(
                generada = generada,
                generacion = generacion,
                bits = bits,
                haptica = haptica
            )

            // Fila de acciones para la contraseña generada
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonBoveda(
                    texto = "Regenerar",
                    icono = Icons.Filled.Refresh,
                    variante = VarianteBoton.SECUNDARIO,
                    modifier = Modifier.weight(1f),
                    alPulsar = {
                        haptica.toque()
                        regenerar()
                    }
                )

                BotonIconoSuperficie(
                    icono = Icons.Filled.ContentCopy,
                    descripcion = "Copiar al portapapeles",
                    alPulsar = {
                        haptica.exito()
                        portapapeles.setText(AnnotatedString(generada))
                        Toast.makeText(contexto, "Contraseña copiada al portapapeles", Toast.LENGTH_SHORT).show()
                    }
                )

                BotonIconoSuperficie(
                    icono = Icons.Filled.Share,
                    descripcion = "Compartir o resguardar",
                    alPulsar = {
                        haptica.toque()
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Contraseña maestra - Bóveda Local")
                            putExtra(Intent.EXTRA_TEXT, generada)
                        }
                        contexto.startActivity(Intent.createChooser(intent, "Resguardar contraseña maestra"))
                    }
                )
            }

            // Selector de estrategia (Aleatoria, Diceware o Patrón)
            ComponenteGrupo(etiqueta = "Estrategia") {
                SelectorModoEstrategia(
                    opciones = opciones,
                    alCambiarOpciones = {
                        haptica.tic()
                        opciones = it
                    },
                    haptica = haptica
                )
            }

            // Opciones detalladas según el modo seleccionado
            when {
                opciones.modoFrase -> {
                    ComponenteGrupo(etiqueta = "Configuración Diceware") {
                        PanelModoDiceware(
                            opciones = opciones,
                            alCambiarOpciones = {
                                haptica.tic()
                                opciones = it
                            },
                            haptica = haptica,
                            alAbrirDiccionario = null
                        )
                    }
                }
                opciones.modoPatron -> {
                    ComponenteGrupo(etiqueta = "Configuración de Patrón") {
                        SeccionConfiguracionPatron(
                            opciones = opciones,
                            alCambiarOpciones = {
                                haptica.tic()
                                opciones = it
                            },
                            haptica = haptica
                        )
                    }
                }
                else -> {
                    ComponenteGrupo(etiqueta = "Configuración de Caracteres") {
                        SeccionConfiguracionAleatoria(
                            opciones = opciones,
                            alCambiarOpciones = {
                                haptica.tic()
                                opciones = it
                            },
                            haptica = haptica
                        )
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            // Botón de aplicación
            BotonBoveda(
                texto = "Usar esta contraseña maestra",
                icono = Icons.Filled.Check,
                variante = VarianteBoton.PRIMARIO,
                modifier = Modifier.fillMaxWidth(),
                alPulsar = {
                    haptica.exito()
                    alSeleccionarContrasena(generada)
                }
            )
        }
    }
}
