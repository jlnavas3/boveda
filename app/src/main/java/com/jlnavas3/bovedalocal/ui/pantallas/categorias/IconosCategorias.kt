package com.jlnavas3.bovedalocal.ui.pantallas.categorias

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

object IconosCategorias {
    val OPCIONES: List<OpcionIconoCategoria> = listOf(
        OpcionIconoCategoria("carpeta", "Carpeta", Icons.Filled.Folder),
        OpcionIconoCategoria("trabajo", "Trabajo", Icons.Filled.Work),
        OpcionIconoCategoria("finanzas", "Finanzas", Icons.Filled.AccountBalance),
        OpcionIconoCategoria("personal", "Personal", Icons.Filled.Home),
        OpcionIconoCategoria("seguro", "Seguro", Icons.Filled.Security),
        OpcionIconoCategoria("llave", "Llaves", Icons.Filled.VpnKey),
        OpcionIconoCategoria("estrella", "Destacado", Icons.Filled.Star),
        OpcionIconoCategoria("compras", "Compras", Icons.Filled.ShoppingCart),
        OpcionIconoCategoria("web", "Sitios Web", Icons.Filled.Language),
        OpcionIconoCategoria("correo", "Correos", Icons.Filled.Email),
        OpcionIconoCategoria("dispositivos", "Dispositivos", Icons.Filled.Devices),
        OpcionIconoCategoria("favorito", "Favoritos", Icons.Filled.Favorite),
        OpcionIconoCategoria("identidad", "Identidad", Icons.Filled.AccountCircle),
        OpcionIconoCategoria("seguridad", "Protección", Icons.Filled.Security)
    )

    val COLORES_PREDETERMINADOS = listOf(
        "#3B82F6", // Azul
        "#10B981", // Verde Esmeralda
        "#F59E0B", // Ámbar
        "#8B5CF6", // Púrpura
        "#EF4444", // Rojo
        "#EC4899", // Rosa
        "#06B6D4", // Cian
        "#84CC16", // Lima
        "#6366F1", // Índigo
        "#F97316"  // Naranja
    )

    fun obtenerIcono(id: String): ImageVector {
        return OPCIONES.firstOrNull { it.id.equals(id, ignoreCase = true) }?.icono
            ?: Icons.Filled.Folder
    }

    fun parsearColorHex(hex: String?): Color? {
        if (hex.isNullOrBlank()) return null
        return try {
            val limpio = hex.removePrefix("#")
            val valor = limpio.toLong(16)
            if (limpio.length == 6) {
                Color(valor or 0x00000000FF000000L)
            } else if (limpio.length == 8) {
                Color(valor)
            } else null
        } catch (_: Exception) {
            null
        }
    }
}
