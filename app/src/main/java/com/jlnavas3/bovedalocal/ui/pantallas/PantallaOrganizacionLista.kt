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
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes

/**
 * Pantalla contenedora de Nivel 2 para Diseño de Lista.
 * Navegación fractal limpia con filas 100% tipográficas sin íconos.
 */
@Composable
fun PantallaOrganizacionLista(
    vm: VaultViewModel,
    seccionId: String? = null
) {
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
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Estructura de tarjetas, densidad y jerarquía organizativa")
                Spacer(Modifier.height(10.dp))

                // Grupo: Estructura de visualización
                ComponenteGrupo(
                    etiqueta = "Estructura de visualización",
                    idGrupo = "03-LST-DES-G01",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Estructura y densidad",
                        icono = null,
                        idFila = "03-LST-DES-EST",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.OrganizacionEstructura()) }
                    )
                }

                Spacer(Modifier.height(EspaciadoComponentes))

                // Grupo: Identidades y jerarquía
                ComponenteGrupo(
                    etiqueta = "Identidades y jerarquía",
                    idGrupo = "03-LST-DES-G02",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Identidades y jerarquía",
                        icono = null,
                        idFila = "03-LST-DES-JER",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.OrganizacionJerarquia()) }
                    )
                }

                Spacer(Modifier.height(EspaciadoComponentes))

                // Grupo: Indicadores y datos
                ComponenteGrupo(
                    etiqueta = "Indicadores y datos",
                    idGrupo = "03-LST-DES-G03",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteNavegacion(
                        titulo = "Indicadores de contenido",
                        icono = null,
                        idFila = "03-LST-DES-IND",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = { vm.ir(Pantalla.OrganizacionIndicadores()) }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@BovedaPantallaPreview
@Composable
private fun PantallaOrganizacionListaPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Diseño de lista",
                idEtiqueta = "03-LST-DES",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Estructura de tarjetas, densidad y jerarquía organizativa")
        }
    }
}
