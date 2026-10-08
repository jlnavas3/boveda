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
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.MapaAjustes
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes

/**
 * Pantalla contenedora de Nivel 2 para Opciones Avanzadas.
 * Navegación fractal limpia con filas 100% tipográficas sin íconos.
 */
@Composable
fun PantallaAvanzada(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val hijosActuales = remember(ajustes.reparentingPersonalizado, ajustes.ordenJerarquiaPersonalizado) {
        MapaAjustes.obtenerHijosDe(
            padreId = "06-SIS-AVZ",
            ordenPersonalizado = ajustes.ordenJerarquiaPersonalizado,
            reparenting = ajustes.reparentingPersonalizado
        )
    }
    val hijosIds = remember(hijosActuales) { hijosActuales.map { it.id }.toSet() }

    val itemsG1 = remember(hijosIds) { listOf("06-SIS-AVZ-DES", "06-SIS-AVZ-LGT", "06-SIS-AVZ-ORG").filter { it in hijosIds } }
    val itemsG2 = remember(hijosIds) { listOf("06-SIS-AVZ-HAP").filter { it in hijosIds } }
    val itemsG3 = remember(hijosIds) { listOf("06-SIS-AVZ-PEL").filter { it in hijosIds } }
    val idsNativos = remember { setOf("06-SIS-AVZ-DES", "06-SIS-AVZ-LGT", "06-SIS-AVZ-ORG", "06-SIS-AVZ-HAP", "06-SIS-AVZ-PEL") }
    val itemsAdoptados = remember(hijosActuales) { hijosActuales.filter { it.id !in idsNativos } }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Opciones avanzadas",
                idEtiqueta = "06-SIS-AVZ",
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
                DescripcionPantalla(subtitulo = "Desarrollo, calibración háptica, ordenación y zona de peligro")
                Spacer(Modifier.height(10.dp))

                if (hijosActuales.isEmpty()) {
                    ComponenteGrupo(
                        etiqueta = "Opciones avanzadas",
                        idGrupo = "06-SIS-AVZ-G00",
                        mostrarId = ajustes.mostrarIdsAjustes
                    ) {
                        Text(
                            text = "Todos los ajustes de esta sección han sido reorganizados a otros niveles.",
                            color = ColorAjusteGris,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    if (itemsG1.isNotEmpty()) {
                        ComponenteGrupo(
                            etiqueta = "Desarrollo y navegación",
                            idGrupo = "06-SIS-AVZ-G01",
                            mostrarId = ajustes.mostrarIdsAjustes
                        ) {
                            itemsG1.forEachIndexed { idx, id ->
                                if (idx > 0) ComponenteSeparador()
                                when (id) {
                                    "06-SIS-AVZ-DES" -> ComponenteNavegacion(
                                        titulo = "Desarrollo e identificadores",
                                        icono = null,
                                        idFila = "06-SIS-AVZ-DES",
                                        mostrarId = ajustes.mostrarIdsAjustes,
                                        alPulsar = { vm.ir(Pantalla.AvanzadaDesarrollo()) }
                                    )
                                    "06-SIS-AVZ-LGT" -> ComponenteNavegacion(
                                        titulo = "Alumbrado y navegación",
                                        icono = null,
                                        idFila = "06-SIS-AVZ-LGT",
                                        mostrarId = ajustes.mostrarIdsAjustes,
                                        alPulsar = { vm.ir(Pantalla.AvanzadaAlumbrado()) }
                                    )
                                    "06-SIS-AVZ-ORG" -> ComponenteNavegacion(
                                        titulo = "Reorganizar ajustes",
                                        icono = null,
                                        idFila = "06-SIS-AVZ-ORG",
                                        mostrarId = ajustes.mostrarIdsAjustes,
                                        alPulsar = { vm.ir(Pantalla.ReorganizarAjustes) }
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(EspaciadoComponentes))
                    }

                    if (itemsG2.isNotEmpty()) {
                        ComponenteGrupo(
                            etiqueta = "Interacción y sistema",
                            idGrupo = "06-SIS-AVZ-G02",
                            mostrarId = ajustes.mostrarIdsAjustes
                        ) {
                            ComponenteNavegacion(
                                titulo = "Respuesta táctil y vibración",
                                icono = null,
                                idFila = "06-SIS-AVZ-HAP",
                                mostrarId = ajustes.mostrarIdsAjustes,
                                alPulsar = { vm.ir(Pantalla.AvanzadaHaptica()) }
                            )
                        }
                        Spacer(Modifier.height(EspaciadoComponentes))
                    }

                    if (itemsG3.isNotEmpty()) {
                        ComponenteGrupo(
                            etiqueta = "Seguridad crítica",
                            idGrupo = "06-SIS-AVZ-G03",
                            mostrarId = ajustes.mostrarIdsAjustes
                        ) {
                            ComponenteNavegacion(
                                titulo = "Zona de peligro",
                                icono = null,
                                idFila = "06-SIS-AVZ-PEL",
                                mostrarId = ajustes.mostrarIdsAjustes,
                                alPulsar = { vm.ir(Pantalla.AvanzadaZonaPeligro()) }
                            )
                        }
                        Spacer(Modifier.height(EspaciadoComponentes))
                    }

                    if (itemsAdoptados.isNotEmpty()) {
                        ComponenteGrupo(
                            etiqueta = "Ajustes vinculados",
                            idGrupo = "06-SIS-AVZ-EXT",
                            mostrarId = ajustes.mostrarIdsAjustes
                        ) {
                            itemsAdoptados.forEachIndexed { idx, hijo ->
                                if (idx > 0) ComponenteSeparador()
                                ComponenteNavegacion(
                                    titulo = hijo.titulo,
                                    icono = null,
                                    idFila = hijo.id,
                                    mostrarId = ajustes.mostrarIdsAjustes,
                                    alPulsar = {
                                        if (hijo.pantallaDestino != null) vm.ir(hijo.pantallaDestino)
                                        else vm.irPorId(hijo.id)
                                    }
                                )
                            }
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
private fun PantallaAvanzadaPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Opciones avanzadas",
                idEtiqueta = "06-SIS-AVZ",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Desarrollo, calibración háptica, ordenación y zona de peligro")
        }
    }
}
