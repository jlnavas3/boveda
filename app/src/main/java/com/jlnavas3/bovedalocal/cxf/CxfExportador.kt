package com.jlnavas3.bovedalocal.cxf

import android.util.Log
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Serializador que convierte colecciones de credenciales de Bóveda Local a documentos
 * compatibles con la especificación FIDO CXF (Credential Exchange Format).
 */
object CxfExportador {

    private const val TAG = "BovedaCXF"

    private val formatoJson = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = false
    }

    /**
     * Convierte una lista de entradas de Bóveda a una cadena JSON FIDO CXF lista para transferir.
     */
    fun exportarAJson(entradas: List<Entrada>): String {
        val documento = exportarADocumento(entradas)
        val json = formatoJson.encodeToString(documento)
        Log.i(TAG, "CxfExportador: generados ${documento.accounts?.firstOrNull()?.items?.size ?: 0} ítems CXF (${json.length} bytes)")
        return json
    }

    /**
     * Genera la estructura en memoria de [DocumentoCxf] a partir de las entradas de Bóveda.
     */
    fun exportarADocumento(entradas: List<Entrada>): DocumentoCxf {
        val ahora = System.currentTimeMillis()
        val items = entradas.mapNotNull { entrada ->
            convertirEntradaAItem(entrada)
        }

        val cuenta = CuentaCxf(
            id = "boveda_local_principal",
            username = "boveda_local",
            email = "soporte@bovedalocal.app",
            fullName = "Bóveda Local",
            name = "Bóveda Local",
            collections = emptyList(),
            items = items
        )

        val versionObj = buildJsonObject {
            put("major", 1)
            put("minor", 0)
        }

        return DocumentoCxf(
            version = versionObj,
            exporterRpId = "com.jlnavas3.bovedalocal",
            exporterDisplayName = "Bóveda Local",
            timestamp = ahora / 1000L,
            accounts = listOf(cuenta)
        )
    }

    private fun convertirEntradaAItem(entrada: Entrada): ItemCxf? {
        val credenciales = mutableListOf<CredencialCxf>()
        val urlsNormalizadas = entrada.urls
            .map { CxfNormalizadorUrl.normalizar(it) }
            .filter { it.isNotBlank() }
            .distinct()

        val tituloFinal = entrada.titulo.ifBlank {
            entrada.usuario.ifBlank {
                urlsNormalizadas.firstOrNull() ?: "Credencial"
            }
        }

        // 1. Llave de paso (Passkey / WebAuthn)
        if (entrada.passkey != null) {
            val pk = entrada.passkey
            val usr = pk.usuario.ifBlank { entrada.usuario.ifBlank { tituloFinal } }
            val rpIdFinal = pk.rpId.ifBlank {
                urlsNormalizadas.firstOrNull()?.removePrefix("https://")?.removePrefix("http://")?.substringBefore('/') ?: "bovedalocal.app"
            }
            credenciales.add(
                CredencialCxf(
                    type = "passkey",
                    credentialId = pk.credId,
                    rpId = rpIdFinal,
                    username = JsonPrimitive(usr),
                    userDisplayName = usr,
                    userHandle = pk.userHandle,
                    key = pk.clavePrivada
                )
            )
        }

        // 2. Contraseña / autenticación básica
        if (entrada.contrasena.isNotBlank() || (entrada.usuario.isNotBlank() && entrada.passkey == null)) {
            val usernameObj = buildJsonObject {
                put("id", "${entrada.id}-usr")
                put("fieldType", "string")
                put("value", entrada.usuario)
            }
            val passwordObj = buildJsonObject {
                put("id", "${entrada.id}-pwd")
                put("fieldType", "concealed-string")
                put("value", entrada.contrasena)
            }
            credenciales.add(
                CredencialCxf(
                    type = "basic-auth",
                    id = "${entrada.id}-pwd",
                    username = usernameObj,
                    password = passwordObj
                )
            )
        }

        // 3. Verificación en dos pasos (TOTP / OTP)
        if (!entrada.secretoTotp.isNullOrBlank()) {
            val algoritmoNormalizado = when (entrada.totpAlgoritmo.uppercase()) {
                "HMACSHA256", "SHA256", "SHA_256" -> "sha256"
                "HMACSHA512", "SHA512", "SHA_512" -> "sha512"
                else -> "sha1"
            }
            credenciales.add(
                CredencialCxf(
                    type = "totp",
                    secret = JsonPrimitive(entrada.secretoTotp),
                    algorithm = algoritmoNormalizado,
                    digits = entrada.totpDigitos,
                    period = entrada.totpPeriodo,
                    issuer = entrada.totpEmisor.ifBlank { tituloFinal },
                    accountName = entrada.usuario.ifBlank { null },
                    username = entrada.usuario.takeIf { it.isNotBlank() }?.let { JsonPrimitive(it) }
                )
            )
        }

        // 4. Nota segura
        if (entrada.tipo == TipoEntrada.NOTA && entrada.notas.isNotBlank()) {
            val contentObj = buildJsonObject {
                put("id", "${entrada.id}-note")
                put("fieldType", "concealed-string")
                put("value", entrada.notas)
            }
            credenciales.add(
                CredencialCxf(
                    type = "note",
                    content = contentObj
                )
            )
        }

        if (credenciales.isEmpty() && entrada.notas.isBlank()) {
            return null
        }

        val scope = if (urlsNormalizadas.isNotEmpty()) {
            ScopeCxf(
                urls = urlsNormalizadas,
                androidApps = emptyList()
            )
        } else null

        val ahoraSegundos = System.currentTimeMillis() / 1000L

        return ItemCxf(
            id = entrada.id,
            title = tituloFinal,
            subtitle = entrada.usuario.takeIf { it.isNotBlank() },
            scope = scope,
            creationAt = if (entrada.creadaEn > 0L) entrada.creadaEn / 1000L else ahoraSegundos,
            modifiedAt = if (entrada.modificadaEn > 0L) entrada.modificadaEn / 1000L else ahoraSegundos,
            credentials = credenciales
        )
    }
}
