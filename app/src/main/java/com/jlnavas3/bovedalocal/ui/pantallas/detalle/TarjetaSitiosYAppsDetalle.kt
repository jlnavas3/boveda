package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosApp
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces
import kotlinx.coroutines.delay

@Composable
fun TarjetaSitiosYAppsDetalle(
    urls: List<String>,
    alCopiarUrl: (String) -> Unit,
    alAvisar: (String) -> Unit,
    alAbrirUrl: ((String) -> Unit)? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }

    val listaUrls = remember(urls) {
        urls.flatMap { it.split(",", "\n", ";") }.map { it.trim() }.filter { it.isNotEmpty() }
    }
    if (listaUrls.isEmpty()) return

    val urlCopiadas = remember { mutableStateMapOf<String, Boolean>() }

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Sitios web y apps (${listaUrls.size})")

        listaUrls.forEachIndexed { index, url ->
            val paquete = remember(url) { LanzadorEnlaces.extraerPaquete(url) }
            val esApp = paquete != null
            val estaInstalada = remember(url, contexto) {
                if (paquete != null) LanzadorEnlaces.estaInstalada(contexto, paquete) else false
            }
            val nombreApp = remember(url, contexto) {
                if (paquete != null && estaInstalada) LanzadorEnlaces.obtenerNombreApp(contexto, paquete) else null
            }
            val iconoAppBitmap = remember(paquete, contexto, estaInstalada) {
                if (paquete != null && estaInstalada) GestorAppsInstaladas.obtenerIconoApp(contexto, paquete) else null
            }
            val colorDato = if (esApp) ColorDatosApp else ColorDatosWeb

            if (index > 0) {
                Spacer(Modifier.height(EspaciadoDetalle))
            }

            val copiado = urlCopiadas[url] == true
            LaunchedEffect(copiado) {
                if (copiado) {
                    delay(1500)
                    urlCopiadas[url] = false
                }
            }

            TarjetaDatoDetalle(
                colorBorde = colorDato,
                alPulsar = {
                    haptica.toque()
                    alAbrirUrl?.invoke(url)
                    LanzadorEnlaces.abrir(contexto, url, onAviso = alAvisar)
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 10.dp, top = 12.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (esApp && iconoAppBitmap != null) {
                        Image(
                            bitmap = iconoAppBitmap,
                            contentDescription = nombreApp ?: paquete ?: "App",
                            modifier = Modifier
                                .size(38.dp)
                                .clip(FormaPequena)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(FormaPequena)
                                .background(ColorCampoAjustes)
                                .border(1.dp, ColorSeparadorAjustes, FormaPequena),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (esApp) Icons.Filled.Android else Icons.Filled.Language,
                                contentDescription = null,
                                tint = ColorAcento,
                                modifier = Modifier.size(20.dp)
                            )
                        }
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
                            color = TextoSecundario,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    BotonCopiarDetalle(
                        copiado = copiado,
                        alPulsar = {
                            haptica.toque()
                            urlCopiadas[url] = true
                            alCopiarUrl(url)
                        }
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaSitiosYAppsDetallePreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        TarjetaSitiosYAppsDetalle(
            urls = listOf(
                "https://accounts.google.com",
                "androidapp://com.google.android.gm"
            ),
            alCopiarUrl = {},
            alAvisar = {}
        )
    }
}

