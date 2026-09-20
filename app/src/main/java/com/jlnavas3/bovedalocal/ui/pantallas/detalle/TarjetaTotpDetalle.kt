package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.GrupoAjustes
import com.jlnavas3.bovedalocal.ui.theme.Color2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.Menta
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

    GrupoAjustes(etiqueta = "Código de verificación (2FA)") {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    haptica.exito()
                    copiado = true
                    alCopiarTotp(codigo)
                }
                .padding(horizontal = 16.dp, vertical = 14.dp),
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
            IconButton(onClick = {
                haptica.exito()
                copiado = true
                alCopiarTotp(codigo)
            }) {
                AnimatedVisibility(
                    visible = copiado,
                    enter = scaleIn(spring(dampingRatio = 0.5f)),
                    exit = scaleOut(spring(dampingRatio = 0.6f))
                ) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = "Copiado",
                        tint = Menta,
                        modifier = Modifier.size(24.dp)
                    )
                }
                AnimatedVisibility(
                    visible = !copiado,
                    enter = scaleIn(spring(dampingRatio = 0.5f)),
                    exit = scaleOut(spring(dampingRatio = 0.6f))
                ) {
                    Icon(
                        Icons.Filled.ContentCopy,
                        contentDescription = "Copiar código",
                        tint = ColorIconosInternos,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
