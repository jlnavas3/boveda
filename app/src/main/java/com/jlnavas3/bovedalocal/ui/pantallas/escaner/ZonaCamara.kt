package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.camara.EstadoCamara
import com.jlnavas3.bovedalocal.camara.LectorQr
import com.jlnavas3.bovedalocal.camara.MotorCamara
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.FormaTarjeta
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.Obsidiana
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.Superficie
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Diagnostico
import java.util.concurrent.atomic.AtomicBoolean

/**
 * El visor con su cadena de motores: CameraX y, si falla y el ajuste es automático, el
 * compatible. Cuando los dos fallan, lo dice claro y ofrece leer el QR de una imagen.
 * [alLeer] devuelve false si el texto no valía, para seguir escaneando.
 */
@Composable
fun ZonaCamara(
    motorPreferido: MotorCamara,
    alLeer: (String, String) -> Boolean,
    alElegirImagen: () -> Unit
) {
    var motorActual by remember(motorPreferido) {
        mutableStateOf(if (motorPreferido == MotorCamara.COMPATIBLE) MotorCamara.COMPATIBLE else MotorCamara.CAMERAX)
    }
    var estado by remember(motorPreferido) { mutableStateOf<EstadoCamara>(EstadoCamara.Iniciando) }
    var falloCameraX by remember(motorPreferido) { mutableStateOf<EstadoCamara.Fallo?>(null) }
    val yaLeido = remember { AtomicBoolean(false) }
    val principal = remember { Handler(Looper.getMainLooper()) }
    val alLeerActual = rememberUpdatedState(alLeer)

    val alFrame: (ByteArray, Int, Int) -> Unit = { datos, ancho, alto ->
        if (!yaLeido.get()) {
            val texto = LectorQr.decodificarLuminancia(datos, ancho, alto, probarInvertido = true)
            if (texto != null && yaLeido.compareAndSet(false, true)) {
                val origen = "la cámara (${motorActual.clave})"
                principal.post {
                    val valido = alLeerActual.value(texto, origen)
                    if (!valido) principal.postDelayed({ yaLeido.set(false) }, 2_500)
                }
            }
        }
    }

    val alEstado: (EstadoCamara) -> Unit = { nuevo ->
        if (nuevo is EstadoCamara.Fallo && motorActual == MotorCamara.CAMERAX && motorPreferido == MotorCamara.AUTOMATICO) {
            Diagnostico.apuntar("camara", "CameraX falló; cambio al motor compatible")
            falloCameraX = nuevo
            estado = EstadoCamara.Iniciando
            motorActual = MotorCamara.COMPATIBLE
        } else {
            estado = nuevo
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(FormaTarjeta)
            .background(Superficie)
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaTarjeta)
                else Modifier
            )
    ) {
        if (estado !is EstadoCamara.Fallo) {
            key(motorActual) {
                when (motorActual) {
                    MotorCamara.COMPATIBLE -> VistaLegado(alFrame, alEstado)
                    else -> VistaCameraX(alFrame, alEstado)
                }
            }
        }

        when (val actual = estado) {
            EstadoCamara.Iniciando -> Aviso(
                if (falloCameraX != null) "CameraX no ha podido. Abriendo el motor compatible…" else "Abriendo la cámara…",
                Modifier.align(Alignment.BottomCenter)
            )
            is EstadoCamara.Funcionando -> if (!actual.conImagen) {
                Aviso("Esta cámara no da imagen en pantalla, pero está leyendo: apunta al QR igualmente.", Modifier.align(Alignment.Center))
            }
            is EstadoCamara.Fallo -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("La cámara de este móvil no responde", color = Peligro, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                Text(actual.motivo + ".", color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                falloCameraX?.let {
                    Text("Antes, CameraX: ${it.motivo}.", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                }
                Text(actual.detalle, color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(12.dp))
                Text(
                    "No pasa nada: lee el QR desde una captura de pantalla o escribe la clave a mano. Y si me mandas el informe de Audítame, lo miro.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(12.dp))
                BotonAmbar(
                    texto = "Leer el QR de una imagen",
                    icono = Icons.Filled.Image
                ) { alElegirImagen() }
            }
        }
    }
}

@Composable
fun Aviso(texto: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(12.dp)
            .clip(FormaPequena)
            .background(Obsidiana.copy(alpha = 0.72f))
            .then(
                if (GrosorBorde > 0.dp && ColorBordeActual != Color.Transparent)
                    Modifier.border(GrosorBorde, ColorBordeActual, FormaPequena)
                else Modifier
            )
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(texto, color = TextoPrincipal, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
    }
}
