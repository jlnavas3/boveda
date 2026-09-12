package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Contenedor principal de pantalla (equivalente al `<main class="container">` en HTML).
 * Controla el fondo, padding exterior homogéneo y scroll vertical automático.
 */
@Composable
fun ContenedorPrincipal(
    modifier: Modifier = Modifier,
    conScroll: Boolean = true,
    paddingHorizontal: Dp = 20.dp,
    paddingVertical: Dp = 16.dp,
    espaciado: Dp = EspaciadoComponentes,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val modBase = modifier
        .fillMaxSize()
        .background(Obsidiana)
    val modScroll = if (conScroll) modBase.verticalScroll(rememberScrollState()) else modBase

    Column(
        modifier = modScroll.padding(horizontal = paddingHorizontal, vertical = paddingVertical),
        verticalArrangement = Arrangement.spacedBy(espaciado),
        content = contenido
    )
}

/**
 * Contenedor de sección temática (equivalente a `<section>` en HTML).
 * Proporciona cabecera con icono, título estilizado y subtítulo explicativo opcional.
 */
@Composable
fun ContenedorSeccion(
    titulo: String,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    icono: ImageVector? = null,
    colorTitulo: Color = ColorTitulos,
    espaciado: Dp = 10.dp,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(espaciado)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (icono != null) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = titulo,
                color = colorTitulo,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
        if (!subtitulo.isNullOrBlank()) {
            Text(
                text = subtitulo,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        contenido()
    }
}

/**
 * Contenedor tipo tarjeta (equivalente a `<article class="card">` en HTML).
 * Aplica el color configurable de tarjetas, bordes configurables y curvatura de esquinas.
 */
@Composable
fun ContenedorTarjeta(
    modifier: Modifier = Modifier,
    colorFondo: Color = ColorTarjetas,
    colorBorde: Color = ColorBordeActual,
    radioEsquinas: Dp = CurvaturaEsquinas,
    grosorBorde: Dp = GrosorBorde,
    paddingInterno: Dp = 16.dp,
    alPulsar: (() -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(radioEsquinas)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(colorFondo)
            .then(
                if (grosorBorde > 0.dp && colorBorde != Color.Transparent) {
                    Modifier.border(grosorBorde, colorBorde, forma)
                } else {
                    Modifier
                }
            )
            .then(if (alPulsar != null) Modifier.clickable { alPulsar() } else Modifier)
            .padding(paddingInterno)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = contenido
        )
    }
}

/**
 * Contenedor para filas de elementos interactivos (equivalente a `<div class="row">` en HTML).
 * Organiza icono inicial, textos y acción final (botón, switch, chevron, etc.).
 */
@Composable
fun ContenedorFila(
    titulo: String,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color = ColorIconosInternos,
    alPulsar: (() -> Unit)? = null,
    accionFinal: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (alPulsar != null) Modifier.clickable { alPulsar() } else Modifier)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(colorIcono.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorIcono,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
            if (!subtitulo.isNullOrBlank()) {
                Text(
                    text = subtitulo,
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        if (accionFinal != null) {
            Spacer(Modifier.width(8.dp))
            accionFinal()
        }
    }
}

/**
 * Contenedor destacado tipo aviso o banner (equivalente a `<aside class="callout">` en HTML).
 */
@Composable
fun ContenedorDestacado(
    titulo: String,
    descripcion: String,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    colorBordeAcento: Color = ColorAcento,
    accion: (@Composable () -> Unit)? = null
) {
    val forma = FormaTarjeta
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(colorBordeAcento.copy(alpha = 0.08f))
            .then(
                if (GrosorBorde > 0.dp) {
                    Modifier.border(GrosorBorde, colorBordeAcento.copy(alpha = 0.35f), forma)
                } else {
                    Modifier
                }
            )
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icono != null) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorBordeAcento,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = titulo,
                    color = TextoPrincipal,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            Text(
                text = descripcion,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
            if (accion != null) {
                Spacer(Modifier.height(4.dp))
                accion()
            }
        }
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

/**
 * Botón estilizado con color semántico personalizado, bordes y esquinas de tema dinámico.
 */
@Composable
fun BotonColorido(
    texto: String,
    color: Color,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    activo: Boolean = true,
    alPulsar: () -> Unit
) {
    val escala by animateFloatAsState(
        targetValue = if (activo) 1f else 0.98f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "escalaBotonColorido"
    )
    val forma = FormaBoton
    val colorTexto = colorContraste(color)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(forma)
            .background(if (activo) color else Borde)
            .then(
                if (GrosorBorde > 0.dp && !activo) {
                    Modifier.border(GrosorBorde, Borde, forma)
                } else {
                    Modifier
                }
            )
            .clickable(enabled = activo) { alPulsar() },
        contentAlignment = Alignment.Center
    ) {
        if (icono != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = if (activo) colorTexto else TextoSecundario,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = texto,
                    color = if (activo) colorTexto else TextoSecundario,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = (15 * escala * EscalaTexto).sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Text(
                text = texto,
                color = if (activo) colorTexto else TextoSecundario,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = (15 * escala * EscalaTexto).sp,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

/**
 * Tarjeta de acción normalizada según el patrón de diseño de 'Apariencia'.
 * Muestra título descriptivo, texto de contexto y un botón prominente con color semántico.
 */
@Composable
fun TarjetaAccion(
    titulo: String,
    descripcion: String,
    textoBoton: String,
    colorBoton: Color,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    activo: Boolean = true,
    alPulsar: () -> Unit
) {
    ContenedorTarjeta(
        modifier = modifier,
        paddingInterno = 16.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (icono != null) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorBoton,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = titulo,
                color = TextoPrincipal,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
        if (descripcion.isNotBlank()) {
            Text(
                text = descripcion,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(4.dp))
        }
        BotonColorido(
            texto = textoBoton,
            color = colorBoton,
            icono = icono,
            activo = activo,
            alPulsar = alPulsar
        )
    }
}
