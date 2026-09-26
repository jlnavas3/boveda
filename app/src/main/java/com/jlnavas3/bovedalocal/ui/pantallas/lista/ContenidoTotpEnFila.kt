package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.ui.componentes.IndicadorTotpTarta
import com.jlnavas3.bovedalocal.ui.theme.ColorTitulos
import com.jlnavas3.bovedalocal.ui.theme.EstiloMono

@Composable
fun ContenidoTotpEnFila(
    secreto: String,
    segundosUnix: Long,
    periodo: Long,
    digitos: Int,
    algoritmo: String,
    separarDigitosTotp: Boolean,
    compacta: Boolean,
    alCopiarCodigo: (String) -> Unit
) {
    val codigo = remember(segundosUnix / periodo, secreto, digitos, algoritmo) {
        if (secreto.isBlank()) ""
        else {
            try {
                Totp.codigo(
                    secreto = Base32.decodificar(secreto),
                    segundosUnix = segundosUnix,
                    digitos = digitos,
                    periodo = periodo,
                    algoritmo = algoritmo
                )
            } catch (e: Exception) {
                "------"
            }
        }
    }

    if (codigo.isNotBlank()) {
        val codigoVisible = if (separarDigitosTotp && codigo.length == 6) {
            "${codigo.take(3)} ${codigo.drop(3)}"
        } else {
            codigo
        }
        Spacer(Modifier.width(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { alCopiarCodigo(codigo) }
        ) {
            Text(
                text = codigoVisible,
                style = if (compacta) {
                    EstiloMono.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp)
                } else {
                    EstiloMono.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp)
                },
                color = ColorTitulos
            )
            Spacer(Modifier.width(5.dp))
            IndicadorTotpTarta(
                segundosRestantes = Totp.segundosRestantes(segundosUnix, periodo),
                periodo = periodo,
                tamano = if (compacta) 11.dp else 12.dp,
                colorPersonalizado = Color(0xFF9E9E9E)
            )
        }
    }
}
