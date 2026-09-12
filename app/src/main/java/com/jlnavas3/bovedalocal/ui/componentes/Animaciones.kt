package com.jlnavas3.bovedalocal.ui.componentes

import android.os.SystemClock
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.AmbarFuerte
import com.jlnavas3.bovedalocal.ui.theme.Borde
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.security.SecureRandom

private val GLIFOS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#\$%&*?/-_=+".toCharArray()
private val aleatorio = SecureRandom()

fun contrasenaColoreada(texto: String): AnnotatedString = buildAnnotatedString {
    texto.forEach { c ->
        val color = when {
            c.isDigit() -> Ambar
            c.isLetter() -> TextoPrincipal
            else -> Menta
        }
        withStyle(SpanStyle(color = color)) { append(c) }
    }
}

@Composable
fun ContrasenaSlotMachine(
    objetivo: String,
    generacion: Int,
    haptica: Haptica?,
    modifier: Modifier = Modifier
) {
    var mostrado by remember { mutableStateOf(objetivo) }
    LaunchedEffect(generacion, objetivo) {
        if (objetivo.isEmpty()) {
            mostrado = ""
            return@LaunchedEffect
        }
        val n = objetivo.length
        val buffer = CharArray(n) { GLIFOS[aleatorio.nextInt(GLIFOS.size)] }
        val fijados = BooleanArray(n)
        var siguiente = 0
        val duracionTotal = 420L
        val inicio = System.currentTimeMillis()
        while (siguiente < n) {
            val transcurrido = System.currentTimeMillis() - inicio
            val objetivoFijados = ((transcurrido.toFloat() / duracionTotal) * n).toInt().coerceAtMost(n)
            while (siguiente < objetivoFijados) {
                buffer[siguiente] = objetivo[siguiente]
                fijados[siguiente] = true
                siguiente++
                haptica?.tic()
            }
            for (i in 0 until n) {
                if (!fijados[i]) buffer[i] = GLIFOS[aleatorio.nextInt(GLIFOS.size)]
            }
            mostrado = String(buffer)
            delay(26)
        }
        mostrado = objetivo
    }
    Text(
        text = contrasenaColoreada(mostrado),
        style = if (objetivo.length > 28) EstiloMono else EstiloMonoGrande,
        modifier = modifier
    )
}

@Composable
fun AnilloTotp(
    codigo: String,
    segundosRestantes: Long,
    tamano: Int = 92,
    periodo: Long = Totp.PERIODO_SEGUNDOS
) {
    val objetivo = (segundosRestantes.toFloat() / periodo).coerceIn(0f, 1f)
    // El vaciado va continuo, no a saltos de un segundo. Cuando el ciclo se reinicia
    // (la fraccion sube) el anillo salta al maximo de golpe: rellenarse despacio
    // se veria al reves de lo que pasa.
    val animada = remember { Animatable(objetivo) }
    LaunchedEffect(objetivo) {
        if (objetivo > animada.value) {
            animada.snapTo(objetivo)
        } else {
            animada.animateTo(objetivo, tween(durationMillis = 1000, easing = LinearEasing))
        }
    }
    val fraccion = animada.value
    val color by animateColorAsState(
        targetValue = when {
            segundosRestantes <= 5 -> Peligro
            segundosRestantes <= 10 -> AmbarFuerte
            else -> Ambar
        },
        animationSpec = spring(dampingRatio = 0.7f),
        label = "colorAnillo"
    )
    Box(modifier = Modifier.size(tamano.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(tamano.dp)) {
            // Grosor proporcional: en 26dp un trazo de 7dp se come el círculo.
            val grosor = (tamano * 0.09f).coerceIn(2.5f, 7f).dp.toPx()
            drawArc(
                color = Borde,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(grosor / 2, grosor / 2),
                size = Size(size.width - grosor, size.height - grosor),
                style = Stroke(width = grosor)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * fraccion,
                useCenter = false,
                topLeft = Offset(grosor / 2, grosor / 2),
                size = Size(size.width - grosor, size.height - grosor),
                style = Stroke(width = grosor, cap = StrokeCap.Round)
            )
        }
        if (codigo.isBlank()) {
            // Anillo suelto (la lista): dentro va la cuenta atrás, que es lo único
            // que hay que leer. El texto se escala al círculo para que quepa.
            Text(
                text = segundosRestantes.toString(),
                fontSize = (tamano * 0.42f).sp,
                fontWeight = FontWeight.Bold,
                color = color,
                lineHeight = (tamano * 0.42f).sp
            )
            return@Box
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                codigo.forEachIndexed { indice, digito ->
                    AnimatedContent(
                        targetState = digito,
                        transitionSpec = {
                            (slideInVertically { alto -> alto } togetherWith slideOutVertically { alto -> -alto })
                        },
                        label = "digito$indice"
                    ) { valor ->
                        Text(text = valor.toString(), style = EstiloMono, color = TextoPrincipal)
                    }
                }
            }
            Text("${segundosRestantes}s", style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
        }
    }
}

