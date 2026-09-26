package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.colorParaGrupoId
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

/**
 * Botón de icono estandarizado para las cabeceras de pantalla (40.dp, recorte circular, icono de 20.dp).
 */
@Composable
fun BotonIconoCabecera(
    onClick: () -> Unit,
    icono: ImageVector,
    descripcion: String,
    modifier: Modifier = Modifier,
    tint: Color = ColorIconosInternos,
    colorFondo: Color = ColorTarjetaAjustes,
    habilitado: Boolean = true
) {
    IconButton(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (habilitado) colorFondo else colorFondo.copy(alpha = 0.5f))
    ) {
        Icon(
            imageVector = icono,
            contentDescription = descripcion,
            tint = if (habilitado) tint else tint.copy(alpha = 0.4f),
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * Barra superior fija de navegación (Sticky Top Bar) para pantallas secundarias.
 * Queda anclada arriba, fuera del scroll, con botón atrás, título y espacio para acciones.
 */
@Composable
fun BarraSuperiorPantalla(
    titulo: String,
    alVolver: () -> Unit,
    modifier: Modifier = Modifier,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    conSeparador: Boolean = false,
    colorFondo: Color = Color.Unspecified,
    acciones: (@Composable RowScope.() -> Unit)? = null
) {
    val fondoBarra = if (colorFondo != Color.Unspecified) colorFondo else Obsidiana
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(fondoBarra)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonIconoCabecera(
                onClick = alVolver,
                icono = Icons.AutoMirrored.Filled.ArrowBack,
                descripcion = "Volver atrás"
            )
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = titulo,
                    color = ColorTitulos,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (mostrarId && !idEtiqueta.isNullOrBlank()) {
                    Spacer(Modifier.height(2.dp))
                    val colorId = colorParaGrupoId(idEtiqueta)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(fondoBadgeParaTema(colorId))
                            .padding(horizontal = 6.dp, vertical = 1.5.dp)
                    ) {
                        Text(
                            text = idEtiqueta,
                            color = colorLegibleParaTema(colorId),
                            style = EstiloMono.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
            if (acciones != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    content = acciones
                )
            }
        }
        if (conSeparador) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(ColorBordeActual.copy(alpha = 0.35f))
            )
        }
    }
}

/**
 * Descripción contextual que vive dentro del área con desplazamiento.
 * Desaparece al hacer scroll hacia abajo para priorizar las opciones y tarjetas.
 */
@Composable
fun DescripcionPantalla(
    subtitulo: String,
    modifier: Modifier = Modifier
) {
    if (subtitulo.isNotBlank()) {
        Text(
            text = subtitulo,
            color = TextoSecundario,
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        )
    }
}

/**
 * Cabecera de navegación unificada para todas las pantallas secundarias de la app.
 * Incluye flecha hacia atrás vinculada a la pila de navegación, título, subtítulo opcional
 * y espacio para acciones contextuales en el extremo derecho.
 */
@Composable
fun CabeceraPantalla(
    titulo: String,
    alVolver: () -> Unit,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    acciones: (@Composable RowScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = alVolver,
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver atrás",
                tint = ColorIconosInternos,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                color = ColorTitulos,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            if (!subtitulo.isNullOrBlank()) {
                Text(
                    text = subtitulo,
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        if (acciones != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = acciones
            )
        }
    }
}
