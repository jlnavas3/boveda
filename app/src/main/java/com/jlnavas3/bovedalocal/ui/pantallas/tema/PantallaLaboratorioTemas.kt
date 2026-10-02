package com.jlnavas3.bovedalocal.ui.pantallas.tema

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.BotonPeligro
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SwitchBoveda
import com.jlnavas3.bovedalocal.ui.componentes.colorAhsv
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.SliderBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.FilaAjusteMenu
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaAjuste
import com.jlnavas3.bovedalocal.ui.preview.BovedaPantallaPreview
import com.jlnavas3.bovedalocal.ui.preview.PreviewMocks
import com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobria
import com.jlnavas3.bovedalocal.ui.theme.PaletaSobriaDefaults
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.ui.theme.aHexConAlfa
import com.jlnavas3.bovedalocal.ui.theme.colorContraste
import com.jlnavas3.bovedalocal.ui.theme.crearGris
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.ui.theme.paletaSobriaEnVivo
import com.jlnavas3.bovedalocal.ui.theme.restringirLuminancia
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Laboratorio de Temas y Paleta Sobria:
 * Herramienta visual interactiva para ajustar la escala neutra de grises y el acento esencial,
 * aplicando la filosofía 90% neutro + 10% acento con restricción inteligente de luminancia.
 */
