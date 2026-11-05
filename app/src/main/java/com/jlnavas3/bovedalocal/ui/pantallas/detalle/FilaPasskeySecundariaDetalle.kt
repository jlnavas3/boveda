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
import com.jlnavas3.bovedalocal.data.DatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FilaPasskeySecundariaDetalle(
    passkey: DatosPasskey,
    onEliminar: () -> Unit,
    onSolicitarRestaurar: () -> Unit,
    modifier: Modifier = Modifier,
    fechaEntradaFallback: Long = 0L
) {
    val rpName = passkey.rpName.trim()
    val rpId = passkey.rpId.trim()
    val tituloPrincipal = if (rpName.isNotBlank()) rpName else rpId.ifBlank { "Llave de acceso" }
    val subtitulo = if (rpName.isNotBlank() && rpId.isNotBlank() && !rpName.equals(rpId, ignoreCase = true)) {
        "Dominio: $rpId"
    } else null
    val usuario = passkey.usuario.trim()

    TarjetaDatoDetalle(
        colorBorde = ColorDatosPasskey,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 10.dp, top = 12.dp, bottom = 12.dp)
        ) {
            // Fila superior: Título a la izquierda y botones de acción arriba a la derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tituloPrincipal,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = ColorTitulos,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BotonIconoDetalle(
                        icono = Icons.Filled.Delete,
                        descripcion = "Eliminar llave de acceso secundaria",
                        alPulsar = onEliminar,
                        tint = Peligro.copy(alpha = 0.7f)
                    )
                    BotonIconoDetalle(
                        icono = Icons.Filled.Restore,
                        descripcion = "Establecer como llave de acceso principal",
                        alPulsar = onSolicitarRestaurar,
                        tint = TextoSecundario
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // Fila inferior: Datos técnicos a la izquierda y Badge de fecha en esquina inferior derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (subtitulo != null) {
                        Text(
                            text = subtitulo,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextoSecundario
                        )
                        Spacer(Modifier.height(2.dp))
                    }
                    if (usuario.isNotBlank()) {
                        Text(
                            text = "Cuenta: $usuario",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextoSecundario
                        )
                        Spacer(Modifier.height(2.dp))
                    }
                    Text(
                        text = "Algoritmo: ${passkey.algoritmo} (P-256)",
                        style = MaterialTheme.typography.labelSmall,
                        color = ColorDatosPasskey
                    )
                }

                val fechaAMostrar = if (passkey.creadaEn > 0L) passkey.creadaEn else fechaEntradaFallback
                if (fechaAMostrar > 0L) {
                    val fechaCorta = remember(fechaAMostrar) {
                        SimpleDateFormat("dd/MM/yy HH:mm", Locale.forLanguageTag("es-ES")).format(Date(fechaAMostrar))
                    }
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ColorDatosPasskey.copy(alpha = 0.12f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = fechaCorta,
                            color = ColorDatosPasskey,
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
