/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

@Composable
fun TarjetaTotpDetalle(
    entrada: Entrada,
    ajustes: AjustesApp,
    haptica: Haptica,
    alCopiarTotp: (String) -> Unit,
    alIntercambiarTotp: ((String) -> Unit)? = null
) {
    val tienePrincipal = !entrada.secretoTotp.isNullOrBlank()
    val camposTotp = remember(entrada.camposPersonalizados) {
        entrada.camposPersonalizados.filter {
            it.etiqueta.contains("TOTP", ignoreCase = true) && it.valor.isNotBlank()
        }
    }

    if (!tienePrincipal && camposTotp.isEmpty()) return

    var ahora by remember { mutableLongStateOf(System.currentTimeMillis() / 1000) }

    LaunchedEffect(Unit) {
        while (true) {
            ahora = System.currentTimeMillis() / 1000
            delay(500)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Verificación en dos pasos")

        if (tienePrincipal) {
            BloqueTotpItem(
                etiqueta = "CÓDIGO TEMPORAL (TOTP)",
                secreto = entrada.secretoTotp,
                digitos = entrada.totpDigitos,
                periodo = entrada.totpPeriodo.toLong(),
                algoritmo = entrada.totpAlgoritmo,
                ajustes = ajustes,
                haptica = haptica,
                ahora = ahora,
                alCopiar = alCopiarTotp
            )
        }

        camposTotp.forEachIndexed { index, campo ->
            if (tienePrincipal || index > 0) {
                Spacer(Modifier.height(10.dp))
            }
            BloqueTotpItem(
                etiqueta = campo.etiqueta.uppercase(),
                secreto = campo.valor.trim(),
                digitos = 6,
                periodo = 30L,
                algoritmo = "HmacSHA1",
                ajustes = ajustes,
                haptica = haptica,
                ahora = ahora,
                alCopiar = alCopiarTotp,
                alIntercambiar = if (alIntercambiarTotp != null) {
                    { alIntercambiarTotp(campo.id) }
                } else null
            )
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaTotpDetallePreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        val contexto = androidx.compose.ui.platform.LocalContext.current
        TarjetaTotpDetalle(
            entrada = com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo,
            ajustes = com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.ajustes,
            haptica = remember { Haptica(contexto) },
            alCopiarTotp = {}
        )
    }
}
