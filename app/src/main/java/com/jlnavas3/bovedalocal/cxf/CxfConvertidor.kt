package com.jlnavas3.bovedalocal.cxf

import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import java.util.UUID

/**
 * Conversor y analizador de documentos FIDO CXF (Credential Exchange Format).
 *
 * Transforma el JSON estructurado recibido de las Credential Transfer APIs de Android
 * (o exportado por otros gestores compatibles con FIDO CXF) a modelos nativos de Bóveda Local.
 */
object CxfConvertidor {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    /**
     * Procesa un JSON en formato FIDO CXF y devuelve las entradas convertidas junto con sus estadísticas.
     */
    fun convertir(jsonString: String): ResultadoConversionCxf {
        val doc = try {
            json.decodeFromString<DocumentoCxf>(jsonString)
        } catch (e: Exception) {
            com.jlnavas3.bovedalocal.util.Diagnostico.apuntar("cxf", "Fallo al decodificar DocumentoCxf: ${e.message}", e)
            // Intentamos parsear como lista de ítems o credenciales si la raíz no es un documento
            return parsearEstructuraAlternativa(jsonString)
        }

        val exportador = doc.exporterDisplayName
            ?: doc.exporter?.displayName
            ?: doc.exporter?.name
            ?: doc.exporterRpId
            ?: doc.exporter?.rpId

        // Recolectar todos los ítems de cuentas y de raíz evitando duplicados
        val listaItems = mutableListOf<ItemCxf>()
        val idsVistos = mutableSetOf<String>()

        fun agregarItem(item: ItemCxf) {
            val id = item.id
            if (id.isNullOrBlank() || idsVistos.add(id)) {
                listaItems.add(item)
            }
        }

        doc.accounts?.forEach { cuenta ->
            cuenta.items?.forEach { agregarItem(it) }
            if (cuenta.credentials != null && cuenta.credentials.isNotEmpty()) {
                agregarItem(
                    ItemCxf(
                        id = cuenta.id,
                        title = cuenta.name ?: cuenta.fullName ?: cuenta.username ?: cuenta.userName.extraerTexto(),
                        userName = cuenta.userName ?: cuenta.username?.let { JsonPrimitive(it) },
                        credentials = cuenta.credentials
                    )
                )
            }
        }
        doc.items?.forEach { agregarItem(it) }
        doc.entries?.forEach { agregarItem(it) }

        // Si hay credenciales sueltas en la raíz
        if (!doc.credentials.isNullOrEmpty()) {
            listaItems.add(
                ItemCxf(
                    id = "root_credentials",
                    title = "Credenciales importadas",
                    credentials = doc.credentials
                )
            )
        }

        var countPasskeys = 0
        var countContrasenas = 0
        var countTotp = 0
        val resultadoEntradas = mutableListOf<Entrada>()

        val ahora = System.currentTimeMillis()

        for (item in listaItems) {
            val credenciales = item.credentials ?: emptyList()
            if (credenciales.isEmpty()) continue

            // Agrupar credenciales del ítem
            val passkeys = credenciales.filter { esPasskey(it.type) }
            val basicAuths = credenciales.filter { esPassword(it.type) }
            val totps = credenciales.filter { esTotp(it.type) }
            val notas = credenciales.filter { esNota(it.type) }

            val urlsItem = extraerUrls(item)
            val tituloItem = (item.title ?: item.name ?: item.serviceName ?: item.scope?.urls?.firstOrNull() ?: item.scope?.androidApps?.firstOrNull()?.name ?: "").trim()
            val usuarioItem = (item.userName.extraerTexto() ?: item.username.extraerTexto() ?: "").trim()
            val notaItem = (item.notes.extraerTexto() ?: item.note.extraerTexto() ?: "").trim()

            val creada = item.creationAt?.let { if (it < 10_000_000_000L) it * 1000L else it } ?: ahora
            val modificada = item.modifiedAt?.let { if (it < 10_000_000_000L) it * 1000L else it } ?: creada
            val favorita = item.favorite ?: false

            // Caso 1: Ítem con 1 contraseña y opcionalmente 1 passkey y/o 1 totp para la misma cuenta
            if (basicAuths.size <= 1 && passkeys.size <= 1 && totps.size <= 1 && (basicAuths.isNotEmpty() || passkeys.isNotEmpty() || totps.isNotEmpty())) {
                val passkey = passkeys.firstOrNull()?.let { parsearPasskey(it, item, urlsItem) }
                val passkeyCred = passkeys.firstOrNull()
                val passwordCred = basicAuths.firstOrNull()
                val totpCred = totps.firstOrNull()

                val usuarioFinal = listOfNotNull(
                    passkeyCred?.username.extraerTexto()?.takeIf { it.isNotBlank() },
                    passkeyCred?.userDisplayName?.takeIf { it.isNotBlank() },
                    passkeyCred?.userName.extraerTexto()?.takeIf { it.isNotBlank() },
                    passwordCred?.username.extraerTexto()?.takeIf { it.isNotBlank() },
                    passwordCred?.userName.extraerTexto()?.takeIf { it.isNotBlank() },
                    passwordCred?.user.extraerTexto()?.takeIf { it.isNotBlank() },
                    totpCred?.accountName?.takeIf { it.isNotBlank() },
                    usuarioItem.takeIf { it.isNotBlank() }
                ).firstOrNull() ?: ""

                val passkeyValida = passkey?.takeIf { it.credId.isNotBlank() && it.clavePrivada.isNotBlank() }
                if (passkeyValida != null) countPasskeys++

                val pass = passwordCred?.password.extraerTexto() ?: passwordCred?.passwordValue.extraerTexto() ?: ""
                if (pass.isNotBlank() || passwordCred != null) countContrasenas++

                val secretoTotp = totpCred?.secret.extraerTexto()?.trim()?.filterNot { it.isWhitespace() }
                if (!secretoTotp.isNullOrBlank()) countTotp++

                val tituloFinal = when {
                    tituloItem.isNotBlank() && !tituloItem.startsWith("http://", ignoreCase = true) && !tituloItem.startsWith("https://", ignoreCase = true) -> tituloItem
                    passkeyValida != null && passkeyValida.rpName.isNotBlank() -> passkeyValida.rpName
                    tituloItem.isNotBlank() -> extraerHost(tituloItem).ifBlank { tituloItem }
                    urlsItem.isNotEmpty() -> extraerHost(urlsItem.first()).ifBlank { urlsItem.first() }
                    else -> "Entrada importada"
                }

                val tipo = when {
                    passkeyValida != null && pass.isBlank() -> TipoEntrada.PASSKEY
                    else -> TipoEntrada.LOGIN
                }

                val notasFinal = listOfNotNull(
                    notaItem.takeIf { it.isNotBlank() },
                    passwordCred?.notes.extraerTexto()?.takeIf { it.isNotBlank() },
                    passkeyCred?.notes.extraerTexto()?.takeIf { it.isNotBlank() }
                ).joinToString("\n\n")

                val urlsFinales = if (urlsItem.isNotEmpty()) {
                    urlsItem
                } else if (passkeyValida != null && passkeyValida.rpId.isNotBlank()) {
                    listOf(passkeyValida.rpId)
                } else emptyList()

                resultadoEntradas.add(
                    Entrada(
                        id = UUID.randomUUID().toString(),
                        tipo = tipo,
                        titulo = tituloFinal,
                        usuario = usuarioFinal,
                        contrasena = pass,
                        urls = urlsFinales,
                        notas = notasFinal,
                        secretoTotp = secretoTotp,
                        totpEmisor = totpCred?.issuer ?: tituloFinal,
                        totpDigitos = totpCred?.digits ?: 6,
                        totpPeriodo = totpCred?.period ?: 30,
                        totpAlgoritmo = normalizarAlgoritmoTotp(totpCred?.algorithm),
                        favorito = favorita,
                        passkey = passkeyValida,
                        creadaEn = creada,
                        modificadaEn = modificada
                    )
                )
            } else {
                // Caso 2: Múltiples credenciales en un mismo ítem (desglosar de manera individual)
                for (passCred in passkeys) {
                    val passkey = parsearPasskey(passCred, item, urlsItem)
                    if (passkey != null && passkey.credId.isNotBlank() && passkey.clavePrivada.isNotBlank()) {
                        countPasskeys++
                        val usr = passkey.usuario.ifBlank { usuarioItem }
                        val tit = passkey.rpName.ifBlank { tituloItem.ifBlank { passkey.rpId } }
                        resultadoEntradas.add(
                            Entrada(
                                id = UUID.randomUUID().toString(),
                                tipo = TipoEntrada.PASSKEY,
                                titulo = tit,
                                usuario = usr,
                                contrasena = "",
                                urls = if (urlsItem.isNotEmpty()) urlsItem else listOf(passkey.rpId),
                                notas = passCred.notes.extraerTexto() ?: notaItem,
                                favorito = favorita,
                                passkey = passkey,
                                creadaEn = creada,
                                modificadaEn = modificada
                            )
                        )
                    }
                }

                for (passCred in basicAuths) {
                    val pass = passCred.password.extraerTexto() ?: passCred.passwordValue.extraerTexto() ?: ""
                    val usr = passCred.username.extraerTexto() ?: passCred.userName.extraerTexto() ?: passCred.user.extraerTexto() ?: usuarioItem
                    val tit = tituloItem.ifBlank { urlsItem.firstOrNull() ?: "Contraseña importada" }
                    countContrasenas++
                    resultadoEntradas.add(
                        Entrada(
                            id = UUID.randomUUID().toString(),
                            tipo = TipoEntrada.LOGIN,
                            titulo = tit,
                            usuario = usr,
                            contrasena = pass,
                            urls = urlsItem,
                            notas = passCred.notes.extraerTexto() ?: notaItem,
                            favorito = favorita,
                            creadaEn = creada,
                            modificadaEn = modificada
                        )
                    )
                }

                for (totpCred in totps) {
                    val sec = totpCred.secret.extraerTexto()?.trim()?.filterNot { it.isWhitespace() }
                    if (!sec.isNullOrBlank()) {
                        countTotp++
                        val usr = totpCred.accountName ?: usuarioItem
                        val emisor = totpCred.issuer ?: tituloItem.ifBlank { "2FA" }
                        resultadoEntradas.add(
                            Entrada(
                                id = UUID.randomUUID().toString(),
                                tipo = TipoEntrada.LOGIN,
                                titulo = emisor,
                                usuario = usr,
                                contrasena = "",
                                urls = urlsItem,
                                notas = notaItem,
                                secretoTotp = sec,
                                totpEmisor = emisor,
                                totpDigitos = totpCred.digits ?: 6,
                                totpPeriodo = totpCred.period ?: 30,
                                totpAlgoritmo = normalizarAlgoritmoTotp(totpCred.algorithm),
                                favorito = favorita,
                                creadaEn = creada,
                                modificadaEn = modificada
                            )
                        )
                    }
                }
            }

            for (notaCred in notas) {
                val tit = notaCred.title ?: tituloItem.ifBlank { "Nota segura" }
                val texto = notaCred.notes.extraerTexto() ?: notaCred.user.extraerTexto() ?: notaItem
                resultadoEntradas.add(
                    Entrada(
                        id = UUID.randomUUID().toString(),
                        tipo = TipoEntrada.NOTA,
                        titulo = tit,
                        usuario = "",
                        contrasena = "",
                        notas = texto,
                        favorito = favorita,
                        creadaEn = creada,
                        modificadaEn = modificada
                    )
                )
            }
        }

        return ResultadoConversionCxf(
            entradas = resultadoEntradas,
            totalPasskeys = countPasskeys,
            totalContrasenas = countContrasenas,
            totalTotp = countTotp,
            exportador = exportador
        )
    }

