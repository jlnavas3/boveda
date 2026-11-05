/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Menú contextual flotante para filtrar rápidamente las entradas por su tipo o 2FA.
 */
@Composable
fun DropdownFiltroTipos(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    tipoSeleccionado: TipoEntrada?,
    solo2fa: Boolean = false,
    conteoPorTipo: Map<TipoEntrada, Int>,
    conteo2fa: Int = 0,
    totalEntradas: Int,
    alSeleccionarTipo: (TipoEntrada?) -> Unit,
    alSeleccionar2fa: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val itemsTipos: List<Pair<TipoEntrada, Pair<String, ImageVector>>> = listOf(
        TipoEntrada.LOGIN to ("Inicios de sesión" to Icons.Filled.VpnKey),
        TipoEntrada.PASSKEY to ("Llaves de acceso (Passkeys)" to Icons.Filled.Fingerprint),
        TipoEntrada.TARJETA to ("Tarjetas bancarias" to Icons.Filled.CreditCard),
        TipoEntrada.CUENTA_BANCARIA to ("Cuentas bancarias" to Icons.Filled.AccountBalance),
        TipoEntrada.IDENTIDAD to ("Documentos de identidad" to Icons.Filled.Badge),
        TipoEntrada.CONTACTO to ("Contactos seguros" to Icons.Filled.Person),
        TipoEntrada.NOTA to ("Notas seguras" to Icons.Filled.Description),
        TipoEntrada.WIFI to ("Redes Wi-Fi" to Icons.Filled.Wifi),
        TipoEntrada.SERVIDOR to ("Servidores" to Icons.Filled.Dns),
        TipoEntrada.WALLET to ("Cripto Wallets" to Icons.Filled.AccountBalanceWallet)
    )

    MenuDesplegableBoveda(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.widthIn(min = 230.dp, max = 300.dp)
    ) {
        Column(
            modifier = Modifier
                .heightIn(max = 340.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Todos los tipos
            val esTodos = tipoSeleccionado == null && !solo2fa
            val textoTodos = if (totalEntradas > 0) "Todos los tipos ($totalEntradas)" else "Todos los tipos"
            ElementoMenuCompacto(
                texto = textoTodos,
                icono = Icons.Filled.Category,
                colorIcono = if (esTodos) ColorAcento else ColorIconosInternos,
                colorTexto = if (esTodos) ColorAcento else TextoPrincipal,
                iconoFinal = if (esTodos) Icons.Filled.Check else null,
                colorIconoFinal = ColorAcento,
                onClick = {
                    alSeleccionarTipo(null)
                    onDismissRequest()
                }
            )

            // 2. Códigos 2FA / TOTP
            val es2faActivo = solo2fa
            val texto2fa = if (conteo2fa > 0) "Códigos 2FA (TOTP) ($conteo2fa)" else "Códigos 2FA (TOTP)"
            ElementoMenuCompacto(
                texto = texto2fa,
                icono = Icons.Filled.Timer,
                colorIcono = if (es2faActivo) ColorAcento else ColorIconosInternos,
                colorTexto = if (es2faActivo) ColorAcento else TextoPrincipal,
                iconoFinal = if (es2faActivo) Icons.Filled.Check else null,
                colorIconoFinal = ColorAcento,
                onClick = {
                    alSeleccionar2fa()
                    onDismissRequest()
                }
            )

            SeparadorOpcionMenu()

            // 3. Tipos individuales
            itemsTipos.forEach { (tipo, par) ->
                val (etiqueta, icono) = par
                val esSeleccionado = tipo == tipoSeleccionado && !solo2fa
                val conteo = conteoPorTipo[tipo] ?: 0
                val textoMostrar = if (conteo > 0) "$etiqueta ($conteo)" else etiqueta

                ElementoMenuCompacto(
                    texto = textoMostrar,
                    icono = icono,
                    colorIcono = if (esSeleccionado) ColorAcento else ColorIconosInternos,
                    colorTexto = if (esSeleccionado) ColorAcento else TextoPrincipal,
                    iconoFinal = if (esSeleccionado) Icons.Filled.Check else null,
                    colorIconoFinal = ColorAcento,
                    onClick = {
                        alSeleccionarTipo(tipo)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}
