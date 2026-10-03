package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
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
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Peligro

@Composable
fun BarraSuperiorDetalle(
    esFavorito: Boolean,
    conSeparador: Boolean,
    alVolver: () -> Unit,
    alCompartirQr: () -> Unit,
    alAlternarFavorito: () -> Unit,
    alMoverAPapelera: () -> Unit,
    alIrCopiaRapida: () -> Unit,
    alIrFormatosCampos: () -> Unit,
    alIrSeguridadDatos: () -> Unit,
    alIrColoresIds: () -> Unit
) {
    var menuAbierto by remember { mutableStateOf(false) }

    BarraSuperiorPantalla(
        titulo = "",
        alVolver = alVolver,
        conSeparador = conSeparador,
        colorFondo = ColorAjustesFondo,
        acciones = {
            BotonIconoCabecera(
                onClick = alCompartirQr,
                icono = Icons.Filled.QrCode,
                descripcion = "Compartir por código QR"
            )
            BotonIconoCabecera(
                onClick = alAlternarFavorito,
                icono = Icons.Filled.Star,
                descripcion = if (esFavorito) "Quitar de favoritos" else "Marcar como favorito",
                tint = if (esFavorito) ColorAcento else ColorIconosInternos.copy(alpha = 0.4f)
            )
            BotonIconoCabecera(
                onClick = alMoverAPapelera,
                icono = Icons.Filled.Delete,
                descripcion = "Mover a papelera",
                tint = Peligro
            )
            Box {
                BotonIconoCabecera(
                    onClick = { menuAbierto = true },
                    icono = Icons.Default.MoreVert,
                    descripcion = "Más opciones"
                )

                MenuDesplegableBoveda(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false },
                    modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
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
                        texto = "Seguridad de pantalla...",
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
                }
            }
        }
    )
}
