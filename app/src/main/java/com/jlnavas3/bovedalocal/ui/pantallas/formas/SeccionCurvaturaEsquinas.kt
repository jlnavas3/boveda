package com.jlnavas3.bovedalocal.ui.pantallas.formas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropSquare
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
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeccionCurvaturaEsquinas(
    ajustes: AjustesApp,
    seccionDestino: String?,
    reqCurvatura: BringIntoViewRequester,
    alAjustarCurvatura: (Float) -> Unit
) {
    TarjetaBovedaDesplegable(
        titulo = "Curvatura de esquinas",
        descripcion = "Define qué tan redondeados son las tarjetas, botones y campos",
        icono = Icons.Filled.CropSquare,
        colorIcono = ColorGenerador,
        inicialmenteAbierta = seccionDestino == "09.3.3",
        idEtiqueta = "09.3.3",
        mostrarId = ajustes.mostrarIdsAjustes,
        modifier = Modifier.bringIntoViewRequester(reqCurvatura)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Radio de esquinas", color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
            InsigniaValorBoveda("${ajustes.curvaturaEsquinasDp.roundToInt()} dp")
        }
        SliderBoveda(
            value = ajustes.curvaturaEsquinasDp,
            onValueChange = alAjustarCurvatura,
            valueRange = 0f..32f,
            steps = 31,
            colorAcento = ColorAcento
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0 dp (Recto)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            Text("18 dp (Estándar)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            Text("32 dp (Píldora)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
        }
        ComponenteBotonFila(
            titulo = "Restablecer",
            alPulsar = {
                alAjustarCurvatura(6f)
            }
        )
    }
}
