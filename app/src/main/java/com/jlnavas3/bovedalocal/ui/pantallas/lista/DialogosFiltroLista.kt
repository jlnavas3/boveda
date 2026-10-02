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
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.FilaOpcionModal
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
        TipoEntrada.PASSKEY to ("Llaves de paso" to Icons.Filled.Fingerprint),
        TipoEntrada.NOTA to ("Notas seguras" to Icons.Filled.Description),
        TipoEntrada.TARJETA to ("Tarjetas bancarias" to Icons.Filled.CreditCard),
        TipoEntrada.WIFI to ("Redes Wi-Fi" to Icons.Filled.Wifi),
        TipoEntrada.CUENTA_BANCARIA to ("Cuentas bancarias" to Icons.Filled.AccountBalance),
        TipoEntrada.IDENTIDAD to ("Identidad" to Icons.Filled.Badge),
        TipoEntrada.SERVIDOR to ("Servidores" to Icons.Filled.Dns),
        TipoEntrada.WALLET to ("Cripto Wallets" to Icons.Filled.AccountBalanceWallet)
    )

    DialogoBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Filtrar por tipo",
        icono = Icons.Filled.SelectAll,
        colorIcono = Ambar,
        botonConfirmar = {
            TextButton(onClick = alCerrar) {
                Text("Cerrar", color = Ambar)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tipos.forEach { (tipo, par) ->
                val (nombre, icono) = par
                val seleccionado = filtroActual == tipo
                FilaOpcionModal(
                    titulo = nombre,
                    icono = icono,
                    seleccionado = seleccionado,
                    colorAcento = Ambar,
                    alPulsar = {
                        alSeleccionarTipo(tipo)
                        alCerrar()
                    },
                    controlFinal = if (seleccionado) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Ambar,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null
                )
            }
        }
    }
}

@Composable
fun DialogoOrdenacionLista(
    criterioActual: CriterioOrdenacion,
    alSeleccionarCriterio: (CriterioOrdenacion) -> Unit,
    alCerrar: () -> Unit
) {
    DialogoBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Ordenar por",
        icono = Icons.AutoMirrored.Filled.Sort,
        colorIcono = Ambar,
        botonConfirmar = {
            TextButton(onClick = alCerrar) {
                Text("Cerrar", color = Ambar)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CriterioOrdenacion.entries.forEach { criterio ->
                val seleccionado = criterio == criterioActual
                FilaOpcionModal(
                    titulo = criterio.etiqueta,
                    icono = Icons.AutoMirrored.Filled.Sort,
                    seleccionado = seleccionado,
                    colorAcento = Ambar,
                    alPulsar = {
                        alSeleccionarCriterio(criterio)
                        alCerrar()
                    },
                    controlFinal = if (seleccionado) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Ambar,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null
                )
            }
        }
    }
}
