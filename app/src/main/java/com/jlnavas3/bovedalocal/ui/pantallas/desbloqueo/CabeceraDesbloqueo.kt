/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.ui.pantallas.desbloqueo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.AjustesApp
import com.jlnavas3.bovedalocal.data.obtenerPuertaColor
import com.jlnavas3.bovedalocal.data.obtenerPuertaColorDestello
import com.jlnavas3.bovedalocal.ui.componentes.ContenedorIconoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.EstiloTitulo
import com.jlnavas3.bovedalocal.ui.componentes.PuertaBoveda
import com.jlnavas3.bovedalocal.ui.componentes.TamanoInsignia
import com.jlnavas3.bovedalocal.ui.componentes.TextoSubtitulo
import com.jlnavas3.bovedalocal.ui.componentes.TextoTitulo
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorTarjetaAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.ui.theme.esOscuroActivo
import com.jlnavas3.bovedalocal.ui.theme.parsearColorO

@Composable
fun CabeceraDesbloqueo(
    abriendo: Boolean,
    tipoAnimacion: String,
    modifier: Modifier = Modifier,
    ajustes: AjustesApp? = null
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (tipoAnimacion) {
            "engranajes" -> {
                Spacer(Modifier.height(250.dp))
            }
            "puerta" -> {
                val hexPuerta = ajustes?.obtenerPuertaColor(esOscuroActivo).orEmpty()
                val colorPuerta = if (hexPuerta.isNotBlank()) parsearColorO(hexPuerta, ColorAcento) else null
                val hexDestello = ajustes?.obtenerPuertaColorDestello(esOscuroActivo).orEmpty()
                val colorDestello = if (hexDestello.isNotBlank()) parsearColorO(hexDestello, ColorAcento) else null

                PuertaBoveda(
                    abierta = abriendo,
                    tamano = 180,
                    velocidadFactor = ajustes?.puertaVelocidad ?: 1.0f,
                    grosorFactor = ajustes?.puertaGrosorAnillos ?: 1.0f,
                    colorPersonalizado = colorPuerta,
                    colorDestello = colorDestello
                )
            }
            else -> {
                // Modo estático "ninguna": 0% uso de GPU/CPU, sin ciclos de animación continua
                ContenedorIconoInsignia(
                    icono = if (abriendo) Icons.Filled.LockOpen else Icons.Filled.Lock,
                    tamano = TamanoInsignia.HERO,
                    colorFondo = ColorTarjetaAjustes,
                    colorIcono = ColorAcento,
                    conBorde = true,
                    descripcion = "Bóveda cifrada"
                )
            }
        }

        Spacer(Modifier.height(18.dp))
        TextoTitulo(
            texto = "Bóveda cerrada",
            estilo = EstiloTitulo.GRANDE,
            alineacion = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        TextoSubtitulo(
            texto = "Todo sigue cifrado en este dispositivo.",
            alineacion = TextAlign.Center
        )

        if (ajustes?.mostrarAvisosEnPantallaBloqueo == true && (ajustes.conteoAvisosEnBloqueo) > 0) {
            val conteo = ajustes.conteoAvisosEnBloqueo
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ColorAcento.copy(alpha = 0.12f))
                    .padding(horizontal = 12.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = null,
                    tint = ColorAcento,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "$conteo ${if (conteo == 1) "aviso pendiente" else "avisos pendientes"}",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = ColorAcento
                )
            }
        }
    }
}
