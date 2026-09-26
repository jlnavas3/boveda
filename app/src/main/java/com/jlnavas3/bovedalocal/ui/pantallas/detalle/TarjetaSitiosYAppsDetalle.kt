package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosApp
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

@Composable
fun TarjetaSitiosYAppsDetalle(
    urls: List<String>,
    alCopiarUrl: (String) -> Unit,
    alAvisar: (String) -> Unit
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    val listaUrls = remember(urls) {
        urls.flatMap { it.split(",", "\n", ";") }.map { it.trim() }.filter { it.isNotEmpty() }
    }
    if (listaUrls.isEmpty()) return

    GrupoAjustes(etiqueta = "Sitios web y apps (${listaUrls.size})") {
        listaUrls.forEachIndexed { index, url ->
            val paquete = remember(url) { LanzadorEnlaces.extraerPaquete(url) }
            val esApp = paquete != null
            val estaInstalada = remember(url, contexto) {
                if (paquete != null) LanzadorEnlaces.estaInstalada(contexto, paquete) else false
            }
            val nombreApp = remember(url, contexto) {
                if (paquete != null && estaInstalada) LanzadorEnlaces.obtenerNombreApp(contexto, paquete) else null
            }
            val colorDato = if (esApp) ColorDatosApp else ColorDatosWeb

            if (index > 0) SeparadorFilaSimple()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(fondoBadgeParaTema(colorDato))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            haptica.toque()
                            LanzadorEnlaces.abrir(contexto, url, onAviso = alAvisar)
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(FormaPequena)
                            .background(fondoBadgeParaTema(colorDato)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (esApp) Icons.Filled.Android else Icons.Filled.Language,
                            contentDescription = null,
                            tint = colorLegibleParaTema(colorDato),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = nombreApp ?: paquete ?: url,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = TextoPrincipal,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Clip
                        )
                        Spacer(Modifier.height(2.dp))
                        val subtitulo = when {
                            esApp && estaInstalada -> "App instalada · Toca para abrir"
                            esApp -> "App no instalada · Ver en Google Play"
                            else -> "Sitio web · Toca para abrir"
                        }
                        Text(
                            text = subtitulo,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = if (esApp && estaInstalada) Menta else TextoSecundario,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    IconButton(
                        onClick = {
                            haptica.toque()
                            alCopiarUrl(url)
                        },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = "Copiar enlace",
                            tint = ColorIconosInternos,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = {
                            haptica.toque()
                            LanzadorEnlaces.abrir(contexto, url, onAviso = alAvisar)
                        },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = "Abrir enlace",
                            tint = ColorIconosInternos,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
                Box(modifier = Modifier.matchParentSize()) {
                    Box(
                        modifier = Modifier
                            .width(4.5.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterStart)
                            .background(colorDato)
                    )
                }
            }
        }
    }
}
