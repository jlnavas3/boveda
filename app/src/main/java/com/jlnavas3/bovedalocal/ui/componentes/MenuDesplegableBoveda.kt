package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

/**
 * Divisor / separador sutil entre opciones de un menú desplegable, adaptado dinámicamente
 * al tema claro u oscuro para no resaltar excesivamente ni quedar invisible.
 */
@Composable
fun SeparadorOpcionMenu(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(0.8.dp)
            .background(if (esOscuroActivo) Color(0xFF2D2C30) else Color(0xFFEBEBEB))
    )
}

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
    content: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(14.dp)
    val fondoMenu = if (esOscuroActivo) Color(0xFF262529) else Color(0xFFFFFFFF)

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = offset,
        properties = properties,
        modifier = modifier
            .clip(forma)
            .background(fondoMenu)
    ) {
        content()
    }
}
