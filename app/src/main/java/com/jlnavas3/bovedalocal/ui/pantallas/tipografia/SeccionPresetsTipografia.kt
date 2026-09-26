package com.jlnavas3.bovedalocal.ui.pantallas.tipografia

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
import com.jlnavas3.bovedalocal.ui.VaultViewModel
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
fun SeccionPresetsTipografia(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica,
    seccionDestino: String?,
    requester: BringIntoViewRequester,
    modifier: Modifier = Modifier
) {
    TarjetaBovedaDesplegable(
        titulo = "Estilos predefinidos",
        descripcion = "Combinaciones optimizadas para lectura, terminales o accesibilidad",
        icono = Icons.Filled.AutoAwesome,
        colorIcono = ColorIconosInternos,
        inicialmenteAbierta = seccionDestino == "09.5.2",
        idEtiqueta = "09.5.2",
        mostrarId = ajustes.mostrarIdsAjustes,
        modifier = modifier.bringIntoViewRequester(requester)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChipPresetTipografia(
                etiqueta = "Equilibrado",
                descripcion = "Sans 1.0x",
                activo = ajustes.escalaTexto == 1.0f && ajustes.familiaFuente == "sans" && ajustes.pesoTexto == "normal" && !ajustes.cursivaTexto
            ) {
                haptica.tic()
                vm.aplicarPresetTipografia(
                    escala = 1.0f,
                    peso = "normal",
                    cursiva = false,
                    kerning = 0.0f,
                    interlineado = 1.0f,
                    familia = "sans"
                )
            }
            ChipPresetTipografia(
                etiqueta = "Terminal Mono",
                descripcion = "Hacker 0.95x",
                activo = ajustes.familiaFuente == "mono"
            ) {
                haptica.tic()
                vm.aplicarPresetTipografia(
                    escala = 0.95f,
                    peso = "medio",
                    cursiva = false,
                    kerning = 0.5f,
                    interlineado = 1.05f,
                    familia = "mono"
                )
            }
            ChipPresetTipografia(
                etiqueta = "Editorial",
                descripcion = "Serif 1.05x",
                activo = ajustes.familiaFuente == "serif"
            ) {
                haptica.tic()
                vm.aplicarPresetTipografia(
                    escala = 1.05f,
                    peso = "normal",
                    cursiva = false,
                    kerning = 0.0f,
                    interlineado = 1.20f,
                    familia = "serif"
                )
            }
            ChipPresetTipografia(
                etiqueta = "Accesibilidad",
                descripcion = "Grande 1.25x",
                activo = ajustes.escalaTexto >= 1.20f && ajustes.pesoTexto == "seminegrita"
            ) {
                haptica.tic()
                vm.aplicarPresetTipografia(
                    escala = 1.25f,
                    peso = "seminegrita",
                    cursiva = false,
                    kerning = 0.2f,
                    interlineado = 1.25f,
                    familia = "sans"
                )
            }
            ChipPresetTipografia(
                etiqueta = "Compacto",
                descripcion = "Denso 0.85x",
                activo = ajustes.escalaTexto <= 0.88f
            ) {
                haptica.tic()
                vm.aplicarPresetTipografia(
                    escala = 0.85f,
                    peso = "fino",
                    cursiva = false,
                    kerning = -0.2f,
                    interlineado = 0.95f,
                    familia = "sans"
                )
            }
        }
    }
}

@Composable
fun ChipPresetTipografia(
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
