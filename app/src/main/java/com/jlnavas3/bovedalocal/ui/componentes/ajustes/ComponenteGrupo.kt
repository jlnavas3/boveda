@file:OptIn(ExperimentalFoundationApi::class)

package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.colorParaGrupoId

val LocalColorGrupo = compositionLocalOf<Color?> { null }

/**
 * Contenedor de tarjeta agrupada para Ajustes estilo Samsung One UI / Honor MagicOS.
 *
 * Características:
 * - Cabecera con ícono pequeño opcional y etiqueta en mayúsculas discretas.
 * - Botón minimalista de restablecer grupo a la derecha (RestartAlt) sin texto.
 * - InsigniaIdAjuste posicionado homogéneamente debajo del título del grupo si mostrarId es true.
 * - Tarjeta con bordes redondeados (18.dp) y fondo de superficie limpio.
 * - Descripción o subtítulo opcional a nivel de grupo.
 */
@Composable
fun ComponenteGrupo(
    modifier: Modifier = Modifier,
    etiqueta: String? = null,
    icono: ImageVector? = null,
    colorIcono: Color? = null,
    alRestablecer: (() -> Unit)? = null,
    idGrupo: String? = null,
    mostrarId: Boolean = false,
    descripcion: String? = null,
    descripcionComoPie: Boolean = true,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val tieneBadgeGrupo = mostrarId && !idGrupo.isNullOrBlank()
    val colorBadgeGrupo = colorParaGrupoId(idGrupo)
    val estadoAlumbrado = recordarEstadoAlumbrado(idGrupo)
    val coordinador = LocalCoordinadorResaltado.current
    val colorAcento = ColorAcento

    Column(
        modifier = modifier
            .fillMaxWidth()
            .bringIntoViewRequester(estadoAlumbrado.bringIntoViewRequester)
            .onGloballyPositioned { coords ->
                coordinador?.registrarYEjecutarSiCoincide(
                    id = idGrupo,
                    itemCoordinates = coords,
                    estadoAlumbrado = estadoAlumbrado,
                    colorAcento = colorAcento
                )
            }
    ) {
        // Encabezado del grupo
        if (!etiqueta.isNullOrBlank() || tieneBadgeGrupo || alRestablecer != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 12.dp, bottom = 8.dp, top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f, fill = false),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (icono != null) {
                            Icon(
                                imageVector = icono,
                                contentDescription = null,
                                tint = colorIcono ?: ColorAjusteGris,
                                modifier = Modifier.size(15.dp)
                            )
                        }

                        if (!etiqueta.isNullOrBlank()) {
                            Text(
                                text = etiqueta.uppercase(),
                                color = ColorAjusteGris,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    }

                    if (tieneBadgeGrupo) {
                        Spacer(Modifier.height(3.dp))
                        InsigniaIdAjuste(id = idGrupo!!)
                    }
                }

                if (alRestablecer != null) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(onClick = alRestablecer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.RestartAlt,
                            contentDescription = "Restablecer grupo",
                            tint = ColorAjusteGris,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Descripción antes de la tarjeta si descripcionComoPie es false
        if (!descripcionComoPie && !descripcion.isNullOrBlank()) {
            Text(
                text = descripcion,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )
        }

        // Tarjeta agrupada con esquinas redondeadas
        val formaTarjeta = RoundedCornerShape(CurvaturaEsquinas)
        CompositionLocalProvider(LocalColorGrupo provides colorBadgeGrupo) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (GrosorBorde > 0.dp) {
                            Modifier.border(
                                width = GrosorBorde,
                                color = ColorBordeActual,
                                shape = formaTarjeta
                            )
                        } else Modifier
                    )
                    .clip(formaTarjeta)
                    .background(ColorTarjetaAjustes)
                    .background(estadoAlumbrado.colorFondoAnimado.value)
            ) {
                contenido()
            }
        }

        // Descripción como pie sutil debajo de la tarjeta (estilo nativo Samsung One UI)
        if (descripcionComoPie && !descripcion.isNullOrBlank()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = descripcion,
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
