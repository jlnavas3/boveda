/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.TotpSecundario
import com.jlnavas3.bovedalocal.ui.componentes.IndicadorTotpTarta
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TemporizadorAutoOcultar
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FilaTotpSecundarioDetalle(
    totp: TotpSecundario,
    ajustes: AjustesApp?,
    haptica: Haptica,
    ahora: Long,
    alCopiarTotp: (String) -> Unit,
    onSolicitarRestaurar: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val periodoValido = totp.periodo.toLong().coerceAtLeast(10L)
    val codigo = remember(ahora / periodoValido, totp.secreto, totp.digitos, totp.algoritmo) {
        try {
            Totp.codigo(
                secreto = Base32.decodificar(totp.secreto),
                segundosUnix = ahora,
                digitos = totp.digitos,
                periodo = periodoValido,
                algoritmo = totp.algoritmo
            )
        } catch (_: Exception) {
            "------"
        }
    }

    val segundosRestantes = Totp.segundosRestantes(ahora, periodoValido)
    val codigoVisible = if (ajustes?.totpSepararDigitos == true && codigo.length == 6) {
        "${codigo.take(3)} ${codigo.drop(3)}"
    } else {
        codigo
    }

    val protegerTotp = (ajustes?.seguridadVisualActiva == true) && (ajustes.ocultarTotp)
    var totpRevelado by remember(totp.secreto, protegerTotp) { mutableStateOf(!protegerTotp) }

    TemporizadorAutoOcultar(
        revelado = totpRevelado && protegerTotp,
        tiempoSegundos = ajustes?.tiempoAutoOcultarSegundos ?: 15,
        alAutoOcultar = { totpRevelado = false }
    )

    var copiado by remember { mutableStateOf(false) }
    LaunchedEffect(copiado) {
        if (copiado) {
            delay(1500)
            copiado = false
        }
    }

    val etiqueta = if (totp.emisor.isNotBlank()) totp.emisor.uppercase() else "CÓDIGO TEMPORAL (2FA)"
    val estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"

    TarjetaDatoDetalle(
        colorBorde = ColorDatos2FA,
        alPulsar = {
            haptica.exito()
            copiado = true
            alCopiarTotp(codigo)
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 10.dp, top = 12.dp, bottom = 12.dp)
        ) {
            // Fila superior: Emisor/etiqueta a la izquierda y botones de acción arriba a la derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = etiqueta,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color2FA,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BotonIconoDetalle(
                        icono = Icons.Filled.Delete,
                        descripcion = "Eliminar código 2FA secundario",
                        alPulsar = onEliminar,
                        tint = Peligro.copy(alpha = 0.7f)
                    )
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
                            alCopiarTotp(codigo)
                        }
                    )
                    BotonIconoDetalle(
                        icono = Icons.Filled.Restore,
                        descripcion = "Establecer como código 2FA principal",
                        alPulsar = onSolicitarRestaurar,
                        tint = TextoSecundario
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // Fila inferior: Código 2FA y subtítulo a la izquierda, Badge de fecha en esquina inferior derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextoSeguroVisual(
                            texto = codigoVisible,
                            oculto = protegerTotp && !totpRevelado,
                            estilo = estiloOcultamiento,
                            estiloTexto = EstiloMonoGrande.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp),
                            colorTexto = ColorTitulos
                        )
                        IndicadorTotpTarta(
                            segundosRestantes = segundosRestantes,
                            periodo = periodoValido,
                            tamano = 18.dp
                        )
                    }
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Toca para copiar · Se renueva en $segundosRestantes s",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }

                if (totp.agregadoEn > 0L) {
                    val fechaCorta = remember(totp.agregadoEn) {
                        SimpleDateFormat("dd/MM/yy HH:mm", Locale.forLanguageTag("es-ES")).format(Date(totp.agregadoEn))
                    }
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color2FA.copy(alpha = 0.12f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = fechaCorta,
                            color = Color2FA,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}
