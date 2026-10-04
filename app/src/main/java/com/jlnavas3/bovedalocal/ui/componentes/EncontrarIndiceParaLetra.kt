package com.jlnavas3.bovedalocal.ui.componentes

import com.jlnavas3.bovedalocal.util.ItemAgrupado

private fun ItemAgrupado.tituloParaIndice(): String = when (this) {
    is ItemAgrupado.Suelto -> entrada.titulo
    is ItemAgrupado.Grupo -> clave.removePrefix("www.")
    is ItemAgrupado.Hijo -> entrada.titulo
}

private fun ItemAgrupado.esFavorito(): Boolean = when (this) {
    is ItemAgrupado.Suelto -> entrada.favorito
    is ItemAgrupado.Grupo -> entradas.any { it.favorito }
    is ItemAgrupado.Hijo -> entrada.favorito
}

/**
 * Encuentra el índice del elemento en la lista correspondiente a la letra solicitada.
 * Prioriza los elementos de la sección alfabética general para no quedar atrapado
 * en elementos favoritos fijados arriba, y avanza a la siguiente letra disponible si no hay coincidencia directa.
 */
fun encontrarIndiceParaLetra(items: List<ItemAgrupado>, letra: Char, incluirEnie: Boolean = true): Int? {
    if (items.isEmpty()) return null
    val listaLetras = obtenerLetrasIndice(incluirEnie)

    if (letra == '#') {
        val exactoNoFav = items.indexOfFirst { !it.esFavorito() && letraInicialIndice(it.tituloParaIndice(), incluirEnie) == '#' }
        if (exactoNoFav >= 0) return exactoNoFav
        val exactoCualquiera = items.indexOfFirst { letraInicialIndice(it.tituloParaIndice(), incluirEnie) == '#' }
        return if (exactoCualquiera >= 0) exactoCualquiera else 0
    }

    val idxExactoNoFav = items.indexOfFirst { !it.esFavorito() && letraInicialIndice(it.tituloParaIndice(), incluirEnie) == letra }
    if (idxExactoNoFav >= 0) return idxExactoNoFav

    val idxExactoCualquiera = items.indexOfFirst { letraInicialIndice(it.tituloParaIndice(), incluirEnie) == letra }
    if (idxExactoCualquiera >= 0) return idxExactoCualquiera

    val posLetraBuscada = listaLetras.indexOf(letra)
    if (posLetraBuscada >= 0) {
        for (i in (posLetraBuscada + 1) until listaLetras.size) {
            val letraSiguiente = listaLetras[i]
            val idxSigNoFav = items.indexOfFirst { !it.esFavorito() && letraInicialIndice(it.tituloParaIndice(), incluirEnie) == letraSiguiente }
            if (idxSigNoFav >= 0) return idxSigNoFav

            val idxSigCualquiera = items.indexOfFirst { letraInicialIndice(it.tituloParaIndice(), incluirEnie) == letraSiguiente }
            if (idxSigCualquiera >= 0) return idxSigCualquiera
        }
    }

    return items.lastIndex
}
