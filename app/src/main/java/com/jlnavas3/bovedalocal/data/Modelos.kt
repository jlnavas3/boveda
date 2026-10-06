package com.jlnavas3.bovedalocal.data

fun normalizarEtiqueta(valor: String): String = valor
    .trim()
    .removePrefix("#")
    .filterNot { it.isWhitespace() }

/** Typealias para compatibilidad hacia atrás durante la transición */
typealias Coleccion = Categoria
