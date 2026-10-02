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

    // Colores calculados reactivamente
    val colorFondo = remember(lumFondo, modoOscuro) {
        crearGris(restringirLuminancia(lumFondo, modoOscuro, esSuperficie = true))
    }
    val colorTarjeta = remember(lumTarjeta, modoOscuro) {
        crearGris(restringirLuminancia(lumTarjeta, modoOscuro, esSuperficie = true))
    }
    val colorCampo = remember(lumCampo, modoOscuro) {
        crearGris(restringirLuminancia(lumCampo, modoOscuro, esSuperficie = true))
    }
    val colorBorde = remember(lumBorde, modoOscuro) {
        crearGris(restringirLuminancia(lumBorde, modoOscuro, esSuperficie = true))
    }
    val colorTextoPrincipal = remember(lumTextoPrincipal, modoOscuro) {
        crearGris(restringirLuminancia(lumTextoPrincipal, modoOscuro, esSuperficie = false))
    }
    val colorTextoSecundario = remember(lumTextoSecundario, modoOscuro) {
        crearGris(restringirLuminancia(lumTextoSecundario, modoOscuro, esSuperficie = false))
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

    fun copiarPaletaAlPortapapeles() {
        haptica.exito()
        val json = paletaActual.aJson()
        portapapeles.setText(AnnotatedString(json))
        Toast.makeText(contexto, "¡Paleta copiada al portapapeles! Pégala en el chat.", Toast.LENGTH_LONG).show()
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
        colorAcentoActual = d.acento
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

            // SECCIÓN 1: Escala de Grises (90% de la interfaz)
            ComponenteGrupo(
                etiqueta = "Escala de Grises (Capas de Elevación)",
                icono = Icons.Filled.Palette
            ) {
                // Capa 0: Fondo de Pantalla
                ControlLuminanciaFila(
                    etiqueta = "Fondo general (Capa 0)",
                    colorActual = colorFondo,
                    valor = lumFondo,
                    alCambiar = { lumFondo = it }
                )
                SeparadorFilaAjuste()

                // Capa 1: Tarjetas y Grupos
                ControlLuminanciaFila(
                    etiqueta = "Tarjetas y grupos (Capa 1)",
                    colorActual = colorTarjeta,
                    valor = lumTarjeta,
                    alCambiar = { lumTarjeta = it }
                )
                SeparadorFilaAjuste()

                // Capa 2: Campos de entrada y chips
                ControlLuminanciaFila(
                    etiqueta = "Campos y chips (Capa 2)",
                    colorActual = colorCampo,
                    valor = lumCampo,
                    alCambiar = { lumCampo = it }
                )
                SeparadorFilaAjuste()

                // Bordes y separadores
                ControlLuminanciaFila(
                    etiqueta = "Bordes y líneas divisorias",
                    colorActual = colorBorde,
                    valor = lumBorde,
                    alCambiar = { lumBorde = it }
                )
                SeparadorFilaAjuste()

                // Texto Principal
                ControlLuminanciaFila(
                    etiqueta = "Texto principal (Títulos y datos)",
                    colorActual = colorTextoPrincipal,
                    valor = lumTextoPrincipal,
                    alCambiar = { lumTextoPrincipal = it }
                )
                SeparadorFilaAjuste()

                // Texto Secundario
                ControlLuminanciaFila(
                    etiqueta = "Texto secundario (Subtítulos)",
                    colorActual = colorTextoSecundario,
                    valor = lumTextoSecundario,
                    alCambiar = { lumTextoSecundario = it }
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
                        text = "Elige el color protagonista para acciones primarias, botones y estados activos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorTextoSecundario
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        acentosPredefinidos.forEach { (color, nombre) ->
                            val seleccionado = colorAcentoActual == color
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
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (seleccionado) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = nombre,
                                        tint = if (color == Color(0xFFE5A93C) || color == Color(0xFFD4AF37)) Color.Black else Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
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
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = if (colorAcento == Color(0xFFE5A93C) || colorAcento == Color(0xFFD4AF37)) Color.Black else Color.White,
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
                            color = if (colorAcento == Color(0xFFE5A93C) || colorAcento == Color(0xFFD4AF37)) Color.Black else Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ControlLuminanciaFila(
    etiqueta: String,
    colorActual: Color,
    valor: Float,
    alCambiar: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
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
                text = colorActual.aHex(),
                style = EstiloMono.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
        }
        Spacer(Modifier.height(4.dp))
        SliderBoveda(
            value = valor,
            onValueChange = alCambiar,
            valueRange = 0.0f..1.0f
        )
    }
}

@BovedaPantallaPreview
@Composable
private fun PreviewPantallaLaboratorioTemas() {
    PreviewTemaBoveda {
        PantallaLaboratorioTemas()
    }
}
