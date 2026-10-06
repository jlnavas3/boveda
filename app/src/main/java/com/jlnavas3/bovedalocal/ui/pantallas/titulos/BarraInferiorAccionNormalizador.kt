package com.jlnavas3.bovedalocal.ui.pantallas.titulos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.VarianteBoton
import com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
import com.jlnavas3.bovedalocal.ui.theme.BovedaTheme
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta

@Composable
fun BarraInferiorAccionNormalizador(
    totalCuentas: Int,
    alAplicar: () -> Unit,
    alCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SuperficieAlta,
        tonalElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .border(1.dp, ColorAcento.copy(alpha = 0.2f), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                BotonBoveda(
                    texto = "Cancelar",
                    alPulsar = alCancelar,
                    variante = VarianteBoton.SECUNDARIO,
                    icono = Icons.Filled.Close,
                    modifier = Modifier.weight(1f)
                )

                BotonBoveda(
                    texto = "Aplicar ($totalCuentas)",
                    alPulsar = alAplicar,
                    variante = VarianteBoton.PRIMARIO,
                    icono = Icons.Filled.Check,
                    modifier = Modifier.weight(1.5f)
                )
            }
        }
    }
}

@BovedaPreview
@Composable
private fun PreviaBarraInferiorAccionNormalizador() {
    BovedaTheme {
        BarraInferiorAccionNormalizador(
            totalCuentas = 543,
            alAplicar = {},
            alCancelar = {}
        )
    }
}
