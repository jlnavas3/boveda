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
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla atómica dedicada a los indicadores visuales y colores de campos en tarjetas.
 */
@Composable
fun PantallaOrganizacionIndicadores(
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
                titulo = "Indicadores de contenido",
                idEtiqueta = "03-LST-DES-IND",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("03-LST-DES-G04", "Indicadores de tarjetas")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.ajustarMostrarIndicadoresContenido(true)
                            vm.avisar("Indicadores de contenido restablecidos")
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
                DescripcionPantalla(subtitulo = "Insignias y pastillas de campos en tarjetas de credenciales")
                Spacer(Modifier.height(10.dp))

                ComponenteGrupo(
                    etiqueta = "Indicadores de tarjetas",
                    idGrupo = "03-LST-DES-G04",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSwitch(
                        titulo = "Mostrar pastillas de campos",
                        icono = null,
                        activo = ajustes.mostrarIndicadoresContenido,
                        idFila = "03-LST-DES-IND-SW",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarMostrarIndicadoresContenido(it)
                        }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Colores de campos y datos",
                        icono = null,
                        idFila = "02-APA-THM-G04",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.tic()
                            vm.ir(Pantalla.ColoresDatos())
                        }
                    )
                    ComponenteSeparador()
                    ComponenteNavegacion(
                        titulo = "Asistente de títulos",
                        icono = null,
                        idFila = "03-LST-DES-TIT",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alPulsar = {
                            haptica.tic()
                            vm.ir(Pantalla.NormalizadorTitulos())
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@BovedaPantallaPreview
@Composable
private fun PantallaOrganizacionIndicadoresPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Indicadores de contenido",
                idEtiqueta = "03-LST-DES-IND",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Insignias y pastillas de campos en tarjetas de credenciales")
        }
    }
}
