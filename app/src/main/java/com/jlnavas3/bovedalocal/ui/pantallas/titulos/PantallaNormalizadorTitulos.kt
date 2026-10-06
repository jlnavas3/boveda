package com.jlnavas3.bovedalocal.ui.pantallas.titulos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

@Composable
fun PantallaNormalizadorTitulos(
    vm: VaultViewModel,
    esPostImportacion: Boolean = false,
    alVolver: () -> Unit = { vm.volverAtras() }
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val grupos by vm.gruposTitulos.collectAsStateWithLifecycle()
    val modo by vm.modoFormatoTitulos.collectAsStateWithLifecycle()
    val respetarManuales by vm.respetarTitulosManuales.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        vm.prepararNormalizacionTitulos()
    }

    val totalCuentas = grupos.sumOf { it.entradas.size }

    Scaffold(
        topBar = {
            BarraSuperiorPantalla(
                titulo = if (esPostImportacion) "Revisar títulos importados" else "Normalizador de Títulos",
                idEtiqueta = "03-LST-TIT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = alVolver,
                conSeparador = listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    IconButton(onClick = { vm.ir(Pantalla.ReglasNormalizacion) }) {
                        Icon(
                            imageVector = Icons.Filled.Tune,
                            contentDescription = "Reglas de URLs y redes",
                            tint = ColorAcento
                        )
                    }
                }
            )
        },
        bottomBar = {
            BarraInferiorAccionNormalizador(
                totalCuentas = totalCuentas,
                alAplicar = {
                    vm.aplicarNormalizacionTitulos {
                        alVolver()
                    }
                },
                alCancelar = alVolver
            )
        },
        containerColor = ColorAjustesFondo
    ) { relleno ->
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = relleno.calculateTopPadding() + 8.dp,
                bottom = relleno.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item(key = "selector_modo") {
                SelectorModoFormatoTitulos(
                    modoActual = modo,
                    respetarManuales = respetarManuales,
                    alCambiarModo = { vm.cambiarModoFormatoTitulos(it) },
                    alCambiarRespetarManuales = { vm.cambiarRespetarTitulosManuales(it) },
                    mostrarId = ajustes.mostrarIdsAjustes,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            items(
                items = grupos,
                key = { it.dominioClave }
            ) { grupo ->
                TarjetaGrupoTitulos(
                    grupo = grupo,
                    modo = modo,
                    alCambiarNombre = { nuevoNombre ->
                        vm.actualizarNombreGrupo(grupo.dominioClave, nuevoNombre)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
