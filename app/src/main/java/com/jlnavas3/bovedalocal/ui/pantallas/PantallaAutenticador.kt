package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.EstadoBoveda
import com.jlnavas3.bovedalocal.ui.Pantalla
import com.jlnavas3.bovedalocal.ui.VaultViewModel
import com.jlnavas3.bovedalocal.ui.componentes.AnilloTotp
import com.jlnavas3.bovedalocal.ui.componentes.BotonAmbar
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaPepo
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import kotlinx.coroutines.delay

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.CabeceraPantalla
import com.jlnavas3.bovedalocal.ui.theme.Color2FA

@Composable
fun PantallaAutenticador(vm: VaultViewModel, estado: EstadoBoveda) {
    val ajustes by vm.ajustes.collectAsStateWithLifecycle()
    val entradas = (estado as? EstadoBoveda.Desbloqueada)?.entradas ?: emptyList()
    val conTotp = vm.entradasConTotp(entradas)

    var ahora by remember { mutableLongStateOf(System.currentTimeMillis() / 1000) }
    LaunchedEffect(Unit) {
        while (true) {
            ahora = System.currentTimeMillis() / 1000
            delay(500)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        CabeceraPantalla(
            titulo = "Autenticador 2FA",
            subtitulo = "Códigos de doble factor calculados en el dispositivo",
            alVolver = { vm.volverAtras() }
        )

        BotonColorido(
            texto = "Escanear código QR",
            color = Color2FA,
            icono = Icons.Filled.QrCodeScanner
        ) { vm.ir(Pantalla.Escaner()) }
        Spacer(Modifier.height(10.dp))
        BotonBorde(
            texto = "Escribir clave a mano",
            icono = Icons.Filled.Edit
        ) { vm.ir(Pantalla.Escaner(soloManual = true)) }
        Spacer(Modifier.height(18.dp))

        if (conTotp.isEmpty()) {
            TarjetaPepo {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Timer,
                        contentDescription = null,
                        tint = Color2FA,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Todavía no hay dobles factores",
                        color = ColorTitulos,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Cuando una web te ofrezca activar el 2FA, escanea su QR o pega su clave. Aquí verás el código de 6 dígitos actualizado cada 30 segundos.",
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            conTotp.forEach { entrada ->
                val secreto = entrada.secretoTotp ?: return@forEach
                val periodo = entrada.totpPeriodo.toLong().coerceAtLeast(10L)
                val codigo = remember(ahora / periodo, secreto, entrada.totpDigitos) {
                    try {
                        Totp.codigo(
                            secreto = com.jlnavas3.bovedalocal.crypto.Base32.decodificar(secreto),
                            segundosUnix = ahora,
                            digitos = entrada.totpDigitos,
                            periodo = periodo
                        )
                    } catch (e: Exception) {
                        "------"
                    }
                }
                TarjetaPepo(alPulsar = { vm.copiar("Código 2FA", codigo, true) }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnilloTotp(
                            codigo = "",
                            segundosRestantes = Totp.segundosRestantes(ahora, periodo),
                            tamano = 54,
                            periodo = periodo
                        )
                        Spacer(Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                entrada.totpEmisor.ifBlank { entrada.titulo },
                                color = TextoSecundario,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                if (ajustes.totpSepararDigitos && codigo.length == 6) {
                                    "${codigo.take(3)} ${codigo.drop(3)}"
                                } else codigo,
                                color = ColorTitulos,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            if (entrada.usuario.isNotBlank()) {
                                Text(entrada.usuario, color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                            }
                            Text("Toca para copiar", color = TextoSecundario, style = MaterialTheme.typography.bodySmall)
                        }
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = null,
                            tint = TextoSecundario.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}
