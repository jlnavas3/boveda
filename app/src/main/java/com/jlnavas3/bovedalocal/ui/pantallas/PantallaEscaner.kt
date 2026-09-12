package com.jlnavas3.bovedalocal.ui.pantallas

import android.Manifest
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.PepoBovedaApp
import com.jlnavas3.bovedalocal.camara.EstadoCamara
import com.jlnavas3.bovedalocal.camara.LectorImagenes
import com.jlnavas3.bovedalocal.camara.LectorQr
import com.jlnavas3.bovedalocal.camara.MotorCamara
import com.jlnavas3.bovedalocal.camara.MotorCamaraLegado
import com.jlnavas3.bovedalocal.camara.MotorCameraX
import com.jlnavas3.bovedalocal.camara.PermisoCamara
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.CampoPepo
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepo
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/** Qué ha pasado con el permiso de cámara desde que se abrió la pantalla. */
private enum class EstadoPermiso { NO_PEDIDO, DENEGADO, DENEGADO_PARA_SIEMPRE }

@Composable
fun PantallaEscaner(
    vm: VaultViewModel,
    actividad: FragmentActivity,
    entradaDestino: String?,
    soloManual: Boolean = false
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ambito = rememberCoroutineScope()
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val motorPreferido = MotorCamara.desde(ajustes.motorCamara)

    var permiso by remember { mutableStateOf(PermisoCamara.concedido(contexto)) }
    var estadoPermiso by remember { mutableStateOf(EstadoPermiso.NO_PEDIDO) }
    var manual by remember { mutableStateOf("") }
    var fallo by remember { mutableStateOf(false) }
    var qrPasskey by remember { mutableStateOf(false) }
    var avisoImagen by remember { mutableStateOf<String?>(null) }
    var leyendoImagen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!soloManual) Diagnostico.apuntar("camara", "Escáner abierto (motor ajustado: ${motorPreferido.clave})")
    }

    /** Devuelve true si el texto valía y el 2FA se guardó. Deja [qrPasskey] al día en todos los casos. */
    fun procesarTexto(texto: String, origen: String): Boolean {
        qrPasskey = LectorQr.esQrDePasskey(texto)
        if (qrPasskey) {
            Diagnostico.apuntar("2fa", "Escaneo detectó un QR de Passkey en lugar de TOTP")
            return false
        }
        if (!vm.altaTotp(texto, entradaDestino)) {
            Diagnostico.apuntar("2fa", "Formato de clave 2FA no reconocido ($origen)")
            return false
        }
        val detalle = if (origen == "el teclado") "escrito a mano desde teclado" else "escaneado desde $origen"
        Diagnostico.apuntar("2fa", "Doble factor (TOTP) añadido exitosamente ($detalle)")
        vm.avisar("Doble factor añadido")
        return true
    }

    val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        permiso = concedido
        if (concedido) {
            estadoPermiso = EstadoPermiso.NO_PEDIDO
            Diagnostico.apuntar("camara", "Permiso de cámara concedido")
        } else {
            // Acabamos de pedirlo y lo han negado: si Android ya no deja explicarlo, es que
            // no lo va a volver a preguntar (dos negativas, o "no volver a preguntar").
            val paraSiempre = !ActivityCompat.shouldShowRequestPermissionRationale(actividad, Manifest.permission.CAMERA)
            estadoPermiso = if (paraSiempre) EstadoPermiso.DENEGADO_PARA_SIEMPRE else EstadoPermiso.DENEGADO
            Diagnostico.apuntar("camara", "Permiso de cámara denegado" + if (paraSiempre) " (Android ya no lo preguntará)" else "")
        }
    }

    // Si has venido a escanear, la cámara se abre sola: pedir permiso es el
    // único paso que Android no me deja saltarme.
    var permisoPedido by remember { mutableStateOf(false) }
    LaunchedEffect(soloManual) {
        if (!soloManual && !permiso && !permisoPedido) {
            permisoPedido = true
            pedirPermiso.launch(Manifest.permission.CAMERA)
        }
    }

    // Al volver de la ficha de la app con el permiso ya dado, la cámara arranca sola.
    LifecycleResumeEffect(Unit) {
        val ahora = PermisoCamara.concedido(contexto)
        if (ahora != permiso) {
            permiso = ahora
            if (ahora) estadoPermiso = EstadoPermiso.NO_PEDIDO
        }
        onPauseOrDispose { }
    }

    val elegirImagen = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        PepoBovedaApp.salidaTerminada(contexto)
        if (uri == null) {
            Diagnostico.apuntar("camara", "Selector de imagen cerrado sin elegir nada")
            return@rememberLauncherForActivityResult
        }
        leyendoImagen = true
        avisoImagen = null
        ambito.launch {
            val texto = withContext(Dispatchers.Default) { LectorImagenes.leerQr(contexto, uri) }
            leyendoImagen = false
            when {
                texto == null -> {
                    haptica.error()
                    avisoImagen = "No veo ningún QR en esa imagen. Prueba con una captura más nítida, o escribe la clave a mano."
                }
                procesarTexto(texto, "una imagen") -> haptica.exito()
                qrPasskey -> haptica.error()
                else -> {
                    haptica.error()
                    avisoImagen = "La imagen tiene un QR, pero no es de un doble factor."
                }
            }
        }
    }

    fun abrirSelectorDeImagen() {
        avisoImagen = null
        // Salimos a otra app un momento a por un resultado: que eso no cierre la bóveda.
        PepoBovedaApp.salidaPendiente(contexto)
        try {
            elegirImagen.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } catch (e: Exception) {
            PepoBovedaApp.salidaTerminada(contexto)
            Diagnostico.apuntar("camara", "No se pudo abrir el selector de imágenes", e)
            avisoImagen = "Este móvil no tiene ningún selector de imágenes que pueda abrir."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        CabeceraPantalla(
            titulo = "Añadir Doble Factor",
            subtitulo = "Escaneo seguro offline de QR o introducción manual",
            alVolver = { vm.volverAtras() }
        )

        if (soloManual) {
            // Nada de cámara: has venido a teclear la clave.
        } else {
            if (permiso) {
                ZonaCamara(
                    motorPreferido = motorPreferido,
                    alLeer = { texto, origen ->
                        val valido = procesarTexto(texto, origen)
                        if (valido) haptica.exito() else haptica.error()
                        valido
                    },
                    alElegirImagen = { abrirSelectorDeImagen() }
                )
            } else {
                TarjetaPepo {
                    Text("Escanear el QR", color = ColorTitulos, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Para leerlo, Android tiene que darme la cámara. Se usa solo aquí, para descifrar ese QR, y no hay permiso de red con el que enviar nada.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(12.dp))
                    when (estadoPermiso) {
                        EstadoPermiso.DENEGADO_PARA_SIEMPRE -> {
                            Text(
                                "Android ya no me deja pedirlo desde aquí. Ábrelo tú en la ficha de la app (Permisos > Cámara) y vuelve: la cámara arrancará sola.",
                                color = Peligro,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(Modifier.height(10.dp))
                            BotonAmbar("Abrir la ficha de la app") {
                                if (!AjustesSistema.abrirFichaApp(contexto)) vm.avisar("No encuentro la ficha de la app en este móvil")
                            }
                        }
                        EstadoPermiso.DENEGADO -> {
                            BotonAmbar("Usar la cámara") { pedirPermiso.launch(Manifest.permission.CAMERA) }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Sin permiso no hay cámara, y no pasa nada: lee el QR desde una captura o escribe el código a mano aquí abajo.",
                                color = Peligro,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        EstadoPermiso.NO_PEDIDO -> BotonAmbar("Usar la cámara") { pedirPermiso.launch(Manifest.permission.CAMERA) }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            BotonBorde(if (leyendoImagen) "Buscando el QR en la imagen…" else "Leer el QR de una imagen") {
                if (!leyendoImagen) abrirSelectorDeImagen()
            }
        }

        avisoImagen?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Peligro, style = MaterialTheme.typography.bodyMedium)
        }

        if (qrPasskey) {
            Spacer(Modifier.height(18.dp))
            TarjetaPepo {
                Text("Ese QR es de una llave de acceso, no de un 2FA", color = Peligro, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                Text(
                    buildAnnotatedString {
                        append("Ese código lo enseña el navegador de tu ordenador para pasar la llave al móvil, y para eso hace falta internet y Bluetooth. Yo no tengo permiso de red, así que no puedo leerlo, y prefiero decírtelo a fingir que funciona.\n\nLas llaves de acceso no se escanean aquí. Abre la web en el navegador del propio móvil y, cuando te pregunte dónde guardar la llave, elige ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("Bóveda local") }
                        append(". Si no aparezco en esa lista, actívame en Ajustes.")
                    },
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.height(18.dp))
        Box(modifier = Modifier.fillMaxWidth().clip(FormaTarjeta)) {
            Column(modifier = Modifier.fillMaxWidth().padding(0.dp)) {
                Text(
                    if (soloManual) "Pega o escribe la clave" else "O escríbelo a mano",
                    color = ColorTitulos,
                    style = MaterialTheme.typography.titleMedium
                )
                if (soloManual) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Es la clave que la web te da junto al QR, la que suele venir " +
                            "en bloques de cuatro letras. Sirve igual que escanearlo.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(Modifier.height(10.dp))
                CampoPepo(
                    valor = manual,
                    etiqueta = "Clave del 2FA o enlace otpauth://",
                    alCambiar = { manual = it; fallo = false },
                    monoespaciada = true
                )
                if (fallo) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Eso no me sirve. Espero la clave en Base32 (letras A-Z y números 2-7) o un enlace otpauth://totp/...",
                        color = Peligro,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(Modifier.height(12.dp))
                BotonAmbar("Añadir este código", activo = manual.isNotBlank()) {
                    val texto = manual.trim()
                    if (!procesarTexto(texto, "el teclado") && !qrPasskey) fallo = true
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        BotonBorde("Cancelar") { vm.volverAtras() }
        Spacer(Modifier.height(32.dp))
    }
}

/**
 * El visor con su cadena de motores: CameraX y, si falla y el ajuste es automático, el
 * compatible. Cuando los dos fallan, lo dice claro y ofrece leer el QR de una imagen.
 * [alLeer] devuelve false si el texto no valía, para seguir escaneando.
 */
@Composable
private fun ZonaCamara(
    motorPreferido: MotorCamara,
    alLeer: (String, String) -> Boolean,
    alElegirImagen: () -> Unit
) {
    var motorActual by remember(motorPreferido) {
        mutableStateOf(if (motorPreferido == MotorCamara.COMPATIBLE) MotorCamara.COMPATIBLE else MotorCamara.CAMERAX)
    }
    var estado by remember(motorPreferido) { mutableStateOf<EstadoCamara>(EstadoCamara.Iniciando) }
    var falloCameraX by remember(motorPreferido) { mutableStateOf<EstadoCamara.Fallo?>(null) }
    val yaLeido = remember { AtomicBoolean(false) }
    val contadorFrames = remember { AtomicInteger(0) }
    val principal = remember { Handler(Looper.getMainLooper()) }
    val alLeerActual = rememberUpdatedState(alLeer)

    val alFrame: (ByteArray, Int, Int) -> Unit = { datos, ancho, alto ->
        if (!yaLeido.get()) {
            // El intento invertido (QR claro sobre negro) cuesta otra pasada: uno de cada cuatro frames.
            val n = contadorFrames.incrementAndGet()
            val texto = LectorQr.decodificarLuminancia(datos, ancho, alto, probarInvertido = n % 4 == 0)
            if (texto != null && yaLeido.compareAndSet(false, true)) {
                val origen = "la cámara (${motorActual.clave})"
                principal.post {
                    val valido = alLeerActual.value(texto, origen)
                    // Si no valía (llave de acceso, texto raro), en un par de segundos se vuelve a escanear.
                    if (!valido) principal.postDelayed({ yaLeido.set(false) }, 2_500)
                }
            }
        }
    }

    val alEstado: (EstadoCamara) -> Unit = { nuevo ->
        if (nuevo is EstadoCamara.Fallo && motorActual == MotorCamara.CAMERAX && motorPreferido == MotorCamara.AUTOMATICO) {
            Diagnostico.apuntar("camara", "CameraX falló; cambio al motor compatible")
            falloCameraX = nuevo
            estado = EstadoCamara.Iniciando
            motorActual = MotorCamara.COMPATIBLE
        } else {
            estado = nuevo
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(FormaTarjeta)
            .background(Superficie)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
                else Modifier
            )
    ) {
        if (estado !is EstadoCamara.Fallo) {
            key(motorActual) {
                when (motorActual) {
                    MotorCamara.COMPATIBLE -> VistaLegado(alFrame, alEstado)
                    else -> VistaCameraX(alFrame, alEstado)
                }
            }
        }

        when (val actual = estado) {
            EstadoCamara.Iniciando -> Aviso(
                if (falloCameraX != null) "CameraX no ha podido. Abriendo el motor compatible…" else "Abriendo la cámara…",
                Modifier.align(Alignment.BottomCenter)
            )
            is EstadoCamara.Funcionando -> if (!actual.conImagen) {
                Aviso("Esta cámara no da imagen en pantalla, pero está leyendo: apunta al QR igualmente.", Modifier.align(Alignment.Center))
            }
            is EstadoCamara.Fallo -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("La cámara de este móvil no responde", color = Peligro, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                Text(actual.motivo + ".", color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                falloCameraX?.let {
                    Text("Antes, CameraX: ${it.motivo}.", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                }
                Text(actual.detalle, color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(12.dp))
                Text(
                    "No pasa nada: lee el QR desde una captura de pantalla o escribe la clave a mano. Y si me mandas el informe de Audítame, lo miro.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(12.dp))
                BotonAmbar("Leer el QR de una imagen") { alElegirImagen() }
            }
        }
    }
}

@Composable
private fun Aviso(texto: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(12.dp)
            .clip(FormaPequena)
            .background(Obsidiana.copy(alpha = 0.72f))
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaPequena)
                else Modifier
            )
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(texto, color = TextoPrincipal, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
    }
}

@Composable
private fun VistaCameraX(alFrame: (ByteArray, Int, Int) -> Unit, alEstado: (EstadoCamara) -> Unit) {
    val contexto = LocalContext.current
    val dueno = LocalLifecycleOwner.current
    val frame = rememberUpdatedState(alFrame)
    val estado = rememberUpdatedState(alEstado)
    val motor = remember {
        MotorCameraX(
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
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            PreviewView(ctx).apply {
                // TextureView en vez de SurfaceView: con FLAG_SECURE puesto, SurfaceView sale
                // negro en muchos Xiaomi/MediaTek y además queda fuera de la protección de capturas.
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
                setBackgroundColor(Superficie.toArgb())
                motor.iniciar(this)
            }
        }
    )
}

@Composable
private fun VistaLegado(alFrame: (ByteArray, Int, Int) -> Unit, alEstado: (EstadoCamara) -> Unit) {
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
        modifier = Modifier.fillMaxSize(),
        factory = { motor.crearVista() }
    )
}
