package com.pepotech.pepoboveda.util

import com.pepotech.pepoboveda.data.Entrada
import com.pepotech.pepoboveda.data.TipoEntrada

/**
 * El sitio por el que se agrupan varias entradas: dominio raíz en claves y rpId en passkeys.
 * Así, www.amazon.com, login.amazon.com y amazon.com comparten grupo.
 */
fun claveAgrupacionSitio(entrada: Entrada): String? = when (entrada.tipo) {
    TipoEntrada.LOGIN -> entrada.urls.asSequence()
        .map { Dominios.raiz(it) }
        .firstOrNull { it.isNotBlank() }
    TipoEntrada.PASSKEY -> entrada.passkey?.rpId?.takeIf { it.isNotBlank() }
    TipoEntrada.NOTA -> null
}

sealed interface ItemAgrupado {
    data class Suelto(val entrada: Entrada) : ItemAgrupado
    data class Grupo(val clave: String, val entradas: List<Entrada>) : ItemAgrupado
    data class Hijo(val entrada: Entrada) : ItemAgrupado
}

/**
 * Junta en un solo item plegable las entradas que comparten sitio (como en
 * passwords.google.com); si [expandido] dice que sí para ese sitio, añade además
 * sus entradas justo debajo como hijos. Un sitio con una sola cuenta no se agrupa.
 * Se usa tanto en la lista principal como en Salud de la bóveda, para no repetir
 * la misma cuenta en pantalla una vez por cada aviso.
 */
fun construirItemsAgrupadosPorSitio(entradas: List<Entrada>, expandido: (String) -> Boolean): List<ItemAgrupado> {
    val porSitio = entradas.groupBy { claveAgrupacionSitio(it) }
    val vistos = mutableSetOf<String>()
    return buildList {
        entradas.forEach { entrada ->
            val clave = claveAgrupacionSitio(entrada)
            val delMismoSitio = clave?.let { porSitio[it] }
            if (clave != null && delMismoSitio != null && delMismoSitio.size > 1) {
                if (vistos.add(clave)) {
                    add(ItemAgrupado.Grupo(clave, delMismoSitio))
                    if (expandido(clave)) {
                        delMismoSitio.forEach { add(ItemAgrupado.Hijo(it)) }
                    }
                }
            } else {
                add(ItemAgrupado.Suelto(entrada))
            }
        }
    }
}
