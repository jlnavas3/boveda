/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

import com.jlnavas3.bovedalocal.data.ContenidoBoveda

/**
 * Mensajes de alto nivel intercambiados a través del túnel seguro P2P.
 * Espejo canónico de `MensajeP2P` en `src/sync/p2p/wire_protocol.rs` en Rust.
 */
sealed interface MensajeP2P {
    /** Bóveda completa serializada para sincronización bidireccional (tipo 0x01). */
    data class SyncPayload(val boveda: ContenidoBoveda) : MensajeP2P

    /** Confirmación de recepción y commit persistido atómicamente (tipo 0x02). */
    data class CommitAck(val ack: String = "OK") : MensajeP2P
}
