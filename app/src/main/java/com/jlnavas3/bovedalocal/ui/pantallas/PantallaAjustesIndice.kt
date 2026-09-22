package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.componentes.IndiceAlfabetico
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSlider
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSwitch
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ProveedorResaltadoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla dedicada a la configuración y personalización del abecedario lateral.
 * Dispone de la barra de letras real montada en el extremo derecho de la pantalla a altura completa,
 * permitiendo calibrar la ola, las escalas, el alcance, los tonos y la 'Ñ' con interacción táctil en vivo.
 */
@Composable
fun PantallaAjustesIndice(
    vm: VaultViewModel,
    seccionId: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    var letraArrastrada by remember { mutableStateOf<Char?>('G') }
    val scrollState = rememberScrollState()

    val mockItems = remember {
        listOf(
            "Amazon" to "Compras y suscripción",
            "Apple" to "ID de Apple y iCloud",
            "GitHub" to "Cuenta de desarrollo",
            "Google" to "admin@gmail.com",
            "Netflix" to "Suscripción familiar",
            "Ñandú" to "Cuenta de prueba en español",
            "Spotify" to "Música y podcasts",
            "Twitter" to "@usuario_boveda",
            "Zara" to "Moda y calzado"
        )
    }

    ProveedorResaltadoAjustes(seccionId) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorAjustesFondo)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                BarraSuperiorPantalla(
                    titulo = "Abecedario lateral",
                    idEtiqueta = "03.4",
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
                        .padding(start = 16.dp, end = 48.dp, top = 12.dp, bottom = 48.dp)
                ) {
                    DescripcionPantalla(subtitulo = "Personaliza la ola, escalas, háptica y apariencia")
                    Spacer(Modifier.height(10.dp))

                    // 1. Vista previa interactiva
                    ComponenteGrupo(
                        etiqueta = "Vista previa interactiva",
                        idGrupo = "03.4.G1",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        descripcion = "Desliza en el borde derecho para calibrar en tiempo real"
                    ) {
                        val primerIndiceCoincidente = remember(mockItems, letraArrastrada, ajustes.indiceIncluirEnie, ajustes.indiceResaltarEntradas, ajustes.indiceResaltarSoloPrimera) {
                            if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) null
                            else if (ajustes.indiceResaltarSoloPrimera) {
                                mockItems.indexOfFirst { (nombre, _) ->
                                    com.jlnavas3.bovedalocal.ui.componentes.letraInicialIndice(nombre, ajustes.indiceIncluirEnie) == letraArrastrada
                                }.takeIf { it >= 0 }
                            } else null
                        }

                        Column(modifier = Modifier.padding(14.dp)) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(FormaCampo)
                                    .background(Superficie)
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                mockItems.forEachIndexed { indice, (nombre, detalle) ->
                                    val coincide = if (!ajustes.indiceResaltarEntradas || letraArrastrada == null) {
                                        false
                                    } else if (ajustes.indiceResaltarSoloPrimera) {
                                        indice == primerIndiceCoincidente
                                    } else {
                                        com.jlnavas3.bovedalocal.ui.componentes.letraInicialIndice(nombre, ajustes.indiceIncluirEnie) == letraArrastrada
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(FormaPequena)
                                            .background(if (coincide) Ambar.copy(alpha = 0.18f) else SuperficieAlta)
                                            .then(
                                                if (coincide) Modifier.border(1.5.dp, Ambar, FormaPequena) else Modifier
                                            )
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(if (coincide) Ambar else Borde),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                nombre.take(1),
                                                color = if (coincide) ColorSobreAcento else TextoPrincipal,
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                        }
                                        Spacer(Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                nombre,
                                                color = if (coincide) Ambar else TextoPrincipal,
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                                maxLines = 1
                                            )
                                            Text(detalle, color = TextoSecundario, style = MaterialTheme.typography.labelSmall, maxLines = 1)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    // 2. Efecto de ola Niagara
                    val amplitud = ajustes.indiceAmplitudOlaDp
                    val textoAmplitud = when {
                        amplitud <= 0f -> "Recto (sin ola)"
                        amplitud < 45f -> "Curva sutil"
                        amplitud < 85f -> "Equilibrado"
                        amplitud <= 115f -> "Ola amplia (predeterminado)"
                        else -> "Super exagerado"
                    }
                    val radio = ajustes.indiceRadioOlaDp
                    val textoRadio = when {
                        radio < 140f -> "Concentrado"
                        radio < 220f -> "Arco medio"
                        radio <= 265f -> "Arco amplio (predeterminado)"
                        else -> "Abarca todo el abecedario"
                    }
                    val escala = ajustes.indiceEscalaLetras
                    val textoEscala = when {
                        escala <= 1.05f -> "Sin aumento"
                        escala < 1.45f -> "Crecimiento suave"
                        escala <= 1.8f -> "Letras destacadas (predeterminado)"
                        escala < 2.3f -> "Letras grandes en cresta"
                        else -> "Letras gigantescas"
                    }
                    val escalaStr = "%.1f".format(java.util.Locale.US, escala)

                    ComponenteGrupo(
                        etiqueta = "Efecto de ola Niagara",
                        idGrupo = "03.4.G2",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        descripcion = "Curvatura dinámica y escala que sigue el movimiento del dedo"
                    ) {
                        ComponenteSwitch(
                            titulo = "Activar ola interactiva",
                            icono = Icons.Filled.Animation,
                            colorIcono = Color(0xFF6A1B9A),
                            activo = ajustes.indiceEfectoOla,
                            idFila = "03.4.1",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = { haptica.tic(); vm.ajustarIndiceEfectoOla(it) }
                        )

                        if (ajustes.indiceEfectoOla) {
                            ComponenteSeparador()
                            ComponenteSlider(
                                titulo = "Amplitud de la curvatura",
                                valor = amplitud,
                                valorTexto = "${amplitud.toInt()} dp ($textoAmplitud)",
                                rango = 0f..130f,
                                idFila = "03.4.2",
                                mostrarId = ajustes.mostrarIdsAjustes,
                                alCambiar = { vm.ajustarIndiceAmplitudOlaDp(it) }
                            )

                            ComponenteSeparador()
                            ComponenteSlider(
                                titulo = "Alcance vertical",
                                valor = radio,
                                valorTexto = "${radio.toInt()} dp ($textoRadio)",
                                rango = 80f..300f,
                                idFila = "03.4.3",
                                mostrarId = ajustes.mostrarIdsAjustes,
                                alCambiar = { vm.ajustarIndiceRadioOlaDp(it) }
                            )

                            ComponenteSeparador()
                            ComponenteSlider(
                                titulo = "Aumento de letras en cresta",
                                valor = escala,
                                valorTexto = "${escalaStr}x ($textoEscala)",
                                rango = 1.0f..2.6f,
                                idFila = "03.4.4",
                                mostrarId = ajustes.mostrarIdsAjustes,
                                alCambiar = { vm.ajustarIndiceEscalaLetras(it) }
                            )
                        }

                        ComponenteSeparador()
                        ComponenteBotonFila(
                            titulo = "Restablecer grupo",
                            alPulsar = {
                                vm.ajustarIndiceEfectoOla(true)
                                vm.ajustarIndiceAmplitudOlaDp(109f)
                                vm.ajustarIndiceRadioOlaDp(169f)
                                vm.ajustarIndiceEscalaLetras(1.5f)
                            }
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    // 3. Círculo en la cresta
                    val tamanoCirculo = ajustes.indiceTamanoCirculoDp
                    val textoTamano = when {
                        tamanoCirculo < 65f -> "Compacto y discreto"
                        tamanoCirculo <= 85f -> "Estándar equilibrado (predeterminado)"
                        else -> "Grande y vistoso"
                    }
                    val offset = ajustes.indiceOffsetCirculoDp
                    val textoOffset = when {
                        offset < 80f -> "Cerca de la franja"
                        offset < 130f -> "Proyección flotante"
                        else -> "Casi a media pantalla"
                    }

                    ComponenteGrupo(
                        etiqueta = "Círculo en la cresta",
                        idGrupo = "03.4.G3",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        descripcion = "Muestra la letra activa proyectada hacia el centro"
                    ) {
                        ComponenteSwitch(
                            titulo = "Mostrar círculo en cresta",
                            icono = Icons.Filled.Circle,
                            colorIcono = Color(0xFF00ACC1),
                            activo = ajustes.indiceMostrarCirculo,
                            idFila = "03.4.5",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = { haptica.tic(); vm.ajustarIndiceMostrarCirculo(it) }
                        )

                        if (ajustes.indiceMostrarCirculo) {
                            ComponenteSeparador()
                            ComponenteSlider(
                                titulo = "Tamaño del círculo",
                                valor = tamanoCirculo,
                                valorTexto = "${tamanoCirculo.toInt()} dp ($textoTamano)",
                                rango = 50f..110f,
                                idFila = "03.4.6",
                                mostrarId = ajustes.mostrarIdsAjustes,
                                alCambiar = { vm.ajustarIndiceTamanoCirculoDp(it) }
                            )

                            ComponenteSeparador()
                            ComponenteSlider(
                                titulo = "Desplazamiento del círculo",
                                valor = offset,
                                valorTexto = "${offset.toInt()} dp ($textoOffset)",
                                rango = 50f..160f,
                                idFila = "03.4.7",
                                mostrarId = ajustes.mostrarIdsAjustes,
                                alCambiar = { vm.ajustarIndiceOffsetCirculoDp(it) }
                            )
                        }

                        ComponenteSeparador()
                        ComponenteBotonFila(
                            titulo = "Restablecer grupo",
                            alPulsar = {
                                vm.ajustarIndiceMostrarCirculo(true)
                                vm.ajustarIndiceTamanoCirculoDp(50f)
                                vm.ajustarIndiceOffsetCirculoDp(136f)
                            }
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    // 4. Tacto, háptica y contraste
                    val anchoTactil = ajustes.indiceAnchoTactilDp
                    val textoAnchoTactil = when {
                        anchoTactil < 35f -> "Estrecho (solo sobre letras)"
                        anchoTactil <= 55f -> "Estándar cómodo (predeterminado)"
                        anchoTactil < 75f -> "Área amplia"
                        else -> "Extremadamente amplio"
                    }
                    val tono = ajustes.indiceTonoLetras
                    val textoTono = if (esOscuroActivo) {
                        when {
                            tono < 25f -> "Muy tenue / discreto"
                            tono < 45f -> "Oscuro suave"
                            tono <= 65f -> "Equilibrado (predeterminado)"
                            tono < 85f -> "Claro y nítido"
                            else -> "Máximo brillo / blanco puro"
                        }
                    } else {
                        when {
                            tono < 25f -> "Muy tenue / discreto"
                            tono < 45f -> "Gris suave"
                            tono <= 65f -> "Equilibrado (predeterminado)"
                            tono < 85f -> "Oscuro y nítido"
                            else -> "Máximo contraste / negro definido"
                        }
                    }

                    ComponenteGrupo(
                        etiqueta = "Tacto, háptica y contraste",
                        idGrupo = "03.4.G4",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        descripcion = "Sensibilidad de arrastre, vibración y legibilidad"
                    ) {
                        ComponenteSwitch(
                            titulo = "Vibración háptica al deslizar",
                            icono = Icons.Filled.Vibration,
                            colorIcono = Color(0xFFE91E63),
                            activo = ajustes.indiceHaptica,
                            idFila = "03.4.8",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = { haptica.tic(); vm.ajustarIndiceHaptica(it) }
                        )

                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Zona táctil de arrastre",
                            valor = anchoTactil,
                            valorTexto = "${anchoTactil.toInt()} dp ($textoAnchoTactil)",
                            rango = 26f..90f,
                            idFila = "03.4.9",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = { vm.ajustarIndiceAnchoTactilDp(it) }
                        )

                        ComponenteSeparador()
                        ComponenteSlider(
                            titulo = "Tono y contraste de letras",
                            valor = tono,
                            valorTexto = "${tono.toInt()}% ($textoTono)",
                            rango = 10f..100f,
                            idFila = "03.4.10",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = { vm.ajustarIndiceTonoLetras(it) }
                        )

                        ComponenteSeparador()
                        ComponenteSwitch(
                            titulo = "Incluir letra Ñ",
                            icono = Icons.AutoMirrored.Filled.Sort,
                            colorIcono = Color(0xFF3F51B5),
                            activo = ajustes.indiceIncluirEnie,
                            idFila = "03.4.11",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = { haptica.tic(); vm.ajustarIndiceIncluirEnie(it) }
                        )

                        ComponenteSeparador()
                        ComponenteBotonFila(
                            titulo = "Restablecer grupo",
                            alPulsar = {
                                vm.ajustarIndiceHaptica(true)
                                vm.ajustarIndiceAnchoTactilDp(45f)
                                vm.ajustarIndiceTonoLetras(80f)
                                vm.ajustarIndiceIncluirEnie(true)
                            }
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    // 5. Resaltado de entradas al arrastrar
                    ComponenteGrupo(
                        etiqueta = "Resaltado al deslizar",
                        idGrupo = "03.4.G5",
                        mostrarId = ajustes.mostrarIdsAjustes,
                        descripcion = "Destaca visualmente las entradas de la letra activa"
                    ) {
                        ComponenteSwitch(
                            titulo = "Resaltar entradas al deslizar",
                            icono = Icons.Filled.Highlight,
                            colorIcono = ColorIconosInternos,
                            activo = ajustes.indiceResaltarEntradas,
                            idFila = "03.4.12",
                            mostrarId = ajustes.mostrarIdsAjustes,
                            alCambiar = { haptica.tic(); vm.ajustarIndiceResaltarEntradas(it) }
                        )

                        if (ajustes.indiceResaltarEntradas) {
                            ComponenteSeparador()
                            ComponenteSwitch(
                                titulo = "Resaltar solo la primera entrada",
                                icono = Icons.Filled.Visibility,
                                colorIcono = Color(0xFF43A047),
                                activo = ajustes.indiceResaltarSoloPrimera,
                                idFila = "03.4.13",
                                mostrarId = ajustes.mostrarIdsAjustes,
                                alCambiar = { haptica.tic(); vm.ajustarIndiceResaltarSoloPrimera(it) }
                            )
                        }

                        ComponenteSeparador()
                        ComponenteBotonFila(
                            titulo = "Restablecer grupo",
                            alPulsar = {
                                vm.ajustarIndiceResaltarEntradas(true)
                                vm.ajustarIndiceResaltarSoloPrimera(true)
                            }
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    ComponenteGrupo {
                        ComponenteBotonFila(
                            titulo = "Restablecer módulo",
                            alPulsar = {
                                vm.restablecerAjustesIndiceAlfabetico()
                            }
                        )
                    }

                    Spacer(Modifier.height(32.dp))
                }
            }

            // Barra lateral real en vivo
            IndiceAlfabetico(
                alSeleccionarLetra = { },
                alCambiarLetraActiva = { letraArrastrada = it },
                incluirEnie = ajustes.indiceIncluirEnie,
                efectoOla = ajustes.indiceEfectoOla,
                amplitudOlaDp = ajustes.indiceAmplitudOlaDp,
                radioOlaDp = ajustes.indiceRadioOlaDp,
                escalaMaximaLetras = ajustes.indiceEscalaLetras,
                mostrarCirculo = ajustes.indiceMostrarCirculo,
                tamanoCirculoDp = ajustes.indiceTamanoCirculoDp,
                offsetCirculoDp = ajustes.indiceOffsetCirculoDp,
                hapticaActiva = ajustes.indiceHaptica,
                anchoZonaTactilDp = ajustes.indiceAnchoTactilDp,
                tonoLetras = ajustes.indiceTonoLetras,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 150.dp, bottom = 100.dp)
                    .fillMaxHeight()
            )
        }
    }
}
