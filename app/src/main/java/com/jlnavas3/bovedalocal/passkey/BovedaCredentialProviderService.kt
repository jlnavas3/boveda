package com.jlnavas3.bovedalocal.passkey

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.CancellationSignal
import android.os.OutcomeReceiver
import androidx.annotation.RequiresApi
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.CreateCredentialUnknownException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialUnknownException
import androidx.credentials.provider.BeginCreateCredentialRequest
import androidx.credentials.provider.BeginCreateCredentialResponse
import androidx.credentials.provider.BeginCreatePasswordCredentialRequest
import androidx.credentials.provider.BeginCreatePublicKeyCredentialRequest
import androidx.credentials.provider.BeginGetCredentialRequest
import androidx.credentials.provider.BeginGetCredentialResponse
import androidx.credentials.provider.BeginGetPasswordOption
import androidx.credentials.provider.BeginGetPublicKeyCredentialOption
import androidx.credentials.provider.CreateEntry
import androidx.credentials.provider.CredentialProviderService
import androidx.credentials.provider.PasswordCredentialEntry
import androidx.credentials.provider.ProviderClearCredentialStateRequest
import androidx.credentials.provider.PublicKeyCredentialEntry
import android.graphics.drawable.Icon
import com.jlnavas3.bovedalocal.autofill.AutofillUtiles
import com.jlnavas3.bovedalocal.data.VaultRepository
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.Diagnostico
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces
import java.time.Instant

