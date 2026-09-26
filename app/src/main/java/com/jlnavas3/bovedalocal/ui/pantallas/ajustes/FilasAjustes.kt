package com.jlnavas3.bovedalocal.ui.pantallas.ajustes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun GrupoAjustes(
    etiqueta: String? = null,
    modifier: Modifier = Modifier,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (!etiqueta.isNullOrBlank()) {
            Text(
                text = etiqueta.uppercase(),
                color = ColorAjusteGris,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 4.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(ColorTarjetaAjustes)
        ) {
            contenido()
        }
    }
}

@Composable
fun FilaAjusteMenu(
    titulo: String,
    icono: ImageVector,
    colorIcono: Color,
    subtitulo: String? = null,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    valorTexto: String? = null,
    alPulsar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptica.tic()
                alPulsar()
            }
            .padding(
                horizontal = 16.dp,
                vertical = if (mostrarId && !idEtiqueta.isNullOrBlank()) 11.dp else 15.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colorIcono),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = titulo,
                color = ColorTextoAjustes,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (mostrarId && !idEtiqueta.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "ID: $idEtiqueta",
                    color = ColorAjusteGris,
                    style = EstiloMono.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            } else if (!subtitulo.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitulo,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (!valorTexto.isNullOrBlank()) {
            Spacer(Modifier.width(8.dp))
            Text(
                text = valorTexto,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
            )
        }

        Spacer(Modifier.width(6.dp))
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = ColorAjusteGris,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SeparadorFilaAjuste(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 68.dp)
            .height(0.5.dp)
            .background(ColorSeparadorAjustes)
    )
}

@Composable
fun SeparadorFilaSimple(modifier: Modifier = Modifier, paddingInicio: Dp = 16.dp) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = paddingInicio)
            .height(0.5.dp)
            .background(ColorSeparadorAjustes)
    )
}

@Composable
fun FilaEntradaSubmenu(
    titulo: String,
    subtitulo: String? = null,
    valorTexto: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    esPeligro: Boolean = false,
    mostrarChevron: Boolean = true,
    alPulsar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptica.tic()
                alPulsar()
            }
            .padding(
                horizontal = 16.dp,
                vertical = if (mostrarId && !idEtiqueta.isNullOrBlank()) 12.dp else 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono ?: (if (esPeligro) Peligro else ColorAjusteGris),
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(14.dp))
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = titulo,
                color = if (esPeligro) Peligro else ColorTextoAjustes,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.5.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (mostrarId && !idEtiqueta.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "ID: $idEtiqueta",
                    color = ColorAjusteGris,
                    style = EstiloMono.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            } else if (!subtitulo.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitulo,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (!valorTexto.isNullOrBlank()) {
            Spacer(Modifier.width(8.dp))
            Text(
                text = valorTexto,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp)
            )
        }

        if (mostrarChevron) {
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = ColorAjusteGris.copy(alpha = 0.7f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun FilaOpcionRadio(
    titulo: String,
    subtitulo: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    seleccionado: Boolean,
    alSeleccionar: () -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptica.tic()
                alSeleccionar()
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono ?: (if (seleccionado) ColorAcento else ColorAjusteGris),
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(14.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                color = ColorTextoAjustes,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (seleccionado) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 15.5.sp
                )
            )
            if (!subtitulo.isNullOrBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitulo,
                    color = ColorAjusteGris,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(
                    width = if (seleccionado) 6.dp else 1.5.dp,
                    color = if (seleccionado) ColorAcento else ColorAjusteGris.copy(alpha = 0.5f),
                    shape = CircleShape
                )
        )
    }
}

@Composable
fun EnlaceAjuste(texto: String, alPulsar: () -> Unit) {
    Spacer(Modifier.height(8.dp))
    Text(
        texto,
        color = ColorAcento,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
            .clickable { alPulsar() }
            .padding(vertical = 4.dp)
    )
}

/**
 * Switch estándar para toda la app con diseño nativo Honor MagicOS / Samsung One UI.
 * Sin bordes duros, con track redondeado y colores sólidos.
 */
@Composable
fun SwitchBoveda(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorActivo: Color = Ambar
) {
    SwitchBoveda(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colorActivo = colorActivo
    )
}

@Composable
fun FilaAjuste(
    titulo: String,
    descripcion: String,
    activo: Boolean,
    habilitado: Boolean = true,
    colorActivo: Color = Ambar,
    alCambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(titulo, color = TextoPrincipal, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(2.dp))
            Text(descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
        }
        SwitchBoveda(
            checked = activo,
            enabled = habilitado,
            colorActivo = colorActivo,
            onCheckedChange = alCambiar
        )
    }
}
