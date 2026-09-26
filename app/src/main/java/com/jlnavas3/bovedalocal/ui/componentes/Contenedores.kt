package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema

/**
 * Contenedor principal de pantalla (equivalente al `<main class="container">` en HTML).
 * Controla el fondo, la cabecera fija superior (si se indica título y alVolver),
 * el padding exterior homogéneo y el scroll vertical automático.
 */
@Composable
fun ContenedorPrincipal(
    modifier: Modifier = Modifier,
    titulo: String? = null,
    alVolver: (() -> Unit)? = null,
    subtitulo: String? = null,
    acciones: (@Composable RowScope.() -> Unit)? = null,
    conScroll: Boolean = true,
    paddingHorizontal: Dp = 20.dp,
    paddingVertical: Dp = 16.dp,
    espaciado: Dp = EspaciadoComponentes,
    cabeceraFlotante: (@Composable () -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Obsidiana)
    ) {
        if (titulo != null && alVolver != null) {
            BarraSuperiorPantalla(
                titulo = titulo,
                alVolver = alVolver,
                conSeparador = conScroll && scrollState.value > 0,
                colorFondo = Obsidiana,
                acciones = acciones
            )
        }

        val modScroll = if (conScroll) Modifier.verticalScroll(scrollState) else Modifier

        if (cabeceraFlotante != null) {
            val density = LocalDensity.current
            var alturaCabeceraDp by remember { mutableStateOf(140.dp) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(modScroll)
                        .padding(horizontal = paddingHorizontal)
                        .padding(top = alturaCabeceraDp + 8.dp, bottom = paddingVertical),
                    verticalArrangement = Arrangement.spacedBy(espaciado)
                ) {
                    if (!subtitulo.isNullOrBlank()) {
                        DescripcionPantalla(subtitulo = subtitulo)
                    }
                    contenido()
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .background(Obsidiana)
                        .onGloballyPositioned { coords ->
                            alturaCabeceraDp = with(density) { coords.size.height.toDp() }
                        }
                        .padding(horizontal = paddingHorizontal, vertical = 6.dp)
                ) {
                    cabeceraFlotante()
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .then(modScroll)
                    .padding(horizontal = paddingHorizontal, vertical = paddingVertical),
                verticalArrangement = Arrangement.spacedBy(espaciado)
            ) {
                if (!subtitulo.isNullOrBlank()) {
                    DescripcionPantalla(subtitulo = subtitulo)
                }
                contenido()
            }
        }
    }
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
    colorIcono: Color = ColorIconosInternos,
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
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (icono != null) {
                val colorLegible = colorLegibleParaTema(colorIcono)
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(FormaPequena)
                        .background(fondoBadgeParaTema(colorIcono)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorLegible,
                        modifier = Modifier.size(19.dp)
                    )
                }
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
            val colorLegible = colorLegibleParaTema(colorIcono)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(FormaPequena)
                    .background(fondoBadgeParaTema(colorIcono)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = colorLegible,
                    modifier = Modifier.size(19.dp)
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
    val colorLegible = colorLegibleParaTema(colorBordeAcento)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(fondoBadgeParaTema(colorBordeAcento))
            .then(
                if (GrosorBorde > 0.dp) {
                    Modifier.border(GrosorBorde, colorLegible.copy(alpha = 0.35f), forma)
                } else {
                    Modifier
                }
            )
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icono != null) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(FormaPequena)
                            .background(fondoBadgeParaTema(colorBordeAcento)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = colorLegible,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
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
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (icono != null) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(FormaPequena)
                        .background(colorBoton.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorBoton,
                        modifier = Modifier.size(20.dp)
                    )
                }
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
