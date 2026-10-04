package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeDropdown
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde

/**
 * Menú desplegable estándar con contenedor delimitado por borde exterior adaptativo
 * y esquinas consistentes con el diseño de la aplicación.
 */
@Composable
fun MenuDesplegableBoveda(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    properties: PopupProperties = PopupProperties(focusable = true),
    containerColor: Color = ColorCampoAjustes,
    content: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = offset,
        properties = properties,
        shape = forma,
        containerColor = containerColor,
        tonalElevation = 0.dp,
        shadowElevation = 6.dp,
        border = if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
            androidx.compose.foundation.BorderStroke(GrosorBorde, ColorBordeDropdown)
        } else null,
        modifier = modifier
    ) {
        content()
    }
}
