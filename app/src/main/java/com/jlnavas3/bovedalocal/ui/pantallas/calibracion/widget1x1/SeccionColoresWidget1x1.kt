package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionColoresWidget1x1(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    colorBorde: Color,
    colorIcono: Color,
    colorFondo: Color,
    haptica: Haptica
) {
    var color1x1TabSeleccionada by remember { mutableIntStateOf(0) }

    ComponenteGrupo(
        etiqueta = "Colores del widget",
        idGrupo = "03.3.G13",
        mostrarId = ajustes.mostrarIdsAjustes,
        descripcion = "Personaliza los colores de borde, ícono y fondo"
    ) {
        val pestanasColores1x1 = listOf(
            Triple("Borde", colorBorde, Icons.Filled.BorderColor),
            Triple("Ícono", colorIcono, Icons.Filled.Key),
            Triple("Fondo", colorFondo, Icons.Filled.CropSquare)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            pestanasColores1x1.forEachIndexed { idx, (nombre, color, _) ->
                val seleccionado = color1x1TabSeleccionada == idx
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (seleccionado) color.copy(alpha = 0.22f) else SuperficieAlta)
                        .then(
                            if (seleccionado) Modifier.border(1.5.dp, color, RoundedCornerShape(10.dp))
                            else Modifier
                        )
                        .clickable {
                            haptica.tic()
                            color1x1TabSeleccionada = idx
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(color)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = nombre,
                            color = if (seleccionado) TextoPrincipal else TextoSecundario,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        when (color1x1TabSeleccionada) {
            0 -> {
                SelectorColorEnTiempoReal(
                    colorInicial = colorBorde,
                    titulo = "Color del Borde"
                ) { nuevoColor ->
                    vm.ajustarWidget1x1ColorBorde(nuevoColor.aHex())
                }
            }
            1 -> {
                SelectorColorEnTiempoReal(
                    colorInicial = colorIcono,
                    titulo = "Color del Ícono"
                ) { nuevoColor ->
                    vm.ajustarWidget1x1ColorIcono(nuevoColor.aHex())
                }
            }
            2 -> {
                SelectorColorEnTiempoReal(
                    colorInicial = colorFondo,
                    titulo = "Color del Fondo"
                ) { nuevoColor ->
                    vm.ajustarWidget1x1ColorFondo(nuevoColor.aHex())
                }
            }
        }
    }
}
