package com.jlnavas3.bovedalocal.ui.pantallas

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
import androidx.compose.foundation.relocation.bringIntoViewRequester
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
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.emergencia.BannerRespaldoOffline
import com.jlnavas3.bovedalocal.ui.pantallas.emergencia.BotonesAccionKitEmergencia
import com.jlnavas3.bovedalocal.ui.pantallas.emergencia.GrupoOpcionesDocumento
import com.jlnavas3.bovedalocal.ui.pantallas.emergencia.SeccionVistaPreviaKit
import com.jlnavas3.bovedalocal.util.GeneradorKitEmergencia
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.Portapapeles

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaKitEmergencia(
    vm: VaultViewModel,
    actividad: Activity,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val estado by vm.estado.collectAsStateWithLifecycle()
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val reqOpciones = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null) {
            when {
                seccionDestino == "05.3.1" -> reqOpciones.bringIntoView()
                seccionDestino.startsWith("05.3.") && seccionDestino != "05.3" -> reqOpciones.bringIntoView()
            }
        }
    }

    val entradas = remember(estado) {
        (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    }

    var incluirContrasenas by remember { mutableStateOf(false) }
    var soloFavoritos by remember { mutableStateOf(false) }
    var incluirNotas by remember { mutableStateOf(false) }

    val opciones = remember(incluirContrasenas, soloFavoritos, incluirNotas) {
        GeneradorKitEmergencia.OpcionesKit(
            incluirContrasenas = incluirContrasenas,
            soloFavoritos = soloFavoritos,
            incluirNotas = incluirNotas
        )
    }

    val textoPreview = remember(entradas, opciones) {
        GeneradorKitEmergencia.generarTexto(entradas, opciones)
    }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Kit de emergencia",
                idEtiqueta = "05.3",
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
                // Banner informativo
                BannerRespaldoOffline(mostrarIdsAjustes = ajustes.mostrarIdsAjustes)

                Spacer(Modifier.height(14.dp))

                // Opciones de configuración
                GrupoOpcionesDocumento(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    incluirContrasenas = incluirContrasenas,
                    soloFavoritos = soloFavoritos,
                    incluirNotas = incluirNotas,
                    alCambiarIncluirContrasenas = {
                        haptica.tic()
                        incluirContrasenas = it
                    },
                    alCambiarSoloFavoritos = {
                        haptica.tic()
                        soloFavoritos = it
                    },
                    alCambiarIncluirNotas = {
                        haptica.tic()
                        incluirNotas = it
                    },
                    modifier = Modifier.bringIntoViewRequester(reqOpciones)
                )

                Spacer(Modifier.height(14.dp))

                // Botones de acción principales
                BotonesAccionKitEmergencia(
                    alImprimirPdf = {
                        haptica.exito()
                        val html = GeneradorKitEmergencia.generarHtml(entradas, opciones)
                        GeneradorKitEmergencia.imprimir(actividad, html)
                    },
                    alCopiarTexto = {
                        haptica.toque()
                        Portapapeles.copiar(contexto, "Kit de Emergencia Bóveda Local", textoPreview)
                        vm.avisar("Kit de emergencia copiado al portapapeles")
                    }
                )

                Spacer(Modifier.height(14.dp))

                // Vista previa del documento
                SeccionVistaPreviaKit(
                    textoPreview = textoPreview,
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
