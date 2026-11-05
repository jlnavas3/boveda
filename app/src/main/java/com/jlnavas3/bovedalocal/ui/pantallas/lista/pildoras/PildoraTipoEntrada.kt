/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Píldora compacta y permanente para el filtro rápido de tipos de entrada o 2FA.
 * En estado inactivo muestra únicamente [ 🗂️ ▾ ] ocupando el mínimo espacio horizontal,
 * y se expande con el nombre y conteo al seleccionar un tipo específico o códigos 2FA.
 */
@Composable
fun PildoraTipoEntrada(
    tipo: TipoEntrada?,
    solo2fa: Boolean = false,
    conteoPorTipo: Map<TipoEntrada, Int>,
    conteo2fa: Int = 0,
    totalEntradas: Int,
    alSeleccionarTipo: (TipoEntrada?) -> Unit,
    alSeleccionar2fa: () -> Unit = {},
    alLimpiar: () -> Unit = { alSeleccionarTipo(null) },
    modifier: Modifier = Modifier
) {
    var menuAbierto by remember { mutableStateOf(false) }
    val activo = tipo != null || solo2fa

    val icono: ImageVector = when {
        solo2fa -> Icons.Filled.Timer
        tipo == null -> Icons.Filled.Category
        tipo == TipoEntrada.LOGIN -> Icons.Filled.VpnKey
        tipo == TipoEntrada.NOTA -> Icons.Filled.Description
        tipo == TipoEntrada.TARJETA -> Icons.Filled.CreditCard
        tipo == TipoEntrada.WIFI -> Icons.Filled.Wifi
        tipo == TipoEntrada.CUENTA_BANCARIA -> Icons.Filled.AccountBalance
        tipo == TipoEntrada.IDENTIDAD -> Icons.Filled.Badge
        tipo == TipoEntrada.SERVIDOR -> Icons.Filled.Dns
        tipo == TipoEntrada.WALLET -> Icons.Filled.AccountBalanceWallet
        tipo == TipoEntrada.PASSKEY -> Icons.Filled.Fingerprint
        tipo == TipoEntrada.CONTACTO -> Icons.Filled.Person
        else -> Icons.Filled.Category
    }

    val texto: String? = when {
        solo2fa -> {
            if (conteo2fa > 0) "2FA ($conteo2fa)" else "2FA"
        }
        tipo != null -> {
            val etiqueta = when (tipo) {
                TipoEntrada.LOGIN -> "Logins"
                TipoEntrada.NOTA -> "Notas"
                TipoEntrada.TARJETA -> "Tarjetas"
                TipoEntrada.WIFI -> "Wi-Fi"
                TipoEntrada.CUENTA_BANCARIA -> "Cuentas"
                TipoEntrada.IDENTIDAD -> "Identidad"
                TipoEntrada.SERVIDOR -> "Servidores"
                TipoEntrada.WALLET -> "Wallets"
                TipoEntrada.PASSKEY -> "Passkeys"
                TipoEntrada.CONTACTO -> "Contactos"
            }
            val conteo = conteoPorTipo[tipo] ?: 0
            if (conteo > 0) "$etiqueta ($conteo)" else etiqueta
        }
        else -> null
    }

    val descripcionHud = when {
        solo2fa -> "Filtro: Códigos 2FA"
        tipo != null -> "Tipo: ${texto ?: ""}"
        else -> "Filtrar por tipo"
    }

    Box(modifier = modifier) {
        PildoraFiltroBase(
            icono = icono,
            texto = texto,
            activo = activo,
            colorAcento = ColorAcento,
            descripcionHud = descripcionHud,
            alPulsar = { menuAbierto = true },
            alLimpiar = if (activo) alLimpiar else null
        )

        DropdownFiltroTipos(
            expanded = menuAbierto,
            onDismissRequest = { menuAbierto = false },
            tipoSeleccionado = tipo,
            solo2fa = solo2fa,
            conteoPorTipo = conteoPorTipo,
            conteo2fa = conteo2fa,
            totalEntradas = totalEntradas,
            alSeleccionarTipo = alSeleccionarTipo,
            alSeleccionar2fa = alSeleccionar2fa
        )
    }
}
