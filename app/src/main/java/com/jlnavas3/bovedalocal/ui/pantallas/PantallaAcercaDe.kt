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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.acercade.GrupoAcercaDeApp
import com.jlnavas3.bovedalocal.ui.pantallas.acercade.GrupoAislamientoPrivacidad
import com.jlnavas3.bovedalocal.ui.pantallas.acercade.GrupoBiometriaSensores
import com.jlnavas3.bovedalocal.ui.pantallas.acercade.GrupoCriptografiaBlindaje
import com.jlnavas3.bovedalocal.ui.pantallas.acercade.GrupoHardwareSistema
import com.jlnavas3.bovedalocal.ui.pantallas.acercade.GrupoMemoriaAlmacenamiento
import com.jlnavas3.bovedalocal.ui.pantallas.acercade.IndicadorCargaAuditoria
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.InformeDiagnostico
import com.jlnavas3.bovedalocal.util.Portapapeles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PantallaAcercaDe(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var datosAuditoria by remember { mutableStateOf<InformeDiagnostico.DatosAuditoria?>(null) }
    var refresco by remember { mutableIntStateOf(0) }

    val reqAislamiento = remember { BringIntoViewRequester() }
    val reqHardware = remember { BringIntoViewRequester() }
    val reqMemoria = remember { BringIntoViewRequester() }
    val reqCripto = remember { BringIntoViewRequester() }

    LifecycleResumeEffect(Unit) {
        refresco++
        onPauseOrDispose { }
    }

    LaunchedEffect(refresco) {
        datosAuditoria = withContext(Dispatchers.IO) {
            InformeDiagnostico.recopilarAuditoria(contexto, vm.repositorio)
        }
    }

    LaunchedEffect(seccionDestino, datosAuditoria) {
        if (seccionDestino != null && datosAuditoria != null) {
            when {
                seccionDestino == "11.3.1.1" -> reqAislamiento.bringIntoView()
                seccionDestino == "11.3.1.2" || seccionDestino == "11.3.1.3" -> reqHardware.bringIntoView()
                seccionDestino == "11.3.1.4" -> reqMemoria.bringIntoView()
                seccionDestino == "11.3.1.5" -> reqCripto.bringIntoView()
                seccionDestino.startsWith("11.3.1.") && seccionDestino != "11.3.1" -> reqAislamiento.bringIntoView()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Acerca de y diagnóstico",
            alVolver = { vm.volverAtras() },
            conSeparador = scrollState.value > 0,
            colorFondo = ColorAjustesFondo
        )

        val datos = datosAuditoria
        if (datos == null) {
            IndicadorCargaAuditoria(modifier = Modifier.fillMaxSize())
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Arquitectura 100% offline, parámetros criptográficos y estado del dispositivo")
                Spacer(Modifier.height(10.dp))

                // 1. Aislamiento y Privacidad
                GrupoAislamientoPrivacidad(
                    datos = datos,
                    modifier = Modifier.bringIntoViewRequester(reqAislamiento)
                )

                Spacer(Modifier.height(16.dp))

                // 2. Criptografía y Blindaje
                GrupoCriptografiaBlindaje(
                    datos = datos,
                    modifier = Modifier.bringIntoViewRequester(reqCripto)
                )

                Spacer(Modifier.height(16.dp))

                // 3. Hardware y Sistema
                GrupoHardwareSistema(
                    datos = datos,
                    modifier = Modifier.bringIntoViewRequester(reqHardware)
                )

                Spacer(Modifier.height(16.dp))

                // 4. Memoria y Almacenamiento
                GrupoMemoriaAlmacenamiento(
                    datos = datos,
                    modifier = Modifier.bringIntoViewRequester(reqMemoria)
                )

                Spacer(Modifier.height(16.dp))

                // 5. Biometría y Sensores
                GrupoBiometriaSensores(datos = datos)

                Spacer(Modifier.height(16.dp))

                // 6. Acerca de Bóveda Local y Acciones
                GrupoAcercaDeApp(
                    alCopiarAuditoria = {
                        haptica.toque()
                        val informe = InformeDiagnostico.generar(contexto, datos)
                        Portapapeles.copiar(contexto, "Informe de auditoría", informe)
                        vm.avisar("Informe copiado al portapapeles")
                    },
                    alAbrirFichaApp = {
                        haptica.toque()
                        if (!AjustesSistema.abrirFichaApp(contexto)) {
                            vm.avisar("No se pudo abrir la ficha del sistema")
                        }
                    }
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
