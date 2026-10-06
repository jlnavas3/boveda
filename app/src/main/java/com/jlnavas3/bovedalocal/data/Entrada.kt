package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    /** Marca de tiempo (milisegundos) del último uso registrado (autofill, passkey, copia o apertura). */
    val ultimoUsoEn: Long = 0L,
    /** 0 si está activa; si no, cuándo se mandó a la papelera. */
    val eliminadaEn: Long = 0L,
    /** Etiquetas libres que pone el usuario (ej. "Trabajo", "Familia"). */
    val etiquetas: List<String> = emptyList(),
    /** Contraseñas anteriores de esta entrada, la más reciente primero. */
    val historialContrasenas: List<CambioContrasena> = emptyList(),
    val passkey: DatosPasskey? = null,
    val camposPersonalizados: List<CampoPersonalizado> = emptyList(),
    /** Si es true, esta entrada se ignora en el cálculo y avisos de auditoría de salud. */
    val ignoradaEnSalud: Boolean = false,
    /** Lista de IDs de categorías a las que pertenece esta entrada (mantiene clave 'colecciones' en JSON para retrocompatibilidad). */
    @SerialName("colecciones")
    val categorias: List<String> = emptyList(),
    /** ID de la identidad vinculada explícitamente (si es null, se usa vinculación inteligente por correo). */
    val identidadId: String? = null
) {
    /** Alias de compatibilidad hacia atrás durante la migración */
    val colecciones: List<String> get() = categorias
}
