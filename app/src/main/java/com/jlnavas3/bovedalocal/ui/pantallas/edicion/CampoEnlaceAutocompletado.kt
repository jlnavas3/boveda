package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AppInstalada
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

private fun esUrlWeb(q: String): Boolean {
    val limpio = q.trim().lowercase()
    if (limpio.isBlank()) return false
    if (limpio.startsWith("http://") ||
        limpio.startsWith("https://") ||
        limpio.startsWith("www.") ||
        limpio.contains("/") ||
        limpio.contains("?") ||
        limpio.contains("@")
    ) {
        return true
    }
    val partes = limpio.split('.')
    if (partes.size == 2) {
        val tldsWeb = setOf(
            "com", "org", "net", "edu", "gov", "es", "co", "mx", "io", "dev",
            "app", "ai", "tv", "me", "cl", "pe", "ar", "ec", "ve", "br", "uk"
        )
        if (tldsWeb.contains(partes.last())) {
            return true
        }
    }
    return false
}

/**
 * Campo de entrada para enlaces o aplicaciones que muestra un desplegable emergente
 * de autocompletado en tiempo real con las aplicaciones instaladas que coincidan.
 */
@Composable
fun CampoEnlaceAutocompletado(
    valor: String,
    etiqueta: String,
    alCambiar: (String) -> Unit,
    listaApps: List<AppInstalada>,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val esOscuro = isSystemInDarkTheme()
    val fondoPopup = if (esOscuro) Color(0xFF28272A) else Color(0xFFF5F5F7)

    var enFoco by remember { mutableStateOf(false) }
    var ignorarSugerencias by remember { mutableStateOf(false) }

    val q = valor.trim()
    val esWeb = remember(q) { esUrlWeb(q) }

    val coincidencias = remember(q, listaApps, enFoco, ignorarSugerencias, esWeb) {
        if (!enFoco || ignorarSugerencias || q.length < 2 || esWeb) {
            emptyList()
        } else {
            val filtradas = GestorAppsInstaladas.filtrarApps(listaApps, q.lowercase())
            if (filtradas.size == 1 && filtradas.first().paquete.equals(q, ignoreCase = true)) {
                emptyList()
            } else {
                filtradas.take(4)
            }
        }
    }

    val mostrarPlayStore = enFoco && !ignorarSugerencias && q.length >= 2 && !esWeb && !LanzadorEnlaces.esNombrePaquete(q)

    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { enFoco = it.isFocused }
        ) {
            ComponenteCampoTexto(
                valor = valor,
                etiqueta = etiqueta,
                alCambiar = { nuevo ->
                    ignorarSugerencias = false
                    alCambiar(nuevo)
                },
                tipo = TipoCampoTexto.ENLACE,
                trailingIcon = trailingIcon
            )

            val desplegado = (coincidencias.isNotEmpty() || mostrarPlayStore)
            DropdownMenu(
                expanded = desplegado,
                onDismissRequest = { ignorarSugerencias = true },
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .background(fondoPopup)
            ) {
                coincidencias.forEach { app ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                if (app.iconoBitmap != null) {
                                    Image(
                                        bitmap = app.iconoBitmap,
                                        contentDescription = app.nombre,
                                        modifier = Modifier.size(28.dp)
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(FormaPequena)
                                            .background(ColorAcento.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Android,
                                            contentDescription = null,
                                            tint = ColorAcento,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = app.nombre,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = TextoPrincipal,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = app.paquete,
                                        style = EstiloMono.copy(fontSize = 10.sp),
                                        color = TextoSecundario,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        },
                        onClick = {
                            haptica.toque()
                            ignorarSugerencias = true
                            alCambiar(app.paquete)
                        }
                    )
                }

                if (mostrarPlayStore) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(FormaPequena)
                                        .background(Ambar.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ShoppingBag,
                                        contentDescription = null,
                                        tint = Ambar,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Spacer(Modifier.width(10.dp))

                                Text(
                                    text = "Buscar en Play Store",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = Ambar,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        },
                        onClick = {
                            haptica.toque()
                            ignorarSugerencias = true
                            val uri = Uri.encode(q)
                            LanzadorEnlaces.abrir(contexto, "market://search?q=$uri")
                        }
                    )
                }
            }
        }
    }
}
