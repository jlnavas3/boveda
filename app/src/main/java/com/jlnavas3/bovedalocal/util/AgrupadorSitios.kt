package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion

/**
 * El sitio por el que se agrupan varias entradas: preserva subdominios significativos
 * (ej. account.xiaomi vs xiaomi) y normaliza TLDs equivalentes (amazon.com y amazon.es -> amazon).
 */
fun claveAgrupacionSitio(entrada: Entrada): String? = when (entrada.tipo) {
    TipoEntrada.LOGIN -> {
        val web = entrada.urls.asSequence()
            .filterNot { it.trim().lowercase().startsWith("android://") }
            .map { Dominios.sitioAgrupacion(it) }
            .firstOrNull { it.isNotBlank() }
        web ?: entrada.titulo
            .takeIf { it.isNotBlank() }
            ?.let { Dominios.sitioAgrupacion(it).ifBlank { it.trim().lowercase() } }
    }
    TipoEntrada.PASSKEY -> entrada.passkey?.rpId
        ?.let { Dominios.sitioAgrupacion(it) }
        ?.takeIf { it.isNotBlank() }
    TipoEntrada.NOTA -> null
}

sealed interface ItemAgrupado {
    data class Suelto(val entrada: Entrada) : ItemAgrupado
    data class Grupo(val clave: String, val entradas: List<Entrada>) : ItemAgrupado
    data class Hijo(val entrada: Entrada) : ItemAgrupado
}

/**
 * Junta en un solo item plegable las entradas que comparten sitio o servicio;
 * si [agrupar] es false, se muestran todas individualmente.
 * Ordena los grupos y entradas sueltas según [criterio] para que la etiqueta
 * visible en pantalla respete en todo momento el orden visual esperado.
 */
fun construirItemsAgrupadosPorSitio(
    entradas: List<Entrada>,
    criterio: CriterioOrdenacion = CriterioOrdenacion.NOMBRE_AZ,
    agrupar: Boolean = true,
    expandido: (String) -> Boolean
): List<ItemAgrupado> {
    if (!agrupar) {
        return entradas.map { ItemAgrupado.Suelto(it) }
    }

    val porSitio = entradas.groupBy { claveAgrupacionSitio(it) }
    val vistos = mutableSetOf<String>()
    val itemsPrincipales = mutableListOf<ItemAgrupado>()

    entradas.forEach { entrada ->
        val clave = claveAgrupacionSitio(entrada)
        val delMismoSitio = clave?.let { porSitio[it] }
        if (clave != null && delMismoSitio != null && delMismoSitio.size > 1) {
            if (vistos.add(clave)) {
                val hijosOrdenados = ordenarEntradasInternas(delMismoSitio, criterio)
                itemsPrincipales.add(ItemAgrupado.Grupo(clave, hijosOrdenados))
            }
        } else {
            itemsPrincipales.add(ItemAgrupado.Suelto(entrada))
        }
    }

    val comparadorTopLevel: Comparator<ItemAgrupado> = when (criterio) {
        CriterioOrdenacion.NOMBRE_AZ -> compareByDescending<ItemAgrupado> { item ->
            when (item) {
                is ItemAgrupado.Grupo -> item.entradas.any { it.favorito }
                is ItemAgrupado.Suelto -> item.entrada.favorito
                is ItemAgrupado.Hijo -> false
            }
        }.thenBy { item ->
            when (item) {
                is ItemAgrupado.Grupo -> item.clave.lowercase()
                is ItemAgrupado.Suelto -> item.entrada.titulo.lowercase()
                is ItemAgrupado.Hijo -> ""
            }
        }
        CriterioOrdenacion.NOMBRE_ZA -> compareByDescending<ItemAgrupado> { item ->
            when (item) {
                is ItemAgrupado.Grupo -> item.clave.lowercase()
                is ItemAgrupado.Suelto -> item.entrada.titulo.lowercase()
                is ItemAgrupado.Hijo -> ""
            }
        }
        CriterioOrdenacion.MODIFICACION_RECIENTE -> compareByDescending<ItemAgrupado> { item ->
            when (item) {
                is ItemAgrupado.Grupo -> item.entradas.maxOfOrNull { it.modificadaEn } ?: 0L
                is ItemAgrupado.Suelto -> item.entrada.modificadaEn
                is ItemAgrupado.Hijo -> 0L
            }
        }
        CriterioOrdenacion.CREACION_RECIENTE -> compareByDescending<ItemAgrupado> { item ->
            when (item) {
                is ItemAgrupado.Grupo -> item.entradas.maxOfOrNull { it.creadaEn } ?: 0L
                is ItemAgrupado.Suelto -> item.entrada.creadaEn
                is ItemAgrupado.Hijo -> 0L
            }
        }
        CriterioOrdenacion.ANTIGUEDAD -> compareBy<ItemAgrupado> { item ->
            when (item) {
                is ItemAgrupado.Grupo -> item.entradas.minOfOrNull { it.creadaEn } ?: 0L
                is ItemAgrupado.Suelto -> item.entrada.creadaEn
                is ItemAgrupado.Hijo -> 0L
            }
        }
    }

    val principalesOrdenados = itemsPrincipales.sortedWith(comparadorTopLevel)

    return buildList {
        principalesOrdenados.forEach { item ->
            add(item)
            if (item is ItemAgrupado.Grupo && expandido(item.clave)) {
                item.entradas.forEach { add(ItemAgrupado.Hijo(it)) }
            }
        }
    }
}

private fun ordenarEntradasInternas(entradas: List<Entrada>, criterio: CriterioOrdenacion): List<Entrada> {
    val comp: Comparator<Entrada> = when (criterio) {
        CriterioOrdenacion.NOMBRE_AZ -> compareByDescending<Entrada> { it.favorito }.thenBy { it.titulo.lowercase() }
        CriterioOrdenacion.NOMBRE_ZA -> compareByDescending<Entrada> { it.titulo.lowercase() }
        CriterioOrdenacion.MODIFICACION_RECIENTE -> compareByDescending<Entrada> { it.modificadaEn }
        CriterioOrdenacion.CREACION_RECIENTE -> compareByDescending<Entrada> { it.creadaEn }
        CriterioOrdenacion.ANTIGUEDAD -> compareBy<Entrada> { it.creadaEn }
    }
    return entradas.sortedWith(comp)
}
