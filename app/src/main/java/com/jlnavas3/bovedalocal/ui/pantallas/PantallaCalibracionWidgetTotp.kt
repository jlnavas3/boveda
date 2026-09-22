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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.LineWeight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.jlnavas3.bovedalocal.ui.componentes.IndicadorTotpTarta
import com.jlnavas3.bovedalocal.ui.componentes.SelectorColorEnTiempoReal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.aHex
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun PantallaCalibracionWidgetTotp(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var vistaBloqueadaEnPreview by remember { mutableStateOf(false) }

    // Simulación de cuenta atrás para la vista previa en tiempo real
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

    var colorTabSeleccionada by remember { mutableIntStateOf(0) }

    ProveedorResaltadoAjustes(seccionDestino, scrollState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            // 1. Barra superior
            BarraSuperiorPantalla(
                titulo = "Calibración Códigos 2FA",
                idEtiqueta = "03.3.G1",
                mostrarId = ajustes.mostrarIdsAjustes,
                alVolver = { vm.volverAtras() },
                conSeparador = false,
                colorFondo = ColorAjustesFondo
            )

            // 2. Cabecera FLOTANTE fija: Vista previa interactiva en tiempo real sin títulos estorbosos
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF101216))
                        .padding(10.dp)
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
                            .padding(10.dp)
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
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "Códigos 2FA",
                                    color = colorTituloIconoEfectivo,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            if (vistaBloqueadaEnPreview) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF26231E))
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Filled.Lock,
                                            contentDescription = null,
                                            tint = ColorSeguridad,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(Modifier.width(6.dp))
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
                                    Triple("Google", "correo@gmail.com", "731 564")
                                )
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    itemsEjemplo.forEach { (cuenta, usuario, codigo) ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFF26231E))
                                                .padding(horizontal = 9.dp, vertical = 6.dp),
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
                                            Spacer(Modifier.width(6.dp))
                                            IndicadorTotpTarta(
                                                segundosRestantes = segundosRestantes,
                                                periodo = 30L,
                                                tamano = 15.dp,
                                                colorPersonalizado = colorContadorEfectivo
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Botón pequeño en la esquina para alternar bloqueado / desbloqueado en la previa
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.55f))
                            .clickable {
                                haptica.tic()
                                vistaBloqueadaEnPreview = !vistaBloqueadaEnPreview
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (vistaBloqueadaEnPreview) Icons.Filled.Lock else Icons.Filled.LockOpen,
                            contentDescription = if (vistaBloqueadaEnPreview) "Bloqueado" else "Desbloqueado",
                            tint = if (vistaBloqueadaEnPreview) ColorSeguridad else ColorAcento,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // 3. Controles desplazables que se deslizan por debajo de la vista previa fija
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Forma y bordes
                ComponenteGrupo(
                    etiqueta = "Forma y bordes",
                    idGrupo = "03.3.G1",
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
                        colorIcono = ColorIconosInternos
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
                        colorIcono = ColorIconosInternos
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Transparencia del fondo
                ComponenteGrupo(
                    etiqueta = "Transparencia del fondo",
                    idGrupo = "03.3.G2",
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
                        colorIcono = ColorIconosInternos
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Colores del widget
                val opcionesColor = listOf(
                    "Borde" to colorBordeEfectivo,
                    "Contador" to colorContadorEfectivo,
                    "Código 2FA" to colorCodigoEfectivo,
                    "Título" to colorTituloIconoEfectivo
                )

                ComponenteGrupo(
                    etiqueta = "Colores del widget",
                    idGrupo = "03.3.G3",
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

                // Restablecer aspecto
                ComponenteGrupo {
                    ComponenteBotonFila(
                        titulo = "Restablecer aspecto predeterminado",
                        icono = Icons.Filled.RotateLeft,
                        colorIcono = ColorIconosInternos,
                        alPulsar = {
                            haptica.exito()
                            vm.ajustarWidgetGrosorBorde(0f)
                            vm.ajustarWidgetCurvaturaEsquinas(0f)
                            vm.ajustarWidgetTransparenciaFondo(0.50f)
                            vm.ajustarWidgetColorBorde("#FFB300")
                            vm.ajustarWidgetColorContador("#FFFFFF")
                            vm.ajustarWidgetColorCodigo("#FFB300")
                            vm.ajustarWidgetColorTituloIcono("#FFFFFF")
                            vm.avisar("Aspecto del widget 2FA restablecido")
                        }
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
