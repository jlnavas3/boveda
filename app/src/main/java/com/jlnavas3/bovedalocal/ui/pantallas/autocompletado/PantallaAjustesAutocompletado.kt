package com.jlnavas3.bovedalocal.ui.pantallas.autocompletado

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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.AccionSaltoGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaAjustesAutocompletado(
    vm: VaultViewModel,
    actividad: FragmentActivity,
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
                titulo = "Autocompletado y llaves",
                idEtiqueta = "04-HER-PSK",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        grupos = listOf(
                            AccionSaltoGrupo("04-HER-PSK-G01", "Sugerencias en teclado"),
                            AccionSaltoGrupo("04-HER-PSK-G02", "Proveedor del sistema"),
                            AccionSaltoGrupo("04-HER-PSK-G03", "Reglas de detección")
                        ),
                        alRestablecerPantalla = {
                            vm.ajustarAutofillSugerenciasTeclado(true)
                            vm.restablecerReglasAutocompletado()
                        },
                        mensajeToastRestablecer = "Autocompletado restablecido"
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
                GrupoSugerenciasTeclado(
                    sugerenciasTeclado = ajustes.autofillSugerenciasTeclado,
                    maxSugerencias = ajustes.maxSugerenciasAutofill,
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alCambiarSugerenciasTeclado = { valor ->
                        haptica.tic()
                        vm.ajustarAutofillSugerenciasTeclado(valor)
                    },
                    alCambiarMaxSugerencias = { valor ->
                        haptica.tic()
                        vm.ajustarMaxSugerenciasAutofill(valor)
                    },
                    alRestablecer = {
                        vm.ajustarAutofillSugerenciasTeclado(true)
                        vm.ajustarMaxSugerenciasAutofill(5)
                    }
                )

                Spacer(Modifier.height(16.dp))

                GrupoReglasDeteccionAutofill(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alAbrirReglas = {
                        haptica.tic()
                        vm.ir(com.jlnavas3.bovedalocal.ui.Pantalla.ReglasAutocompletado)
                    }
                )

                Spacer(Modifier.height(16.dp))

                GrupoProveedorCredenciales(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alConfigurarProveedor = {
                        haptica.tic()
                        if (!AjustesSistema.abrirProveedorCredenciales(actividad)) {
                            vm.avisar("Ajustes › Contraseñas y cuentas › Contraseñas y llaves de acceso")
                        }
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
