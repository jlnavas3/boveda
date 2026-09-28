package com.jlnavas3.bovedalocal.ui.pantallas.generador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos

@Composable
fun BarraSuperiorGenerador(
    alVolver: () -> Unit,
    conSeparador: Boolean,
    alRegenerar: () -> Unit,
    alCopiar: () -> Unit,
    alCrearEntrada: () -> Unit,
    modifier: Modifier = Modifier,
    idEtiqueta: String = "04-HER-GEN",
    mostrarId: Boolean = false
) {
    BarraSuperiorPantalla(
        titulo = "Generador",
        idEtiqueta = idEtiqueta,
        mostrarId = mostrarId,
        alVolver = alVolver,
        conSeparador = conSeparador,
        colorFondo = ColorAjustesFondo,
        acciones = {
            // Botón Regenerar
            IconButton(
                onClick = alRegenerar,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(ColorTarjetaAjustes)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Regenerar contraseña",
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(5.dp))

            // Botón Copiar al portapapeles
            IconButton(
                onClick = alCopiar,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(ColorTarjetaAjustes)
            ) {
                Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = "Copiar contraseña",
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(5.dp))

            // Botón Crear nueva entrada con la clave
            IconButton(
                onClick = alCrearEntrada,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(ColorTarjetaAjustes)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Crear nueva entrada con esta contraseña",
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    )
}
