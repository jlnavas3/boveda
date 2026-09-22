package com.jlnavas3.bovedalocal.ui.pantallas

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.jlnavas3.bovedalocal.crypto.OpcionesGenerador
import com.jlnavas3.bovedalocal.crypto.PasswordGenerator
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO
import com.jlnavas3.bovedalocal.util.Haptica
import kotlin.math.roundToInt

@Composable
fun PantallaCalibracionWidget1x1(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val color1x1BordeEfectivo = parsearColorO(ajustes.widget1x1ColorBorde, Ambar)
    val color1x1IconoEfectivo = parsearColorO(ajustes.widget1x1ColorIcono, Ambar)
    val color1x1FondoEfectivo = parsearColorO(ajustes.widget1x1ColorFondo, Color(0xFF1C1A17))

    var color1x1TabSeleccionada by remember { mutableIntStateOf(0) }

    val opcionesAlineacion = remember {
        listOf(
            OpcionSelectorModal(
                valor = "arriba",
                etiquetaFila = "Arriba (predeterminado)",
                etiquetaModal = "Arriba (predeterminado)",
                descripcionModal = "Alineado a la parte superior de la celda 1x1 del launcher",
                icono = Icons.Filled.VerticalAlignTop
            ),
            OpcionSelectorModal(
                valor = "centro",
                etiquetaFila = "Centro",
                etiquetaModal = "Centro",
                descripcionModal = "Centrado vertical y horizontalmente en la celda 1x1",
                icono = Icons.Filled.CropSquare
            ),
            OpcionSelectorModal(
                valor = "abajo",
                etiquetaFila = "Abajo",
                etiquetaModal = "Abajo",
                descripcionModal = "Alineado a la parte inferior de la celda 1x1 del launcher",
                icono = Icons.Filled.VerticalAlignBottom
            ),
            OpcionSelectorModal(
                valor = "izquierda",
                etiquetaFila = "Izquierda",
                etiquetaModal = "Izquierda",
                descripcionModal = "Alineado al margen izquierdo de la celda 1x1",
                icono = Icons.AutoMirrored.Filled.FormatAlignLeft
            ),
            OpcionSelectorModal(
                valor = "derecha",
                etiquetaFila = "Derecha",
                etiquetaModal = "Derecha",
                descripcionModal = "Alineado al margen derecho de la celda 1x1",
                icono = Icons.AutoMirrored.Filled.FormatAlignRight
            )
        )
    }

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            // 1. Barra superior
            BarraSuperiorPantalla(
                titulo = "Calibración Generador 1x1",
                idEtiqueta = "03.3.G1",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )

            // 2. Cabecera FLOTANTE fija: Simulador visual de la celda 1x1 del launcher
            val alineacionPreview = when (ajustes.widget1x1Alineamiento.lowercase()) {
                "abajo" -> Alignment.BottomCenter
                "centro" -> Alignment.Center
                "izquierda" -> Alignment.CenterStart
                "derecha" -> Alignment.CenterEnd
                else /* "arriba" */ -> Alignment.TopCenter
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF101216))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                // Marco simulador de la celda 1x1 del launcher (116x116 dp)
                Box(
                    modifier = Modifier
                        .size(116.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF17191E))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
                        .padding(4.dp),
                    contentAlignment = alineacionPreview
                ) {
                    val forma1x1 = RoundedCornerShape(ajustes.widget1x1CurvaturaEsquinasDp.dp)
                    val colorFondo1x1 = color1x1FondoEfectivo.copy(alpha = ajustes.widget1x1TransparenciaFondo.coerceIn(0f, 1f))
                    val grosor1x1Dp = ajustes.widget1x1GrosorBordeDp.dp
                    val ancho1x1Dp = ajustes.widget1x1AnchoDp.dp
                    val alto1x1Dp = ajustes.widget1x1AltoDp.dp
                    val minDimDp = minOf(ajustes.widget1x1AnchoDp, ajustes.widget1x1AltoDp).dp

                    Box(
                        modifier = Modifier
                            .offset(x = ajustes.widget1x1OffsetX.dp, y = ajustes.widget1x1OffsetY.dp)
                            .size(width = ancho1x1Dp, height = alto1x1Dp)
                            .clip(forma1x1)
                            .background(colorFondo1x1)
                            .then(
                                if (ajustes.widget1x1GrosorBordeDp > 0.1f) {
                                    Modifier.border(
                                        grosor1x1Dp,
                                        color1x1BordeEfectivo.copy(alpha = (ajustes.widget1x1TransparenciaFondo.coerceAtLeast(0.6f))),
                                        forma1x1
                                    )
                                } else {
                                    Modifier
                                }
                            )
                            .clickable {
                                haptica.probar(ajustes.widget1x1HapticaIntensidad)
                                val clave = if (ajustes.widget1x1Modo == "patron") {
                                    PasswordGenerator.generarPorPatron(ajustes.widget1x1Patron)
                                } else {
                                    PasswordGenerator.generarAleatoria(
                                        OpcionesGenerador(
                                            longitud = ajustes.widget1x1Longitud,
                                            mayusculas = true,
                                            minusculas = true,
                                            digitos = true,
                                            simbolos = true,
                                            simbolosPersonalizados = ajustes.widget1x1Simbolos
                                        )
                                    )
                                }
                                Toast.makeText(contexto, "Clave: $clave", Toast.LENGTH_SHORT).show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Key,
                            contentDescription = "Generador Rápido 1x1",
                            tint = color1x1IconoEfectivo,
                            modifier = Modifier.size((minDimDp * 0.52f).coerceAtLeast(16.dp))
                        )
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // 3. Controles desplazables que se deslizan suavemente por debajo de la previa fija
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Presets de tamaño
                ComponenteGrupo(
                    etiqueta = "Presets de tamaño",
                    idGrupo = "03.3.G11A",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Dimensiones rápidas optimizadas para diferentes cuadrículas de launcher"
                ) {
                    val presets = listOf(
                        "Compacto" to 42f,
                        "Estándar" to 52f,
                        "Grande" to 60f,
                        "Honor" to 55f
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        presets.forEach { (nombre, tamano) ->
                            val seleccionado = if (nombre == "Honor") {
                                ajustes.widget1x1AnchoDp == 55f && ajustes.widget1x1AltoDp == 51f && ajustes.widget1x1CurvaturaEsquinasDp == 15f && ajustes.widget1x1GrosorBordeDp == 0f
                            } else {
                                ajustes.widget1x1AnchoDp == tamano && ajustes.widget1x1AltoDp == tamano
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (seleccionado) ColorAcento.copy(alpha = 0.22f) else SuperficieAlta)
                                    .then(
                                        if (seleccionado) Modifier.border(1.5.dp, ColorAcento, RoundedCornerShape(8.dp))
                                        else Modifier
                                    )
                                    .clickable {
                                        haptica.tic()
                                        if (nombre == "Honor") {
                                            vm.aplicarPresetHonorWidget1x1()
                                        } else {
                                            vm.ajustarWidget1x1PresetTamano(tamano)
                                        }
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = nombre,
                                    color = if (seleccionado) ColorAcento else TextoPrincipal,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Dimensiones y posición
                ComponenteGrupo(
                    etiqueta = "Dimensiones y posición",
                    idGrupo = "03.3.G11B",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Calibra ancho, alto, alineación y desplazamiento dentro del espacio 1x1"
                ) {
                    ComponenteSwitch(
                        titulo = "Bloquear proporción 1:1",
                        icono = Icons.Filled.AspectRatio,
                        colorIcono = ColorIconosInternos,
                        idFila = "03.3.21",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        activo = ajustes.widget1x1BloquearProporcion,
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarWidget1x1BloquearProporcion(it)
                        }
                    )

                    ComponenteSeparador()

                    ComponenteSlider(
                        titulo = if (ajustes.widget1x1BloquearProporcion) "Tamaño del botón" else "Ancho del widget",
                        valor = ajustes.widget1x1AnchoDp,
                        valorTexto = "${ajustes.widget1x1AnchoDp.roundToInt()} dp",
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarWidget1x1Ancho(it)
                        },
                        rango = 32f..80f,
                        pasos = 47,
                        etiquetaMin = "32 dp",
                        etiquetaMax = "80 dp",
                        idFila = "03.3.22",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        icono = Icons.Filled.CropSquare,
                        colorIcono = ColorIconosInternos
                    )

                    if (!ajustes.widget1x1BloquearProporcion) {
                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Alto del widget",
                            valor = ajustes.widget1x1AltoDp,
                            valorTexto = "${ajustes.widget1x1AltoDp.roundToInt()} dp",
                            alCambiar = {
                                haptica.tic()
                                vm.ajustarWidget1x1Alto(it)
                            },
                            rango = 32f..80f,
                            pasos = 47,
                            etiquetaMin = "32 dp",
                            etiquetaMax = "80 dp",
                            idFila = "03.3.23",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            icono = Icons.Filled.CropSquare,
                            colorIcono = ColorIconosInternos
                        )
                    }

                    ComponenteSeparador()

                    ComponenteSelectorModal(
                        titulo = "Alineación base",
                        icono = Icons.Filled.AutoAwesome,
                        colorIcono = ColorIconosInternos,
                        valorSeleccionado = ajustes.widget1x1Alineamiento,
                        opciones = opcionesAlineacion,
                        idFila = "03.3.24",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        alSeleccionar = {
                            haptica.tic()
                            vm.ajustarWidget1x1Alineamiento(it)
                        }
                    )

                    ComponenteSeparador()

                    ComponenteSlider(
                        titulo = "Ajuste fino vertical (Y)",
                        valor = ajustes.widget1x1OffsetY,
                        valorTexto = if (ajustes.widget1x1OffsetY > 0) "+${ajustes.widget1x1OffsetY.roundToInt()} dp (bajar)"
                        else if (ajustes.widget1x1OffsetY < 0) "${ajustes.widget1x1OffsetY.roundToInt()} dp (subir)"
                        else "0 dp (centro)",
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarWidget1x1OffsetY(it)
                        },
                        rango = -30f..30f,
                        pasos = 60,
                        etiquetaMin = "-30 dp (subir)",
                        etiquetaMax = "+30 dp (bajar)",
                        idFila = "03.3.25A",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        icono = Icons.Filled.VerticalAlignBottom,
                        colorIcono = ColorIconosInternos
                    )

                    ComponenteSeparador()

                    ComponenteSlider(
                        titulo = "Ajuste fino horizontal (X)",
                        valor = ajustes.widget1x1OffsetX,
                        valorTexto = if (ajustes.widget1x1OffsetX > 0) "+${ajustes.widget1x1OffsetX.roundToInt()} dp (derecha)"
                        else if (ajustes.widget1x1OffsetX < 0) "${ajustes.widget1x1OffsetX.roundToInt()} dp (izquierda)"
                        else "0 dp (centro)",
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarWidget1x1OffsetX(it)
                        },
                        rango = -30f..30f,
                        pasos = 60,
                        etiquetaMin = "-30 dp (izq)",
                        etiquetaMax = "+30 dp (der)",
                        idFila = "03.3.25B",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        icono = Icons.AutoMirrored.Filled.FormatAlignLeft,
                        colorIcono = ColorIconosInternos
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Forma y bordes
                ComponenteGrupo(
                    etiqueta = "Forma y bordes",
                    idGrupo = "03.3.G10",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Grosor de trazo perimetral y radio de curvatura de esquinas"
                ) {
                    ComponenteSlider(
                        titulo = "Grosor del borde",
                        valor = ajustes.widget1x1GrosorBordeDp,
                        valorTexto = if (ajustes.widget1x1GrosorBordeDp <= 0.1f) "0 dp" else "%.1f dp".format(ajustes.widget1x1GrosorBordeDp),
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarWidget1x1GrosorBorde(it)
                        },
                        rango = 0f..5f,
                        pasos = 49,
                        etiquetaMin = "Sin borde",
                        etiquetaMax = "5 dp",
                        idFila = "03.3.11",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        icono = Icons.Filled.LineWeight,
                        colorIcono = ColorIconosInternos
                    )
                    ComponenteSeparador()
                    ComponenteSlider(
                        titulo = "Radio de esquinas",
                        valor = ajustes.widget1x1CurvaturaEsquinasDp,
                        valorTexto = if (ajustes.widget1x1CurvaturaEsquinasDp <= 0.1f) "0 dp" else "%.0f dp".format(ajustes.widget1x1CurvaturaEsquinasDp),
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarWidget1x1CurvaturaEsquinas(it)
                        },
                        rango = 0f..32f,
                        pasos = 31,
                        etiquetaMin = "Recto",
                        etiquetaMax = "32 dp",
                        idFila = "03.3.12",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        icono = Icons.Filled.CropSquare,
                        colorIcono = ColorIconosInternos
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Transparencia del fondo
                ComponenteGrupo(
                    etiqueta = "Transparencia del fondo",
                    idGrupo = "03.3.G12",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Nivel de opacidad o transparencia del fondo del botón"
                ) {
                    ComponenteSlider(
                        titulo = "Opacidad del fondo",
                        valor = ajustes.widget1x1TransparenciaFondo,
                        valorTexto = "${(ajustes.widget1x1TransparenciaFondo * 100).roundToInt()}%",
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarWidget1x1TransparenciaFondo(it)
                        },
                        rango = 0.0f..1.0f,
                        pasos = 99,
                        etiquetaMin = "0% (Transparente)",
                        etiquetaMax = "100% (Sólido)",
                        idFila = "03.3.26",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        icono = Icons.Filled.Opacity,
                        colorIcono = ColorIconosInternos
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Colores del widget 1x1
                ComponenteGrupo(
                    etiqueta = "Colores del widget",
                    idGrupo = "03.3.G13",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Personaliza los colores de borde, ícono y fondo"
                ) {
                    val pestanasColores1x1 = listOf(
                        Triple("Borde", color1x1BordeEfectivo, Icons.Filled.BorderColor),
                        Triple("Ícono", color1x1IconoEfectivo, Icons.Filled.Key),
                        Triple("Fondo", color1x1FondoEfectivo, Icons.Filled.CropSquare)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        pestanasColores1x1.forEachIndexed { idx, (nombre, color, _) ->
                            val seleccionado = color1x1TabSeleccionada == idx
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (seleccionado) color.copy(alpha = 0.22f) else SuperficieAlta)
                                    .then(
                                        if (seleccionado) Modifier.border(1.5.dp, color, RoundedCornerShape(10.dp))
                                        else Modifier
                                    )
                                    .clickable {
                                        haptica.tic()
                                        color1x1TabSeleccionada = idx
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(color)
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = nombre,
                                        color = if (seleccionado) TextoPrincipal else TextoSecundario,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    when (color1x1TabSeleccionada) {
                        0 -> {
                            SelectorColorEnTiempoReal(
                                colorInicial = color1x1BordeEfectivo,
                                titulo = "Color del Borde"
                            ) { nuevoColor ->
                                vm.ajustarWidget1x1ColorBorde(nuevoColor.aHex())
                            }
                        }
                        1 -> {
                            SelectorColorEnTiempoReal(
                                colorInicial = color1x1IconoEfectivo,
                                titulo = "Color del Ícono"
                            ) { nuevoColor ->
                                vm.ajustarWidget1x1ColorIcono(nuevoColor.aHex())
                            }
                        }
                        2 -> {
                            SelectorColorEnTiempoReal(
                                colorInicial = color1x1FondoEfectivo,
                                titulo = "Color del Fondo"
                            ) { nuevoColor ->
                                vm.ajustarWidget1x1ColorFondo(nuevoColor.aHex())
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Restablecer aspecto
                ComponenteGrupo {
                    ComponenteBotonFila(
                        titulo = "Restablecer aspecto predeterminado",
                        icono = Icons.Filled.RotateLeft,
                        colorIcono = ColorIconosInternos,
                        alPulsar = {
                            haptica.exito()
                            vm.ajustarWidget1x1GrosorBorde(0f)
                            vm.ajustarWidget1x1CurvaturaEsquinas(15f)
                            vm.ajustarWidget1x1TransparenciaFondo(1.0f)
                            vm.ajustarWidget1x1Tamano(55f)
                            vm.ajustarWidget1x1Ancho(55f)
                            vm.ajustarWidget1x1Alto(51f)
                            vm.ajustarWidget1x1BloquearProporcion(false)
                            vm.ajustarWidget1x1OffsetX(0f)
                            vm.ajustarWidget1x1OffsetY(4f)
                            vm.ajustarWidget1x1Alineamiento("arriba")
                            vm.ajustarWidget1x1ColorBorde("#33332E")
                            vm.ajustarWidget1x1ColorIcono("#E6FCFF")
                            vm.ajustarWidget1x1ColorFondo("#2E3333")
                            vm.avisar("Aspecto del widget 1x1 restablecido")
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
