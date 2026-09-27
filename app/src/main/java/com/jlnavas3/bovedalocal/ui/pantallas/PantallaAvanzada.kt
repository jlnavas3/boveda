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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoBorrarBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.DialogoCambioMaestra
import com.jlnavas3.bovedalocal.ui.pantallas.avanzada.GrupoCredencialMaestra
import com.jlnavas3.bovedalocal.ui.pantallas.avanzada.GrupoDesarrolloReferencia
import com.jlnavas3.bovedalocal.ui.pantallas.avanzada.GrupoRespuestaHaptica
import com.jlnavas3.bovedalocal.ui.pantallas.avanzada.GrupoZonaPeligro
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaAvanzada(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var dialogoCambio by remember { mutableStateOf(false) }
    var dialogoBorrar by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Opciones avanzadas",
                idEtiqueta = "06.1",
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
                // Grupo 1: Credencial maestra
                GrupoCredencialMaestra(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alSolicitarCambio = { dialogoCambio = true }
                )

                Spacer(Modifier.height(14.dp))

                // Grupo 2: Desarrollo y referencia
                GrupoDesarrolloReferencia(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alumbradoActivo = ajustes.alumbradoActivo,
                    alumbradoIntensidad = ajustes.alumbradoIntensidad,
                    alumbradoRepeticiones = ajustes.alumbradoRepeticiones,
                    alumbradoDuracionMs = ajustes.alumbradoDuracionMs,
                    alCambiarMostrarIds = { vm.ajustarMostrarIdsAjustes(it) },
                    alCambiarAlumbradoActivo = { vm.ajustarAlumbradoActivo(it) },
                    alCambiarIntensidad = { vm.ajustarAlumbradoIntensidad(it) },
                    alCambiarRepeticiones = { vm.ajustarAlumbradoRepeticiones(it) },
                    alCambiarDuracion = { vm.ajustarAlumbradoDuracionMs(it) },
                    haptica = haptica
                )

                Spacer(Modifier.height(14.dp))

                // Grupo 3: Respuesta táctil y vibración
                GrupoRespuestaHaptica(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    hapticaApp = ajustes.hapticaApp,
                    hapticaAppIntensidad = ajustes.hapticaAppIntensidad,
                    alCambiarHapticaApp = { vm.ajustarHapticaApp(it) },
                    alCambiarIntensidad = { vm.ajustarHapticaAppIntensidad(it) },
                    haptica = haptica
                )

                Spacer(Modifier.height(14.dp))

                // Grupo 4: Zona de peligro
                GrupoZonaPeligro(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    alSolicitarBorrado = { dialogoBorrar = true }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }

    if (dialogoCambio) {
        DialogoCambioMaestra(
            alDescartar = { dialogoCambio = false },
            alConfirmar = { actual, nueva ->
                dialogoCambio = false
                vm.cambiarContrasenaMaestra(actual, nueva)
            }
        )
    }

    if (dialogoBorrar) {
        DialogoBorrarBoveda(
            alDescartar = { dialogoBorrar = false },
            alConfirmar = {
                dialogoBorrar = false
                vm.repositorio.borrarTodo()
                vm.ir(Pantalla.Onboarding)
            }
        )
    }
}
