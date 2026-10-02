package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.IndicadorTotpTarta
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica
import kotlinx.coroutines.delay

@Composable
fun TarjetaTotpDetalle(
    entrada: Entrada,
    ajustes: AjustesApp,
    haptica: Haptica,
    alCopiarTotp: (String) -> Unit
) {
    val secreto = entrada.secretoTotp?.takeIf { it.isNotBlank() } ?: return
    val periodo = entrada.totpPeriodo.toLong().coerceAtLeast(10L)
    var ahora by remember { mutableLongStateOf(System.currentTimeMillis() / 1000) }

    LaunchedEffect(secreto) {
        while (true) {
            ahora = System.currentTimeMillis() / 1000
            delay(500)
        }
    }

    val codigo = remember(ahora / periodo, secreto, entrada.totpDigitos, entrada.totpAlgoritmo) {
        try {
            Totp.codigo(
                secreto = Base32.decodificar(secreto),
                segundosUnix = ahora,
                digitos = entrada.totpDigitos,
                periodo = periodo,
                algoritmo = entrada.totpAlgoritmo
            )
        } catch (e: Exception) {
            "------"
        }
    }

    val segundosRestantes = Totp.segundosRestantes(ahora, periodo)
    val codigoVisible = if (ajustes.totpSepararDigitos && codigo.length == 6) {
        "${codigo.take(3)} ${codigo.drop(3)}"
    } else {
        codigo
    }

    var copiado by remember { mutableStateOf(false) }
    LaunchedEffect(copiado) {
        if (copiado) {
            delay(1500)
            copiado = false
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        EtiquetaSeccionDetalle(texto = "Verificación en dos pasos")

        TarjetaDatoDetalle(
            colorBorde = ColorDatos2FA,
            alPulsar = {
                haptica.exito()
                copiado = true
                alCopiarTotp(codigo)
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 10.dp, top = 14.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "CÓDIGO TEMPORAL (TOTP)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color2FA
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = codigoVisible,
                            style = EstiloMonoGrande.copy(fontWeight = FontWeight.Bold, fontSize = 26.sp),
                            color = ColorTitulos
                        )
                        IndicadorTotpTarta(
                            segundosRestantes = segundosRestantes,
                            periodo = periodo,
                            tamano = 18.dp
                        )
                    }
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = "Toca para copiar · Se renueva en $segundosRestantes s",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }
                BotonCopiarDetalle(
                    copiado = copiado,
                    alPulsar = {
                        haptica.exito()
                        copiado = true
                        alCopiarTotp(codigo)
                    }
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Previews
// -------------------------------------------------------------------------------------------------

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun TarjetaTotpDetallePreview() {
    com.jlnavas3.bovedalocal.ui.preview.PreviewTemaBoveda {
        val contexto = androidx.compose.ui.platform.LocalContext.current
        TarjetaTotpDetalle(
            entrada = com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.entradaEjemplo,
            ajustes = com.jlnavas3.bovedalocal.ui.preview.PreviewMocks.ajustes,
            haptica = remember { Haptica(contexto) },
            alCopiarTotp = {}
        )
    }
}

