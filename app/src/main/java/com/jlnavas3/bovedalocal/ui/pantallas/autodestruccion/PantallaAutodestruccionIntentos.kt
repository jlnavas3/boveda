package com.jlnavas3.bovedalocal.ui.pantallas.autodestruccion

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
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla atómica dedicada a la autodestrucción por exceso de intentos erróneos consecutivos.
 */
@Composable
fun PantallaAutodestruccionIntentos(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Autodestrucción por intentos",
                idEtiqueta = "01-SEG-DES-INT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("01-SEG-DES-INT-G01", "Intentos fallidos")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.ajustarAutodestruccionIntentosFallidosMax(0)
                            vm.avisar("Autodestrucción por intentos desactivada")
                        }
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Borrado permanente e irreversible de los datos tras superar el límite de intentos erróneos consecutivos")
                Spacer(Modifier.height(10.dp))

                GrupoAutodestruccionIntentosFallidos(
                    intentosMax = ajustes.autodestruccionIntentosFallidosMax,
                    alCambiarIntentosMax = { nuevoLimite ->
                        haptica.tic()
                        vm.ajustarAutodestruccionIntentosFallidosMax(nuevoLimite)
                    },
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@BovedaPantallaPreview
@Composable
private fun PantallaAutodestruccionIntentosPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Autodestrucción por intentos",
                idEtiqueta = "01-SEG-DES-INT",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Borrado permanente e irreversible de los datos tras superar el límite de intentos erróneos consecutivos")
        }
    }
}
