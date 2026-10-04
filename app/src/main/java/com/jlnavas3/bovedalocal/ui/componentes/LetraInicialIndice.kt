package com.jlnavas3.bovedalocal.ui.componentes

fun letraInicialIndice(texto: String, incluirEnie: Boolean = true): Char {
    val limpia = texto.trim()
    if (limpia.isEmpty()) return '#'
    return normalizarCaracterIndice(limpia.first(), incluirEnie)
}
