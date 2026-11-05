/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.importar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.TipoEntrada

/**
 * Sección de importación para las entradas desglosadas por tipo de credencial.
 */
@Composable
fun SeccionEntradasImportacion(
    conteoPorTipo: Map<TipoEntrada, Int>,
    tiposSeleccionados: Set<TipoEntrada>,
    alAlternarTipo: (TipoEntrada) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalEntradas = conteoPorTipo.values.sum()
    if (totalEntradas == 0) return

    val descripcionesPorTipo = mapOf(
        TipoEntrada.LOGIN to Pair("Contraseñas e inicios de sesión", "Cuentas con usuario, contraseña y enlaces web para inicio de sesión"),
        TipoEntrada.PASSKEY to Pair("Llaves de acceso (Passkeys)", "Credenciales criptográficas FIDO2 / WebAuthn para inicio sin contraseña"),
        TipoEntrada.NOTA to Pair("Notas seguras", "Apuntes cifrados, códigos de recuperación y textos confidenciales"),
        TipoEntrada.TARJETA to Pair("Tarjetas bancarias", "Números de tarjeta, fechas de caducidad, códigos CVV y titulares"),
        TipoEntrada.CUENTA_BANCARIA to Pair("Cuentas bancarias", "IBAN, números de cuenta, códigos SWIFT/BIC y entidades"),
        TipoEntrada.WIFI to Pair("Redes Wi-Fi", "Nombres de red (SSID), contraseñas de acceso y tipo de seguridad"),
        TipoEntrada.IDENTIDAD to Pair("Documentos de identidad", "DNI, pasaportes, licencias de conducir y números de soporte"),
        TipoEntrada.SERVIDOR to Pair("Servidores / SSH", "Hosts, puertos, usuarios, contraseñas y claves SSH"),
        TipoEntrada.WALLET to Pair("Cripto Wallets", "Direcciones públicas, frases semilla (seed) y claves privadas"),
        TipoEntrada.CONTACTO to Pair("Contactos seguros", "Libreta de contactos privados y teléfonos confidenciales")
    )

    ContenedorSeccionImportacion(
        titulo = "Credenciales y registros",
        icono = Icons.Filled.Lock,
        contador = totalEntradas,
        modifier = modifier
    ) {
        TipoEntrada.entries.forEach { tipo ->
            val cantidad = conteoPorTipo[tipo] ?: 0
            if (cantidad > 0) {
                val (titulo, descripcion) = descripcionesPorTipo[tipo] ?: Pair(tipo.etiqueta, "")
                FilaOpcionImportacion(
                    titulo = titulo,
                    descripcion = descripcion,
                    contador = cantidad,
                    seleccionado = tiposSeleccionados.contains(tipo),
                    alAlternar = { alAlternarTipo(tipo) }
                )
            }
        }
    }
}
