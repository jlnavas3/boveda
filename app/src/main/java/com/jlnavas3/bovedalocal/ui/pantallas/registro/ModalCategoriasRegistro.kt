package com.jlnavas3.bovedalocal.ui.pantallas.registro

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.FilaOpcionModal
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
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
