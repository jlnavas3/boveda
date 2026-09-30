package com.jlnavas3.bovedalocal.passkey

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CreatePasswordResponse
import androidx.credentials.exceptions.CreateCredentialUnknownException
import androidx.credentials.provider.PendingIntentHandler
import androidx.credentials.provider.ProviderCreateCredentialRequest
import androidx.fragment.app.FragmentActivity
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.autofill.AutofillUtiles
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

/** Confirma y guarda una contraseña pedida por Android Credential Manager. */
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class PasswordCreateActivity : FragmentActivity() {

    private lateinit var repositorio: VaultRepository
    private var peticion: ProviderCreateCredentialRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repositorio = VaultRepository.obtener(this)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        peticion = PendingIntentHandler.retrieveProviderCreateCredentialRequest(intent)
        Diagnostico.apuntar("credential", "Confirmación de guardado abierta")
        val solicitud = peticion?.callingRequest as? CreatePasswordRequest
        if (solicitud == null) {
            Diagnostico.apuntar("credential", "Guardado rechazado: petición sin contraseña")
            fallar("Bóveda local solo guarda contraseñas y passkeys")
            return
        }
        if (solicitud.password.isBlank()) {
            Diagnostico.apuntar("credential", "Guardado rechazado: petición sin contraseña")
            fallar("La solicitud no trae una contraseña")
            return
        }

        val info = peticion?.callingAppInfo
        val paquete = info?.packageName
        val objetivo = objetivoSolicitante(info)
        val esApp = paquete != null && LanzadorEnlaces.estaInstalada(this, paquete)
        val nombreApp = if (esApp && paquete != null) LanzadorEnlaces.obtenerNombreApp(this, paquete) else null
        val iconoBitmap = if (esApp && paquete != null) AutofillUtiles.obtenerBitmapIconoCircular(this, paquete, 120) else null
        val sitioMostrar = nombreApp ?: objetivo

        setContent {
            BovedaTheme {
                HojaPasskey(
                    actividad = this,
                    repositorio = repositorio,
                    titulo = "Guardar contraseña",
                    sitio = sitioMostrar,
                    detalle = "$sitioMostrar quiere guardar una contraseña para ${solicitud.id}.",
                    textoAccion = "Guardar contraseña",
                    textoPie = "La contraseña queda cifrada en esta bóveda local.",
                    iconoBitmap = iconoBitmap,
                    alConfirmar = { guardar(solicitud, objetivo, paquete, nombreApp) },
                    alCancelar = { cancelar() }
                )
            }
        }
    }

    private fun guardar(solicitud: CreatePasswordRequest, objetivo: String, paquete: String?, nombreApp: String?) {
        try {
            val existente = repositorio.entradas().firstOrNull { entrada ->
                entrada.usuario == solicitud.id && entrada.urls.any { Dominios.coincide(it, objetivo) }
            }
            val urlGuardada = if (paquete != null && (objetivo == paquete || !objetivo.contains('/'))) {
                "android://$paquete"
            } else objetivo

            val titulo = when {
                !nombreApp.isNullOrBlank() -> nombreApp
                paquete != null && !paquete.contains('/') -> Dominios.dominioDePaquete(paquete)
                objetivo.contains('.') -> Dominios.raiz(objetivo)
                else -> objetivo
            }

            val entrada = existente?.let { exist ->
                val domPaquete = if (paquete != null) Dominios.dominioDePaquete(paquete) else ""
                val debeActualizarTitulo = exist.titulo.isBlank() ||
                    exist.titulo == "Nueva contraseña" ||
                    exist.titulo == "Nueva entrada" ||
                    (domPaquete.isNotBlank() && exist.titulo.equals(domPaquete, ignoreCase = true)) ||
                    (paquete != null && exist.titulo.equals(paquete, ignoreCase = true))

                val urlsActualizadas = if (urlGuardada.startsWith("android://") && !exist.urls.any { LanzadorEnlaces.extraerPaquete(it) == paquete }) {
                    exist.urls + urlGuardada
                } else exist.urls

                exist.copy(
                    contrasena = solicitud.password,
                    titulo = if (debeActualizarTitulo && !nombreApp.isNullOrBlank()) nombreApp else exist.titulo,
                    urls = urlsActualizadas
                )
            } ?: Entrada(
                id = repositorio.nuevoId(),
                tipo = TipoEntrada.LOGIN,
                titulo = titulo.ifBlank { "Nueva contraseña" },
                usuario = solicitud.id,
                contrasena = solicitud.password,
                urls = listOf(urlGuardada)
            )
            repositorio.guardarEntrada(entrada)

            val respuesta = Intent()
            PendingIntentHandler.setCreateCredentialResponse(respuesta, CreatePasswordResponse())
            Diagnostico.apuntar("credential", "Contraseña guardada correctamente")
            setResult(Activity.RESULT_OK, respuesta)
            finish()
        } catch (e: Exception) {
            Diagnostico.apuntar("credential", "Guardado falló: ${e.javaClass.simpleName}")
            fallar("No se pudo guardar la contraseña")
        }
    }

    private fun fallar(mensaje: String) {
        val respuesta = Intent()
        PendingIntentHandler.setCreateCredentialException(respuesta, CreateCredentialUnknownException(mensaje))
        setResult(Activity.RESULT_OK, respuesta)
        finish()
    }

    private fun cancelar() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}