package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal

/**
 * Insignia visual modular para representar el conteo y tipo de credencial
 * (llaves de paso, contraseñas, TOTP) en flujos de transferencia FIDO CXF.
 */
@Composable
fun InsigniaConteoCxf(
    icono: ImageVector,
    texto: String,
    color: Color = ColorAcento,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = FormaPequena,
        color = ColorCampoAjustes,
        border = BorderStroke(1.dp, ColorSeparadorAjustes),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = ColorAcento,
                modifier = Modifier.size(14.dp)
            )
            com.jlnavas3.bovedalocal.ui.componentes.TextoPiePagina(
                texto = texto,
                color = TextoPrincipal
            )
        }
    }
}
