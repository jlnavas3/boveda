package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
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
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.generador.PanelModoAleatorio
import com.jlnavas3.bovedalocal.ui.pantallas.generador.PanelModoDiceware
import com.jlnavas3.bovedalocal.ui.pantallas.generador.PanelModoPatron
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SelectorCaracteresGenerador
import com.jlnavas3.bovedalocal.ui.pantallas.generador.SelectorModoGenerador
import com.jlnavas3.bovedalocal.ui.pantallas.generador.TarjetaResultadoGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
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
    val espaciado = EspaciadoComponentes.coerceAtLeast(10.dp)

    val modoActual = when {
        opciones.modoFrase -> "Frase Diceware"
        opciones.modoPatron -> "Por Patrón"
        else -> "Aleatoria"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        CabeceraPantalla(
            titulo = "Generador",
            subtitulo = "Genera contraseñas fuertes o frases Diceware en local",
            alVolver = { vm.volverAtras() }
        )

        // 1. Tarjeta: Resultado y Entropía
        TarjetaResultadoGenerador(
            generada = generada,
            generacion = generacion,
            bits = bits,
            haptica = haptica
        )

        Spacer(Modifier.height(espaciado))

        // 2. Tarjeta: Configuración de Parámetros
        TarjetaBoveda {
            EtiquetaSeccion("Configuración del Generador")
            Spacer(Modifier.height(10.dp))

            // Selectores desplegables: Caracteres (50%) a la izquierda y Modo de generación (50%) a la derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SelectorCaracteresGenerador(
                    opciones = opciones,
                    alCambiarOpciones = { opciones = it },
                    haptica = haptica,
                    habilitado = !opciones.modoFrase && !opciones.modoPatron,
                    modifier = Modifier.weight(1f)
                )

                SelectorModoGenerador(
                    modoActual = modoActual,
                    alSeleccionarModo = { nuevoModo ->
                        haptica.tic()
                        opciones = when (nuevoModo) {
                            "Frase Diceware" -> opciones.copy(modoFrase = true, modoPatron = false)
                            "Por Patrón" -> opciones.copy(modoFrase = false, modoPatron = true)
                            else -> opciones.copy(modoFrase = false, modoPatron = false)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(14.dp))

            when {
                opciones.modoFrase -> {
                    PanelModoDiceware(
                        opciones = opciones,
                        alCambiarOpciones = { opciones = it },
                        haptica = haptica
                    )
                }
                opciones.modoPatron -> {
                    PanelModoPatron(
                        opciones = opciones,
                        alCambiarOpciones = { opciones = it },
                        haptica = haptica
                    )
                }
                else -> {
                    PanelModoAleatorio(
                        opciones = opciones,
                        alCambiarOpciones = { opciones = it },
                        haptica = haptica
                    )
                }
            }
        }

        Spacer(Modifier.height(espaciado))

        // 3. Botones de acción inferiores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BotonColorido(
                texto = "Regenerar",
                color = ColorGenerador,
                icono = Icons.Filled.Refresh,
                modifier = Modifier.weight(1f)
            ) {
                haptica.toque()
                regenerar()
            }

            BotonColorido(
                texto = "Copiar",
                color = ColorSeguridad,
                icono = Icons.Filled.ContentCopy,
                modifier = Modifier.weight(1f)
            ) {
                haptica.exito()
                vm.copiar("Contraseña", generada, sensible = true)
            }
        }

        Spacer(Modifier.height(10.dp))

        BotonBorde(
            texto = "Crear nueva entrada",
            color = ColorTitulos,
            icono = Icons.Filled.Add
        ) {
            vm.ir(Pantalla.Editar(null, generada))
        }

        Spacer(Modifier.height(32.dp))
    }
}
