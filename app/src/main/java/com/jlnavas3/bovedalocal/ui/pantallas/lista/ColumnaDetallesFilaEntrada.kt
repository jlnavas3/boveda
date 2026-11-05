/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.ColorFavorito
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
    segundosUnix: Long? = null,
    separarDigitosTotp: Boolean,
    alCopiarCodigo: (String) -> Unit,
    ocultarUsuario: Boolean,
    ocultarTotp: Boolean,
    estiloOcultamiento: String,
    seguridadVisualActiva: Boolean = false,
    identidadAsociada: com.jlnavas3.bovedalocal.data.Identidad? = null,
    ocultarEmailIdentidad: Boolean = false,
    mostrarChipIdentidad: Boolean = true,
    mostrarFavorito: Boolean = false,
    alFavorito: (() -> Unit)? = null,
    mostrarIndicadores: Boolean = true,
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
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tituloMostrar,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (resaltado) ColorAcento else TextoPrincipal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (tieneTotp && secreto != null) {
                Spacer(Modifier.width(8.dp))
                if (segundosUnix != null) {
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
                } else {
                    ContenidoTotpEnFila(
                        secreto = secreto,
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

            if (mostrarFavorito && alFavorito != null) {
                Spacer(Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(if (compacta) 22.dp else 24.dp)
                        .clip(CircleShape)
                        .clickable(onClick = alFavorito),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Favorito",
                        tint = ColorFavorito,
                        modifier = Modifier.size(if (compacta) 15.dp else 17.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(2.dp))

        val textoSubtitulo = com.jlnavas3.bovedalocal.util.ResolverSubtituloEntrada.resolver(
            entrada = entrada,
            identidadAsociada = identidadAsociada,
            ocultarEmailIdentidad = ocultarEmailIdentidad
        )
        val esDatoUsuario = entrada.usuario.isNotBlank() && (
            entrada.tipo == TipoEntrada.LOGIN ||
            entrada.tipo == TipoEntrada.PASSKEY ||
            entrada.tipo !in listOf(TipoEntrada.NOTA, TipoEntrada.WIFI)
        )
        val debeOcultarSubtitulo = ocultarUsuario && esDatoUsuario

        val usuarioNormalizado = entrada.usuario.trim()
        val coincideConCorreoIdentidad = identidadAsociada != null && usuarioNormalizado.isNotBlank() && (
            usuarioNormalizado.equals(identidadAsociada.correoPrincipal.trim(), ignoreCase = true) ||
            identidadAsociada.correosSecundarios.any { it.trim().equals(usuarioNormalizado, ignoreCase = true) }
        )

        val coincideConDocumentoIdentidad = identidadAsociada != null && (
            (usuarioNormalizado.isNotBlank() && !identidadAsociada.numeroDocumento.isNullOrBlank() &&
                com.jlnavas3.bovedalocal.util.esMismoDocumento(identidadAsociada.numeroDocumento, usuarioNormalizado)) ||
            (entrada.camposPersonalizados.any { cp ->
                cp.valor.isNotBlank() && !identidadAsociada.numeroDocumento.isNullOrBlank() &&
                    com.jlnavas3.bovedalocal.util.esMismoDocumento(identidadAsociada.numeroDocumento, cp.valor)
            })
        )

        val sustituyeTextoPorInsignia = (coincideConCorreoIdentidad || coincideConDocumentoIdentidad) && mostrarChipIdentidad

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (sustituyeTextoPorInsignia && identidadAsociada != null) {
                    com.jlnavas3.bovedalocal.ui.pantallas.identidades.InsigniaIdentidadEntrada(
                        identidad = identidadAsociada,
                        compacta = compacta,
                        esDocumento = coincideConDocumentoIdentidad,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                } else {
                    TextoSeguroVisual(
                        texto = textoSubtitulo,
                        oculto = debeOcultarSubtitulo,
                        estilo = estiloOcultamiento,
                        estiloTexto = MaterialTheme.typography.labelSmall,
                        colorTexto = TextoSecundario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    if (identidadAsociada != null && mostrarChipIdentidad) {
                        Spacer(Modifier.width(6.dp))
                        com.jlnavas3.bovedalocal.ui.pantallas.identidades.InsigniaIdentidadEntrada(
                            identidad = identidadAsociada,
                            compacta = compacta,
                            esDocumento = false
                        )
                    }
                }
            }

            if (mostrarIndicadores && (com.jlnavas3.bovedalocal.ui.theme.EstiloIndicadores == "puntos" || com.jlnavas3.bovedalocal.ui.theme.EstiloIndicadores == "iconos")) {
                Spacer(Modifier.width(8.dp))
                IndicadorContenidoTarjeta(
                    entrada = entrada,
                    estilo = com.jlnavas3.bovedalocal.ui.theme.EstiloIndicadores
                )
            }
        }
    }
}
