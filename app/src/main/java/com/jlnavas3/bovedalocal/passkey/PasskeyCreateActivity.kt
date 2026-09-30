package com.jlnavas3.bovedalocal.passkey

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.credentials.CreatePublicKeyCredentialRequest
import androidx.credentials.CreatePublicKeyCredentialResponse
import androidx.credentials.exceptions.CreateCredentialUnknownException
import androidx.credentials.provider.PendingIntentHandler
import androidx.credentials.provider.ProviderCreateCredentialRequest
import androidx.fragment.app.FragmentActivity
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.autofill.AutofillUtiles
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

/** Confirma y crea una passkey nueva pedida por una web o app. */
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class PasskeyCreateActivity : FragmentActivity() {

    private lateinit var repositorio: VaultRepository
    private var peticion: ProviderCreateCredentialRequest? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repositorio = VaultRepository.obtener(this)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        peticion = PendingIntentHandler.retrieveProviderCreateCredentialRequest(intent)
        val solicitud = peticion?.callingRequest as? CreatePublicKeyCredentialRequest
        if (solicitud == null) {
            fallar("Bóveda local solo crea passkeys")
            return
        }
        val datos = try {
            WebAuthn.leerCreacion(solicitud.requestJson)
        } catch (e: Exception) {
            fallar("Petición de passkey ilegible")
            return
        }
        if (datos.rpId.isBlank() || datos.reto.isBlank()) {
            fallar("La petición no trae ni sitio ni reto")
            return
        }

        val info = peticion?.callingAppInfo
        val paquete = info?.packageName
            ?: LanzadorEnlaces.extraerPaquete(datos.rpId)
            ?: if (LanzadorEnlaces.estaInstalada(this, datos.rpId)) datos.rpId else null

        val esApp = paquete != null && LanzadorEnlaces.estaInstalada(this, paquete)
        val nombreApp = if (esApp && paquete != null) LanzadorEnlaces.obtenerNombreApp(this, paquete) else null
        val iconoBitmap = if (esApp && paquete != null) AutofillUtiles.obtenerBitmapIconoCircular(this, paquete, 120) else null

        val sitioMostrar = nombreApp ?: datos.rpName.takeIf { it.isNotBlank() && it != datos.rpId } ?: datos.rpId

        setContent {
            BovedaTheme {
                HojaPasskey(
                    actividad = this,
                    repositorio = repositorio,
                    titulo = "Crear passkey",
                    sitio = sitioMostrar,
                    detalle = "$sitioMostrar quiere crear una passkey para " +
                        datos.usuario.ifBlank { "tu cuenta" } + ".",
                    textoAccion = "Crear la passkey",
                    iconoBitmap = iconoBitmap,
                    alConfirmar = { crear(datos, paquete, nombreApp) },
                    alCancelar = { cancelar() }
                )
            }
        }
    }

    private fun crear(datos: WebAuthn.PeticionCreacion, paquete: String?, nombreApp: String?) {
        try {
            val par = WebAuthn.generarPar()
            val credId = WebAuthn.nuevoCredId()
            if (!WebAuthn.credIdValido(credId)) {
                fallar("El autenticador generó un identificador de passkey inválido")
                return
            }
            val info = peticion?.callingAppInfo
            // Si quien pide es un navegador, el origen que hay que firmar es el de la
            // web, no el de la app. Viene en callingAppInfo.origin y solo lo rellena
            // el sistema para clientes privilegiados.
            val origen = if (datos.rpId.contains('.')) {
                Origen.deWeb(datos.rpId)
            } else if (info != null) {
                Origen.deApp(info.packageName, info.signingInfo)
            } else {
                Origen.deWeb(datos.rpId)
            }
            val clientData = WebAuthn.clientDataJson("webauthn.create", datos.reto, origen)
            val authData = WebAuthn.authenticatorDataRegistro(datos.rpId, credId, par.x, par.y)
            val attestation = WebAuthn.attestationObject(authData)
            val json = WebAuthn.respuestaRegistro(credId, clientData, attestation, authData, par.x, par.y)

            val passkey = DatosPasskey(
                rpId = datos.rpId,
                rpName = datos.rpName.ifBlank { datos.rpId },
                userHandle = datos.userHandle,
                credId = WebAuthn.aB64Url(credId),
                clavePrivada = WebAuthn.aB64Url(par.privadaPkcs8),
                usuario = datos.usuario
            )

            val existente = repositorio.entradas().firstOrNull { e ->
                (e.usuario.equals(datos.usuario, ignoreCase = true) || datos.usuario.isBlank()) &&
                e.urls.any { u -> Dominios.coincide(u, datos.rpId) || u.contains(datos.rpId, ignoreCase = true) }
            }

            val tituloFinal = when {
                !nombreApp.isNullOrBlank() -> nombreApp
                datos.rpName.isNotBlank() && datos.rpName != datos.rpId -> datos.rpName
                paquete != null && !paquete.contains('/') -> Dominios.dominioDePaquete(paquete)
                else -> datos.rpId
            }
            val urlGuardada = if (paquete != null && !datos.rpId.startsWith("android://") && datos.rpId == paquete) {
                "android://$paquete"
            } else datos.rpId

            val entrada = existente?.let { exist ->
                val domPaquete = if (paquete != null) Dominios.dominioDePaquete(paquete) else ""
                val debeActualizarTitulo = exist.titulo.isBlank() ||
                    exist.titulo == "Nueva entrada" ||
                    (domPaquete.isNotBlank() && exist.titulo.equals(domPaquete, ignoreCase = true)) ||
                    (paquete != null && exist.titulo.equals(paquete, ignoreCase = true)) ||
                    exist.titulo.equals(datos.rpId, ignoreCase = true)

                val urlsActualizadas = if (urlGuardada.startsWith("android://") && !exist.urls.any { LanzadorEnlaces.extraerPaquete(it) == paquete }) {
                    exist.urls + urlGuardada
                } else exist.urls

                exist.copy(
                    passkey = passkey,
                    titulo = if (debeActualizarTitulo && !nombreApp.isNullOrBlank()) nombreApp else exist.titulo,
                    urls = urlsActualizadas,
                    modificadaEn = System.currentTimeMillis()
                )
            } ?: Entrada(
                id = repositorio.nuevoId(),
                tipo = TipoEntrada.PASSKEY,
                titulo = tituloFinal,
                usuario = datos.usuario,
                urls = listOf(urlGuardada),
                passkey = passkey
            )
            repositorio.guardarEntrada(entrada)
            Diagnostico.apuntar("passkey", "Passkey guardada o vinculada correctamente en la bóveda")

            val respuesta = Intent()
            PendingIntentHandler.setCreateCredentialResponse(
                respuesta,
                CreatePublicKeyCredentialResponse(json)
            )
            setResult(Activity.RESULT_OK, respuesta)
            finish()
        } catch (e: Exception) {
            fallar("No se pudo crear la passkey: ${e.javaClass.simpleName}: ${e.message}")
        }
    }

    private fun fallar(mensaje: String) {
        Diagnostico.apuntar("passkey", "Fallo al crear passkey: $mensaje")
        val respuesta = Intent()
        PendingIntentHandler.setCreateCredentialException(
            respuesta,
            CreateCredentialUnknownException(mensaje)
        )
        setResult(Activity.RESULT_OK, respuesta)
        finish()
    }

    private fun cancelar() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}
