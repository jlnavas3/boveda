package com.jlnavas3.bovedalocal.crypto

import java.security.SecureRandom
import kotlin.math.ln
import kotlin.math.pow

data class OpcionesGenerador(
    val longitud: Int = 20,
    val mayusculas: Boolean = true,
    val minusculas: Boolean = true,
    val digitos: Boolean = true,
    val simbolos: Boolean = true,
    val modoFrase: Boolean = false,
    val palabras: Int = 5,
    val separadorFrase: String = "-",
    val modoPatron: Boolean = false,
    val patron: String = "XXXXX-XXXXX-XXXXX-XXXXX-XXXXX"
)

object PasswordGenerator {

    const val MAYUSCULAS = "ABCDEFGHJKLMNPQRSTUVWXYZ"
    const val MINUSCULAS = "abcdefghijkmnopqrstuvwxyz"
    const val DIGITOS = "23456789"
    const val SIMBOLOS = "!@#\$%&*()-_=+[]{}?/.,:;"

    const val ALFANUM_MAYUS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    const val LETRAS_MAYUS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    const val LETRAS_MINUS = "abcdefghijklmnopqrstuvwxyz"
    const val DIGITOS_TODOS = "0123456789"

    private val aleatorio = SecureRandom()

    fun conjunto(opciones: OpcionesGenerador): String = buildString {
        if (opciones.mayusculas) append(MAYUSCULAS)
        if (opciones.minusculas) append(MINUSCULAS)
        if (opciones.digitos) append(DIGITOS)
        if (opciones.simbolos) append(SIMBOLOS)
    }

    fun generar(opciones: OpcionesGenerador): String = when {
        opciones.modoPatron -> generarPorPatron(opciones.patron)
        opciones.modoFrase -> generarFrase(opciones.palabras.coerceIn(3, 12), opciones.separadorFrase)
        else -> generarAleatoria(opciones)
    }

    fun generarFrase(numeroPalabras: Int, separador: String = "-"): String {
        val lista = Wordlist.PALABRAS
        val cant = numeroPalabras.coerceIn(3, 12)
        return (0 until cant)
            .map { lista[aleatorio.nextInt(lista.size)] }
            .joinToString(separador)
    }

    fun generarPorPatron(patron: String): String {
        if (patron.isBlank()) return ""
        val sb = StringBuilder()
        var i = 0
        while (i < patron.length) {
            val c = patron[i]
            when (c) {
                'X' -> sb.append(ALFANUM_MAYUS[aleatorio.nextInt(ALFANUM_MAYUS.length)])
                'A' -> sb.append(LETRAS_MAYUS[aleatorio.nextInt(LETRAS_MAYUS.length)])
                'a' -> sb.append(LETRAS_MINUS[aleatorio.nextInt(LETRAS_MINUS.length)])
                '9', 'd' -> sb.append(DIGITOS_TODOS[aleatorio.nextInt(DIGITOS_TODOS.length)])
                'w' -> sb.append(Wordlist.PALABRAS[aleatorio.nextInt(Wordlist.PALABRAS.size)])
                '\\' -> {
                    if (i + 1 < patron.length) {
                        sb.append(patron[i + 1])
                        i++
                    } else {
                        sb.append('\\')
                    }
                }
                else -> sb.append(c)
            }
            i++
        }
        return sb.toString()
    }

    fun generarAleatoria(opciones: OpcionesGenerador): String {
        val longitud = opciones.longitud.coerceIn(8, 64)
        val grupos = buildList {
            if (opciones.mayusculas) add(MAYUSCULAS)
            if (opciones.minusculas) add(MINUSCULAS)
            if (opciones.digitos) add(DIGITOS)
            if (opciones.simbolos) add(SIMBOLOS)
        }.ifEmpty { listOf(MINUSCULAS) }
        val todos = grupos.joinToString("")
        val salida = CharArray(longitud)
        for (i in 0 until longitud) salida[i] = todos[aleatorio.nextInt(todos.length)]
        // Garantiza al menos un carácter de cada grupo seleccionado.
        val posiciones = (0 until longitud).shuffled(aleatorio)
        grupos.forEachIndexed { indice, grupo ->
            if (indice < longitud) {
                val pos = posiciones[indice]
                salida[pos] = grupo[aleatorio.nextInt(grupo.length)]
            }
        }
        val resultado = String(salida)
        salida.fill('\u0000')
        return resultado
    }

    fun entropiaBits(opciones: OpcionesGenerador): Double = when {
        opciones.modoPatron -> entropiaPatron(opciones.patron)
        opciones.modoFrase -> log2(Wordlist.TAMANO.toDouble()) * opciones.palabras.coerceIn(3, 12)
        else -> {
            val tamano = conjunto(opciones).length.coerceAtLeast(1)
            log2(tamano.toDouble()) * opciones.longitud.coerceIn(8, 64)
        }
    }

    fun entropiaPatron(patron: String): Double {
        var bits = 0.0
        var i = 0
        while (i < patron.length) {
            val c = patron[i]
            when (c) {
                'X' -> bits += log2(ALFANUM_MAYUS.length.toDouble())
                'A' -> bits += log2(LETRAS_MAYUS.length.toDouble())
                'a' -> bits += log2(LETRAS_MINUS.length.toDouble())
                '9', 'd' -> bits += log2(DIGITOS_TODOS.length.toDouble())
                'w' -> bits += log2(Wordlist.TAMANO.toDouble())
                '\\' -> i++
            }
            i++
        }
        return bits
    }

    private fun log2(x: Double): Double = ln(x) / ln(2.0)

    /** Estimación en lenguaje humano suponiendo 10^11 intentos por segundo. */
    fun tiempoDeCrackeo(bits: Double): String {
        if (bits <= 0) return "al instante"
        val intentosPorSegundo = 1e11
        val segundos = 2.0.pow(bits - 1) / intentosPorSegundo
        return when {
            segundos < 1 -> "menos de un segundo"
            segundos < 60 -> "${segundos.toLong()} segundos"
            segundos < 3_600 -> "${(segundos / 60).toLong()} minutos"
            segundos < 86_400 -> "${(segundos / 3_600).toLong()} horas"
            segundos < 2_592_000 -> "${(segundos / 86_400).toLong()} días"
            segundos < 31_536_000 -> "${(segundos / 2_592_000).toLong()} meses"
            segundos < 3.1536e9 -> "${(segundos / 31_536_000).toLong()} años"
            segundos < 3.1536e10 -> "${(segundos / 3.1536e9).toLong()} siglos"
            segundos < 3.1536e16 -> "${formatoGrande(segundos / 31_536_000)} años"
            else -> "más que la edad del universo"
        }
    }

    private fun formatoGrande(valor: Double): String {
        val unidades = listOf("" to 1.0, " mil" to 1e3, " millones de" to 1e6, " mil millones de" to 1e9, " billones de" to 1e12)
        for ((sufijo, factor) in unidades.reversed()) {
            if (valor >= factor) return "${(valor / factor).toLong()}$sufijo"
        }
        return valor.toLong().toString()
    }
}
