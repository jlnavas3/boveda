package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion

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
