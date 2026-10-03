package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeDropdown
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.util.Haptica

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
            .background(ColorSeparadorAjustes)
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

/**
 * Opción de menú desplegable compacta, ergonómica y totalmente adaptada al sistema
 * de tipografía y temas de la aplicación.
 */
@Composable
fun ElementoMenuCompacto(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    colorIcono: Color = ColorAcento,
    colorTexto: Color = TextoPrincipal,
    iconoFinal: ImageVector? = null,
    colorIconoFinal: Color = ColorIconosInternos.copy(alpha = 0.6f),
    habilitado: Boolean = true
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 38.dp)
            .clickable(enabled = habilitado) {
                haptica.tic()
                onClick()
            }
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = if (habilitado) colorIcono else colorIcono.copy(alpha = 0.38f),
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(10.dp))
        }
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = if (habilitado) colorTexto else colorTexto.copy(alpha = 0.38f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (iconoFinal != null) {
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = iconoFinal,
                contentDescription = null,
                tint = if (habilitado) colorIconoFinal else colorIconoFinal.copy(alpha = 0.38f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Cabecera de navegación para submenús desplegables multinivel con botón atrás integrado.
 */
@Composable
fun ElementoRetornoSubmenu(
    titulo: String,
    alVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 38.dp)
            .clickable {
                haptica.tic()
                alVolver()
            }
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Volver",
            tint = ColorAcento,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = titulo,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = TextoPrincipal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


