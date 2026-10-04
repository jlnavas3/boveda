package com.jlnavas3.bovedalocal.ui.pantallas.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.ui.componentes.BotonBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.ModalInferiorBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TamanoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoCuerpo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSobreAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Diálogo modal con estética Samsung One UI / Honor MagicOS y Paleta Sobria para mostrar
 * los pilares criptográficos de la aplicación respetando el tema visual global.
 */
@Composable
fun DialogoGarantiasSeguridad(alCerrar: () -> Unit) {
    ModalInferiorBoveda(
        abierto = true,
        alCerrar = alCerrar,
        fijarAbajo = false,
        titulo = "Privacidad y Cifrado",
        descripcion = "Air-Gapped · Argon2id + AES-256 · Sin telemetría",
        icono = Icons.Filled.Shield,
        colorIcono = ColorSobreAcento,
        fondoIcono = ColorAcento,
        mostrarBotonCerrar = false
    ) {
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
                descripcion = "La aplicación carece por completo de permisos de red en el sistema operativo. Tus secretos jamás abandonan físicamente este dispositivo: no existen servidores remotos ni telemetría."
            )

            FilaPilarSeguridad(
                icono = Icons.Filled.Lock,
                titulo = "Argon2id + AES-256-GCM (Estándar de Oro)",
                descripcion = "Cúspide del cifrado autenticado. Derivación de clave intensiva en memoria contra granjas GPU/ASIC y cifrado simétrico autenticado inmune al algoritmo cuántico de Grover."
            )

            FilaPilarSeguridad(
                icono = Icons.Filled.VerifiedUser,
                titulo = "Aislamiento y Transparencia Radical",
                descripcion = "Únicamente biometría de hardware para acceso instantáneo y cámara para escaneo local de códigos QR/2FA. Sin acceso a tus contactos, fotos, archivos personales ni ubicación."
            )

            Spacer(Modifier.height(4.dp))

            // Tarjeta de garantías sintetizada usando ContenedorTarjeta
            ContenedorTarjeta(
                colorFondo = ColorAjusteGris.copy(alpha = 0.08f),
                paddingInterno = 10.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(
                        "100% Offline",
                        "Zero-Knowledge",
                        "Post-Cuántica"
                    ).forEach { garantia ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = ColorAcento,
                                modifier = Modifier.size(14.dp)
                            )
                            TextoCuerpo(
                                texto = garantia,
                                tamano = TamanoCuerpo.MINI,
                                color = TextoPrincipal
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Botón principal de cierre con el componente oficial BotonBoveda
        BotonBoveda(
            texto = "Entendido",
            modifier = Modifier.fillMaxWidth(),
            alPulsar = alCerrar
        )
    }
}

/**
 * Fila visual de un pilar de seguridad con icono descriptivo, título y explicación.
 */
@Composable
private fun FilaPilarSeguridad(
    icono: ImageVector,
    titulo: String,
    descripcion: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        ContenedorIconoInsignia(
            icono = icono,
            tamano = TamanoInsignia.MEDIANO,
            colorFondo = ColorAjusteGris.copy(alpha = 0.15f),
            colorIcono = ColorIconosInternos,
            modifier = Modifier.padding(top = 2.dp)
        )
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            TextoTitulo(
                texto = titulo,
                estilo = EstiloTitulo.PEQUENO
            )
            Spacer(Modifier.height(2.dp))
            TextoCuerpo(
                texto = descripcion,
                tamano = TamanoCuerpo.PEQUENO,
                color = TextoSecundario
            )
        }
    }
}
