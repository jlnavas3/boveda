/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.salud

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jlnavas3.bovedalocal.data.AnalizadorDuplicados
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.util.ContrasenasComunes
import com.jlnavas3.bovedalocal.util.MedidorFuerza
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Agrupa, optimiza y calcula asíncronamente en Dispatchers.Default los análisis
 * de contraseñas de la bóveda para garantizar apertura a 60 fps sin congelamiento.
 */
@Composable
fun rememberDatosSaludBoveda(
    entradas: List<Entrada>,
    umbralDias: Int,
    ahora: Long,
    contexto: Context,
    anadidasComunes: Set<String> = emptySet(),
    excluidasComunes: Set<String> = emptySet()
): DatosSaludBoveda {
    var resultado by remember {
        mutableStateOf(
            DatosSaludBoveda(calculando = true)
        )
    }

    LaunchedEffect(entradas, umbralDias, ahora, anadidasComunes, excluidasComunes) {
        val datos = withContext(Dispatchers.Default) {
            val claves = entradas.filter {
                it.tipo == TipoEntrada.LOGIN && it.contrasena.isNotBlank() && !it.ignoradaEnSalud
            }
            val ignoradas = entradas.filter { it.ignoradaEnSalud }

            val duplicadas = claves.groupBy { it.contrasena }.values.filter { it.size > 1 }

            // Optimización con caché de contraseñas únicas para evitar evaluar zxcvbn miles de veces
            val cachePuntuacion = mutableMapOf<String, Int>()
            fun puntuacionClave(pass: String): Int {
                return cachePuntuacion.getOrPut(pass) {
                    when {
                        pass.length < 6 -> 0
                        pass.length >= 20 && pass.any { it.isUpperCase() } && pass.any { it.isDigit() } && pass.any { !it.isLetterOrDigit() } -> 4
                        else -> MedidorFuerza.medir(pass).puntuacion
                    }
                }
            }

            val debiles = claves
                .filter { puntuacionClave(it.contrasena) <= 1 }
                .sortedBy { puntuacionClave(it.contrasena) }

            val muyComunes = claves.filter {
                ContrasenasComunes.esComun(contexto, it.contrasena, anadidasComunes, excluidasComunes)
            }

            val antiguas = if (umbralDias <= 0) {
                emptyList()
            } else {
                claves.filter { it.modificadaEn > 0 && diasDesde(it.modificadaEn, ahora) >= umbralDias.toLong() }
                    .sortedBy { it.modificadaEn }
            }

            val gruposDuplicados = AnalizadorDuplicados.analizar(entradas)
            val totalSobrantesDuplicadas = gruposDuplicados.sumOf { it.entradasSecundarias.size }

            val pestanasConDatos = buildList {
                if (duplicadas.isNotEmpty()) add(PestanaSalud.REPETIDAS)
                if (muyComunes.isNotEmpty()) add(PestanaSalud.COMUNES)
                if (debiles.isNotEmpty()) add(PestanaSalud.DEBILES)
                if (antiguas.isNotEmpty()) add(PestanaSalud.ANTIGUAS)
                if (ignoradas.isNotEmpty()) add(PestanaSalud.IGNORADAS)
            }

            DatosSaludBoveda(
                claves = claves,
                ignoradas = ignoradas,
                duplicadas = duplicadas,
                debiles = debiles,
                muyComunes = muyComunes,
                antiguas = antiguas,
                totalSobrantesDuplicadas = totalSobrantesDuplicadas,
                pestanasConDatos = pestanasConDatos,
                calculando = false
            )
        }
        resultado = datos
    }

    return resultado
}
