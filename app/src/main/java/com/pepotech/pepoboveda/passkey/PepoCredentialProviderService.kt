package com.pepotech.pepoboveda.passkey

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
import com.pepotech.pepoboveda.data.VaultRepository
import com.pepotech.pepoboveda.data.TipoEntrada
import com.pepotech.pepoboveda.util.Dominios
import java.time.Instant

/**
 * Provee passkeys al sistema. Solo publica entradas que coinciden con el rpId
 * pedido, y todo lo delicado ocurre en las actividades con confirmación.
 */
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class PepoCredentialProviderService : CredentialProviderService() {

    companion object {
        const val EXTRA_ENTRADA_ID = "pepo.passkey.entrada"
        const val EXTRA_OPCION_ID = "pepo.passkey.opcion"
        const val EXTRA_OBJETIVO = "pepo.credencial.objetivo"
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
                    entrada.tipo == TipoEntrada.LOGIN &&
                        entrada.contrasena.isNotBlank() &&
                        entrada.urls.any { Dominios.coincide(it, objetivo) } &&
                        (opcion.allowedUserIds.isEmpty() || opcion.allowedUserIds.contains(entrada.usuario))
                }.forEach { entrada ->
                    constructor.addCredentialEntry(
                        PasswordCredentialEntry.Builder(
                            this,
                            entrada.usuario.ifBlank { entrada.titulo.ifBlank { objetivo } },
                            pendienteObtenerPassword(entrada.id, objetivo),
                            opcion
                        )
                            .setDisplayName(entrada.titulo.ifBlank { objetivo })
                            .setLastUsedTime(Instant.ofEpochMilli(entrada.modificadaEn.takeIf { it > 0 } ?: entrada.creadaEn))
                            .build()
                    )
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
                        username = "Desbloquear Pepo Bóveda",
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
                constructor.addCredentialEntry(
                    PublicKeyCredentialEntry(
                        context = this,
                        username = datos.usuario.ifBlank { entrada.titulo.ifBlank { datos.rpId } },
                        pendingIntent = pendienteObtener(entrada.id, opcion.id),
                        beginGetPublicKeyCredentialOption = opcion
                    )
                )
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
