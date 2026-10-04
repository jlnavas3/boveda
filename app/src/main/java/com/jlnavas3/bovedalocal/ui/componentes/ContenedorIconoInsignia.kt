package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/**
 * Contenedor de insignia universal para íconos visuales y estados criptográficos.
 * Respeta de forma reactiva los tokens de curvatura de esquinas, bordes y colores temáticos.
 */
@Composable
fun ContenedorIconoInsignia(
    icono: ImageVector,
    modifier: Modifier = Modifier,
    tamano: TamanoInsignia = TamanoInsignia.MEDIANO,
    colorFondo: Color = ColorTarjetaAjustes,
    colorIcono: Color = ColorAcento,
    conBorde: Boolean = false,
    descripcion: String? = null
) {
    val forma = when (tamano) {
        TamanoInsignia.PEQUENO -> FormaPequena
        TamanoInsignia.MEDIANO -> FormaPequena
        TamanoInsignia.GRANDE -> RoundedCornerShape(CurvaturaEsquinas.coerceAtLeast(12.dp))
        TamanoInsignia.HERO -> RoundedCornerShape(CurvaturaEsquinas.coerceAtLeast(20.dp))
    }

    Box(
        modifier = modifier
            .size(tamano.tamanoCaja)
            .clip(forma)
            .background(colorFondo)
            .then(
                if (conBorde && GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(GrosorBorde, ColorBordeActual, forma)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = colorIcono,
            modifier = Modifier.size(tamano.tamanoIcono)
        )
    }
}
