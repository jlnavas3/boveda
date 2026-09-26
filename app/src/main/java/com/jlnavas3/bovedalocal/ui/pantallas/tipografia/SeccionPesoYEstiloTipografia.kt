package com.jlnavas3.bovedalocal.ui.pantallas.tipografia

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatBold
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
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.FilaAjuste
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeccionPesoYEstiloTipografia(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica,
    seccionDestino: String?,
    requester: BringIntoViewRequester,
    modifier: Modifier = Modifier
) {
    TarjetaBovedaDesplegable(
        titulo = "Grosor y estilo de texto",
        descripcion = "Densidad de trazos y estilo itálico",
        icono = Icons.Filled.FormatBold,
        colorIcono = ColorSeguridad,
        inicialmenteAbierta = seccionDestino == "09.5.5",
        idEtiqueta = "09.5.5",
        mostrarId = ajustes.mostrarIdsAjustes,
        modifier = modifier.bringIntoViewRequester(requester)
    ) {
        Text(
            text = "Grosor / Peso de la fuente",
            color = TextoSecundario,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AlmacenAjustes.OPCIONES_PESO_TEXTO.forEach { (clave, etiqueta) ->
                val seleccionado = ajustes.pesoTexto == clave
                Box(
                    modifier = Modifier
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
                            vm.ajustarPesoTexto(clave)
                        }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
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

        Spacer(Modifier.height(14.dp))

        FilaAjuste(
            titulo = "Texto en cursiva",
            descripcion = "Añade inclinación estética a títulos, descripciones y etiquetas.",
            activo = ajustes.cursivaTexto,
            alCambiar = {
                haptica.tic()
                vm.ajustarCursivaTexto(it)
            }
        )
        ComponenteBotonFila(
            titulo = "Restablecer",
            alPulsar = {
                vm.ajustarPesoTexto("normal")
                vm.ajustarCursivaTexto(false)
            }
        )
    }
}
