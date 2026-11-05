/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.InsigniaPildora
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorFavorito
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Fila individual para una credencial CXF que va a ser importada.
 * Incorpora check circular tipo lista principal, título, usuario, contraseña con
 * seguridad visual, fechas de creación/modificación de Google, tags y chips de tipo.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilaConfirmacionCxf(
    item: ItemSeleccionableCxf,
    alAlternar: () -> Unit,
    seguridadVisualActiva: Boolean = false,
    ocultarContrasena: Boolean = false,
    estiloOcultamiento: String = "desenfoque",
    tiempoAutoOcultarSegundos: Int = 0,
    modifier: Modifier = Modifier
) {
    ContenedorTarjeta(
        onClick = alAlternar,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.Top
        ) {
            CheckCircularSeleccionCxf(
                seleccionado = item.seleccionada,
                alAlternar = alAlternar,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Título con estrella de favorito si corresponde y badge de existencia
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (item.entrada.favorito) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Favorito",
                            tint = ColorFavorito,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    TextoTitulo(
                        texto = item.entrada.titulo.ifBlank { "Sin título" },
                        estilo = EstiloTitulo.PEQUENO,
                        maxLineas = 1,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    if (item.yaExisteEnBoveda) {
                        InsigniaPildora(
                            texto = "Ya en bóveda",
                            colorAcento = Peligro,
                            mostrarPunto = false
                        )
                    }
                }

                // Usuario / correo si existe
                if (item.entrada.usuario.isNotBlank()) {
                    TextoSubtitulo(
                        texto = item.entrada.usuario,
                        maxLineas = 1
                    )
                }

                // Contraseña con soporte para Seguridad Visual
                if (item.entrada.contrasena.isNotBlank()) {
                    FilaContrasenaImportacionCxf(
                        contrasena = item.entrada.contrasena,
                        seguridadVisualActiva = seguridadVisualActiva,
                        ocultarContrasena = ocultarContrasena,
                        estiloOcultamiento = estiloOcultamiento,
                        tiempoAutoOcultarSegundos = tiempoAutoOcultarSegundos
                    )
                }

                // Notas si fueron provistas por Google
                if (item.entrada.notas.isNotBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Notes,
                            contentDescription = null,
                            tint = ColorAjusteGris,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = item.entrada.notas,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextoSecundario,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Metadatos provistos por Google: fechas de creación, modificación y etiquetas
                FilaMetadatosImportacionCxf(entrada = item.entrada)

                Spacer(Modifier.height(2.dp))

                // Etiquetas de tipo de credencial
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (item.entrada.passkey != null) {
                        MiniChipCxf("Llave de acceso", ColorPasskeys)
                    }
                    if (item.entrada.contrasena.isNotBlank()) {
                        MiniChipCxf("Contraseña", Color(0xFF2196F3))
                    }
                    if (!item.entrada.secretoTotp.isNullOrBlank()) {
                        MiniChipCxf("Dos pasos", Color2FA)
                    }
                }
            }
        }
    }
}
