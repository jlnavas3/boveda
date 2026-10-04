package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import com.jlnavas3.bovedalocal.data.Entrada

data class ItemSeleccionableCxf(
    val entrada: Entrada,
    var seleccionada: Boolean,
    val yaExisteEnBoveda: Boolean
)
