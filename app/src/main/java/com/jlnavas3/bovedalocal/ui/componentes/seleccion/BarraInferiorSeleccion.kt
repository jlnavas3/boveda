package com.jlnavas3.bovedalocal.ui.componentes.seleccion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Elemento de acción individual en la barra inferior de selección (Icono + Texto centrado debajo).
 */
@Composable
fun ItemAccionSeleccion(
    icono: ImageVector,
    texto: String,
    habilitado: Boolean,
    alPulsar: () -> Unit,
    modifier: Modifier = Modifier,
    colorIcono: Color = ColorAcento,
    colorTexto: Color = TextoPrincipal,
    descripcion: String? = null
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                enabled = habilitado,
                onClick = alPulsar
            )
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion ?: texto,
            tint = if (habilitado) colorIcono else TextoSecundario.copy(alpha = 0.35f),
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            ),
            color = if (habilitado) colorTexto else TextoSecundario.copy(alpha = 0.35f),
            maxLines = 1
        )
    }
}

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
    alAsignarColeccion: (() -> Unit)? = null,
    alBorrar: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val habilitado = cantidad > 0
    val puedeComparar = cantidad >= 2
    val forma = RoundedCornerShape(CurvaturaEsquinas)

    // Determina si las acciones secundarias deben ir en el menú desplegable "Más" de 3 puntos
    val hayMenuOverflow = (alRenombrar != null || alAsignarColeccion != null) &&
        (alRespaldar != null || alTransferirCxf != null || alBorrar != null)
    var menuMasAbierto by remember { mutableStateOf(false) }

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
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Favorito
            if (alAlternarFavoritos != null) {
                ItemAccionSeleccion(
                    icono = Icons.Filled.Star,
                    texto = "Favorito",
                    habilitado = habilitado,
                    colorIcono = ColorAcento,
                    descripcion = if (todosSonFavoritos) "Quitar de favoritos" else "Marcar como favorito",
                    alPulsar = {
                        haptica.tic()
                        alAlternarFavoritos()
                    }
                )
            }

            // 2. Compara
            if (alComparar != null) {
                ItemAccionSeleccion(
                    icono = Icons.AutoMirrored.Filled.CompareArrows,
                    texto = "Compara",
                    habilitado = puedeComparar,
                    colorIcono = ColorAcento,
                    descripcion = "Comparar entradas seleccionadas",
                    alPulsar = {
                        haptica.toque()
                        alComparar()
                    }
                )
            }

            // 3. Renombra
            if (alRenombrar != null) {
                ItemAccionSeleccion(
                    icono = Icons.Filled.Edit,
                    texto = "Renombra",
                    habilitado = habilitado,
                    colorIcono = TextoPrincipal,
                    alPulsar = {
                        haptica.tic()
                        alRenombrar()
                    }
                )
            }

            // 4. Colección
            if (alAsignarColeccion != null) {
                ItemAccionSeleccion(
                    icono = Icons.Filled.Folder,
                    texto = "Colección",
                    habilitado = habilitado,
                    colorIcono = ColorAcento,
                    alPulsar = {
                        haptica.tic()
                        alAsignarColeccion()
                    }
                )
            }

            // 5. Menú "Más" de 3 puntos (si aplica overflow)
            if (hayMenuOverflow) {
                Box {
                    ItemAccionSeleccion(
                        icono = Icons.Filled.MoreVert,
                        texto = "Más",
                        habilitado = habilitado,
                        colorIcono = TextoPrincipal,
                        alPulsar = {
                            haptica.toque()
                            menuMasAbierto = true
                        }
                    )

                    MenuDesplegableBoveda(
                        expanded = menuMasAbierto,
                        onDismissRequest = { menuMasAbierto = false },
                        modifier = Modifier.widthIn(min = 200.dp, max = 260.dp)
                    ) {
                        if (alRespaldar != null) {
                            ElementoMenuCompacto(
                                texto = "Respaldar",
                                icono = Icons.Filled.Backup,
                                colorIcono = ColorAcento,
                                onClick = {
                                    menuMasAbierto = false
                                    haptica.tic()
                                    alRespaldar()
                                }
                            )
                        }

                        if (alTransferirCxf != null) {
                            if (alRespaldar != null) SeparadorOpcionMenu()
                            ElementoMenuCompacto(
                                texto = "Transferir",
                                icono = Icons.Filled.VpnKey,
                                colorIcono = ColorPasskeys,
                                onClick = {
                                    menuMasAbierto = false
                                    haptica.toque()
                                    alTransferirCxf()
                                }
                            )
                        }

                        if (alBorrar != null) {
                            if (alRespaldar != null || alTransferirCxf != null) SeparadorOpcionMenu()
                            ElementoMenuCompacto(
                                texto = "Eliminar",
                                icono = Icons.Filled.Delete,
                                colorIcono = Peligro,
                                colorTexto = Peligro,
                                onClick = {
                                    menuMasAbierto = false
                                    haptica.error()
                                    alBorrar()
                                }
                            )
                        }
                    }
                }
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
            alAsignarColeccion = {},
            alBorrar = {}
        )
    }
}