    private fun parsearPasskey(cred: CredencialCxf, item: ItemCxf, urlsItem: List<String>): DatosPasskey? {
        val credIdRaw = cred.credentialId ?: cred.credId ?: return null
        val credIdB64Url = if (!credIdRaw.contains('+') && !credIdRaw.contains('/') && !credIdRaw.contains('=')) {
            credIdRaw.trim()
        } else {
            try {
                val bytes = UtilBase64Cxf.decodificar(credIdRaw)
                UtilBase64Cxf.aBase64Url(bytes)
            } catch (_: Exception) {
                credIdRaw.trim()
            }
        }

        val clavePrivadaB64Url = extraerClavePrivadaB64Url(cred) ?: return null

        val rpId = cred.rpId?.takeIf { it.isNotBlank() }
            ?: urlsItem.firstOrNull()?.let { extraerHost(it) }
            ?: item.domain
            ?: ""

        val rpName = cred.rpName?.takeIf { it.isNotBlank() }
            ?: item.title?.takeIf { it.isNotBlank() }
            ?: item.name?.takeIf { it.isNotBlank() }
            ?: rpId

        val userHandleB64Url = cred.userHandle?.let { uh ->
            if (!uh.contains('+') && !uh.contains('/') && !uh.contains('=')) {
                uh.trim()
            } else {
                try {
                    val bytes = UtilBase64Cxf.decodificar(uh)
                    UtilBase64Cxf.aBase64Url(bytes)
                } catch (_: Exception) {
                    uh.trim()
                }
            }
        } ?: ""

        val usuario = cred.username.extraerTexto()
            ?: cred.userDisplayName?.takeIf { it.isNotBlank() }
            ?: cred.userName.extraerTexto()
            ?: item.userName.extraerTexto()
            ?: item.username.extraerTexto()
            ?: ""

        val algoritmo = when (val a = cred.alg?.toString()?.replace("\"", "")?.trim()) {
            "-7", "ES256" -> "ES256"
            "-257", "RS256" -> "RS256"
            "-8", "EdDSA" -> "EdDSA"
            else -> "ES256"
        }

        return DatosPasskey(
            rpId = rpId,
            rpName = rpName,
            userHandle = userHandleB64Url,
            credId = credIdB64Url,
            clavePrivada = clavePrivadaB64Url,
            algoritmo = algoritmo,
            usuario = usuario
        )
    }

