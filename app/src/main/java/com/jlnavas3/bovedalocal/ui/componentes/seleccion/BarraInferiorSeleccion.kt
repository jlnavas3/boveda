package com.jlnavas3.bovedalocal.ui.componentes.seleccion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Barra inferior de acciones contextuales para selección múltiple (estilo Solid Explorer / Bottom Action Bar).
 * Permite ejecutar acciones al alcance ergonómico del pulgar:
 * - Favoritos (marcar/desmarcar)
 * - Comparar (recorrido cíclico infinito entre seleccionadas)
 * - Respaldar (copia de seguridad cifrada selectiva .bvda)
 * - Renombrar (modificación por lote de título)
 * - Eliminar (enviar a papelera / borrar con confirmación)
 */
@Composable
fun BarraInferiorSeleccion(
    cantidad: Int,
    todosSonFavoritos: Boolean = false,
    alAlternarFavoritos: (() -> Unit)? = null,
    alComparar: (() -> Unit)? = null,
    alRespaldar: (() -> Unit)? = null,
    alTransferirCxf: (() -> Unit)? = null,
    alRenombrar: (() -> Unit)? = null,
    alBorrar: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = Haptica(contexto)
    val habilitado = cantidad > 0
    val puedeComparar = cantidad >= 2
    val forma = RoundedCornerShape(CurvaturaEsquinas)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 8.dp, shape = forma)
                .clip(forma)
                .background(ColorTarjetaAjustes)
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, forma)
                    } else Modifier
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Favoritos
            if (alAlternarFavoritos != null) {
                IconButton(
                    onClick = {
                        haptica.tic()
                        alAlternarFavoritos()
                    },
                    enabled = habilitado,
                    modifier = Modifier.size(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = if (todosSonFavoritos) "Quitar de favoritos" else "Marcar como favorito",
                        tint = if (!habilitado) TextoSecundario.copy(alpha = 0.35f)
                               else ColorAcento,
                        modifier = Modifier.size(23.dp)
                    )
                }
            }

            // 2. Comparar
            if (alComparar != null) {
                IconButton(
                    onClick = {
                        haptica.toque()
                        alComparar()
                    },
                    enabled = puedeComparar,
                    modifier = Modifier.size(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                        contentDescription = "Comparar entradas seleccionadas",
                        tint = if (puedeComparar) ColorAcento else TextoSecundario.copy(alpha = 0.35f),
                        modifier = Modifier.size(23.dp)
                    )
                }
            }

            // 3. Respaldar (Copia cifrada selectiva)
            if (alRespaldar != null) {
                IconButton(
                    onClick = {
                        haptica.tic()
                        alRespaldar()
                    },
                    enabled = habilitado,
                    modifier = Modifier.size(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Backup,
                        contentDescription = "Respaldar seleccionadas",
                        tint = if (habilitado) ColorAcento else TextoSecundario.copy(alpha = 0.35f),
                        modifier = Modifier.size(23.dp)
                    )
                }
            }

            // 4. Transferir (FIDO CXF selectivo)
            if (alTransferirCxf != null) {
                BotonTransferirSeleccion(
                    habilitado = habilitado,
                    alPulsar = alTransferirCxf
                )
            }

            // 5. Renombrar
            if (alRenombrar != null) {
                IconButton(
                    onClick = {
                        haptica.tic()
                        alRenombrar()
                    },
                    enabled = habilitado,
                    modifier = Modifier.size(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Renombrar",
                        tint = if (habilitado) TextoPrincipal else TextoSecundario.copy(alpha = 0.35f),
                        modifier = Modifier.size(23.dp)
                    )
                }
            }

            // 4. Eliminar
            if (alBorrar != null) {
                IconButton(
                    onClick = {
                        haptica.error()
                        alBorrar()
                    },
                    enabled = habilitado,
                    modifier = Modifier.size(46.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Eliminar seleccionadas",
                        tint = if (habilitado) Peligro else Peligro.copy(alpha = 0.35f),
                        modifier = Modifier.size(23.dp)
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun BarraInferiorSeleccionPreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        BarraInferiorSeleccion(
            cantidad = 3,
            todosSonFavoritos = false,
            alAlternarFavoritos = {},
            alComparar = {},
            alRespaldar = {},
            alTransferirCxf = {},
            alRenombrar = {},
            alBorrar = {}
        )
    }
}

