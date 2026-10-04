package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

@Composable
fun TarjetaAjuste(
    titulo: String,
    icono: ImageVector,
    descripcion: String,
    inicialmenteAbierta: Boolean = false,
    colorIcono: Color = ColorIconosInternos,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    abiertaControlada: Boolean? = null,
    alAlternarAbierta: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier,
    contenido: @Composable ColumnScope.() -> Unit
) {
    TarjetaBovedaDesplegable(
        titulo = titulo,
        icono = icono,
        descripcion = descripcion,
        inicialmenteAbierta = inicialmenteAbierta,
        colorIcono = colorIcono,
        idEtiqueta = idEtiqueta,
        mostrarId = mostrarId,
        abiertaControlada = abiertaControlada,
        alAlternarAbierta = alAlternarAbierta,
        modifier = modifier,
        contenido = contenido
    )
}
