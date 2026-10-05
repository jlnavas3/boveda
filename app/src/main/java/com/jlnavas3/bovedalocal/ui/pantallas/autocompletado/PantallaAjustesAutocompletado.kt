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
                            AccionSaltoGrupo("04-HER-PSK-G02", "Proveedor del sistema")
                        ),
                        alRestablecerPantalla = {
                            vm.ajustarAutofillSugerenciasTeclado(true)
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
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alCambiarSugerenciasTeclado = { valor ->
                        haptica.tic()
                        vm.ajustarAutofillSugerenciasTeclado(valor)
                    },
                    alRestablecer = {
                        vm.ajustarAutofillSugerenciasTeclado(true)
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
