/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.busqueda

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wifi

/**
 * Proveedor de catálogo de sugerencias y modificadores rápidos para la búsqueda.
 */
object ProveedorSugerenciasBusqueda {

    val destacadas: List<ItemSugerenciaBusqueda> = listOf(
        ItemSugerenciaBusqueda("t:login", "Contraseñas", Icons.Filled.Lock, "Tipos"),
        ItemSugerenciaBusqueda("t:tarjeta", "Tarjetas", Icons.Filled.CreditCard, "Tipos"),
        ItemSugerenciaBusqueda("t:wifi", "Wi-Fi", Icons.Filled.Wifi, "Tipos"),
        ItemSugerenciaBusqueda("has:totp", "Con 2FA", Icons.Filled.Pin, "Propiedades"),
        ItemSugerenciaBusqueda("is:fav", "Favoritos", Icons.Filled.Star, "Propiedades"),
        ItemSugerenciaBusqueda("has:passkey", "Passkeys", Icons.Filled.Fingerprint, "Propiedades"),
        ItemSugerenciaBusqueda("tag:", "Etiqueta...", Icons.Filled.Label, "Atributos", requiereParametro = true),
        ItemSugerenciaBusqueda("cat:", "Categoría...", Icons.Filled.Folder, "Atributos", requiereParametro = true)
    )

    val todasPorCategoria: Map<String, List<ItemSugerenciaBusqueda>> = mapOf(
        "Tipos de entrada" to listOf(
            ItemSugerenciaBusqueda("t:login", "Contraseña", Icons.Filled.Lock, "Tipos de entrada"),
            ItemSugerenciaBusqueda("t:tarjeta", "Tarjeta", Icons.Filled.CreditCard, "Tipos de entrada"),
            ItemSugerenciaBusqueda("t:wifi", "Wi-Fi", Icons.Filled.Wifi, "Tipos de entrada"),
            ItemSugerenciaBusqueda("t:banco", "Cuenta bancaria", Icons.Filled.AccountBalance, "Tipos de entrada"),
            ItemSugerenciaBusqueda("t:servidor", "Servidor / SSH", Icons.Filled.Dns, "Tipos de entrada"),
            ItemSugerenciaBusqueda("t:wallet", "Cripto Wallet", Icons.Filled.Security, "Tipos de entrada"),
            ItemSugerenciaBusqueda("t:passkey", "Llave de paso", Icons.Filled.Fingerprint, "Tipos de entrada"),
            ItemSugerenciaBusqueda("t:nota", "Nota segura", Icons.Filled.Description, "Tipos de entrada"),
            ItemSugerenciaBusqueda("t:contacto", "Contacto", Icons.Filled.Person, "Tipos de entrada")
        ),
        "Propiedades y estado" to listOf(
            ItemSugerenciaBusqueda("has:totp", "Con código TOTP", Icons.Filled.Pin, "Propiedades y estado"),
            ItemSugerenciaBusqueda("has:passkey", "Con Llave Passkey", Icons.Filled.Fingerprint, "Propiedades y estado"),
            ItemSugerenciaBusqueda("has:nota", "Con notas adjuntas", Icons.Filled.Description, "Propiedades y estado"),
            ItemSugerenciaBusqueda("is:fav", "Marcadas como Favorito", Icons.Filled.Star, "Propiedades y estado"),
            ItemSugerenciaBusqueda("is:ignorado", "Ignoradas en auditoría", Icons.Filled.VisibilityOff, "Propiedades y estado")
        ),
        "Organización" to listOf(
            ItemSugerenciaBusqueda("tag:", "Filtrar por Etiqueta", Icons.Filled.Label, "Organización", requiereParametro = true),
            ItemSugerenciaBusqueda("cat:", "Filtrar por Categoría", Icons.Filled.Folder, "Organización", requiereParametro = true)
        )
    )
}
