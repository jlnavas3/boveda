package com.jlnavas3.bovedalocal.ui.pantallas.registro

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.FilaOpcionModal
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun ModalCategoriasRegistro(
    categoriasOpciones: List<CategoriaOpcion>,
    categoriaSeleccionada: String,
    alSeleccionarCategoria: (String) -> Unit,
    alCerrar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    ModalInferiorBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Filtrar por categoría",
        descripcion = "Muestra solo los eventos del área seleccionada",
        icono = Icons.Filled.FilterList,
        colorIcono = ColorAcento,
        fondoIcono = ColorAcento.copy(alpha = 0.15f),
        mostrarBotonCerrar = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 420.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            categoriasOpciones.forEach { opcion ->
                val esSeleccionado = opcion.nombre == categoriaSeleccionada

                FilaOpcionModal(
                    titulo = opcion.nombre,
                    descripcion = opcion.descripcion,
                    icono = opcion.icono,
                    seleccionado = esSeleccionado,
                    colorAcento = opcion.color,
                    alPulsar = {
                        haptica.tic()
                        alSeleccionarCategoria(opcion.nombre)
                        alCerrar()
                    },
                    controlFinal = {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .border(
                                    width = if (esSeleccionado) 6.dp else 1.5.dp,
                                    color = if (esSeleccionado) opcion.color else ColorAjusteGris.copy(alpha = 0.45f),
                                    shape = CircleShape
                                )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun DialogoOrdenacionRegistro(
    criterioActual: CriterioOrdenRegistro,
    alSeleccionarCriterio: (CriterioOrdenRegistro) -> Unit,
    alCerrar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    DialogoBoveda(
        abierto = true,
        alCerrar = alCerrar,
        titulo = "Ordenar por",
        icono = Icons.AutoMirrored.Filled.Sort,
        botonConfirmar = {
            TextButton(onClick = alCerrar) {
                Text("Cerrar", color = ColorAcento)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CriterioOrdenRegistro.entries.forEach { criterio ->
                val seleccionado = criterio == criterioActual
                FilaOpcionModal(
                    titulo = criterio.etiqueta,
                    seleccionado = seleccionado,
                    icono = Icons.AutoMirrored.Filled.Sort,
                    alPulsar = {
                        haptica.tic()
                        alSeleccionarCriterio(criterio)
                        alCerrar()
                    },
                    controlFinal = if (seleccionado) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = ColorAcento,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else null
                )
            }
        }
    }
}
