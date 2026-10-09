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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
 * Pantalla atómica dedicada al bloqueo de la aplicación por inactividad y protección FLAG_SECURE.
 */
@Composable
fun PantallaBloqueoApp(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    var confirmarDesactivarSecure by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Bloqueo de aplicación",
                idEtiqueta = "01-SEG-BIO-APP",
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
                DescripcionPantalla(subtitulo = "Temporizadores de inactividad y protección de capturas de pantalla")
                Spacer(Modifier.height(10.dp))

                GrupoBloqueoApp(
                    autoBloqueoSegundos = ajustes.autoBloqueoSegundos,
                    proteccionPantalla = ajustes.proteccionPantalla,
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alAjustarAutoBloqueo = { valor ->
                        haptica.tic()
                        vm.ajustarAutoBloqueo(valor)
                    },
                    alCambiarProteccionPantalla = { activar ->
                        if (activar) {
                            vm.ajustarProteccionPantalla(true)
                            haptica.tic()
                        } else {
                            confirmarDesactivarSecure = true
                        }
                    },
                    proteccionTapjacking = ajustes.proteccionTapjacking,
                    alCambiarProteccionTapjacking = { activar ->
                        haptica.tic()
                        vm.ajustarProteccionTapjacking(activar)
                    },
                    alRestablecer = {
                        haptica.tic()
                        vm.restablecerBloqueoApp()
                        vm.avisar("Valores de bloqueo restablecidos")
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (confirmarDesactivarSecure) {
        DialogoDesactivarSecure(
            alConfirmar = {
                confirmarDesactivarSecure = false
                vm.ajustarProteccionPantalla(false)
                haptica.tic()
            },
            alDescartar = { confirmarDesactivarSecure = false }
        )
    }
}

@Preview(name = "Pantalla Bloqueo App", showBackground = true)
@Composable
private fun PantallaBloqueoAppPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
                .padding(16.dp)
        ) {
            DescripcionPantalla(subtitulo = "Temporizadores de inactividad y protección de capturas de pantalla")
            Spacer(Modifier.height(10.dp))
            GrupoBloqueoApp(
                autoBloqueoSegundos = 1200,
                proteccionPantalla = false,
                mostrarIdsAjustes = true,
                alAjustarAutoBloqueo = {},
                alCambiarProteccionPantalla = {},
                alRestablecer = {}
            )
        }
    }
}
