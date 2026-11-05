/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
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
    val anclado: Boolean = false,
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
    /** Llaves de acceso (Passkeys) secundarias conservadas de unificaciones o respaldos adicionales. */
    val passkeysSecundarias: List<DatosPasskey> = emptyList(),
    /** Códigos de verificación en dos pasos (TOTP) secundarios o alternativos. */
    val totpsSecundarios: List<TotpSecundario> = emptyList(),
    val camposPersonalizados: List<CampoPersonalizado> = emptyList(),
    /** Submodelos nativos fuertemente tipados para tipos de entrada especializados */
    val datosTarjeta: DatosTarjetaBancaria? = null,
    val datosWifi: DatosRedWifi? = null,
    val datosCuentaBancaria: DatosCuentaBancaria? = null,
    val datosIdentidad: DatosDocumentoIdentidad? = null,
    val datosServidor: DatosServidorSsh? = null,
    val datosWallet: DatosCriptoWallet? = null,
    val datosContacto: DatosContacto? = null,
    /** Si es true, esta entrada se ignora en el cálculo y avisos de auditoría de salud. */
    val ignoradaEnSalud: Boolean = false,
    /** Lista de IDs de categorías a las que pertenece esta entrada (mantiene clave 'colecciones' en JSON para retrocompatibilidad). */
    @SerialName("colecciones")
    val categorias: List<String> = emptyList(),
    /** ID del perfil vinculado explícitamente (si es null, se usa vinculación inteligente por correo). */
    @SerialName("identidadId")
    val perfilId: String? = null,
    /** Fecha límite de expiración o caducidad en milisegundos Unix, o null si no caduca. */
    val fechaCaducidad: Long? = null,
    /** Archivos adjuntos cifrados localmente dentro del contenedor de la entrada. */
    val adjuntos: List<AdjuntoBoveda> = emptyList(),
    /** Indica si el usuario editó o creó el título de esta entrada de forma manual. */
    val tituloPersonalizado: Boolean = false,
    /** Contador monotónico de revisiones para resolución determinista de sincronización P2P. */
    val revision: Long = 1L,
    /** Hash SHA-256 (64 hex chars) del contenido en el momento de la última sincronización mutua. */
    @SerialName("lastSyncHash")
    val lastSyncHash: String? = null
) {
    /** Alias de compatibilidad hacia atrás durante la migración */
    val colecciones: List<String> get() = categorias
    val identidadId: String? get() = perfilId

    /** Determina si la entrada tiene un secreto TOTP configurado y válido (primario o secundario). */
    val tieneTotp: Boolean get() = !secretoTotp.isNullOrBlank() || totpsSecundarios.isNotEmpty()

    /** Accesos directos tipados que resuelven nativamente o desde entradas heredadas */
    val tarjetaEfectiva: DatosTarjetaBancaria? get() = datosTarjeta ?: if (tipo == TipoEntrada.TARJETA) MigradorEntradasNativas.extraerTarjeta(this) else null
    val wifiEfectivo: DatosRedWifi? get() = datosWifi ?: if (tipo == TipoEntrada.WIFI) MigradorEntradasNativas.extraerWifi(this) else null
    val cuentaBancariaEfectiva: DatosCuentaBancaria? get() = datosCuentaBancaria ?: if (tipo == TipoEntrada.CUENTA_BANCARIA) MigradorEntradasNativas.extraerCuentaBancaria(this) else null
    val identidadEfectiva: DatosDocumentoIdentidad? get() = datosIdentidad ?: if (tipo == TipoEntrada.IDENTIDAD) MigradorEntradasNativas.extraerIdentidad(this) else null
    val servidorEfectivo: DatosServidorSsh? get() = datosServidor ?: if (tipo == TipoEntrada.SERVIDOR) MigradorEntradasNativas.extraerServidor(this) else null
    val walletEfectiva: DatosCriptoWallet? get() = datosWallet ?: if (tipo == TipoEntrada.WALLET) MigradorEntradasNativas.extraerWallet(this) else null
    val contactoEfectivo: DatosContacto? get() = datosContacto ?: if (tipo == TipoEntrada.CONTACTO) MigradorEntradasNativas.extraerContacto(this) else null

    /** Campos personalizados purgados de campos base nativos residuales */
    val camposPersonalizadosEfectivos: List<CampoPersonalizado>
        get() = MigradorEntradasNativas.depurarCamposPersonalizados(tipo, camposPersonalizados)
}
