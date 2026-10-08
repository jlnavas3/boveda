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
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.MapaAjustes
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes

/**
 * Pantalla contenedora de Nivel 2 para Tema y Colores.
 * Navegación fractal limpia con filas 100% tipográficas sin íconos.
 */
@Composable
fun PantallaTema(
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
                titulo = "Tema y colores",
                idEtiqueta = "02-APA-THM",
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
                DescripcionPantalla(subtitulo = "Paleta cromática, animaciones de desbloqueo y personalización visual")
                Spacer(Modifier.height(10.dp))

                // Grupo: Apariencia general
                ComponenteGrupo(
                    etiqueta = "Apariencia general",
                    idGrupo = "02-APA-THM-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Tema y colores",
                        icono = null,
                        idFila = "02-APA-THM-PAL",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.TemaPaletas()) }
                    )
                }

                Spacer(Modifier.height(EspaciadoComponentes))

                // Grupo: Pantalla de desbloqueo
                ComponenteGrupo(
                    etiqueta = "Pantalla de desbloqueo y efectos",
                    idGrupo = "02-APA-THM-G02",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Pantalla de desbloqueo",
                        icono = null,
                        idFila = "02-APA-THM-ANI",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.AnimacionDesbloqueo()) }
                    )
                }

                Spacer(Modifier.height(EspaciadoComponentes))

                // Grupo: Laboratorio
                ComponenteGrupo(
                    etiqueta = "Laboratorio y personalización",
                    idGrupo = "02-APA-THM-G07",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Laboratorio de temas",
                        icono = null,
                        idFila = "02-APA-THM-LAB",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.LaboratorioTemas()) }
                    )
                }

                // Subajustes adoptados dinámicamente mediante reparenting
                val hijosAdoptados = remember(ajustes.reparentingPersonalizado, ajustes.ordenJerarquiaPersonalizado) {
                    MapaAjustes.obtenerHijosDe(
                        padreId = "02-APA-THM",
                        ordenPersonalizado = ajustes.ordenJerarquiaPersonalizado,
                        reparenting = ajustes.reparentingPersonalizado
                    ).filter { it.padreId != "02-APA-THM" }
                }

                if (hijosAdoptados.isNotEmpty()) {
                    Spacer(Modifier.height(EspaciadoComponentes))
                    ComponenteGrupo(
                        etiqueta = "Ajustes vinculados",
                        idGrupo = "02-APA-THM-EXT",
                        mostrarId = ajustes.mostrarIdsAjustes
                    ) {
                        hijosAdoptados.forEachIndexed { index, hijo ->
                            if (index > 0) ComponenteSeparador(sangriaInicio = 68.dp)
                            ComponenteNavegacion(
                                titulo = hijo.titulo,
                                icono = hijo.icono,
                                colorIcono = hijo.colorIcono,
                                idFila = hijo.id,
                                mostrarId = ajustes.mostrarIdsAjustes,
                                alPulsar = { hijo.pantallaDestino?.let { vm.ir(it) } }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@BovedaPantallaPreview
@Composable
private fun PantallaTemaPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Tema y colores",
                idEtiqueta = "02-APA-THM",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Paleta cromática, animaciones de desbloqueo y personalización visual")
        }
    }
}
