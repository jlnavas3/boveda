package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.InsigniaIdAjuste
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/**
 * Grupo de opciones para el menú lateral con encabezado en mayúsculas y tarjeta redondeada estilo MagicOS/One UI.
 */
@Composable
fun GrupoMenuLateral(
    titulo: String,
    modifier: Modifier = Modifier,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    ajustes: AjustesApp? = null,
    contenido: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titulo.uppercase(),
                color = ColorAjusteGris,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    fontSize = 11.sp
                ),
                modifier = Modifier.weight(1f, fill = false)
            )
            if (mostrarId && !idEtiqueta.isNullOrBlank()) {
                Spacer(modifier = Modifier.width(8.dp))
                InsigniaIdAjuste(id = idEtiqueta, ajustes = ajustes, colorForzado = ColorAcento)
            }
        }
        val formaGrupo = RoundedCornerShape(CurvaturaEsquinas)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && ajustes?.menuLateralSinBordes != true) {
                        Modifier.border(
                            width = GrosorBorde,
                            color = ColorBordeActual,
                            shape = formaGrupo
                        )
                    } else Modifier
                )
                .clip(formaGrupo)
                .background(ColorTarjetaAjustes)
        ) {
            contenido()
        }
    }
}
