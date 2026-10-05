package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.data.VaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface VaultCategoriasDelegate {
    val repositorio: VaultRepository
    val filtroCategoriaInterno: MutableStateFlow<String?>
    fun avisar(texto: String)
    fun ejecutar(bloque: suspend () -> Unit)

    val categoriaSeleccionadaId: StateFlow<String?> get() = filtroCategoriaInterno

    fun seleccionarCategoria(id: String?) {
        filtroCategoriaInterno.value = id
    }

    fun crearCategoria(nombre: String, icono: String = "carpeta", colorHex: String? = null): Categoria {
        val cat = repositorio.crearCategoria(nombre, icono, colorHex)
        avisar("Categoría '${cat.nombre}' creada")
        return cat
    }

    fun actualizarCategoria(id: String, nombre: String, icono: String, colorHex: String?) {
        repositorio.actualizarCategoria(id, nombre, icono, colorHex)
        avisar("Categoría actualizada")
    }

    fun eliminarCategoria(id: String) {
        if (filtroCategoriaInterno.value == id) {
            filtroCategoriaInterno.value = null
        }
        repositorio.eliminarCategoria(id)
        avisar("Categoría eliminada")
    }

    fun asignarCategoriasAEntradas(
        idsEntradas: Set<String>,
        idsAgregar: Set<String>,
        idsQuitar: Set<String>
    ) {
        repositorio.asignarCategoriasAEntradas(idsEntradas, idsAgregar, idsQuitar)
        val cant = idsEntradas.size
        avisar(if (cant == 1) "Categoría actualizada" else "$cant entradas actualizadas")
    }

    // --- Alias retrocompatibles ---
    val filtroColeccionInterno: MutableStateFlow<String?> get() = filtroCategoriaInterno
    val coleccionSeleccionadaId: StateFlow<String?> get() = categoriaSeleccionadaId
    fun seleccionarColeccion(id: String?) = seleccionarCategoria(id)
    fun crearColeccion(nombre: String, icono: String = "carpeta", colorHex: String? = null): Coleccion =
        crearCategoria(nombre, icono, colorHex)
    fun actualizarColeccion(id: String, nombre: String, icono: String, colorHex: String?) =
        actualizarCategoria(id, nombre, icono, colorHex)
    fun eliminarColeccion(id: String) = eliminarCategoria(id)
    fun asignarColeccionesAEntradas(
        idsEntradas: Set<String>,
        idsAgregar: Set<String>,
        idsQuitar: Set<String>
    ) = asignarCategoriasAEntradas(idsEntradas, idsAgregar, idsQuitar)
}

typealias VaultColeccionesDelegate = VaultCategoriasDelegate
