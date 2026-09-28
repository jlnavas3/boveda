package com.jlnavas3.bovedalocal.ui.pantallas.tema

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppShortcut
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.PaletaAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeccionIconoLauncher(
    ajustes: AjustesApp,
    haptica: Haptica,
    reqLauncher: BringIntoViewRequester,
    alSolicitarCambioIcono: (PaletaAcento) -> Unit
) {
    ComponenteGrupo(
        etiqueta = "ÍCONO EN EL LAUNCHER",
        icono = Icons.Filled.AppShortcut,
        colorIcono = Color(0xFFE91E63),
        idGrupo = "02-APA-THM-G06",
        mostrarId = ajustes.mostrarIdsAjustes,
        alRestablecer = {
            if (ajustes.iconoLauncher != "ambar") {
                alSolicitarCambioIcono(PaletaAcento.AMBAR)
            }
        },
        modifier = Modifier.bringIntoViewRequester(reqLauncher)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PaletaAcento.entries.forEach { paleta ->
                    val seleccionado = ajustes.iconoLauncher == paleta.clave
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(paleta.base)
                            .border(
                                width = if (seleccionado) 3.dp else 1.dp,
                                color = if (seleccionado) TextoPrincipal else Borde,
                                shape = CircleShape
                            )
                            .clickable {
                                if (ajustes.iconoLauncher != paleta.clave) {
                                    haptica.tic()
                                    alSolicitarCambioIcono(paleta)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (seleccionado) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = paleta.etiqueta,
                                tint = colorContraste(paleta.base),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
