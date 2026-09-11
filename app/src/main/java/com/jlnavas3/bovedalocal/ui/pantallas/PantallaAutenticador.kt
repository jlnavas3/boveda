package com.jlnavas3.bovedalocal.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.icons.filled.QrCodeScanner
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
            texto = "Escanear QR con la cámara",
            color = Color2FA,
            icono = Icons.Filled.QrCodeScanner
        ) { vm.ir(Pantalla.Escaner()) }
        Spacer(Modifier.height(10.dp))
        BotonBorde("Escribir el código a mano") { vm.ir(Pantalla.Escaner(soloManual = true)) }
        Spacer(Modifier.height(18.dp))

        if (conTotp.isEmpty()) {
            TarjetaPepo {
                Text("Todavía no hay dobles factores", color = ColorTitulos, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Cuando una web te ofrezca activar el 2FA, escanea su QR o pega su clave. Aquí verás el código de 6 dígitos.",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // El anillo solo lleva la cuenta atras. Los 6 digitos van fuera,
                        // grandes: dentro del circulo no caben legibles.
                        AnilloTotp(
                            codigo = "",
                            segundosRestantes = Totp.segundosRestantes(ahora, periodo),
                            tamano = 56,
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
                                Text(entrada.usuario, color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
                            }
                            Text("Toca para copiar", color = TextoSecundario, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}
