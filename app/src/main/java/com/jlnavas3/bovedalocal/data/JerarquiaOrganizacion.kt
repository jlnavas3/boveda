package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable

/**
 * Define la jerarquia y relacion de anidacion entre Identidades y Categorias en la lista principal:
 * - [IDENTIDAD_SOBRE_CATEGORIA]: La Identidad es el nivel superior (perfil personal). Las categorias se subordinan a la identidad activa.
 * - [CATEGORIA_SOBRE_IDENTIDAD]: La Categoria es el nivel superior (carpeta/proyecto tematico). Las identidades se subordinan a la categoria activa.
 */
@Serializable
enum class JerarquiaOrganizacion(val clave: String, val etiqueta: String, val descripcion: String) {
    IDENTIDAD_SOBRE_CATEGORIA(
        clave = "identidad_sobre_categoria",
        etiqueta = "Identidades sobre Categorías (Recomendado)",
        descripcion = "La Identidad define el contexto superior de perfil. Las categorías se subordinan o filtran según la identidad activa."
    ),
    CATEGORIA_SOBRE_IDENTIDAD(
        clave = "categoria_sobre_identidad",
        etiqueta = "Categorías sobre Identidades",
        descripcion = "La Categoría define el contexto superior temático. Las identidades se subordinan o filtran según la categoría activa."
    );

    companion object {
        // Alias retrocompatibles
        val IDENTIDAD_SOBRE_COLECCION get() = IDENTIDAD_SOBRE_CATEGORIA
        val COLECCION_SOBRE_IDENTIDAD get() = CATEGORIA_SOBRE_IDENTIDAD

        fun desdeClave(clave: String?): JerarquiaOrganizacion = when (clave) {
            "categoria_sobre_identidad", "coleccion_sobre_identidad" -> CATEGORIA_SOBRE_IDENTIDAD
            else -> IDENTIDAD_SOBRE_CATEGORIA
        }
    }
}
