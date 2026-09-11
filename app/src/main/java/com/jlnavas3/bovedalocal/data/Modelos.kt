package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable

fun normalizarEtiqueta(valor: String): String = valor
    .trim()
    .removePrefix("#")
    .filterNot { it.isWhitespace() }

@Serializable
enum class TipoEntrada {
    LOGIN, PASSKEY, NOTA;

    val etiqueta: String
        get() = when (this) {
            LOGIN -> "Contraseña"
            PASSKEY -> "Passkey"
            NOTA -> "Nota segura"
        }
}

@Serializable
data class DatosPasskey(
    val rpId: String,
    val rpName: String,
    val userHandle: String,
    val credId: String,
    val clavePrivada: String,
    val algoritmo: String = "ES256",
    val usuario: String = ""
)

/** Una contraseña anterior de una entrada, con la fecha en la que dejó de usarse. */
@Serializable
data class CambioContrasena(val contrasena: String, val cambiadaEn: Long)

@Serializable
enum class TipoCampo {
    TEXTO, OCULTO, PIN;

    val etiqueta: String
        get() = when (this) {
            TEXTO -> "Texto"
            OCULTO -> "Oculto"
            PIN -> "PIN"
        }
}

@Serializable
data class CampoPersonalizado(
    val id: String = java.util.UUID.randomUUID().toString(),
    val etiqueta: String = "",
    val valor: String = "",
    val tipo: TipoCampo = TipoCampo.TEXTO
)

@Serializable
data class Entrada(
    val id: String,
    val tipo: TipoEntrada = TipoEntrada.LOGIN,
    val titulo: String = "",
    val usuario: String = "",
    val contrasena: String = "",
    val urls: List<String> = emptyList(),
    val notas: String = "",
    val secretoTotp: String? = null,
    val totpEmisor: String = "",
    val totpDigitos: Int = 6,
    val totpPeriodo: Int = 30,
    val totpAlgoritmo: String = "HmacSHA1",
    val favorito: Boolean = false,
    val creadaEn: Long = 0L,
    val modificadaEn: Long = 0L,
    /** 0 si está activa; si no, cuándo se mandó a la papelera. */
    val eliminadaEn: Long = 0L,
    /** Etiquetas libres que pone el usuario (ej. "Trabajo", "Familia"). */
    val etiquetas: List<String> = emptyList(),
    /** Contraseñas anteriores de esta entrada, la más reciente primero. */
    val historialContrasenas: List<CambioContrasena> = emptyList(),
    val passkey: DatosPasskey? = null,
    val camposPersonalizados: List<CampoPersonalizado> = emptyList()
)

@Serializable
data class ContenidoBoveda(
    val version: Int = 1,
    val entradas: List<Entrada> = emptyList(),
    /** Entradas borradas pero aún recuperables; se vacían solas pasado un tiempo. */
    val papelera: List<Entrada> = emptyList()
)

sealed interface EstadoBoveda {
    object SinCrear : EstadoBoveda
    object Bloqueada : EstadoBoveda
    data class Desbloqueada(val entradas: List<Entrada>, val papelera: List<Entrada> = emptyList()) : EstadoBoveda
}
