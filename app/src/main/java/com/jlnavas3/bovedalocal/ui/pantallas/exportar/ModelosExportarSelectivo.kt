package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario

data class CategoriaExportacion(
    val id: String,
    val etiqueta: String,
    val icono: ImageVector,
    val color: Color,
    val filtro: (Entrada) -> Boolean
)

object ProveedorCategoriasExportacion {
    fun obtenerTodas(): List<CategoriaExportacion> = listOf(
        CategoriaExportacion("todos", "Todas", Icons.Filled.Layers, ColorAcento) { true },
        CategoriaExportacion("passkeys", "Llaves de paso", Icons.Filled.Fingerprint, ColorDatosPasskey) { it.tipo == TipoEntrada.PASSKEY || it.passkey != null },
        CategoriaExportacion("2fa", "Dos pasos", Icons.Filled.Shield, ColorDatos2FA) { !it.secretoTotp.isNullOrBlank() },
        CategoriaExportacion("login", "Cuentas", Icons.Filled.Lock, ColorDatosUsuario) { it.tipo == TipoEntrada.LOGIN },
        CategoriaExportacion("tarjetas", "Tarjetas", Icons.Filled.CreditCard, Color(0xFFE57373)) { it.tipo == TipoEntrada.TARJETA },
        CategoriaExportacion("identidades", "Identidades", Icons.Filled.Badge, Color(0xFF81C784)) { it.tipo == TipoEntrada.IDENTIDAD },
        CategoriaExportacion("wifi", "Wi-Fi", Icons.Filled.Wifi, Color(0xFF4FC3F7)) { it.tipo == TipoEntrada.WIFI },
        CategoriaExportacion("servidores", "Servidores", Icons.Filled.Dns, Color(0xFFFFB74D)) { it.tipo == TipoEntrada.SERVIDOR },
        CategoriaExportacion("wallet", "Crypto Wallets", Icons.Filled.AccountBalanceWallet, Color(0xFFBA68C8)) { it.tipo == TipoEntrada.WALLET },
        CategoriaExportacion("notas", "Notas", Icons.Filled.Description, Color(0xFFFFD54F)) { it.tipo == TipoEntrada.NOTA }
    )
}
