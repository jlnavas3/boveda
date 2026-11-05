/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.identidades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Tarjeta unificada y ejecutiva que agrupa los 4 elementos de autocompletado y facturación
 * (documento, tarjeta, cuenta bancaria y contacto/dirección) en una ficha limpia y compacta.
 */
@Composable
fun SeccionAutocompletadoUnificadaPerfil(
    documentoPrincipal: Entrada?,
    cantidadDocumentos: Int,
    alAbrirDocumentos: () -> Unit,
    tarjetaPrincipal: Entrada?,
    cantidadTarjetas: Int,
    alAbrirTarjetas: () -> Unit,
    cuentaPrincipal: Entrada?,
    cantidadCuentas: Int,
    alAbrirCuentas: () -> Unit,
    contactoPrincipal: Entrada?,
    cantidadContactos: Int,
    alAbrirContactos: () -> Unit,
    colorAcento: Color,
    modifier: Modifier = Modifier
) {
    ContenedorTarjeta(
        modifier = modifier,
        colorFondo = ColorTarjetaAjustes,
        paddingInterno = 14.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Cabecera descriptiva unificada
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ContenedorIconoInsignia(
                    icono = Icons.Filled.Payment,
                    tamano = TamanoInsignia.MEDIANO,
                    colorFondo = ColorCampoAjustes,
                    colorIcono = TextoPrincipal
                )
                Column {
                    TextoTitulo(
                        texto = "Autocompletado y pagos",
                        estilo = EstiloTitulo.PEQUENO
                    )
                    TextoCuerpo(
                        texto = "Datos vinculados para rellenar compras y formularios web",
                        tamano = TamanoCuerpo.MINI,
                        color = TextoSecundario
                    )
                }
            }

            // 1. Documento de identidad
            val tipoDoc = documentoPrincipal?.identidadEfectiva?.tipoDocumento.orEmpty()
            val numDoc = documentoPrincipal?.identidadEfectiva?.numero.orEmpty().ifBlank { documentoPrincipal?.usuario.orEmpty() }
            val detalleDoc = if (tipoDoc.isNotBlank() && numDoc.isNotBlank()) "$tipoDoc • $numDoc" else numDoc.ifBlank { null }

            FilaElementoAutocompletadoPerfil(
                icono = Icons.Filled.Badge,
                tituloCategoria = "Documento de identidad",
                tituloElemento = documentoPrincipal?.titulo?.ifBlank { "Documento" },
                detalleElemento = detalleDoc,
                cantidadTotal = cantidadDocumentos,
                colorAcento = colorAcento,
                alPulsar = alAbrirDocumentos
            )

            HorizontalDivider(color = ColorSeparadorAjustes.copy(alpha = 0.5f), thickness = 0.8.dp)

            // 2. Tarjeta bancaria
            val numTarjeta = tarjetaPrincipal?.datosTarjeta?.numero.orEmpty()
            val ultimosTarjeta = if (numTarjeta.length >= 4) "•••• ${numTarjeta.takeLast(4)}" else numTarjeta
            val venceTarjeta = tarjetaPrincipal?.datosTarjeta?.vencimiento?.ifBlank { null }
            val detalleTarjeta = if (venceTarjeta != null && ultimosTarjeta.isNotBlank()) "$ultimosTarjeta (Vence: $venceTarjeta)" else ultimosTarjeta.ifBlank { null }

            FilaElementoAutocompletadoPerfil(
                icono = Icons.Filled.CreditCard,
                tituloCategoria = "Tarjeta bancaria",
                tituloElemento = tarjetaPrincipal?.titulo?.ifBlank { "Tarjeta" },
                detalleElemento = detalleTarjeta,
                cantidadTotal = cantidadTarjetas,
                colorAcento = colorAcento,
                alPulsar = alAbrirTarjetas
            )

            HorizontalDivider(color = ColorSeparadorAjustes.copy(alpha = 0.5f), thickness = 0.8.dp)

            // 3. Cuenta bancaria
            val banco = cuentaPrincipal?.datosCuentaBancaria?.banco.orEmpty()
            val numCta = cuentaPrincipal?.datosCuentaBancaria?.numeroCuenta.orEmpty()
            val tipoCta = cuentaPrincipal?.datosCuentaBancaria?.tipoCuenta.orEmpty()
            val detalleCuenta = listOf(banco, tipoCta, numCta).filter { it.isNotBlank() }.joinToString(" • ").ifBlank { null }

            FilaElementoAutocompletadoPerfil(
                icono = Icons.Filled.AccountBalance,
                tituloCategoria = "Cuenta bancaria",
                tituloElemento = cuentaPrincipal?.titulo?.ifBlank { "Cuenta bancaria" },
                detalleElemento = detalleCuenta,
                cantidadTotal = cantidadCuentas,
                colorAcento = colorAcento,
                alPulsar = alAbrirCuentas
            )

            HorizontalDivider(color = ColorSeparadorAjustes.copy(alpha = 0.5f), thickness = 0.8.dp)

            // 4. Contacto y dirección de entrega
            val tel = contactoPrincipal?.usuario.orEmpty()
            val ciudad = contactoPrincipal?.contactoEfectivo?.ciudad.orEmpty()
            val dir = contactoPrincipal?.contactoEfectivo?.direccion.orEmpty()
            val detalleContacto = listOf(tel, dir.ifBlank { ciudad }).filter { it.isNotBlank() }.joinToString(" • ").ifBlank { null }

            FilaElementoAutocompletadoPerfil(
                icono = Icons.Filled.LocationOn,
                tituloCategoria = "Contacto y dirección",
                tituloElemento = contactoPrincipal?.titulo?.ifBlank { "Contacto" },
                detalleElemento = detalleContacto,
                cantidadTotal = cantidadContactos,
                colorAcento = colorAcento,
                alPulsar = alAbrirContactos
            )
        }
    }
}
