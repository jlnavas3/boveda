package com.jlnavas3.bovedalocal.ui.pantallas.historial

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
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla atómica dedicada al historial de versiones previas por credencial y retención en papelera.
 */
@Composable
fun PantallaHistorialCredenciales(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val opcionesHistorialEntrada = remember {
        AlmacenAjustes.OPCIONES_MAX_HISTORIAL_CONTRASENAS_POR_ENTRADA.map { (valor, etiqueta) ->
            OpcionSelectorModal(
                valor = valor,
                etiquetaFila = etiqueta,
                etiquetaModal = etiqueta,
                descripcionModal = if (valor == 0) "No conserva versiones anteriores al modificar una credencial" else "Guarda hasta las últimas $valor versiones de contraseñas de cada entrada",
                icono = Icons.Filled.LockReset
            )
        }
    }

    val opcionesRetencionPapelera = remember {
        AlmacenAjustes.OPCIONES_DIAS_RETENCION_PAPELERA.map { (dias, etiqueta) ->
            OpcionSelectorModal(
                valor = dias,
                etiquetaFila = etiqueta,
                etiquetaModal = etiqueta,
                descripcionModal = if (dias <= 0) "Las entradas descartadas permanecen en la papelera indefinidamente" else "Las entradas descartadas se borran automáticamente tras $dias días",
                icono = Icons.Filled.DeleteSweep
            )
        }
    }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Historial por entrada y papelera",
                idEtiqueta = "04-HER-HST-ENT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("04-HER-HST-G03", "Credenciales y papelera")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.restablecerAjustesRetencionEHistorial()
                            vm.avisar("Ciclo de vida y retención restablecidos")
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
                DescripcionPantalla(subtitulo = "Versiones previas de contraseñas por entrada y retención de papelera")
                Spacer(Modifier.height(10.dp))

                ComponenteGrupo(
                    etiqueta = "Credenciales y papelera",
                    idGrupo = "04-HER-HST-G03",
                    mostrarId = ajustes.mostrarIdsAjustes
                ) {
                    ComponenteSelectorModal(
                        titulo = "Versiones previas por entrada",
                        descripcionModal = "Cantidad máxima de contraseñas anteriores guardadas por cada entrada al modificarla",
                        icono = null,
                        idFila = "04-HER-HST-ENT-SEL",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.maxHistorialContrasenasPorEntrada,
                        opciones = opcionesHistorialEntrada,
                        alSeleccionar = { max ->
                            haptica.tic()
                            vm.ajustarMaxHistorialContrasenasPorEntrada(max)
                        }
                    )

                    ComponenteSeparador(sangriaInicio = 16.dp)

                    ComponenteSelectorModal(
                        titulo = "Retención de la papelera",
                        descripcionModal = "Días antes de purgar permanentemente las entradas descartadas de la papelera",
                        icono = null,
                        idFila = "04-HER-HST-PAP",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        valorSeleccionado = ajustes.diasRetencionPapelera,
                        opciones = opcionesRetencionPapelera,
                        alSeleccionar = { dias ->
                            haptica.tic()
                            vm.ajustarDiasRetencionPapelera(dias)
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
private fun PantallaHistorialCredencialesPreview() {
    BovedaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Historial por entrada y papelera",
                idEtiqueta = "04-HER-HST-ENT",
                mostrarId = true,
                alVolver = {},
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )
            DescripcionPantalla(subtitulo = "Versiones previas de contraseñas por entrada y retención de papelera")
        }
    }
}
