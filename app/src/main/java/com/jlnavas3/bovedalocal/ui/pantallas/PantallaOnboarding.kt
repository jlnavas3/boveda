package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.jlnavas3.bovedalocal.crypto.PerfilArgon2
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraFuerza
import com.jlnavas3.bovedalocal.ui.componentes.BarraProgresoForja
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.CampoBoveda
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SelectorPerfilArgon2
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.MedidorFuerza

@Composable
fun PantallaOnboarding(vm: VaultViewModel, actividad: FragmentActivity) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes = vm.repositorio.ajustes.actual
    var perfilSeleccionado by remember {
        mutableStateOf(PerfilArgon2.desde(ajustes.perfilArgon2))
    }
    var paso by remember { mutableIntStateOf(0) }
    var contrasena by remember { mutableStateOf("") }
    var repetida by remember { mutableStateOf("") }
    var mostrar by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = paso,
        transitionSpec = {
            (scaleIn(spring(dampingRatio = 0.6f), initialScale = 0.94f) + fadeIn(spring(dampingRatio = 0.6f))) togetherWith
                fadeOut(spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium))
        },
        label = "onboarding"
    ) { actual ->
        when (actual) {
            0 -> Column(
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
                    // Píldora de estado de seguridad
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

                // Tarjeta de garantías de seguridad y transparencia
                ContenedorTarjeta(
                    paddingInterno = 18.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Shield,
                            contentDescription = null,
                            tint = ColorAcento,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "Cúspide de Privacidad y Cifrado",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = ColorTitulos
                        )
                    }

                    Spacer(Modifier.height(6.dp))

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
                        alSeleccionarPerfil = { nuevo ->
                            perfilSeleccionado = nuevo
                            vm.repositorio.ajustes.actualizar { it.copy(perfilArgon2 = nuevo.clave) }
                        }
                    )
                    Spacer(Modifier.height(4.dp))
                    BotonAmbar(
                        texto = "Crear mi bóveda",
                        icono = Icons.Filled.VpnKey
                    ) {
                        haptica.toque()
                        paso = 1
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

            1 -> {
                val fuerza = remember(contrasena) { MedidorFuerza.medir(contrasena) }
                val coinciden = contrasena.isNotEmpty() && contrasena == repetida
                val valida = contrasena.length >= 10 && coinciden
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Tu contraseña maestra", style = MaterialTheme.typography.headlineMedium, color = ColorTitulos)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Es la única llave de tu bóveda. Protegida por derivación criptográfica Argon2id: sin ella es matemáticamente imposible descifrar los datos. No se almacena en ningún sitio y no existe recuperación: si la pierdes, la bóveda se queda cerrada para siempre.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario
                    )
                    Spacer(Modifier.height(24.dp))
                    CampoBoveda(
                        valor = contrasena,
                        etiqueta = "Contraseña maestra",
                        alCambiar = { contrasena = it },
                        esContrasena = true,
                        mostrarContrasena = mostrar,
                        monoespaciada = true
                    )
                    Spacer(Modifier.height(12.dp))
                    CampoBoveda(
                        valor = repetida,
                        etiqueta = "Repítela",
                        alCambiar = { repetida = it },
                        esContrasena = true,
                        mostrarContrasena = mostrar,
                        monoespaciada = true
                    )
                    Spacer(Modifier.height(16.dp))
                    BarraFuerza(fuerza.fraccion, fuerza.etiqueta, fuerza.tiempo)
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            if (repetida.isEmpty()) "Mínimo 10 caracteres" else if (coinciden) "Las dos coinciden" else "No coinciden",
                            color = if (repetida.isNotEmpty() && !coinciden) MaterialTheme.colorScheme.error else TextoSecundario,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            if (mostrar) "Ocultar" else "Mostrar",
                            color = ColorAcento,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(start = 12.dp)
                                .clickable { mostrar = !mostrar }
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    SelectorPerfilArgon2(
                        perfilActual = perfilSeleccionado,
                        alSeleccionarPerfil = { nuevo ->
                            perfilSeleccionado = nuevo
                            vm.repositorio.ajustes.actualizar { it.copy(perfilArgon2 = nuevo.clave) }
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                    BotonAmbar("Forjar la bóveda", activo = valida, icono = Icons.Filled.Shield) {
                        haptica.toque()
                        paso = 2
                    }
                    Spacer(Modifier.height(12.dp))
                    BotonBorde("Volver", icono = Icons.AutoMirrored.Filled.ArrowBack) { paso = 0 }
                }
            }

            else -> {
                LaunchedEffect(Unit) {
                    vm.crearBoveda(contrasena) { haptica.exito() }
                }
                val perfil = com.jlnavas3.bovedalocal.crypto.PerfilArgon2.desde(vm.repositorio.ajustes.actual.perfilArgon2)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    PuertaBoveda(abierta = false, tamano = 210)
                    Spacer(Modifier.height(32.dp))
                    Text("Forjando tu bóveda", style = MaterialTheme.typography.headlineSmall, color = TextoPrincipal)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Argon2id está derivando tu clave con ${perfil.resumen}. Esta barrera intensiva de cálculo y memoria hace que un ataque por fuerza bruta sea computacional y físicamente inviable.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoSecundario,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(28.dp))
                    BarraProgresoForja()
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
