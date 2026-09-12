package com.jlnavas3.bovedalocal.data

import android.content.Context
import com.jlnavas3.bovedalocal.crypto.Zeroizar
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.security.MessageDigest
import java.security.SecureRandom

@Serializable
data class DatosBovedaSenuelo(
    val activo: Boolean = false,
    val saltHex: String = "",
    val hashHex: String = "",
    val entradas: List<Entrada> = emptyList()
)

object BovedaSenuelo {

    private const val NOMBRE_ARCHIVO = "senuelo_v1.json"
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = false }
    private val aleatorio = SecureRandom()

    private fun archivo(contexto: Context): File = File(contexto.filesDir, NOMBRE_ARCHIVO)

    fun cargar(contexto: Context): DatosBovedaSenuelo {
        val f = archivo(contexto)
        if (!f.exists()) return DatosBovedaSenuelo()
        return try {
            json.decodeFromString(f.readText())
        } catch (_: Exception) {
            DatosBovedaSenuelo()
        }
    }

    fun guardar(contexto: Context, datos: DatosBovedaSenuelo) {
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
        if (saltHex.isBlank() || hashHex.isBlank()) return false
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

    fun configurar(contexto: Context, pinCoaccion: String, poblarCuentasEjemplo: Boolean = true) {
        require(pinCoaccion.isNotBlank()) { "El PIN de coacción no puede estar vacío" }
        val salt = ByteArray(16).also { aleatorio.nextBytes(it) }
        val hash = sha256(pinCoaccion, salt)

        val entradasActuales = if (poblarCuentasEjemplo) cuentasEjemplo() else emptyList()
        val datos = DatosBovedaSenuelo(
            activo = true,
            saltHex = salt.joinToString("") { "%02x".format(it) },
            hashHex = hash.joinToString("") { "%02x".format(it) },
            entradas = entradasActuales
        )
        guardar(contexto, datos)
    }

    fun desactivar(contexto: Context) {
        val actual = cargar(contexto)
        guardar(contexto, actual.copy(activo = false))
    }

    fun esPinCoaccion(contexto: Context, candidato: String): Boolean {
        val datos = cargar(contexto)
        if (!datos.activo) return false
        return verificarPin(candidato, datos.saltHex, datos.hashHex)
    }

    fun guardarEntradasSenuelo(contexto: Context, entradas: List<Entrada>) {
        val actual = cargar(contexto)
        if (actual.activo) {
            guardar(contexto, actual.copy(entradas = entradas))
        }
    }

    fun cuentasEjemplo(): List<Entrada> {
        val ahora = System.currentTimeMillis()
        return listOf(
            Entrada(
                id = "senuelo-1",
                titulo = "Wi-Fi Casa",
                usuario = "Red_Fibra_Plus",
                contrasena = "FibraOptica2024!",
                urls = listOf("192.168.1.1"),
                notas = "Clave del router del salón",
                creadaEn = ahora - 86400000L * 15,
                modificadaEn = ahora - 86400000L * 15
            ),
            Entrada(
                id = "senuelo-2",
                titulo = "Correo Secundario",
                usuario = "contacto.alternativo@gmail.com",
                contrasena = "VeranoAzul_88",
                urls = listOf("https://gmail.com"),
                notas = "Cuenta para suscripciones y tiendas",
                creadaEn = ahora - 86400000L * 30,
                modificadaEn = ahora - 86400000L * 30
            ),
            Entrada(
                id = "senuelo-3",
                titulo = "Spotify Familiar",
                usuario = "musica_libre@outlook.com",
                contrasena = "PlaylistRock99#",
                urls = listOf("https://spotify.com"),
                creadaEn = ahora - 86400000L * 5,
                modificadaEn = ahora - 86400000L * 5
            )
        )
    }
}
