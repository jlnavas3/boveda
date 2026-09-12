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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.IndiceAlfabetico
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTarjetas
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.SuperficieAlta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

/**
 * Pantalla dedicada a la configuración y personalización del abecedario lateral.
 * Dispone de la barra de letras real montada en el extremo derecho de la pantalla a altura completa,
 * permitiendo calibrar la ola, las escalas, el alcance, los tonos y la 'Ñ' con interacción táctil en vivo.
 */
@Composable
fun PantallaAjustesIndice(vm: VaultViewModel) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsState()
    var letraArrastrada by remember { mutableStateOf<Char?>('G') }

    val mockItems = remember {
        listOf(
            "Amazon" to "Compras y suscripción",
            "Apple" to "ID de Apple y iCloud",
            "GitHub" to "Cuenta de desarrollo",
            "Google" to "admin@gmail.com",
            "Netflix" to "Suscripción familiar",
            "Ñandú" to "Cuenta de prueba en español",
            "Spotify" to "Música y podcasts",
            "Twitter" to "@usuario_pepo",
            "Zara" to "Moda y calzado"
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Contenido scrollable izquierdo y central
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 52.dp, top = 16.dp, bottom = 48.dp)
        ) {
            CabeceraPantalla(
                titulo = "Abecedario lateral",
                subtitulo = "Personaliza la ola, escalas, haptica y apariencia",
                alVolver = { vm.volverAtras() }
            )

            Spacer(Modifier.height(8.dp))

            // Tarjeta de demostración interactiva
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FormaTarjeta)
                    .background(SuperficieAlta)
                    .then(
                        if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                            Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
                        else Modifier
                    )
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "DEMO INTERACTIVA",
                        color = Ambar,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        "Desliza en el borde 👉",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaCampo)
                        .background(Superficie)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    mockItems.forEach { (nombre, detalle) ->
                        val primeraChar = com.jlnavas3.bovedalocal.ui.componentes.letraInicialIndice(nombre, ajustes.indiceIncluirEnie)
                        val coincide = letraArrastrada != null && primeraChar == letraArrastrada
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(FormaPequena)
                                .background(if (coincide) Ambar.copy(alpha = 0.18f) else SuperficieAlta)
                                .then(
                                    if (coincide) Modifier.border(1.5.dp, Ambar, FormaPequena) else Modifier
                                )
                                .padding(horizontal = 8.dp, vertical = 5.dp),
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

            Spacer(Modifier.height(18.dp))

            // 1. Efecto de ola Niagara
            FilaAjusteIndice(
                titulo = "Efecto de ola Niagara",
                descripcion = "Curvatura dinámica continua que sigue el movimiento del dedo.",
                activo = ajustes.indiceEfectoOla,
                alCambiar = { haptica.tic(); vm.ajustarIndiceEfectoOla(it) }
            )

            if (ajustes.indiceEfectoOla) {
                Spacer(Modifier.height(10.dp))
                val amplitud = ajustes.indiceAmplitudOlaDp
                val textoAmplitud = when {
                    amplitud <= 0f -> "Recto (sin ola)"
                    amplitud < 45f -> "Curva sutil"
                    amplitud < 85f -> "Equilibrado"
                    amplitud <= 115f -> "Ola amplia (predeterminado)"
                    else -> "Super exagerado"
                }
                SliderAjusteIndice(
                    titulo = "Amplitud de la curvatura: ${amplitud.toInt()} dp",
                    subtitulo = textoAmplitud,
                    valor = amplitud,
                    rango = 0f..130f,
                    alCambiar = { vm.ajustarIndiceAmplitudOlaDp(it) }
                )

                Spacer(Modifier.height(10.dp))
                val radio = ajustes.indiceRadioOlaDp
                val textoRadio = when {
                    radio < 140f -> "Concentrado"
                    radio < 220f -> "Arco medio"
                    radio <= 265f -> "Arco amplio (predeterminado)"
                    else -> "Abarca todo el abecedario"
                }
                SliderAjusteIndice(
                    titulo = "Alcance vertical: ${radio.toInt()} dp",
                    subtitulo = textoRadio,
                    valor = radio,
                    rango = 80f..300f,
                    alCambiar = { vm.ajustarIndiceRadioOlaDp(it) }
                )

                Spacer(Modifier.height(10.dp))
                val escala = ajustes.indiceEscalaLetras
                val textoEscala = when {
                    escala <= 1.05f -> "Sin aumento"
                    escala < 1.45f -> "Crecimiento suave"
                    escala <= 1.8f -> "Letras destacadas (predeterminado)"
                    escala < 2.3f -> "Letras grandes en cresta"
                    else -> "Letras gigantescas"
                }
                val escalaStr = "%.1f".format(java.util.Locale.US, escala)
                SliderAjusteIndice(
                    titulo = "Aumento de letras en la cresta: ${escalaStr}x",
                    subtitulo = textoEscala,
                    valor = escala,
                    rango = 1.0f..2.6f,
                    alCambiar = { vm.ajustarIndiceEscalaLetras(it) }
                )
            }

            Spacer(Modifier.height(14.dp))

            // 2. Círculo aumentado en la cresta
            FilaAjusteIndice(
                titulo = "Círculo aumentado en la cresta",
                descripcion = "Muestra la letra activa sin borde proyectada hacia el centro.",
                activo = ajustes.indiceMostrarCirculo,
                alCambiar = { haptica.tic(); vm.ajustarIndiceMostrarCirculo(it) }
            )

            if (ajustes.indiceMostrarCirculo) {
                Spacer(Modifier.height(10.dp))
                val offset = ajustes.indiceOffsetCirculoDp
                val textoOffset = when {
                    offset < 80f -> "Cerca de la franja"
                    offset < 130f -> "Proyección flotante"
                    else -> "Casi a media pantalla"
                }
                SliderAjusteIndice(
                    titulo = "Desplazamiento del círculo: ${offset.toInt()} dp",
                    subtitulo = textoOffset,
                    valor = offset,
                    rango = 50f..160f,
                    alCambiar = { vm.ajustarIndiceOffsetCirculoDp(it) }
                )
            }

            Spacer(Modifier.height(14.dp))

            // 3. Vibración háptica
            FilaAjusteIndice(
                titulo = "Vibración háptica al deslizar",
                descripcion = "Respuesta táctil sutil en cada letra seleccionada.",
                activo = ajustes.indiceHaptica,
                alCambiar = { haptica.tic(); vm.ajustarIndiceHaptica(it) }
            )

            Spacer(Modifier.height(10.dp))

            // 4. Zona táctil de arrastre
            val anchoTactil = ajustes.indiceAnchoTactilDp
            val textoAnchoTactil = when {
                anchoTactil < 35f -> "Estrecho (solo sobre letras)"
                anchoTactil <= 55f -> "Estándar cómodo (predeterminado)"
                anchoTactil < 75f -> "Área amplia"
                else -> "Extremadamente amplio"
            }
            SliderAjusteIndice(
                titulo = "Zona táctil de arrastre: ${anchoTactil.toInt()} dp",
                subtitulo = textoAnchoTactil,
                valor = anchoTactil,
                rango = 26f..90f,
                alCambiar = { vm.ajustarIndiceAnchoTactilDp(it) }
            )

            Spacer(Modifier.height(10.dp))

            // 5. Color y luminosidad de las letras
            val tono = ajustes.indiceTonoLetras
            val textoTono = when {
                tono < 25f -> "Muy oscuro / discreto"
                tono < 45f -> "Oscuro suave"
                tono <= 65f -> "Equilibrado (predeterminado)"
                tono < 85f -> "Claro y nítido"
                else -> "Máximo brillo / blanco puro"
            }
            SliderAjusteIndice(
                titulo = "Color de las letras: ${tono.toInt()}%",
                subtitulo = textoTono,
                valor = tono,
                rango = 10f..100f,
                alCambiar = { vm.ajustarIndiceTonoLetras(it) }
            )

            Spacer(Modifier.height(14.dp))

            // 6. Switch para la letra Ñ
            FilaAjusteIndice(
                titulo = "Incluir letra Ñ",
                descripcion = "Muestra la 'Ñ' en su posición alfabética en español (si se desactiva, se normaliza en la 'N').",
                activo = ajustes.indiceIncluirEnie,
                alCambiar = { haptica.tic(); vm.ajustarIndiceIncluirEnie(it) }
            )

            Spacer(Modifier.height(18.dp))

            // 7. Botón para restablecer valores por defecto
            BotonBorde(
                texto = "Restablecer valores por defecto",
                icono = Icons.Filled.Refresh,
                alPulsar = {
                    haptica.toque()
                    vm.restablecerAjustesIndiceAlfabetico()
                }
            )
        }

        // Barra lateral real en vivo montada en el extremo derecho a pantalla completa
        IndiceAlfabetico(
            alSeleccionarLetra = { },
            alCambiarLetraActiva = { letraArrastrada = it },
            incluirEnie = ajustes.indiceIncluirEnie,
            efectoOla = ajustes.indiceEfectoOla,
            amplitudOlaDp = ajustes.indiceAmplitudOlaDp,
            radioOlaDp = ajustes.indiceRadioOlaDp,
            escalaMaximaLetras = ajustes.indiceEscalaLetras,
            mostrarCirculo = ajustes.indiceMostrarCirculo,
            offsetCirculoDp = ajustes.indiceOffsetCirculoDp,
            hapticaActiva = ajustes.indiceHaptica,
            anchoZonaTactilDp = ajustes.indiceAnchoTactilDp,
            tonoLetras = ajustes.indiceTonoLetras,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
        )
    }
}

@Composable
private fun FilaAjusteIndice(
    titulo: String,
    descripcion: String,
    activo: Boolean,
    alCambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FormaTarjeta)
            .background(ColorTarjetas)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
                else Modifier
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(titulo, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
            Text(descripcion, color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
        }
        Switch(
            checked = activo,
            onCheckedChange = alCambiar,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ColorSobreAcento,
                checkedTrackColor = Ambar
            )
        )
    }
}

@Composable
private fun SliderAjusteIndice(
    titulo: String,
    subtitulo: String,
    valor: Float,
    rango: ClosedFloatingPointRange<Float>,
    alCambiar: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(FormaTarjeta)
            .background(ColorTarjetas)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
                else Modifier
            )
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(titulo, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
        Text(subtitulo, color = Ambar, style = MaterialTheme.typography.bodySmall)
        Slider(
            value = valor,
            onValueChange = alCambiar,
            valueRange = rango,
            colors = SliderDefaults.colors(
                thumbColor = Ambar,
                activeTrackColor = Ambar,
                inactiveTrackColor = Borde
            )
        )
    }
}