/** Puerta de bóveda: círculos concéntricos que rotan dinámicamente y se abren al desbloquear. */
@Composable
fun PuertaBoveda(abierta: Boolean, modifier: Modifier = Modifier, tamano: Int = 200) {
    var progreso by remember { mutableFloatStateOf(0f) }
    var tiempoSegundos by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        val inicio = SystemClock.uptimeMillis()
        while (isActive) {
            withFrameMillis { frameTime ->
                tiempoSegundos = (frameTime - inicio) / 1000f
            }
        }
    }

    LaunchedEffect(abierta) {
        if (abierta) {
            val inicio = SystemClock.uptimeMillis()
            val duracion = 450f
            while (isActive) {
                withFrameMillis { ahora ->
                    val t = ((ahora - inicio) / duracion).coerceIn(0f, 1f)
                    val factor = 1f - t
                    progreso = 1f - factor * factor * factor
                }
                if (progreso >= 1f) break
            }
        } else {
            progreso = 0f
        }
    }

    Box(modifier = modifier.size(tamano.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(tamano.dp)) {
            val centro = Offset(size.width / 2, size.height / 2)
            val radioBase = size.minDimension / 2
            val p = progreso

            // Velocidades angulares individuales continuas en grados por segundo (°/s)
            val velocidadesGrados = listOf(80f, -105f, 135f)
            val barridos = listOf(260f, 220f, 180f)
            val grosores = listOf(7.5.dp.toPx(), 5.5.dp.toPx(), 4.dp.toPx())

            for (anillo in 0..2) {
                val separacion = p * (anillo + 1) * radioBase * 0.18f
                val radio = radioBase * (0.92f - anillo * 0.22f) - separacion
                if (radio <= 0f) continue

                val anguloBase = (tiempoSegundos * velocidadesGrados[anillo]) + p * 180f
                val alpha = (1f - p * 0.55f).coerceIn(0f, 1f)

                rotate(degrees = anguloBase, pivot = centro) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Ambar.copy(alpha = alpha),
                                AmbarFuerte.copy(alpha = alpha * 0.6f),
                                Color(0xFFFFD54F).copy(alpha = alpha),
                                Ambar.copy(alpha = alpha)
                            ),
                            center = centro
                        ),
                        startAngle = 0f,
                        sweepAngle = barridos[anillo],
                        useCenter = false,
                        topLeft = Offset(centro.x - radio, centro.y - radio),
                        size = Size(radio * 2, radio * 2),
                        style = Stroke(width = grosores[anillo], cap = StrokeCap.Round)
                    )

                    // Perno o muesca de seguridad en el hueco del anillo para realzar el giro
                    val radioPerno = (3.dp.toPx() - anillo * 0.5f).coerceAtLeast(1.5f)
                    val anguloPernoRad = Math.toRadians((barridos[anillo] + 50.0))
                    val pernoX = centro.x + radio * kotlin.math.cos(anguloPernoRad).toFloat()
                    val pernoY = centro.y + radio * kotlin.math.sin(anguloPernoRad).toFloat()
                    drawCircle(
                        color = Ambar.copy(alpha = alpha * 0.8f),
                        radius = radioPerno,
                        center = Offset(pernoX, pernoY)
                    )
                }
            }

            // Núcleo central: Rueda/manivela de la bóveda
            val radioNucleo = radioBase * 0.22f * (1f + p * 0.35f)
            val rotacionNucleo = (tiempoSegundos * 45f) + p * 90f

            rotate(degrees = rotacionNucleo, pivot = centro) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFFD54F), AmbarFuerte),
                        center = centro,
                        radius = radioNucleo
                    ),
                    radius = radioNucleo,
                    center = centro,
                    alpha = (1f - p * 0.2f).coerceIn(0f, 1f)
                )
                drawCircle(
                    color = Color(0xFF1E232E),
                    radius = radioNucleo * 0.52f,
                    center = centro
                )
                for (radioIdx in 0..2) {
                    val radAngulo = Math.toRadians(radioIdx * 120.0)
                    val finX = centro.x + (radioNucleo * 0.88f) * kotlin.math.cos(radAngulo).toFloat()
                    val finY = centro.y + (radioNucleo * 0.88f) * kotlin.math.sin(radAngulo).toFloat()
                    drawLine(
                        color = Ambar,
                        start = centro,
                        end = Offset(finX, finY),
                        strokeWidth = 2.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Composable
fun IlustracionVacio(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Canvas(modifier = Modifier.size(160.dp)) {
            val centro = Offset(size.width / 2, size.height / 2)
            drawCircle(color = Borde, radius = size.minDimension / 2.2f, center = centro, style = Stroke(width = 6.dp.toPx()))
            drawCircle(
                brush = Brush.linearGradient(listOf(Ambar, AmbarFuerte)),
                radius = size.minDimension / 7f,
                center = centro.copy(y = centro.y - size.minDimension / 14f),
                style = Stroke(width = 8.dp.toPx())
            )
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Ambar, AmbarFuerte)),
                topLeft = Offset(centro.x - size.minDimension / 26f, centro.y + size.minDimension / 30f),
                size = Size(size.minDimension / 13f, size.minDimension / 4.5f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f, 12f)
            )
        }
        Text(
            "Tu bóveda está vacía",
            style = MaterialTheme.typography.headlineSmall,
            color = TextoPrincipal,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            "Guarda tu primera contraseña con el botón de abajo. Nada saldrá de este teléfono.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario,
            modifier = Modifier.padding(top = 6.dp, start = 24.dp, end = 24.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun BarraProgresoForja(modifier: Modifier = Modifier) {
    val transicion = rememberInfiniteTransition(label = "forja")
    val fase by transicion.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1_400, easing = LinearEasing)),
        label = "fase"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(8.dp)) {
            drawRoundRect(color = Borde, cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f))
            val ancho = size.width * 0.35f
            val x = (size.width + ancho) * fase - ancho
            drawRoundRect(
                brush = Brush.horizontalGradient(listOf(Color.Transparent, Ambar, AmbarFuerte, Color.Transparent)),
                topLeft = Offset(x, 0f),
                size = Size(ancho, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
            )
        }
    }
}
