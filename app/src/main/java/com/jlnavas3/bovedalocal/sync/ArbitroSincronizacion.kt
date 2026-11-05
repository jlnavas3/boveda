/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

/**
 * Árbitro determinista de resolución de estados de sincronización P2P.
 * Función pura libre de efectos secundarios (espejo exacto de `src/sync/merge/arbiter.rs`).
 */
object ArbitroSincronizacion {

    /**
     * Evalúa dos versiones de la misma entrada (mismo UUID) de forma 100% determinista.
     *
     * @param hashLocal Hash SHA-256 de 32 bytes del contenido local actual.
     * @param lastSyncLocal Hash SHA-256 de 32 bytes registrado en el último sync local, o null si nunca sincronizó.
     * @param revLocal Contador de revisión local.
     * @param hashRemoto Hash SHA-256 de 32 bytes del contenido remoto actual.
     * @param lastSyncRemoto Hash SHA-256 de 32 bytes registrado en el último sync remoto, o null si nunca sincronizó.
     * @param revRemoto Contador de revisión remoto.
     */
    fun evaluarEstadoEntrada(
        hashLocal: ByteArray,
        lastSyncLocal: ByteArray?,
        revLocal: Long,
        hashRemoto: ByteArray,
        lastSyncRemoto: ByteArray?,
        revRemoto: Long
    ): DecisionArbitro {
        require(hashLocal.size == 32 && hashRemoto.size == 32) {
            "Los hashes de contenido deben ser exactamente de 32 bytes (SHA-256)."
        }
        if (lastSyncLocal != null) {
            require(lastSyncLocal.size == 32) { "lastSyncLocal debe tener 32 bytes si está presente." }
        }
        if (lastSyncRemoto != null) {
            require(lastSyncRemoto.size == 32) { "lastSyncRemoto debe tener 32 bytes si está presente." }
        }

        // 1. Idénticas en contenido: nada que hacer
        if (hashLocal.contentEquals(hashRemoto)) {
            return DecisionArbitro.SIN_CAMBIOS
        }

        // 2. Fast-Forward Local: El dispositivo local avanzó, pero el remoto sigue
        //    exactamente en el estado del último sync que recuerda el local.
        if (lastSyncLocal != null && lastSyncLocal.contentEquals(hashRemoto) && revLocal > revRemoto) {
            return DecisionArbitro.FAST_FORWARD_LOCAL
        }

        // 3. Fast-Forward Remoto: El dispositivo remoto avanzó, pero el local sigue
        //    exactamente en el estado del último sync que recuerda el remoto.
        if (lastSyncRemoto != null && lastSyncRemoto.contentEquals(hashLocal) && revRemoto > revLocal) {
            return DecisionArbitro.FAST_FORWARD_REMOTO
        }

        // 4. Fork simétrico (rev 5 vs rev 5), fork asimétrico (rev 6 vs rev 5 con base común rev 4),
        //    o primer sync sin ancestro (null): ¡Split-Brain Concurrente!
        return DecisionArbitro.SPLIT_BRAIN
    }
}
