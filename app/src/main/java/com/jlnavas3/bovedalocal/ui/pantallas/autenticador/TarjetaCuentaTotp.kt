package com.jlnavas3.bovedalocal.ui.pantallas.autenticador

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.IndicadorTotpTarta
import com.jlnavas3.bovedalocal.ui.theme.Ambar
import com.jlnavas3.bovedalocal.ui.theme.ColorDatos2FA
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMonoGrande
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario
import com.jlnavas3.bovedalocal.util.Haptica

@Composable
fun TarjetaCuentaTotp(
    entrada: Entrada,
    ahora: Long,
    separarDigitos: Boolean,
    haptica: Haptica,
    alCopiarCodigo: (String) -> Unit,
    alAlternarFavorito: () -> Unit
) {
    val secreto = entrada.secretoTotp ?: return
    val periodo = entrada.totpPeriodo.toLong().coerceAtLeast(10L)
    val segundosRestantes = Totp.segundosRestantes(ahora, periodo)
    val codigo = remember(ahora / periodo, secreto, entrada.totpDigitos) {
        try {
            Totp.codigo(
                secreto = Base32.decodificar(secreto),
                segundosUnix = ahora,
                digitos = entrada.totpDigitos,
                periodo = periodo
            )
        } catch (e: Exception) {
            "------"
        }
    }
    val codigoVisible = if (separarDigitos && codigo.length == 6) {
        "${codigo.take(3)} ${codigo.drop(3)}"
    } else {
        codigo
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
                .background(ColorDatos2FA)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    haptica.exito()
                    alCopiarCodigo(codigo)
                }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entrada.totpEmisor.ifBlank { entrada.titulo },
                    color = ColorTitulos,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (entrada.usuario.isNotBlank()) {
                    Text(
                        text = entrada.usuario,
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = codigoVisible,
                        color = ColorTitulos,
                        style = EstiloMonoGrande.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    )
                    IndicadorTotpTarta(
                        segundosRestantes = segundosRestantes,
                        periodo = periodo,
                        tamano = 18.dp
                    )
                }
                Spacer(Modifier.height(3.dp))
                Text(
                    text = "Toca para copiar · $segundosRestantes s restantes",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable {
                            haptica.tic()
                            alAlternarFavorito()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = if (entrada.favorito) "Quitar de favoritos" else "Marcar como favorito",
                        tint = if (entrada.favorito) Ambar else ColorIconosInternos.copy(alpha = 0.25f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = "Copiar código",
                    tint = ColorIconosInternos,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
