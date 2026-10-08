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
import com.jlnavas3.bovedalocal.data.ModoVisualizacionIdentidades
import com.jlnavas3.bovedalocal.ui.Pantalla
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
 * Pantalla atómica dedicada al modo de visualización de identidades y jerarquía con categorías.
 */
@Composable
fun PantallaOrganizacionJerarquia(
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
                titulo = "Identidades y jerarquía",
                idEtiqueta = "03-LST-DES-JER",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("03-LST-DES-GID", "Modo de identidades"),
                            AccionSaltoGrupo("03-LST-DES-GJR", "Jerarquía de organización")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.cambiarModoVisualizacionIdentidades(ModoVisualizacionIdentidades.CHIPS)
                            vm.avisar("Modo de identidades restablecido")
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
                DescripcionPantalla(subtitulo = "Modo de visualización de perfiles de correo y orden de categorías")
                Spacer(Modifier.height(10.dp))

                // Modo de visualización de identidades
                GrupoModoIdentidadesAjustes(
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

                // Jerarquía de organización
                GrupoJerarquiaOrganizacionAjustes(
                    jerarquiaActual = ajustes.jerarquiaOrganizacionEfectiva,
                    mostrarId = ajustes.mostrarIdsAjustes,
                    habilitado = ajustes.modoVisualizacionIdentidades != ModoVisualizacionIdentidades.DESACTIVADO,
                    alSeleccionarJerarquia = { jerarquia ->
                        haptica.tic()
                        vm.ajustarJerarquiaOrganizacion(jerarquia)
                    },
                    alGestionarCategorias = {
                        haptica.tic()
                        vm.ir(Pantalla.Categorias("03-LST-CAT"))
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@BovedaPantallaPreview
@Composable
private fun PantallaOrganizacionJerarquiaPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Identidades y jerarquía",
                idEtiqueta = "03-LST-DES-JER",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Modo de visualización de perfiles de correo y orden de categorías")
        }
    }
}
