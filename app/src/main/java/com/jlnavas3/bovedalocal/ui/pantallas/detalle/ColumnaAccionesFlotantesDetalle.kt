package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Columna vertical de acciones flotantes (FAB stack) para la pantalla de detalle:
 * 1. Papelera / Eliminar
 * 2. Compartir por código QR
 * 3. Alternar Favorito
 * 4. Botón principal: Editar
 */
@Composable
fun ColumnaAccionesFlotantesDetalle(
    esFavorito: Boolean,
    alEliminar: () -> Unit,
    alCompartirQr: () -> Unit,
    alAlternarFavorito: () -> Unit,
    alEditar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formaFab = RoundedCornerShape(CurvaturaEsquinas)

    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Eliminar / Mover a papelera
        SmallFloatingActionButton(
            onClick = alEliminar,
            containerColor = ColorTarjetaAjustes,
            contentColor = Peligro,
            shape = formaFab,
            modifier = Modifier.then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Mover a papelera",
                tint = Peligro,
                modifier = Modifier.size(20.dp)
            )
        }

        // 2. Compartir por código QR
        SmallFloatingActionButton(
            onClick = alCompartirQr,
            containerColor = ColorTarjetaAjustes,
            contentColor = ColorAcento,
            shape = formaFab,
            modifier = Modifier.then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
        ) {
            Icon(
                imageVector = Icons.Filled.QrCode,
                contentDescription = "Compartir por código QR",
                tint = ColorAcento,
                modifier = Modifier.size(20.dp)
            )
        }

        // 3. Alternar Favorito
        SmallFloatingActionButton(
            onClick = alAlternarFavorito,
            containerColor = ColorTarjetaAjustes,
            contentColor = if (esFavorito) ColorAcento else TextoSecundario,
            shape = formaFab,
            modifier = Modifier.then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = if (esFavorito) "Quitar de favoritos" else "Marcar como favorito",
                tint = if (esFavorito) ColorAcento else TextoSecundario.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }

        // 4. Botón flotante principal: Editar
        FloatingActionButton(
            onClick = alEditar,
            containerColor = ColorAcento,
            contentColor = ColorSobreAcento,
            shape = formaFab,
            modifier = Modifier.then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, formaFab)
                } else Modifier
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Editar entrada",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
