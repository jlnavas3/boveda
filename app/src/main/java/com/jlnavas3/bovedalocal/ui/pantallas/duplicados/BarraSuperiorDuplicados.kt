package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
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
 * Microcomponente de la barra superior para PantallaDuplicados.
 */
@Composable
fun BarraSuperiorDuplicados(
    mostrarId: Boolean,
    busquedaVisible: Boolean,
    textoBusqueda: String,
    totalSobrantesIdenticas: Int,
    haptica: Haptica,
    alVolver: () -> Unit,
    alAlternarBusqueda: () -> Unit,
    alPedirLimpiezaMasiva: () -> Unit,
    alIrSalud: () -> Unit,
    alIrCopia: () -> Unit
) {
    BarraSuperiorPantalla(
        titulo = "Duplicados",
        idEtiqueta = "03-LST-DUP",
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
            if (totalSobrantesIdenticas > 0) {
                BotonIconoCabecera(
                    onClick = {
                        haptica.toque()
                        alPedirLimpiezaMasiva()
                    },
                    icono = Icons.Filled.AutoFixHigh,
                    descripcion = "Limpieza rápida masiva",
                    tint = ColorAcento
                )
            }
            var menuAbiertoDup by remember { mutableStateOf(false) }
            Box {
                BotonIconoCabecera(
                    onClick = { menuAbiertoDup = true },
                    icono = Icons.Filled.MoreVert,
                    descripcion = "Más opciones"
                )

                MenuDesplegableBoveda(
                    expanded = menuAbiertoDup,
                    onDismissRequest = { menuAbiertoDup = false },
                    modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
                ) {
                    ElementoMenuCompacto(
                        texto = "Salud...",
                        icono = Icons.Filled.HealthAndSafety,
                        colorIcono = ColorAcento,
                        onClick = {
                            menuAbiertoDup = false
                            alIrSalud()
                        }
                    )
                    SeparadorOpcionMenu()
                    ElementoMenuCompacto(
                        texto = "Copia preventiva...",
                        icono = Icons.Filled.Backup,
                        colorIcono = ColorAcento,
                        onClick = {
                            menuAbiertoDup = false
                            alIrCopia()
                        }
                    )
                }
            }
        }
    )
}
