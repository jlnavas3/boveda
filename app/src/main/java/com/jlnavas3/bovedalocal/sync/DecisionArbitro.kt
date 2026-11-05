/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

/**
 * Decisión tomada por el árbitro determinista de resolución de estados para una entrada.
 * Espejo canónico de `DecisionArbitro` en Rust (`src/sync/merge/arbiter.rs`).
 */
enum class DecisionArbitro {
    SIN_CAMBIOS,
    FAST_FORWARD_LOCAL,
    FAST_FORWARD_REMOTO,
    SPLIT_BRAIN
}
