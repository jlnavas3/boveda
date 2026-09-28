package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widgettotp

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
import androidx.compose.material.icons.filled.Palette
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
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionColoresWidgetTotp(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    colorBordeEfectivo: Color,
    colorContadorEfectivo: Color,
    colorCodigoEfectivo: Color,
    colorTituloIconoEfectivo: Color,
    colorFilasEfectivo: Color,
    haptica: Haptica,
    modifier: Modifier = Modifier
) {
    var colorTabSeleccionada by remember { mutableIntStateOf(0) }

    val opcionesColor = listOf(
        "Borde" to colorBordeEfectivo,
        "Contador" to colorContadorEfectivo,
        "Código" to colorCodigoEfectivo,
        "Título" to colorTituloIconoEfectivo,
        "Filas" to colorFilasEfectivo
    )

    ComponenteGrupo(
        etiqueta = "Colores del widget",
        icono = Icons.Filled.Palette,
        colorIcono = Color2FA,
        idGrupo = "04-HER-WGT-CAL-G03",
        mostrarId = ajustes.mostrarIdsAjustes,
        alRestablecer = {
            haptica.tic()
            vm.ajustarWidgetColorBorde("#FFB300")
            vm.ajustarWidgetColorContador("#FFFFFF")
            vm.ajustarWidgetColorCodigo("#FFB300")
            vm.ajustarWidgetColorTituloIcono("#FFFFFF")
            vm.ajustarWidgetColorFilas("#00000000")
        },
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Selecciona un elemento para cambiar su color:",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                opcionesColor.forEachIndexed { idx, (nombre, color) ->
                    val seleccionado = colorTabSeleccionada == idx
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
                                colorTabSeleccionada = idx
                            }
                            .padding(vertical = 8.dp),
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

            when (colorTabSeleccionada) {
                0 -> {
                    SelectorColorEnTiempoReal(
                        colorInicial = colorBordeEfectivo,
                        titulo = "Color del Borde"
                    ) { nuevoColor ->
                        vm.ajustarWidgetColorBorde(nuevoColor.aHex())
                    }
                }
                1 -> {
                    SelectorColorEnTiempoReal(
                        colorInicial = colorContadorEfectivo,
                        titulo = "Color del Contador Circular"
                    ) { nuevoColor ->
                        vm.ajustarWidgetColorContador(nuevoColor.aHex())
                    }
                }
                2 -> {
                    SelectorColorEnTiempoReal(
                        colorInicial = colorCodigoEfectivo,
                        titulo = "Color del Código 2FA"
                    ) { nuevoColor ->
                        vm.ajustarWidgetColorCodigo(nuevoColor.aHex())
                    }
                }
                3 -> {
                    SelectorColorEnTiempoReal(
                        colorInicial = colorTituloIconoEfectivo,
                        titulo = "Color del Título e Ícono"
                    ) { nuevoColor ->
                        vm.ajustarWidgetColorTituloIcono(nuevoColor.aHex())
                    }
                }
                4 -> {
                    SelectorColorEnTiempoReal(
                        colorInicial = colorFilasEfectivo,
                        titulo = "Color de Fondo de Filas"
                    ) { nuevoColor ->
                        vm.ajustarWidgetColorFilas(nuevoColor.aHex())
                    }
                }
            }
        }
    }
}
