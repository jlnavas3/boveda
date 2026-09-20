package com.jlnavas3.bovedalocal.ui.pantallas

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
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.IndicadorTotpTarta
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorGenerador
import com.jlnavas3.bovedalocal.ui.theme.ColorSalud
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun PantallaAjustesWidget(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var vistaBloqueadaEnPreview by remember { mutableStateOf(false) }

    val reqPreview = remember { BringIntoViewRequester() }
    val reqGrosor = remember { BringIntoViewRequester() }
    val reqCurvatura = remember { BringIntoViewRequester() }
    val reqOpacidad = remember { BringIntoViewRequester() }
    val reqColores = remember { BringIntoViewRequester() }

    LaunchedEffect(seccionDestino) {
        if (seccionDestino != null) {
            when {
                seccionDestino == "09.4.1" -> reqPreview.bringIntoView()
                seccionDestino == "09.4.2" -> reqGrosor.bringIntoView()
                seccionDestino == "09.4.3" -> reqCurvatura.bringIntoView()
                seccionDestino == "09.4.4" -> reqOpacidad.bringIntoView()
                seccionDestino.startsWith("09.4.") && seccionDestino != "09.4" -> reqColores.bringIntoView()
            }
        }
    }

    // Simulación de cuenta atrás para la vista previa
    var ahoraSegundos by remember { mutableLongStateOf(System.currentTimeMillis() / 1000) }
    LaunchedEffect(Unit) {
        while (true) {
            ahoraSegundos = System.currentTimeMillis() / 1000
            delay(1000)
        }
    }
    val segundosRestantes = Totp.segundosRestantes(ahoraSegundos, 30L)

    val colorBordeEfectivo = parsearColorO(ajustes.widgetColorBorde, Ambar)
    val colorContadorEfectivo = parsearColorO(ajustes.widgetColorContador, Color.White)
    val colorCodigoEfectivo = parsearColorO(ajustes.widgetColorCodigo, Ambar)
    val colorTituloIconoEfectivo = parsearColorO(ajustes.widgetColorTituloIcono, Color.White)

    // Pestaña activa para el selector de colores
    var colorTabSeleccionada by remember { mutableIntStateOf(0) }

    ProveedorResaltadoAjustes(seccionDestino) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            BarraSuperiorPantalla(
                titulo = "Widgets de escritorio",
                idEtiqueta = "03.3",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = scrollState.value > 0,
                colorFondo = ColorAjustesFondo
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Personaliza la apariencia, bordes, opacidad y colores del widget de inicio")
                Spacer(Modifier.height(10.dp))

                // 1. Vista previa interactiva en vivo
                ComponenteGrupo(
                    etiqueta = "Vista previa en vivo",
                    idGrupo = "03.3.G1",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Simulación en tiempo real del aspecto en pantalla de inicio"
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Aspecto en pantalla de inicio",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = TextoPrincipal
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ColorAcento.copy(alpha = 0.15f))
                                    .clickable {
                                        haptica.tic()
                                        vistaBloqueadaEnPreview = !vistaBloqueadaEnPreview
                                    }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = if (vistaBloqueadaEnPreview) "Ver: Desbloqueado" else "Ver: Bloqueado",
                                    color = ColorAcento,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Fondo simulado para visualizar la transparencia
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF101216))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val formaWidget = RoundedCornerShape(ajustes.widgetCurvaturaEsquinasDp.dp)
                            val colorFondoWidget = Color(0xFF1A1815).copy(alpha = ajustes.widgetTransparenciaFondo.coerceIn(0f, 1f))
                            val grosorDp = ajustes.widgetGrosorBordeDp.dp

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(formaWidget)
                                    .background(colorFondoWidget)
                                    .then(
                                        if (ajustes.widgetGrosorBordeDp > 0.1f) {
                                            Modifier.border(
                                                grosorDp,
                                                colorBordeEfectivo.copy(alpha = (ajustes.widgetTransparenciaFondo.coerceAtLeast(0.6f))),
                                                formaWidget
                                            )
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .padding(12.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            Icons.Filled.Timer,
                                            contentDescription = null,
                                            tint = colorTituloIconoEfectivo,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            "Códigos 2FA",
                                            color = colorTituloIconoEfectivo,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(Modifier.height(10.dp))

                                    if (vistaBloqueadaEnPreview) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Color(0xFF26231E))
                                                .padding(horizontal = 14.dp, vertical = 12.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Filled.Lock,
                                                    contentDescription = null,
                                                    tint = ColorSeguridad,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(Modifier.width(8.dp))
                                                Text(
                                                    "Bóveda bloqueada",
                                                    color = TextoSecundario,
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                        }
                                    } else {
                                        val itemsEjemplo = listOf(
                                            Triple("GitHub", "jlnavas3", "482 190"),
                                            Triple("Google", "correo@gmail.com", "731 564"),
                                            Triple("AWS", "admin-root", "095 823")
                                        )
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            itemsEjemplo.forEach { (cuenta, usuario, codigo) ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(Color(0xFF26231E))
                                                        .padding(horizontal = 10.dp, vertical = 7.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            cuenta,
                                                            color = TextoPrincipal,
                                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                                        )
                                                        Text(
                                                            usuario,
                                                            color = TextoSecundario,
                                                            style = MaterialTheme.typography.labelSmall,
                                                            fontSize = 10.sp
                                                        )
                                                    }
                                                    Text(
                                                        codigo,
                                                        color = colorCodigoEfectivo,
                                                        style = MaterialTheme.typography.bodyMedium.copy(
                                                            fontFamily = FontFamily.Monospace,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                    )
                                                    Spacer(Modifier.width(8.dp))
                                                    IndicadorTotpTarta(
                                                        segundosRestantes = segundosRestantes,
                                                        periodo = 30L,
                                                        tamano = 16.dp,
                                                        colorPersonalizado = colorContadorEfectivo
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 2. Forma y Bordes
                ComponenteGrupo(
                    etiqueta = "Forma y bordes",
                    idGrupo = "03.3.G2",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Grosor de trazo exterior y radio de redondeo"
                ) {
                    ComponenteSlider(
                        titulo = "Grosor del borde",
                        valor = ajustes.widgetGrosorBordeDp,
                        valorTexto = if (ajustes.widgetGrosorBordeDp <= 0.1f) "0 dp" else "%.1f dp".format(ajustes.widgetGrosorBordeDp),
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarWidgetGrosorBorde(it)
                        },
                        rango = 0f..5f,
                        pasos = 49,
                        etiquetaMin = "Sin borde",
                        etiquetaMax = "5 dp",
                        idFila = "03.3.1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        icono = Icons.Filled.LineWeight,
                        colorIcono = ColorAcento
                    )

                    ComponenteSeparador()

                    ComponenteSlider(
                        titulo = "Radio de esquinas",
                        valor = ajustes.widgetCurvaturaEsquinasDp,
                        valorTexto = if (ajustes.widgetCurvaturaEsquinasDp <= 0.1f) "0 dp" else "%.0f dp".format(ajustes.widgetCurvaturaEsquinasDp),
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarWidgetCurvaturaEsquinas(it)
                        },
                        rango = 0f..32f,
                        pasos = 31,
                        etiquetaMin = "Recto",
                        etiquetaMax = "32 dp",
                        idFila = "03.3.2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        icono = Icons.Filled.CropSquare,
                        colorIcono = ColorAcento
                    )
                }

                Spacer(Modifier.height(16.dp))

                // 3. Transparencia del fondo
                ComponenteGrupo(
                    etiqueta = "Transparencia del fondo",
                    idGrupo = "03.3.G3",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Nivel de translucidez para combinar con tu fondo de pantalla"
                ) {
                    ComponenteSlider(
                        titulo = "Opacidad del fondo",
                        valor = ajustes.widgetTransparenciaFondo,
                        valorTexto = "${(ajustes.widgetTransparenciaFondo * 100).roundToInt()}%",
                        alCambiar = {
                            haptica.tic()
                            vm.ajustarWidgetTransparenciaFondo(it)
                        },
                        rango = 0f..1f,
                        pasos = 99,
                        etiquetaMin = "0%",
                        etiquetaMax = "100%",
                        idFila = "03.3.3",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        icono = Icons.Filled.Opacity,
                        colorIcono = ColorAcento
                    )
                }

                Spacer(Modifier.height(16.dp))

                // 4. Colores del widget
                val opcionesColor = listOf(
                    "Borde" to colorBordeEfectivo,
                    "Contador" to colorContadorEfectivo,
                    "Código 2FA" to colorCodigoEfectivo,
                    "Título" to colorTituloIconoEfectivo
                )

                ComponenteGrupo(
                    etiqueta = "Colores del widget",
                    idGrupo = "03.3.G4",
                    mostrarId = ajustes.mostrarIdsAjustes,
                    descripcion = "Personalización de los tonos de cada elemento visual"
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Selecciona un elemento para cambiar su color:",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall
                        )
                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        opcionesColor.forEachIndexed { idx, (nombre, color) ->
                            val seleccionado = colorTabSeleccionada == idx
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
                                        colorTabSeleccionada = idx
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

                    when (colorTabSeleccionada) {
                        0 -> {
                            SelectorColorEnTiempoReal(
                                colorInicial = colorBordeEfectivo,
                                titulo = "Color del Borde"
                            ) { nuevoColor ->
                                vm.ajustarWidgetColorBorde(nuevoColor.aHex())
                            }
                        }
                        1 -> {
                            SelectorColorEnTiempoReal(
                                colorInicial = colorContadorEfectivo,
                                titulo = "Color del Contador Circular"
                            ) { nuevoColor ->
                                vm.ajustarWidgetColorContador(nuevoColor.aHex())
                            }
                        }
                        2 -> {
                            SelectorColorEnTiempoReal(
                                colorInicial = colorCodigoEfectivo,
                                titulo = "Color del Código 2FA"
                            ) { nuevoColor ->
                                vm.ajustarWidgetColorCodigo(nuevoColor.aHex())
                            }
                        }
                        3 -> {
                            SelectorColorEnTiempoReal(
                                colorInicial = colorTituloIconoEfectivo,
                                titulo = "Color del Título e Ícono"
                            ) { nuevoColor ->
                                vm.ajustarWidgetColorTituloIcono(nuevoColor.aHex())
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // 5. Restablecer valores
            ComponenteGrupo {
                ComponenteBotonFila(
                    titulo = "Restablecer módulo",
                    alPulsar = {
                        haptica.exito()
                        vm.restablecerAjustesWidget()
                        vm.avisar("Ajustes del widget restablecidos")
                    }
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
}

@Composable
private fun IndicadorValor(texto: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(ColorAcento.copy(alpha = 0.15f))
            .border(1.dp, ColorAcento.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = texto,
            color = ColorAcento,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
