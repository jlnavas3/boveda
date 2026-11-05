/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.data

/**
 * Representa los estilos de animación nativos de Compose disponibles para la navegación entre pantallas.
 */
enum class EstiloTransicionPantalla(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val consumoGpu: String,
    val detalleRendimiento: String
) {
    PARALLAX(
        id = "PARALLAX",
        titulo = "Deslizamiento suave",
        descripcion = "Desplazamiento horizontal con efecto parallax en la pantalla de fondo.",
        consumoGpu = "GPU Bajo",
        detalleRendimiento = "Acelerado por hardware nativo. Rendimiento óptimo en cualquier móvil."
    ),
    STACK(
        id = "STACK",
        titulo = "Pila de tarjetas 3D",
        descripcion = "La pantalla anterior se contrae sutilmente mientras la nueva entra por encima.",
        consumoGpu = "GPU Moderado",
        detalleRendimiento = "Combina escala y desplazamiento. Sensación de profundidad elegante."
    ),
    FADE(
        id = "FADE",
        titulo = "Desvanecimiento puro",
        descripcion = "Transición suave de opacidad sin movimiento espacial.",
        consumoGpu = "GPU Mínimo",
        detalleRendimiento = "Máxima discreción y rapidez. Consumo insignificante de batería."
    ),
    ZOOM(
        id = "ZOOM",
        titulo = "Escala y zoom",
        descripcion = "La pantalla entra expandiéndose desde el centro hacia los bordes.",
        consumoGpu = "GPU Moderado",
        detalleRendimiento = "Transformación vectorial fluida asistida por la GPU."
    ),
    SHEET(
        id = "SHEET",
        titulo = "Deslizamiento vertical",
        descripcion = "Entrada desde la parte inferior como una hoja o tarjeta modal.",
        consumoGpu = "GPU Bajo",
        detalleRendimiento = "Desplazamiento vertical nativo de alta respuesta táctil."
    ),
    WIPE(
        id = "WIPE",
        titulo = "Cortinilla directa",
        descripcion = "La nueva pantalla cubre la vista de lado a lado con fondo estático.",
        consumoGpu = "GPU Bajo",
        detalleRendimiento = "Movimiento lineal directo sin renderizado de fondo simultáneo."
    ),
    INSTANTANEO(
        id = "INSTANTANEO",
        titulo = "Sin animación",
        descripcion = "Cambio inmediato de 0 ms entre pantallas.",
        consumoGpu = "GPU 0%",
        detalleRendimiento = "Cero procesamiento gráfico. Velocidad instantánea absoluta."
    );

    companion object {
        fun desdeId(id: String): EstiloTransicionPantalla =
            entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: INSTANTANEO
    }
}
