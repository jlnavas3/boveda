package com.jlnavas3.bovedalocal.ui.pantallas

import android.widget.Toast
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
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.tile.GrupoConfiguracionTile
import com.jlnavas3.bovedalocal.ui.pantallas.tile.GrupoWidgetsInicioTile
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaTileRapido(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionId) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Mosaico rápido",
                idEtiqueta = "04.5",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("04.5.G1", "Generación rápida"),
                            AccionSaltoGrupo("04.5.G2", "Accesos rápidos")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.ajustarTileModo("longitud")
                            vm.ajustarTileLongitud(20)
                            vm.ajustarTilePatron("XXXXX-XXXXX-XXXXX-XXXXX")
                            vm.ajustarTileCopiarPortapapeles(true)
                            vm.ajustarTileMostrarToast(true)
                            vm.ajustarTileHaptica(true)
                            vm.ajustarTileHapticaIntensidad(0.8f)
                            vm.avisar("Mosaico rápido restablecido")
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
                // Grupo 1: Modo de generación y opciones del mosaico
                GrupoConfiguracionTile(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    tileModo = ajustes.tileModo,
                    tileLongitud = ajustes.tileLongitud,
                    tilePatron = ajustes.tilePatron,
                    tileCopiarPortapapeles = ajustes.tileCopiarPortapapeles,
                    tileMostrarToast = ajustes.tileMostrarToast,
                    tileHaptica = ajustes.tileHaptica,
                    tileHapticaIntensidad = ajustes.tileHapticaIntensidad,
                    haptica = haptica,
                    alCambiarTileModo = { valor ->
                        haptica.tic()
                        vm.ajustarTileModo(valor)
                    },
                    alCambiarTileLongitud = { valor ->
                        haptica.tic()
                        vm.ajustarTileLongitud(valor)
                    },
                    alCambiarTilePatron = { vm.ajustarTilePatron(it) },
                    alCambiarTileCopiarPortapapeles = {
                        haptica.toque()
                        vm.ajustarTileCopiarPortapapeles(it)
                        Toast.makeText(
                            contexto,
                            if (it) "Copia al portapapeles activada" else "Copia al portapapeles desactivada",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    alCambiarTileMostrarToast = {
                        haptica.tic()
                        vm.ajustarTileMostrarToast(it)
                    },
                    alCambiarTileHaptica = {
                        haptica.tic()
                        vm.ajustarTileHaptica(it)
                    },
                    alCambiarTileHapticaIntensidad = { vm.ajustarTileHapticaIntensidad(it) },
                    alRestablecerGrupo = {
                        haptica.tic()
                        vm.ajustarTileModo("longitud")
                        vm.ajustarTileLongitud(20)
                        vm.ajustarTilePatron("XXXXX-XXXXX-XXXXX-XXXXX")
                        vm.ajustarTileCopiarPortapapeles(true)
                        vm.ajustarTileMostrarToast(true)
                        vm.ajustarTileHaptica(true)
                        vm.ajustarTileHapticaIntensidad(0.8f)
                        vm.avisar("Mosaico rápido restablecido")
                    }
                )

                Spacer(Modifier.height(14.dp))

                // Grupo 2: Widgets de inicio
                GrupoWidgetsInicioTile(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alNavegarWidgets = {
                        haptica.tic()
                        vm.ir(Pantalla.AjustesWidget("04.4"))
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
