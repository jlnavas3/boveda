@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.jlnavas3.bovedalocal.ui.componentes.ajustes

import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoActivo
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoDuracionMs
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoIntensidad
import com.jlnavas3.bovedalocal.ui.theme.AlumbradoRepeticiones
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import kotlinx.coroutines.delay

@Composable
fun recordarEstadoAlumbrado(idFila: String?): EstadoAlumbradoFila {
    val requester = remember { BringIntoViewRequester() }
    val estado = remember(idFila) { EstadoAlumbradoFila(idFila, requester) }
    val destinoActual = LocalDestinoHighlight.current
    val coordinador = LocalCoordinadorResaltado.current
    val colorAcento = ColorAcento

    LaunchedEffect(destinoActual, idFila) {
        if (!idFila.isNullOrBlank() && !destinoActual.isNullOrBlank() && coordinador == null) {
            val coincide = (idFila == destinoActual) ||
                (destinoActual == "03.2.G2" && idFila == "03.2.6") ||
                ((destinoActual == "02-APA-THM-G04" || destinoActual == "02-APA-THM-DAT" || destinoActual == "02-APA-THM-G02") &&
                 (idFila == "02-APA-THM-G04" || idFila == "02-APA-THM-DAT" || idFila == "02-APA-THM-G02")) ||
                (destinoActual == "05.1.G2" && idFila == "05.1.6") ||
                (destinoActual == "05-COP-MAN-G02" && idFila == "05-COP-MAN")
            if (coincide) {
                delay(120)
                try {
                    estado.bringIntoViewRequester.bringIntoView()
                } catch (_: Exception) {}
                estado.dispararEfectoAlumbrado(
                    colorAcento = colorAcento,
                    activo = AlumbradoActivo,
                    intensidad = AlumbradoIntensidad,
                    repeticiones = AlumbradoRepeticiones,
                    duracionMs = AlumbradoDuracionMs
                )
            }
        }
    }

    return estado
}
