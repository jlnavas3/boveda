package com.pepotech.pepoboveda.util

import android.content.Context

/**
 * Comprueba si una contraseña está entre las que más se repiten en filtraciones
 * conocidas. La lista vive dentro del APK (assets/contrasenas_comunes.txt); no se
 * consulta ningún servicio, así que no hace falta ni permiso de red ni conexión.
 */
object ContrasenasComunes {

    @Volatile private var lista: Set<String>? = null

    private fun cargar(contexto: Context): Set<String> {
        lista?.let { return it }
        synchronized(this) {
            lista?.let { return it }
            val leidas = try {
                contexto.applicationContext.assets.open("contrasenas_comunes.txt")
                    .bufferedReader(Charsets.UTF_8)
                    .useLines { lineas -> lineas.map { it.trim().lowercase() }.filter { it.isNotEmpty() }.toHashSet() }
            } catch (e: Exception) {
                emptySet()
            }
            lista = leidas
            return leidas
        }
    }

    fun esComun(contexto: Context, contrasena: String): Boolean {
        if (contrasena.isBlank()) return false
        return cargar(contexto).contains(contrasena.trim().lowercase())
    }
}
