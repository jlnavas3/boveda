/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.componentes.seleccion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMerge
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Advertencia
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorAnclado
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorFavorito
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Barra inferior de acciones contextuales para selección múltiple (estilo Solid Explorer / Bottom Action Bar).
 * Presenta iconos ergonómicos con texto centrado debajo:
 * - Listado principal: 'Favorito', 'Anclar', 'Renombra', 'Categoría' y menú 'Más' (3 puntos) con Comparar, Respaldar, Transferir y Eliminar.
 * - Pantallas como Salud y Duplicados: 'Favorito', 'Compara' y 'Eliminar' directamente con su texto debajo.
 */
@Composable
fun BarraInferiorSeleccion(
    cantidad: Int,
    todosSonFavoritos: Boolean = false,
    alAlternarFavoritos: (() -> Unit)? = null,
    todosSonAnclados: Boolean = false,
    alAlternarAnclados: (() -> Unit)? = null,
    alComparar: (() -> Unit)? = null,
    alRespaldar: (() -> Unit)? = null,
    alTransferirCxf: (() -> Unit)? = null,
    alRenombrar: (() -> Unit)? = null,
    alAsignarCategoria: (() -> Unit)? = null,
    alAsignarColeccion: (() -> Unit)? = null,
    alAlternarIgnorarAuditoria: (() -> Unit)? = null,
    todosIgnorados: Boolean = false,
    alUnificar: (() -> Unit)? = null,
    alBorrar: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = Haptica(contexto)
    val habilitado = cantidad > 0
    val accionCategoria = alAsignarCategoria ?: alAsignarColeccion

    // Si existen acciones avanzadas de respaldo/transferencia junto con borrado, agrupamos en menú "Más"
    val hayMenuOverflow = (alRespaldar != null || alTransferirCxf != null) && (alRenombrar != null || accionCategoria != null)

    Surface(
        color = com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo,
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(
                        width = GrosorBorde,
                        color = ColorBordeActual.copy(alpha = 0.5f)
                    )
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
            // 1. Favorito (Alternar favorito a toda la selección con estrella amarilla)
            if (alAlternarFavoritos != null) {
                BotonAccionSeleccionIcono(
                    icono = Icons.Filled.Star,
                    descripcion = if (todosSonFavoritos) "Quitar fav" else "Favorito",
                    habilitado = habilitado,
                    colorIcono = if (todosSonFavoritos) ColorFavorito else ColorIconosInternos,
                    alPulsar = {
                        haptica.tic()
                        alAlternarFavoritos()
                    }
                )
            }

            // 2. Anclar (o Comparar en pantallas secundarias sin anclado)
            if (alAlternarAnclados != null) {
                BotonAccionSeleccionIcono(
                    icono = Icons.Filled.PushPin,
                    descripcion = if (todosSonAnclados) "Desanclar" else "Anclar",
                    habilitado = habilitado,
                    colorIcono = if (todosSonAnclados) ColorAnclado else ColorIconosInternos,
                    alPulsar = {
                        haptica.tic()
                        alAlternarAnclados()
                    }
                )
            } else if (alComparar != null && !hayMenuOverflow) {
                val puedeComparar = cantidad >= 2
                BotonAccionSeleccionIcono(
                    icono = Icons.AutoMirrored.Filled.CompareArrows,
                    descripcion = "Comparar",
                    habilitado = puedeComparar,
                    colorIcono = ColorIconosInternos,
                    alPulsar = {
                        haptica.toque()
                        alComparar()
                    }
                )
            }

            // 3. Renombrar
            if (alRenombrar != null) {
                BotonAccionSeleccionIcono(
                    icono = Icons.Filled.Edit,
                    descripcion = "Renombrar",
                    habilitado = habilitado,
                    colorIcono = ColorIconosInternos,
                    alPulsar = {
                        haptica.tic()
                        alRenombrar()
                    }
                )
            }

            // 4. Categoría
            if (accionCategoria != null) {
                BotonAccionSeleccionIcono(
                    icono = Icons.Filled.Folder,
                    descripcion = "Categoría",
                    habilitado = habilitado,
                    colorIcono = ColorIconosInternos,
                    alPulsar = {
                        haptica.tic()
                        accionCategoria()
                    }
                )
            }

            // 5. Eliminar (afuera directamente en la barra con color neutro)
            if (alBorrar != null) {
                BotonAccionSeleccionIcono(
                    icono = Icons.Filled.Delete,
                    descripcion = "Eliminar",
                    habilitado = habilitado,
                    colorIcono = ColorIconosInternos,
                    alPulsar = {
                        haptica.error()
                        alBorrar()
                    }
                )
            }

            // 6. Menú "Más" de 3 puntos (si aplica overflow) o acciones directas
            if (hayMenuOverflow) {
                MenuOverflowSeleccion(
                    habilitado = habilitado,
                    alComparar = alComparar,
                    puedeComparar = cantidad >= 2,
                    alRespaldar = alRespaldar,
                    alTransferirCxf = alTransferirCxf,
                    alUnificar = alUnificar,
                    alAlternarIgnorarAuditoria = alAlternarIgnorarAuditoria,
                    todosIgnorados = todosIgnorados,
                    alBorrar = null,
                    haptica = haptica
                )
            } else {
                // Modo sin overflow (p. ej. en Salud y Duplicados)
                if (alUnificar != null) {
                    val puedeUnificar = cantidad >= 2
                    BotonAccionSeleccionIcono(
                        icono = Icons.AutoMirrored.Filled.CallMerge,
                        descripcion = "Unificar seleccionados",
                        habilitado = puedeUnificar,
                        colorIcono = ColorIconosInternos,
                        alPulsar = {
                            haptica.toque()
                            alUnificar()
                        }
                    )
                }
                if (alRespaldar != null) {
                    BotonAccionSeleccionIcono(
                        icono = Icons.Filled.Backup,
                        descripcion = "Respaldar seleccionados",
                        habilitado = habilitado,
                        colorIcono = ColorIconosInternos,
                        alPulsar = {
                            haptica.tic()
                            alRespaldar()
                        }
                    )
                }

                if (alTransferirCxf != null) {
                    BotonAccionSeleccionIcono(
                        icono = Icons.Filled.VpnKey,
                        descripcion = "Transferir credenciales",
                        habilitado = habilitado,
                        colorIcono = ColorIconosInternos,
                        alPulsar = {
                            haptica.toque()
                            alTransferirCxf()
                        }
                    )
                }
            }
        }
    }
}
}

