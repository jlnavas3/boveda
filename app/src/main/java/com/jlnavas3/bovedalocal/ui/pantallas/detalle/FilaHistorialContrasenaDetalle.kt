/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.CambioContrasena
import com.jlnavas3.bovedalocal.ui.componentes.contrasenaColoreada
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FilaHistorialContrasenaDetalle(
    cambio: CambioContrasena,
    revelada: Boolean,
    copiado: Boolean,
    onToggleRevelar: () -> Unit,
    onCopiar: () -> Unit,
    onEliminar: () -> Unit,
    onSolicitarRestaurar: () -> Unit,
    modifier: Modifier = Modifier,
    seguridadVisualActiva: Boolean = true,
    estiloOcultamiento: String = "desenfoque"
) {
    TarjetaDatoDetalle(
        colorBorde = ColorDatosContrasena,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 10.dp, top = 12.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = formatearFechaDetalleHistorial(cambio.cambiadaEn),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${cambio.contrasena.length} caracteres",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextoSecundario.copy(alpha = 0.8f)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    BotonIconoDetalle(
                        icono = Icons.Filled.Delete,
                        descripcion = "Eliminar contraseña anterior",
                        alPulsar = onEliminar,
                        tint = Peligro.copy(alpha = 0.7f)
                    )
                    if (seguridadVisualActiva) {
                        BotonIconoDetalle(
                            icono = if (revelada) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            descripcion = if (revelada) "Ocultar" else "Ver contraseña",
                            alPulsar = onToggleRevelar
                        )
                    }
                    BotonCopiarDetalle(
                        copiado = copiado,
                        alPulsar = onCopiar
                    )
                    BotonIconoDetalle(
                        icono = Icons.Filled.Restore,
                        descripcion = "Restaurar como activa",
                        alPulsar = onSolicitarRestaurar,
                        tint = TextoSecundario
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            val mostrarPlano = !seguridadVisualActiva || revelada
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                if (mostrarPlano) {
                    Text(
                        text = contrasenaColoreada(cambio.contrasena),
                        style = EstiloMono,
                        maxLines = 1,
                        softWrap = false
                    )
                } else {
                    TextoSeguroVisual(
                        texto = cambio.contrasena,
                        oculto = true,
                        estilo = estiloOcultamiento,
                        estiloTexto = EstiloMono.copy(letterSpacing = 2.sp),
                        colorTexto = TextoPrincipal,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        softWrap = false
                    )
                }
            }
        }
    }
}

private fun formatearFechaDetalleHistorial(milisegundos: Long): String {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
        sdf.format(Date(milisegundos))
    } catch (_: Exception) {
        "Fecha desconocida"
    }
}
