package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.jlnavas3.bovedalocal.camara.EstadoCamara
import com.jlnavas3.bovedalocal.camara.MotorCamaraLegado
import com.jlnavas3.bovedalocal.camara.MotorCameraX
import com.jlnavas3.bovedalocal.ui.theme.Superficie

@Composable
fun VistaCameraX(
    alFrame: (ByteArray, Int, Int) -> Unit,
    alEstado: (EstadoCamara) -> Unit,
    modifier: Modifier = Modifier,
    alMotorListo: ((MotorCameraX) -> Unit)? = null
) {
    val contexto = LocalContext.current
    val dueno = LocalLifecycleOwner.current
    val frame = rememberUpdatedState(alFrame)
    val estado = rememberUpdatedState(alEstado)
    val motorListo = rememberUpdatedState(alMotorListo)
    val motor = remember {
        MotorCameraX(
            contexto = contexto,
            dueno = dueno,
            alFrame = { d, w, h -> frame.value(d, w, h) },
            alEstado = { e -> estado.value(e) }
        ).also { m ->
            motorListo.value?.invoke(m)
        }
    }
    DisposableEffect(motor) {
        onDispose { motor.detener() }
    }
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
                setBackgroundColor(Superficie.toArgb())
                motor.iniciar(this)
            }
        }
    )
}

@Composable
fun VistaLegado(
    alFrame: (ByteArray, Int, Int) -> Unit,
    alEstado: (EstadoCamara) -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val dueno = LocalLifecycleOwner.current
    val frame = rememberUpdatedState(alFrame)
    val estado = rememberUpdatedState(alEstado)
    val motor = remember {
        MotorCamaraLegado(
            contexto = contexto,
            dueno = dueno,
            alFrame = { d, w, h -> frame.value(d, w, h) },
            alEstado = { e -> estado.value(e) }
        )
    }
    DisposableEffect(motor) {
        onDispose { motor.detener() }
    }
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { motor.crearVista() }
    )
}
