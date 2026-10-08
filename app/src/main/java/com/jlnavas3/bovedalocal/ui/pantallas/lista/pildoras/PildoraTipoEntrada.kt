package com.jlnavas3.bovedalocal.ui.pantallas.lista.pildoras

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento

/**
 * Píldora compacta para el filtro de tipo de entrada activo [ Tipo ✕ ].
 */
@Composable
fun PildoraTipoEntrada(
    tipo: TipoEntrada?,
    alLimpiarTipo: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (tipo == null) return

    val icono: ImageVector = when (tipo) {
        TipoEntrada.LOGIN -> Icons.Filled.VpnKey
        TipoEntrada.NOTA -> Icons.Filled.Description
        TipoEntrada.TARJETA -> Icons.Filled.CreditCard
        TipoEntrada.WIFI -> Icons.Filled.Wifi
        TipoEntrada.CUENTA_BANCARIA -> Icons.Filled.AccountBalance
        TipoEntrada.IDENTIDAD -> Icons.Filled.Badge
        TipoEntrada.SERVIDOR -> Icons.Filled.Dns
        TipoEntrada.WALLET -> Icons.Filled.AccountBalanceWallet
        TipoEntrada.PASSKEY -> Icons.Filled.Fingerprint
    }

    PildoraFiltroBase(
        icono = icono,
        texto = null,
        activo = true,
        colorAcento = ColorAcento,
        alPulsar = alLimpiarTipo,
        alLimpiar = alLimpiarTipo,
        modifier = modifier
    )
}
