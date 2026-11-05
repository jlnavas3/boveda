/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.crypto

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToLong

/**
 * Microcalculador desacoplado para estimar la resistencia temporal de una contraseña
 * ante ataques modernos de fuerza bruta offline (10^11 intentos/segundo - cluster de GPUs).
 */
object CalculadorTiempoResistencia {

    private const val INTENTOS_POR_SEGUNDO = 1e11
    private const val SEGUNDOS_POR_ANO = 31_536_000.0 // 365 días estándar
    private val formatoEspanol: NumberFormat by lazy {
        NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-ES"))
    }

    /**
     * Estima el tiempo promedio en lenguaje humano en español para romper una clave
     * con [bits] de entropía mediante fuerza bruta exhaustiva (promedio = 2^(bits - 1)).
     */
    fun formatear(bits: Double): String {
        if (bits <= 0.0) return "Descifrable al instante"

        // Si la entropía es masiva (>= 145 bits), supera 1 cuatrillón de años
        if (bits >= 145.0) return "Resiste más de 1 cuatrillón de años"

        val segundos = 2.0.pow(bits - 1.0) / INTENTOS_POR_SEGUNDO

        return when {
            segundos < 1.0 -> "Descifrable al instante"
            segundos < 60.0 -> {
                val s = segundos.roundToLong().coerceAtLeast(1L)
                if (s == 1L) "Descifrable en 1 segundo" else "Descifrable en $s segundos"
            }
            segundos < 3_600.0 -> {
                val m = (segundos / 60.0).roundToLong().coerceAtLeast(1L)
                if (m == 1L) "Descifrable en 1 minuto" else "Descifrable en $m minutos"
            }
            segundos < 86_400.0 -> {
                val h = (segundos / 3_600.0).roundToLong().coerceAtLeast(1L)
                if (h == 1L) "Descifrable en 1 hora" else "Descifrable en $h horas"
            }
            segundos < 2_592_000.0 -> { // < 30 días
                val d = (segundos / 86_400.0).roundToLong().coerceAtLeast(1L)
                if (d == 1L) "Resiste 1 día" else "Resiste $d días"
            }
            segundos < SEGUNDOS_POR_ANO -> { // < 12 meses
                val meses = (segundos / 2_592_000.0).roundToLong().coerceAtLeast(1L)
                if (meses == 1L) "Resiste 1 mes" else "Resiste $meses meses"
            }
            else -> {
                val anos = segundos / SEGUNDOS_POR_ANO
                formatearEscalaAnos(anos)
            }
        }
    }

    private fun formatearEscalaAnos(anos: Double): String {
        return when {
            anos < 1_000.0 -> {
                val a = anos.roundToLong().coerceAtLeast(1L)
                if (a == 1L) "Resiste 1 año" else "Resiste $a años"
            }
            anos < 1_000_000.0 -> {
                val miles = (anos / 1_000.0).roundToLong().coerceAtLeast(1L)
                if (miles == 1L) "Resiste mil años" else "Resiste ${formatoEspanol.format(miles)} mil años"
            }
            anos < 1_000_000_000.0 -> {
                val millones = (anos / 1_000_000.0).roundToLong().coerceAtLeast(1L)
                if (millones == 1L) "Resiste 1 millón de años" else "Resiste ${formatoEspanol.format(millones)} millones de años"
            }
            anos < 1e12 -> {
                val milesMillones = (anos / 1_000_000_000.0).roundToLong().coerceAtLeast(1L)
                if (milesMillones == 1L) "Resiste mil millones de años" else "Resiste ${formatoEspanol.format(milesMillones)} mil millones de años"
            }
            anos < 1e15 -> {
                val billones = (anos / 1e12).roundToLong().coerceAtLeast(1L)
                if (billones == 1L) "Resiste 1 billón de años" else "Resiste ${formatoEspanol.format(billones)} billones de años"
            }
            anos < 1e18 -> {
                val milBillones = (anos / 1e15).roundToLong().coerceAtLeast(1L)
                if (milBillones == 1L) "Resiste mil billones de años" else "Resiste ${formatoEspanol.format(milBillones)} mil billones de años"
            }
            anos < 1e24 -> {
                val trillones = (anos / 1e18).roundToLong().coerceAtLeast(1L)
                if (trillones == 1L) "Resiste 1 trillón de años" else "Resiste ${formatoEspanol.format(trillones)} trillones de años"
            }
            else -> "Resiste más de 1 cuatrillón de años"
        }
    }
}
