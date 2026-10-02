package com.jlnavas3.bovedalocal.cxf

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


/**
 * Modelos de datos para el formato FIDO CXF (Credential Exchange Format).
 *
 * La especificación permite transportar credenciales (llaves de paso, contraseñas,
 * códigos de verificación en dos pasos y notas) de manera estructurada e interoperable.
 * Los modelos son lenientes para admitir tanto variantes jerárquicas
 * (`accounts` -> `items` -> `credentials`) como estructuras planas (`items`, `entries` o `credentials`).
 */
@Serializable
data class DocumentoCxf(
    val version: JsonElement? = null,
    val exporter: ExporterCxf? = null,
    val exporterRpId: String? = null,
    val exporterDisplayName: String? = null,
    val timestamp: Long? = null,
    val accounts: List<CuentaCxf>? = null,
    val items: List<ItemCxf>? = null,
    val credentials: List<CredencialCxf>? = null,
    val entries: List<ItemCxf>? = null
)

@Serializable
data class ExporterCxf(
    val rpId: String? = null,
    val name: String? = null,
    val displayName: String? = null
)

@Serializable
data class ColeccionCxf(
    val id: String = "boveda_local_col",
    val title: String = "Bóveda Local",
    val items: List<String> = emptyList()
)

@Serializable
data class CuentaCxf(
    val id: String? = null,
    val username: String? = null,
    val email: String? = null,
    val fullName: String? = null,
    val name: String? = null,
    val collections: List<ColeccionCxf>? = null,
    val items: List<ItemCxf>? = null,
    val credentials: List<CredencialCxf>? = null,
    val userName: JsonElement? = null
)

@Serializable
data class ItemCxf(
    val id: String? = null,
    val title: String? = null,
    val subtitle: String? = null,
    val scope: ScopeCxf? = null,
    val creationAt: Long? = null,
    val modifiedAt: Long? = null,
    val credentials: List<CredencialCxf>? = null,
    val tags: List<String>? = null,
    // Compatibilidad leniente para importación de otros gestores
    val name: String? = null,
    val serviceName: String? = null,
    val urls: List<String>? = null,
    val url: String? = null,
    val domains: List<String>? = null,
    val domain: String? = null,
    val userName: JsonElement? = null,
    val username: JsonElement? = null,
    val notes: JsonElement? = null,
    val note: JsonElement? = null,
    val favorite: Boolean? = null
)

@Serializable
data class ScopeCxf(
    val urls: List<String>? = null,
    val androidApps: List<AndroidAppCxf>? = null
)

@Serializable
data class AndroidAppCxf(
    val bundleId: String? = null,
    val name: String? = null
)

@Serializable
data class CredencialCxf(
    val type: String = "",
    // Identificación y autenticación básica / contraseña
    val id: String? = null,
    val username: JsonElement? = null,
    val userName: JsonElement? = null,
    val user: JsonElement? = null,
    val userDisplayName: String? = null,
    val password: JsonElement? = null,
    val passwordValue: JsonElement? = null,
    val notes: JsonElement? = null,
    val content: JsonElement? = null,

    // Llave de paso (Passkey / WebAuthn)
    val rpId: String? = null,
    val rpName: String? = null,
    val userHandle: String? = null,
    val credentialId: String? = null,
    val credId: String? = null,
    val key: String? = null,
    val privateKey: String? = null,
    val privateKeyPkcs8: String? = null,
    val privateKeyJwk: JsonElement? = null,
    val jwkPrivateKey: JsonElement? = null,
    val alg: JsonElement? = null,
    val signCount: Long? = null,
    val transports: List<String>? = null,

    // Verificación en dos pasos (TOTP / OTP)
    val secret: JsonElement? = null,
    val algorithm: String? = null,
    val digits: Int? = null,
    val period: Int? = null,
    val issuer: String? = null,
    val accountName: String? = null,

    // Metadatos contextuales opcionales
    val title: String? = null,
    val urls: List<String>? = null,
    val url: String? = null
)
