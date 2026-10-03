package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.InsigniaIdAjuste
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Grupo de opciones para el menú lateral con encabezado en mayúsculas y tarjeta redondeada estilo MagicOS/One UI.
 */
@Composable
fun GrupoMenuLateral(
    titulo: String,
    modifier: Modifier = Modifier,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    ajustes: AjustesApp? = null,
    contenido: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titulo.uppercase(),
                color = ColorAjusteGris,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp,
                    fontSize = 11.sp
                ),
                modifier = Modifier.weight(1f, fill = false)
            )
            if (mostrarId && !idEtiqueta.isNullOrBlank()) {
                Spacer(modifier = Modifier.width(8.dp))
                InsigniaIdAjuste(id = idEtiqueta, ajustes = ajustes, colorForzado = ColorAcento)
            }
        }
        val formaGrupo = RoundedCornerShape(CurvaturaEsquinas)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(
                            width = GrosorBorde,
                            color = ColorBordeActual,
                            shape = formaGrupo
                        )
                    } else Modifier
                )
                .clip(formaGrupo)
                .background(ColorTarjetaAjustes)
        ) {
            contenido()
        }
    }
}

/**
 * Elemento interactivo del menú lateral con icono redondeado, texto, badge numérico y flecha de navegación.
 */
@Composable
fun ItemMenu(
    texto: String,
    icono: ImageVector,
    colorIcono: Color = ColorIconosInternos,
    colorTexto: Color = ColorTextoAjustes,
    badge: String? = null,
    colorBadge: Color = ColorAcento,
    idEtiqueta: String? = null,
    mostrarId: Boolean = false,
    mostrarChevron: Boolean = true,
    ajustes: AjustesApp? = null,
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
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(11.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = colorTexto,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (mostrarId && !idEtiqueta.isNullOrBlank()) {
                Spacer(Modifier.height(3.dp))
                InsigniaIdAjuste(id = idEtiqueta, ajustes = ajustes, colorForzado = ColorAcento)
            }
        }

        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colorBadge)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.5.sp
                    ),
                    color = Color.White
                )
            }
            Spacer(Modifier.width(4.dp))
        }

        if (mostrarChevron) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = ColorAjusteGris.copy(alpha = 0.7f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

/**
 * Divisor visual tenue entre elementos dentro de un GrupoMenuLateral.
 */
@Composable
fun SeparadorItemMenu(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 61.dp)
            .height(0.5.dp)
            .background(ColorSeparadorAjustes)
    )
}

/**
 * Botón estilo fila para bloquear la aplicación situado debajo del pie del menú lateral.
 * Sigue los colores dinámicos del tema activo (claro / oscuro).
 */
@Composable
fun BotonFilaBloquear(
    alBloquear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = RoundedCornerShape(CurvaturaEsquinas)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                    Modifier.border(
                        width = GrosorBorde,
                        color = ColorBordeActual,
                        shape = forma
                    )
                } else Modifier
            )
            .clip(forma)
            .background(ColorTarjetaAjustes)
            .clickable { alBloquear() }
            .padding(horizontal = 14.dp, vertical = 11.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Lock,
            contentDescription = null,
            tint = ColorAcento,
            modifier = Modifier.size(19.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Bloquear aplicación",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Medium
            ),
            color = ColorTextoAjustes
        )
    }
}
