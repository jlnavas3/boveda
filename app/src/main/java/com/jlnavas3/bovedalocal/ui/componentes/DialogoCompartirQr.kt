package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.util.GeneradorQr
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.Portapapeles
import kotlinx.coroutines.delay

@Composable
fun DialogoCompartirQr(
    entrada: Entrada,
    alCerrar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    val estadoQr = remember(entrada) {
        ExtractorModosQr.extraerEstado(entrada)
    }

    var modoSeleccionado by remember(estadoQr) {
        mutableStateOf(estadoQr.modoInicial)
    }

    val textoQr = remember(entrada, modoSeleccionado, estadoQr) {
        ExtractorModosQr.generarTextoQr(entrada, modoSeleccionado, estadoQr)
    }

    val qrBitmap = remember(textoQr) {
        try {
            GeneradorQr.generarBitmap(textoQr, tamano = 640)
        } catch (_: Exception) {
            null
        }
    }

    var tiempoRestante by remember { mutableIntStateOf(60) }
    LaunchedEffect(Unit) {
        while (tiempoRestante > 0) {
            delay(1000)
            tiempoRestante--
        }
        alCerrar()
    }

    ModalInferiorBoveda(
        abierto = true,
        alCerrar = alCerrar,
        fijarAbajo = false
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            CabeceraCompartirQr(
                esWifi = estadoQr.esWifi,
                modoSeleccionado = modoSeleccionado,
                ssidWifi = estadoQr.ssidWifi,
                tituloEntrada = entrada.titulo,
                onCopiar = {
                    haptica.toque()
                    Portapapeles.copiar(contexto, "Contenido QR", textoQr)
                },
                onCerrar = alCerrar
            )

            Spacer(Modifier.height(16.dp))

            val fondoSelector = ColorCampoAjustes
            SelectorModosCompartirQr(
                modosDisponibles = estadoQr.modosDisponibles,
                modoSeleccionado = modoSeleccionado,
                fondoSelector = fondoSelector,
                onSeleccionar = { modo ->
                    haptica.tic()
                    modoSeleccionado = modo
                }
            )

            if (estadoQr.modosDisponibles.size > 1) {
                Spacer(Modifier.height(16.dp))
            }

            VisorCodigoQr(qrBitmap = qrBitmap)

            Spacer(Modifier.height(14.dp))

            Text(
                text = modoSeleccionado.descripcionInformativa,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(Modifier.height(14.dp))

            CapsulaTemporizadorSeguridad(tiempoRestante = tiempoRestante)
        }
    }
}
