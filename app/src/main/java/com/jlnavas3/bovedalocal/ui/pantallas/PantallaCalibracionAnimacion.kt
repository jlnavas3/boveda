package com.jlnavas3.bovedalocal.ui.pantallas

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.EngranajesBoveda
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.componentes.aEngranajesConfig
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteNavegacion
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO
import kotlin.math.roundToInt

private data class InfoParteEngranaje(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val colorActual: Color,
    val colorPorDefecto: Color,
    val mutador: (Color) -> Unit
)

@Composable
fun PantallaCalibracionAnimacion(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val esEngranajes = ajustes.animacionDesbloqueo == "engranajes"

    var parteEngranajeElegida by remember { mutableIntStateOf(0) }
    var dropdownPartesEngranajeAbierto by remember { mutableStateOf(false) }

    val partesEngranajes = remember(ajustes) {
        listOf(
            InfoParteEngranaje(
                id = "brillo",
                nombre = "Cuerpo: Brillo superior",
                descripcion = "Reflejo metálico en la parte superior del engranaje",
                colorActual = parsearColorO(ajustes.engranajesColorBrillo, Color(0xFFABA799)),
                colorPorDefecto = Color(0xFFABA799),
                mutador = { vm.ajustarEngranajesColor("brillo", it.aHex()) }
            ),
            InfoParteEngranaje(
                id = "principal",
                nombre = "Cuerpo: Color principal",
                descripcion = "Tono base dominante del cuerpo de los dientes",
                colorActual = parsearColorO(ajustes.engranajesColorPrincipal, Color(0xFF918D7E)),
                colorPorDefecto = Color(0xFF918D7E),
                mutador = { vm.ajustarEngranajesColor("principal", it.aHex()) }
            ),
            InfoParteEngranaje(
                id = "sombra_medio",
                nombre = "Cuerpo: Sombra degradado",
                descripcion = "Tono intermedio del degradado radial",
                colorActual = parsearColorO(ajustes.engranajesColorSombraMedio, Color(0xFF635C57)),
                colorPorDefecto = Color(0xFF635C57),
                mutador = { vm.ajustarEngranajesColor("sombra_medio", it.aHex()) }
            ),
            InfoParteEngranaje(
                id = "sombra_oscuro",
                nombre = "Cuerpo: Sombra profunda",
                descripcion = "Relieve y sombra inferior del cuerpo metálico",
                colorActual = parsearColorO(ajustes.engranajesColorSombraOscuro, Color(0xFF404038)),
                colorPorDefecto = Color(0xFF404038),
                mutador = { vm.ajustarEngranajesColor("sombra_oscuro", it.aHex()) }
            ),
            InfoParteEngranaje(
                id = "bisel",
                nombre = "Bordes y biseles",
                descripcion = "Líneas de contorno, bisel de dientes y remaches",
                colorActual = parsearColorO(ajustes.engranajesColorBisel, Color(0xFFA5A19D)),
                colorPorDefecto = Color(0xFFA5A19D),
                mutador = { vm.ajustarEngranajesColor("bisel", it.aHex()) }
            ),
            InfoParteEngranaje(
                id = "interior",
                nombre = "Fondo cavidad interior",
                descripcion = "Fondo de hendidura (transparente para ver a través de las ruedas)",
                colorActual = parsearColorO(ajustes.engranajesColorInterior, Color(0x00000000)),
                colorPorDefecto = Color(0x00000000),
                mutador = { vm.ajustarEngranajesColor("interior", it.aHex()) }
            ),
            InfoParteEngranaje(
                id = "cubo",
                nombre = "Cubo central (remache)",
                descripcion = "Centro de rotación del engranaje",
                colorActual = parsearColorO(ajustes.engranajesColorCubo, Color(0xFFD3D1C8)),
                colorPorDefecto = Color(0xFFD3D1C8),
                mutador = { vm.ajustarEngranajesColor("cubo", it.aHex()) }
            ),
            InfoParteEngranaje(
                id = "eje",
                nombre = "Eje central (núcleo)",
                descripcion = "Orificio interior y ranura del eje de giro",
                colorActual = parsearColorO(ajustes.engranajesColorEje, Color(0xFF141316)),
                colorPorDefecto = Color(0xFF141316),
                mutador = { vm.ajustarEngranajesColor("eje", it.aHex()) }
            )
        )
    }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            // 1. Barra superior
            BarraSuperiorPantalla(
                titulo = if (esEngranajes) "Mecanismo de engranajes" else "Puerta de bóveda",
                idEtiqueta = "03.2.G2",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )

            // 2. Cabecera FLOTANTE fija: Muestra solo la animación sin textos ni títulos
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(FormaCampo)
                    .background(Superficie)
                    .then(
                        if (GrosorBorde > 0.dp) Modifier.border(GrosorBorde, ColorBordeActual, FormaCampo) else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (esEngranajes) {
                    EngranajesBoveda(
                        abierta = false,
                        modifier = Modifier.fillMaxSize(),
                        config = ajustes.aEngranajesConfig()
                    )
                } else {
                    val colorPuertaPersonalizado = if (ajustes.puertaColor.isNotBlank()) {
                        parsearColorO(ajustes.puertaColor, Ambar)
                    } else null

                    PuertaBoveda(
                        abierta = false,
                        tamano = 160,
                        velocidadFactor = ajustes.puertaVelocidad,
                        grosorFactor = ajustes.puertaGrosorAnillos,
                        colorPersonalizado = colorPuertaPersonalizado
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            // 3. Controles scrollables que se deslizan por debajo de la animación flotante
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                if (esEngranajes) {
                    // --- CALIBRACIÓN DE ENGRANAJES ---

                    // Grupo 1: Geometría y movimiento
                    ComponenteGrupo(
                        etiqueta = "Geometría y movimiento",
                        descripcion = "Ajusta en tiempo real las dimensiones y comportamiento físico de las ruedas",
                        mostrarId = ajustes.mostrarIdsAjustes
                    ) {
                        ComponenteSlider(
                            titulo = "Velocidad de rotación",
                            valor = ajustes.engranajesVelocidad,
                            valorTexto = "${ajustes.engranajesVelocidad.roundToInt()} °/s",
                            rango = 0f..120f,
                            alCambiar = { vm.ajustarEngranajesVelocidad(it) },
                            etiquetaMin = "0°/s (Detenido)",
                            etiquetaMax = "120°/s (Rápido)"
                        )
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Grosor de borde y bisel",
                            valor = ajustes.engranajesGrosorBorde,
                            valorTexto = if (ajustes.engranajesGrosorBorde == 0f) "0 dp (Sin borde)" else "${String.format(java.util.Locale.US, "%.1f", ajustes.engranajesGrosorBorde)} dp",
                            rango = 0f..6f,
                            alCambiar = { vm.ajustarEngranajesGrosorBorde(it) },
                            etiquetaMin = "0 dp (Plano)",
                            etiquetaMax = "6.0 dp (Grueso)"
                        )
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Altura de los dientes",
                            valor = ajustes.engranajesAlturaDientes,
                            valorTexto = "${String.format(java.util.Locale.US, "%.2f", ajustes.engranajesAlturaDientes)}x",
                            rango = 0.2f..2.5f,
                            alCambiar = { vm.ajustarEngranajesAlturaDientes(it) },
                            etiquetaMin = "0.2x (Cortos)",
                            etiquetaMax = "2.5x (Profundos)"
                        )
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Ancho de los dientes",
                            valor = ajustes.engranajesAnchoDientes,
                            valorTexto = "${String.format(java.util.Locale.US, "%.2f", ajustes.engranajesAnchoDientes)}x",
                            rango = 0.3f..2.0f,
                            alCambiar = { vm.ajustarEngranajesAnchoDientes(it) },
                            etiquetaMin = "0.3x (Finos)",
                            etiquetaMax = "2.0x (Anchos)"
                        )
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Grosor de radios",
                            valor = ajustes.engranajesGrosorRadios,
                            valorTexto = if (ajustes.engranajesGrosorRadios == 0f) "Sin radios" else "${String.format(java.util.Locale.US, "%.2f", ajustes.engranajesGrosorRadios)}x",
                            rango = 0f..4.0f,
                            alCambiar = { vm.ajustarEngranajesGrosorRadios(it) },
                            etiquetaMin = "0x (Ciego)",
                            etiquetaMax = "4.0x (Robustos)"
                        )
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Cantidad de radios",
                            valor = if (ajustes.engranajesCantidadRadios in 3..12) ajustes.engranajesCantidadRadios.toFloat() else 2f,
                            valorTexto = if (ajustes.engranajesCantidadRadios <= 2) "Auto (según tamaño)" else "${ajustes.engranajesCantidadRadios} radios",
                            rango = 2f..12f,
                            pasos = 9,
                            alCambiar = { vm.ajustarEngranajesCantidadRadios(if (it.roundToInt() <= 2) 0 else it.roundToInt()) },
                            etiquetaMin = "Auto (3-6)",
                            etiquetaMax = "12 radios"
                        )
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Curvatura de radios",
                            valor = ajustes.engranajesCurvaturaRadios,
                            valorTexto = "${String.format(java.util.Locale.US, "%.2f", ajustes.engranajesCurvaturaRadios)}x",
                            rango = 0.0f..1.0f,
                            alCambiar = { vm.ajustarEngranajesCurvaturaRadios(it) },
                            etiquetaMin = "0.0x (Recto)",
                            etiquetaMax = "1.0x (Redondeado)"
                        )
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Cavidad interior (radio)",
                            valor = ajustes.engranajesRadioInterior,
                            valorTexto = String.format(java.util.Locale.US, "%.2f", ajustes.engranajesRadioInterior),
                            rango = 0.20f..0.95f,
                            alCambiar = { vm.ajustarEngranajesRadioInterior(it) },
                            etiquetaMin = "0.20 (Pequeña)",
                            etiquetaMax = "0.95 (Amplia)"
                        )
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Tamaño eje y cubo central",
                            valor = ajustes.engranajesTamanoEje,
                            valorTexto = "${String.format(java.util.Locale.US, "%.2f", ajustes.engranajesTamanoEje)}x",
                            rango = 0.2f..2.5f,
                            alCambiar = { vm.ajustarEngranajesTamanoEje(it) },
                            etiquetaMin = "0.2x (Minúsculo)",
                            etiquetaMax = "2.5x (Grande)"
                        )
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Intensidad de sombra y relieve",
                            valor = ajustes.engranajesSombraIntensidad,
                            valorTexto = String.format(java.util.Locale.US, "%.2f", ajustes.engranajesSombraIntensidad),
                            rango = 0.0f..1.0f,
                            alCambiar = { vm.ajustarEngranajesSombraIntensidad(it) },
                            etiquetaMin = "0.0 (Sin sombra)",
                            etiquetaMax = "1.0 (Contraste alto)"
                        )
                        ComponenteSeparador()
                        ComponenteBotonFila(
                            titulo = "Restablecer grupo",
                            alPulsar = {
                                vm.restablecerAjustesEngranajes()
                                Toast.makeText(contexto, "Geometría restablecida", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    // Grupo 2: Selector de colores de piezas
                    val parteActual = partesEngranajes[parteEngranajeElegida.coerceIn(0, partesEngranajes.size - 1)]
                    ComponenteGrupo(
                        etiqueta = "Colores de las piezas",
                        descripcion = "Personaliza la tonalidad cromática de cada elemento del engranaje",
                        mostrarId = ajustes.mostrarIdsAjustes
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(FormaCampo)
                                        .background(Superficie)
                                        .then(
                                            if (GrosorBorde > 0.dp) {
                                                Modifier.border(
                                                    GrosorBorde,
                                                    if (dropdownPartesEngranajeAbierto) ColorTitulos else ColorBordeActual,
                                                    FormaCampo
                                                )
                                            } else {
                                                Modifier
                                            }
                                        )
                                        .clickable { dropdownPartesEngranajeAbierto = true }
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(parteActual.colorActual)
                                            .border(1.dp, TextoPrincipal.copy(alpha = 0.3f), CircleShape)
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = parteActual.nombre,
                                            color = TextoPrincipal,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                        )
                                        Text(
                                            text = parteActual.descripcion,
                                            color = TextoSecundario,
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Icon(
                                        imageVector = if (dropdownPartesEngranajeAbierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                        contentDescription = null,
                                        tint = TextoSecundario
                                    )
                                }

                                MenuDesplegableBoveda(
                                    expanded = dropdownPartesEngranajeAbierto,
                                    onDismissRequest = { dropdownPartesEngranajeAbierto = false }
                                ) {
                                    partesEngranajes.forEachIndexed { idx, p ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(24.dp)
                                                            .clip(CircleShape)
                                                            .background(p.colorActual)
                                                            .border(1.dp, TextoPrincipal.copy(alpha = 0.3f), CircleShape)
                                                    )
                                                    Spacer(Modifier.width(10.dp))
                                                    Column {
                                                        Text(p.nombre, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                                                        Text(p.descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                                                    }
                                                }
                                            },
                                            onClick = {
                                                parteEngranajeElegida = idx
                                                dropdownPartesEngranajeAbierto = false
                                            }
                                        )
                                        if (idx < partesEngranajes.size - 1) SeparadorOpcionMenu()
                                    }
                                }
                            }

                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = "Color actual: ${parteActual.colorActual.aHex()}",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(Modifier.height(8.dp))

                            SelectorColorEnTiempoReal(
                                colorInicial = parteActual.colorActual,
                                titulo = parteActual.nombre
                            ) { nuevoColor ->
                                parteActual.mutador(nuevoColor)
                            }
                        }

                        ComponenteSeparador()
                        ComponenteBotonFila(
                            titulo = "Restablecer",
                            alPulsar = {
                                parteActual.mutador(parteActual.colorPorDefecto)
                            }
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    // Grupo 3: Acciones globales del mecanismo
                    ComponenteGrupo(
                        etiqueta = "Acciones del mecanismo",
                        mostrarId = ajustes.mostrarIdsAjustes
                    ) {
                        ComponenteNavegacion(
                            titulo = "Copiar valores",
                            icono = Icons.Filled.ContentCopy,
                            colorIcono = ColorAcento,
                            alPulsar = {
                                val textoConfig = buildString {
                                    appendLine("=== CONFIGURACIÓN DE ENGRANAJES ===")
                                    appendLine("velocidad = ${ajustes.engranajesVelocidad.roundToInt()}f")
                                    appendLine("grosorBorde = ${String.format(java.util.Locale.US, "%.1f", ajustes.engranajesGrosorBorde)}f")
                                    appendLine("alturaDientes = ${String.format(java.util.Locale.US, "%.2f", ajustes.engranajesAlturaDientes)}f")
                                    appendLine("anchoDientes = ${String.format(java.util.Locale.US, "%.2f", ajustes.engranajesAnchoDientes)}f")
                                    appendLine("grosorRadios = ${String.format(java.util.Locale.US, "%.2f", ajustes.engranajesGrosorRadios)}f")
                                    appendLine("curvaturaRadios = ${String.format(java.util.Locale.US, "%.2f", ajustes.engranajesCurvaturaRadios)}f")
                                    appendLine("cantidadRadios = ${if (ajustes.engranajesCantidadRadios <= 2) "0 (Auto)" else "${ajustes.engranajesCantidadRadios}"}")
                                    appendLine("radioInterior = ${String.format(java.util.Locale.US, "%.2f", ajustes.engranajesRadioInterior)}f")
                                    appendLine("tamanoEje = ${String.format(java.util.Locale.US, "%.2f", ajustes.engranajesTamanoEje)}f")
                                    appendLine("sombraIntensidad = ${String.format(java.util.Locale.US, "%.2f", ajustes.engranajesSombraIntensidad)}f")
                                    appendLine("colorBrillo = \"${ajustes.engranajesColorBrillo}\"")
                                    appendLine("colorPrincipal = \"${ajustes.engranajesColorPrincipal}\"")
                                    appendLine("colorSombraMedio = \"${ajustes.engranajesColorSombraMedio}\"")
                                    appendLine("colorSombraOscuro = \"${ajustes.engranajesColorSombraOscuro}\"")
                                    appendLine("colorBisel = \"${ajustes.engranajesColorBisel}\"")
                                    appendLine("colorInterior = \"${ajustes.engranajesColorInterior}\"")
                                    appendLine("colorCubo = \"${ajustes.engranajesColorCubo}\"")
                                    appendLine("colorEje = \"${ajustes.engranajesColorEje}\"")
                                }
                                val clipboard = contexto.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Configuración Engranajes", textoConfig))
                                Toast.makeText(contexto, "Configuración copiada al portapapeles", Toast.LENGTH_SHORT).show()
                            }
                        )
                        ComponenteSeparador()
                        ComponenteBotonFila(
                            titulo = "Restablecer módulo",
                            alPulsar = {
                                vm.restablecerAjustesEngranajes()
                                Toast.makeText(contexto, "Engranajes restablecidos", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                } else {
                    // --- CALIBRACIÓN DE PUERTA DE BÓVEDA ---

                    // Grupo 1: Movimiento y geometría
                    ComponenteGrupo(
                        etiqueta = "Movimiento y geometría",
                        descripcion = "Ajusta la velocidad de giro y el ancho de trazo de los anillos concéntricos",
                        mostrarId = ajustes.mostrarIdsAjustes
                    ) {
                        ComponenteSlider(
                            titulo = "Velocidad de rotación",
                            icono = Icons.Filled.Speed,
                            colorIcono = Color(0xFF1E88E5),
                            valor = ajustes.puertaVelocidad,
                            valorTexto = "${String.format(java.util.Locale.US, "%.1f", ajustes.puertaVelocidad)}x",
                            rango = 0.2f..3.0f,
                            alCambiar = { vm.ajustarPuertaVelocidad(it) },
                            etiquetaMin = "0.2x (Lenta)",
                            etiquetaMax = "3.0x (Rápida)"
                        )
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Grosor de los anillos",
                            icono = Icons.Filled.Tune,
                            colorIcono = Color(0xFF00897B),
                            valor = ajustes.puertaGrosorAnillos,
                            valorTexto = "${String.format(java.util.Locale.US, "%.1f", ajustes.puertaGrosorAnillos)}x",
                            rango = 0.5f..2.5f,
                            alCambiar = { vm.ajustarPuertaGrosorAnillos(it) },
                            etiquetaMin = "0.5x (Fino)",
                            etiquetaMax = "2.5x (Grueso)"
                        )
                        ComponenteSeparador()
                        ComponenteBotonFila(
                            titulo = "Restablecer grupo",
                            alPulsar = {
                                vm.ajustarPuertaVelocidad(1.0f)
                                vm.ajustarPuertaGrosorAnillos(1.0f)
                                Toast.makeText(contexto, "Geometría de puerta restablecida", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    // Grupo 2: Tonalidad cromática
                    val colorActualPuerta = if (ajustes.puertaColor.isNotBlank()) {
                        parsearColorO(ajustes.puertaColor, Ambar)
                    } else Ambar

                    ComponenteGrupo(
                        etiqueta = "Color de la puerta de bóveda",
                        descripcion = "Tono cromático de los anillos giratorios y el núcleo de seguridad",
                        mostrarId = ajustes.mostrarIdsAjustes
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (ajustes.puertaColor.isBlank()) "Color actual: Dorado ámbar predeterminado" else "Color actual: ${ajustes.puertaColor}",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(Modifier.height(8.dp))
                            SelectorColorEnTiempoReal(
                                colorInicial = colorActualPuerta,
                                titulo = "Anillos de la bóveda"
                            ) { nuevoColor ->
                                vm.ajustarPuertaColor(nuevoColor.aHex())
                            }
                        }

                        ComponenteSeparador()
                        ComponenteBotonFila(
                            titulo = "Restablecer",
                            alPulsar = {
                                vm.ajustarPuertaColor("")
                            }
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    // Grupo 3: Acciones
                    ComponenteGrupo(
                        etiqueta = "Acciones de la puerta",
                        mostrarId = ajustes.mostrarIdsAjustes
                    ) {
                        ComponenteBotonFila(
                            titulo = "Restablecer módulo",
                            alPulsar = {
                                vm.restablecerAjustesPuerta()
                                Toast.makeText(contexto, "Puerta de bóveda restablecida", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
