/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.exportar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.Color
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario

import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

object ProveedorCategoriasExportacion {
    fun obtenerTodas(): List<CategoriaExportacion> = listOf(
        CategoriaExportacion("todos", "Todas", Icons.Filled.Layers, TextoSecundario) { true },
        CategoriaExportacion("passkeys", "Llaves de acceso", Icons.Filled.Fingerprint, ColorDatosPasskey) { it.tipo == TipoEntrada.PASSKEY || it.passkey != null },
        CategoriaExportacion("2fa", "Dos pasos", Icons.Filled.Shield, ColorDatos2FA) { !it.secretoTotp.isNullOrBlank() },
        CategoriaExportacion("login", "Cuentas", Icons.Filled.Lock, ColorDatosUsuario) { it.tipo == TipoEntrada.LOGIN },
        CategoriaExportacion("tarjetas", "Tarjetas", Icons.Filled.CreditCard, Color(0xFFE57373)) { it.tipo == TipoEntrada.TARJETA },
        CategoriaExportacion("cuentas", "Cuentas bancarias", Icons.Filled.AccountBalance, Color(0xFF64B5F6)) { it.tipo == TipoEntrada.CUENTA_BANCARIA },
        CategoriaExportacion("identidades", "Documentos", Icons.Filled.Badge, Color(0xFF81C784)) { it.tipo == TipoEntrada.IDENTIDAD },
        CategoriaExportacion("wifi", "Wi-Fi", Icons.Filled.Wifi, Color(0xFF4FC3F7)) { it.tipo == TipoEntrada.WIFI },
        CategoriaExportacion("servidores", "Servidores", Icons.Filled.Dns, Color(0xFFFFB74D)) { it.tipo == TipoEntrada.SERVIDOR },
        CategoriaExportacion("wallet", "Crypto Wallets", Icons.Filled.AccountBalanceWallet, Color(0xFFBA68C8)) { it.tipo == TipoEntrada.WALLET },
        CategoriaExportacion("contactos", "Contactos", Icons.Filled.Person, Color(0xFF4DB6AC)) { it.tipo == TipoEntrada.CONTACTO },
        CategoriaExportacion("notas", "Notas", Icons.Filled.Description, Color(0xFFFFD54F)) { it.tipo == TipoEntrada.NOTA }
    )
}
