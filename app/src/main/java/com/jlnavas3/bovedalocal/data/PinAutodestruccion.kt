package com.jlnavas3.bovedalocal.data

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.security.MessageDigest
import java.security.SecureRandom

@Serializable
data class DatosPinAutodestruccion(
    val activo: Boolean = false,
    val saltHex: String = "",
    val hashHex: String = ""
)

object PinAutodestruccion {

    private const val NOMBRE_ARCHIVO = "autodestruccion_v1.json"
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = false }
    private val aleatorio = SecureRandom()

    private fun archivo(contexto: Context): File = File(contexto.filesDir, NOMBRE_ARCHIVO)

    fun cargar(contexto: Context): DatosPinAutodestruccion {
        val f = archivo(contexto)
        if (!f.exists()) return DatosPinAutodestruccion()
        return try {
            json.decodeFromString(f.readText())
        } catch (_: Exception) {
            DatosPinAutodestruccion()
        }
    }

    fun guardar(contexto: Context, datos: DatosPinAutodestruccion) {
        val f = archivo(contexto)
        try {
            val contenido = json.encodeToString(datos)
            val tmp = File(contexto.filesDir, "$NOMBRE_ARCHIVO.tmp")
            tmp.writeText(contenido)
            tmp.renameTo(f)
        } catch (_: Exception) {
        }
    }

    fun sha256(texto: String, salt: ByteArray): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(salt)
        return md.digest(texto.toByteArray(Charsets.UTF_8))
    }

    fun verificarPin(candidato: String, saltHex: String, hashHex: String): Boolean {
        if (candidato.isBlank() || saltHex.isBlank() || hashHex.isBlank()) return false
        val salt = try {
            saltHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        } catch (_: Exception) {
            return false
        }
        val hashCandidato = sha256(candidato, salt)
        val hashEsperado = try {
            hashHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        } catch (_: Exception) {
            return false
        }
        return MessageDigest.isEqual(hashCandidato, hashEsperado)
    }

    fun configurar(contexto: Context, pin: String) {
        require(pin.isNotBlank()) { "El PIN de autodestrucción no puede estar vacío" }
        require(pin.length >= 4) { "El PIN de autodestrucción debe tener al menos 4 caracteres" }
        val salt = ByteArray(16).also { aleatorio.nextBytes(it) }
        val hash = sha256(pin, salt)

        val datos = DatosPinAutodestruccion(
            activo = true,
            saltHex = salt.joinToString("") { "%02x".format(it) },
            hashHex = hash.joinToString("") { "%02x".format(it) }
        )
        guardar(contexto, datos)
    }

    fun desactivar(contexto: Context) {
        val actual = cargar(contexto)
        guardar(contexto, actual.copy(activo = false))
    }

    fun esPinAutodestruccion(contexto: Context, candidato: String): Boolean {
        if (candidato.isBlank()) return false
        val datos = cargar(contexto)
        if (!datos.activo) return false
        return verificarPin(candidato, datos.saltHex, datos.hashHex)
    }
}
