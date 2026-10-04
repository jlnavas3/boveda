package com.jlnavas3.bovedalocal.ui.componentes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay
import java.security.SecureRandom

private val GLIFOS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#\$%&*?/-_=+".toCharArray()
private val aleatorio = SecureRandom()

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