/**
 * Provee passkeys al sistema. Solo publica entradas que coinciden con el rpId
 * pedido, y todo lo delicado ocurre en las actividades con confirmación.
 */
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class BovedaCredentialProviderService : CredentialProviderService() {

    companion object {
        const val EXTRA_ENTRADA_ID = "boveda.passkey.entrada"
        const val EXTRA_OPCION_ID = "boveda.passkey.opcion"
        const val EXTRA_OBJETIVO = "boveda.credencial.objetivo"
        private const val PETICION_CREAR = 2001
        private const val PETICION_OBTENER = 2002
        private const val PETICION_CREAR_PASSWORD = 2101
        private const val PETICION_OBTENER_PASSWORD = 2102
    }

    override fun onBeginCreateCredentialRequest(
        request: BeginCreateCredentialRequest,
        cancellationSignal: CancellationSignal,
        callback: OutcomeReceiver<BeginCreateCredentialResponse, CreateCredentialException>
    ) {
        if (request is BeginCreatePasswordCredentialRequest) {
            Diagnostico.apuntar("credential", "Android solicitó guardar una contraseña")
            val intent = Intent(this, PasswordCreateActivity::class.java)
            val pendiente = PendingIntent.getActivity(
                this,
                PETICION_CREAR_PASSWORD,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            callback.onResult(
                BeginCreateCredentialResponse.Builder()
                    .addCreateEntry(CreateEntry("Bóveda local", pendiente))
                    .build()
            )
            return
        }
        if (request !is BeginCreatePublicKeyCredentialRequest) {
            callback.onError(CreateCredentialUnknownException("Bóveda local solo guarda contraseñas y passkeys"))
            return
        }
        val peticion = try {
            WebAuthn.leerCreacion(request.requestJson)
        } catch (e: Exception) {
            callback.onError(CreateCredentialUnknownException("Petición de passkey ilegible"))
            return
        }
        if (!peticion.algoritmosSoportados) {
            callback.onError(CreateCredentialUnknownException("Bóveda local solo firma con ES256"))
            return
        }
        val intent = Intent(this, PasskeyCreateActivity::class.java)
        val pendiente = PendingIntent.getActivity(
            this,
            PETICION_CREAR,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        val etiqueta = peticion.usuario.ifBlank { peticion.rpId.ifBlank { "Bóveda local" } }
        val respuesta = BeginCreateCredentialResponse.Builder()
            .addCreateEntry(CreateEntry(etiqueta, pendiente))
            .build()
        callback.onResult(respuesta)
    }

    override fun onBeginGetCredentialRequest(
        request: BeginGetCredentialRequest,
        cancellationSignal: CancellationSignal,
        callback: OutcomeReceiver<BeginGetCredentialResponse, GetCredentialException>
    ) {
        val repositorio = VaultRepository.obtener(this)
        val constructor = BeginGetCredentialResponse.Builder()
        var alguna = false

        request.beginGetCredentialOptions.forEach { opcion ->
            if (opcion is BeginGetPasswordOption) {
                val objetivo = objetivoSolicitante(request.callingAppInfo)
                if (!repositorio.estaDesbloqueada) {
                    constructor.addCredentialEntry(
                        PasswordCredentialEntry.Builder(
                            this,
                            "Desbloquear Bóveda local",
                            pendienteObtenerPassword(null, objetivo),
                            opcion
                        )
                            .setDisplayName(objetivo.ifBlank { "Bóveda local" })
                            .build()
                    )
                    alguna = true
                    return@forEach
                }
                repositorio.entradas().filter { entrada ->
                    (entrada.tipo == TipoEntrada.LOGIN || entrada.tipo == TipoEntrada.PASSKEY) &&
                        entrada.contrasena.isNotBlank() &&
                        entrada.urls.any { Dominios.coincide(it, objetivo) } &&
                        (opcion.allowedUserIds.isEmpty() || opcion.allowedUserIds.contains(entrada.usuario))
                }.forEach { entrada ->
                    val paquete = entrada.urls.firstNotNullOfOrNull { LanzadorEnlaces.extraerPaquete(it) }
                        ?: LanzadorEnlaces.extraerPaquete(objetivo)
                        ?: if (LanzadorEnlaces.estaInstalada(this, objetivo)) objetivo else null

                    val nombreApp = if (paquete != null && LanzadorEnlaces.estaInstalada(this, paquete)) {
                        LanzadorEnlaces.obtenerNombreApp(this, paquete)
                    } else null

                    val iconoBitmap = if (paquete != null && LanzadorEnlaces.estaInstalada(this, paquete)) {
                        AutofillUtiles.obtenerBitmapIconoCircular(this, paquete, 96)
                    } else null

                    val domPaquete = if (paquete != null) Dominios.dominioDePaquete(paquete) else null
                    val tituloAlmacenado = entrada.titulo.trim()

                    val tituloMostrar = when {
                        !nombreApp.isNullOrBlank() && (tituloAlmacenado.isBlank() ||
                            tituloAlmacenado == "Nueva entrada" ||
                            tituloAlmacenado.equals(domPaquete, ignoreCase = true) ||
                            tituloAlmacenado.equals(paquete, ignoreCase = true)) -> nombreApp
                        tituloAlmacenado.isNotBlank() -> tituloAlmacenado
                        !nombreApp.isNullOrBlank() -> nombreApp
                        else -> objetivo.ifBlank { "Bóveda local" }
                    }

                    val builder = PasswordCredentialEntry.Builder(
                        this,
                        entrada.usuario.ifBlank { tituloMostrar },
                        pendienteObtenerPassword(entrada.id, objetivo),
                        opcion
                    )
                        .setDisplayName(tituloMostrar)
                        .setLastUsedTime(Instant.ofEpochMilli(entrada.ultimoUsoEn.takeIf { it > 0 } ?: (entrada.modificadaEn.takeIf { it > 0 } ?: entrada.creadaEn)))

                    if (iconoBitmap != null) {
                        builder.setIcon(Icon.createWithBitmap(iconoBitmap))
                    }

                    constructor.addCredentialEntry(builder.build())
                    alguna = true
                }
                return@forEach
            }
            if (opcion !is BeginGetPublicKeyCredentialOption) return@forEach
            val peticion = try {
                WebAuthn.leerAsercion(opcion.requestJson)
            } catch (e: Exception) {
                return@forEach
            }
            if (!repositorio.estaDesbloqueada) {
                // Con la bóveda cerrada no sabemos qué hay dentro: ofrecemos una
                // entrada genérica que abrirá el desbloqueo.
                val pendiente = pendienteObtener(null, opcion.id)
                constructor.addCredentialEntry(
                    PublicKeyCredentialEntry(
                        context = this,
                        username = "Desbloquear Bóveda local",
                        pendingIntent = pendiente,
                        beginGetPublicKeyCredentialOption = opcion
                    )
                )
                alguna = true
                return@forEach
            }
            val candidatas = repositorio.passkeysDe(peticion.rpId).filter { entrada ->
                val datos = entrada.passkey ?: return@filter false
                peticion.credencialesPermitidas.isEmpty() ||
                    peticion.credencialesPermitidas.contains(datos.credId)
            }
            candidatas.forEach { entrada ->
                val datos = entrada.passkey ?: return@forEach

                val paquete = entrada.urls.firstNotNullOfOrNull { LanzadorEnlaces.extraerPaquete(it) }
                    ?: LanzadorEnlaces.extraerPaquete(peticion.rpId)
                    ?: if (LanzadorEnlaces.estaInstalada(this, peticion.rpId)) peticion.rpId else null

                val nombreApp = if (paquete != null && LanzadorEnlaces.estaInstalada(this, paquete)) {
                    LanzadorEnlaces.obtenerNombreApp(this, paquete)
                } else null

                val iconoBitmap = if (paquete != null && LanzadorEnlaces.estaInstalada(this, paquete)) {
                    AutofillUtiles.obtenerBitmapIconoCircular(this, paquete, 96)
                } else null

                val domPaquete = if (paquete != null) Dominios.dominioDePaquete(paquete) else null
                val tituloAlmacenado = entrada.titulo.trim()

                val tituloMostrar = when {
                    !nombreApp.isNullOrBlank() && (tituloAlmacenado.isBlank() ||
                        tituloAlmacenado == "Nueva entrada" ||
                        tituloAlmacenado.equals(domPaquete, ignoreCase = true) ||
                        tituloAlmacenado.equals(paquete, ignoreCase = true) ||
                        tituloAlmacenado.equals(datos.rpId, ignoreCase = true)) -> nombreApp
                    datos.rpName.isNotBlank() && datos.rpName != datos.rpId -> datos.rpName
                    tituloAlmacenado.isNotBlank() -> tituloAlmacenado
                    else -> datos.rpId
                }

                val pubKeyBuilder = PublicKeyCredentialEntry.Builder(
                    this,
                    datos.usuario.ifBlank { tituloMostrar },
                    pendienteObtener(entrada.id, opcion.id),
                    opcion
                )
                    .setDisplayName(tituloMostrar)
                    .setLastUsedTime(Instant.ofEpochMilli(entrada.ultimoUsoEn.takeIf { it > 0 } ?: (entrada.modificadaEn.takeIf { it > 0 } ?: entrada.creadaEn)))

                if (iconoBitmap != null) {
                    pubKeyBuilder.setIcon(Icon.createWithBitmap(iconoBitmap))
                }

                constructor.addCredentialEntry(pubKeyBuilder.build())
                alguna = true
            }
        }

        if (!alguna) {
            callback.onError(GetCredentialUnknownException("Bóveda local no tiene passkeys para este sitio"))
            return
        }
        callback.onResult(constructor.build())
    }

    private fun pendienteObtener(entradaId: String?, opcionId: String): PendingIntent {
        val intent = Intent(this, PasskeyGetActivity::class.java).apply {
            putExtra(EXTRA_ENTRADA_ID, entradaId)
            putExtra(EXTRA_OPCION_ID, opcionId)
        }
        return PendingIntent.getActivity(
            this,
            PETICION_OBTENER + (entradaId?.hashCode() ?: 0),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    private fun pendienteObtenerPassword(entradaId: String?, objetivo: String): PendingIntent {
        val intent = Intent(this, PasswordGetActivity::class.java).apply {
            putExtra(EXTRA_ENTRADA_ID, entradaId)
            putExtra(EXTRA_OBJETIVO, objetivo)
        }
        return PendingIntent.getActivity(
            this,
            PETICION_OBTENER_PASSWORD + (entradaId ?: objetivo).hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    override fun onClearCredentialStateRequest(
        request: ProviderClearCredentialStateRequest,
        cancellationSignal: CancellationSignal,
        callback: OutcomeReceiver<Void?, ClearCredentialException>
    ) {
        callback.onResult(null)
    }
}
