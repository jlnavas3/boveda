/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.tema

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.LocalCoordinadorResaltado
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.contenedorScrollAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.PaletaAcento
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla atómica dedicada al modo de tema claro/oscuro e ícono del launcher.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaTemaPaletas(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val actividad = contexto as? Activity
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var dialogoConfirmarIcono by remember { mutableStateOf<VarianteIconoLauncher?>(null) }

    val reqLauncher = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null && seccionDestino == "09.2.8") {
            reqLauncher.bringIntoView()
        }
    }

    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        val coordinador = LocalCoordinadorResaltado.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Modos de tema",
                idEtiqueta = "02-APA-THM-PAL",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("02-APA-THM-G01", "Modo de tema"),
                            AccionSaltoGrupo("02-APA-THM-G06", "Ícono en el launcher")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.ajustarTema(AjustesDefaults.Tema.TEMA_APP)
                            vm.avisar("Modo de tema restablecido")
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .contenedorScrollAjustes(coordinador)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Modo de tema claro/oscuro y personalización del ícono launcher")
                Spacer(Modifier.height(10.dp))

                // Modo de tema (Sistema, Claro, Oscuro)
                SeccionModoTema(
                    ajustes = ajustes,
                    alCambiarTema = { vm.ajustarTema(it) }
                )

                // Ícono de la app en el Launcher
                Spacer(Modifier.height(14.dp))
                SeccionIconoLauncher(
                    ajustes = ajustes,
                    haptica = haptica,
                    reqLauncher = reqLauncher,
                    alSolicitarCambioIcono = { paleta -> dialogoConfirmarIcono = paleta }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    dialogoConfirmarIcono?.let { variante ->
        DialogoConfirmacionIconoLauncher(
            variante = variante,
            alConfirmar = { elegido ->
                dialogoConfirmarIcono = null
                vm.ajustarIconoLauncher(elegido.clave)
                actividad?.finishAffinity()
            },
            alDescartar = { dialogoConfirmarIcono = null }
        )
    }
}

@BovedaPantallaPreview
@Composable
private fun PantallaTemaPaletasPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Modos de tema",
                idEtiqueta = "02-APA-THM-PAL",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Modo de tema claro/oscuro y personalización del ícono launcher")
        }
    }
}
