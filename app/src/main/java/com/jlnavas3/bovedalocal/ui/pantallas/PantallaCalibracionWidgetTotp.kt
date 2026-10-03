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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.AjustesDefaults
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widgettotp.SeccionColoresWidgetTotp
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widgettotp.SeccionFormaYTransparenciaWidgetTotp
import com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widgettotp.SimuladorWidgetTotpFlotante
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.BotonMenuOpcionesPantalla
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

@Composable
fun PantallaCalibracionWidgetTotp(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var vistaBloqueadaEnPreview by remember { mutableStateOf(false) }

    var ahoraSegundos by remember { mutableLongStateOf(System.currentTimeMillis() / 1000) }
    LaunchedEffect(Unit) {
        while (true) {
            ahoraSegundos = System.currentTimeMillis() / 1000
            delay(1000)
        }
    }
    val segundosRestantes = Totp.segundosRestantes(ahoraSegundos, 30L)

    val colorBordeEfectivo = parsearColorO(ajustes.widgetColorBorde, parsearColorO(AjustesDefaults.WidgetTotp.COLOR_BORDE, ColorAcento))
    val colorContadorEfectivo = parsearColorO(ajustes.widgetColorContador, Color.White)
    val colorCodigoEfectivo = parsearColorO(ajustes.widgetColorCodigo, parsearColorO(AjustesDefaults.WidgetTotp.COLOR_CODIGO, ColorAcento))
    val colorTituloIconoEfectivo = parsearColorO(ajustes.widgetColorTituloIcono, Color.White)
    val colorFilasEfectivo = parsearColorO(
        if (ajustes.widgetColorFilas.isBlank() || ajustes.widgetColorFilas == AjustesDefaults.WidgetTotp.COLOR_FILAS)
            AjustesDefaults.WidgetTotp.COLOR_FILAS_DEFECTO
        else
            ajustes.widgetColorFilas,
        ColorCampoAjustes
    )

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Calibración Códigos 2FA",
                idEtiqueta = "04-HER-WGT-CAL",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = false,
                colorFondo = ColorAjustesFondo,
                acciones = {
                    BotonMenuOpcionesPantalla(
                        alRestablecerPantalla = {
                            vm.restablecerAjustesWidget()
                        },
                        mensajeToastRestablecer = "Aspecto del widget 2FA restablecido"
                    )
                }
            )

            SimuladorWidgetTotpFlotante(
                ajustes = ajustes,
                segundosRestantes = segundosRestantes,
                colorBordeEfectivo = colorBordeEfectivo,
                colorContadorEfectivo = colorContadorEfectivo,
                colorCodigoEfectivo = colorCodigoEfectivo,
                colorTituloIconoEfectivo = colorTituloIconoEfectivo,
                colorFilasEfectivo = colorFilasEfectivo,
                vistaBloqueadaEnPreview = vistaBloqueadaEnPreview,
                haptica = haptica,
                alAlternarBloqueo = { vistaBloqueadaEnPreview = !vistaBloqueadaEnPreview }
            )

            Spacer(Modifier.height(6.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                SeccionFormaYTransparenciaWidgetTotp(
                    ajustes = ajustes,
                    vm = vm,
                    haptica = haptica
                )

                Spacer(Modifier.height(16.dp))

                SeccionColoresWidgetTotp(
                    ajustes = ajustes,
                    vm = vm,
                    colorBordeEfectivo = colorBordeEfectivo,
                    colorContadorEfectivo = colorContadorEfectivo,
                    colorCodigoEfectivo = colorCodigoEfectivo,
                    colorTituloIconoEfectivo = colorTituloIconoEfectivo,
                    colorFilasEfectivo = colorFilasEfectivo,
                    haptica = haptica
                )

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
