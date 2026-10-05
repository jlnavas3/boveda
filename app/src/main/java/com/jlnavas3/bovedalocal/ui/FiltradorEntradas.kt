package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * Motor de filtrado y ordenación multi-criterio para las entradas de la bóveda.
 */
object FiltradorEntradas {

    fun filtrarYOrdenar(
        entradas: List<Entrada>,
        busqueda: String = "",
        filtroTipo: TipoEntrada? = null,
        soloFavoritos: Boolean = false,
        filtroEtiqueta: String? = null,
        filtroColeccion: String? = null,
        criterioOrdenacion: CriterioOrdenacion = CriterioOrdenacion.NOMBRE_AZ,
        filtroIdentidad: String? = null,
        identidades: List<com.jlnavas3.bovedalocal.data.Identidad> = emptyList()
    ): List<Entrada> {
        val texto = busqueda.trim().lowercase()
        val filtradas = entradas.filter { entrada ->
            val coincideIdentidad = when (filtroIdentidad) {
                null -> true
                "__SIN_IDENTIDAD__" -> com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada(entrada, identidades) == null
                else -> com.jlnavas3.bovedalocal.util.resolverIdentidadParaEntrada(entrada, identidades)?.id == filtroIdentidad
            }
            coincideIdentidad &&
                (filtroTipo == null || entrada.tipo == filtroTipo) &&
                (!soloFavoritos || entrada.favorito) &&
                (filtroEtiqueta == null || entrada.etiquetas.contains(filtroEtiqueta)) &&
                (filtroColeccion == null || entrada.colecciones.contains(filtroColeccion)) &&
                (texto.isEmpty() ||
                    entrada.titulo.lowercase().contains(texto) ||
                    entrada.usuario.lowercase().contains(texto) ||
                    entrada.urls.any { it.lowercase().contains(texto) } ||
                    entrada.etiquetas.any { it.lowercase().contains(texto) })
        }

        val comparador: Comparator<Entrada> = when (criterioOrdenacion) {
            CriterioOrdenacion.NOMBRE_AZ -> compareByDescending<Entrada> { it.favorito }.thenBy { it.titulo.lowercase() }
            CriterioOrdenacion.NOMBRE_ZA -> compareByDescending<Entrada> { it.titulo.lowercase() }.thenByDescending { it.favorito }
            CriterioOrdenacion.MODIFICACION_RECIENTE -> compareByDescending<Entrada> { it.modificadaEn }.thenByDescending { it.favorito }
            CriterioOrdenacion.CREACION_RECIENTE -> compareByDescending<Entrada> { it.creadaEn }.thenByDescending { it.favorito }
            CriterioOrdenacion.ANTIGUEDAD -> compareBy<Entrada> { it.creadaEn }.thenByDescending { it.favorito }
            CriterioOrdenacion.USO_RECIENTE -> compareByDescending<Entrada> { it.ultimoUsoEn }
                .thenByDescending { it.favorito }
                .thenBy { it.titulo.lowercase() }
            CriterioOrdenacion.IGNORADAS -> compareByDescending<Entrada> { it.ignoradaEnSalud }
                .thenByDescending { it.favorito }
                .thenBy { it.titulo.lowercase() }
        }
        return filtradas.sortedWith(comparador)
    }
}
