package com.jlnavas3.bovedalocal.ui.pantallas.salud

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
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
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Microcomponente de la barra superior para PantallaSaludBoveda.
 * Contiene controles de búsqueda, visor de auditoría y menú desplegable de herramientas.
 */
@Composable
fun BarraSuperiorSalud(
    mostrarId: Boolean,
    busquedaVisible: Boolean,
    textoBusqueda: String,
    haptica: Haptica,
    alVolver: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alAbrirAuditoria: () -> Unit,
    alIrDuplicados: () -> Unit,
    alIrSeguridad: () -> Unit,
    alIrGenerador: () -> Unit
) {
    BarraSuperiorPantalla(
        titulo = "Salud",
        idEtiqueta = "03-LST-SLD",
        mostrarId = mostrarId,
        alVolver = alVolver,
        colorFondo = ColorAjustesFondo,
        acciones = {
            BotonIconoCabecera(
                onClick = {
                    haptica.toque()
                    alAlternarBusqueda()
                },
                icono = Icons.Filled.Search,
                descripcion = "Buscar",
                tint = if (busquedaVisible || textoBusqueda.isNotBlank()) ColorAcento else ColorIconosInternos
            )
            BotonIconoCabecera(
                onClick = {
                    haptica.toque()
                    alAbrirAuditoria()
                },
                icono = Icons.Filled.Analytics,
                descripcion = "Resumen de auditoría",
                tint = ColorAcento
            )
            var menuAbiertoSalud by remember { mutableStateOf(false) }
            Box {
                BotonIconoCabecera(
                    onClick = { menuAbiertoSalud = true },
                    icono = Icons.Filled.MoreVert,
                    descripcion = "Más opciones"
                )

                MenuDesplegableBoveda(
                    expanded = menuAbiertoSalud,
                    onDismissRequest = { menuAbiertoSalud = false },
                    modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
                ) {
                    ElementoMenuCompacto(
                        texto = "Duplicados...",
                        icono = Icons.Filled.ContentCopy,
                        colorIcono = ColorAcento,
                        onClick = {
                            menuAbiertoSalud = false
                            alIrDuplicados()
                        }
                    )
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = "Ajustes de seguridad...",
                        icono = Icons.Filled.Security,
                        colorIcono = ColorAcento,
                        onClick = {
                            menuAbiertoSalud = false
                            alIrSeguridad()
                        }
                    )
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = "Generador de contraseñas...",
                        icono = Icons.Filled.Key,
                        colorIcono = ColorAcento,
                        onClick = {
                            menuAbiertoSalud = false
                            alIrGenerador()
                        }
                    )
                }
            }
        }
    )
}
