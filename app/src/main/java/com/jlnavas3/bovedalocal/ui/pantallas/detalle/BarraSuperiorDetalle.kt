package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Peligro

@Composable
fun BarraSuperiorDetalle(
    esFavorito: Boolean,
    conSeparador: Boolean,
    alVolver: () -> Unit,
    alCompartirQr: () -> Unit,
    alAlternarFavorito: () -> Unit,
    alEditar: () -> Unit,
    alMoverAPapelera: () -> Unit
) {
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
                tint = if (esFavorito) Ambar else ColorIconosInternos.copy(alpha = 0.4f)
            )
            BotonIconoCabecera(
                onClick = alEditar,
                icono = Icons.Filled.Edit,
                descripcion = "Editar"
            )
            BotonIconoCabecera(
                onClick = alMoverAPapelera,
                icono = Icons.Filled.Delete,
                descripcion = "Mover a papelera",
                tint = Peligro
            )
        }
    )
}
