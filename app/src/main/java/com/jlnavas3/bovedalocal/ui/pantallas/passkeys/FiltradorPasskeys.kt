package com.jlnavas3.bovedalocal.ui.pantallas.passkeys

import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion

/**
 * Filtra y ordena una lista de llaves de paso (Passkeys) según los criterios
 * de búsqueda, favoritos y ordenamiento seleccionados.
 */
fun filtrarYOrdenarPasskeys(
    todasLasPasskeys: List<Entrada>,
    textoBusqueda: String,
    soloFavoritos: Boolean,
    criterioOrdenacion: CriterioOrdenacion
): List<Entrada> {
    val q = textoBusqueda.trim().lowercase()
    return todasLasPasskeys
        .filter { entrada ->
            val datos = entrada.passkey ?: return@filter false
            val coincideTexto = if (q.isBlank()) true else {
                entrada.titulo.lowercase().contains(q) ||
                datos.rpName.lowercase().contains(q) ||
                datos.rpId.lowercase().contains(q) ||
                datos.usuario.lowercase().contains(q) ||
                entrada.usuario.lowercase().contains(q)
            }
            val coincideFavorito = if (soloFavoritos) entrada.favorito else true
            coincideTexto && coincideFavorito
        }
        .sortedWith { a, b ->
            val datosA = a.passkey
            val datosB = b.passkey
            val nombreA = datosA?.rpName?.ifBlank { datosA.rpId } ?: a.titulo
            val nombreB = datosB?.rpName?.ifBlank { datosB.rpId } ?: b.titulo
            when (criterioOrdenacion) {
                CriterioOrdenacion.NOMBRE_AZ -> nombreA.compareTo(nombreB, ignoreCase = true)
                CriterioOrdenacion.NOMBRE_ZA -> nombreB.compareTo(nombreA, ignoreCase = true)
                CriterioOrdenacion.MODIFICACION_RECIENTE -> b.modificadaEn.compareTo(a.modificadaEn)
                CriterioOrdenacion.ANTIGUEDAD -> a.creadaEn.compareTo(b.creadaEn)
                CriterioOrdenacion.CREACION_RECIENTE -> b.creadaEn.compareTo(a.creadaEn)
                CriterioOrdenacion.USO_RECIENTE -> b.ultimoUsoEn.compareTo(a.ultimoUsoEn)
                CriterioOrdenacion.IGNORADAS -> b.ignoradaEnSalud.compareTo(a.ignoradaEnSalud)
            }
        }
}
