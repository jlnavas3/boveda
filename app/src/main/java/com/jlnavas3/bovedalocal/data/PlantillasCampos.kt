package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

@Serializable
data class CampoPlantilla(
    val etiqueta: String,
    val tipo: TipoCampo = TipoCampo.TEXTO
)

@Serializable
data class PlantillaCampos(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val descripcion: String = "",
    val esPredeterminada: Boolean = false,
    val campos: List<CampoPlantilla>
) {
    fun aCamposPersonalizados(): List<CampoPersonalizado> = campos.map {
        CampoPersonalizado(
            id = UUID.randomUUID().toString(),
            etiqueta = it.etiqueta,
            valor = "",
            tipo = it.tipo
        )
    }
}

object GestorPlantillasCampos {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    val PREDETERMINADAS: List<PlantillaCampos> = listOf(
        PlantillaCampos(
            id = "pred_tarjeta",
            nombre = "Tarjeta Bancaria",
            descripcion = "Número, Titular, Vencimiento, CVV y PIN",
            esPredeterminada = true,
            campos = listOf(
                CampoPlantilla("Número de tarjeta", TipoCampo.TEXTO),
                CampoPlantilla("Titular de la tarjeta", TipoCampo.TEXTO),
                CampoPlantilla("Vencimiento (MM/AA)", TipoCampo.TEXTO),
                CampoPlantilla("CVV", TipoCampo.OCULTO),
                CampoPlantilla("PIN del cajero", TipoCampo.PIN)
            )
        ),
        PlantillaCampos(
            id = "pred_wifi",
            nombre = "Red Wi-Fi",
            descripcion = "Nombre de red (SSID), Contraseña y Seguridad",
            esPredeterminada = true,
            campos = listOf(
                CampoPlantilla("Nombre de red (SSID)", TipoCampo.TEXTO),
                CampoPlantilla("Contraseña Wi-Fi", TipoCampo.OCULTO),
                CampoPlantilla("Tipo de seguridad", TipoCampo.TEXTO)
            )
        ),
        PlantillaCampos(
            id = "pred_cuenta_bancaria",
            nombre = "Cuenta Bancaria",
            descripcion = "Banco, Tipo de cuenta, IBAN/Número y CBU/CLABE",
            esPredeterminada = true,
            campos = listOf(
                CampoPlantilla("Banco", TipoCampo.TEXTO),
                CampoPlantilla("Tipo de cuenta", TipoCampo.TEXTO),
                CampoPlantilla("Número de cuenta / IBAN", TipoCampo.TEXTO),
                CampoPlantilla("CBU / CLABE / SWIFT", TipoCampo.TEXTO),
                CampoPlantilla("Titular", TipoCampo.TEXTO)
            )
        ),
        PlantillaCampos(
            id = "pred_documento",
            nombre = "Documento de Identidad",
            descripcion = "Tipo, Número de documento y Fechas",
            esPredeterminada = true,
            campos = listOf(
                CampoPlantilla("Tipo de documento", TipoCampo.TEXTO),
                CampoPlantilla("Número de documento", TipoCampo.TEXTO),
                CampoPlantilla("Fecha de expedición", TipoCampo.TEXTO),
                CampoPlantilla("Fecha de caducidad", TipoCampo.TEXTO)
            )
        ),
        PlantillaCampos(
            id = "pred_servidor",
            nombre = "Servidor / SSH",
            descripcion = "Host/IP, Puerto, Usuario y Clave privada",
            esPredeterminada = true,
            campos = listOf(
                CampoPlantilla("Host o IP", TipoCampo.TEXTO),
                CampoPlantilla("Puerto", TipoCampo.TEXTO),
                CampoPlantilla("Usuario", TipoCampo.TEXTO),
                CampoPlantilla("Clave privada / Password", TipoCampo.OCULTO)
            )
        ),
        PlantillaCampos(
            id = "pred_cripto",
            nombre = "Cripto Wallet",
            descripcion = "Red, Dirección pública, Frase semilla y Clave",
            esPredeterminada = true,
            campos = listOf(
                CampoPlantilla("Red o Blockchain", TipoCampo.TEXTO),
                CampoPlantilla("Dirección pública", TipoCampo.TEXTO),
                CampoPlantilla("Frase semilla (Seed)", TipoCampo.OCULTO),
                CampoPlantilla("Clave privada", TipoCampo.OCULTO)
            )
        )
    )

    fun decodificarPersonalizadas(rawJson: String): List<PlantillaCampos> {
        if (rawJson.isBlank()) return emptyList()
        return try {
            json.decodeFromString<List<PlantillaCampos>>(rawJson)
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun codificarPersonalizadas(plantillas: List<PlantillaCampos>): String {
        return try {
            json.encodeToString(plantillas)
        } catch (_: Exception) {
            ""
        }
    }
}
