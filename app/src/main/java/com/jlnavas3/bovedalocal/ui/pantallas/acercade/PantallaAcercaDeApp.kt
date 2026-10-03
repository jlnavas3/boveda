package com.jlnavas3.bovedalocal.ui.pantallas.acercade

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.BuildConfig
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteFila
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteGrupo
import com.jlnavas3.bovedalocal.ui.componentes.ajustes.ComponenteSeparador
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Pantalla dedicada "Acerca de" que detalla la identidad de la aplicación,
 * versión, creador, filosofía offline y arquitectura criptográfica.
 */
@Composable
fun PantallaAcercaDeApp(
    alVolver: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Acerca de",
            alVolver = alVolver,
            conSeparador = scrollState.value > 0,
            colorFondo = ColorAjustesFondo
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Emblema e identidad principal
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(CurvaturaEsquinas.coerceAtLeast(18.dp)))
                    .background(ColorTarjetaAjustes),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(40.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Bóveda Local",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = ColorTitulos,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "v${BuildConfig.VERSION_NAME} · by: jlnavas3",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    ),
                    color = ColorAjusteGris,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Gestor de contraseñas, passkeys y datos confidenciales 100% local, zero-knowledge y soberano.",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                    color = TextoSecundario,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }

            // Grupo 1: Información de la Aplicación
            ComponenteGrupo(
                etiqueta = "Información de la aplicación"
            ) {
                ComponenteFila(
                    titulo = "Versión",
                    valorTexto = "v${BuildConfig.VERSION_NAME}",
                    icono = Icons.Filled.Info,
                    colorIcono = ColorAjusteGris.copy(alpha = 0.15f),
                    colorTinteIcono = ColorIconosInternos
                )

                ComponenteSeparador()

                ComponenteFila(
                    titulo = "Creador",
                    valorTexto = "jlnavas3",
                    icono = Icons.Filled.Code,
                    colorIcono = ColorAjusteGris.copy(alpha = 0.15f),
                    colorTinteIcono = ColorIconosInternos
                )

                ComponenteSeparador()

                ComponenteFila(
                    titulo = "Licencia",
                    valorTexto = "Local-First / Privado",
                    icono = Icons.Filled.Verified,
                    colorIcono = ColorAjusteGris.copy(alpha = 0.15f),
                    colorTinteIcono = ColorIconosInternos
                )
            }

            // Grupo 2: Filosofía y Privacidad
            ComponenteGrupo(
                etiqueta = "Filosofía y Privacidad"
            ) {
                ComponenteFila(
                    titulo = "100% Offline (Air-Gapped)",
                    subtitulo = "Sin conexión a internet ni permisos de red en el manifiesto Android",
                    icono = Icons.Filled.Security,
                    colorIcono = ColorAjusteGris.copy(alpha = 0.15f),
                    colorTinteIcono = ColorIconosInternos
                )

                ComponenteSeparador()

                ComponenteFila(
                    titulo = "Zero-Knowledge",
                    subtitulo = "Tus credenciales y datos maestros nunca salen de tu dispositivo",
                    icono = Icons.Filled.VisibilityOff,
                    colorIcono = ColorAjusteGris.copy(alpha = 0.15f),
                    colorTinteIcono = ColorIconosInternos
                )

                ComponenteSeparador()

                ComponenteFila(
                    titulo = "Sin telemetría",
                    subtitulo = "Cero rastreadores, analíticas o conexiones con servidores de terceros",
                    icono = Icons.Filled.Shield,
                    colorIcono = ColorAjusteGris.copy(alpha = 0.15f),
                    colorTinteIcono = ColorIconosInternos
                )
            }

            // Grupo 3: Arquitectura Criptográfica
            ComponenteGrupo(
                etiqueta = "Arquitectura Criptográfica"
            ) {
                ComponenteFila(
                    titulo = "Cifrado Simétrico",
                    subtitulo = "AES-256 en modo GCM con autenticación de integridad en cada bloque",
                    maxSubtituloLines = 3,
                    icono = Icons.Filled.Lock,
                    colorIcono = ColorAjusteGris.copy(alpha = 0.15f),
                    colorTinteIcono = ColorIconosInternos
                )

                ComponenteSeparador()

                ComponenteFila(
                    titulo = "Derivación de Clave",
                    subtitulo = "Argon2id de alta memoria contra ataques de fuerza bruta y GPU/ASIC",
                    icono = Icons.Filled.Memory,
                    colorIcono = ColorAjusteGris.copy(alpha = 0.15f),
                    colorTinteIcono = ColorIconosInternos
                )

                ComponenteSeparador()

                ComponenteFila(
                    titulo = "Almacenamiento Blindado",
                    subtitulo = "Base de datos Room cifrada y enclave criptográfico de hardware",
                    icono = Icons.Filled.VpnKey,
                    colorIcono = ColorAjusteGris.copy(alpha = 0.15f),
                    colorTinteIcono = ColorIconosInternos
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
