package com.jlnavas3.bovedalocal.crypto

import com.jlnavas3.bovedalocal.data.AjustesDefaults
import java.security.SecureRandom
import kotlin.math.ln
import kotlin.math.pow

data class OpcionesGenerador(
    val longitud: Int = AjustesDefaults.Generador.LONGITUD,
    val mayusculas: Boolean = AjustesDefaults.Generador.MAYUSCULAS,
    val minusculas: Boolean = AjustesDefaults.Generador.MINUSCULAS,
    val digitos: Boolean = AjustesDefaults.Generador.DIGITOS,
    val simbolos: Boolean = AjustesDefaults.Generador.SIMBOLOS,
    val simbolosPersonalizados: String = PasswordGenerator.SIMBOLOS,
    val excluirAmbiguos: Boolean = AjustesDefaults.Generador.EXCLUIR_AMBIGUOS,
    val modoFrase: Boolean = AjustesDefaults.Generador.MODO_FRASE,
    val palabras: Int = AjustesDefaults.Generador.PALABRAS,
    val separadorFrase: String = AjustesDefaults.Generador.SEPARADOR_FRASE,
    val idiomaFrase: String = AjustesDefaults.Generador.IDIOMA_FRASE,
    val capitalizarFrase: Boolean = AjustesDefaults.Generador.CAPITALIZAR_FRASE,
    val modoPatron: Boolean = AjustesDefaults.Generador.MODO_PATRON,
    val patron: String = AjustesDefaults.Generador.PATRON
)

object PasswordGenerator {

    const val MAYUSCULAS = "ABCDEFGHJKLMNPQRSTUVWXYZ"
    const val MINUSCULAS = "abcdefghijkmnopqrstuvwxyz"
    const val DIGITOS = "23456789"
    const val SIMBOLOS = "!@#$%&*()-_=+[]{}?/.,:;"

    const val ALFANUM_MAYUS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    const val LETRAS_MAYUS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    const val LETRAS_MINUS = "abcdefghijklmnopqrstuvwxyz"
    const val DIGITOS_TODOS = "0123456789"

    private val aleatorio = SecureRandom()

    fun conjunto(opciones: OpcionesGenerador): String = buildString {
        val mayus = if (opciones.excluirAmbiguos) MAYUSCULAS else LETRAS_MAYUS
        val minus = if (opciones.excluirAmbiguos) MINUSCULAS else LETRAS_MINUS
        val dig = if (opciones.excluirAmbiguos) DIGITOS else DIGITOS_TODOS
        if (opciones.mayusculas) append(mayus)
        if (opciones.minusculas) append(minus)
        if (opciones.digitos) append(dig)
        if (opciones.simbolos) {
            val s = if (opciones.simbolosPersonalizados.isNotEmpty()) opciones.simbolosPersonalizados else SIMBOLOS
            append(s)
        }
    }

    fun generar(opciones: OpcionesGenerador): String = when {
        opciones.modoPatron -> generarPorPatron(opciones.patron)
        opciones.modoFrase -> generarFrase(
            numeroPalabras = opciones.palabras.coerceIn(AjustesDefaults.Generador.PALABRAS_MIN, AjustesDefaults.Generador.PALABRAS_MAX),
            separador = opciones.separadorFrase,
            idioma = opciones.idiomaFrase,
            capitalizar = opciones.capitalizarFrase
        )
        else -> generarAleatoria(opciones)
    }

    fun generarFrase(
        numeroPalabras: Int,
        separador: String = AjustesDefaults.Generador.SEPARADOR_FRASE,
        idioma: String = AjustesDefaults.Generador.IDIOMA_FRASE,
        capitalizar: Boolean = AjustesDefaults.Generador.CAPITALIZAR_FRASE
    ): String {
        val lista = Wordlist.obtenerPalabras(idioma)
        val cant = numeroPalabras.coerceIn(AjustesDefaults.Generador.PALABRAS_MIN, AjustesDefaults.Generador.PALABRAS_MAX)
        return (0 until cant)
            .map {
                val palabra = lista[aleatorio.nextInt(lista.size)]
                if (capitalizar) palabra.replaceFirstChar { c -> c.uppercaseChar() } else palabra
            }
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
        val longitud = opciones.longitud.coerceIn(AjustesDefaults.Generador.LONGITUD_MIN, AjustesDefaults.Generador.LONGITUD_MAX)
        val simbolosEfectivos = if (opciones.simbolosPersonalizados.isNotEmpty()) opciones.simbolosPersonalizados else SIMBOLOS
        val mayus = if (opciones.excluirAmbiguos) MAYUSCULAS else LETRAS_MAYUS
        val minus = if (opciones.excluirAmbiguos) MINUSCULAS else LETRAS_MINUS
        val dig = if (opciones.excluirAmbiguos) DIGITOS else DIGITOS_TODOS
        val grupos = buildList {
            if (opciones.mayusculas) add(mayus)
            if (opciones.minusculas) add(minus)
            if (opciones.digitos) add(dig)
            if (opciones.simbolos && simbolosEfectivos.isNotEmpty()) add(simbolosEfectivos)
        }.ifEmpty { listOf(minus) }
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
        opciones.modoFrase -> {
            val tamano = Wordlist.obtenerTamano(opciones.idiomaFrase)
            log2(tamano.toDouble()) * opciones.palabras.coerceIn(AjustesDefaults.Generador.PALABRAS_MIN, AjustesDefaults.Generador.PALABRAS_MAX)
        }
        else -> {
            val tamano = conjunto(opciones).length.coerceAtLeast(1)
            log2(tamano.toDouble()) * opciones.longitud.coerceIn(AjustesDefaults.Generador.LONGITUD_MIN, AjustesDefaults.Generador.LONGITUD_MAX)
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

    /** Estimación en lenguaje humano realista y profesional para resistencia ante ataques de fuerza bruta. */
    fun tiempoDeCrackeo(bits: Double): String {
        if (bits <= 0) return "Descifrable al instante"
        val intentosPorSegundo = 1e11
        val segundos = 2.0.pow(bits - 1) / intentosPorSegundo
        val segundosL = segundos.toLong()
        return when {
            segundos < 1 -> "Descifrable al instante"
            segundos < 60 -> if (segundosL == 1L) "Descifrable en 1 segundo" else "Descifrable en $segundosL segundos"
            segundos < 3_600 -> {
                val m = (segundos / 60).toLong()
                if (m == 1L) "Descifrable en 1 minuto" else "Descifrable en $m minutos"
            }
            segundos < 86_400 -> {
                val h = (segundos / 3_600).toLong()
                if (h == 1L) "Descifrable en 1 hora" else "Descifrable en $h horas"
            }
            segundos < 2_592_000 -> {
                val d = (segundos / 86_400).toLong()
                if (d == 1L) "Resistente por 1 día" else "Resistente por $d días"
            }
            segundos < 31_536_000 -> {
                val mes = (segundos / 2_592_000).toLong()
                if (mes == 1L) "Resistente por 1 mes" else "Resistente por $mes meses"
            }
            segundos < 3.1536e9 -> {
                val a = (segundos / 31_536_000).toLong()
                if (a == 1L) "Resistente por 1 año" else "Resistente por $a años"
            }
            segundos < 3.1536e10 -> "Resistente por varios siglos"
            else -> "Inquebrantable por fuerza bruta"
        }
    }
}
