package com.jlnavas3.bovedalocal.ui.pantallas.organizacion

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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla atómica dedicada a la estructura de filas, densidad y ordenación de la lista.
 */
@Composable
fun PantallaOrganizacionEstructura(
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
                titulo = "Estructura y densidad",
                idEtiqueta = "03-LST-DES-EST",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("03-LST-DES-G01", "Agrupamiento"),
                            AccionSaltoGrupo("03-LST-DES-G02", "Densidad"),
                            AccionSaltoGrupo("03-LST-DES-G03", "Orden predeterminado")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.restablecerOrganizacionLista()
                            vm.avisar("Estructura de lista restablecida")
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
                DescripcionPantalla(subtitulo = "Agrupamiento por sitio, densidad de lista y ordenación predeterminada")
                Spacer(Modifier.height(10.dp))

                // Agrupamiento
                ComponenteGrupo(
                    etiqueta = "Agrupamiento",
                    idGrupo = "03-LST-DES-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSwitch(
                        titulo = "Agrupar cuentas por servicio",
                        icono = null,
                        activo = ajustes.agruparPorSitio,
                        idFila = "03-LST-DES-GRP",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarAgruparPorSitio(it)
                        }
                    )
                }

                Spacer(Modifier.height(14.dp))

                // Densidad de lista
                GrupoDensidadFilas(
                    mostrarId = ajustes.mostrarIdsAjustes,
                    densidadLista = ajustes.densidadLista,
                    alSeleccionarDensidad = { densidad ->
                        haptica.tic()
                        vm.ajustarDensidadLista(densidad)
                    }
                )

                Spacer(Modifier.height(14.dp))

                // Ordenación predeterminada
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
                        vm.avisar("Ordenación restablecida")
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@BovedaPantallaPreview
@Composable
private fun PantallaOrganizacionEstructuraPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Estructura y densidad",
                idEtiqueta = "03-LST-DES-EST",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Agrupamiento por sitio, densidad de lista y ordenación predeterminada")
        }
    }
}
