package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.CriterioOrdenacion
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

@Composable
fun DialogoFiltrosLista(
    filtroActual: TipoEntrada?,
    alSeleccionarTipo: (TipoEntrada?) -> Unit,
    alCerrar: () -> Unit
) {
    val tipos = listOf(
        null to ("Todo" to Icons.Filled.SelectAll),
        TipoEntrada.LOGIN to ("Claves / Logins" to Icons.Filled.Lock),
        TipoEntrada.PASSKEY to ("Passkeys" to Icons.Filled.Fingerprint),
        TipoEntrada.NOTA to ("Notas seguras" to Icons.Filled.Description),
        TipoEntrada.TARJETA to ("Tarjetas bancarias" to Icons.Filled.CreditCard),
        TipoEntrada.WIFI to ("Redes Wi-Fi" to Icons.Filled.Wifi),
        TipoEntrada.CUENTA_BANCARIA to ("Cuentas bancarias" to Icons.Filled.AccountBalance),
        TipoEntrada.IDENTIDAD to ("Identidad" to Icons.Filled.Badge),
        TipoEntrada.SERVIDOR to ("Servidores" to Icons.Filled.Dns),
        TipoEntrada.WALLET to ("Cripto Wallets" to Icons.Filled.AccountBalanceWallet)
    )

    AlertDialog(
        onDismissRequest = alCerrar,
        containerColor = ColorTarjetaAjustes,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 0.dp,
        title = {
            Text(
                "Filtrar por tipo",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = ColorTitulos
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                tipos.forEach { (tipo, par) ->
                    val (nombre, icono) = par
                    val seleccionado = filtroActual == tipo
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (seleccionado) Ambar.copy(alpha = 0.12f) else Color.Transparent)
                            .clickable {
                                alSeleccionarTipo(tipo)
                                alCerrar()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = if (seleccionado) Ambar else ColorIconosInternos,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = nombre,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            color = if (seleccionado) Ambar else TextoPrincipal,
                            modifier = Modifier.weight(1f)
                        )
                        if (seleccionado) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Ambar,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = alCerrar) {
                Text("Cerrar", color = Ambar)
            }
        }
    )
}

@Composable
fun DialogoOrdenacionLista(
    criterioActual: CriterioOrdenacion,
    alSeleccionarCriterio: (CriterioOrdenacion) -> Unit,
    alCerrar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = alCerrar,
        containerColor = ColorTarjetaAjustes,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 0.dp,
        title = {
            Text(
                "Ordenar por",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = ColorTitulos
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CriterioOrdenacion.entries.forEach { criterio ->
                    val seleccionado = criterio == criterioActual
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (seleccionado) Ambar.copy(alpha = 0.12f) else Color.Transparent)
                            .clickable {
                                alSeleccionarCriterio(criterio)
                                alCerrar()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = null,
                            tint = if (seleccionado) Ambar else ColorIconosInternos,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = criterio.etiqueta,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            color = if (seleccionado) Ambar else TextoPrincipal,
                            modifier = Modifier.weight(1f)
                        )
                        if (seleccionado) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Ambar,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = alCerrar) {
                Text("Cerrar", color = Ambar)
            }
        }
    )
}
