package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MergeType
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.GrupoDuplicado
import com.jlnavas3.bovedalocal.data.TipoDuplicado
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosContrasena
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TarjetaGrupoDuplicado(
    grupo: GrupoDuplicado,
    alConservar: (Entrada) -> Unit,
    alUnificar: () -> Unit,
    alVerDetalle: (String) -> Unit
) {
    val colorBadge = when (grupo.tipo) {
        TipoDuplicado.IDENTICO -> Menta
        TipoDuplicado.MISMA_CUENTA_DISTINTA_CLAVE -> Peligro
        TipoDuplicado.VARIANTE_USUARIO -> ColorAcento
    }

    val iconoGrupo = when {
        grupo.tipo == TipoDuplicado.IDENTICO -> Icons.Filled.ContentCopy
        grupo.esAppAndroid -> Icons.Filled.Android
        else -> Icons.AutoMirrored.Filled.MergeType
    }
    val origen = if (grupo.esAppAndroid) "App Android" else "Web"
    val etiquetaGrupo = "${grupo.claveVisual.uppercase()} · $origen · ${grupo.tipo.titulo.uppercase()}"

    GrupoAjustes(etiqueta = etiquetaGrupo) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .background(ColorDatosContrasena)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(fondoBadgeParaTema(colorBadge)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconoGrupo,
                            contentDescription = null,
                            tint = colorLegibleParaTema(colorBadge),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = grupo.claveVisual,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = ColorTextoAjustes
                        )
                        Text(
                            text = grupo.tipo.descripcion,
                            style = MaterialTheme.typography.bodySmall,
                            color = ColorAjusteGris
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Botones de acción rápida para el grupo
                if (grupo.tipo == TipoDuplicado.IDENTICO) {
                    BotonColorido(
                        texto = "Conservar la mejor versión",
                        color = Menta,
                        icono = Icons.Filled.Check,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        alConservar(grupo.sugeridaPrincipal)
                    }
                } else {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        BotonColorido(
                            texto = "Unificar (conservar todas las claves)",
                            color = ColorAcento,
                            icono = Icons.AutoMirrored.Filled.MergeType,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            alUnificar()
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Guarda las claves alternativas en campos personalizados e historial para no perder ninguna.",
                            style = MaterialTheme.typography.labelSmall,
                            color = ColorAjusteGris
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Comparativa de entradas dentro del grupo
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val formatoFecha = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
                    for (entrada in grupo.entradas) {
                        val esPrincipal = entrada.id == grupo.sugeridaPrincipal.id
                        var claveVisible by remember { mutableStateOf(false) }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (esPrincipal) fondoBadgeParaTema(colorBadge).copy(alpha = 0.2f) else Superficie)
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = entrada.titulo.ifBlank { "Sin título" },
                                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                            color = ColorTextoAjustes
                                        )
                                        if (esPrincipal) {
                                            Spacer(Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(fondoBadgeParaTema(Menta))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    "Sugerida",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                                    color = colorLegibleParaTema(Menta)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "Usuario: ${entrada.usuario.ifBlank { "(Vacío)" }}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ColorAjusteGris
                                    )
                                }

                                TextButton(onClick = { alVerDetalle(entrada.id) }) {
                                    Text("Ver", color = ColorAcento)
                                }
                            }

                            Spacer(Modifier.height(4.dp))

                            // Contraseña
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (claveVisible) entrada.contrasena.ifBlank { "(Sin clave)" } else "•".repeat(entrada.contrasena.length.coerceIn(8, 14)),
                                    style = EstiloMono,
                                    color = ColorTextoAjustes,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { claveVisible = !claveVisible },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = if (claveVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = "Mostrar",
                                        tint = ColorAjusteGris,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            // Metadatos y URLs
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Modif: ${formatoFecha.format(Date(entrada.modificadaEn))}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ColorAjusteGris
                                )
                                if (entrada.urls.isNotEmpty()) {
                                    Text(
                                        text = entrada.urls.first(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ColorAjusteGris,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }

                            // Si no es la principal, botón para elegirla como la que se desea conservar
                            if (!esPrincipal) {
                                Spacer(Modifier.height(8.dp))
                                TextButton(
                                    onClick = { alConservar(entrada) },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Icon(Icons.Filled.DoneAll, contentDescription = null, tint = Menta, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("Conservar esta copia", color = Menta, style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
