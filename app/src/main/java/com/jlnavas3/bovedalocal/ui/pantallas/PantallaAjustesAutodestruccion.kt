package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.BovedaSenuelo
import com.jlnavas3.bovedalocal.data.PinAutodestruccion
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.autodestruccion.AlertaCriticaAutodestruccion
import com.jlnavas3.bovedalocal.ui.pantallas.autodestruccion.GrupoConfiguracionPinAutodestruccion
import com.jlnavas3.bovedalocal.ui.pantallas.autodestruccion.TarjetaExplicativaAutodestruccion
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaAjustesAutodestruccion(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var datosAutodestruccion by remember { mutableStateOf(PinAutodestruccion.cargar(contexto)) }
    var activo by remember { mutableStateOf(datosAutodestruccion.activo) }
    var pinNuevo by remember { mutableStateOf("") }
    var pinConfirmar by remember { mutableStateOf("") }
    var verPin by remember { mutableStateOf(false) }

    val reqExplicacion = remember { BringIntoViewRequester() }
    val reqPin = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null) {
            when {
                seccionDestino == "01.3.1" -> reqPin.bringIntoView()
                seccionDestino.startsWith("01.3.") && seccionDestino != "01.3" -> reqPin.bringIntoView()
            }
        }
    }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "PIN de autodestrucción",
                idEtiqueta = "01.3",
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
                DescripcionPantalla(subtitulo = "Borrado permanente ante coerción o emergencia extrema")
                Spacer(Modifier.height(10.dp))

                // Banner de Alerta Crítica
                AlertaCriticaAutodestruccion()

                Spacer(Modifier.height(16.dp))

                // Tarjeta Explicativa
                TarjetaExplicativaAutodestruccion(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    modifier = Modifier.bringIntoViewRequester(reqExplicacion)
                )

                Spacer(Modifier.height(16.dp))

                // Configuración y formulario del PIN
                GrupoConfiguracionPinAutodestruccion(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    activo = activo,
                    pinNuevo = pinNuevo,
                    pinConfirmar = pinConfirmar,
                    verPin = verPin,
                    tieneHashGuardado = datosAutodestruccion.hashHex.isNotBlank(),
                    alCambiarActivo = { nuevoEstado ->
                        haptica.tic()
                        activo = nuevoEstado
                        if (!nuevoEstado) {
                            PinAutodestruccion.desactivar(contexto)
                            datosAutodestruccion = PinAutodestruccion.cargar(contexto)
                            vm.avisar("PIN de autodestrucción desactivado")
                        }
                    },
                    alCambiarPinNuevo = { pinNuevo = it },
                    alCambiarPinConfirmar = { pinConfirmar = it },
                    alAlternarVerPin = { verPin = !verPin },
                    alGuardarPin = {
                        when {
                            pinNuevo.length < 4 -> {
                                haptica.error()
                                vm.avisar("El PIN debe tener al menos 4 caracteres")
                            }
                            pinNuevo != pinConfirmar -> {
                                haptica.error()
                                vm.avisar("Los PIN ingresados no coinciden")
                            }
                            BovedaSenuelo.esPinCoaccion(contexto, pinNuevo) -> {
                                haptica.error()
                                vm.avisar("El PIN no puede ser idéntico al de la Bóveda Señuelo")
                            }
                            else -> {
                                haptica.exito()
                                PinAutodestruccion.configurar(contexto, pinNuevo)
                                datosAutodestruccion = PinAutodestruccion.cargar(contexto)
                                pinNuevo = ""
                                pinConfirmar = ""
                                vm.avisar("PIN de autodestrucción configurado correctamente")
                            }
                        }
                    },
                    modifier = Modifier.bringIntoViewRequester(reqPin)
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
