package com.jlnavas3.bovedalocal.ui.pantallas.passkeys

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.util.IconoAppCircular
import com.jlnavas3.bovedalocal.util.rememberIconoAppInstalada
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosPasskey
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import java.text.SimpleDateFormat
import java.util.Date

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.icons.filled.Check
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IndicadorContenidoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import androidx.compose.foundation.shape.RoundedCornerShape
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilaPasskey(
    entrada: Entrada,
    formato: SimpleDateFormat,
    haptica: Haptica,
    alPulsar: () -> Unit,
    alAlternarFavorito: () -> Unit,
    modifier: Modifier = Modifier,
    seleccionActiva: Boolean = false,
    seleccionado: Boolean = false,
    alPulsarLargo: (() -> Unit)? = null,
    alAlternarSeleccion: (() -> Unit)? = null,
    mostrarIndicadores: Boolean = false
) {
    val datos = entrada.passkey ?: return
    val forma = RoundedCornerShape(CurvaturaEsquinas)

    val fondoFila = if (seleccionado) {
        Ambar.copy(alpha = 0.22f)
    } else {
        ColorTarjetaAjustes
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(forma)
            .background(fondoFila)
            .then(
                if (seleccionado) Modifier.border(1.dp, Ambar.copy(alpha = 0.5f), forma)
                else if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") Modifier.border(GrosorBorde, ColorBordeActual, forma)
                else Modifier
            )
    ) {
        if (mostrarIndicadores) {
            IndicadorContenidoTarjeta(
                entrada = entrada,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .background(ColorDatosPasskey)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        if (seleccionActiva) {
                            haptica.tic()
                            alAlternarSeleccion?.invoke()
                        } else {
                            haptica.toque()
                            alPulsar()
                        }
                    },
                    onLongClick = {
                        if (!seleccionActiva && alPulsarLargo != null) {
                            haptica.toque()
                            alPulsarLargo()
                        }
                    }
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (seleccionActiva) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(if (seleccionado) Ambar else Borde),
                    contentAlignment = Alignment.Center
                ) {
                    if (seleccionado) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = ColorSobreAcento,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
            }

            val iconoApp = rememberIconoAppInstalada(entrada)
            if (iconoApp != null) {
                IconoAppCircular(
                    bitmap = iconoApp,
                    descripcion = datos.rpName.ifBlank { datos.rpId },
                    tamanoDp = 42.dp
                )
            } else {
                Monograma(
                    titulo = datos.rpName.ifBlank { datos.rpId },
                    semilla = datos.rpId,
                    tamano = 42
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    datos.rpName.ifBlank { datos.rpId },
                    color = ColorTitulos,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    datos.usuario.ifBlank { entrada.usuario.ifBlank { datos.rpId } },
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Creada el ${formato.format(Date(entrada.creadaEn))}",
                    color = Menta,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }

            if (!seleccionActiva) {
                // Botón de favorito con ripple circular
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable {
                            haptica.tic()
                            alAlternarFavorito()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = if (entrada.favorito) "Quitar de favoritos" else "Marcar como favorito",
                        tint = if (entrada.favorito) Ambar else ColorIconosInternos.copy(alpha = 0.25f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = TextoSecundario.copy(alpha = 0.4f),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
