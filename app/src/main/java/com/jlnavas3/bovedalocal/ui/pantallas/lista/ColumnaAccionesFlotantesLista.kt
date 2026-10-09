package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro

/**
 * Columna vertical de botones flotantes (FAB) para la pantalla principal:
 * 0. Alerta de accesibilidad (Advertencia ámbar, si hay apps sospechosas activas).
 * 1. Bloquear bóveda (Candado superior).
 * 2. Crear nueva entrada (+ inferior).
 */
@Composable
fun ColumnaAccionesFlotantesLista(
    alBloquear: () -> Unit,
    alNuevaEntrada: () -> Unit,
    modifier: Modifier = Modifier,
    tieneAlertaAccesibilidad: Boolean = false,
    alAbrirAuditoriaAccesibilidad: (() -> Unit)? = null
) {
    val formaFab = RoundedCornerShape(CurvaturaEsquinas)

    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // FAB de advertencia: Alerta de servicios de accesibilidad no autorizados
        if (tieneAlertaAccesibilidad && alAbrirAuditoriaAccesibilidad != null) {
            val colorAlerta = Color(0xFFF59E0B)
            SmallFloatingActionButton(
                onClick = alAbrirAuditoriaAccesibilidad,
                containerColor = colorAlerta.copy(alpha = 0.20f),
                contentColor = colorAlerta,
                shape = formaFab,
                modifier = Modifier.then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, colorAlerta.copy(alpha = 0.40f), formaFab)
                    } else Modifier
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Auditoría de accesibilidad",
                    tint = colorAlerta,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // FAB superior: Bloquear app (Candado) con estilo y color idéntico al menú lateral
        SmallFloatingActionButton(
            onClick = alBloquear,
            containerColor = Peligro.copy(alpha = 0.15f),
            contentColor = Peligro,
            shape = formaFab,
            modifier = Modifier.then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, Peligro.copy(alpha = 0.30f), formaFab)
                } else Modifier
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Bloquear bóveda",
                tint = Peligro,
                modifier = Modifier.size(22.dp)
            )
        }

        // FAB inferior: Nueva entrada (+)
        FloatingActionButton(
            onClick = alNuevaEntrada,
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
                imageVector = Icons.Filled.Add,
                contentDescription = "Nueva entrada",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
