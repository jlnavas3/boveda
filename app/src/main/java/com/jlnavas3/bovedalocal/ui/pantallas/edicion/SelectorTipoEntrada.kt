package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun SelectorTipoEntrada(
    tipoActual: TipoEntrada,
    alSeleccionarTipo: (TipoEntrada) -> Unit
) {
    var abierto by remember { mutableStateOf(false) }
    val forma = FormaBoton

    val icono = when (tipoActual) {
        TipoEntrada.LOGIN -> Icons.Filled.Lock
        TipoEntrada.NOTA -> Icons.Filled.Description
        TipoEntrada.TARJETA -> Icons.Filled.CreditCard
        TipoEntrada.WIFI -> Icons.Filled.Wifi
        TipoEntrada.CUENTA_BANCARIA -> Icons.Filled.AccountBalance
        TipoEntrada.IDENTIDAD -> Icons.Filled.Badge
        TipoEntrada.SERVIDOR -> Icons.Filled.Dns
        TipoEntrada.WALLET -> Icons.Filled.AccountBalanceWallet
        TipoEntrada.PASSKEY -> Icons.Filled.Fingerprint
    }
    val texto = tipoActual.etiqueta

    val esOscuro = androidx.compose.foundation.isSystemInDarkTheme()
    val fondoCaja = if (esOscuro) Color(0xFF161518) else Color(0xFFF4F4F6)
    val bordeCaja = if (abierto) ColorAcento else (if (esOscuro) Color(0xFF333238) else Color(0xFFDFDFE3))

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .clip(forma)
                .background(fondoCaja)
                .clickable { abierto = true }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = if (abierto) ColorAcento else ColorIconosInternos,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = texto,
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Desplegar tipos de entrada",
                tint = TextoSecundario,
                modifier = Modifier.size(18.dp)
            )
        }

        MenuDesplegableBoveda(
            expanded = abierto,
            onDismissRequest = { abierto = false }
        ) {
            val opciones = listOf(
                Triple(TipoEntrada.LOGIN, "Contraseña", Icons.Filled.Lock),
                Triple(TipoEntrada.NOTA, "Nota segura", Icons.Filled.Description),
                Triple(TipoEntrada.TARJETA, "Tarjeta bancaria", Icons.Filled.CreditCard),
                Triple(TipoEntrada.WIFI, "Red Wi-Fi", Icons.Filled.Wifi),
                Triple(TipoEntrada.CUENTA_BANCARIA, "Cuenta bancaria", Icons.Filled.AccountBalance),
                Triple(TipoEntrada.IDENTIDAD, "Documento de identidad", Icons.Filled.Badge),
                Triple(TipoEntrada.SERVIDOR, "Servidor / SSH", Icons.Filled.Dns),
                Triple(TipoEntrada.WALLET, "Cripto Wallet", Icons.Filled.AccountBalanceWallet)
            )

            opciones.forEachIndexed { index, (t, titulo, ic) ->
                if (index > 0) SeparadorOpcionMenu()
                val seleccionado = tipoActual == t
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(
                            imageVector = ic,
                            contentDescription = null,
                            tint = if (seleccionado) ColorIconosInternos else TextoSecundario,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    text = {
                        Text(
                            text = titulo,
                            color = if (seleccionado) ColorTitulos else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    },
                    trailingIcon = if (seleccionado) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = ColorIconosInternos,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else null,
                    onClick = {
                        alSeleccionarTipo(t)
                        abierto = false
                    }
                )
            }
        }
    }
}
