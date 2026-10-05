package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.data.normalizarEtiqueta
import com.jlnavas3.bovedalocal.ui.componentes.ChipBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes

@Composable
fun ChipEtiqueta(texto: String, sugerida: Boolean = false, alPulsar: () -> Unit) {
    ChipBoveda(
        texto = "#${normalizarEtiqueta(texto)}",
        colorFondoPersonalizado = if (sugerida) ColorTarjetaAjustes else ColorCampoAjustes,
        alPulsar = alPulsar,
        alRemover = if (!sugerida) alPulsar else null
    )
}

