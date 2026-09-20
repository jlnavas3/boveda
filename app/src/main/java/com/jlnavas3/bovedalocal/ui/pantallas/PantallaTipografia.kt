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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatLineSpacing
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.FilaAjuste
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
import com.jlnavas3.bovedalocal.ui.theme.EspaciadoComponentes
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.FormaBoton
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun PantallaTipografia(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()

    val reqPreview = remember { BringIntoViewRequester() }
    val reqPresets = remember { BringIntoViewRequester() }
    val reqEscala = remember { BringIntoViewRequester() }
    val reqFamilia = remember { BringIntoViewRequester() }
    val reqPeso = remember { BringIntoViewRequester() }
    val reqEspaciado = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null) {
            when {
                seccionDestino == "09.5.1" -> reqPreview.bringIntoView()
                seccionDestino == "09.5.2" -> reqPresets.bringIntoView()
                seccionDestino == "09.5.3" -> reqEscala.bringIntoView()
                seccionDestino == "09.5.4" -> reqFamilia.bringIntoView()
                seccionDestino == "09.5.5" -> reqPeso.bringIntoView()
                seccionDestino == "09.5.6" -> reqEspaciado.bringIntoView()
                seccionDestino.startsWith("09.5.") && seccionDestino != "09.5" -> reqPresets.bringIntoView()
            }
        }
    }

    ContenedorPrincipal(
        titulo = "Tipografía y Textos",
        subtitulo = "Personaliza fuentes, escalas y pesos en tiempo real",
        alVolver = { vm.volverAtras() },
        conScroll = true,
        espaciado = EspaciadoComponentes
    ) {
        // 1. Tarjeta de vista previa en tiempo real
        TarjetaBovedaDesplegable(
            titulo = "Vista previa",
            descripcion = "Observa en vivo el tamaño, peso, familia y espaciado de las fuentes",
            icono = Icons.Filled.TextFields,
            colorIcono = ColorSalud,
            inicialmenteAbierta = seccionDestino == "09.5.1",
            idEtiqueta = "09.5.1",
            mostrarId = ajustes.mostrarIdsAjustes,
            modifier = Modifier.bringIntoViewRequester(reqPreview)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Banco Nacional",
                    color = ColorTitulos,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Box(
                    modifier = Modifier
                        .clip(FormaPequena)
                        .background(Menta.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "Seguridad Alta",
                        color = Menta,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
            Text(
                text = "usuario.principal@correo.com • Modificado hace 2 días",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(6.dp))
            // Caja monoespaciada con contraseña
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FormaBoton)
                    .background(Obsidiana)
                    .then(
                        if (GrosorBorde > 0.dp) {
                            Modifier.border(GrosorBorde, ColorBordeActual, FormaBoton)
                        } else {
                            Modifier
                        }
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "k9#mP$2vL@xQ!8zW",
                        color = ColorAcento,
                        style = EstiloMonoGrande.copy(fontWeight = FontWeight.Bold)
                    )
                    Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = "Copiar",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    BotonAmbar(
                        texto = "Acción Principal",
                        icono = Icons.Filled.Key
                    ) { haptica.tic() }
                }
                Box(modifier = Modifier.weight(1f)) {
                    BotonBorde(
                        texto = "Detalles",
                        icono = Icons.Filled.Info
                    ) { haptica.tic() }
                }
            }
        }

        // 2. Presets rápidos de tipografía
        TarjetaBovedaDesplegable(
            titulo = "Estilos predefinidos",
            descripcion = "Combinaciones optimizadas para lectura, terminales o accesibilidad",
            icono = Icons.Filled.AutoAwesome,
            colorIcono = ColorAcento,
            inicialmenteAbierta = seccionDestino == "09.5.2",
            idEtiqueta = "09.5.2",
            mostrarId = ajustes.mostrarIdsAjustes,
            modifier = Modifier.bringIntoViewRequester(reqPresets)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipPresetTipografia(
                    etiqueta = "Equilibrado",
                    descripcion = "Sans 1.0x",
                    activo = ajustes.escalaTexto == 1.0f && ajustes.familiaFuente == "sans" && ajustes.pesoTexto == "normal" && !ajustes.cursivaTexto
                ) {
                    haptica.tic()
                    vm.aplicarPresetTipografia(
                        escala = 1.0f,
                        peso = "normal",
                        cursiva = false,
                        kerning = 0.0f,
                        interlineado = 1.0f,
                        familia = "sans"
                    )
                }
                ChipPresetTipografia(
                    etiqueta = "Terminal Mono",
                    descripcion = "Hacker 0.95x",
                    activo = ajustes.familiaFuente == "mono"
                ) {
                    haptica.tic()
                    vm.aplicarPresetTipografia(
                        escala = 0.95f,
                        peso = "medio",
                        cursiva = false,
                        kerning = 0.5f,
                        interlineado = 1.05f,
                        familia = "mono"
                    )
                }
                ChipPresetTipografia(
                    etiqueta = "Editorial",
                    descripcion = "Serif 1.05x",
                    activo = ajustes.familiaFuente == "serif"
                ) {
                    haptica.tic()
                    vm.aplicarPresetTipografia(
                        escala = 1.05f,
                        peso = "normal",
                        cursiva = false,
                        kerning = 0.0f,
                        interlineado = 1.20f,
                        familia = "serif"
                    )
                }
                ChipPresetTipografia(
                    etiqueta = "Accesibilidad",
                    descripcion = "Grande 1.25x",
                    activo = ajustes.escalaTexto >= 1.20f && ajustes.pesoTexto == "seminegrita"
                ) {
                    haptica.tic()
                    vm.aplicarPresetTipografia(
                        escala = 1.25f,
                        peso = "seminegrita",
                        cursiva = false,
                        kerning = 0.2f,
                        interlineado = 1.25f,
                        familia = "sans"
                    )
                }
                ChipPresetTipografia(
                    etiqueta = "Compacto",
                    descripcion = "Denso 0.85x",
                    activo = ajustes.escalaTexto <= 0.88f
                ) {
                    haptica.tic()
                    vm.aplicarPresetTipografia(
                        escala = 0.85f,
                        peso = "fino",
                        cursiva = false,
                        kerning = -0.2f,
                        interlineado = 0.95f,
                        familia = "sans"
                    )
                }
            }
        }

        // 3. Slider de Escala de Texto
        TarjetaBovedaDesplegable(
            titulo = "Tamaño de fuente (Escala)",
            descripcion = "Agranda o reduce todos los textos de la app manteniendo proporciones",
            icono = Icons.Filled.FormatSize,
            colorIcono = ColorGenerador,
            inicialmenteAbierta = seccionDestino == "09.5.3",
            idEtiqueta = "09.5.3",
            mostrarId = ajustes.mostrarIdsAjustes,
            modifier = Modifier.bringIntoViewRequester(reqEscala)
        ) {
            val porcentaje = ((ajustes.escalaTexto - 1.0f) * 100).roundToInt()
            val textoPorcentaje = if (porcentaje == 0) "1.00x (Normal)" else if (porcentaje > 0) "${String.format("%.2f", ajustes.escalaTexto)}x (+$porcentaje%)" else "${String.format("%.2f", ajustes.escalaTexto)}x ($porcentaje%)"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Escalado global", color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
                IndicadorValor(textoPorcentaje)
            }
            Slider(
                value = ajustes.escalaTexto,
                onValueChange = { vm.ajustarEscalaTexto(it) },
                valueRange = 0.80f..1.35f,
                steps = 10,
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
                Text("0.80x (-20%)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Text("1.00x (100%)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Text("1.35x (+35%)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            }
            ComponenteBotonFila(
                titulo = "Restablecer",
                alPulsar = {
                    vm.ajustarEscalaTexto(1.0f)
                }
            )
        }

        // 4. Selector de Familia Tipográfica
        TarjetaBovedaDesplegable(
            titulo = "Familia tipográfica",
            descripcion = "Selecciona el estilo de fuente principal para la interfaz y lecturas",
            icono = Icons.Filled.TextFields,
            colorIcono = ColorPasskeys,
            inicialmenteAbierta = seccionDestino == "09.5.4",
            idEtiqueta = "09.5.4",
            mostrarId = ajustes.mostrarIdsAjustes,
            modifier = Modifier.bringIntoViewRequester(reqFamilia)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AlmacenAjustes.OPCIONES_FAMILIA_FUENTE.forEach { (clave, etiqueta) ->
                    val seleccionado = ajustes.familiaFuente == clave
                    Box(
                        modifier = Modifier
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
                                vm.ajustarFamiliaFuente(clave)
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
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
            ComponenteBotonFila(
                titulo = "Restablecer",
                alPulsar = {
                    vm.ajustarFamiliaFuente("sans")
                }
            )
        }

        // 5. Selector de Grosor y Estilo
        TarjetaBovedaDesplegable(
            titulo = "Grosor y estilo de texto",
            descripcion = "Densidad de trazos y estilo itálico",
            icono = Icons.Filled.FormatBold,
            colorIcono = ColorSeguridad,
            inicialmenteAbierta = seccionDestino == "09.5.5",
            idEtiqueta = "09.5.5",
            mostrarId = ajustes.mostrarIdsAjustes,
            modifier = Modifier.bringIntoViewRequester(reqPeso)
        ) {
            Text(
                text = "Grosor / Peso de la fuente",
                color = TextoSecundario,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AlmacenAjustes.OPCIONES_PESO_TEXTO.forEach { (clave, etiqueta) ->
                    val seleccionado = ajustes.pesoTexto == clave
                    Box(
                        modifier = Modifier
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
                                vm.ajustarPesoTexto(clave)
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
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

            Spacer(Modifier.height(14.dp))

            FilaAjuste(
                titulo = "Texto en cursiva",
                descripcion = "Añade inclinación estética a títulos, descripciones y etiquetas.",
                activo = ajustes.cursivaTexto,
                alCambiar = {
                    haptica.tic()
                    vm.ajustarCursivaTexto(it)
                }
            )
            ComponenteBotonFila(
                titulo = "Restablecer",
                alPulsar = {
                    vm.ajustarPesoTexto("normal")
                    vm.ajustarCursivaTexto(false)
                }
            )
        }

        // 6. Espaciado e Interlineado
        TarjetaBovedaDesplegable(
            titulo = "Espaciado e interlineado",
            descripcion = "Separación entre letras y altura de línea",
            icono = Icons.Filled.FormatLineSpacing,
            colorIcono = ColorAcento,
            inicialmenteAbierta = seccionDestino == "09.5.6",
            idEtiqueta = "09.5.6",
            mostrarId = ajustes.mostrarIdsAjustes,
            modifier = Modifier.bringIntoViewRequester(reqEspaciado)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Espaciado horizontal (Kerning)", color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
                IndicadorValor(if (ajustes.espaciadoLetrasSp == 0f) "0.0 sp (Normal)" else "${String.format("%+.1f", ajustes.espaciadoLetrasSp)} sp")
            }
            Slider(
                value = ajustes.espaciadoLetrasSp,
                onValueChange = { vm.ajustarEspaciadoLetras(it) },
                valueRange = -0.5f..2.0f,
                steps = 24,
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
                Text("-0.5 sp (Condensado)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Text("0.0 sp (Normal)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Text("+2.0 sp (Separado)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Factor de interlineado", color = TextoPrincipal, style = MaterialTheme.typography.bodyLarge)
                IndicadorValor("${String.format("%.2f", ajustes.interlineadoFactor)}x")
            }
            Slider(
                value = ajustes.interlineadoFactor,
                onValueChange = { vm.ajustarInterlineadoFactor(it) },
                valueRange = 0.85f..1.40f,
                steps = 10,
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
                Text("0.85x (Compacto)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Text("1.00x (Normal)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Text("1.40x (Amplio)", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
            }
            ComponenteBotonFila(
                titulo = "Restablecer",
                alPulsar = {
                    vm.ajustarEspaciadoLetras(0.0f)
                    vm.ajustarInterlineadoFactor(1.0f)
                }
            )
        }

        // 7. Botón de restauración
        ComponenteGrupo {
            ComponenteBotonFila(
                titulo = "Restablecer módulo",
                alPulsar = {
                    vm.restablecerTipografia()
                }
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ChipPresetTipografia(
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
