package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.EngranajesBoveda
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.componentes.aEngranajesConfig
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSelectorModal
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.OpcionSelectorModal
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorArgon2
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Pantalla inicial de bienvenida del flujo de onboarding.
 */
@Composable
fun PasoBienvenida(
    perfilSeleccionado: PerfilArgon2,
    ajustes: AjustesApp = AjustesApp(),
    alCambiarPerfil: (PerfilArgon2) -> Unit,
    alIniciarCreacion: () -> Unit
) {
    var mostrarModalGarantias by remember { mutableStateOf(false) }

    val opcionesArgon2 = remember {
        PerfilArgon2.entries.map { perfil ->
            OpcionSelectorModal(
                valor = perfil,
                etiquetaFila = perfil.titulo,
                etiquetaModal = perfil.titulo,
                descripcionModal = "${perfil.resumen}\n${perfil.detalle}",
                icono = Icons.Filled.Memory
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Animación mecánica de la bóveda (engranajes, puerta o estática según ajustes)
        when (ajustes.animacionDesbloqueo) {
            "engranajes" -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    EngranajesBoveda(
                        abierta = false,
                        modifier = Modifier.fillMaxSize(),
                        config = ajustes.aEngranajesConfig()
                    )
                }
            }
            "puerta" -> {
                PuertaBoveda(abierta = false, tamano = 175)
            }
            else -> {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas.coerceAtLeast(18.dp)))
                        .background(com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = com.jlnavas3.bovedalocal.ui.theme.ColorAcento,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        // Encabezado con insignia de seguridad
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(ColorAcento.copy(alpha = 0.12f))
                    .border(0.8.dp, ColorAcento.copy(alpha = 0.35f), RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(Menta)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "ARGON2ID · AES-256 · SIN CONEXIÓN",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.1.sp
                    ),
                    color = ColorAcento
                )
            }

            Text(
                text = "Bóveda Local",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                ),
                color = ColorTitulos,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Solo tú tienes la llave de acceso a tu información.",
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                color = TextoSecundario,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        // Tarjeta agrupada de Configuración y Privacidad estilo MagicOS / One UI
        ComponenteGrupo(
            etiqueta = "Configuración y Privacidad"
        ) {
            ComponenteSelectorModal(
                titulo = "Perfil Argon2id",
                valorSeleccionado = perfilSeleccionado,
                opciones = opcionesArgon2,
                alSeleccionar = alCambiarPerfil,
                icono = Icons.Filled.Memory,
                colorIcono = ColorArgon2,
                colorTinteIcono = ColorSobreAcento,
                descripcionModal = "Parámetros KDF para la derivación de tu clave maestra"
            )

            ComponenteSeparador()

            ComponenteBotonFila(
                titulo = "Garantías de privacidad y cifrado",
                alPulsar = { mostrarModalGarantias = true },
                icono = Icons.Filled.Shield,
                colorIcono = ColorSeguridad,
                colorTinteIcono = ColorSobreAcento,
                valorTexto = "Air-Gapped"
            )

            ComponenteSeparador()

            ComponenteBotonFila(
                titulo = "Crear mi bóveda",
                alPulsar = alIniciarCreacion,
                icono = Icons.Filled.VpnKey,
                colorIcono = ColorSeguridad,
                colorTinteIcono = Color.White
            )
        }
    }

    if (mostrarModalGarantias) {
        DialogoGarantiasSeguridad(alCerrar = { mostrarModalGarantias = false })
    }
}
