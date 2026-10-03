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
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.formatos.GrupoConfiguracionFormatos
import com.jlnavas3.bovedalocal.ui.pantallas.formatos.GrupoPreviaFormatos
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaFormatosCampos(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val ejemploFecha = FormateadorCampos.formatearFecha(2026, 11, 31, ajustes.formatoFecha)
    val ejemploHora = FormateadorCampos.formatearHora(19, 30, ajustes.formatoHora)
    val ejemploTel = FormateadorCampos.aplicarMascaraTelefono("612345678", ajustes.formatoTelefono)
    val ejemploDecimal = "1250${ajustes.separadorDecimal}50"
    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionId) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Formatos",
                idEtiqueta = "03-LST-FMT",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("03-LST-FMT-G01", "Vista previa"),
                            AccionSaltoGrupo("03-LST-FMT-G02", "Configuración de formatos")
                        ),
                        alRestablecerPantalla = {
                            haptica.tic()
                            vm.restablecerFormatos()
                            vm.avisar("Formatos restablecidos")
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
                // Grupo 1: Vista previa en tiempo real
                GrupoPreviaFormatos(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    ejemploFecha = ejemploFecha,
                    ejemploHora = ejemploHora,
                    ejemploTel = ejemploTel,
                    ejemploDecimal = ejemploDecimal
                )

                Spacer(Modifier.height(14.dp))

                // Grupo 2: Configuración de formatos
                GrupoConfiguracionFormatos(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    formatoFecha = ajustes.formatoFecha,
                    formatoHora = ajustes.formatoHora,
                    formatoTelefono = ajustes.formatoTelefono,
                    separadorDecimal = ajustes.separadorDecimal,
                    alCambiarFormatoFecha = {
                        haptica.tic()
                        vm.ajustarFormatoFecha(it)
                    },
                    alCambiarFormatoHora = {
                        haptica.tic()
                        vm.ajustarFormatoHora(it)
                    },
                    alCambiarFormatoTelefono = {
                        haptica.tic()
                        vm.ajustarFormatoTelefono(it)
                    },
                    alCambiarSeparadorDecimal = {
                        haptica.tic()
                        vm.ajustarSeparadorDecimal(it)
                    },
                    alRestablecerGrupo = {
                        haptica.tic()
                        vm.restablecerFormatos()
                        vm.avisar("Formatos restablecidos")
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
