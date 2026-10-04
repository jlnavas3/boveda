package com.jlnavas3.bovedalocal.ui.pantallas.duplicados

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes

/**
 * Microcomponente para mostrar y alternar el código TOTP dentro de una fila de credencial duplicada.
 */
@Composable
fun FilaTotpDuplicada(
    entrada: Entrada,
    mostrarTotp: Boolean,
    alAlternarMostrarTotp: () -> Unit,
    seguridadVisualActiva: Boolean,
    estiloOcultamiento: String,
    modifier: Modifier = Modifier
) {
    if (entrada.secretoTotp.isNullOrBlank()) return

    val codigoTotp = remember(entrada.secretoTotp, entrada.totpDigitos, entrada.totpPeriodo, entrada.totpAlgoritmo) {
        try {
            val secretoBytes = Base32.decodificar(entrada.secretoTotp.replace(" ", "").uppercase())
            val ahora = System.currentTimeMillis() / 1000
            val periodo = entrada.totpPeriodo.toLong().coerceAtLeast(1L)
            val digitos = entrada.totpDigitos.coerceIn(6, 8)
            val algo = when (entrada.totpAlgoritmo.uppercase()) {
                "SHA256", "HMACSHA256" -> "HmacSHA256"
                "SHA512", "HMACSHA512" -> "HmacSHA512"
                else -> "HmacSHA1"
            }
            Totp.codigo(secretoBytes, ahora, digitos, periodo, algo)
        } catch (_: Exception) {
            "------"
        }
    }

    Spacer(Modifier.height(2.dp))
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val textoTotp = if (codigoTotp.length == 6) "${codigoTotp.take(3)} ${codigoTotp.drop(3)}" else codigoTotp
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Text(
                text = "TOTP: ",
                color = ColorAjusteGris,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
            )
            TextoSeguroVisual(
                texto = textoTotp,
                oculto = !mostrarTotp,
                estilo = if (seguridadVisualActiva) estiloOcultamiento else "puntos_fijos",
                estiloTexto = if (mostrarTotp) {
                    MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                } else {
                    MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp)
                },
                colorTexto = ColorTextoAjustes.copy(alpha = 0.9f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = alAlternarMostrarTotp,
            modifier = Modifier.size(22.dp)
        ) {
            Icon(
                imageVector = if (mostrarTotp) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                contentDescription = if (mostrarTotp) "Ocultar TOTP" else "Mostrar TOTP",
                tint = ColorAjusteGris,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}
