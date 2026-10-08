package com.jlnavas3.bovedalocal.ui.pantallas.seguridad

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla atómica dedicada a la protección contra fuerza bruta y freno progresivo de reintentos.
 */
@Composable
fun PantallaFuerzaBruta(
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
                titulo = "Protección contra fuerza bruta",
                idEtiqueta = "01-SEG-BIO-BRU",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Defensa progresiva contra ataques de diccionario y limitación de reintentos")
                Spacer(Modifier.height(10.dp))

                GrupoFrenoFuerzaBruta(
                    frenoIntentosGratis = ajustes.frenoIntentosGratis,
                    frenoSegundosMax = ajustes.frenoSegundosMax,
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alAjustarIntentos = { valor ->
                        haptica.tic()
                        vm.ajustarFrenoIntentosGratis(valor)
                    },
                    alAjustarMaxTiempo = { segundos ->
                        haptica.tic()
                        vm.ajustarFrenoSegundosMax(segundos)
                    },
                    alRestablecer = {
                        haptica.tic()
                        vm.restablecerFrenoIntentos()
                        vm.avisar("Protección contra fuerza bruta restablecida")
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Preview(name = "Pantalla Fuerza Bruta", showBackground = true)
@Composable
private fun PantallaFuerzaBrutaPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
                .padding(16.dp)
        ) {
            DescripcionPantalla(subtitulo = "Defensa progresiva contra ataques de diccionario y limitación de reintentos")
            Spacer(Modifier.height(10.dp))
            GrupoFrenoFuerzaBruta(
                frenoIntentosGratis = 3,
                frenoSegundosMax = 900,
                mostrarIdsAjustes = true,
                alAjustarIntentos = {},
                alAjustarMaxTiempo = {},
                alRestablecer = {}
            )
        }
    }
}
