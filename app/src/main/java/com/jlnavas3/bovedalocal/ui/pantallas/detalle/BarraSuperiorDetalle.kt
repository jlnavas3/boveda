/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.DynamicForm
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo

@Composable
fun BarraSuperiorDetalle(
    conSeparador: Boolean,
    alVolver: () -> Unit,
    alIrCopiaRapida: () -> Unit,
    alIrFormatosCampos: () -> Unit,
    alIrSeguridadDatos: () -> Unit,
    alIrColoresIds: () -> Unit,
    alIrDatosTecnicosPasskey: (() -> Unit)? = null,
    alIrUmbralContrasenas: (() -> Unit)? = null,
    alIrContrasenasComunes: (() -> Unit)? = null,
    alIrPlantillasPersonalizadas: () -> Unit,
    alIrAyuda: () -> Unit
) {
    var menuAbierto by remember { mutableStateOf(false) }

    BarraSuperiorPantalla(
        titulo = "",
        alVolver = alVolver,
        conSeparador = conSeparador,
        colorFondo = ColorAjustesFondo,
        acciones = {
            Box {
                BotonIconoCabecera(
                    onClick = { menuAbierto = true },
                    icono = Icons.Default.MoreVert,
                    descripcion = "Más opciones"
                )

                MenuDesplegableBoveda(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false },
                    modifier = Modifier.widthIn(min = 220.dp, max = 290.dp)
                ) {
                    ElementoMenuCompacto(
                        texto = "Ajustes de copia rápida...",
                        icono = Icons.Filled.Timer,
                        onClick = {
                            menuAbierto = false
                            alIrCopiaRapida()
                        }
                    )
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = "Formatos y campos...",
                        icono = Icons.Filled.Tune,
                        onClick = {
                            menuAbierto = false
                            alIrFormatosCampos()
                        }
                    )
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = "Seguridad visual...",
                        icono = Icons.Filled.Security,
                        onClick = {
                            menuAbierto = false
                            alIrSeguridadDatos()
                        }
                    )
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = "Colores de identificadores...",
                        icono = Icons.Filled.Palette,
                        onClick = {
                            menuAbierto = false
                            alIrColoresIds()
                        }
                    )
                    if (alIrDatosTecnicosPasskey != null) {
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Datos técnicos en passkeys...",
                            icono = Icons.Filled.Fingerprint,
                            onClick = {
                                menuAbierto = false
                                alIrDatosTecnicosPasskey()
                            }
                        )
                    }
                    if (alIrUmbralContrasenas != null) {
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Umbral de contraseñas...",
                            icono = Icons.Filled.History,
                            onClick = {
                                menuAbierto = false
                                alIrUmbralContrasenas()
                            }
                        )
                    }
                    if (alIrContrasenasComunes != null) {
                        SeparadorOpcionMenu()
                        ElementoMenuCompacto(
                            texto = "Contraseñas comunes...",
                            icono = Icons.Filled.Password,
                            onClick = {
                                menuAbierto = false
                                alIrContrasenasComunes()
                            }
                        )
                    }
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = "Plantillas personalizadas...",
                        icono = Icons.Filled.DynamicForm,
                        onClick = {
                            menuAbierto = false
                            alIrPlantillasPersonalizadas()
                        }
                    )
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = "Ayuda...",
                        icono = Icons.AutoMirrored.Filled.HelpOutline,
                        onClick = {
                            menuAbierto = false
                            alIrAyuda()
                        }
                    )
                }
            }
        }
    )
}
