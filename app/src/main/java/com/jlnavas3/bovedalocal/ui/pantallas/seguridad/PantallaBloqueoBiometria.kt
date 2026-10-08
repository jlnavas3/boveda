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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla contenedora de Bloqueo y biometría con subniveles limpios
 * estilo Google Pixel / Apple iOS (solo título y chevron >).
 */
@Composable
fun PantallaBloqueoBiometria(
    vm: VaultViewModel,
    actividad: FragmentActivity,
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
                titulo = "Bloqueo y biometría",
                idEtiqueta = "01-SEG-BIO-BLO",
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
                DescripcionPantalla(subtitulo = "Políticas de acceso, autenticación biométrica y defensa progresiva")
                Spacer(Modifier.height(10.dp))

                ComponenteGrupo(
                    etiqueta = "Configuración de acceso",
                    idGrupo = "01-SEG-BIO-BLO-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Biometría",
                        icono = null,
                        idFila = "01-SEG-BIO-BIO",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.AjustesBiometria()) }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Bloqueo de aplicación",
                        icono = null,
                        idFila = "01-SEG-BIO-APP",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.BloqueoApp()) }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Protección contra fuerza bruta",
                        icono = null,
                        idFila = "01-SEG-BIO-BRU",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.FuerzaBruta()) }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Preview(name = "Pantalla Bloqueo y Biometría Fractal", showBackground = true)
@Composable
private fun PantallaBloqueoBiometriaPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
                .padding(16.dp)
        ) {
            DescripcionPantalla(subtitulo = "Políticas de acceso, autenticación biométrica y defensa progresiva")
            Spacer(Modifier.height(10.dp))
            ComponenteGrupo(
                etiqueta = "Configuración de acceso",
                idGrupo = "01-SEG-BIO-BLO-G01",
                mostrarId = true
            ) {
                ComponenteNavegacion(
                    titulo = "Biometría",
                    icono = null,
                    idFila = "01-SEG-BIO-BIO",
                    mostrarId = true,
                    alPulsar = {}
                )
                ComponenteSeparador()
                ComponenteNavegacion(
                    titulo = "Bloqueo de aplicación",
                    icono = null,
                    idFila = "01-SEG-BIO-APP",
                    mostrarId = true,
                    alPulsar = {}
                )
                ComponenteSeparador()
                ComponenteNavegacion(
                    titulo = "Protección contra fuerza bruta",
                    icono = null,
                    idFila = "01-SEG-BIO-BRU",
                    mostrarId = true,
                    alPulsar = {}
                )
            }
        }
    }
}
