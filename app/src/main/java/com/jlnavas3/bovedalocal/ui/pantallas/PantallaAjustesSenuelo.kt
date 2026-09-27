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
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.senuelo.AlertaBiometriaSenuelo
import com.jlnavas3.bovedalocal.ui.pantallas.senuelo.GrupoConfiguracionPinSenuelo
import com.jlnavas3.bovedalocal.ui.pantallas.senuelo.GrupoCuentasSimuladasSenuelo
import com.jlnavas3.bovedalocal.ui.pantallas.senuelo.TarjetaExplicativaSenuelo
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaAjustesSenuelo(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var datosSenuelo by remember { mutableStateOf(BovedaSenuelo.cargar(contexto)) }
    var activo by remember { mutableStateOf(datosSenuelo.activo) }
    var pinCoaccion by remember { mutableStateOf("") }
    var verPin by remember { mutableStateOf(false) }

    val reqExplicacion = remember { BringIntoViewRequester() }
    val reqPin = remember { BringIntoViewRequester() }
    val reqCuentas = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null) {
            when {
                seccionDestino == "01.3.1" -> reqExplicacion.bringIntoView()
                seccionDestino == "01.3.2" -> reqPin.bringIntoView()
                seccionDestino == "01.3.3" -> reqCuentas.bringIntoView()
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
                titulo = "Modo señuelo",
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
                DescripcionPantalla(subtitulo = "Protección anti-extorsión y apertura señuelo transparente")
                Spacer(Modifier.height(10.dp))

                // Tarjeta Explicativa
                TarjetaExplicativaSenuelo(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    modifier = Modifier.bringIntoViewRequester(reqExplicacion)
                )

                Spacer(Modifier.height(16.dp))

                // Configuración del PIN de Coacción
                GrupoConfiguracionPinSenuelo(
                    mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                    activo = activo,
                    pinCoaccion = pinCoaccion,
                    verPin = verPin,
                    tieneHashGuardado = datosSenuelo.hashHex.isNotBlank(),
                    alCambiarActivo = { nuevoEstado ->
                        haptica.tic()
                        activo = nuevoEstado
                        if (!nuevoEstado) {
                            BovedaSenuelo.desactivar(contexto)
                            datosSenuelo = BovedaSenuelo.cargar(contexto)
                            vm.avisar("Bóveda señuelo desactivada")
                        }
                    },
                    alCambiarPinCoaccion = { pinCoaccion = it },
                    alAlternarVerPin = { verPin = !verPin },
                    alGuardarPin = {
                        if (pinCoaccion.isBlank()) {
                            haptica.error()
                            vm.avisar("Introduce un PIN de coacción válido")
                        } else {
                            haptica.exito()
                            BovedaSenuelo.configurar(contexto, pinCoaccion, poblarCuentasEjemplo = datosSenuelo.entradas.isEmpty())
                            datosSenuelo = BovedaSenuelo.cargar(contexto)
                            pinCoaccion = ""
                            vm.avisar("Bóveda señuelo configurada correctamente")
                        }
                    },
                    modifier = Modifier.bringIntoViewRequester(reqPin)
                )

                // Alerta de Biometría si la bóveda señuelo está activada
                if (activo) {
                    Spacer(Modifier.height(16.dp))
                    AlertaBiometriaSenuelo(
                        biometriaActiva = ajustes.biometriaActiva,
                        alDesactivarBiometria = {
                            haptica.exito()
                            vm.repositorio.desactivarBiometria()
                            vm.avisar("Acceso por huella desactivado")
                        }
                    )
                }

                // Estado y Cuentas Señuelo
                if (activo) {
                    Spacer(Modifier.height(16.dp))
                    GrupoCuentasSimuladasSenuelo(
                        mostrarIdsAjustes = ajustes.mostrarIdsAjustes,
                        cantidadCuentas = datosSenuelo.entradas.size,
                        alRestablecerEjemplos = {
                            haptica.toque()
                            val ejemplos = BovedaSenuelo.cuentasEjemplo()
                            BovedaSenuelo.guardarEntradasSenuelo(contexto, ejemplos)
                            datosSenuelo = BovedaSenuelo.cargar(contexto)
                            vm.avisar("Cuentas de ejemplo restablecidas (${ejemplos.size})")
                        },
                        modifier = Modifier.bringIntoViewRequester(reqCuentas)
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
