package com.jlnavas3.bovedalocal.ui.pantallas.edicion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
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
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteCampoTexto
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.TipoCampoTexto
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosApp
import com.jlnavas3.bovedalocal.ui.theme.ColorDatosWeb
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.EnlaceEditable
import com.jlnavas3.bovedalocal.util.GestorAppsInstaladas
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

@Composable
fun SeccionSitiosYAppsEdicion(
    listaEnlaces: MutableList<EnlaceEditable>,
    alSolicitarExplorarApp: (Int?) -> Unit
) {
    val contexto = LocalContext.current

    GrupoAjustes(etiqueta = "Sitios o aplicaciones") {
        Column(modifier = Modifier.padding(14.dp)) {
            if (listaEnlaces.isNotEmpty()) {
                listaEnlaces.forEachIndexed { index, enlace ->
                    val paquete = remember(enlace.valor) { LanzadorEnlaces.extraerPaquete(enlace.valor) }
                    val esAppEnlace = paquete != null
                    val esApp = remember(paquete, contexto) { paquete != null && LanzadorEnlaces.estaInstalada(contexto, paquete) }
                    val nombreApp = remember(paquete, contexto, esApp) {
                        if (paquete != null && esApp) LanzadorEnlaces.obtenerNombreApp(contexto, paquete) else null
                    }
                    val iconoApp = remember(paquete, contexto, esApp) {
                        if (paquete != null && esApp) GestorAppsInstaladas.obtenerIconoApp(contexto, paquete) else null
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        ComponenteCampoTexto(
                            valor = enlace.valor,
                            etiqueta = if (listaEnlaces.size > 1) "Sitio web o app #${index + 1}" else "Sitio web o app",
                            alCambiar = { nuevoTexto ->
                                listaEnlaces[index] = enlace.copy(valor = nuevoTexto)
                            },
                            tipo = TipoCampoTexto.ENLACE,
                            mostrarIcono = true,
                            icono = if (esAppEnlace) Icons.Filled.Android else Icons.Filled.Language,
                            leadingIcon = if (esApp && iconoApp != null) {
                                {
                                    Image(
                                        bitmap = iconoApp,
                                        contentDescription = nombreApp ?: "App",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(FormaPequena)
                                    )
                                }
                            } else null,
                            colorBordeIzquierdo = if (esAppEnlace) ColorDatosApp else ColorDatosWeb,
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        listaEnlaces.removeAt(index)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Eliminar sitio o app",
                                        tint = TextoSecundario,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        )

                        if (enlace.hashOriginal != null) {
                            Spacer(Modifier.height(4.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(FormaPequena)
                                    .background(ColorCampoAjustes)
                                    .border(1.dp, ColorSeparadorAjustes, FormaPequena)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Security,
                                    contentDescription = "Certificado DAL",
                                    tint = ColorAcento,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Certificado DAL: ${enlace.hashOriginal}",
                                    style = EstiloMono.copy(fontSize = 11.sp),
                                    color = TextoPrincipal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        if (esApp && nombreApp != null) {
                            Spacer(Modifier.height(4.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(FormaPequena)
                                    .background(ColorCampoAjustes)
                                    .border(1.dp, ColorSeparadorAjustes, FormaPequena)
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Android,
                                    contentDescription = "App detectada",
                                    tint = ColorAcento,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "App detectada: $nombreApp",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = TextoPrincipal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BotonBorde(
                    texto = "Añadir sitio web",
                    icono = Icons.Filled.Add,
                    modifier = Modifier.weight(1f)
                ) {
                    listaEnlaces.add(EnlaceEditable(valor = ""))
                }

                BotonBorde(
                    texto = "Explorar app",
                    icono = Icons.Filled.Android,
                    modifier = Modifier.weight(1f)
                ) {
                    alSolicitarExplorarApp(null)
                }
            }
        }
    }
}
