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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.organizacion.GrupoAgrupamientoEIndicadores
import com.jlnavas3.bovedalocal.ui.pantallas.organizacion.GrupoDensidadFilas
import com.jlnavas3.bovedalocal.ui.pantallas.organizacion.GrupoOrdenacionPredeterminada
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla dedicada al Diseño de Lista:
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
                titulo = "Diseño de lista",
                idEtiqueta = "03-LST-DES",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("03-LST-DES-G01", "Agrupamiento"),
                            AccionSaltoGrupo("03-LST-DES-G02", "Densidad de lista"),
                            AccionSaltoGrupo("03-LST-DES-G03", "Orden predeterminado")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.restablecerOrganizacionLista()
                            vm.avisar("Diseño de lista restablecido")
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
                // 1. Agrupamiento por sitio e indicadores
                GrupoAgrupamientoEIndicadores(
                    mostrarId = ajustes.mostrarIdsAjustes,
                    agruparPorSitio = ajustes.agruparPorSitio,
                    mostrarIndicadoresContenido = ajustes.mostrarIndicadoresContenido,
                    alCambiarAgruparPorSitio = {
                        haptica.tic()
                        vm.ajustarAgruparPorSitio(it)
                    },
                    alCambiarMostrarIndicadores = {
                        haptica.tic()
                        vm.ajustarMostrarIndicadoresContenido(it)
                    },
                    alIrAColoresDatos = {
                        haptica.tic()
                        vm.ir(Pantalla.ColoresDatos())
                    }
                )

                Spacer(Modifier.height(14.dp))

                // 1b. Modo de visualización de identidades
                com.jlnavas3.bovedalocal.ui.pantallas.organizacion.GrupoModoIdentidadesAjustes(
                    modoActual = ajustes.modoVisualizacionIdentidades,
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alSeleccionarModo = { modo ->
                        haptica.tic()
                        vm.cambiarModoVisualizacionIdentidades(modo)
                    },
                    alGestionarIdentidades = {
                        haptica.tic()
                        vm.ir(Pantalla.Identidades("03-LST-DES-GID"))
                    }
                )

                Spacer(Modifier.height(14.dp))

                // 1c. Jerarquía de organización (Identidades vs Colecciones)
                com.jlnavas3.bovedalocal.ui.pantallas.organizacion.GrupoJerarquiaOrganizacionAjustes(
                    jerarquiaActual = ajustes.jerarquiaOrganizacionEfectiva,
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alSeleccionarJerarquia = { jerarquia ->
                        haptica.tic()
                        vm.ajustarJerarquiaOrganizacion(jerarquia)
                    }
                )

                Spacer(Modifier.height(14.dp))

                // 2. Densidad de lista
                GrupoDensidadFilas(
                    mostrarId = ajustes.mostrarIdsAjustes,
                    densidadLista = ajustes.densidadLista,
                    alSeleccionarDensidad = { densidad ->
                        haptica.tic()
                        vm.ajustarDensidadLista(densidad)
                    }
                )

                Spacer(Modifier.height(14.dp))

                // 3. Ordenación predeterminada
                GrupoOrdenacionPredeterminada(
                    mostrarId = ajustes.mostrarIdsAjustes,
                    criterioSeleccionado = ajustes.criterioOrdenacion,
                    alSeleccionarCriterio = { criterio ->
                        haptica.tic()
                        vm.cambiarCriterioOrdenacion(criterio)
                    },
                    alRestablecerGrupo = {
                        haptica.tic()
                        vm.restablecerOrganizacionLista()
                        vm.avisar("Valores de lista restablecidos")
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
