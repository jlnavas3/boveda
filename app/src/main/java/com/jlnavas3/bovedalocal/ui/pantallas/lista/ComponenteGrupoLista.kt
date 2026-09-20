package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Contenedor de tarjeta agrupada para el Listado Principal estilo Samsung One UI / Honor MagicOS.
 *
 * Encapsula la cabecera del grupo (dominio, total de cuentas, icono y chevron)
 * y sus entradas hijas dentro de una única tarjeta con esquinas redondeadas (18.dp),
 * separadas por divisores interiores discretos con sangría interior.
 */
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ComponenteGrupoLista(
    clave: String,
    entradas: List<Entrada>,
    expandido: Boolean,
    alAlternar: () -> Unit,
    modifier: Modifier = Modifier,
    alturaFila: Dp = 74.dp,
    tamanoMonograma: Int = 46,
    resaltado: Boolean = false,
    alPulsarLargo: (() -> Unit)? = null,
    contenidoEntrada: @Composable (entrada: Entrada, indice: Int, total: Int) -> Unit
) {
    val compacta = alturaFila.value <= 48f
    val formaGrupo = RoundedCornerShape(18.dp)
    val tamanoIcono = if (compacta) 32 else if (alturaFila.value <= 64f) 36 else 40
    val sangriaSeparador = (14 + tamanoIcono + 12).dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(formaGrupo)
            .background(if (resaltado) Ambar.copy(alpha = 0.16f) else ColorTarjetaAjustes)
            .then(
                if (resaltado) Modifier.border(1.5.dp, Ambar, formaGrupo) else Modifier
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Cabecera del grupo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(alturaFila)
                    .combinedClickable(
                        onClick = alAlternar,
                        onLongClick = alPulsarLargo
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(tamanoIcono.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF5C6BC0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Dns,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size((tamanoIcono * 0.52f).dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = clave,
                        style = if (compacta) {
                            MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        } else {
                            MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        },
                        color = if (resaltado) Ambar else TextoPrincipal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "${entradas.size} ${if (entradas.size == 1) "cuenta" else "cuentas"}",
                        style = if (compacta) {
                            MaterialTheme.typography.labelSmall
                        } else {
                            MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp)
                        },
                        color = TextoSecundario,
                        maxLines = 1
                    )
                }

                Icon(
                    imageVector = if (expandido) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expandido) "Contraer" else "Expandir",
                    tint = if (resaltado) Ambar else ColorIconosInternos
                )
            }

            // Entradas hijas integradas dentro de la misma tarjeta
            AnimatedVisibility(
                visible = expandido,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    entradas.forEachIndexed { indice, entrada ->
                        HorizontalDivider(
                            color = ColorBordeActual.copy(alpha = 0.20f),
                            thickness = 0.6.dp,
                            modifier = Modifier.padding(start = sangriaSeparador, end = 14.dp)
                        )
                        contenidoEntrada(entrada, indice, entradas.size)
                    }
                }
            }
        }
    }
}
