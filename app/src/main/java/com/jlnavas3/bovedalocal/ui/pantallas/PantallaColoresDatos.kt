/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.colores.DialogoSelectorColorDato
import com.jlnavas3.bovedalocal.ui.pantallas.colores.GrupoDimensionesIndicadorDinamico
import com.jlnavas3.bovedalocal.ui.pantallas.colores.GrupoSelectoresColoresDatos
import com.jlnavas3.bovedalocal.ui.pantallas.colores.SelectorEstiloIndicador
import com.jlnavas3.bovedalocal.ui.pantallas.colores.TarjetaStickyPreviaColores
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla integral para configurar el estilo de los indicadores de tarjetas, sus dimensiones/apariencia
 * y la paleta cromática de los 6 tipos de credenciales/datos.
 */
@Composable
fun PantallaColoresDatos(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val entradaPrueba = remember {
        Entrada(
            id = "preview_test",
            titulo = "Cuenta de Prueba",
            usuario = "usuario@ejemplo.com",
            contrasena = "SuperClave123!",
            secretoTotp = "JBSWY3DPEHPK3PXP",
            passkey = DatosPasskey("ejemplo.com", "Ejemplo", "user", "cred123", "key123"),
            urls = listOf("https://ejemplo.com", "androidapp://com.ejemplo.app")
        )
    }

    var estiloTemporal by remember(ajustes.estiloIndicadores) { mutableStateOf(ajustes.estiloIndicadores) }
    var alturaTemporal by remember(ajustes.alturaIndicadoresDp) { mutableFloatStateOf(ajustes.alturaIndicadoresDp) }
    var opacidadTemporal by remember(ajustes.opacidadIndicadores) { mutableFloatStateOf(ajustes.opacidadIndicadores) }

    var colorSeleccionando by remember { mutableStateOf<String?>(null) }
    var colorInicialModal by remember { mutableStateOf(Color.White) }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Indicadores y colores de campos",
                idEtiqueta = seccionDestino ?: "02-APA-COL",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        alRestablecerPantalla = {
                            haptica.tic()
                            estiloTemporal = AjustesDefaults.ColoresDatos.ESTILO_INDICADORES
                            alturaTemporal = AjustesDefaults.ColoresDatos.ALTURA_INDICADORES_DP
                            opacidadTemporal = AjustesDefaults.ColoresDatos.OPACIDAD_INDICADORES
                            vm.restablecerColoresDatos()
                        },
                        mensajeToastRestablecer = "Indicadores y colores restablecidos"
                    )
                }
            )

            // Tarjeta de Vista Previa FLOTANTE SUPERIOR FIJA (Sticky Top con reactividad instantánea)
            TarjetaStickyPreviaColores(
                entradaPrueba = entradaPrueba,
                estiloIndicadores = estiloTemporal,
                alturaIndicadores = alturaTemporal.dp,
                opacidadIndicadores = opacidadTemporal
            )

            // Columna scrolleable de ajustes
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Selector de Estilo de Indicadores
                SelectorEstiloIndicador(
                    estiloActual = estiloTemporal,
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alSeleccionarEstilo = { nuevoEstilo ->
                        haptica.tic()
                        estiloTemporal = nuevoEstilo
                        vm.ajustarEstiloIndicadores(nuevoEstilo)
                    },
                    alRestablecer = {
                        haptica.tic()
                        estiloTemporal = AjustesDefaults.ColoresDatos.ESTILO_INDICADORES
                        vm.ajustarEstiloIndicadores(AjustesDefaults.ColoresDatos.ESTILO_INDICADORES)
                    }
                )

                Spacer(Modifier.height(14.dp))

                // Controles Contextuales Dinámicos según el estilo seleccionado
                GrupoDimensionesIndicadorDinamico(
                    estilo = estiloTemporal,
                    dimensionTemporal = alturaTemporal,
                    opacidadTemporal = opacidadTemporal,
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alCambiarDimension = {
                        alturaTemporal = it
                        vm.ajustarAlturaIndicadores(it)
                    },
                    alRestablecerDimension = {
                        haptica.tic()
                        val def = when (estiloTemporal) {
                            "iconos" -> 12.5f
                            "puntos" -> 5f
                            "lateral" -> 3.5f
                            else -> 2f
                        }
                        alturaTemporal = def
                        vm.ajustarAlturaIndicadores(def)
                    },
                    alCambiarOpacidad = {
                        opacidadTemporal = it
                        vm.ajustarOpacidadIndicadores(it)
                    },
                    alRestablecerOpacidad = {
                        haptica.tic()
                        opacidadTemporal = AjustesDefaults.ColoresDatos.OPACIDAD_INDICADORES
                        vm.ajustarOpacidadIndicadores(AjustesDefaults.ColoresDatos.OPACIDAD_INDICADORES)
                    },
                    alRestablecerGrupo = {
                        haptica.tic()
                        val def = when (estiloTemporal) {
                            "iconos" -> 12.5f
                            "puntos" -> 5f
                            "lateral" -> 3.5f
                            else -> 2f
                        }
                        alturaTemporal = def
                        opacidadTemporal = AjustesDefaults.ColoresDatos.OPACIDAD_INDICADORES
                        vm.ajustarAlturaIndicadores(def)
                        vm.ajustarOpacidadIndicadores(AjustesDefaults.ColoresDatos.OPACIDAD_INDICADORES)
                    }
                )

                Spacer(Modifier.height(14.dp))

                // Paleta de Colores por Campo/Dato
                GrupoSelectoresColoresDatos(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    onSeleccionarColor = { clave, color ->
                        haptica.tic()
                        colorInicialModal = color
                        colorSeleccionando = clave
                    },
                    alRestablecer = {
                        haptica.tic()
                        vm.restablecerColoresDatos()
                        vm.avisar("Colores restablecidos")
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    // Modal Selector de Color
    colorSeleccionando?.let { clave ->
        DialogoSelectorColorDato(
            claveColor = clave,
            colorInicial = colorInicialModal,
            alCerrar = { colorSeleccionando = null },
            alCambiarColor = { k, nuevoColor ->
                val hex = nuevoColor.aHex()
                when (k) {
                    "usuario" -> vm.ajustarColorDatosUsuario(hex)
                    "contrasena" -> vm.ajustarColorDatosContrasena(hex)
                    "2fa" -> vm.ajustarColorDatos2FA(hex)
                    "passkey" -> vm.ajustarColorDatosPasskey(hex)
                    "web" -> vm.ajustarColorDatosWeb(hex)
                    "app" -> vm.ajustarColorDatosApp(hex)
                }
            }
        )
    }
}
