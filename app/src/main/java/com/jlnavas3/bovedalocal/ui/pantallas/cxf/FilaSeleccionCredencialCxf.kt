package com.jlnavas3.bovedalocal.ui.pantallas.cxf

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.AccionDeslizamiento
import com.jlnavas3.bovedalocal.ui.componentes.CheckboxBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorDeslizamientoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IconoTipoEntrada
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IndicadorContenidoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Fila individual para la selección de una credencial en el diálogo interactivo de exportación CXF.
 * Soporta deslizamiento horizontal (swipe) para alternar selección/deselección con retroalimentación háptica.
 * Muestra indicador de contenido en tarjetas, icono de app asociada (o tipo), clave oculta con puntos,
 * URLs, y tipografía compacta gobernada por 02-APA-TYP y 02-APA-GEO.
 */
@Composable
fun FilaSeleccionCredencialCxf(
    entrada: Entrada,
    seleccionada: Boolean,
    alAlternar: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val forma = FormaTarjeta
    val tieneContrasena = entrada.contrasena.isNotBlank()
    val urlsLimpias = remember(entrada.urls) { entrada.urls.filter { it.isNotBlank() } }

    val accionDeslizamiento = remember(seleccionada) {
        AccionDeslizamiento(
            texto = if (seleccionada) "Deseleccionar" else "Seleccionar",
            icono = if (seleccionada) Icons.Filled.Close else Icons.Filled.Check,
            color = if (seleccionada) Peligro else ColorAcento,
            alEjecutar = { alAlternar(!seleccionada) }
        )
    }

    ContenedorDeslizamientoBoveda(
        idItem = entrada.id,
        modifier = modifier,
        forma = forma,
        accionIzquierda = accionDeslizamiento,
        accionDerecha = accionDeslizamiento
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(ColorTarjetas)
                .then(
                    if (GrosorBorde > 0.dp && EstiloBorde != "ninguno" && ColorBordeActual != Color.Transparent) {
                        Modifier.border(GrosorBorde, ColorBordeActual, forma)
                    } else Modifier
                )
                .clickable { alAlternar(!seleccionada) }
        ) {
            // Línea de indicadores de contenido en tarjetas
            IndicadorContenidoTarjeta(entrada = entrada)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Icono de la app asociada o tipo de credencial
                IconoTipoEntrada(
                    entrada = entrada,
                    tamanoIcono = 34
                )

                // Información central: Título, Usuario, Contraseña (puntos), Enlaces
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entrada.titulo.ifBlank { "Sin título" },
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = (12.5f * EscalaTexto).sp
                        ),
                        color = ColorTitulos,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (entrada.usuario.isNotBlank()) {
                        Spacer(Modifier.height(1.5.dp))
                        Text(
                            text = entrada.usuario,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = (11f * EscalaTexto).sp
                            ),
                            color = TextoSecundario,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (tieneContrasena) {
                        Spacer(Modifier.height(1.5.dp))
                        val textoClave = "•".repeat(entrada.contrasena.length.coerceIn(8, 16))
                        Text(
                            text = textoClave,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = (11f * EscalaTexto).sp
                            ),
                            color = TextoSecundario.copy(alpha = 0.85f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (urlsLimpias.isNotEmpty()) {
                        Spacer(Modifier.height(1.5.dp))
                        urlsLimpias.forEach { url ->
                            Text(
                                text = url,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = (10f * EscalaTexto).sp
                                ),
                                color = TextoSecundario.copy(alpha = 0.7f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Checkbox unificado
                CheckboxBoveda(
                    checked = seleccionada,
                    onCheckedChange = alAlternar
                )
            }
        }
    }
}
