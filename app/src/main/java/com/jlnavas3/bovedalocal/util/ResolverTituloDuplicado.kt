package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada

/**
 * Microcomponente puro desacoplado para resolver colisiones de títulos estilo Windows ("Copia ...").
 *
 * Ejemplos:
 * - "Banco" -> "Copia Banco"
 * - "Copia Banco" -> "Copia (2) Banco"
 * - "Copia (2) Banco" -> "Copia (3) Banco"
 */
object ResolverTituloDuplicado {

    private val patronCopiaConNumero = Regex("""^Copia\s+\((\d+)\)\s+(.+)$""", RegexOption.IGNORE_CASE)
    private val patronCopiaSinNumero = Regex("""^Copia\s+(.+)$""", RegexOption.IGNORE_CASE)

    fun resolver(
        tituloOriginal: String,
        titulosExistentes: Collection<String>
    ): String {
        val tituloLimpio = tituloOriginal.trim()
        val existentesSet = titulosExistentes
            .map { it.trim().lowercase() }
            .filter { it.isNotBlank() }
            .toSet()

        val claveActual = tituloLimpio.lowercase()
        if (claveActual !in existentesSet) {
            return tituloLimpio
        }

        val base: String
        val inicioNum: Int

        val matchConNum = patronCopiaConNumero.matchEntire(tituloLimpio)
        val matchSinNum = if (matchConNum == null) patronCopiaSinNumero.matchEntire(tituloLimpio) else null

        when {
            matchConNum != null -> {
                inicioNum = (matchConNum.groupValues[1].toIntOrNull() ?: 1) + 1
                base = matchConNum.groupValues[2].trim()
            }
            matchSinNum != null -> {
                inicioNum = 2
                base = matchSinNum.groupValues[1].trim()
            }
            else -> {
                inicioNum = 1
                base = tituloLimpio.ifBlank { "Entrada" }
            }
        }

        if (inicioNum == 1) {
            val candidatoSimple = "Copia $base"
            if (candidatoSimple.lowercase() !in existentesSet) {
                return candidatoSimple
            }
        }

        var num = if (inicioNum == 1) 2 else inicioNum
        while (true) {
            val candidato = "Copia ($num) $base"
            if (candidato.lowercase() !in existentesSet) {
                return candidato
            }
            num++
        }
    }

    fun resolverEntrada(
        entrada: Entrada,
        titulosExistentes: Collection<String>
    ): Entrada {
        val nuevoTitulo = resolver(entrada.titulo, titulosExistentes)
        return if (nuevoTitulo == entrada.titulo) entrada else entrada.copy(titulo = nuevoTitulo)
    }
}
