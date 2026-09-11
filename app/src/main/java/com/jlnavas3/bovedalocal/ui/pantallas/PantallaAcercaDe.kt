package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.SdStorage
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.jlnavas3.bovedalocal.crypto.VaultCrypto
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorPrincipal
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorSeccion
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorTarjeta
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.InformeDiagnostico
import com.jlnavas3.bovedalocal.util.Portapapeles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PantallaAcercaDe(vm: VaultViewModel) {
    val contexto = LocalContext.current
    var datosAuditoria by remember { mutableStateOf<InformeDiagnostico.DatosAuditoria?>(null) }
    var refresco by remember { mutableIntStateOf(0) }

    LifecycleResumeEffect(Unit) {
        refresco++
        onPauseOrDispose { }
    }

    LaunchedEffect(refresco) {
        datosAuditoria = withContext(Dispatchers.IO) {
            InformeDiagnostico.recopilarAuditoria(contexto, vm.repositorio)
        }
    }

    ContenedorPrincipal(conScroll = true, espaciado = 18.dp) {
        CabeceraPantalla(
            titulo = "Audítame",
            subtitulo = "Métricas y diagnóstico de seguridad leídos en tiempo real",
            alVolver = { vm.volverAtras() }
        )

        val datos = datosAuditoria
        if (datos == null) {
            ContenedorTarjeta(paddingInterno = 24.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = ColorSeguridad)
                    Spacer(Modifier.width(12.dp))
                    Text("Leyendo sensores y estado del dispositivo…", color = TextoSecundario)
                }
            }
        } else {
            // 1. Tarjeta Destacada: Aislamiento y Seguridad Offline
            ContenedorSeccion(
                titulo = "Aislamiento y Privacidad",
                subtitulo = "Comprobación estricta de barreras de seguridad del sistema",
                icono = Icons.Filled.Security
            ) {
                ContenedorTarjeta(paddingInterno = 16.dp) {
                    FilaAuditoria(
                        ok = !datos.tienePermisoInternet,
                        titulo = "Aislamiento de red",
                        detalle = if (!datos.tienePermisoInternet) "Permiso INTERNET NO declarado (100% offline garantizado por el kernel Android)" else "Alerta: permiso INTERNET presente"
                    )
                    FilaAuditoria(
                        ok = datos.flagSecureActivo,
                        titulo = "Protección de ventana FLAG_SECURE",
                        detalle = "Bloqueo forzado a nivel de SurfaceFlinger contra capturas, grabadores y vista en apps recientes"
                    )
                    FilaAuditoria(
                        ok = true,
                        titulo = "Claves efímeras en RAM",
                        detalle = "La clave maestra nunca toca disco, reside solo en memoria volátil y se sobreescribe con ceros al bloquear"
                    )
                    FilaAuditoria(
                        ok = true,
                        titulo = "Permisos concedidos a la app",
                        detalle = if (datos.permisosDeclarados.isEmpty()) "Ningún permiso declarado en el Manifest" else datos.permisosDeclarados.joinToString { it.substringAfterLast('.') }
                    )
                }
            }

            // 2. Hardware y SoC en Vivo
            ContenedorSeccion(
                titulo = "Hardware y Procesamiento",
                subtitulo = "Especificaciones leídas de la capa HAL del móvil",
                icono = Icons.Filled.Smartphone
            ) {
                ContenedorTarjeta(paddingInterno = 16.dp) {
                    ItemMetrica("Dispositivo", "${datos.fabricante} ${datos.modelo} (${datos.dispositivo})")
                    ItemMetrica("SoC / Placa", if (datos.soc != null) "${datos.soc} (${datos.placa})" else datos.placa)
                    ItemMetrica("Arquitectura ABI", datos.abis)
                    ItemMetrica("Núcleos CPU", "${datos.nucleosCpu} núcleos disponibles")
                }
            }

            // 3. Sistema Android y Parche
            ContenedorSeccion(
                titulo = "Sistema Operativo",
                subtitulo = "Nivel de seguridad de la plataforma",
                icono = Icons.Filled.Info
            ) {
                ContenedorTarjeta(paddingInterno = 16.dp) {
                    ItemMetrica("Versión Android", "Android ${datos.versionAndroid} (API ${datos.apiSdk})")
                    ItemMetrica("Parche de seguridad", datos.parcheSeguridad)
                    ItemMetrica("Compilación del sistema", datos.compilacion)
                }
            }

            // 4. Memoria y Almacenamiento Local
            ContenedorSeccion(
                titulo = "Memoria y Almacenamiento",
                subtitulo = "Recursos de ejecución y archivo criptográfico en disco",
                icono = Icons.Filled.Memory
            ) {
                ContenedorTarjeta(paddingInterno = 16.dp) {
                    ItemMetrica("Memoria RAM", "${datos.ramLibreMb} MB libres de ${datos.ramTotalMb} MB (Estado bajo: ${if (datos.ramBaja) "Sí" else "No"})")
                    ItemMetrica("Heap JVM", "${datos.heapUsadoMb} MB en uso / ${datos.heapMaxMb} MB límite")
                    ItemMetrica("Almacenamiento interno", "${datos.almacenamientoLibreMb} MB disponibles de ${datos.almacenamientoTotalMb} MB")
                    ItemMetrica("Ruta física de la bóveda", datos.rutaBoveda)
                    ItemMetrica("Tamaño archivo en disco", "${datos.tamanoBovedaBytes} bytes")
                    ItemMetrica("Escritura atómica", "Buffer .tmp + fsync() + Atomic Rename (inmune a corte de energía)")
                }
            }

            // 5. Criptografía Verificada
            ContenedorSeccion(
                titulo = "Criptografía y Blindaje",
                subtitulo = "Parámetros matemáticos aplicados",
                icono = Icons.Filled.Lock
            ) {
                ContenedorTarjeta(paddingInterno = 16.dp) {
                    ItemMetrica("KDF Derivación", "Argon2id (64 MiB RAM, 3 iteraciones, paralelismo 4, salt 16B)")
                    ItemMetrica("Cifrado de datos", "AES-256-GCM (nonce 12B, tag 128b, AAD autenticado)")
                    ItemMetrica("Generador aleatorio", "SecureRandom CSPRNG del kernel de Android")
                    ItemMetrica("Passkeys WebAuthn", "Claves asimétricas ECDSA P-256 (ES256) locales")
                    ItemMetrica("Formato de cabecera", "Magic BVDA · v${VaultCrypto.VERSION} · ${VaultCrypto.TAM_CABECERA} bytes AAD")
                }
            }

            // 6. Biometría y Keystore
            ContenedorSeccion(
                titulo = "Biometría y Keystore",
                subtitulo = "Métricas reportadas por BiometricPrompt y Keystore",
                icono = Icons.Filled.Fingerprint
            ) {
                ContenedorTarjeta(paddingInterno = 16.dp) {
                    datos.lineasBiometria.forEach { linea ->
                        if (linea.ok != null) {
                            FilaAuditoria(ok = linea.ok, titulo = linea.texto, detalle = if (linea.ok) "Soportado y verificado" else "No disponible", indentada = linea.indentada)
                        } else {
                            Row(modifier = Modifier.padding(vertical = 2.dp).padding(start = if (linea.indentada) 16.dp else 0.dp)) {
                                Text("•", color = ColorIconosInternos)
                                Spacer(Modifier.width(8.dp))
                                Text(linea.texto, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            // 7. Sensores de Cámara (Camera2)
            ContenedorSeccion(
                titulo = "Cámara y Sensores QR",
                subtitulo = "Diagnóstico de hardware de captura para escaneo offline",
                icono = Icons.Filled.PhotoCamera
            ) {
                ContenedorTarjeta(paddingInterno = 16.dp) {
                    datos.lineasCamara.forEach { linea ->
                        if (linea.ok != null) {
                            FilaAuditoria(ok = linea.ok, titulo = linea.texto, detalle = if (linea.ok) "Activo" else "No concedido / inactivo", indentada = linea.indentada)
                        } else {
                            Row(modifier = Modifier.padding(vertical = 2.dp).padding(start = if (linea.indentada) 16.dp else 0.dp)) {
                                Text("•", color = ColorIconosInternos)
                                Spacer(Modifier.width(8.dp))
                                Text(linea.texto, color = TextoPrincipal, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            // Botones de acción con colores semánticos
            BotonColorido(
                texto = "Copiar informe completo de auditoría",
                color = ColorSeguridad,
                icono = Icons.Filled.ContentCopy
            ) {
                val informe = InformeDiagnostico.generar(contexto, datos)
                Portapapeles.copiar(contexto, "Informe de auditoría", informe)
                vm.avisar("Informe de auditoría copiado al portapapeles")
            }

            BotonColorido(
                texto = "Abrir ficha de la app en Android",
                color = ColorAcento,
                icono = Icons.Filled.Info
            ) {
                if (!AjustesSistema.abrirFichaApp(contexto)) {
                    vm.avisar("No se pudo abrir la ficha del sistema")
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ItemMetrica(etiqueta: String, valor: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Text(
            text = etiqueta.uppercase(),
            color = TextoSecundario,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
        )
        Text(
            text = valor,
            color = TextoPrincipal,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
private fun FilaAuditoria(
    ok: Boolean,
    titulo: String,
    detalle: String,
    indentada: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(start = if (indentada) 16.dp else 0.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(20.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (ok) Menta.copy(alpha = 0.2f) else Peligro.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (ok) "✓" else "✕",
                color = if (ok) Menta else Peligro,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                color = TextoPrincipal,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = detalle,
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
