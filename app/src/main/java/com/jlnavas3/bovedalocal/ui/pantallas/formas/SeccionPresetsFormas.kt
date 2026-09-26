package com.jlnavas3.bovedalocal.ui.pantallas.formas

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
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeccionPresetsFormas(
    ajustes: AjustesApp,
    seccionDestino: String?,
    reqPresets: BringIntoViewRequester,
    haptica: Haptica,
    alAplicarPreset: (curvatura: Float, grosor: Float, estilo: String, espaciado: Float) -> Unit
) {
    TarjetaBovedaDesplegable(
        titulo = "Estilos predefinidos",
        descripcion = "Aplica combinaciones armónicas de esquinas y trazos en un solo toque",
        icono = Icons.Filled.AutoAwesome,
        colorIcono = ColorIconosInternos,
        inicialmenteAbierta = seccionDestino == "09.3.2",
        idEtiqueta = "09.3.2",
        mostrarId = ajustes.mostrarIdsAjustes,
        modifier = Modifier.bringIntoViewRequester(reqPresets)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChipPresetForma(
                etiqueta = "Predeterminado",
                descripcion = "6 dp / 0.8 dp",
                activo = ajustes.curvaturaEsquinasDp == 6f && ajustes.grosorBordeDp == 0.8f && ajustes.estiloBorde == "marcado"
            ) {
                haptica.tic()
                alAplicarPreset(6f, 0.8f, "marcado", 14f)
            }
            ChipPresetForma(
                etiqueta = "Redondeado",
                descripcion = "18 dp / 1 dp",
                activo = ajustes.curvaturaEsquinasDp == 18f && ajustes.grosorBordeDp == 1f && ajustes.estiloBorde == "sutil"
            ) {
                haptica.tic()
                alAplicarPreset(18f, 1f, "sutil", 14f)
            }
            ChipPresetForma(
                etiqueta = "Neobrutalista",
                descripcion = "0 dp / 2.5 dp",
                activo = ajustes.curvaturaEsquinasDp == 0f && ajustes.grosorBordeDp == 2.5f && ajustes.estiloBorde == "marcado"
            ) {
                haptica.tic()
                alAplicarPreset(0f, 2.5f, "marcado", 16f)
            }
            ChipPresetForma(
                etiqueta = "Píldora M3",
                descripcion = "28 dp / 1 dp",
                activo = ajustes.curvaturaEsquinasDp == 28f && ajustes.grosorBordeDp == 1f
            ) {
                haptica.tic()
                alAplicarPreset(28f, 1f, "sutil", 16f)
            }
            ChipPresetForma(
                etiqueta = "Compacto",
                descripcion = "8 dp / 1.5 dp",
                activo = ajustes.curvaturaEsquinasDp == 8f && ajustes.espaciadoComponentesDp == 8f
            ) {
                haptica.tic()
                alAplicarPreset(8f, 1.5f, "sutil", 8f)
            }
            ChipPresetForma(
                etiqueta = "Sin bordes",
                descripcion = "16 dp / 0 dp",
                activo = ajustes.grosorBordeDp == 0f || ajustes.estiloBorde == "ninguno"
            ) {
                haptica.tic()
                alAplicarPreset(16f, 0f, "ninguno", 14f)
            }
        }
    }
}

@Composable
fun ChipPresetForma(
    etiqueta: String,
    descripcion: String,
    activo: Boolean,
    alPulsar: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(FormaBoton)
            .background(if (activo) ColorAcento else SuperficieAlta)
            .then(
                if (!activo && GrosorBorde > 0.dp) {
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaBoton)
                } else {
                    Modifier
                }
            )
            .clickable { alPulsar() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = etiqueta,
                color = if (activo) ColorSobreAcento else TextoPrincipal,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = descripcion,
                color = if (activo) ColorSobreAcento.copy(alpha = 0.8f) else TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
            )
        }
    }
}
