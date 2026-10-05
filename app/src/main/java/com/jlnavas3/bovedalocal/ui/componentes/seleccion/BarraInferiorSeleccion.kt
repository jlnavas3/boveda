package com.jlnavas3.bovedalocal.ui.componentes.seleccion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
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
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
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
 * - Listado principal: 'Favorito', 'Compara', 'Renombra', 'Colección' y menú 'Más' (3 puntos) con Respaldar, Transferir y Eliminar.
 * - Pantallas como Salud y Duplicados: 'Favorito', 'Compara' y 'Eliminar' directamente con su texto debajo.
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
    alAsignarCategoria: (() -> Unit)? = null,
    alAsignarColeccion: (() -> Unit)? = null,
    alBorrar: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = Haptica(contexto)
    val habilitado = cantidad > 0
    val accionCategoria = alAsignarCategoria ?: alAsignarColeccion

    // Si existen acciones avanzadas de respaldo/transferencia junto con borrado, agrupamos en menú "Más"
    val hayMenuOverflow = (alRespaldar != null || alTransferirCxf != null) && (alRenombrar != null || accionCategoria != null)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .shadow(16.dp, RoundedCornerShape(CurvaturaEsquinas))
            .clip(RoundedCornerShape(CurvaturaEsquinas))
            .background(ColorTarjetaAjustes)
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, RoundedCornerShape(CurvaturaEsquinas))
                } else Modifier
            )
            .padding(horizontal = 8.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Favorito (Alternar favorito a toda la selección)
            if (alAlternarFavoritos != null) {
                ItemAccionSeleccion(
                    icono = Icons.Filled.Star,
                    texto = if (todosSonFavoritos) "Quitar fav" else "Favorito",
                    habilitado = habilitado,
                    colorIcono = if (todosSonFavoritos) TextoPrincipal else ColorAcento,
                    alPulsar = {
                        haptica.tic()
                        alAlternarFavoritos()
                    }
                )
            }

            // 2. Comparar (habilitado solo cuando exactamente 2 están seleccionados)
            if (alComparar != null) {
                val puedeComparar = cantidad == 2
                ItemAccionSeleccion(
                    icono = Icons.AutoMirrored.Filled.CompareArrows,
                    texto = "Compara",
                    habilitado = puedeComparar,
                    colorIcono = ColorAcento,
                    alPulsar = {
                        haptica.toque()
                        alComparar()
                    }
                )
            }

            // 3. Renombrar
            if (alRenombrar != null) {
                ItemAccionSeleccion(
                    icono = Icons.Filled.Edit,
                    texto = "Renombra",
                    habilitado = habilitado,
                    colorIcono = ColorAcento,
                    alPulsar = {
                        haptica.tic()
                        alRenombrar()
                    }
                )
            }

            // 4. Categoría
            if (accionCategoria != null) {
                ItemAccionSeleccion(
                    icono = Icons.Filled.Folder,
                    texto = "Categoría",
                    habilitado = habilitado,
                    colorIcono = ColorAcento,
                    alPulsar = {
                        haptica.tic()
                        accionCategoria()
                    }
                )
            }

            // 5. Menú "Más" de 3 puntos (si aplica overflow) o acciones directas
            if (hayMenuOverflow) {
                MenuOverflowSeleccion(
                    habilitado = habilitado,
                    alRespaldar = alRespaldar,
                    alTransferirCxf = alTransferirCxf,
                    alBorrar = alBorrar,
                    haptica = haptica
                )
            } else {
                // Modo sin overflow (p. ej. en Salud y Duplicados)
                if (alRespaldar != null) {
                    ItemAccionSeleccion(
                        icono = Icons.Filled.Backup,
                        texto = "Respaldar",
                        habilitado = habilitado,
                        colorIcono = ColorAcento,
                        alPulsar = {
                            haptica.tic()
                            alRespaldar()
                        }
                    )
                }

                if (alTransferirCxf != null) {
                    ItemAccionSeleccion(
                        icono = Icons.Filled.VpnKey,
                        texto = "Transferir",
                        habilitado = habilitado,
                        colorIcono = ColorPasskeys,
                        alPulsar = {
                            haptica.toque()
                            alTransferirCxf()
                        }
                    )
                }

                if (alBorrar != null) {
                    ItemAccionSeleccion(
                        icono = Icons.Filled.Delete,
                        texto = "Eliminar",
                        habilitado = habilitado,
                        colorIcono = Peligro,
                        colorTexto = Peligro,
                        alPulsar = {
                            haptica.error()
                            alBorrar()
                        }
                    )
                }
            }
        }
    }
}
