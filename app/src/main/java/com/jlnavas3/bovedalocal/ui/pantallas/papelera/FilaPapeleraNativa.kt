/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.papelera

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.seguridad.TextoSeguroVisual
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorAjusteGris
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTextoAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorIconosInternos
import com.jlnavas3.bovedalocal.ui.theme.Menta
import com.jlnavas3.bovedalocal.ui.theme.Peligro
import com.jlnavas3.bovedalocal.ui.componentes.AccionDeslizamiento
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorDeslizamientoBoveda
import com.jlnavas3.bovedalocal.ui.pantallas.lista.IndicadorContenidoTarjeta
import com.jlnavas3.bovedalocal.ui.theme.EstiloIndicadores
import com.jlnavas3.bovedalocal.ui.theme.FormaPequena
import com.jlnavas3.bovedalocal.ui.theme.colorLegibleParaTema
import com.jlnavas3.bovedalocal.ui.theme.fondoBadgeParaTema
import com.jlnavas3.bovedalocal.util.IconoAppCircular
import com.jlnavas3.bovedalocal.util.rememberIconoAppInstalada
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.RectangleShape
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.lista.AvatarSeleccionableEntrada
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.CurvaturaEsquinas
import com.jlnavas3.bovedalocal.ui.theme.GrosorBorde
import com.jlnavas3.bovedalocal.ui.theme.EstiloBorde
import com.jlnavas3.bovedalocal.ui.theme.ColorBordeActual

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilaPapeleraNativa(
    entrada: Entrada,
    diasRestantes: Long,
    alRestaurar: () -> Unit,
    alBorrarDefinitivo: () -> Unit,
    modifier: Modifier = Modifier,
    ajustes: AjustesApp? = null,
    mostrarIndicadores: Boolean = false,
    enGrupo: Boolean = true,
    seleccionado: Boolean = false,
    seleccionActiva: Boolean = false,
    alAlternarSeleccion: () -> Unit = {},
    alPulsarLargo: () -> Unit = {}
) {
    val icono = when (entrada.tipo) {
        TipoEntrada.LOGIN -> Icons.Filled.Lock
        TipoEntrada.PASSKEY -> Icons.Filled.Fingerprint
        TipoEntrada.NOTA -> Icons.Filled.Description
        TipoEntrada.TARJETA -> Icons.Filled.CreditCard
        TipoEntrada.WIFI -> Icons.Filled.Wifi
        TipoEntrada.CUENTA_BANCARIA -> Icons.Filled.AccountBalance
        TipoEntrada.IDENTIDAD -> Icons.Filled.Badge
        TipoEntrada.SERVIDOR -> Icons.Filled.Dns
        TipoEntrada.WALLET -> Icons.Filled.AccountBalanceWallet
        TipoEntrada.CONTACTO -> Icons.Filled.Person
    }

    val subtitulo = entrada.usuario.ifBlank {
        entrada.urls.firstOrNull() ?: entrada.tipo.etiqueta
    }

    val forma = if (enGrupo) RectangleShape else RoundedCornerShape(CurvaturaEsquinas)
    val fondo = when {
        seleccionado -> ColorAcento.copy(alpha = 0.12f)
        enGrupo -> ColorTarjetaAjustes
        else -> ColorTarjetaAjustes
    }

    ContenedorDeslizamientoBoveda(
        idItem = entrada.id,
        modifier = modifier,
        forma = forma,
        enGrupo = enGrupo,
        accionIzquierda = AccionDeslizamiento(
            texto = "Restaurar",
            icono = Icons.Filled.Restore,
            color = Menta,
            alEjecutar = alRestaurar
        ),
        accionDerecha = AccionDeslizamiento(
            texto = "Eliminar\nDefinitivo",
            icono = Icons.Filled.DeleteForever,
            color = Peligro,
            alEjecutar = alBorrarDefinitivo
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(forma)
                .background(fondo)
                .then(
                    if (seleccionado) {
                        Modifier.border(
                            width = (GrosorBorde.takeIf { it > 0.dp } ?: 1.dp),
                            color = ColorAcento,
                            shape = forma
                        )
                    } else if (!enGrupo && GrosorBorde > 0.dp && EstiloBorde != "ninguno") {
                        Modifier.border(GrosorBorde, ColorBordeActual, forma)
                    } else Modifier
                )
                .combinedClickable(
                    onClick = {
                        if (seleccionActiva) alAlternarSeleccion()
                    },
                    onLongClick = {
                        alPulsarLargo()
                    }
                )
        ) {
            if (mostrarIndicadores && (EstiloIndicadores == "lineas" || EstiloIndicadores == "lateral")) {
                val alineacionIndicador = when (EstiloIndicadores) {
                    "lateral" -> Alignment.CenterStart
                    else -> Alignment.TopCenter
                }
                IndicadorContenidoTarjeta(
                    entrada = entrada,
                    modifier = Modifier.align(alineacionIndicador)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarSeleccionableEntrada(
                    entrada = entrada,
                    tamanoIcono = 38,
                    seleccionado = seleccionado,
                    alAlternarSeleccion = alAlternarSeleccion
                )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entrada.titulo.ifBlank { "Sin título" },
                    color = ColorTextoAjustes,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val seguridadVisualActiva = ajustes?.seguridadVisualActiva == true
                val ocultarUsuario = seguridadVisualActiva && (ajustes.ocultarUsuario)
                val estiloOcultamiento = ajustes?.estiloOcultamientoVisual ?: "desenfoque"

                if (entrada.usuario.isNotBlank() && ocultarUsuario) {
                    TextoSeguroVisual(
                        texto = subtitulo,
                        oculto = true,
                        estilo = estiloOcultamiento,
                        estiloTexto = MaterialTheme.typography.bodySmall,
                        colorTexto = ColorAjusteGris,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Text(
                        text = subtitulo,
                        color = ColorAjusteGris,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val esPeligro = diasRestantes in 0..3
                        val colorTiempo = if (esPeligro) Peligro else ColorAjusteGris
                        val textoExpiracion = when {
                            diasRestantes < 0 -> "Conservada indefinidamente"
                            diasRestantes > 0 -> "Expira en $diasRestantes días"
                            else -> "Expira en cualquier momento"
                        }
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = null,
                            tint = colorTiempo,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = textoExpiracion,
                            color = colorTiempo,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (mostrarIndicadores && (EstiloIndicadores == "puntos" || EstiloIndicadores == "iconos")) {
                        Spacer(Modifier.width(8.dp))
                        IndicadorContenidoTarjeta(
                            entrada = entrada,
                            estilo = EstiloIndicadores
                        )
                    }
                }
            }
        }
        }
    }
}

@com.jlnavas3.bovedalocal.ui.preview.BovedaPreview
@Composable
private fun FilaPapeleraNativaPreview() {
    com.jlnavas3.bovedalocal.ui.theme.BovedaTheme {
        FilaPapeleraNativa(
            entrada = Entrada(
                id = "demo",
                titulo = "Google Workspace",
                usuario = "usuario@correo.com",
                contrasena = "secret123",
                eliminadaEn = System.currentTimeMillis()
            ),
            diasRestantes = 15,
            alRestaurar = {},
            alBorrarDefinitivo = {},
            mostrarIndicadores = true,
            enGrupo = false
        )
    }
}
