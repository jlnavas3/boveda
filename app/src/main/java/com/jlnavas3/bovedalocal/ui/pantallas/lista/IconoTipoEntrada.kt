package com.jlnavas3.bovedalocal.ui.pantallas.lista

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jlnavas3.bovedalocal.data.Entrada
import com.jlnavas3.bovedalocal.data.TipoEntrada
import com.jlnavas3.bovedalocal.ui.componentes.Monograma
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorCampoAjustes
import com.jlnavas3.bovedalocal.ui.pantallas.ajustes.ColorSeparadorAjustes
import com.jlnavas3.bovedalocal.ui.theme.ColorAcento
import com.jlnavas3.bovedalocal.util.IconoAppCircular
import com.jlnavas3.bovedalocal.util.rememberIconoAppInstalada
import com.jlnavas3.bovedalocal.util.LanzadorEnlaces

/**
 * Avatar circular / Icono representativo a la izquierda de la fila según el tipo de entrada o monograma.
 */
@Composable
fun IconoTipoEntrada(
    entrada: Entrada,
    tamanoIcono: Int,
    modifier: Modifier = Modifier
) {
    val iconoApp = rememberIconoAppInstalada(entrada)

    Box(
        modifier = modifier.size(tamanoIcono.dp),
        contentAlignment = Alignment.Center
    ) {
        if (iconoApp != null) {
            IconoAppCircular(
                bitmap = iconoApp,
                descripcion = entrada.titulo,
                tamanoDp = tamanoIcono.dp
            )
        } else {
            when (entrada.tipo) {
                TipoEntrada.PASSKEY -> IconoTipoCircular(Icons.Filled.Fingerprint, tamanoIcono)
                TipoEntrada.NOTA -> IconoTipoCircular(Icons.Filled.Description, tamanoIcono)
                TipoEntrada.TARJETA -> IconoTipoCircular(Icons.Filled.CreditCard, tamanoIcono)
                TipoEntrada.WIFI -> IconoTipoCircular(Icons.Filled.Wifi, tamanoIcono)
                TipoEntrada.CUENTA_BANCARIA -> IconoTipoCircular(Icons.Filled.AccountBalance, tamanoIcono)
                TipoEntrada.SERVIDOR -> IconoTipoCircular(Icons.Filled.Dns, tamanoIcono)
                TipoEntrada.WALLET -> IconoTipoCircular(Icons.Filled.AccountBalanceWallet, tamanoIcono)
                TipoEntrada.IDENTIDAD -> IconoTipoCircular(Icons.Filled.Badge, tamanoIcono)
                TipoEntrada.CONTACTO -> IconoTipoCircular(Icons.Filled.Person, tamanoIcono)
                else -> {
                    Monograma(
                        titulo = entrada.titulo.ifBlank { "?" },
                        semilla = entrada.urls.firstOrNull() ?: entrada.usuario.ifBlank { entrada.titulo },
                        tamano = tamanoIcono
                    )
                }
            }
        }
    }
}

@Composable
private fun IconoTipoCircular(
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    tamanoIcono: Int
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(ColorCampoAjustes)
            .border(0.8.dp, ColorSeparadorAjustes, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = ColorAcento,
            modifier = Modifier.size((tamanoIcono * 0.52f).dp)
        )
    }
}
