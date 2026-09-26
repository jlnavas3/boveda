package com.jlnavas3.bovedalocal.ui.pantallas.formas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.InsigniaValorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.SliderBoveda
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeccionEspaciadoComponentes(
    ajustes: AjustesApp,
    seccionDestino: String?,
    reqEspaciado: BringIntoViewRequester,
    alAjustarEspaciado: (Float) -> Unit
) {
    TarjetaBovedaDesplegable(
        titulo = "Espaciado y separación",
        descripcion = "Ajusta la separación vertical entre secciones, tarjetas y bloques",
        icono = Icons.Filled.SpaceDashboard,
        colorIcono = ColorIconosInternos,
        inicialmenteAbierta = seccionDestino == "09.3.6",
        idEtiqueta = "09.3.6",
        mostrarId = ajustes.mostrarIdsAjustes,
        modifier = Modifier.bringIntoViewRequester(reqEspaciado)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Separación vertical", color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
            InsigniaValorBoveda("${ajustes.espaciadoComponentesDp.roundToInt()} dp")
        }
        SliderBoveda(
            value = ajustes.espaciadoComponentesDp,
            onValueChange = alAjustarEspaciado,
            valueRange = 6f..24f,
            steps = 17,
            colorAcento = ColorAcento
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("6 dp (Compacto)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            Text("14 dp (Equilibrado)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            Text("24 dp (Amplio)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
        }
        ComponenteBotonFila(
            titulo = "Restablecer",
            alPulsar = {
                alAjustarEspaciado(14f)
            }
        )
    }
}
