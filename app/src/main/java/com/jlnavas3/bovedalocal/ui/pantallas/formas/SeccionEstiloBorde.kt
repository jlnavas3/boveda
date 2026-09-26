package com.jlnavas3.bovedalocal.ui.pantallas.formas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeccionEstiloBorde(
    ajustes: AjustesApp,
    seccionDestino: String?,
    reqEstilo: BringIntoViewRequester,
    haptica: Haptica,
    alAjustarEstilo: (String) -> Unit
) {
    TarjetaBovedaDesplegable(
        titulo = "Tono y estilo del borde",
        descripcion = "Elige el matiz cromático con el que se dibujarán las líneas de contorno",
        icono = Icons.Filled.Tune,
        colorIcono = ColorSeguridad,
        inicialmenteAbierta = seccionDestino == "09.3.5",
        idEtiqueta = "09.3.5",
        mostrarId = ajustes.mostrarIdsAjustes,
        modifier = Modifier.bringIntoViewRequester(reqEstilo)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AlmacenAjustes.OPCIONES_ESTILO_BORDE.forEach { (clave, etiqueta) ->
                val seleccionado = ajustes.estiloBorde == clave
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(FormaBoton)
                        .background(if (seleccionado) ColorAcento else SuperficieAlta)
                        .then(
                            if (!seleccionado && GrosorBorde > 0.dp) {
                                Modifier.border(GrosorBorde, ColorBordeActual, FormaBoton)
                            } else {
                                Modifier
                            }
                        )
                        .clickable {
                            haptica.tic()
                            alAjustarEstilo(clave)
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = etiqueta,
                        color = if (seleccionado) ColorSobreAcento else TextoPrincipal,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
        ComponenteBotonFila(
            titulo = "Restablecer",
            alPulsar = {
                alAjustarEstilo("marcado")
            }
        )
    }
}
