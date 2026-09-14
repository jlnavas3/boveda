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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBovedaDesplegable
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SelectorPerfilArgon2
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

@Composable
fun PasoBienvenida(
    perfilSeleccionado: PerfilArgon2,
    alCambiarPerfil: (PerfilArgon2) -> Unit,
    alIniciarCreacion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Rueda giratoria mecánica animada de la bóveda
        PuertaBoveda(abierta = false, tamano = 175)

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
                    text = "CÚSPIDE CRIPTOGRÁFICA · GRADO MILITAR",
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
                text = "Custodia soberana de tus contraseñas y secretos bajo la máxima arquitectura de cifrado simétrico existente, con resistencia post-cuántica y cero conocimiento.",
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                color = TextoSecundario,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        // Tarjeta de garantías de seguridad y transparencia (desplegable colapsada)
        TarjetaBovedaDesplegable(
            titulo = "Cúspide de Privacidad y Cifrado",
            descripcion = "Air-Gapped · Argon2id + AES-256 · Sin telemetría",
            icono = Icons.Filled.Shield,
            colorIcono = ColorSeguridad,
            inicialmenteAbierta = false
        ) {
            FilaPilarSeguridad(
                icono = Icons.Filled.WifiOff,
                titulo = "Cero conexión a Internet (Air-Gapped)",
                descripcion = "La aplicación carece por completo de permisos de red en el sistema operativo. Tus secretos jamás abandonan físicamente este dispositivo: no existen servidores remotos ni telemetría.",
                colorIcono = ColorAcento
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

            // Distintivo de garantía inferior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Menta.copy(alpha = 0.08f))
                    .border(0.8.dp, Menta.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
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
                    text = "100% Fuera de línea · Cero conocimiento · Inmunidad post-cuántica",
                    color = Menta,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }

        // Acciones
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SelectorPerfilArgon2(
                perfilActual = perfilSeleccionado,
                alSeleccionarPerfil = alCambiarPerfil
            )
            Spacer(Modifier.height(4.dp))
            BotonAmbar(
                texto = "Crear mi bóveda",
                icono = Icons.Filled.VpnKey
            ) {
                alIniciarCreacion()
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    tint = TextoSecundario.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Configurarás tu contraseña maestra en el siguiente paso",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoSecundario
                )
            }
        }
    }
}

@Composable
private fun FilaPilarSeguridad(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    colorIcono: Color = ColorAcento
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
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
