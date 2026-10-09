package com.jlnavas3.bovedalocal.ui.pantallas.escaner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.BotonColorido
import com.jlnavas3.bovedalocal.ui.componentes.DialogoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual
import com.jlnavas3.bovedalocal.ui.theme.FormaCampo
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Diálogo modal para confirmar la importación de una entrada detectada por código QR:
 * contacto (vCard), red Wi-Fi o transferencia estructurada de Bóveda Local.
 */
@Composable
fun DialogoConfirmarImportacionQr(
    entrada: Entrada,
    alConfirmar: (Entrada) -> Unit,
    alDescartar: () -> Unit
) {
    val tituloModal = when (entrada.tipo) {
        TipoEntrada.CONTACTO -> "Contacto detectado"
        TipoEntrada.WIFI -> "Red Wi-Fi detectada"
        else -> "Credencial detectada"
    }

    val icono = when (entrada.tipo) {
        TipoEntrada.CONTACTO -> Icons.Filled.Person
        TipoEntrada.WIFI -> Icons.Filled.Wifi
        else -> Icons.Filled.VpnKey
    }

    val subtitulo = entrada.usuario.ifBlank {
        if (entrada.tipo == TipoEntrada.WIFI) "Configuración Wi-Fi protegida" else entrada.tipo.etiqueta
    }

    DialogoBoveda(
        abierto = true,
        alCerrar = alDescartar,
        titulo = tituloModal,
        botonConfirmar = {
            BotonColorido(
                texto = "Guardar",
                icono = Icons.Filled.Download,
                color = ColorAcento,
                alPulsar = { alConfirmar(entrada) }
            )
        },
        botonDescartar = {
            TextButton(onClick = alDescartar) {
                Text("Cancelar", color = TextoSecundario)
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(FormaCampo)
                    .background(ColorCampoAjustes)
                    .border(0.8.dp, ColorBordeActual, FormaCampo)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ColorAcento.copy(alpha = 0.15f))
                        .border(1.dp, ColorAcento.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = ColorAcento,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entrada.titulo.ifBlank { "Sin título" },
                        color = TextoPrincipal,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = subtitulo,
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Text(
                text = "¿Deseas agregar esta entrada de forma segura y cifrada a tu bóveda local?",
                color = TextoSecundario,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp)
            )
        }
    }
}
