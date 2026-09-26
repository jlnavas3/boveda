package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import java.security.SecureRandom

private val GLIFOS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#\$%&*?/-_=+".toCharArray()
private val aleatorio = SecureRandom()

val ColorDigitos = Color(0xFFFFB74D)

fun contrasenaColoreada(texto: String): AnnotatedString = buildAnnotatedString {
    texto.forEach { c ->
        val color = when {
            c.isDigit() -> ColorDigitos
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
