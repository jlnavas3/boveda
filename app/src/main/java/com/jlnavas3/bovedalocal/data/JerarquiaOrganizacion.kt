package com.jlnavas3.bovedalocal.data

import kotlinx.serialization.Serializable

/**
 * Define la jerarquia y relacion de anidacion entre Identidades y Colecciones en la lista principal:
 * - [IDENTIDAD_SOBRE_COLECCION]: La Identidad es el nivel superior (perfil personal). Las colecciones se subordinan a la identidad activa.
 * - [COLECCION_SOBRE_IDENTIDAD]: La Coleccion es el nivel superior (carpeta/proyecto tematico). Las identidades se subordinan a la coleccion activa.
 */
@Serializable
enum class JerarquiaOrganizacion(val clave: String, val etiqueta: String, val descripcion: String) {
    IDENTIDAD_SOBRE_COLECCION(
        clave = "identidad_sobre_coleccion",
        etiqueta = "Identidades sobre Colecciones (Recomendado)",
        descripcion = "La Identidad define el contexto superior de perfil. Las colecciones se subordinan o filtran según la identidad activa."
    ),
    COLECCION_SOBRE_IDENTIDAD(
        clave = "coleccion_sobre_identidad",
        etiqueta = "Colecciones sobre Identidades",
        descripcion = "La Colección define el contexto superior temático. Las identidades se subordinan o filtran según la colección activa."
    );

    companion object {
        fun desdeClave(clave: String?): JerarquiaOrganizacion =
            entries.firstOrNull { it.clave == clave } ?: IDENTIDAD_SOBRE_COLECCION
    }
}
