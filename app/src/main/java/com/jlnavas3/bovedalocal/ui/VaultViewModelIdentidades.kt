/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui

import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.ModoVisualizacionIdentidades
import com.jlnavas3.bovedalocal.data.Perfil
import com.jlnavas3.bovedalocal.data.VaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface VaultIdentidadesDelegate {
    val repositorio: VaultRepository
    val filtroIdentidadInterno: MutableStateFlow<String?>
    fun avisar(texto: String)
    fun ejecutar(bloque: suspend () -> Unit)

    val identidadSeleccionadaId: StateFlow<String?> get() = filtroIdentidadInterno
    val perfilSeleccionadoId: StateFlow<String?> get() = filtroIdentidadInterno

    fun seleccionarIdentidad(id: String?) {
        filtroIdentidadInterno.value = id
    }

    fun seleccionarPerfil(id: String?) = seleccionarIdentidad(id)

    fun crearIdentidad(
        nombre: String,
        correoPrincipal: String = "",
        correosSecundarios: List<String> = emptyList(),
        tipoDocumento: String? = null,
        numeroDocumento: String? = null,
        preferenciaIdentificador: String = com.jlnavas3.bovedalocal.data.PreferenciaIdentificadorIdentidad.CORREO,
        colorHex: String? = null,
        icono: String = "person"
    ): Identidad {
        val iden = repositorio.crearIdentidad(
            nombre = nombre,
            correoPrincipal = correoPrincipal,
            correosSecundarios = correosSecundarios,
            tipoDocumento = tipoDocumento,
            numeroDocumento = numeroDocumento,
            preferenciaIdentificador = preferenciaIdentificador,
            colorHex = colorHex,
            icono = icono
        )
        avisar("Perfil '${iden.nombre}' creado")
        return iden
    }

    fun crearPerfil(
        nombre: String,
        correoPrincipal: String = "",
        correosSecundarios: List<String> = emptyList(),
        tipoDocumento: String? = null,
        numeroDocumento: String? = null,
        preferenciaIdentificador: String = com.jlnavas3.bovedalocal.data.PreferenciaIdentificadorPerfil.CORREO,
        colorHex: String? = null,
        icono: String = "person"
    ) = crearIdentidad(nombre, correoPrincipal, correosSecundarios, tipoDocumento, numeroDocumento, preferenciaIdentificador, colorHex, icono)

    fun guardarPerfil(perfil: Perfil): Perfil {
        val guardado = repositorio.guardarPerfil(perfil)
        avisar("Perfil '${guardado.nombre}' guardado")
        return guardado
    }

    fun actualizarIdentidad(
        id: String,
        nombre: String,
        correoPrincipal: String = "",
        correosSecundarios: List<String> = emptyList(),
        tipoDocumento: String? = null,
        numeroDocumento: String? = null,
        preferenciaIdentificador: String = com.jlnavas3.bovedalocal.data.PreferenciaIdentificadorIdentidad.CORREO,
        colorHex: String? = null,
        icono: String = "person"
    ) {
        repositorio.actualizarIdentidad(
            id = id,
            nombre = nombre,
            correoPrincipal = correoPrincipal,
            correosSecundarios = correosSecundarios,
            tipoDocumento = tipoDocumento,
            numeroDocumento = numeroDocumento,
            preferenciaIdentificador = preferenciaIdentificador,
            colorHex = colorHex,
            icono = icono
        )
        avisar("Perfil actualizado")
    }

    fun actualizarPerfil(
        id: String,
        nombre: String,
        correoPrincipal: String = "",
        correosSecundarios: List<String> = emptyList(),
        tipoDocumento: String? = null,
        numeroDocumento: String? = null,
        preferenciaIdentificador: String = com.jlnavas3.bovedalocal.data.PreferenciaIdentificadorPerfil.CORREO,
        colorHex: String? = null,
        icono: String = "person"
    ) = actualizarIdentidad(id, nombre, correoPrincipal, correosSecundarios, tipoDocumento, numeroDocumento, preferenciaIdentificador, colorHex, icono)

    fun eliminarIdentidad(id: String) {
        if (filtroIdentidadInterno.value == id) {
            filtroIdentidadInterno.value = null
        }
        repositorio.eliminarIdentidad(id)
        avisar("Perfil eliminado")
    }

    fun eliminarPerfil(id: String) = eliminarIdentidad(id)

    fun cambiarModoVisualizacionIdentidades(modo: ModoVisualizacionIdentidades) {
        ejecutar {
            repositorio.ajustes.actualizar { it.copy(modoIdentidades = modo.clave) }
        }
    }

    fun cambiarModoVisualizacionPerfiles(modo: com.jlnavas3.bovedalocal.data.ModoVisualizacionPerfiles) = cambiarModoVisualizacionIdentidades(modo)
}
