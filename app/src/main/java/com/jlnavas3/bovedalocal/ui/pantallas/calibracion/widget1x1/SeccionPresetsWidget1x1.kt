package com.jlnavas3.bovedalocal.ui.pantallas.calibracion.widget1x1

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun SeccionPresetsWidget1x1(
    ajustes: AjustesApp,
    vm: VaultViewModel,
    haptica: Haptica
) {
    ComponenteGrupo(
        etiqueta = "Presets de tamaño",
        idGrupo = "04-HER-WGT-1X1-G01",
        mostrarId = ajustes.mostrarIdsAjustes
    ) {
        val presets = listOf(
            "Compacto" to 42f,
            "Estándar" to 52f,
            "Grande" to 60f,
            "Honor" to 55f
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            presets.forEach { (nombre, tamano) ->
                val seleccionado = if (nombre == "Honor") {
                    ajustes.widget1x1AnchoDp == 55f && ajustes.widget1x1AltoDp == 51f && ajustes.widget1x1CurvaturaEsquinasDp == 15f && ajustes.widget1x1GrosorBordeDp == 0f
                } else {
                    ajustes.widget1x1AnchoDp == tamano && ajustes.widget1x1AltoDp == tamano
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (seleccionado) ColorAcento.copy(alpha = 0.22f) else SuperficieAlta)
                        .then(
                            if (seleccionado) Modifier.border(1.5.dp, ColorAcento, RoundedCornerShape(8.dp))
                            else Modifier
                        )
                        .clickable {
                            haptica.tic()
                            if (nombre == "Honor") {
                                vm.aplicarPresetHonorWidget1x1()
                            } else {
                                vm.ajustarWidget1x1PresetTamano(tamano)
                            }
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = nombre,
                        color = if (seleccionado) ColorAcento else TextoPrincipal,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    }
}