    private fun extraerClavePrivadaB64Url(cred: CredencialCxf): String? {
        // Opción 1: clave privada en formato string directo (PKCS#8 o base64, ej. Google usa "key")
        val textoClave = cred.key ?: cred.privateKeyPkcs8 ?: cred.privateKey
        if (!textoClave.isNullOrBlank()) {
            return try {
                val bytes = UtilBase64Cxf.decodificar(textoClave)
                val pkcs8Bytes = asegurarPkcs8(bytes)
                UtilBase64Cxf.aBase64Url(pkcs8Bytes)
            } catch (_: Exception) {
                textoClave.trim()
            }
        }

        // Opción 2: clave privada como objeto JWK
        val jwk = cred.privateKeyJwk ?: cred.jwkPrivateKey
        if (jwk is JsonObject) {
            val dStr = jwk["d"]?.jsonPrimitive?.contentOrNull
            if (!dStr.isNullOrBlank()) {
                return try {
                    val dBytes = UtilBase64Cxf.decodificar(dStr)
                    val pkcs8Bytes = asegurarPkcs8(dBytes)
                    UtilBase64Cxf.aBase64Url(pkcs8Bytes)
                } catch (_: Exception) {
                    null
                }
            }
        }

        return null
    }


    private fun extraerUrls(item: ItemCxf): List<String> {
        val lista = mutableListOf<String>()
        item.urls?.let { lista.addAll(it) }
        item.url?.takeIf { it.isNotBlank() }?.let { lista.add(it) }
        item.domains?.let { lista.addAll(it) }
        item.domain?.takeIf { it.isNotBlank() }?.let { lista.add(it) }
        item.scope?.urls?.let { lista.addAll(it) }
        item.scope?.androidApps?.forEach { app ->
            app.name?.takeIf { it.isNotBlank() }?.let { lista.add(it) }
            app.bundleId?.takeIf { it.isNotBlank() }?.let { lista.add("android://$it") }
        }
        return lista.filter { it.isNotBlank() }.distinct()
    }

