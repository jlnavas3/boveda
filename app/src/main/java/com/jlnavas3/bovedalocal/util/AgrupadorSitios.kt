/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.util

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion

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
    prefijosConfigurados: List<String> = emptyList(),
    plantillaRouter: String = "Router ({ip})",
    plantillaServidor: String = "Servidor ({ip})",
    tldsConfigurados: List<String> = emptyList(),
    marcasPersonalizadas: Map<String, String> = emptyMap(),
    puertosConfigurados: Map<String, String> = emptyMap(),
    octetosRouter: List<Int> = listOf(1, 254),
    expandido: (String) -> Boolean
): List<ItemAgrupado> {
    if (!agrupar) {
        return entradas.map { ItemAgrupado.Suelto(it) }
    }

    val porSitio = entradas.groupBy {
        claveAgrupacionSitio(
            it,
            prefijosConfigurados,
            plantillaRouter,
            plantillaServidor,
            tldsConfigurados,
            marcasPersonalizadas,
            puertosConfigurados,
            octetosRouter
        )
    }
    val vistos = mutableSetOf<String>()
    val itemsPrincipales = mutableListOf<ItemAgrupado>()

    entradas.forEach { entrada ->
        val clave = claveAgrupacionSitio(
            entrada,
            prefijosConfigurados,
            plantillaRouter,
            plantillaServidor,
            tldsConfigurados,
            marcasPersonalizadas,
            puertosConfigurados,
            octetosRouter
        )
        val delMismoSitio = clave?.let { porSitio[it] }
        if (clave != null && delMismoSitio != null && delMismoSitio.size > 1) {
            if (vistos.add(clave)) {
                val hijosOrdenados = ordenarEntradasInternas(delMismoSitio, criterio)
                val tituloGrupo = resolverTituloVisualGrupo(clave, delMismoSitio, tldsConfigurados)
                itemsPrincipales.add(ItemAgrupado.Grupo(tituloGrupo, hijosOrdenados))
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

/**
 * Resuelve el título visible de la cabecera del grupo en la lista principal.
 * Si las entradas aún no han sido normalizadas por el usuario (todas son técnicas/dominios),
 * preserva el dominio o host técnico común (ej. "adobe.com", "discord.com", "activestate.com", "192.168.1.1").
 * Solo si el usuario ha normalizado los títulos (o al menos una entrada tiene nombre amigable),
 * se muestra la marca oficial o nombre limpio normalizado.
 */
private fun resolverTituloVisualGrupo(
    claveNormalizada: String,
    entradas: List<Entrada>,
    tldsConfigurados: List<String>
): String {
    val tieneTituloAmigable = entradas.any { entrada ->
        val tit = entrada.titulo.trim()
        tit.isNotBlank() && !NormalizadorTitulosSitios.esTituloTecnico(tit, entrada.urls, tldsConfigurados)
    }
    if (tieneTituloAmigable) {
        return claveNormalizada
    }

    val dominioRaw = resolverDominioRawGrupo(entradas)
    return dominioRaw ?: claveNormalizada
}

private fun resolverDominioRawGrupo(entradas: List<Entrada>): String? {
    val candidatos = entradas.mapNotNull { entrada ->
        val web = entrada.urls.asSequence()
            .filterNot { it.trim().lowercase().startsWith("android://") }
            .firstOrNull { it.isNotBlank() }
        val target = web ?: entrada.titulo.takeIf { it.isNotBlank() } ?: return@mapNotNull null

        val host = Dominios.host(target)
        if (host.isNotBlank()) {
            if (host.equals("localhost", ignoreCase = true) || host.split('.').all { it.toIntOrNull() != null }) {
                return@mapNotNull host
            }
            val raiz = Dominios.raiz(target)
            if (raiz.isNotBlank()) return@mapNotNull raiz.lowercase()
            return@mapNotNull host.lowercase()
        }
        null
    }
    if (candidatos.isEmpty()) return null
    return candidatos.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
}

