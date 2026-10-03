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
    else -> null
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
        CriterioOrdenacion.USO_RECIENTE -> compareByDescending<ItemAgrupado> { item ->
            when (item) {
                is ItemAgrupado.Grupo -> item.entradas.maxOfOrNull { it.ultimoUsoEn } ?: 0L
                is ItemAgrupado.Suelto -> item.entrada.ultimoUsoEn
                is ItemAgrupado.Hijo -> 0L
            }
        }.thenBy { item ->
            when (item) {
                is ItemAgrupado.Grupo -> item.clave.lowercase()
                is ItemAgrupado.Suelto -> item.entrada.titulo.lowercase()
                is ItemAgrupado.Hijo -> ""
            }
        }
        CriterioOrdenacion.IGNORADAS -> compareByDescending<ItemAgrupado> { item ->
            when (item) {
                is ItemAgrupado.Grupo -> if (item.entradas.any { it.ignoradaEnSalud }) 1 else 0
                is ItemAgrupado.Suelto -> if (item.entrada.ignoradaEnSalud) 1 else 0
                is ItemAgrupado.Hijo -> 0
            }
        }.thenByDescending { item ->
            when (item) {
                is ItemAgrupado.Grupo -> if (item.entradas.any { it.favorito }) 1 else 0
                is ItemAgrupado.Suelto -> if (item.entrada.favorito) 1 else 0
                is ItemAgrupado.Hijo -> 0
            }
        }.thenBy { item ->
            when (item) {
                is ItemAgrupado.Grupo -> item.clave.lowercase()
                is ItemAgrupado.Suelto -> item.entrada.titulo.lowercase()
                is ItemAgrupado.Hijo -> ""
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
        CriterioOrdenacion.USO_RECIENTE -> compareByDescending<Entrada> { it.ultimoUsoEn }.thenBy { it.titulo.lowercase() }
        CriterioOrdenacion.IGNORADAS -> compareByDescending<Entrada> { it.ignoradaEnSalud }.thenByDescending { it.favorito }.thenBy { it.titulo.lowercase() }
    }
    return entradas.sortedWith(comp)
}

/**
 * Agrupa entradas por título (case-insensitive) preservando entradas con títulos equivalentes en un solo grupo.
 */
fun claveAgrupacionPorTitulo(entrada: Entrada): String {
    val t = entrada.titulo.trim()
    if (t.isNotBlank()) return t.lowercase()
    val sitio = claveAgrupacionSitio(entrada)
    if (!sitio.isNullOrBlank()) return sitio.lowercase()
    return "sin título"
}

fun construirItemsAgrupadosPorTitulo(
    entradas: List<Entrada>,
    criterio: CriterioOrdenacion = CriterioOrdenacion.NOMBRE_AZ,
    agrupar: Boolean = true,
    expandido: (String) -> Boolean = { false }
): List<ItemAgrupado> {
    if (!agrupar) {
        return entradas.map { ItemAgrupado.Suelto(it) }
    }

    val porTitulo = entradas.groupBy { claveAgrupacionPorTitulo(it) }
    val vistos = mutableSetOf<String>()
    val itemsPrincipales = mutableListOf<ItemAgrupado>()

    entradas.forEach { entrada ->
        val clave = claveAgrupacionPorTitulo(entrada)
        val delMismoTitulo = porTitulo[clave]
        if (delMismoTitulo != null && delMismoTitulo.size > 1) {
            if (vistos.add(clave)) {
                val tituloVisible = delMismoTitulo.first().titulo.trim().ifBlank { clave }
                val hijosOrdenados = ordenarEntradasInternas(delMismoTitulo, criterio)
                itemsPrincipales.add(ItemAgrupado.Grupo(tituloVisible, hijosOrdenados))
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
        CriterioOrdenacion.USO_RECIENTE -> compareByDescending<ItemAgrupado> { item ->
            when (item) {
                is ItemAgrupado.Grupo -> item.entradas.maxOfOrNull { it.ultimoUsoEn } ?: 0L
                is ItemAgrupado.Suelto -> item.entrada.ultimoUsoEn
                is ItemAgrupado.Hijo -> 0L
            }
        }.thenBy { item ->
            when (item) {
                is ItemAgrupado.Grupo -> item.clave.lowercase()
                is ItemAgrupado.Suelto -> item.entrada.titulo.lowercase()
                is ItemAgrupado.Hijo -> ""
            }
        }
        CriterioOrdenacion.IGNORADAS -> compareByDescending<ItemAgrupado> { item ->
            when (item) {
                is ItemAgrupado.Grupo -> if (item.entradas.any { it.ignoradaEnSalud }) 1 else 0
                is ItemAgrupado.Suelto -> if (item.entrada.ignoradaEnSalud) 1 else 0
                is ItemAgrupado.Hijo -> 0
            }
        }.thenByDescending { item ->
            when (item) {
                is ItemAgrupado.Grupo -> if (item.entradas.any { it.favorito }) 1 else 0
                is ItemAgrupado.Suelto -> if (item.entrada.favorito) 1 else 0
                is ItemAgrupado.Hijo -> 0
            }
        }.thenBy { item ->
            when (item) {
                is ItemAgrupado.Grupo -> item.clave.lowercase()
                is ItemAgrupado.Suelto -> item.entrada.titulo.lowercase()
                is ItemAgrupado.Hijo -> ""
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