@Composable
fun PantallaLaboratorioTemas(
    vm: VaultViewModel? = null,
    seccionDestino: String? = null,
    alVolver: () -> Unit = { vm?.volverAtras() }
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val portapapeles = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    var modoOscuro by remember { mutableStateOf(esOscuroActivo) }

    // Valores iniciales según el modo
    val defaults = if (modoOscuro) PaletaSobriaDefaults.OSCURA else PaletaSobriaDefaults.CLARA

    var lumFondo by remember(modoOscuro) { mutableFloatStateOf(if (modoOscuro) 0.07f else 0.96f) }
    var lumTarjeta by remember(modoOscuro) { mutableFloatStateOf(if (modoOscuro) 0.11f else 1.00f) }
    var lumCampo by remember(modoOscuro) { mutableFloatStateOf(if (modoOscuro) 0.16f else 0.93f) }
    var lumBorde by remember(modoOscuro) { mutableFloatStateOf(if (modoOscuro) 0.22f else 0.85f) }
    var lumTextoPrincipal by remember(modoOscuro) { mutableFloatStateOf(if (modoOscuro) 0.95f else 0.08f) }
    var lumTextoSecundario by remember(modoOscuro) { mutableFloatStateOf(if (modoOscuro) 0.62f else 0.40f) }
    var colorAcentoActual by remember(modoOscuro) { mutableStateOf(defaults.acento) }

    // Control de Tono Unificado vs Individual
    var unificarTonos by remember { mutableStateOf(true) }
    var tonoGlobal by remember { mutableFloatStateOf(215f) } // 215° = Pizarra / Slate
    var saturacionTinte by remember { mutableFloatStateOf(0.08f) } // 8% tinte ergonómico

    var tonoFondo by remember { mutableFloatStateOf(215f) }
    var tonoTarjeta by remember { mutableFloatStateOf(215f) }
    var tonoCampo by remember { mutableFloatStateOf(215f) }
    var tonoBorde by remember { mutableFloatStateOf(215f) }
    var tonoTextoPrincipal by remember { mutableFloatStateOf(215f) }
    var tonoTextoSecundario by remember { mutableFloatStateOf(215f) }

    val tonoEfectivoFondo = if (unificarTonos) tonoGlobal else tonoFondo
    val tonoEfectivoTarjeta = if (unificarTonos) tonoGlobal else tonoTarjeta
    val tonoEfectivoCampo = if (unificarTonos) tonoGlobal else tonoCampo
    val tonoEfectivoBorde = if (unificarTonos) tonoGlobal else tonoBorde
    val tonoEfectivoTextoPrincipal = if (unificarTonos) tonoGlobal else tonoTextoPrincipal
    val tonoEfectivoTextoSecundario = if (unificarTonos) tonoGlobal else tonoTextoSecundario

    fun calcularColorCapa(tono: Float, lum: Float, esSuperficie: Boolean): Color {
        val lumRestringida = restringirLuminancia(lum, modoOscuro, esSuperficie)
        return if (saturacionTinte <= 0.001f) {
            crearGris(lumRestringida)
        } else {
            Color.hsv(tono, saturacionTinte.coerceIn(0f, 1f), lumRestringida)
        }
    }

    // Colores calculados reactivamente
    val colorFondo = remember(lumFondo, tonoEfectivoFondo, saturacionTinte, modoOscuro) {
        calcularColorCapa(tonoEfectivoFondo, lumFondo, esSuperficie = true)
    }
    val colorTarjeta = remember(lumTarjeta, tonoEfectivoTarjeta, saturacionTinte, modoOscuro) {
        calcularColorCapa(tonoEfectivoTarjeta, lumTarjeta, esSuperficie = true)
    }
    val colorCampo = remember(lumCampo, tonoEfectivoCampo, saturacionTinte, modoOscuro) {
        calcularColorCapa(tonoEfectivoCampo, lumCampo, esSuperficie = true)
    }
    val colorBorde = remember(lumBorde, tonoEfectivoBorde, saturacionTinte, modoOscuro) {
        calcularColorCapa(tonoEfectivoBorde, lumBorde, esSuperficie = true)
    }
    val colorTextoPrincipal = remember(lumTextoPrincipal, tonoEfectivoTextoPrincipal, saturacionTinte, modoOscuro) {
        calcularColorCapa(tonoEfectivoTextoPrincipal, lumTextoPrincipal, esSuperficie = false)
    }
    val colorTextoSecundario = remember(lumTextoSecundario, tonoEfectivoTextoSecundario, saturacionTinte, modoOscuro) {
        calcularColorCapa(tonoEfectivoTextoSecundario, lumTextoSecundario, esSuperficie = false)
    }

    val paletaActual = remember(
        modoOscuro, colorFondo, colorTarjeta, colorCampo,
        colorBorde, colorTextoPrincipal, colorTextoSecundario, colorAcentoActual
    ) {
        PaletaSobria(
            esOscuro = modoOscuro,
            fondo = colorFondo,
            tarjeta = colorTarjeta,
            campo = colorCampo,
            borde = colorBorde,
            textoPrincipal = colorTextoPrincipal,
            textoSecundario = colorTextoSecundario,
            acento = colorAcentoActual
        )
    }

    // Inyectar en vivo para que los componentes y la pantalla se actualicen en tiempo real
    LaunchedEffect(paletaActual) {
        paletaSobriaEnVivo = paletaActual
    }

    // Limpiar al salir de la pantalla si se desea o conservar
    DisposableEffect(Unit) {
        onDispose {
            // Se puede limpiar o mantener; mantener permite ver el resultado en toda la app mientras esté abierta
        }
    }

    val acentosPredefinidos = remember {
        listOf(
            Color(0xFFE5A93C) to "Ámbar Bóveda",
            Color(0xFFD4AF37) to "Oro Clásico",
            Color(0xFFC87D55) to "Bronce Cálido",
            Color(0xFF3B82F6) to "Zafiro Seguridad",
            Color(0xFF10B981) to "Esmeralda",
            Color(0xFF8B5CF6) to "Amatista"
        )
    }

    val acentosGrisesApple = remember(modoOscuro) {
        if (modoOscuro) {
            listOf(
                Color(0xFFE1E1E6) to "Titanio Platino",
                Color(0xFFB0B0B8) to "Gris Espacial",
                Color(0xFF9FA4B2) to "Pizarra Fría",
                Color(0xFFB8B2AA) to "Piedra Cálida",
                Color(0xFFC8C8CE) to "Plata Niebla"
            )
        } else {
            listOf(
                Color(0xFF2C2C2E) to "Titanio Carbón",
                Color(0xFF3A3A3C) to "Gris Espacial",
                Color(0xFF323842) to "Pizarra Fría",
                Color(0xFF3E3A36) to "Piedra Cálida",
                Color(0xFF48484A) to "Plata Grafito"
            )
        }
    }

    // Estado para Personalizado (HSV + Transparencia / Alfa)
    val hsvInicial = remember(colorAcentoActual) { colorAhsv(colorAcentoActual) }
    var huePersonalizado by remember(modoOscuro) { mutableFloatStateOf(hsvInicial.first) }
    var satPersonalizado by remember(modoOscuro) { mutableFloatStateOf(hsvInicial.second) }
    var valPersonalizado by remember(modoOscuro) { mutableFloatStateOf(hsvInicial.third) }
    var alfaPersonalizado by remember(modoOscuro) { mutableFloatStateOf(colorAcentoActual.alpha) }
    var esPersonalizadoActivo by remember(modoOscuro) { mutableStateOf(false) }
    var mostrarAjustePersonalizado by remember(modoOscuro) { mutableStateOf(false) }

    fun copiarPaletaAlPortapapeles() {
        haptica.exito()
        val (hAcento, sAcento, vAcento) = colorAhsv(colorAcentoActual)
        val jsonDetallado = """
        {
          "modo": "${if (modoOscuro) "oscuro" else "claro"}",
          "fondo": "${colorFondo.aHexConAlfa()}",
          "tarjeta": "${colorTarjeta.aHexConAlfa()}",
          "campo": "${colorCampo.aHexConAlfa()}",
          "borde": "${colorBorde.aHexConAlfa()}",
          "textoPrincipal": "${colorTextoPrincipal.aHexConAlfa()}",
          "textoSecundario": "${colorTextoSecundario.aHexConAlfa()}",
          "acento": "${colorAcentoActual.aHexConAlfa()}",
          "parametrosLaboratorio": {
            "unificarTonos": $unificarTonos,
            "tonoGlobal": "${tonoGlobal.toInt()}°",
            "saturacionTinteGrises": "${(saturacionTinte * 100).toInt()}%",
            "capas": {
              "fondo": {
                "hex": "${colorFondo.aHexConAlfa()}",
                "luminancia": "${(lumFondo * 100).toInt()}%",
                "tono": "${tonoEfectivoFondo.toInt()}°"
              },
              "tarjeta": {
                "hex": "${colorTarjeta.aHexConAlfa()}",
                "luminancia": "${(lumTarjeta * 100).toInt()}%",
                "tono": "${tonoEfectivoTarjeta.toInt()}°"
              },
              "campo": {
                "hex": "${colorCampo.aHexConAlfa()}",
                "luminancia": "${(lumCampo * 100).toInt()}%",
                "tono": "${tonoEfectivoCampo.toInt()}°"
              },
              "borde": {
                "hex": "${colorBorde.aHexConAlfa()}",
                "luminancia": "${(lumBorde * 100).toInt()}%",
                "tono": "${tonoEfectivoBorde.toInt()}°"
              },
              "textoPrincipal": {
                "hex": "${colorTextoPrincipal.aHexConAlfa()}",
                "luminancia": "${(lumTextoPrincipal * 100).toInt()}%",
                "tono": "${tonoEfectivoTextoPrincipal.toInt()}°"
              },
              "textoSecundario": {
                "hex": "${colorTextoSecundario.aHexConAlfa()}",
                "luminancia": "${(lumTextoSecundario * 100).toInt()}%",
                "tono": "${tonoEfectivoTextoSecundario.toInt()}°"
              }
            },
            "acento": {
              "hex": "${colorAcentoActual.aHexConAlfa()}",
              "tono": "${hAcento.toInt()}°",
              "saturacion": "${(sAcento * 100).toInt()}%",
              "brillo": "${(vAcento * 100).toInt()}%",
              "opacidad": "${(colorAcentoActual.alpha * 100).toInt()}%",
              "canalAlfa": ${colorAcentoActual.alpha}
            }
          }
        }
        """.trimIndent()
        portapapeles.setText(AnnotatedString(jsonDetallado))
        Toast.makeText(contexto, "¡Paleta y parámetros copiados! Pégala en el chat.", Toast.LENGTH_LONG).show()
    }

    fun restablecerValores() {
        haptica.toque()
        val d = if (modoOscuro) PaletaSobriaDefaults.OSCURA else PaletaSobriaDefaults.CLARA
        lumFondo = if (modoOscuro) 0.07f else 0.96f
        lumTarjeta = if (modoOscuro) 0.11f else 1.00f
        lumCampo = if (modoOscuro) 0.16f else 0.93f
        lumBorde = if (modoOscuro) 0.22f else 0.85f
        lumTextoPrincipal = if (modoOscuro) 0.95f else 0.08f
        lumTextoSecundario = if (modoOscuro) 0.62f else 0.40f
        unificarTonos = true
        tonoGlobal = 215f
        saturacionTinte = 0.08f
        tonoFondo = 215f
        tonoTarjeta = 215f
        tonoCampo = 215f
        tonoBorde = 215f
        tonoTextoPrincipal = 215f
        tonoTextoSecundario = 215f
        colorAcentoActual = d.acento
        esPersonalizadoActivo = false
        mostrarAjustePersonalizado = false
        val (h, s, v) = colorAhsv(d.acento)
        huePersonalizado = h
        satPersonalizado = s
        valPersonalizado = v
        alfaPersonalizado = d.acento.alpha
        Toast.makeText(contexto, "Valores sobrios restablecidos", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Laboratorio de Temas",
            alVolver = alVolver,
            acciones = {
                IconButton(onClick = { copiarPaletaAlPortapapeles() }) {
                    Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = "Copiar paleta",
                        tint = colorAcentoActual
                    )
                }
            }
        )

        var switchMuestraActivo by remember { mutableStateOf(true) }

        // VISTA PREVIA FLOTANTE SUPERIOR (STICKY TOP)
        TarjetaFlotantePreviaLaboratorio(
            colorFondo = colorFondo,
            colorTarjeta = colorTarjeta,
            colorBorde = colorBorde,
            colorCampo = colorCampo,
            colorTextoPrincipal = colorTextoPrincipal,
            colorTextoSecundario = colorTextoSecundario,
            colorAcento = colorAcentoActual,
            switchActivo = switchMuestraActivo,
            onAlternarSwitch = {
                haptica.tic()
                switchMuestraActivo = it
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Selector de Modo (Oscuro / Claro)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(CurvaturaEsquinas))
                    .background(ColorTarjetaAjustes)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Píldora Oscuro
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                        .background(if (modoOscuro) colorAcentoActual else Color.Transparent)
                        .clickable {
                            haptica.tic()
                            modoOscuro = true
                        }
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.DarkMode,
                        contentDescription = null,
                        tint = if (modoOscuro) Color.Black else colorTextoSecundario,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Modo Oscuro",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (modoOscuro) FontWeight.Bold else FontWeight.Normal,
                            color = if (modoOscuro) Color.Black else colorTextoPrincipal
                        )
                    )
                }

                // Píldora Claro
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                        .background(if (!modoOscuro) colorAcentoActual else Color.Transparent)
                        .clickable {
                            haptica.tic()
                            modoOscuro = false
                        }
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.LightMode,
                        contentDescription = null,
                        tint = if (!modoOscuro) Color.Black else colorTextoSecundario,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Modo Claro",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (!modoOscuro) FontWeight.Bold else FontWeight.Normal,
                            color = if (!modoOscuro) Color.Black else colorTextoPrincipal
                        )
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            // SECCIÓN 1: Escala de Capas y Tonos (90% de la interfaz)
            ComponenteGrupo(
                etiqueta = "Escala de Capas y Tonos (90% Interfaz)",
                icono = Icons.Filled.Palette
            ) {
                // Control Maestro de Tono y Switch de Unificación
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Aplicar el mismo tono a todos",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = colorTextoPrincipal
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = if (unificarTonos) "Todas las capas comparten el mismo tono" else "Tono independiente por cada capa",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorTextoSecundario
                            )
                        }
                        SwitchBoveda(
                            checked = unificarTonos,
                            colorActivo = colorAcentoActual,
                            onCheckedChange = {
                                haptica.tic()
                                unificarTonos = it
                            }
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    if (unificarTonos) {
                        // Slider de Tono Unificado
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color.hsv(tonoGlobal, 0.8f, 0.9f))
                                    .border(1.dp, colorBorde, CircleShape)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Tono cromático unificado: ${tonoGlobal.toInt()}°",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = colorTextoPrincipal,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        SliderBoveda(
                            value = tonoGlobal,
                            onValueChange = {
                                tonoGlobal = it
                                if (unificarTonos) {
                                    val (_, s, v) = colorAhsv(colorAcentoActual)
                                    colorAcentoActual = Color.hsv(it, if (s < 0.05f) 0.75f else s, v, colorAcentoActual.alpha)
                                    huePersonalizado = it
                                }
                            },
                            valueRange = 0f..360f
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    // Slider de Saturación / Intensidad de tinte en grises
                    Text(
                        text = "Intensidad de tinte en grises: ${(saturacionTinte * 100).toInt()}% ${if (saturacionTinte <= 0.001f) "(Gris puro)" else if (saturacionTinte <= 0.12f) "(Matiz Apple)" else ""}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = colorTextoSecundario
                    )
                    SliderBoveda(
                        value = saturacionTinte,
                        onValueChange = { saturacionTinte = it },
                        valueRange = 0f..0.35f
                    )
                }

                SeparadorFilaAjuste()

                // Capa 0: Fondo de Pantalla
                ControlCapaFila(
                    etiqueta = "Fondo general (Capa 0)",
                    colorActual = colorFondo,
                    luminancia = lumFondo,
                    alCambiarLuminancia = { lumFondo = it },
                    mostrarControlTono = !unificarTonos,
                    tono = tonoFondo,
                    alCambiarTono = { tonoFondo = it }
                )
                SeparadorFilaAjuste()

                // Capa 1: Tarjetas y Grupos
                ControlCapaFila(
                    etiqueta = "Tarjetas y grupos (Capa 1)",
                    colorActual = colorTarjeta,
                    luminancia = lumTarjeta,
                    alCambiarLuminancia = { lumTarjeta = it },
                    mostrarControlTono = !unificarTonos,
                    tono = tonoTarjeta,
                    alCambiarTono = { tonoTarjeta = it }
                )
                SeparadorFilaAjuste()

                // Capa 2: Campos de entrada y chips
                ControlCapaFila(
                    etiqueta = "Campos y chips (Capa 2)",
                    colorActual = colorCampo,
                    luminancia = lumCampo,
                    alCambiarLuminancia = { lumCampo = it },
                    mostrarControlTono = !unificarTonos,
                    tono = tonoCampo,
                    alCambiarTono = { tonoCampo = it }
                )
                SeparadorFilaAjuste()

                // Bordes y separadores
                ControlCapaFila(
                    etiqueta = "Bordes y líneas divisorias",
                    colorActual = colorBorde,
                    luminancia = lumBorde,
                    alCambiarLuminancia = { lumBorde = it },
                    mostrarControlTono = !unificarTonos,
                    tono = tonoBorde,
                    alCambiarTono = { tonoBorde = it }
                )
                SeparadorFilaAjuste()

                // Texto Principal
                ControlCapaFila(
                    etiqueta = "Texto principal (Títulos y datos)",
                    colorActual = colorTextoPrincipal,
                    luminancia = lumTextoPrincipal,
                    alCambiarLuminancia = { lumTextoPrincipal = it },
                    mostrarControlTono = !unificarTonos,
                    tono = tonoTextoPrincipal,
                    alCambiarTono = { tonoTextoPrincipal = it }
                )
                SeparadorFilaAjuste()

                // Texto Secundario
                ControlCapaFila(
                    etiqueta = "Texto secundario (Subtítulos)",
                    colorActual = colorTextoSecundario,
                    luminancia = lumTextoSecundario,
                    alCambiarLuminancia = { lumTextoSecundario = it },
                    mostrarControlTono = !unificarTonos,
                    tono = tonoTextoSecundario,
                    alCambiarTono = { tonoTextoSecundario = it }
                )
            }

            Spacer(Modifier.height(18.dp))

            // SECCIÓN 2: Acento Esencial (10% de color)
            ComponenteGrupo(
                etiqueta = "Acento Esencial (10% Color)",
                icono = Icons.Filled.Security
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Elige el acento protagonista para acciones primarias, botones y estados activos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorTextoSecundario
                    )

                    Spacer(Modifier.height(14.dp))

                    // Fila 1: Acentos Cromáticos
                    Text(
                        text = "Acentos Cromáticos",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = colorTextoSecundario
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        acentosPredefinidos.forEach { (color, nombre) ->
                            val seleccionado = !esPersonalizadoActivo && colorAcentoActual == color
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (seleccionado) 3.dp else 1.dp,
                                        color = if (seleccionado) colorTextoPrincipal else colorBorde,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        haptica.tic()
                                        colorAcentoActual = color
                                        esPersonalizadoActivo = false
                                        val (h, s, v) = colorAhsv(color)
                                        huePersonalizado = h
                                        satPersonalizado = s
                                        valPersonalizado = v
                                        alfaPersonalizado = color.alpha
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (seleccionado) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = nombre,
                                        tint = colorContraste(color),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Fila 2: Matices Neutros (Estilo Apple) y Personalizado
                    Text(
                        text = "Matices Neutros (Estilo Apple) y Personalizado",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = colorTextoSecundario
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        acentosGrisesApple.forEach { (color, nombre) ->
                            val seleccionado = !esPersonalizadoActivo && colorAcentoActual == color
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (seleccionado) 3.dp else 1.dp,
                                        color = if (seleccionado) colorTextoPrincipal else colorBorde,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        haptica.tic()
                                        colorAcentoActual = color
                                        esPersonalizadoActivo = false
                                        val (h, s, v) = colorAhsv(color)
                                        huePersonalizado = h
                                        satPersonalizado = s
                                        valPersonalizado = v
                                        alfaPersonalizado = color.alpha
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (seleccionado) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = nombre,
                                        tint = colorContraste(color),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        // Botón Personalizado
                        val seleccionadoPersonalizado = esPersonalizadoActivo
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    if (seleccionadoPersonalizado) colorAcentoActual else colorCampo
                                )
                                .border(
                                    width = if (seleccionadoPersonalizado) 3.dp else 1.dp,
                                    color = if (seleccionadoPersonalizado) colorTextoPrincipal else colorBorde,
                                    shape = CircleShape
                                )
                                .clickable {
                                    haptica.tic()
                                    esPersonalizadoActivo = true
                                    mostrarAjustePersonalizado = true
                                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Tune,
                                contentDescription = "Personalizado",
                                tint = if (seleccionadoPersonalizado) colorContraste(colorAcentoActual) else colorTextoPrincipal,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Panel expandible para controles personalizados con sliders y transparencias
                    if (mostrarAjustePersonalizado || esPersonalizadoActivo) {
                        Spacer(Modifier.height(16.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                                .background(colorCampo)
                                .border(1.dp, colorBorde, RoundedCornerShape(CurvaturaEsquinas - 4.dp))
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(colorAcentoActual)
                                        .border(1.dp, colorBorde, CircleShape)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Acento Personalizado",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = colorTextoPrincipal,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = colorAcentoActual.aHexConAlfa(),
                                    style = EstiloMono.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp),
                                    color = colorTextoPrincipal
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            // Control 1: Tono / Color
                            Text(
                                text = "Tono: ${huePersonalizado.toInt()}°",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = colorTextoSecundario
                            )
                            SliderBoveda(
                                value = huePersonalizado,
                                onValueChange = {
                                    huePersonalizado = it
                                    if (unificarTonos) {
                                        tonoGlobal = it
                                    }
                                    esPersonalizadoActivo = true
                                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                },
                                valueRange = 0f..360f
                            )

                            Spacer(Modifier.height(8.dp))

                            // Control 2: Saturación (0% es Gris puro)
                            Text(
                                text = "Saturación: ${(satPersonalizado * 100).toInt()}% ${if (satPersonalizado < 0.05f) "(Gris Puro)" else ""}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = colorTextoSecundario
                            )
                            SliderBoveda(
                                value = satPersonalizado,
                                onValueChange = {
                                    satPersonalizado = it
                                    esPersonalizadoActivo = true
                                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                },
                                valueRange = 0f..1f
                            )

                            Spacer(Modifier.height(8.dp))

                            // Control 3: Brillo / Luminosidad
                            Text(
                                text = "Brillo: ${(valPersonalizado * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = colorTextoSecundario
                            )
                            SliderBoveda(
                                value = valPersonalizado,
                                onValueChange = {
                                    valPersonalizado = it
                                    esPersonalizadoActivo = true
                                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                },
                                valueRange = 0.10f..1f
                            )

                            Spacer(Modifier.height(8.dp))

                            // Control 4: Transparencia / Opacidad (Alfa)
                            Text(
                                text = "Transparencia / Opacidad: ${(alfaPersonalizado * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (alfaPersonalizado < 1.0f) colorAcentoActual else colorTextoSecundario
                                )
                            )
                            SliderBoveda(
                                value = alfaPersonalizado,
                                onValueChange = {
                                    alfaPersonalizado = it
                                    esPersonalizadoActivo = true
                                    colorAcentoActual = Color.hsv(huePersonalizado, satPersonalizado, valPersonalizado, alfaPersonalizado)
                                },
                                valueRange = 0.10f..1f
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // SECCIÓN 3: Acciones Finales
            BotonBoveda(
                texto = "📋 Copiar Paleta para el Asistente",
                alPulsar = { copiarPaletaAlPortapapeles() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            BotonPeligro(
                texto = "🔄 Restablecer Valores Predeterminados",
                alPulsar = { restablecerValores() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

/**
 * Tarjeta de vista previa compacta que permanece flotante/fijada arriba (Sticky Top)
 * permitiendo ver en tiempo real la combinación de colores mientras se hace scroll en los controles.
 * Sin títulos innecesarios para maximizar el área visible.
 */
@Composable
private fun TarjetaFlotantePreviaLaboratorio(
    colorFondo: Color,
    colorTarjeta: Color,
    colorBorde: Color,
    colorCampo: Color,
    colorTextoPrincipal: Color,
    colorTextoSecundario: Color,
    colorAcento: Color,
    switchActivo: Boolean,
    onAlternarSwitch: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colorFondo)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(CurvaturaEsquinas))
                .background(colorTarjeta)
                .border(1.dp, colorBorde, RoundedCornerShape(CurvaturaEsquinas))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Fila 1: Credencial con Icono, Textos y Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(colorAcento),
                    contentAlignment = Alignment.Center
                ) {
                    val colorContenidoAcento = if (colorAcento.alpha < 0.45f) {
                        colorTextoPrincipal
                    } else {
                        colorContraste(colorAcento)
                    }
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = colorContenidoAcento,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Google Workspace",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = colorTextoPrincipal
                        ),
                        maxLines = 1
                    )
                    Spacer(Modifier.height(1.dp))
                    Text(
                        text = "usuario@empresa.com • TOTP activo",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = colorTextoSecundario
                        ),
                        maxLines = 1
                    )
                }

                Spacer(Modifier.width(8.dp))

                SwitchBoveda(
                    checked = switchActivo,
                    colorActivo = colorAcento,
                    onCheckedChange = onAlternarSwitch
                )
            }

            Spacer(Modifier.height(8.dp))

            // Divisor sutil
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.6.dp)
                    .background(colorBorde)
            )

            Spacer(Modifier.height(8.dp))

            // Fila 2: Campo interactivo (Capa 2) y Botón Primario
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Campo de texto simulado con Capa 2
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorCampo)
                        .border(0.8.dp, colorBorde, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Security,
                        contentDescription = null,
                        tint = colorTextoSecundario,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Contraseña segura...",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = colorTextoSecundario
                        ),
                        maxLines = 1
                    )
                }

                // Botón Primario compacto
                val colorContenidoBoton = if (colorAcento.alpha < 0.45f) {
                    colorTextoPrincipal
                } else {
                    colorContraste(colorAcento)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorAcento)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Guardar",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = colorContenidoBoton
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ControlCapaFila(
    etiqueta: String,
    colorActual: Color,
    luminancia: Float,
    alCambiarLuminancia: (Float) -> Unit,
    mostrarControlTono: Boolean = false,
    tono: Float = 0f,
    alCambiarTono: ((Float) -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(colorActual)
                    .border(1.dp, Color.Gray.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = colorActual.aHexConAlfa(),
                style = EstiloMono.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
        }
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Luminancia / Brillo",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = Color.Gray
            )
            Text(
                text = "${(luminancia * 100).toInt()}%",
                style = EstiloMono.copy(fontSize = 11.sp),
                color = Color.Gray
            )
        }
        SliderBoveda(
            value = luminancia,
            onValueChange = alCambiarLuminancia,
            valueRange = 0.0f..1.0f
        )

        if (mostrarControlTono && alCambiarTono != null) {
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color.hsv(tono, 0.8f, 0.9f))
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Tono individual: ${tono.toInt()}°",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
            }
            SliderBoveda(
                value = tono,
                onValueChange = alCambiarTono,
                valueRange = 0.0f..360.0f
            )
        }
    }
}

@BovedaPantallaPreview
@Composable
private fun PreviewPantallaLaboratorioTemas() {
    PreviewTemaBoveda {
        PantallaLaboratorioTemas()
    }
}
