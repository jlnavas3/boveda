package com.jlnavas3.bovedalocal.ui.pantallas

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorFila
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorSeccion
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.PaletaAcento
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import androidx.compose.material.icons.filled.ColorLens
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorExportacion
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorPapelera
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun PantallaTema(vm: VaultViewModel) {
    val contexto = LocalContext.current
    val actividad = contexto as? Activity
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var dialogoConfirmarIcono by remember { mutableStateOf<PaletaAcento?>(null) }
    var seccionElegida by remember { mutableStateOf(0) }

    ContenedorPrincipal(conScroll = true, espaciado = 18.dp) {
        CabeceraPantalla(
            titulo = "Personalizar Colores",
            subtitulo = "Paleta visual de la app y colores semánticos por módulo",
            alVolver = { vm.volverAtras() }
        )

        // 1. Tarjeta de previsualización en tiempo real
        ContenedorSeccion(
            titulo = "Vista previa en tiempo real",
            subtitulo = "Observa cómo interactúan tus colores en vivo.",
            icono = Icons.Filled.Palette
        ) {
            ContenedorTarjeta(
                colorFondo = ColorTarjetas,
                paddingInterno = 18.dp
            ) {
                Text(
                    text = "Título de Sección de Ejemplo",
                    color = ColorTitulos,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Este es un texto sobre la tarjeta personalizada. El contraste se calcula automáticamente.",
                    color = ColorSobreTarjetas.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(4.dp))
                ContenedorFila(
                    titulo = "Elemento de prueba",
                    subtitulo = "El ícono usa el color asignado",
                    icono = Icons.Filled.Key,
                    colorIcono = ColorIconosInternos
                )
                Spacer(Modifier.height(6.dp))
                BotonAmbar(texto = "Botón con Color de Acento") {}
            }
        }

        // 1.5 Color dinámico del sistema (Material You - Android 12+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            ContenedorSeccion(
                titulo = "Color dinámico del sistema",
                subtitulo = "Sincroniza automáticamente la paleta con tu fondo de pantalla (Material You).",
                icono = Icons.Filled.AutoAwesome
            ) {
                ContenedorTarjeta {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Material You (Monet)", color = TextoPrincipal, style = MaterialTheme.typography.titleMedium)
                            Text(
                                if (ajustes.colorDinamicoSistema) "Activo: usando colores del fondo de pantalla" else "Desactivado: usando paleta elegida",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Switch(
                            checked = ajustes.colorDinamicoSistema,
                            onCheckedChange = {
                                haptica.tic()
                                vm.alternarColorDinamicoSistema(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = ColorSobreAcento,
                                checkedTrackColor = ColorAcento,
                                checkedBorderColor = ColorAcento,
                                uncheckedThumbColor = TextoSecundario,
                                uncheckedTrackColor = SuperficieAlta,
                                uncheckedBorderColor = TextoSecundario
                            )
                        )
                    }
                }
            }
        }

        // 2. Sección: Color de Acento (con Picker en tiempo real)
        ContenedorSeccion(
            titulo = "Color de acento principal",
            subtitulo = "Afecta a los botones destacados, elementos activos y selector flotante.",
            icono = Icons.Filled.Palette
        ) {
            ContenedorTarjeta {
                SelectorColorEnTiempoReal(
                    colorInicial = ColorAcento,
                    titulo = "Acento"
                ) { nuevoColor ->
                    vm.ajustarColorAcento(nuevoColor.aHex())
                }
            }
        }

        // 3. Sección: Color de Íconos Internos (independiente)
        ContenedorSeccion(
            titulo = "Color de íconos internos",
            subtitulo = "Personaliza los íconos de la lista, menú y botones sin alterar el acento general.",
            icono = Icons.Filled.Security
        ) {
            ContenedorTarjeta {
                val esAdaptativo = ajustes.colorIconosInternos.isBlank()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Color adaptativo del tema", color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                    BotonBorde(
                        texto = if (esAdaptativo) "Activo" else "Restablecer",
                        modifier = Modifier.width(130.dp)
                    ) {
                        haptica.tic()
                        vm.ajustarColorIconosInternos("")
                    }
                }
                Spacer(Modifier.height(8.dp))
                SelectorColorEnTiempoReal(
                    colorInicial = ColorIconosInternos,
                    titulo = "Íconos"
                ) { nuevoColor ->
                    vm.ajustarColorIconosInternos(nuevoColor.aHex())
                }
            }
        }

        // 4. Sección: Color de Títulos y Cabeceras
        ContenedorSeccion(
            titulo = "Color de títulos y cabeceras",
            subtitulo = "Color destacado para las categorías de la lista y títulos de sección.",
            icono = Icons.Filled.Key
        ) {
            ContenedorTarjeta {
                val esAdaptativo = ajustes.colorTitulos.isBlank()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Color adaptativo del tema", color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                    BotonBorde(
                        texto = if (esAdaptativo) "Activo" else "Restablecer",
                        modifier = Modifier.width(130.dp)
                    ) {
                        haptica.tic()
                        vm.ajustarColorTitulos("")
                    }
                }
                Spacer(Modifier.height(8.dp))
                SelectorColorEnTiempoReal(
                    colorInicial = ColorTitulos,
                    titulo = "Títulos"
                ) { nuevoColor ->
                    vm.ajustarColorTitulos(nuevoColor.aHex())
                }
            }
        }

        // 5. Sección: Color de Tarjetas
        ContenedorSeccion(
            titulo = "Color de tarjetas y filas",
            subtitulo = "Personaliza el fondo de las tarjetas de contenido.",
            icono = Icons.Filled.Palette
        ) {
            ContenedorTarjeta {
                val esAdaptativo = ajustes.colorTarjetas.isBlank()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Fondo adaptativo del tema", color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                    BotonBorde(
                        texto = if (esAdaptativo) "Activo" else "Restablecer",
                        modifier = Modifier.width(130.dp)
                    ) {
                        haptica.tic()
                        vm.ajustarColorTarjetas("")
                    }
                }
                Spacer(Modifier.height(8.dp))
                SelectorColorEnTiempoReal(
                    colorInicial = ColorTarjetas,
                    titulo = "Tarjetas"
                ) { nuevoColor ->
                    vm.ajustarColorTarjetas(nuevoColor.aHex())
                }
            }
        }

        // 6. Sección: Colores de Secciones Funcionales
        ContenedorSeccion(
            titulo = "Colores por Sección Funcional",
            subtitulo = "Personaliza el color semántico de cada funcionalidad clave de la app.",
            icono = Icons.Filled.ColorLens
        ) {
            ContenedorTarjeta {
                val secciones = listOf(
                    "Seguridad" to (ColorSeguridad to { c: Color -> vm.ajustarColorSeguridad(c.aHex()) }),
                    "2FA / OTP" to (Color2FA to { c: Color -> vm.ajustarColor2FA(c.aHex()) }),
                    "Passkeys" to (ColorPasskeys to { c: Color -> vm.ajustarColorPasskeys(c.aHex()) }),
                    "Generador" to (ColorGenerador to { c: Color -> vm.ajustarColorGenerador(c.aHex()) }),
                    "Salud Bóveda" to (ColorSalud to { c: Color -> vm.ajustarColorSalud(c.aHex()) }),
                    "Papelera" to (ColorPapelera to { c: Color -> vm.ajustarColorPapelera(c.aHex()) }),
                    "Exportación" to (ColorExportacion to { c: Color -> vm.ajustarColorExportacion(c.aHex()) })
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    secciones.forEachIndexed { i, (nombre, par) ->
                        val (col, _) = par
                        val elegida = seccionElegida == i
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(CurvaturaEsquinas))
                                .background(if (elegida) col.copy(alpha = 0.25f) else ColorTarjetas)
                                .clickable { seccionElegida = i }
                                .padding(horizontal = 12.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = nombre,
                                color = if (elegida) col else TextoSecundario,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (elegida) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                val (nombreSeccion, parActual) = secciones[seccionElegida]
                val (colorActual, mutador) = parActual
                SelectorColorEnTiempoReal(
                    colorInicial = colorActual,
                    titulo = nombreSeccion
                ) { nuevoColor ->
                    mutador(nuevoColor)
                }
            }
        }

        // 7. Sección: Ícono del Launcher (Fossify)
        ContenedorSeccion(
            titulo = "Ícono de la app en el Launcher",
            subtitulo = "Android exige cambiar el icono del launcher mediante variantes del sistema. Al seleccionarlo, la app se reiniciará brevemente.",
            icono = Icons.Filled.Palette
        ) {
            ContenedorTarjeta {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PaletaAcento.entries.forEach { paleta ->
                        val seleccionado = ajustes.iconoLauncher == paleta.clave
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(paleta.base)
                                .border(
                                    width = if (seleccionado) 3.dp else 1.dp,
                                    color = if (seleccionado) TextoPrincipal else Borde,
                                    shape = CircleShape
                                )
                                .clickable {
                                    if (ajustes.iconoLauncher != paleta.clave) {
                                        haptica.tic()
                                        dialogoConfirmarIcono = paleta
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (seleccionado) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = paleta.etiqueta,
                                    tint = colorContraste(paleta.base),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 8. Botones de acción normalizados
        Spacer(Modifier.height(8.dp))
        BotonBorde(
            texto = "Restablecer todos los colores",
            color = ColorTitulos
        ) {
            haptica.tic()
            vm.restablecerColoresTema()
        }

        BotonColorido(
            texto = "Listo",
            color = ColorAcento
        ) {
            haptica.exito()
            vm.volverAtras()
        }

        Spacer(Modifier.height(24.dp))
    }

    dialogoConfirmarIcono?.let { paleta ->
        AlertDialog(
            onDismissRequest = { dialogoConfirmarIcono = null },
            containerColor = SuperficieAlta,
            title = { Text("Cambiar icono a \"${paleta.etiqueta}\"", color = TextoPrincipal) },
            text = {
                Text(
                    "Android cerrará la aplicación por un instante para que el launcher actualice su icono. Ya quedará guardado antes de cerrarse.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val elegido = paleta
                    dialogoConfirmarIcono = null
                    vm.ajustarIconoLauncher(elegido.clave)
                    actividad?.finishAffinity()
                }) { Text("Cambiar y cerrar", color = ColorAcento) }
            },
            dismissButton = {
                TextButton(onClick = { dialogoConfirmarIcono = null }) { Text("Cancelar", color = TextoSecundario) }
            }
        )
    }
}
