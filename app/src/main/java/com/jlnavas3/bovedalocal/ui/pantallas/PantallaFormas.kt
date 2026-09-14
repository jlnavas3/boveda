package com.jlnavas3.bovedalocal.ui.pantallas

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.FormatShapes
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.data.AlmacenAjustes
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorPasskeys
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla

@Composable
fun PantallaFormas(vm: VaultViewModel) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    var textoPrueba by remember { mutableStateOf("Texto de prueba") }

    ContenedorPrincipal(conScroll = true, espaciado = EspaciadoComponentes) {
        CabeceraPantalla(
            titulo = "Bordes y Formas",
            subtitulo = "Personaliza curvaturas, trazos y espaciados en tiempo real",
            alVolver = { vm.volverAtras() }
        )

        // 1. Tarjeta de vista previa interactiva en tiempo real
        TarjetaBovedaDesplegable(
            titulo = "Vista previa en tiempo real",
            descripcion = "Observa en vivo las esquinas, trazos y espaciados de los componentes",
            icono = Icons.Filled.FormatShapes,
            colorIcono = ColorSalud,
            inicialmenteAbierta = true
        ) {
            Text(
                text = "Tarjeta Interactiva",
                color = ColorTitulos,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "La curvatura actual es de ${ajustes.curvaturaEsquinasDp.roundToInt()} dp y el borde tiene un trazo de ${String.format("%.1f", ajustes.grosorBordeDp)} dp.",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(4.dp))
            CampoBoveda(
                valor = textoPrueba,
                etiqueta = "Campo de entrada con borde dinámico",
                alCambiar = { textoPrueba = it }
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    BotonAmbar(
                        texto = "Botón Principal",
                        icono = Icons.Filled.Star
                    ) { haptica.tic() }
                }
                Box(modifier = Modifier.weight(1f)) {
                    BotonBorde(
                        texto = "Secundario",
                        icono = Icons.Filled.Tune
                    ) { haptica.tic() }
                }
            }
        }

        // 2. Presets rápidos de diseño
        TarjetaBovedaDesplegable(
            titulo = "Estilos predefinidos",
            descripcion = "Aplica combinaciones armónicas de esquinas y trazos en un solo toque",
            icono = Icons.Filled.AutoAwesome,
            colorIcono = ColorAcento,
            inicialmenteAbierta = true
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipPresetForma(
                    etiqueta = "Predeterminado",
                    descripcion = "6 dp / 0.8 dp",
                    activo = ajustes.curvaturaEsquinasDp == 6f && ajustes.grosorBordeDp == 0.8f && ajustes.estiloBorde == "marcado"
                ) {
                    haptica.tic()
                    vm.aplicarPresetFormas(curvatura = 6f, grosor = 0.8f, estilo = "marcado", espaciado = 14f)
                }
                ChipPresetForma(
                    etiqueta = "Redondeado",
                    descripcion = "18 dp / 1 dp",
                    activo = ajustes.curvaturaEsquinasDp == 18f && ajustes.grosorBordeDp == 1f && ajustes.estiloBorde == "sutil"
                ) {
                    haptica.tic()
                    vm.aplicarPresetFormas(curvatura = 18f, grosor = 1f, estilo = "sutil", espaciado = 14f)
                }
                ChipPresetForma(
                    etiqueta = "Neobrutalista",
                    descripcion = "0 dp / 2.5 dp",
                    activo = ajustes.curvaturaEsquinasDp == 0f && ajustes.grosorBordeDp == 2.5f && ajustes.estiloBorde == "marcado"
                ) {
                    haptica.tic()
                    vm.aplicarPresetFormas(curvatura = 0f, grosor = 2.5f, estilo = "marcado", espaciado = 16f)
                }
                ChipPresetForma(
                    etiqueta = "Píldora M3",
                    descripcion = "28 dp / 1 dp",
                    activo = ajustes.curvaturaEsquinasDp == 28f && ajustes.grosorBordeDp == 1f
                ) {
                    haptica.tic()
                    vm.aplicarPresetFormas(curvatura = 28f, grosor = 1f, estilo = "sutil", espaciado = 16f)
                }
                ChipPresetForma(
                    etiqueta = "Compacto",
                    descripcion = "8 dp / 1.5 dp",
                    activo = ajustes.curvaturaEsquinasDp == 8f && ajustes.espaciadoComponentesDp == 8f
                ) {
                    haptica.tic()
                    vm.aplicarPresetFormas(curvatura = 8f, grosor = 1.5f, estilo = "sutil", espaciado = 8f)
                }
                ChipPresetForma(
                    etiqueta = "Sin bordes",
                    descripcion = "16 dp / 0 dp",
                    activo = ajustes.grosorBordeDp == 0f || ajustes.estiloBorde == "ninguno"
                ) {
                    haptica.tic()
                    vm.aplicarPresetFormas(curvatura = 16f, grosor = 0f, estilo = "ninguno", espaciado = 14f)
                }
            }
        }

        // 3. Slider de Curvatura de Esquinas
        TarjetaBovedaDesplegable(
            titulo = "Curvatura de esquinas",
            descripcion = "Define qué tan redondeados son las tarjetas, botones y campos",
            icono = Icons.Filled.CropSquare,
            colorIcono = ColorGenerador,
            inicialmenteAbierta = false
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Radio de esquinas", color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
                IndicadorValor("${ajustes.curvaturaEsquinasDp.roundToInt()} dp")
            }
            Slider(
                value = ajustes.curvaturaEsquinasDp,
                onValueChange = { vm.ajustarCurvaturaEsquinas(it) },
                valueRange = 0f..32f,
                steps = 31,
                colors = SliderDefaults.colors(
                    thumbColor = ColorAcento,
                    activeTrackColor = ColorAcento,
                    inactiveTrackColor = ColorBordeActual.copy(alpha = 0.3f)
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("0 dp (Recto)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Text("18 dp (Estándar)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Text("32 dp (Píldora)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            }
        }

        // 4. Slider de Grosor de Bordes
        TarjetaBovedaDesplegable(
            titulo = "Grosor del borde",
            descripcion = "Controla el ancho de trazo perimetral en tarjetas y controles",
            icono = Icons.Filled.LineWeight,
            colorIcono = ColorPasskeys,
            inicialmenteAbierta = false
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ancho de trazo", color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
                IndicadorValor(if (ajustes.grosorBordeDp == 0f) "Sin borde" else "${String.format("%.1f", ajustes.grosorBordeDp)} dp")
            }
            Slider(
                value = ajustes.grosorBordeDp,
                onValueChange = { vm.ajustarGrosorBorde(it) },
                valueRange = 0f..4f,
                steps = 15,
                colors = SliderDefaults.colors(
                    thumbColor = ColorAcento,
                    activeTrackColor = ColorAcento,
                    inactiveTrackColor = ColorBordeActual.copy(alpha = 0.3f)
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("0 dp (Plano)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Text("1 dp (Fino)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Text("4 dp (Grueso)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            }
        }

        // 5. Selector de Estilo de Borde
        TarjetaBovedaDesplegable(
            titulo = "Tono y estilo del borde",
            descripcion = "Elige el matiz cromático con el que se dibujarán las líneas de contorno",
            icono = Icons.Filled.Tune,
            colorIcono = ColorSeguridad,
            inicialmenteAbierta = false
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AlmacenAjustes.OPCIONES_ESTILO_BORDE.forEach { (clave, etiqueta) ->
                    val seleccionado = ajustes.estiloBorde == clave
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(FormaBoton)
                            .background(if (seleccionado) ColorAcento else SuperficieAlta)
                            .then(
                                if (!seleccionado && GrosorBorde > 0.dp) {
                                    Modifier.border(GrosorBorde, ColorBordeActual, FormaBoton)
                                } else {
                                    Modifier
                                }
                            )
                            .clickable {
                                haptica.tic()
                                vm.ajustarEstiloBorde(clave)
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = etiqueta,
                            color = if (seleccionado) ColorSobreAcento else TextoPrincipal,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }

        // 6. Slider de Espaciado entre Componentes
        TarjetaBovedaDesplegable(
            titulo = "Espaciado y separación",
            descripcion = "Ajusta la separación vertical entre secciones, tarjetas y bloques",
            icono = Icons.Filled.SpaceDashboard,
            colorIcono = ColorAcento,
            inicialmenteAbierta = false
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Separación vertical", color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
                IndicadorValor("${ajustes.espaciadoComponentesDp.roundToInt()} dp")
            }
            Slider(
                value = ajustes.espaciadoComponentesDp,
                onValueChange = { vm.ajustarEspaciadoComponentes(it) },
                valueRange = 6f..24f,
                steps = 17,
                colors = SliderDefaults.colors(
                    thumbColor = ColorAcento,
                    activeTrackColor = ColorAcento,
                    inactiveTrackColor = ColorBordeActual.copy(alpha = 0.3f)
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("6 dp (Compacto)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Text("14 dp (Equilibrado)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Text("24 dp (Amplio)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            }
        }

        // 7. Botón de restauración
        BotonBorde(
            texto = "Restablecer bordes y formas predeterminados",
            icono = Icons.Filled.Refresh
        ) {
            haptica.toque()
            vm.restablecerFormas()
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ChipPresetForma(
    etiqueta: String,
    descripcion: String,
    activo: Boolean,
    alPulsar: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(FormaBoton)
            .background(if (activo) ColorAcento else SuperficieAlta)
            .then(
                if (!activo && GrosorBorde > 0.dp) {
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaBoton)
                } else {
                    Modifier
                }
            )
            .clickable { alPulsar() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = etiqueta,
                color = if (activo) ColorSobreAcento else TextoPrincipal,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = descripcion,
                color = if (activo) ColorSobreAcento.copy(alpha = 0.8f) else TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
            )
        }
    }
}

@Composable
private fun IndicadorValor(texto: String) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(ColorAcento.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = texto,
            color = ColorAcento,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}
