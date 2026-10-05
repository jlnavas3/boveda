package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.TextoPrincipal
import com.jlnavas3.bovedalocal.ui.theme.TextoSecundario

/**
 * Microcomponente que renderiza el bloque central de información de una credencial:
 * Título, icono de exclusión en salud, widget dinámico TOTP y subtítulo sensible/ofuscable.
 */
@Composable
fun ColumnaDetallesFilaEntrada(
    entrada: Entrada,
    tituloMostrar: String,
    resaltado: Boolean,
    compacta: Boolean,
    tieneTotp: Boolean,
    secreto: String?,
    segundosUnix: Long,
    separarDigitosTotp: Boolean,
    alCopiarCodigo: (String) -> Unit,
    ocultarUsuario: Boolean,
    ocultarTotp: Boolean,
    estiloOcultamiento: String,
    identidadAsociada: com.jlnavas3.bovedalocal.data.Identidad? = null,
    ocultarEmailIdentidad: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tituloMostrar,
                style = if (compacta) {
                    MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                } else {
                    MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                },
                color = if (resaltado) ColorAcento else TextoPrincipal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )

            if (entrada.ignoradaEnSalud) {
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Filled.VisibilityOff,
                    contentDescription = "Ignorada en salud",
                    tint = TextoSecundario.copy(alpha = 0.55f),
                    modifier = Modifier.size(13.dp)
                )
            }

            if (tieneTotp && secreto != null) {
                ContenidoTotpEnFila(
                    secreto = secreto,
                    segundosUnix = segundosUnix,
                    periodo = entrada.totpPeriodo.toLong().coerceAtLeast(10L),
                    digitos = entrada.totpDigitos,
                    algoritmo = entrada.totpAlgoritmo,
                    separarDigitosTotp = separarDigitosTotp,
                    compacta = compacta,
                    alCopiarCodigo = alCopiarCodigo,
                    ocultarTotp = ocultarTotp,
                    estiloOcultamiento = estiloOcultamiento
                )
            }
        }

        Spacer(Modifier.height(2.dp))

        val textoSubtitulo = when (entrada.tipo) {
            TipoEntrada.PASSKEY -> "Passkey · ${entrada.passkey?.rpId ?: ""}"
            TipoEntrada.NOTA -> "Nota segura"
            TipoEntrada.LOGIN -> {
                if (ocultarEmailIdentidad && identidadAsociada != null) {
                    entrada.urls.firstOrNull() ?: entrada.notas.take(25).ifBlank { "Cuenta vinculada" }
                } else {
                    entrada.usuario.ifBlank { entrada.urls.firstOrNull() ?: "Sin usuario" }
                }
            }
            else -> entrada.usuario.ifBlank {
                entrada.camposPersonalizados.firstOrNull { it.valor.isNotBlank() }?.let {
                    "${it.etiqueta}: ${if (it.esSensibleEfectivo) "••••" else it.valor}"
                } ?: entrada.tipo.etiqueta
            }
        }
        val esDatoUsuario = entrada.usuario.isNotBlank() && (
            entrada.tipo == TipoEntrada.LOGIN ||
            entrada.tipo == TipoEntrada.PASSKEY ||
            entrada.tipo !in listOf(TipoEntrada.NOTA, TipoEntrada.WIFI)
        )
        val debeOcultarSubtitulo = ocultarUsuario && esDatoUsuario

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextoSeguroVisual(
                texto = textoSubtitulo,
                oculto = debeOcultarSubtitulo,
                estilo = estiloOcultamiento,
                estiloTexto = if (compacta) MaterialTheme.typography.labelSmall else MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                colorTexto = TextoSecundario,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )

            if (identidadAsociada != null) {
                Spacer(Modifier.width(6.dp))
                com.jlnavas3.bovedalocal.ui.pantallas.identidades.InsigniaIdentidadEntrada(
                    identidad = identidadAsociada
                )
            }
        }
    }
}
