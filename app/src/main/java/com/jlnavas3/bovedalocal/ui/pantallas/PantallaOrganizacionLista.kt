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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteRadio
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla dedicada a la Organización de la Lista Principal:
 * - Agrupamiento de cuentas por servicio / subdominio.
 * - Densidad y altura de filas (predeterminada, cómoda, compacta).
 * - Criterio de ordenación predeterminado.
 */
@Composable
fun PantallaOrganizacionLista(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = Haptica(contexto)
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionId) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Organización de lista",
                idEtiqueta = "03.5",
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
                DescripcionPantalla(subtitulo = "Configura el agrupamiento por sitio, tamaño de filas y ordenación")
                Spacer(Modifier.height(10.dp))

                // 1. Agrupamiento por sitio
                ComponenteGrupo(
                    etiqueta = "Agrupamiento",
                    idGrupo = "03.5.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Combina cuentas que pertenecen al mismo servicio o dominio"
                ) {
                    ComponenteSwitch(
                        titulo = "Agrupar cuentas por sitio",
                        icono = Icons.Filled.Tune,
                        colorIcono = ColorIconosInternos,
                        activo = ajustes.agruparPorSitio,
                        idFila = "03.5.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarAgruparPorSitio(it)
                        }
                    )
                }

                Spacer(Modifier.height(14.dp))

                // 2. Densidad de lista
                ComponenteGrupo(
                    etiqueta = "Densidad de filas",
                    idGrupo = "03.5.G2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Altura y espacio vertical de cada fila en el listado"
                ) {
                    ComponenteRadio(
                        titulo = "Predeterminada",
                        icono = Icons.Filled.Tune,
                        colorIcono = ColorIconosInternos,
                        seleccionado = ajustes.densidadLista == "predeterminada" || (ajustes.densidadLista != "comoda" && ajustes.densidadLista != "compacta"),
                        idFila = "03.5.2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alSeleccionar = {
                            haptica.tic()
                            vm.ajustarDensidadLista("predeterminada")
                        }
                    )
                    ComponenteSeparador()
                    ComponenteRadio(
                        titulo = "Cómoda",
                        icono = Icons.Filled.Tune,
                        colorIcono = ColorIconosInternos,
                        seleccionado = ajustes.densidadLista == "comoda",
                        idFila = "03.5.3",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alSeleccionar = {
                            haptica.tic()
                            vm.ajustarDensidadLista("comoda")
                        }
                    )
                    ComponenteSeparador()
                    ComponenteRadio(
                        titulo = "Compacta",
                        icono = Icons.Filled.Tune,
                        colorIcono = ColorIconosInternos,
                        seleccionado = ajustes.densidadLista == "compacta",
                        idFila = "03.5.4",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alSeleccionar = {
                            haptica.tic()
                            vm.ajustarDensidadLista("compacta")
                        }
                    )
                }

                Spacer(Modifier.height(14.dp))

                // 3. Ordenación predeterminada
                ComponenteGrupo(
                    etiqueta = "Ordenación predeterminada",
                    idGrupo = "03.5.G3",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Criterio de orden inicial de las cuentas en la bóveda"
                ) {
                    CriterioOrdenacion.entries.forEachIndexed { index, criterio ->
                        if (index > 0) ComponenteSeparador()
                        ComponenteRadio(
                            titulo = criterio.etiqueta,
                            icono = Icons.AutoMirrored.Filled.Sort,
                            colorIcono = ColorIconosInternos,
                            seleccionado = ajustes.criterioOrdenacion == criterio.name,
                            idFila = "03.5.${5 + index}",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alSeleccionar = {
                                haptica.tic()
                                vm.cambiarCriterioOrdenacion(criterio)
                            }
                        )
                    }

                    ComponenteSeparador()

                    ComponenteBotonFila(
                        titulo = "Restablecer grupo",
                        alPulsar = {
                            vm.ajustarAgruparPorSitio(false)
                            vm.ajustarDensidadLista("predeterminada")
                            vm.cambiarCriterioOrdenacion(CriterioOrdenacion.NOMBRE_AZ)
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
