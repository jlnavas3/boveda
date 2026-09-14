package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionAutenticador2FA(
    vm: VaultViewModel,
    ajustes: AjustesApp,
    haptica: Haptica
) {
    TarjetaAjuste(
        titulo = "Autenticador 2FA",
        icono = Icons.Filled.Timer,
        descripcion = "Configura los valores usados al introducir una clave TOTP manualmente.",
        colorIcono = Color2FA
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            "Los QR otpauth traen sus propios parámetros y siempre tienen prioridad. Estos valores solo se usan cuando pegas una clave Base32 sin QR.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(10.dp))
        SelectorAjuste(
            titulo = "Dígitos predeterminados",
            icono = Icons.Filled.Timer,
            seleccionado = "${ajustes.totpManualDigitos} dígitos",
            opciones = listOf(6, 7, 8).map { valor ->
                OpcionAjuste(valor.toString(), "$valor dígitos", Icons.Filled.Timer)
            },
            alSeleccionar = { valor -> haptica.tic(); vm.ajustarTotpManualDigitos(valor.toInt()) }
        )
        Spacer(Modifier.height(10.dp))
        SelectorAjuste(
            titulo = "Período predeterminado",
            icono = Icons.Filled.Timer,
            seleccionado = "${ajustes.totpManualPeriodo} segundos",
            opciones = listOf(30, 60, 90).map { valor ->
                OpcionAjuste(valor.toString(), "$valor segundos", Icons.Filled.Timer)
            },
            alSeleccionar = { valor -> haptica.tic(); vm.ajustarTotpManualPeriodo(valor.toInt()) }
        )
        Spacer(Modifier.height(10.dp))
        SelectorAjuste(
            titulo = "Algoritmo predeterminado",
            icono = Icons.Filled.Security,
            seleccionado = ajustes.totpManualAlgoritmo.removePrefix("Hmac"),
            opciones = listOf("HmacSHA1", "HmacSHA256", "HmacSHA512").map { valor ->
                OpcionAjuste(valor, valor.removePrefix("Hmac"), Icons.Filled.Security)
            },
            alSeleccionar = { valor -> haptica.tic(); vm.ajustarTotpManualAlgoritmo(valor) }
        )
        Spacer(Modifier.height(10.dp))
        FilaAjuste(
            titulo = "Separar códigos de 6 dígitos",
            descripcion = "Muestra 123 456 en lugar de 123456.",
            activo = ajustes.totpSepararDigitos,
            alCambiar = { haptica.tic(); vm.ajustarTotpSepararDigitos(it) }
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Los códigos dependen de la hora del teléfono. Activa fecha y hora automáticas de Android si un código no es aceptado.",
            color = TextoSecundario,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
