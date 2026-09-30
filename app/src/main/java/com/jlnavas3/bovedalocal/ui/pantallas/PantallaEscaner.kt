package com.jlnavas3.bovedalocal.ui.pantallas

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.BovedaApp
import com.jlnavas3.bovedalocal.camara.LectorImagenes
import com.jlnavas3.bovedalocal.camara.LectorQr
import com.jlnavas3.bovedalocal.camara.MotorCamara
import com.jlnavas3.bovedalocal.camara.PermisoCamara
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.pantallas.escaner.EstadoPermiso
import com.jlnavas3.bovedalocal.ui.pantallas.escaner.SeccionEntradaManual
import com.jlnavas3.bovedalocal.ui.pantallas.escaner.TarjetaAvisoPasskey
import com.jlnavas3.bovedalocal.ui.pantallas.escaner.TarjetaPermisoCamara
import com.jlnavas3.bovedalocal.ui.pantallas.escaner.ZonaCamara
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.GoogleAuthMigration
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    var manual by remember { mutableStateOf("") }
    var fallo by remember { mutableStateOf(false) }
    var qrPasskey by remember { mutableStateOf(false) }
    var avisoImagen by remember { mutableStateOf<String?>(null) }
    var leyendoImagen by remember { mutableStateOf(false) }

    /** Devuelve true si el texto valía y el 2FA se guardó. Deja [qrPasskey] al día en todos los casos. */
    fun procesarTexto(texto: String, origen: String): Boolean {
        if (GoogleAuthMigration.esEnlaceMigracion(texto)) {
            Diagnostico.apuntar("2fa", "Escaneo detectó un paquete de migración de Google Authenticator")
            vm.ir(Pantalla.ConfirmarMigracion(texto))
            return true
        }
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

    val elegirImagen = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        BovedaApp.salidaTerminada(contexto)
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
        BovedaApp.salidaPendiente(contexto)
        try {
            elegirImagen.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } catch (e: Exception) {
            BovedaApp.salidaTerminada(contexto)
            Diagnostico.apuntar("camara", "No se pudo abrir el selector de imágenes", e)
            avisoImagen = "Este móvil no tiene ningún selector de imágenes que pueda abrir."
        }
    }

    ContenedorPrincipal(
        titulo = "Añadir Doble Factor",
        subtitulo = "Escaneo seguro offline de QR o introducción manual",
        alVolver = { vm.volverAtras() },
        idEtiqueta = "04-HER-2FA-ADD",
        mostrarId = ajustes.mostrarIdsAjustes,
        conScroll = true,
        espaciado = 16.dp
    ) {
        if (!soloManual) {
            BotonAmbar(
                texto = "Escanear código QR",
                icono = Icons.Filled.QrCodeScanner
            ) {
                vm.ir(Pantalla.CamaraQr(entradaDestino))
            }
            Spacer(Modifier.height(10.dp))
            BotonBorde(
                texto = if (leyendoImagen) "Buscando el QR en la imagen…" else "Leer el QR de una imagen",
                icono = Icons.Filled.Image
            ) {
                if (!leyendoImagen) abrirSelectorDeImagen()
            }
        }

        avisoImagen?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Peligro, style = MaterialTheme.typography.bodyMedium)
        }

        if (qrPasskey) {
            Spacer(Modifier.height(18.dp))
            TarjetaAvisoPasskey()
        }

        Spacer(Modifier.height(18.dp))
        SeccionEntradaManual(
            manual = manual,
            alCambiarManual = {
                manual = it
                fallo = false
            },
            fallo = fallo,
            soloManual = soloManual,
            alAgregarCodigo = {
                val texto = manual.trim()
                if (!procesarTexto(texto, "el teclado") && !qrPasskey) fallo = true
            }
        )

        Spacer(Modifier.height(32.dp))
    }
}
