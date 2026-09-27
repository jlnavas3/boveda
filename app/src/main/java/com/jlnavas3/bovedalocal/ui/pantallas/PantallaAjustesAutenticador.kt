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
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.autenticador.GrupoAccesosRapidosTotp
import com.jlnavas3.bovedalocal.ui.pantallas.autenticador.GrupoValoresManualesTotp
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaAjustesAutenticador(
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
                titulo = "Autenticador 2FA",
                idEtiqueta = "04.1",
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
                DescripcionPantalla(subtitulo = "Parámetros predeterminados para códigos TOTP generados manualmente")
                Spacer(Modifier.height(10.dp))

                // Grupo 1: Valores manuales
                GrupoValoresManualesTotp(
                    mostrarId = ajustes.mostrarIdsAjustes,
                    totpManualDigitos = ajustes.totpManualDigitos,
                    totpManualPeriodo = ajustes.totpManualPeriodo,
                    totpManualAlgoritmo = ajustes.totpManualAlgoritmo,
                    totpSepararDigitos = ajustes.totpSepararDigitos,
                    alCambiarDigitos = { valor ->
                        haptica.tic()
                        vm.ajustarTotpManualDigitos(valor)
                    },
                    alCambiarPeriodo = { valor ->
                        haptica.tic()
                        vm.ajustarTotpManualPeriodo(valor)
                    },
                    alCambiarAlgoritmo = { valor ->
                        haptica.tic()
                        vm.ajustarTotpManualAlgoritmo(valor)
                    },
                    alCambiarSepararDigitos = {
                        haptica.tic()
                        vm.ajustarTotpSepararDigitos(it)
                    },
                    alRestablecerGrupo = {
                        vm.ajustarTotpManualDigitos(6)
                        vm.ajustarTotpManualPeriodo(30)
                        vm.ajustarTotpManualAlgoritmo("HmacSHA1")
                        vm.ajustarTotpSepararDigitos(true)
                    }
                )

                Spacer(Modifier.height(14.dp))

                // Grupo 2: Accesos rápidos
                GrupoAccesosRapidosTotp(
                    mostrarId = ajustes.mostrarIdsAjustes,
                    alNavegarAjustesWidget = {
                        haptica.tic()
                        vm.ir(Pantalla.AjustesWidget("03.3"))
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
