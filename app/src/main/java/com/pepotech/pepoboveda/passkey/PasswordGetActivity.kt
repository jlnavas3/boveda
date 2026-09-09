package com.pepotech.pepoboveda.passkey

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.GetCredentialUnknownException
import androidx.credentials.provider.PendingIntentHandler
import androidx.fragment.app.FragmentActivity
import com.pepotech.pepoboveda.data.Entrada
import com.pepotech.pepoboveda.data.TipoEntrada
import com.pepotech.pepoboveda.data.VaultRepository
import com.pepotech.pepoboveda.ui.theme.PepoBovedaTheme
import com.pepotech.pepoboveda.util.Dominios

/** Confirma y devuelve una contraseña a Android Credential Manager. */
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class PasswordGetActivity : FragmentActivity() {

    private lateinit var repositorio: VaultRepository
    private var entradaId: String? = null
    private var objetivo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repositorio = VaultRepository.obtener(this)
        if (!repositorio.ajustes.actual.modoGrabacion) {
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        }

        PendingIntentHandler.retrieveProviderGetCredentialRequest(intent)
        entradaId = intent.getStringExtra(PepoCredentialProviderService.EXTRA_ENTRADA_ID)
        objetivo = intent.getStringExtra(PepoCredentialProviderService.EXTRA_OBJETIVO).orEmpty()

        setContent {
            PepoBovedaTheme {
                HojaPasskey(
                    actividad = this,
                    repositorio = repositorio,
                    titulo = "Usar contraseña",
                    sitio = objetivo.ifBlank { "esta app" },
                    detalle = "Bóveda local entregará la contraseña seleccionada a ${objetivo.ifBlank { "esta app" }}.",
                    textoAccion = "Usar contraseña",
                    textoPie = "La contraseña se entrega a Android solo para esta solicitud.",
                    alConfirmar = { responder() },
                    alCancelar = { cancelar() }
                )
            }
        }
    }

    private fun elegir(): Entrada? {
        val directa = entradaId?.let { repositorio.entrada(it) }
        if (directa != null && directa.tipo == TipoEntrada.LOGIN && directa.urls.any { Dominios.coincide(it, objetivo) }) {
            return directa
        }
        return repositorio.entradas().firstOrNull { entrada ->
            entrada.tipo == TipoEntrada.LOGIN &&
                entrada.contrasena.isNotBlank() &&
                entrada.urls.any { Dominios.coincide(it, objetivo) }
        }
    }

    private fun responder() {
        try {
            val entrada = elegir()
            if (entrada == null) {
                fallar("No hay ninguna contraseña guardada para $objetivo")
                return
            }
            val respuesta = Intent()
            PendingIntentHandler.setGetCredentialResponse(
                respuesta,
                GetCredentialResponse(PasswordCredential(entrada.usuario, entrada.contrasena))
            )
            setResult(Activity.RESULT_OK, respuesta)
            finish()
        } catch (e: Exception) {
            fallar("No se pudo devolver la contraseña")
        }
    }

    private fun fallar(mensaje: String) {
        val respuesta = Intent()
        PendingIntentHandler.setGetCredentialException(respuesta, GetCredentialUnknownException(mensaje))
        setResult(Activity.RESULT_OK, respuesta)
        finish()
    }

    private fun cancelar() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}