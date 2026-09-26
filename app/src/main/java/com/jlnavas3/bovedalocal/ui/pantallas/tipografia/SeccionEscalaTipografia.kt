package com.jlnavas3.bovedalocal.ui.pantallas.tipografia

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.InsigniaValorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.SliderBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeccionEscalaTipografia(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    seccionDestino: String?,
    requester: BringIntoViewRequester,
    modifier: Modifier = Modifier
) {
    TarjetaBovedaDesplegable(
        titulo = "Tamaño de fuente (Escala)",
        descripcion = "Agranda o reduce todos los textos de la app manteniendo proporciones",
        icono = Icons.Filled.FormatSize,
        colorIcono = ColorGenerador,
        inicialmenteAbierta = seccionDestino == "09.5.3",
        idEtiqueta = "09.5.3",
        mostrarId = ajustes.mostrarIdsAjustes,
        modifier = modifier.bringIntoViewRequester(requester)
    ) {
        val porcentaje = ((ajustes.escalaTexto - 1.0f) * 100).roundToInt()
        val textoPorcentaje = if (porcentaje == 0) "1.00x (Normal)" else if (porcentaje > 0) "${String.format("%.2f", ajustes.escalaTexto)}x (+$porcentaje%)" else "${String.format("%.2f", ajustes.escalaTexto)}x ($porcentaje%)"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Escalado global", color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
            InsigniaValorBoveda(textoPorcentaje)
        }
        SliderBoveda(
            value = ajustes.escalaTexto,
            onValueChange = { vm.ajustarEscalaTexto(it) },
            valueRange = 0.80f..1.35f,
            steps = 10,
            colorAcento = ColorAcento
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0.80x (-20%)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            Text("1.00x (100%)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            Text("1.35x (+35%)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
        }
        ComponenteBotonFila(
            titulo = "Restablecer",
            alPulsar = {
                vm.ajustarEscalaTexto(1.0f)
            }
        )
    }
}
