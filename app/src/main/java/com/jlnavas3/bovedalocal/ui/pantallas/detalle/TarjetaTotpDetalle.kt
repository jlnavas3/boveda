package com.jlnavas3.bovedalocal.ui.pantallas.detalle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
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
import com.jlnavas3.bovedalocal.crypto.Base32
import com.jlnavas3.bovedalocal.crypto.Totp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.ui.componentes.AnilloTotp
import com.jlnavas3.bovedalocal.ui.componentes.BotonBorde
import com.jlnavas3.bovedalocal.ui.componentes.EtiquetaSeccion
import com.jlnavas3.bovedalocal.ui.componentes.TarjetaBoveda
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

    TarjetaBoveda {
        EtiquetaSeccion("Código de verificación (TOTP)")
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            AnilloTotp(
                codigo = if (ajustes.totpSepararDigitos && codigo.length == 6) "${codigo.take(3)} ${codigo.drop(3)}" else codigo,
                segundosRestantes = Totp.segundosRestantes(ahora, periodo),
                periodo = periodo
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Se renueva cada ${periodo} s · ${entrada.totpDigitos} dígitos",
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                BotonBorde(
                    texto = "Copiar código",
                    icono = Icons.Filled.ContentCopy,
                    color = Menta
                ) {
                    haptica.toque()
                    alCopiarTotp(codigo)
                }
            }
        }
    }
}
