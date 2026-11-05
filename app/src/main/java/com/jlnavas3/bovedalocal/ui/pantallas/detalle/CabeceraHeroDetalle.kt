/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EscalaTexto
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Dominios
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.IconoAppCircular
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces
import com.jlnavas3.bovedalocal.util.rememberIconoAppInstalada
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CabeceraHeroDetalle(
    entrada: Entrada,
    alMostrarQr: () -> Unit,
    alActualizarTitulo: ((String) -> Unit)? = null,
    seguridadVisualActiva: Boolean = true
) {
    val contexto = LocalContext.current
    val iconoApp = rememberIconoAppInstalada(entrada)

    val paquete = remember(entrada, contexto) {
        GestorAppsInstaladas.resolverPaqueteApp(contexto, entrada)
    }
    val nombreApp = remember(paquete, contexto) {
        if (paquete != null && LanzadorEnlaces.estaInstalada(contexto, paquete)) {
            LanzadorEnlaces.obtenerNombreApp(contexto, paquete)
        } else null
    }
    val domPaquete = remember(paquete) { if (paquete != null) Dominios.dominioDePaquete(paquete) else null }
    val marcaPaquete = remember(domPaquete) { if (domPaquete != null) Dominios.marca(domPaquete) else null }
    val esTituloTecnico = remember(entrada.titulo, domPaquete, marcaPaquete, paquete, nombreApp) {
        val tit = entrada.titulo.trim()
        val titDominio = tit.removePrefix("https://").removePrefix("http://").removePrefix("www.").trimEnd('/')
        !nombreApp.isNullOrBlank() && !tit.equals(nombreApp, ignoreCase = true) && (
            tit.isBlank() ||
                tit == "Nueva entrada" ||
                (domPaquete != null && (tit.equals(domPaquete, ignoreCase = true) || titDominio.equals(domPaquete, ignoreCase = true))) ||
                (paquete != null && tit.equals(paquete, ignoreCase = true)) ||
                com.jlnavas3.bovedalocal.util.NormalizadorTitulosSitios.esTituloTecnico(tit, entrada.urls)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icono o Monograma compacto
            if (iconoApp != null) {
                IconoAppCircular(
                    bitmap = iconoApp,
                    descripcion = entrada.titulo,
                    tamanoDp = 44.dp
                )
            } else {
                Monograma(
                    titulo = entrada.titulo.ifBlank { "?" },
                    semilla = entrada.urls.firstOrNull() ?: entrada.passkey?.rpId ?: entrada.titulo,
                    tamano = 44
                )
            }

            // Título y Chip de Tipo en columna vertical compacta
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = entrada.titulo.ifBlank { "Sin título" },
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = (18 * EscalaTexto).sp
                    ),
                    color = ColorTitulos,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(FormaPequena)
                            .background(ColorAcento.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = entrada.tipo.etiqueta,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = (10 * EscalaTexto).sp
                            ),
                            color = ColorAcento
                        )
                    }
                    if (seguridadVisualActiva && entrada.ignoradaEnSalud) {
                        Box(
                            modifier = Modifier
                                .clip(FormaPequena)
                                .background(Peligro.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.VisibilityOff,
                                    contentDescription = null,
                                    tint = Peligro,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = "Ignorada",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = (10 * EscalaTexto).sp
                                    ),
                                    color = Peligro
                                )
                            }
                        }
                    }

                    if (entrada.creadaEn > 0L) {
                        val formatoFecha = remember {
                            SimpleDateFormat("dd/MM/yy", Locale.forLanguageTag("es-ES"))
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier.padding(start = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarToday,
                                contentDescription = null,
                                tint = TextoSecundario,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = formatoFecha.format(Date(entrada.creadaEn)),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontStyle = FontStyle.Italic,
                                    fontSize = (11 * EscalaTexto).sp
                                ),
                                color = TextoSecundario
                            )
                        }
                    }
                }
            }
        }

        if (esTituloTecnico && nombreApp != null && alActualizarTitulo != null) {
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(ColorAcento.copy(alpha = 0.15f))
                    .clickable { alActualizarTitulo(nombreApp) }
                    .padding(horizontal = 12.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.AutoFixHigh,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Actualizar a \"$nombreApp\"",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = ColorAcento,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
