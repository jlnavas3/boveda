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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes

/**
 * Pantalla contenedora de Nivel 2 para Copia de Seguridad.
 * Navegación fractal limpia con filas 100% tipográficas sin íconos.
 */
@Composable
fun PantallaCopiaSeguridad(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Copia de seguridad",
                idEtiqueta = "05-COP-MAN",
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
                DescripcionPantalla(subtitulo = "Respaldo y restauración de la bóveda, copias automáticas y alertas")
                Spacer(Modifier.height(10.dp))

                // Grupo: Respaldo y restauración
                ComponenteGrupo(
                    etiqueta = "Respaldo y restauración",
                    idGrupo = "05-COP-MAN-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Copias manuales (.bvda)",
                        icono = null,
                        idFila = "05-COP-MAN-FIL",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.CopiaManual()) }
                    )
                }

                Spacer(Modifier.height(EspaciadoComponentes))

                // Grupo: Automatización
                ComponenteGrupo(
                    etiqueta = "Automatización y recordatorios",
                    idGrupo = "05-COP-MAN-G02",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Copia automática local",
                        icono = null,
                        idFila = "05-COP-AUT-FIL",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.CopiaAutomaticaLocalSub()) }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Recordatorios de respaldo",
                        icono = null,
                        idFila = "05-COP-REC-FIL",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.CopiaRecordatorios()) }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@BovedaPantallaPreview
@Composable
private fun PantallaCopiaSeguridadPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Copia de seguridad",
                idEtiqueta = "05-COP-MAN",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Respaldo y restauración de la bóveda, copias automáticas y alertas")
        }
    }
}
