package com.jlnavas3.bovedalocal.ui.pantallas.formas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LineWeight
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
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeccionGrosorBorde(
    ajustes: AjustesApp,
    seccionDestino: String?,
    reqGrosor: BringIntoViewRequester,
    alAjustarGrosor: (Float) -> Unit
) {
    TarjetaBovedaDesplegable(
        titulo = "Grosor del borde",
        descripcion = "Controla el ancho de trazo perimetral en tarjetas y controles",
        icono = Icons.Filled.LineWeight,
        colorIcono = ColorPasskeys,
        inicialmenteAbierta = seccionDestino == "09.3.4",
        idEtiqueta = "09.3.4",
        mostrarId = ajustes.mostrarIdsAjustes,
        modifier = Modifier.bringIntoViewRequester(reqGrosor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ancho de trazo", color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
            InsigniaValorBoveda(if (ajustes.grosorBordeDp == 0f) "Sin borde" else "${String.format(
                Locale.US, "%.1f", ajustes.grosorBordeDp)} dp")
        }
        SliderBoveda(
            value = ajustes.grosorBordeDp,
            onValueChange = alAjustarGrosor,
            valueRange = 0f..4f,
            steps = 15,
            colorAcento = ColorAcento
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0 dp (Plano)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            Text("1 dp (Fino)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            Text("4 dp (Grueso)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
        }
        ComponenteBotonFila(
            titulo = "Restablecer",
            alPulsar = {
                alAjustarGrosor(0.8f)
            }
        )
    }
}
