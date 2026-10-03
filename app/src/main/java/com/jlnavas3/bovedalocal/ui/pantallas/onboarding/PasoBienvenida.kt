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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.BuildConfig
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.ui.componentes.BotonIconoCabecera
import com.jlnavas3.bovedalocal.ui.componentes.ElementoMenuCompacto
import com.jlnavas3.bovedalocal.ui.componentes.EngranajesBoveda
import com.jlnavas3.bovedalocal.ui.componentes.MenuDesplegableBoveda
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.componentes.SeparadorOpcionMenu
import com.jlnavas3.bovedalocal.ui.componentes.aEngranajesConfig
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteBotonFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Pantalla inicial de bienvenida del flujo de onboarding.
 */
@Composable
fun PasoBienvenida(
    ajustes: AjustesApp = AjustesApp(),
    alIniciarCreacion: () -> Unit,
    alAbrirAcercaDe: () -> Unit,
    alSalir: () -> Unit
) {
    var mostrarModalGarantias by remember { mutableStateOf(false) }
    var menuAbierto by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Cabecera superior limpia con menú de opciones contextuales
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                BotonIconoCabecera(
                    onClick = { menuAbierto = true },
                    icono = Icons.Default.MoreVert,
                    descripcion = "Más opciones"
                )

                MenuDesplegableBoveda(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false },
                    modifier = Modifier.widthIn(min = 220.dp, max = 280.dp)
                ) {
                    ElementoMenuCompacto(
                        texto = "Privacidad y cifrado",
                        icono = Icons.Filled.Shield,
                        colorIcono = ColorIconosInternos,
                        colorTexto = TextoPrincipal,
                        onClick = {
                            menuAbierto = false
                            mostrarModalGarantias = true
                        }
                    )

                    ElementoMenuCompacto(
                        texto = "Acerca de",
                        icono = Icons.Filled.Info,
                        colorIcono = ColorIconosInternos,
                        colorTexto = TextoPrincipal,
                        onClick = {
                            menuAbierto = false
                            alAbrirAcercaDe()
                        }
                    )

                    SeparadorOpcionMenu()
                    Spacer(Modifier.height(20.dp))
                    SeparadorOpcionMenu()

                    ElementoMenuCompacto(
                        texto = "Salir",
                        icono = Icons.AutoMirrored.Filled.ExitToApp,
                        colorIcono = Peligro,
                        colorTexto = Peligro,
                        onClick = {
                            menuAbierto = false
                            alSalir()
                        }
                    )
                }
            }
        }

        // Contenido con scroll
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
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
                            .clip(RoundedCornerShape(CurvaturaEsquinas.coerceAtLeast(18.dp)))
                            .background(ColorTarjetaAjustes),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            tint = ColorAcento,
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

            // Tarjeta agrupada de inicio de configuración estilo MagicOS / One UI
            ComponenteGrupo(
                etiqueta = "Iniciar configuración"
            ) {
                ComponenteBotonFila(
                    titulo = "Crear mi bóveda",
                    alPulsar = alIniciarCreacion,
                    icono = Icons.Filled.VpnKey,
                    colorIcono = ColorAcento,
                    colorTinteIcono = ColorSobreAcento
                )
            }

            Spacer(Modifier.height(18.dp))

            // Pie de página con versión y creador
            Text(
                text = "Bóveda Local · v${BuildConfig.VERSION_NAME} · by: jlnavas3",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                color = ColorAjusteGris.copy(alpha = 0.85f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }

    if (mostrarModalGarantias) {
        DialogoGarantiasSeguridad(alCerrar = { mostrarModalGarantias = false })
    }
}
