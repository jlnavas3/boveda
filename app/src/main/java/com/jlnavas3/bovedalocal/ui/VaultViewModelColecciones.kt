package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.Coleccion
import com.jlnavas3.bovedalocal.data.VaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface VaultColeccionesDelegate {
    val repositorio: VaultRepository
    val filtroColeccionInterno: MutableStateFlow<String?>
    fun avisar(texto: String)
    fun ejecutar(bloque: suspend () -> Unit)

    val coleccionSeleccionadaId: StateFlow<String?> get() = filtroColeccionInterno

    fun seleccionarColeccion(id: String?) {
        filtroColeccionInterno.value = id
    }

    fun crearColeccion(nombre: String, icono: String = "carpeta", colorHex: String? = null): Coleccion {
        val col = repositorio.crearColeccion(nombre, icono, colorHex)
        avisar("Colección '${col.nombre}' creada")
        return col
    }

    fun actualizarColeccion(id: String, nombre: String, icono: String, colorHex: String?) {
        repositorio.actualizarColeccion(id, nombre, icono, colorHex)
        avisar("Colección actualizada")
    }

    fun eliminarColeccion(id: String) {
        if (filtroColeccionInterno.value == id) {
            filtroColeccionInterno.value = null
        }
        repositorio.eliminarColeccion(id)
        avisar("Colección eliminada")
    }

    fun asignarColeccionesAEntradas(
        idsEntradas: Set<String>,
        idsAgregar: Set<String>,
        idsQuitar: Set<String>
    ) {
        repositorio.asignarColeccionesAEntradas(idsEntradas, idsAgregar, idsQuitar)
        val cant = idsEntradas.size
        avisar(if (cant == 1) "Colección actualizada" else "$cant entradas actualizadas")
    }
}
