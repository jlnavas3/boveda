package com.jlnavas3.bovedalocal.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.Identidad
import com.jlnavas3.bovedalocal.data.JerarquiaOrganizacion
import com.jlnavas3.bovedalocal.ui.pantallas.categorias.IconosCategorias
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

/**
 * Construye la lista aplanada para LazyColumn con estructura jerárquica de 2 niveles:
 * - Nivel 1: Sección principal (Identidad o Categoría según la jerarquía elegida)
 * - Nivel 2: Subsección subordinada anidada
 * - Hojas: Entradas individuales
 */
fun construirItemsAgrupadosJerarquicos(
    entradas: List<Entrada>,
    identidades: List<Identidad>,
    categorias: List<Categoria>,
    jerarquia: JerarquiaOrganizacion,
    identidadesActivas: Boolean,
    estaExpandido: (clave: String) -> Boolean
): List<ItemAgrupadoJerarquico> {
    if (entradas.isEmpty()) return emptyList()

    val resultado = mutableListOf<ItemAgrupadoJerarquico>()

    // CASO 1: Identidades desactivadas -> Agrupamiento de nivel único por Categorías
    if (!identidadesActivas || identidades.isEmpty()) {
        val mapaCategorias = categorias.associateBy { it.id }
        val agrupadasPorCat = mutableMapOf<String?, MutableList<Entrada>>()
        entradas.forEach { e ->
            if (e.categorias.isEmpty()) {
                agrupadasPorCat.getOrPut(null) { mutableListOf() }.add(e)
            } else {
                e.categorias.forEach { catId ->
                    agrupadasPorCat.getOrPut(catId) { mutableListOf() }.add(e)
                }
            }
        }

        categorias.forEach { cat ->
            val lista = agrupadasPorCat[cat.id] ?: return@forEach
            val clave = "categoria-${cat.id}"
            val expandido = estaExpandido(clave)
            val ids = lista.map { it.id }.toSet()
            val colorBase = parsearColorO(cat.colorHex ?: "", ColorAcento)
            val icono = IconosCategorias.obtenerIcono(cat.icono)

            resultado.add(
                ItemAgrupadoJerarquico.CabeceraPrincipal(
                    claveGrupo = clave,
                    titulo = cat.nombre,
                    totalEntradas = lista.size,
                    color = colorBase,
                    icono = icono,
                    expandido = expandido,
                    idsEntradas = ids
                )
            )

            if (expandido) {
                lista.forEachIndexed { idx, ent ->
                    resultado.add(
                        ItemAgrupadoJerarquico.EntradaHoja(
                            entrada = ent,
                            esUltimaEnSubseccion = idx == lista.lastIndex,
                            clavePadre = clave
                        )
                    )
                }
            }
        }

        agrupadasPorCat[null]?.let { sinCat ->
            if (sinCat.isNotEmpty()) {
                val clave = "categoria-__SIN_CATEGORIA__"
                val expandido = estaExpandido(clave)
                resultado.add(
                    ItemAgrupadoJerarquico.CabeceraPrincipal(
                        claveGrupo = clave,
                        titulo = "Sin categoría",
                        totalEntradas = sinCat.size,
                        color = TextoSecundario,
                        icono = Icons.Filled.Security,
                        expandido = expandido,
                        idsEntradas = sinCat.map { it.id }.toSet()
                    )
                )
                if (expandido) {
                    sinCat.forEachIndexed { idx, ent ->
                        resultado.add(
                            ItemAgrupadoJerarquico.EntradaHoja(
                                entrada = ent,
                                esUltimaEnSubseccion = idx == sinCat.lastIndex,
                                clavePadre = clave
                            )
                        )
                    }
                }
            }
        }
        return resultado
    }

    // CASO 2: Jerarquía IDENTIDADES SOBRE CATEGORÍAS (Predeterminada)
    if (jerarquia == JerarquiaOrganizacion.IDENTIDAD_SOBRE_CATEGORIA) {
        val agrupadasPorIden = mutableMapOf<String?, MutableList<Entrada>>()
        entradas.forEach { e ->
            val iden = resolverIdentidadParaEntrada(e, identidades)
            agrupadasPorIden.getOrPut(iden?.id) { mutableListOf() }.add(e)
        }

        identidades.forEach { iden ->
            val entradasIden = agrupadasPorIden[iden.id] ?: return@forEach
            val claveNivel1 = "identidad-${iden.id}"
            val expandidoNivel1 = estaExpandido(claveNivel1)
            val colorBase = parsearColorO(iden.colorHex ?: "", ColorAcento)
            val idsIden = entradasIden.map { it.id }.toSet()

            resultado.add(
                ItemAgrupadoJerarquico.CabeceraPrincipal(
                    claveGrupo = claveNivel1,
                    titulo = iden.nombre,
                    totalEntradas = entradasIden.size,
                    color = colorBase,
                    icono = Icons.Filled.Person,
                    expandido = expandidoNivel1,
                    idsEntradas = idsIden
                )
            )

            if (expandidoNivel1) {
                // Subdividir por categorías
                val agrupadasCat = mutableMapOf<String?, MutableList<Entrada>>()
                entradasIden.forEach { e ->
                    if (e.categorias.isEmpty()) {
                        agrupadasCat.getOrPut(null) { mutableListOf() }.add(e)
                    } else {
                        e.categorias.forEach { catId ->
                            agrupadasCat.getOrPut(catId) { mutableListOf() }.add(e)
                        }
                    }
                }

                categorias.forEach { cat ->
                    val listaSub = agrupadasCat[cat.id] ?: return@forEach
                    val claveNivel2 = "subcat-${iden.id}-${cat.id}"
                    val expandidoNivel2 = estaExpandido(claveNivel2)
                    val colorCat = parsearColorO(cat.colorHex ?: "", ColorAcento)
                    val iconoCat = IconosCategorias.obtenerIcono(cat.icono)

                    resultado.add(
                        ItemAgrupadoJerarquico.Subcabecera(
                            claveGrupo = claveNivel2,
                            titulo = cat.nombre,
                            totalEntradas = listaSub.size,
                            color = colorCat,
                            icono = iconoCat,
                            expandido = expandidoNivel2,
                            idsEntradas = listaSub.map { it.id }.toSet()
                        )
                    )

                    if (expandidoNivel2) {
                        listaSub.forEachIndexed { idx, ent ->
                            resultado.add(
                                ItemAgrupadoJerarquico.EntradaHoja(
                                    entrada = ent,
                                    esUltimaEnSubseccion = idx == listaSub.lastIndex,
                                    clavePadre = claveNivel2
                                )
                            )
                        }
                    }
                }

                agrupadasCat[null]?.let { sinCat ->
                    if (sinCat.isNotEmpty()) {
                        val claveNivel2 = "subcat-${iden.id}-__SIN_CAT__"
                        val expandidoNivel2 = estaExpandido(claveNivel2)
                        resultado.add(
                            ItemAgrupadoJerarquico.Subcabecera(
                                claveGrupo = claveNivel2,
                                titulo = "Sin categoría",
                                totalEntradas = sinCat.size,
                                color = TextoSecundario,
                                icono = Icons.Filled.Security,
                                expandido = expandidoNivel2,
                                idsEntradas = sinCat.map { it.id }.toSet()
                            )
                        )

                        if (expandidoNivel2) {
                            sinCat.forEachIndexed { idx, ent ->
                                resultado.add(
                                    ItemAgrupadoJerarquico.EntradaHoja(
                                        entrada = ent,
                                        esUltimaEnSubseccion = idx == sinCat.lastIndex,
                                        clavePadre = claveNivel2
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Entradas "Sin identidad asignada"
        agrupadasPorIden[null]?.let { sinIden ->
            if (sinIden.isNotEmpty()) {
                val claveNivel1 = "identidad-__SIN_IDENTIDAD__"
                val expandidoNivel1 = estaExpandido(claveNivel1)
                resultado.add(
                    ItemAgrupadoJerarquico.CabeceraPrincipal(
                        claveGrupo = claveNivel1,
                        titulo = "Sin identidad asignada",
                        totalEntradas = sinIden.size,
                        color = TextoSecundario,
                        icono = Icons.Filled.Person,
                        expandido = expandidoNivel1,
                        idsEntradas = sinIden.map { it.id }.toSet()
                    )
                )

                if (expandidoNivel1) {
                    val agrupadasCat = mutableMapOf<String?, MutableList<Entrada>>()
                    sinIden.forEach { e ->
                        if (e.categorias.isEmpty()) {
                            agrupadasCat.getOrPut(null) { mutableListOf() }.add(e)
                        } else {
                            e.categorias.forEach { catId ->
                                agrupadasCat.getOrPut(catId) { mutableListOf() }.add(e)
                            }
                        }
                    }

                    categorias.forEach { cat ->
                        val listaSub = agrupadasCat[cat.id] ?: return@forEach
                        val claveNivel2 = "subcat-sin-iden-${cat.id}"
                        val expandidoNivel2 = estaExpandido(claveNivel2)
                        val colorCat = parsearColorO(cat.colorHex ?: "", ColorAcento)
                        val iconoCat = IconosCategorias.obtenerIcono(cat.icono)

                        resultado.add(
                            ItemAgrupadoJerarquico.Subcabecera(
                                claveGrupo = claveNivel2,
                                titulo = cat.nombre,
                                totalEntradas = listaSub.size,
                                color = colorCat,
                                icono = iconoCat,
                                expandido = expandidoNivel2,
                                idsEntradas = listaSub.map { it.id }.toSet()
                            )
                        )

                        if (expandidoNivel2) {
                            listaSub.forEachIndexed { idx, ent ->
                                resultado.add(
                                    ItemAgrupadoJerarquico.EntradaHoja(
                                        entrada = ent,
                                        esUltimaEnSubseccion = idx == listaSub.lastIndex,
                                        clavePadre = claveNivel2
                                    )
                                )
                            }
                        }
                    }

                    agrupadasCat[null]?.let { sinCat ->
                        if (sinCat.isNotEmpty()) {
                            val claveNivel2 = "subcat-sin-iden-__SIN_CAT__"
                            val expandidoNivel2 = estaExpandido(claveNivel2)
                            resultado.add(
                                ItemAgrupadoJerarquico.Subcabecera(
                                    claveGrupo = claveNivel2,
                                    titulo = "Sin categoría",
                                    totalEntradas = sinCat.size,
                                    color = TextoSecundario,
                                    icono = Icons.Filled.Security,
                                    expandido = expandidoNivel2,
                                    idsEntradas = sinCat.map { it.id }.toSet()
                                )
                            )

                            if (expandidoNivel2) {
                                sinCat.forEachIndexed { idx, ent ->
                                    resultado.add(
                                        ItemAgrupadoJerarquico.EntradaHoja(
                                            entrada = ent,
                                            esUltimaEnSubseccion = idx == sinCat.lastIndex,
                                            clavePadre = claveNivel2
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        return resultado
    }

    // CASO 3: Jerarquía CATEGORÍAS SOBRE IDENTIDADES
    val agrupadasPorCat = mutableMapOf<String?, MutableList<Entrada>>()
    entradas.forEach { e ->
        if (e.categorias.isEmpty()) {
            agrupadasPorCat.getOrPut(null) { mutableListOf() }.add(e)
        } else {
            e.categorias.forEach { catId ->
                agrupadasPorCat.getOrPut(catId) { mutableListOf() }.add(e)
            }
        }
    }

    categorias.forEach { cat ->
        val listaCat = agrupadasPorCat[cat.id] ?: return@forEach
        val claveNivel1 = "categoria-${cat.id}"
        val expandidoNivel1 = estaExpandido(claveNivel1)
        val colorCat = parsearColorO(cat.colorHex ?: "", ColorAcento)
        val iconoCat = IconosCategorias.obtenerIcono(cat.icono)

        resultado.add(
            ItemAgrupadoJerarquico.CabeceraPrincipal(
                claveGrupo = claveNivel1,
                titulo = cat.nombre,
                totalEntradas = listaCat.size,
                color = colorCat,
                icono = iconoCat,
                expandido = expandidoNivel1,
                idsEntradas = listaCat.map { it.id }.toSet()
            )
        )

        if (expandidoNivel1) {
            val agrupadasIden = mutableMapOf<String?, MutableList<Entrada>>()
            listaCat.forEach { e ->
                val iden = resolverIdentidadParaEntrada(e, identidades)
                agrupadasIden.getOrPut(iden?.id) { mutableListOf() }.add(e)
            }

            identidades.forEach { iden ->
                val listaSub = agrupadasIden[iden.id] ?: return@forEach
                val claveNivel2 = "subiden-${cat.id}-${iden.id}"
                val expandidoNivel2 = estaExpandido(claveNivel2)
                val colorIden = parsearColorO(iden.colorHex ?: "", ColorAcento)

                resultado.add(
                    ItemAgrupadoJerarquico.Subcabecera(
                        claveGrupo = claveNivel2,
                        titulo = iden.nombre,
                        totalEntradas = listaSub.size,
                        color = colorIden,
                        icono = Icons.Filled.Person,
                        expandido = expandidoNivel2,
                        idsEntradas = listaSub.map { it.id }.toSet()
                    )
                )

                if (expandidoNivel2) {
                    listaSub.forEachIndexed { idx, ent ->
                        resultado.add(
                            ItemAgrupadoJerarquico.EntradaHoja(
                                entrada = ent,
                                esUltimaEnSubseccion = idx == listaSub.lastIndex,
                                clavePadre = claveNivel2
                            )
                        )
                    }
                }
            }

            agrupadasIden[null]?.let { sinIden ->
                if (sinIden.isNotEmpty()) {
                    val claveNivel2 = "subiden-${cat.id}-__SIN_IDEN__"
                    val expandidoNivel2 = estaExpandido(claveNivel2)
                    resultado.add(
                        ItemAgrupadoJerarquico.Subcabecera(
                            claveGrupo = claveNivel2,
                            titulo = "Sin identidad",
                            totalEntradas = sinIden.size,
                            color = TextoSecundario,
                            icono = Icons.Filled.Person,
                            expandido = expandidoNivel2,
                            idsEntradas = sinIden.map { it.id }.toSet()
                        )
                    )

                    if (expandidoNivel2) {
                        sinIden.forEachIndexed { idx, ent ->
                            resultado.add(
                                ItemAgrupadoJerarquico.EntradaHoja(
                                    entrada = ent,
                                    esUltimaEnSubseccion = idx == sinIden.lastIndex,
                                    clavePadre = claveNivel2
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    agrupadasPorCat[null]?.let { sinCat ->
        if (sinCat.isNotEmpty()) {
            val claveNivel1 = "categoria-__SIN_CATEGORIA__"
            val expandidoNivel1 = estaExpandido(claveNivel1)
            resultado.add(
                ItemAgrupadoJerarquico.CabeceraPrincipal(
                    claveGrupo = claveNivel1,
                    titulo = "Sin categoría",
                    totalEntradas = sinCat.size,
                    color = TextoSecundario,
                    icono = Icons.Filled.Security,
                    expandido = expandidoNivel1,
                    idsEntradas = sinCat.map { it.id }.toSet()
                )
            )

            if (expandidoNivel1) {
                val agrupadasIden = mutableMapOf<String?, MutableList<Entrada>>()
                sinCat.forEach { e ->
                    val iden = resolverIdentidadParaEntrada(e, identidades)
                    agrupadasIden.getOrPut(iden?.id) { mutableListOf() }.add(e)
                }

                identidades.forEach { iden ->
                    val listaSub = agrupadasIden[iden.id] ?: return@forEach
                    val claveNivel2 = "subiden-sin-cat-${iden.id}"
                    val expandidoNivel2 = estaExpandido(claveNivel2)
                    val colorIden = parsearColorO(iden.colorHex ?: "", ColorAcento)

                    resultado.add(
                        ItemAgrupadoJerarquico.Subcabecera(
                            claveGrupo = claveNivel2,
                            titulo = iden.nombre,
                            totalEntradas = listaSub.size,
                            color = colorIden,
                            icono = Icons.Filled.Person,
                            expandido = expandidoNivel2,
                            idsEntradas = listaSub.map { it.id }.toSet()
                        )
                    )

                    if (expandidoNivel2) {
                        listaSub.forEachIndexed { idx, ent ->
                            resultado.add(
                                ItemAgrupadoJerarquico.EntradaHoja(
                                    entrada = ent,
                                    esUltimaEnSubseccion = idx == listaSub.lastIndex,
                                    clavePadre = claveNivel2
                                )
                            )
                        }
                    }
                }

                agrupadasIden[null]?.let { sinIden ->
                    if (sinIden.isNotEmpty()) {
                        val claveNivel2 = "subiden-sin-cat-__SIN_IDEN__"
                        val expandidoNivel2 = estaExpandido(claveNivel2)
                        resultado.add(
                            ItemAgrupadoJerarquico.Subcabecera(
                                claveGrupo = claveNivel2,
                                titulo = "Sin identidad",
                                totalEntradas = sinIden.size,
                                color = TextoSecundario,
                                icono = Icons.Filled.Person,
                                expandido = expandidoNivel2,
                                idsEntradas = sinIden.map { it.id }.toSet()
                            )
                        )

                        if (expandidoNivel2) {
                            sinIden.forEachIndexed { idx, ent ->
                                resultado.add(
                                    ItemAgrupadoJerarquico.EntradaHoja(
                                        entrada = ent,
                                        esUltimaEnSubseccion = idx == sinIden.lastIndex,
                                        clavePadre = claveNivel2
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    return resultado
}
