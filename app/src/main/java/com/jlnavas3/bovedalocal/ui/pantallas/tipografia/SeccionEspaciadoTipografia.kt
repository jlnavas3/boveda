package com.jlnavas3.bovedalocal.ui.pantallas.tipografia

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatLineSpacing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.InsigniaValorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.SliderBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeccionEspaciadoTipografia(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    seccionDestino: String?,
    requester: BringIntoViewRequester,
    modifier: Modifier = Modifier
) {
    TarjetaBovedaDesplegable(
        titulo = "Espaciado e interlineado",
        descripcion = "Separación entre letras y altura de línea",
        icono = Icons.Filled.FormatLineSpacing,
        colorIcono = ColorIconosInternos,
        inicialmenteAbierta = seccionDestino == "09.5.6",
        idEtiqueta = "09.5.6",
        mostrarId = ajustes.mostrarIdsAjustes,
        modifier = modifier.bringIntoViewRequester(requester)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Espaciado horizontal (Kerning)", color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
            InsigniaValorBoveda(if (ajustes.espaciadoLetrasSp == 0f) "0.0 sp (Normal)" else "${String.format(Locale.US, "%+.1f", ajustes.espaciadoLetrasSp)} sp")
        }
        SliderBoveda(
            value = ajustes.espaciadoLetrasSp,
            onValueChange = { vm.ajustarEspaciadoLetras(it) },
            valueRange = -0.5f..2.0f,
            steps = 24,
            colorAcento = ColorAcento
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("-0.5 sp (Condensado)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            Text("0.0 sp (Normal)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            Text("+2.0 sp (Separado)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Factor de interlineado", color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
            InsigniaValorBoveda("${String.format(Locale.US, "%.2f", ajustes.interlineadoFactor)}x")
        }
        SliderBoveda(
            value = ajustes.interlineadoFactor,
            onValueChange = { vm.ajustarInterlineadoFactor(it) },
            valueRange = 0.85f..1.40f,
            steps = 10,
            colorAcento = ColorAcento
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0.85x (Compacto)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            Text("1.00x (Normal)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            Text("1.40x (Amplio)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
        }
        ComponenteBotonFila(
            titulo = "Restablecer",
            alPulsar = {
                vm.ajustarEspaciadoLetras(0.0f)
                vm.ajustarInterlineadoFactor(1.0f)
            }
        )
    }
}
