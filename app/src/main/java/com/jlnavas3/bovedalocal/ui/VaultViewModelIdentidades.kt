package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.ModoVisualizacionIdentidades
import com.jlnavas3.bovedalocal.data.VaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface VaultIdentidadesDelegate {
    val repositorio: VaultRepository
    val filtroIdentidadInterno: MutableStateFlow<String?>
    fun avisar(texto: String)
    fun ejecutar(bloque: suspend () -> Unit)

    val identidadSeleccionadaId: StateFlow<String?> get() = filtroIdentidadInterno

    fun seleccionarIdentidad(id: String?) {
        filtroIdentidadInterno.value = id
    }

    fun crearIdentidad(
        nombre: String,
        correoPrincipal: String,
        correosSecundarios: List<String> = emptyList(),
        colorHex: String? = null,
        icono: String = "person"
    ): Identidad {
        val iden = repositorio.crearIdentidad(nombre, correoPrincipal, correosSecundarios, colorHex, icono)
        avisar("Identidad '${iden.nombre}' creada")
        return iden
    }

    fun actualizarIdentidad(
        id: String,
        nombre: String,
        correoPrincipal: String,
        correosSecundarios: List<String> = emptyList(),
        colorHex: String? = null,
        icono: String = "person"
    ) {
        repositorio.actualizarIdentidad(id, nombre, correoPrincipal, correosSecundarios, colorHex, icono)
        avisar("Identidad actualizada")
    }

    fun eliminarIdentidad(id: String) {
        if (filtroIdentidadInterno.value == id) {
            filtroIdentidadInterno.value = null
        }
        repositorio.eliminarIdentidad(id)
        avisar("Identidad eliminada")
    }

    fun cambiarModoVisualizacionIdentidades(modo: ModoVisualizacionIdentidades) {
        ejecutar {
            repositorio.ajustes.actualizar { it.copy(modoIdentidades = modo.clave) }
        }
    }
}
