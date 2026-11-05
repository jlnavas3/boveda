/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.DatosCuentaBancaria
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosUsuario
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.FormateadorCampos
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente desacoplado que renderiza la información de una cuenta bancaria,
 * titular, IBAN y códigos internacionales de transferencia.
 */
@Composable
fun TarjetaDetalleCuentaBancaria(
    cuenta: DatosCuentaBancaria,
    vm: VaultViewModel? = null,
    ajustes: AjustesApp? = null,
    haptica: Haptica,
    ultimaCopia: String?,
    alCopiarCampo: (String) -> Unit
) {
    val ibanFormateado = remember(cuenta.numeroCuenta, ajustes?.formatoIban) {
        FormateadorCampos.aplicarMascaraIban(cuenta.numeroCuenta, ajustes?.formatoIban ?: FormateadorCampos.IBAN_BLOQUES_4)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Cuenta bancaria")

        // 1. Entidad y Tipo de Cuenta
        TarjetaDatoDetalle(colorBorde = ColorDatosWeb) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.AccountBalance,
                            contentDescription = null,
                            tint = TextoSecundario,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Entidad bancaria",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )
                        if (cuenta.tipoCuenta.isNotBlank()) {
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(FormaPequena)
                                    .background(ColorCampoAjustes)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = cuenta.tipoCuenta,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = TextoSecundario
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = cuenta.banco.ifBlank { "—" },
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = TextoPrincipal,
                        maxLines = 1,
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    )
                }

                if (cuenta.banco.isNotBlank()) {
                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "cuenta_banco",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Banco", cuenta.banco, sensible = false)
                            alCopiarCampo("cuenta_banco")
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(EspaciadoDetalle))

        // 2. Número de cuenta / IBAN
        TarjetaDatoDetalle(colorBorde = ColorAcento) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Número de cuenta / IBAN",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = TextoSecundario
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = ibanFormateado.ifBlank { "—" },
                        style = EstiloMono,
                        color = TextoPrincipal,
                        maxLines = 1,
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    )
                }

                if (cuenta.numeroCuenta.isNotBlank()) {
                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "cuenta_numero",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Número de cuenta", cuenta.numeroCuenta, sensible = false)
                            alCopiarCampo("cuenta_numero")
                        }
                    )
                }
            }
        }

        // 3. Titular de la cuenta
        if (cuenta.titular.isNotBlank()) {
            Spacer(Modifier.height(EspaciadoDetalle))
            TarjetaDatoDetalle(colorBorde = ColorDatosUsuario) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Titular de la cuenta",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )

                        Spacer(Modifier.height(2.dp))

                        Text(
                            text = cuenta.titular,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextoPrincipal,
                            maxLines = 1,
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        )
                    }

                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "cuenta_titular",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Titular", cuenta.titular, sensible = false)
                            alCopiarCampo("cuenta_titular")
                        }
                    )
                }
            }
        }

        // 4. SWIFT / BIC / Claves interbancarias
        if (cuenta.swiftBic.isNotBlank() || cuenta.cbuClabe.isNotBlank()) {
            Spacer(Modifier.height(EspaciadoDetalle))
            TarjetaDatoDetalle(colorBorde = ColorDatosWeb) {
                val valorCodigos = listOfNotNull(
                    cuenta.swiftBic.takeIf { it.isNotBlank() },
                    cuenta.cbuClabe.takeIf { it.isNotBlank() }
                ).joinToString(" • ")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SWIFT / BIC / Claves de transferencia",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoSecundario
                        )

                        Spacer(Modifier.height(2.dp))

                        Text(
                            text = valorCodigos,
                            style = EstiloMono,
                            color = TextoPrincipal,
                            maxLines = 1,
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        )
                    }

                    BotonCopiarDetalle(
                        copiado = ultimaCopia == "cuenta_swift",
                        alPulsar = {
                            haptica.exito()
                            vm?.copiar("Código bancario", valorCodigos, sensible = false)
                            alCopiarCampo("cuenta_swift")
                        }
                    )
                }
            }
        }
    }
}
