package com.jlnavas3.bovedalocal.util

import com.nulabinc.zxcvbn.Zxcvbn
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import kotlin.math.ln
import kotlin.math.max

data class Fuerza(
    val puntuacion: Int,
    val etiqueta: String,
    val tiempo: String,
    val fraccion: Float,
    val bits: Double
)

object MedidorFuerza {

    private val zxcvbn by lazy { Zxcvbn() }

    fun medir(contrasena: String): Fuerza {
        if (contrasena.isEmpty()) {
            return Fuerza(0, "Vacía", "Sin contraseña", 0f, 0.0)
        }
        val recortada = if (contrasena.length > 72) contrasena.substring(0, 72) else contrasena
        val medida = zxcvbn.measure(recortada)
        val intentos = max(medida.guesses, 1.0)
        val bitsZxcvbn = ln(intentos) / ln(2.0)

        // Si la clave no tiene patrones de diccionario ni repeticiones (score >= 3),
        // se evalúa la entropía combinatoria del espacio de caracteres usados
        val bits = if (medida.score >= 3) {
            var pool = 0
            if (contrasena.any { it.isUpperCase() }) pool += 26
            if (contrasena.any { it.isLowerCase() }) pool += 26
            if (contrasena.any { it.isDigit() }) pool += 10
            if (contrasena.any { !it.isLetterOrDigit() }) pool += 33
            val bitsCombinatorios = if (pool > 0) contrasena.length * (ln(pool.toDouble()) / ln(2.0)) else 0.0
            maxOf(bitsZxcvbn, bitsCombinatorios)
        } else {
            bitsZxcvbn
        }

        val etiqueta = when {
            bits >= 90.0 || medida.score == 4 -> "Excelente"
            bits >= 65.0 || medida.score == 3 -> "Fuerte"
            bits >= 45.0 || medida.score == 2 -> "Aceptable"
            medida.score == 1 -> "Débil"
            else -> "Muy débil"
        }
        val puntuacion = when (etiqueta) {
            "Excelente" -> 4
            "Fuerte" -> 3
            "Aceptable" -> 2
            "Débil" -> 1
            else -> 0
        }

        return Fuerza(
            puntuacion = puntuacion,
            etiqueta = etiqueta,
            tiempo = PasswordGenerator.tiempoDeCrackeo(bits),
            fraccion = ((puntuacion + 1) / 5f).coerceIn(0.08f, 1f),
            bits = bits
        )
    }
}
