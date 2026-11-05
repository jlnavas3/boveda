/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.ContenidoBoveda
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.FusionadorEntradas
import com.jlnavas3.bovedalocal.data.Perfil
import kotlin.math.max

data class ResultadoFusionBovedas(
    val boveda: ContenidoBoveda,
    val informe: InformeSincronizacion
)

/**
 * Orquestador determinista de sincronización y fusión bidireccional de bóvedas completas.
 * Espejo canónico de `src/sync/merge/vault_merge.rs` en Rust.
 *
 * Utiliza [ArbitroSincronizacion] para decidir la estrategia por entrada (SinCambios, FastForward, SplitBrain),
 * reutiliza [FusionadorEntradas] para la preservación no destructiva de submodelos nativos y credenciales,
 * y sella criptográficamente el `lastSyncHash` de cada elemento resultante.
 */
object FusionadorBovedas {

    /**
     * Fusiona dos bóvedas completas (`bovedaLocal` y `bovedaRemota`) emitiendo en el mismo pase
     * el [InformeSincronizacion] detallado con las máscaras de campos modificados.
     */
    fun fusionarBovedasConInforme(
        bovedaLocal: ContenidoBoveda,
        bovedaRemota: ContenidoBoveda
    ): ResultadoFusionBovedas {
        // 1. Fusión de categorías (colecciones)
        val mapaCategorias = LinkedHashMap<String, Categoria>()
        for (cat in bovedaLocal.categorias) {
            mapaCategorias[cat.id] = cat
        }
        for (cat in bovedaRemota.categorias) {
            val existente = mapaCategorias[cat.id]
            if (existente == null || cat.modificadaEn > existente.modificadaEn) {
                mapaCategorias[cat.id] = cat
            }
        }

        // 2. Fusión de perfiles (identidades)
        val mapaPerfiles = LinkedHashMap<String, Perfil>()
        for (perf in bovedaLocal.perfiles) {
            mapaPerfiles[perf.id] = perf
        }
        for (perf in bovedaRemota.perfiles) {
            val existente = mapaPerfiles[perf.id]
            if (existente == null || perf.modificadaEn > existente.modificadaEn) {
                mapaPerfiles[perf.id] = perf
            }
        }

        // 3. Fusión de entradas (universo completo: activas + papelera)
        val todasLocales = (bovedaLocal.entradas + bovedaLocal.papelera).associateBy { it.id }.toMutableMap()
        val todasRemotas = (bovedaRemota.entradas + bovedaRemota.papelera).associateBy { it.id }

        val entradasFusionadas = LinkedHashMap<String, Entrada>()
        val listaNuevas = mutableListOf<ItemCambioSync>()
        val listaActualizadas = mutableListOf<ItemCambioSync>()
        val listaPapelera = mutableListOf<ItemCambioSync>()
        var sinCambiosCount = 0

        for ((id, remota) in todasRemotas) {
            val local = todasLocales.remove(id)
            if (local != null) {
                val hashLocal = CalculadorHashEntrada.calcularContentHash(local)
                val hashRemoto = CalculadorHashEntrada.calcularContentHash(remota)
                val lastSyncLocalBytes = local.lastSyncHash?.let { CalculadorHashEntrada.hexABytes(it) }
                val lastSyncRemotoBytes = remota.lastSyncHash?.let { CalculadorHashEntrada.hexABytes(it) }

                val decision = ArbitroSincronizacion.evaluarEstadoEntrada(
                    hashLocal = hashLocal,
                    lastSyncLocal = lastSyncLocalBytes,
                    revLocal = local.revision,
                    hashRemoto = hashRemoto,
                    lastSyncRemoto = lastSyncRemotoBytes,
                    revRemoto = remota.revision
                )

                val entradaResuelta = when (decision) {
                    DecisionArbitro.SIN_CAMBIOS,
                    DecisionArbitro.FAST_FORWARD_LOCAL -> {
                        local.copy(lastSyncHash = CalculadorHashEntrada.bytesAHex(hashLocal))
                    }
                    DecisionArbitro.FAST_FORWARD_REMOTO -> {
                        remota.copy(lastSyncHash = CalculadorHashEntrada.bytesAHex(hashRemoto))
                    }
                    DecisionArbitro.SPLIT_BRAIN -> {
                        val (principal, secundaria) = if (remota.modificadaEn >= local.modificadaEn) {
                            remota to local
                        } else {
                            local to remota
                        }
                        val fusionada = FusionadorEntradas.fusionar(principal, listOf(secundaria))
                        val nuevaRev = max(local.revision, remota.revision) + 1L
                        val fusionadaConRev = fusionada.copy(revision = nuevaRev)
                        val nuevoHash = CalculadorHashEntrada.calcularContentHash(fusionadaConRev)
                        fusionadaConRev.copy(lastSyncHash = CalculadorHashEntrada.bytesAHex(nuevoHash))
                    }
                }
                entradasFusionadas[id] = entradaResuelta

                // Evaluación en el mismo pase del informe relativo a bovedaLocal
                val mascara = CalculadorMascaraDiff.calcularDiff(local, entradaResuelta)
                when {
                    local.eliminadaEn > 0L && entradaResuelta.eliminadaEn <= 0L -> {
                        listaPapelera.add(ItemCambioSync(entradaResuelta.id, entradaResuelta.titulo, TipoCambioSync.RESTAURADA_DE_PAPELERA, mascara))
                    }
                    local.eliminadaEn <= 0L && entradaResuelta.eliminadaEn > 0L -> {
                        listaPapelera.add(ItemCambioSync(entradaResuelta.id, entradaResuelta.titulo, TipoCambioSync.ENVIADA_A_PAPELERA, mascara))
                    }
                    mascara.hayCambios || local.titulo != entradaResuelta.titulo -> {
                        listaActualizadas.add(ItemCambioSync(entradaResuelta.id, entradaResuelta.titulo, TipoCambioSync.ACTUALIZADA, mascara))
                    }
                    else -> {
                        sinCambiosCount++
                    }
                }
            } else {
                // Entrada solo existente en remota (100% nueva para local)
                val hash = CalculadorHashEntrada.calcularContentHash(remota)
                val entradaResuelta = remota.copy(lastSyncHash = CalculadorHashEntrada.bytesAHex(hash))
                entradasFusionadas[id] = entradaResuelta

                val mascara = CalculadorMascaraDiff.deEntradaNueva(entradaResuelta)
                listaNuevas.add(ItemCambioSync(entradaResuelta.id, entradaResuelta.titulo, TipoCambioSync.NUEVA, mascara))
            }
        }

        // Entradas restantes que solo existen en local (100% conservadas sin cambios para local)
        for ((id, local) in todasLocales) {
            val hash = CalculadorHashEntrada.calcularContentHash(local)
            entradasFusionadas[id] = local.copy(lastSyncHash = CalculadorHashEntrada.bytesAHex(hash))
            sinCambiosCount++
        }

        // 4. Partición estricta entre activas y papelera
        val activas = entradasFusionadas.values
            .filter { it.eliminadaEn <= 0L }
            .sortedBy { it.titulo.lowercase() }

        val papelera = entradasFusionadas.values
            .filter { it.eliminadaEn > 0L }
            .sortedBy { it.titulo.lowercase() }

        val categoriasFinales = mapaCategorias.values.sortedBy { it.nombre.lowercase() }
        val perfilesFinales = mapaPerfiles.values.sortedBy { it.nombre.lowercase() }

        val configuracionFinal = bovedaRemota.configuracion ?: bovedaLocal.configuracion

        val bovedaFinal = ContenidoBoveda(
            version = max(bovedaLocal.version, bovedaRemota.version),
            entradas = activas,
            papelera = papelera,
            categorias = categoriasFinales,
            perfiles = perfilesFinales,
            configuracion = configuracionFinal,
            aliasDispositivo = null
        )

        val informe = InformeSincronizacion(
            nuevas = listaNuevas,
            actualizadas = listaActualizadas,
            papelera = listaPapelera,
            sinCambiosCount = sinCambiosCount,
            dispositivoRemoto = bovedaRemota.aliasDispositivo
        )

        return ResultadoFusionBovedas(bovedaFinal, informe)
    }

    /**
     * Sobrecarga de compatibilidad: fusiona dos bóvedas retornando únicamente el [ContenidoBoveda] resultante.
     */
    fun fusionarBovedas(bovedaLocal: ContenidoBoveda, bovedaRemota: ContenidoBoveda): ContenidoBoveda {
        return fusionarBovedasConInforme(bovedaLocal, bovedaRemota).boveda
    }
}
