package com.jlnavas3.bovedalocal.ui.pantallas.indice

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.IndiceAlfabetico

@Composable
fun IndiceAlfabeticoCalibracion(
    ajustes: AjustesApp,
    alCambiarLetra: (Char?) -> Unit,
    modifier: Modifier = Modifier
) {
    IndiceAlfabetico(
        alSeleccionarLetra = alCambiarLetra,
        alCambiarLetraActiva = alCambiarLetra,
        incluirEnie = ajustes.indiceIncluirEnie,
        efectoOla = ajustes.indiceEfectoOla,
        amplitudOlaDp = ajustes.indiceAmplitudOlaDp,
        radioOlaDp = ajustes.indiceRadioOlaDp,
        escalaMaximaLetras = ajustes.indiceEscalaLetras,
        mostrarCirculo = ajustes.indiceMostrarCirculo,
        tamanoCirculoDp = ajustes.indiceTamanoCirculoDp,
        offsetCirculoDp = ajustes.indiceOffsetCirculoDp,
        hapticaActiva = ajustes.indiceHaptica,
        anchoZonaTactilDp = ajustes.indiceAnchoTactilDp,
        tonoLetras = ajustes.indiceTonoLetras,
        modifier = modifier
    )
}
