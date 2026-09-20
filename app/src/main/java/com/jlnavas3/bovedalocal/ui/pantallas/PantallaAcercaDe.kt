package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.background
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
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.VaultCrypto
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.BarraSuperiorPantalla
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.DescripcionPantalla
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjustesFondo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.SeparadorFilaSimple
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorSeguridad
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.AjustesSistema
import com.jlnavas3.bovedalocal.util.Haptica
import com.jlnavas3.bovedalocal.util.InformeDiagnostico
import com.jlnavas3.bovedalocal.util.Portapapeles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun PantallaAcercaDe(
    vm: VaultViewModel,
    seccionDestino: String? = null
) {
    val contexto = LocalContext.current
    val haptica = remember { Haptica(contexto) }
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var datosAuditoria by remember { mutableStateOf<InformeDiagnostico.DatosAuditoria?>(null) }
    var refresco by remember { mutableIntStateOf(0) }

    val reqAislamiento = remember { BringIntoViewRequester() }
    val reqHardware = remember { BringIntoViewRequester() }
    val reqMemoria = remember { BringIntoViewRequester() }
    val reqCripto = remember { BringIntoViewRequester() }

    LifecycleResumeEffect(Unit) {
        refresco++
        onPauseOrDispose { }
    }

    LaunchedEffect(refresco) {
        datosAuditoria = withContext(Dispatchers.IO) {
            InformeDiagnostico.recopilarAuditoria(contexto, vm.repositorio)
        }
    }

    LaunchedEffect(seccionDestino, datosAuditoria) {
        if (seccionDestino != null && datosAuditoria != null) {
            when {
                seccionDestino == "11.3.1.1" -> reqAislamiento.bringIntoView()
                seccionDestino == "11.3.1.2" || seccionDestino == "11.3.1.3" -> reqHardware.bringIntoView()
                seccionDestino == "11.3.1.4" -> reqMemoria.bringIntoView()
                seccionDestino == "11.3.1.5" -> reqCripto.bringIntoView()
                seccionDestino.startsWith("11.3.1.") && seccionDestino != "11.3.1" -> reqAislamiento.bringIntoView()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorAjustesFondo)
    ) {
        BarraSuperiorPantalla(
            titulo = "Acerca de y diagnóstico",
            alVolver = { vm.volverAtras() },
            conSeparador = scrollState.value > 0,
            colorFondo = ColorAjustesFondo
        )

        val datos = datosAuditoria
        if (datos == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = ColorSeguridad)
                    Spacer(Modifier.width(14.dp))
                    Text("Leyendo auditoría y estado del hardware…", color = TextoSecundario)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                DescripcionPantalla(subtitulo = "Arquitectura 100% offline, parámetros criptográficos y estado del dispositivo")
                Spacer(Modifier.height(10.dp))

                // 1. Aislamiento y Privacidad
                GrupoAjustes(
                    etiqueta = "Aislamiento y privacidad",
                    modifier = Modifier.bringIntoViewRequester(reqAislamiento)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        FilaAuditoria(
                            ok = !datos.tienePermisoInternet,
                            titulo = "Aislamiento de red total",
                            detalle = if (!datos.tienePermisoInternet) "Permiso INTERNET NO declarado (100% offline garantizado por el kernel Android)" else "Alerta: permiso INTERNET presente"
                        )
                        Spacer(Modifier.height(8.dp))
                        SeparadorFilaSimple()
                        Spacer(Modifier.height(8.dp))

                        FilaAuditoria(
                            ok = datos.flagSecureActivo,
                            titulo = "Protección de ventana FLAG_SECURE",
                            detalle = if (datos.flagSecureActivo)
                                "Activa: capturas, grabaciones de pantalla y vista en apps recientes bloqueadas"
                            else
                                "Permitida por el usuario en Ajustes. Capturas de pantalla habilitadas"
                        )
                        Spacer(Modifier.height(8.dp))
                        SeparadorFilaSimple()
                        Spacer(Modifier.height(8.dp))

                        FilaAuditoria(
                            ok = true,
                            titulo = "Claves volátiles en memoria RAM",
                            detalle = "La clave maestra nunca toca disco, reside solo en RAM volátil y se sanitiza con ceros (zeroizing) al bloquear"
                        )
                        Spacer(Modifier.height(8.dp))
                        SeparadorFilaSimple()
                        Spacer(Modifier.height(8.dp))

                        FilaAuditoria(
                            ok = true,
                            titulo = "Permisos del sistema",
                            detalle = if (datos.permisosDeclarados.isEmpty()) "Ningún permiso peligroso declarado en el Manifest" else datos.permisosDeclarados.joinToString { it.substringAfterLast('.') }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 2. Criptografía y Blindaje
                GrupoAjustes(
                    etiqueta = "Criptografía y blindaje",
                    modifier = Modifier.bringIntoViewRequester(reqCripto)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ItemMetrica("KDF Derivación de clave", "Argon2id · ${datos.perfilArgon2.titulo}")
                        ItemMetrica("Parámetros KDF", "${datos.kdfParams.memoryKiB / 1024} MiB RAM, ${datos.kdfParams.iterations} pasadas, paralelismo ${datos.kdfParams.parallelism}")
                        ItemMetrica("Cifrado autenticado", "AES-256-GCM (nonce 12B, tag 128b, AAD autenticado)")
                        ItemMetrica("Generador aleatorio", "SecureRandom CSPRNG del kernel de Linux")
                        ItemMetrica("Passkeys WebAuthn", "Claves asimétricas ECDSA P-256 (ES256) locales")
                        ItemMetrica("Formato de cabecera", "Magic BVDA · v${VaultCrypto.VERSION} · ${VaultCrypto.TAM_CABECERA} bytes AAD")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 3. Hardware y Sistema
                GrupoAjustes(
                    etiqueta = "Hardware y sistema operativo",
                    modifier = Modifier.bringIntoViewRequester(reqHardware)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ItemMetrica("Dispositivo", "${datos.fabricante} ${datos.modelo} (${datos.dispositivo})")
                        ItemMetrica("SoC / Procesador", if (datos.soc != null) "${datos.soc} (${datos.placa})" else datos.placa)
                        ItemMetrica("Arquitectura CPU", "${datos.abis} · ${datos.nucleosCpu} núcleos")
                        ItemMetrica("Versión Android", "Android ${datos.versionAndroid} (API ${datos.apiSdk})")
                        ItemMetrica("Parche de seguridad", datos.parcheSeguridad)
                        ItemMetrica("Compilación", datos.compilacion)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 4. Memoria y Almacenamiento
                GrupoAjustes(
                    etiqueta = "Memoria y almacenamiento local",
                    modifier = Modifier.bringIntoViewRequester(reqMemoria)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ItemMetrica("Memoria RAM", "${datos.ramLibreMb} MB libres de ${datos.ramTotalMb} MB (Estado crítico: ${if (datos.ramBaja) "Sí" else "No"})")
                        ItemMetrica("Heap JVM", "${datos.heapUsadoMb} MB en uso de ${datos.heapMaxMb} MB límite")
                        ItemMetrica("Almacenamiento interno", "${datos.almacenamientoLibreMb} MB libres de ${datos.almacenamientoTotalMb} MB")
                        ItemMetrica("Ruta de la bóveda", datos.rutaBoveda)
                        ItemMetrica("Tamaño de archivo", "${datos.tamanoBovedaBytes} bytes")
                        ItemMetrica("Integridad de escritura", "Atomic Rename + fsync() a prueba de fallos de energía")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 5. Biometría y Sensores
                GrupoAjustes(etiqueta = "Biometría y sensores") {
                    Column(modifier = Modifier.padding(16.dp)) {
                        datos.lineasBiometria.forEachIndexed { idx, linea ->
                            if (linea.ok != null) {
                                FilaAuditoria(
                                    ok = linea.ok,
                                    titulo = linea.texto,
                                    detalle = linea.detalle ?: if (linea.ok) "Verificado y soportado" else "No disponible",
                                    indentada = linea.indentada
                                )
                            } else {
                                Text(
                                    text = linea.texto,
                                    color = TextoPrincipal,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                            if (idx < datos.lineasBiometria.lastIndex) {
                                Spacer(Modifier.height(4.dp))
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 6. Acerca de Bóveda Local y Acciones
                GrupoAjustes(etiqueta = "Acerca de Bóveda Local") {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Bóveda Local",
                            color = TextoPrincipal,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Gestor de contraseñas, passkeys y datos sensibles 100% offline, zero-knowledge, sin nube ni telemetría.",
                            color = TextoSecundario,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 16.sp
                        )
                        Spacer(Modifier.height(12.dp))
                        SeparadorFilaSimple()
                        Spacer(Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            BotonColorido(
                                texto = "Copiar auditoría",
                                color = ColorSeguridad,
                                icono = Icons.Filled.ContentCopy,
                                modifier = Modifier.weight(1f)
                            ) {
                                haptica.toque()
                                val informe = InformeDiagnostico.generar(contexto, datos)
                                Portapapeles.copiar(contexto, "Informe de auditoría", informe)
                                vm.avisar("Informe copiado al portapapeles")
                            }

                            BotonColorido(
                                texto = "Ficha Android",
                                color = ColorAcento,
                                icono = Icons.Filled.Info,
                                modifier = Modifier.weight(1f)
                            ) {
                                haptica.toque()
                                if (!AjustesSistema.abrirFichaApp(contexto)) {
                                    vm.avisar("No se pudo abrir la ficha del sistema")
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun ItemMetrica(etiqueta: String, valor: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
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
            .padding(vertical = 3.dp)
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