    private fun extraerHost(url: String): String {
        return url.removePrefix("https://")
            .removePrefix("http://")
            .removePrefix("android://")
            .removePrefix("androidapp://")
            .removePrefix("app://")
            .substringBefore('/')
            .substringBefore(':')
            .trim()
    }

    private fun esPasskey(tipo: String): Boolean {
        val t = tipo.lowercase().trim()
        return t == "passkey" || t == "public-key" || t == "public-key-credential" || t == "fido2"
    }

    private fun esPassword(tipo: String): Boolean {
        val t = tipo.lowercase().trim()
        return t == "basic-auth" || t == "password" || t == "login" || t == "credentials"
    }

    private fun esTotp(tipo: String): Boolean {
        val t = tipo.lowercase().trim()
        return t == "totp" || t == "otp" || t == "authenticator" || t == "2fa"
    }

    private fun esNota(tipo: String): Boolean {
        val t = tipo.lowercase().trim()
        return t == "note" || t == "secure-note"
    }

    private fun normalizarAlgoritmoTotp(alg: String?): String {
        return when (alg?.uppercase()?.trim()) {
            "SHA256", "HMACSHA256" -> "HmacSHA256"
            "SHA512", "HMACSHA512" -> "HmacSHA512"
            else -> "HmacSHA1"
        }
    }

    private fun parsearEstructuraAlternativa(jsonString: String): ResultadoConversionCxf {
        // Si el JSON viene como un array de items o credenciales
        return try {
            val lista = json.decodeFromString<List<ItemCxf>>(jsonString)
            convertir(
                json.encodeToString(
                    DocumentoCxf.serializer(),
                    DocumentoCxf(items = lista)
                )
            )
        } catch (_: Exception) {
            ResultadoConversionCxf(
                entradas = emptyList(),
                totalPasskeys = 0,
                totalContrasenas = 0,
                totalTotp = 0
            )
        }
    }
}
