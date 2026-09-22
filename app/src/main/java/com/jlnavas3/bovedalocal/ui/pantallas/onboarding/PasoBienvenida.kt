package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorArgon2
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo

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
        // Animación mecánica de la bóveda (engranajes o puerta según ajustes)
        if (ajustes.animacionDesbloqueo == "engranajes") {
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
        } else {
            PuertaBoveda(abierta = false, tamano = 175)
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

/**
 * Diálogo modal centrado con estética Samsung One UI / Honor MagicOS para mostrar
 * los pilares criptográficos de la aplicación sin sobrecargar la pantalla inicial.
 */
@Composable
private fun DialogoGarantiasSeguridad(alCerrar: () -> Unit) {
    val esOscuro = esOscuroActivo
    val fondoModal = if (esOscuro) Color(0xFF222225) else Color.White

    Dialog(
        onDismissRequest = alCerrar,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { alCerrar() },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(fondoModal)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* Evita cerrar al pulsar dentro de la tarjeta */ }
                    .padding(horizontal = 20.dp, vertical = 22.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Cabecera del modal
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ColorSeguridad),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Shield,
                                contentDescription = null,
                                tint = ColorSobreAcento,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Privacidad y Cifrado",
                                color = TextoPrincipal,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.5.sp
                                )
                            )
                            Text(
                                text = "Air-Gapped · Argon2id + AES-256 · Sin telemetría",
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Lista de pilares de seguridad
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 380.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FilaPilarSeguridad(
                            icono = Icons.Filled.WifiOff,
                            titulo = "Cero conexión a Internet (Air-Gapped)",
                            descripcion = "La aplicación carece por completo de permisos de red en el sistema operativo. Tus secretos jamás abandonan físicamente este dispositivo: no existen servidores remotos ni telemetría.",
                            colorIcono = ColorIconosInternos
                        )

                        FilaPilarSeguridad(
                            icono = Icons.Filled.Lock,
                            titulo = "Argon2id + AES-256-GCM (Estándar de Oro)",
                            descripcion = "La cúspide mundial del cifrado autenticado de grado militar. Derivación de clave intensiva en memoria contra granjas de GPUs/ASICs y cifrado simétrico autenticado inmune al algoritmo cuántico de Grover.",
                            colorIcono = ColorSeguridad
                        )

                        FilaPilarSeguridad(
                            icono = Icons.Filled.VerifiedUser,
                            titulo = "Aislamiento y Transparencia Radical",
                            descripcion = "Únicamente biometría de hardware para acceso instantáneo y cámara para escaneo local de códigos QR/2FA. Sin acceso a tus contactos, fotos, archivos personales ni ubicación.",
                            colorIcono = Menta
                        )

                        Spacer(Modifier.height(4.dp))

                        // Distintivos de garantía inferior
                        val garantias = listOf(
                            "100% Fuera de línea",
                            "Cero conocimiento",
                            "Inmunidad post-cuántica"
                        )
                        garantias.forEach { garantia ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Menta.copy(alpha = 0.08f))
                                    .border(0.8.dp, Menta.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.CheckCircle,
                                    contentDescription = null,
                                    tint = Menta,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = garantia,
                                    color = Menta,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Botón de cierre
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = alCerrar) {
                            Text(
                                text = "Entendido",
                                color = ColorAcento,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaPilarSeguridad(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    colorIcono: Color = ColorIconosInternos
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colorIcono.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                color = ColorTitulos,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = descripcion,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 17.sp)
            )
        }
    }
}

