package com.pepotech.pepoboveda.passkey

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
import com.pepotech.pepoboveda.data.Entrada
import com.pepotech.pepoboveda.data.TipoEntrada
import com.pepotech.pepoboveda.data.VaultRepository
import com.pepotech.pepoboveda.ui.theme.PepoBovedaTheme
import com.pepotech.pepoboveda.util.Dominios

/** Confirma y guarda una contraseña pedida por Android Credential Manager. */
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class PasswordCreateActivity : FragmentActivity() {

    private lateinit var repositorio: VaultRepository
    private var peticion: ProviderCreateCredentialRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repositorio = VaultRepository.obtener(this)
        if (!repositorio.ajustes.actual.modoGrabacion) {
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        }

        peticion = PendingIntentHandler.retrieveProviderCreateCredentialRequest(intent)
        val solicitud = peticion?.callingRequest as? CreatePasswordRequest
        if (solicitud == null) {
            fallar("Bóveda local solo guarda contraseñas y passkeys")
            return
        }
        if (solicitud.id.isBlank() || solicitud.password.isBlank()) {
            fallar("La solicitud no trae usuario o contraseña")
            return
        }

        val objetivo = objetivoSolicitante(peticion?.callingAppInfo)
        setContent {
            PepoBovedaTheme {
                HojaPasskey(
                    actividad = this,
                    repositorio = repositorio,
                    titulo = "Guardar contraseña",
                    sitio = objetivo,
                    detalle = "$objetivo quiere guardar una contraseña para ${solicitud.id}.",
                    textoAccion = "Guardar contraseña",
                    textoPie = "La contraseña queda cifrada en esta bóveda local.",
                    alConfirmar = { guardar(solicitud, objetivo) },
                    alCancelar = { cancelar() }
                )
            }
        }
    }

    private fun guardar(solicitud: CreatePasswordRequest, objetivo: String) {
        try {
            val existente = repositorio.entradas().firstOrNull { entrada ->
                entrada.usuario == solicitud.id && entrada.urls.any { Dominios.coincide(it, objetivo) }
            }
            val titulo = if (objetivo.contains('.')) Dominios.raiz(objetivo) else Dominios.dominioDePaquete(objetivo)
            val entrada = existente?.copy(contrasena = solicitud.password) ?: Entrada(
                id = repositorio.nuevoId(),
                tipo = TipoEntrada.LOGIN,
                titulo = titulo.ifBlank { "Nueva contraseña" },
                usuario = solicitud.id,
                contrasena = solicitud.password,
                urls = listOf(objetivo)
            )
            repositorio.guardarEntrada(entrada)

            val respuesta = Intent()
            PendingIntentHandler.setCreateCredentialResponse(respuesta, CreatePasswordResponse())
            setResult(Activity.RESULT_OK, respuesta)
            finish()
        } catch (e: Exception) {
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