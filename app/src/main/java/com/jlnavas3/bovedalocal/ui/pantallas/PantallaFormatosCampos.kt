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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.formatos.GrupoConfiguracionFormatos
import com.jlnavas3.bovedalocal.ui.pantallas.formatos.GrupoPreviaFormatos
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaFormatosCampos(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val ejemploFecha = FormateadorCampos.formatearFecha(2026, 11, 31, ajustes.formatoFecha)
    val ejemploHora = FormateadorCampos.formatearHora(19, 30, ajustes.formatoHora)
    val ejemploTel = FormateadorCampos.aplicarMascaraTelefono("612345678", ajustes.formatoTelefono)
    val ejemploDecimal = "1250${ajustes.separadorDecimal}50"
    val ejemploTarjeta = FormateadorCampos.aplicarMascaraTarjeta("4532891023456789", ajustes.formatoTarjeta)
    val ejemploExpiracion = FormateadorCampos.aplicarMascaraExpiracion("1228", ajustes.formatoExpiracionTarjeta)
    val ejemploIban = FormateadorCampos.aplicarMascaraIban("ES91210004184502", ajustes.formatoIban)
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionId) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Formatos",
                idEtiqueta = "02-APA-FMT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("02-APA-FMT-G01", "Vista previa"),
                            AccionSaltoGrupo("02-APA-FMT-G02", "Configuración de formatos"),
                            AccionSaltoGrupo("02-APA-FMT-G03", "Formatos financieros")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.restablecerFormatos()
                            vm.avisar("Formatos restablecidos")
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Grupo 1: Vista previa en tiempo real
                GrupoPreviaFormatos(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    ejemploFecha = ejemploFecha,
                    ejemploHora = ejemploHora,
                    ejemploTel = ejemploTel,
                    ejemploDecimal = ejemploDecimal,
                    ejemploTarjeta = ejemploTarjeta,
                    ejemploExpiracion = ejemploExpiracion,
                    ejemploIban = ejemploIban
                )

                Spacer(Modifier.height(14.dp))

                // Grupo 2: Configuración de formatos
                GrupoConfiguracionFormatos(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    formatoFecha = ajustes.formatoFecha,
                    formatoHora = ajustes.formatoHora,
                    formatoTelefono = ajustes.formatoTelefono,
                    separadorDecimal = ajustes.separadorDecimal,
                    alCambiarFormatoFecha = {
                        haptica.tic()
                        vm.ajustarFormatoFecha(it)
                    },
                    alCambiarFormatoHora = {
                        haptica.tic()
                        vm.ajustarFormatoHora(it)
                    },
                    alCambiarFormatoTelefono = {
                        haptica.tic()
                        vm.ajustarFormatoTelefono(it)
                    },
                    alCambiarSeparadorDecimal = {
                        haptica.tic()
                        vm.ajustarSeparadorDecimal(it)
                    },
                    alRestablecerGrupo = {
                        haptica.tic()
                        vm.restablecerFormatos()
                        vm.avisar("Formatos restablecidos")
                    }
                )

                Spacer(Modifier.height(14.dp))

                // Grupo 3: Formatos financieros y bancarios
                com.jlnavas3.bovedalocal.ui.pantallas.formatos.GrupoConfiguracionFormatosFinancieros(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    formatoTarjeta = ajustes.formatoTarjeta,
                    formatoExpiracionTarjeta = ajustes.formatoExpiracionTarjeta,
                    formatoIban = ajustes.formatoIban,
                    alCambiarFormatoTarjeta = {
                        haptica.tic()
                        vm.ajustarFormatoTarjeta(it)
                    },
                    alCambiarFormatoExpiracionTarjeta = {
                        haptica.tic()
                        vm.ajustarFormatoExpiracionTarjeta(it)
                    },
                    alCambiarFormatoIban = {
                        haptica.tic()
                        vm.ajustarFormatoIban(it)
                    },
                    alRestablecerGrupo = {
                        haptica.tic()
                        vm.restablecerFormatosFinancieros()
                        vm.avisar("Formatos financieros restablecidos")
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
