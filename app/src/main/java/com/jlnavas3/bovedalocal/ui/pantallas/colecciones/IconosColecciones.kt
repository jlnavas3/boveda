package com.jlnavas3.bovedalocal.ui.pantallas.colecciones

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class OpcionIconoColeccion(
    val id: String,
    val etiqueta: String,
    val icono: ImageVector
)

object IconosColecciones {
    val OPCIONES: List<OpcionIconoColeccion> = listOf(
        OpcionIconoColeccion("carpeta", "Carpeta", Icons.Filled.Folder),
        OpcionIconoColeccion("trabajo", "Trabajo", Icons.Filled.Work),
        OpcionIconoColeccion("finanzas", "Finanzas", Icons.Filled.AccountBalance),
        OpcionIconoColeccion("personal", "Personal", Icons.Filled.Home),
        OpcionIconoColeccion("candado", "Seguro", Icons.Filled.Lock),
        OpcionIconoColeccion("llave", "Llaves", Icons.Filled.VpnKey),
        OpcionIconoColeccion("estrella", "Destacado", Icons.Filled.Star),
        OpcionIconoColeccion("compras", "Compras", Icons.Filled.ShoppingCart),
        OpcionIconoColeccion("web", "Sitios Web", Icons.Filled.Language),
        OpcionIconoColeccion("correo", "Correos", Icons.Filled.Email),
        OpcionIconoColeccion("dispositivos", "Dispositivos", Icons.Filled.Devices),
        OpcionIconoColeccion("favorito", "Favoritos", Icons.Filled.Favorite),
        OpcionIconoColeccion("identidad", "Identidad", Icons.Filled.Badge),
        OpcionIconoColeccion("seguridad", "Protección", Icons.Filled.Shield)
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
