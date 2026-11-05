/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.edicion.notas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.StrikethroughS
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.FormatoMarkdownAccion

/**
 * Microcomponente desacoplado que renderiza la barra de herramientas horizontal
 * con botones táctiles de acceso rápido para insertar formato Markdown y alternar vista previa.
 *
 * Mantiene los accesos más frecuentes en la fila principal fija (sin necesidad de scroll)
 * y agrupa las opciones secundarias en un menú desplegable compacto.
 */
@Composable
fun BarraHerramientasFormatoNota(
    enVistaPrevia: Boolean,
    alAlternarVistaPrevia: () -> Unit,
    alAplicarFormato: (FormatoMarkdownAccion) -> Unit,
    modifier: Modifier = Modifier
) {
    var menuMasOpcionesAbierto by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón alternador de Vista Previa
        BotonChipAccion(
            texto = if (enVistaPrevia) "Editar" else "Previa",
            icono = if (enVistaPrevia) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
            destacado = enVistaPrevia,
            alPulsar = alAlternarVistaPrevia
        )

        if (!enVistaPrevia) {
            BotonChipIcono(
                icono = Icons.Filled.FormatBold,
                descripcion = "Negrita",
                alPulsar = { alAplicarFormato(FormatoMarkdownAccion.NEGRITA) }
            )

            BotonChipIcono(
                icono = Icons.Filled.FormatItalic,
                descripcion = "Cursiva",
                alPulsar = { alAplicarFormato(FormatoMarkdownAccion.CURSIVA) }
            )

            BotonChipIcono(
                icono = Icons.Filled.Title,
                descripcion = "Título principal",
                alPulsar = { alAplicarFormato(FormatoMarkdownAccion.TITULO_1) }
            )

            BotonChipTexto(
                texto = "H2",
                alPulsar = { alAplicarFormato(FormatoMarkdownAccion.TITULO_2) }
            )

            BotonChipIcono(
                icono = Icons.AutoMirrored.Filled.FormatListBulleted,
                descripcion = "Lista con viñetas",
                alPulsar = { alAplicarFormato(FormatoMarkdownAccion.LISTA_PUNTOS) }
            )

            // Menú desplegable para opciones secundarias (tabla, alineaciones, listas, código, etc.)
            Box {
                BotonChipIcono(
                    icono = Icons.Filled.MoreHoriz,
                    descripcion = "Más formatos",
                    alPulsar = { menuMasOpcionesAbierto = true }
                )

                MenuDesplegableBoveda(
                    expanded = menuMasOpcionesAbierto,
                    onDismissRequest = { menuMasOpcionesAbierto = false }
                ) {
                    ElementoMenuCompacto(
                        texto = "Tabla",
                        icono = Icons.Filled.TableChart,
                        colorIcono = ColorAcento,
                        colorTexto = TextoPrincipal,
                        onClick = {
                            menuMasOpcionesAbierto = false
                            alAplicarFormato(FormatoMarkdownAccion.TABLA)
                        }
                    )
                    ElementoMenuCompacto(
                        texto = "Centrar texto",
                        icono = Icons.Filled.FormatAlignCenter,
                        colorIcono = ColorIconosInternos,
                        colorTexto = TextoPrincipal,
                        onClick = {
                            menuMasOpcionesAbierto = false
                            alAplicarFormato(FormatoMarkdownAccion.CENTRAR)
                        }
                    )
                    ElementoMenuCompacto(
                        texto = "Alinear a la derecha",
                        icono = Icons.AutoMirrored.Filled.FormatAlignRight,
                        colorIcono = ColorIconosInternos,
                        colorTexto = TextoPrincipal,
                        onClick = {
                            menuMasOpcionesAbierto = false
                            alAplicarFormato(FormatoMarkdownAccion.ALINEAR_DERECHA)
                        }
                    )
                    ElementoMenuCompacto(
                        texto = "Lista de tareas",
                        icono = Icons.Filled.CheckBox,
                        colorIcono = ColorIconosInternos,
                        colorTexto = TextoPrincipal,
                        onClick = {
                            menuMasOpcionesAbierto = false
                            alAplicarFormato(FormatoMarkdownAccion.CHECKBOX)
                        }
                    )
                    ElementoMenuCompacto(
                        texto = "Lista numerada",
                        icono = Icons.Filled.FormatListNumbered,
                        colorIcono = ColorIconosInternos,
                        colorTexto = TextoPrincipal,
                        onClick = {
                            menuMasOpcionesAbierto = false
                            alAplicarFormato(FormatoMarkdownAccion.LISTA_NUMEROS)
                        }
                    )
                    ElementoMenuCompacto(
                        texto = "Texto tachado",
                        icono = Icons.Filled.StrikethroughS,
                        colorIcono = ColorIconosInternos,
                        colorTexto = TextoPrincipal,
                        onClick = {
                            menuMasOpcionesAbierto = false
                            alAplicarFormato(FormatoMarkdownAccion.TACHADO)
                        }
                    )
                    ElementoMenuCompacto(
                        texto = "Código monoespaciado",
                        icono = Icons.Filled.Code,
                        colorIcono = ColorIconosInternos,
                        colorTexto = TextoPrincipal,
                        onClick = {
                            menuMasOpcionesAbierto = false
                            alAplicarFormato(FormatoMarkdownAccion.CODIGO_LINEA)
                        }
                    )
                    ElementoMenuCompacto(
                        texto = "Cita en bloque",
                        icono = Icons.Filled.FormatQuote,
                        colorIcono = ColorIconosInternos,
                        colorTexto = TextoPrincipal,
                        onClick = {
                            menuMasOpcionesAbierto = false
                            alAplicarFormato(FormatoMarkdownAccion.CITA)
                        }
                    )
                    ElementoMenuCompacto(
                        texto = "Línea divisoria",
                        icono = Icons.Filled.HorizontalRule,
                        colorIcono = ColorIconosInternos,
                        colorTexto = TextoPrincipal,
                        onClick = {
                            menuMasOpcionesAbierto = false
                            alAplicarFormato(FormatoMarkdownAccion.SEPARADOR)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun BotonChipIcono(
    icono: ImageVector,
    descripcion: String,
    alPulsar: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(FormaPequena)
            .background(ColorCampoAjustes)
            .clickable(onClick = alPulsar)
            .padding(horizontal = 9.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = TextoPrincipal,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun BotonChipTexto(
    texto: String,
    alPulsar: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(FormaPequena)
            .background(ColorCampoAjustes)
            .clickable(onClick = alPulsar)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            ),
            color = TextoPrincipal
        )
    }
}

@Composable
private fun BotonChipAccion(
    texto: String,
    icono: ImageVector,
    destacado: Boolean,
    alPulsar: () -> Unit
) {
    val fondo = if (destacado) ColorAcento else ColorCampoAjustes
    val colorContenido = if (destacado) ColorSobreAcento else ColorAcento

    Row(
        modifier = Modifier
            .clip(FormaPequena)
            .background(fondo)
            .clickable(onClick = alPulsar)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = texto,
            tint = colorContenido,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = texto,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            ),
            color = colorContenido
        )
    }
}
