package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana

/**
 * Contenedor principal de pantalla (equivalente al main container en UI).
 * Controla el fondo, la cabecera fija superior (si se indica título y alVolver),
 * el padding exterior homogéneo y el scroll vertical automático con soporte de cabecera flotante.
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
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    overlayLateral: (@Composable BoxScope.() -> Unit)? = null,
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
                idEtiqueta = idEtiqueta,
                mostrarId = mostrarId,
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

                overlayLateral?.invoke(this)
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(modScroll)
                        .padding(horizontal = paddingHorizontal, vertical = paddingVertical),
                    verticalArrangement = Arrangement.spacedBy(espaciado)
                ) {
                    if (!subtitulo.isNullOrBlank()) {
                        DescripcionPantalla(subtitulo = subtitulo)
                    }
                    contenido()
                }

                overlayLateral?.invoke(this)
            }
        }
    }
}
