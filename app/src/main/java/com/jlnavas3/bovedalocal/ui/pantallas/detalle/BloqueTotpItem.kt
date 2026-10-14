/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.IndicadorTotpTarta
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

@Composable
fun BloqueTotpItem(
    etiqueta: String,
    secreto: String,
    digitos: Int = 6,
    periodo: Long = 30L,
    algoritmo: String = "HmacSHA1",
    ajustes: AjustesApp,
    haptica: Haptica,
    ahora: Long,
    alCopiar: (String) -> Unit,
    alIntercambiar: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val periodoValido = periodo.coerceAtLeast(10L)
    val codigo = remember(ahora / periodoValido, secreto, digitos, algoritmo) {
        try {
            Totp.codigo(
                secreto = Base32.decodificar(secreto),
                segundosUnix = ahora,
                digitos = digitos,
                periodo = periodoValido,
                algoritmo = algoritmo
            )
        } catch (e: Exception) {
            "------"
        }
    }

    val segundosRestantes = Totp.segundosRestantes(ahora, periodoValido)
    val codigoVisible = if (ajustes.totpSepararDigitos && codigo.length == 6) {
        "${codigo.take(3)} ${codigo.drop(3)}"
    } else {
        codigo
    }

    val protegerTotp = ajustes.seguridadVisualActiva && ajustes.ocultarTotp
    var totpRevelado by remember(secreto, protegerTotp) { mutableStateOf(!protegerTotp) }

    TemporizadorAutoOcultar(
        revelado = totpRevelado && protegerTotp,
        tiempoSegundos = ajustes.tiempoAutoOcultarSegundos,
        alAutoOcultar = { totpRevelado = false }
    )

    var copiado by remember { mutableStateOf(false) }
    LaunchedEffect(copiado) {
        if (copiado) {
            delay(1500)
            copiado = false
        }
    }

    TarjetaDatoDetalle(
        colorBorde = ColorDatos2FA,
        alPulsar = {
            haptica.exito()
            copiado = true
            alCopiar(codigo)
        },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 10.dp, top = 14.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = etiqueta,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color2FA
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextoSeguroVisual(
                        texto = codigoVisible,
                        oculto = protegerTotp && !totpRevelado,
                        estilo = ajustes.estiloOcultamientoVisual,
                        estiloTexto = EstiloMonoGrande.copy(fontWeight = FontWeight.Bold, fontSize = 26.sp),
                        colorTexto = ColorTitulos
                    )
                    IndicadorTotpTarta(
                        segundosRestantes = segundosRestantes,
                        periodo = periodoValido,
                        tamano = 18.dp
                    )
                }
                Spacer(Modifier.height(3.dp))
                Text(
                    text = "Toca para copiar · Se renueva en $segundosRestantes s",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (alIntercambiar != null) {
                    BotonIconoDetalle(
                        icono = Icons.Filled.SwapVert,
                        descripcion = "Establecer como principal",
                        alPulsar = alIntercambiar
                    )
                }
                if (protegerTotp) {
                    BotonIconoDetalle(
                        icono = if (totpRevelado) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        descripcion = if (totpRevelado) "Ocultar código" else "Mostrar código",
                        alPulsar = {
                            haptica.toque()
                            totpRevelado = !totpRevelado
                        }
                    )
                }
                BotonCopiarDetalle(
                    copiado = copiado,
                    alPulsar = {
                        haptica.exito()
                        copiado = true
                        alCopiar(codigo)
                    }
                )
            }
        }
    }
}
