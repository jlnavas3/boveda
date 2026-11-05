/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wifi
import com.jlnavas3.bovedalocal.data.Categoria
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * Función pura encargada de resolver las recomendaciones de comandos de búsqueda
 * de forma contextual e inteligente. Solo devuelve sugerencias que realmente
 * arrojen resultados basados en los datos actuales de la bóveda.
 */
fun resolverSugerenciasBusquedaInteligentes(
    entradas: List<Entrada>,
    categorias: List<Categoria>,
    etiquetasDisponibles: List<String>,
    textoBusqueda: String
): List<ItemSugerenciaBusqueda> {
    if (entradas.isEmpty()) return emptyList()

    val conteoPorTipo = entradas.groupingBy { it.tipo }.eachCount()
    val tieneTotp = entradas.any { !it.secretoTotp.isNullOrBlank() }
    val tienePasskey = entradas.any { it.passkey != null || it.tipo == TipoEntrada.PASSKEY }
    val tieneNotas = entradas.any { it.notas.isNotBlank() }
    val tieneFavoritos = entradas.any { it.favorito }
    val tieneIgnoradas = entradas.any { it.ignoradaEnSalud }

    // Catálogo base de sugerencias activables solo si existen datos
    val sugerenciasDisponibles = mutableListOf<ItemSugerenciaBusqueda>()

    // Tipos de entrada existentes
    if ((conteoPorTipo[TipoEntrada.LOGIN] ?: 0) > 0) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("t:login", "Contraseñas", Icons.Filled.Lock, "Tipos"))
    }
    if ((conteoPorTipo[TipoEntrada.TARJETA] ?: 0) > 0) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("t:tarjeta", "Tarjetas", Icons.Filled.CreditCard, "Tipos"))
    }
    if ((conteoPorTipo[TipoEntrada.WIFI] ?: 0) > 0) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("t:wifi", "Redes Wi-Fi", Icons.Filled.Wifi, "Tipos"))
    }
    if ((conteoPorTipo[TipoEntrada.CUENTA_BANCARIA] ?: 0) > 0) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("t:banco", "Cuentas bancarias", Icons.Filled.AccountBalance, "Tipos"))
    }
    if ((conteoPorTipo[TipoEntrada.SERVIDOR] ?: 0) > 0) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("t:servidor", "Servidores / SSH", Icons.Filled.Dns, "Tipos"))
    }
    if ((conteoPorTipo[TipoEntrada.WALLET] ?: 0) > 0) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("t:wallet", "Cripto Wallets", Icons.Filled.Security, "Tipos"))
    }
    if (tienePasskey) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("t:passkey", "Llaves de acceso", Icons.Filled.Fingerprint, "Tipos"))
    }
    if ((conteoPorTipo[TipoEntrada.NOTA] ?: 0) > 0) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("t:nota", "Notas seguras", Icons.Filled.Description, "Tipos"))
    }
    if ((conteoPorTipo[TipoEntrada.CONTACTO] ?: 0) > 0) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("t:contacto", "Contactos", Icons.Filled.Person, "Tipos"))
    }
    if ((conteoPorTipo[TipoEntrada.IDENTIDAD] ?: 0) > 0) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("t:identidad", "Documentos de identidad", Icons.Filled.Person, "Tipos"))
    }

    // Propiedades y estados existentes
    if (tieneTotp) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("has:totp", "Con código TOTP (2FA)", Icons.Filled.Pin, "Propiedades"))
    }
    if (tienePasskey) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("has:passkey", "Con llave Passkey", Icons.Filled.Fingerprint, "Propiedades"))
    }
    if (tieneNotas) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("has:notas", "Con notas adjuntas", Icons.Filled.Description, "Propiedades"))
    }
    if (tieneFavoritos) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("is:fav", "Marcadas como Favorito", Icons.Filled.Star, "Propiedades"))
    }
    if (tieneIgnoradas) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("is:ignorado", "Ignoradas en auditoría", Icons.Filled.VisibilityOff, "Propiedades"))
    }

    // Organización existente
    if (categorias.isNotEmpty()) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("cat:", "Filtrar por Categoría", Icons.Filled.Folder, "Organización", requiereParametro = true))
    }
    if (etiquetasDisponibles.isNotEmpty()) {
        sugerenciasDisponibles.add(ItemSugerenciaBusqueda("tag:", "Filtrar por Etiqueta", Icons.AutoMirrored.Filled.Label, "Organización", requiereParametro = true))
    }

    val textoLimpio = textoBusqueda.trim()
    if (textoLimpio.isBlank()) {
        return sugerenciasDisponibles
    }

    // Autocompletado contextual según el último token tecleado
    val tokens = textoLimpio.split(Regex("\\s+"))
    val ultimoToken = tokens.lastOrNull()?.lowercase() ?: ""

    if (ultimoToken.startsWith("cat:") && categorias.isNotEmpty()) {
        val prefijoCat = ultimoToken.removePrefix("cat:")
        val categoriasFiltradas = if (prefijoCat.isBlank()) {
            categorias
        } else {
            categorias.filter { it.nombre.contains(prefijoCat, ignoreCase = true) }
        }
        return categoriasFiltradas.take(6).map { cat ->
            ItemSugerenciaBusqueda(
                comando = "cat:\"${cat.nombre}\"",
                etiqueta = "Categoría: ${cat.nombre}",
                icono = Icons.Filled.Folder,
                categoria = "Categorías"
            )
        }
    }

    if (ultimoToken.startsWith("tag:") && etiquetasDisponibles.isNotEmpty()) {
        val prefijoTag = ultimoToken.removePrefix("tag:")
        val tagsFiltradas = if (prefijoTag.isBlank()) {
            etiquetasDisponibles
        } else {
            etiquetasDisponibles.filter { it.contains(prefijoTag, ignoreCase = true) }
        }
        return tagsFiltradas.take(6).map { tag ->
            ItemSugerenciaBusqueda(
                comando = "tag:$tag",
                etiqueta = "Etiqueta: $tag",
                icono = Icons.AutoMirrored.Filled.Label,
                categoria = "Etiquetas"
            )
        }
    }

    // Filtrar por término parcial (ej. "wi" -> "t:wifi", "totp" -> "has:totp")
    val coincidencias = sugerenciasDisponibles.filter { item ->
        item.comando.contains(ultimoToken, ignoreCase = true) ||
        item.etiqueta.contains(ultimoToken, ignoreCase = true)
    }

    return if (coincidencias.isNotEmpty()) coincidencias else sugerenciasDisponibles
}
